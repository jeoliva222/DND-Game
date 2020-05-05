package characters;

import java.awt.Dimension;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import effects.DamageIndicator;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

/**
 * Class representing Elite Bunny Warrior enemy
 * @author jeoliva
 */
public class EliteBunnyWarrior extends BunnyWarrior {
	
	// Serialization ID
	private static final long serialVersionUID = 9114187632747458419L;
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_STAB = 3;
	private static final int STATE_ATT_STAB = 4;
	private static final int STATE_PREP_SWIPE = 5;
	private static final int STATE_ATT_SWIPE = 6;
	
	//----------------------------
	// Additional Behavior
	
	// Direction the screen will be marked up for damage indicators
	private int xMarkDir = 0;
	private int yMarkDir = 0;
	
	// Dictates counter data for the slicing combo 
	private int comboCount = 0;
	private final int comboMax = 3;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BWARRIOR);
	private static String bwImage_base = "elite_bunnywarrior";
	
	private static String bwImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BWARRIOR, bwImage_base+"_dead.png");


	public EliteBunnyWarrior(int startX, int startY) {
		super(startX, startY);
	}
	
	public EliteBunnyWarrior(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY, patpat);
	}
	
	public String getName() {
		return "Elite Bunny Warrior";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + bwImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return GPath.NULL;
		}
		
		switch (state) {
			case EliteBunnyWarrior.STATE_IDLE:
			case EliteBunnyWarrior.STATE_PURSUE:
				// No extra path
				break;
			case EliteBunnyWarrior.STATE_PREP_STAB:
				statePath = "_PREP_STAB";
				break;
			case EliteBunnyWarrior.STATE_ALERTED:
			case EliteBunnyWarrior.STATE_ATT_STAB:
				statePath = "_ALERT";
				break;
			case EliteBunnyWarrior.STATE_PREP_SWIPE:
				statePath = "_PREP_SWING";
				break;
			case EliteBunnyWarrior.STATE_ATT_SWIPE:
				if ((comboCount % 2) == 1) {
					statePath = "_ATT_SWING";
				} else {
					statePath = "_ATT_SWING_COMBO";
				}
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return bwImage_DEAD;
	}
	
	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		this.comboCount = 0;
	}

	@Override
	public void takeTurn() {
		// Fetch reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if (!isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = (plrX - xPos);
		int distY = (plrY - yPos);
		
		switch (state) {
			case EliteBunnyWarrior.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					Random r = new Random();
					int whichSound = r.nextInt(2);
					if (whichSound == 0) {
						SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_ALERT.wav"));
					} else {
						SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_ALERT2.wav"));
					}
					this.state = EliteBunnyWarrior.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case EliteBunnyWarrior.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = EliteBunnyWarrior.STATE_PURSUE;
				break;
			case EliteBunnyWarrior.STATE_PURSUE:	
				// Relative movement direction (Initialize at 0)
				int dx = 0;
				int dy = 0;
				
				// Calculate relative movement directions
				// X-movement
				if (distX > 0) {
					dx = 1;
				} else if (distX < 0) {
					dx = -1;
				}
				// Y-movement
				if (distY > 0) {
					dy = 1;
				} else if (distY < 0) {
					dy = -1;
				}
								
				// Change state to prepare a stab/swipe 100% of the time if next to player
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;
					
					// Decide whether to swipe or stab
					Random r = new Random();
					int swipeOrStab = r.nextInt(2);
					if (swipeOrStab == 0) {
						this.state = EliteBunnyWarrior.STATE_PREP_STAB;
					} else {
						this.state = EliteBunnyWarrior.STATE_PREP_SWIPE;
					}
					
				} else {
					// Path-find to the player if we can
					Dimension nextStep = PathFinder.findPath(xPos, yPos, plrX, plrY, this);
					if (nextStep == null) {
						// Blindly pursue the target
						DumbFollow.blindPursue(distX, distY, dx, dy, this);
					} else {
						int changeX = (nextStep.width - xPos);
						int changeY = (nextStep.height - yPos);
						moveCharacter(changeX, changeY);
					}
					
					// Recalculate relative location to player
					distX = (plrX - xPos);
					distY = (plrY - yPos);
					
					// Relative movement direction (Initialize at 0)
					dx = 0;
					dy = 0;
					
					// Recalculate relative movement directions
					// X-movement
					if (distX > 0) {
						dx = 1;
					} else if (distX < 0) {
						dx = -1;
					}
					// Y-movement
					if (distY > 0) {
						dy = 1;
					} else if (distY < 0) {
						dy = -1;
					}
					
					// Decide if bunny should attempt long-ranged stab prep
					// Punishes running away and eager approaches
					// Attempt only 1/3 of the time
					boolean hasAttLOS;
					boolean failedCalc = false;
					Random r = new Random();
					int shouldAttack = r.nextInt(6);
					if ((shouldAttack < 2) && (((Math.abs(distX) <= 4) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 4)))) {
						// Next, make sure there aren't any walls in the way
						hasAttLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
						if (hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							this.state = EliteBunnyWarrior.STATE_PREP_STAB;
						} else {
							// Set the flag that we failed the LOS calculation
							failedCalc = true;
						}
					} 
					else {
						// If not doing running stab, try for a running swipe if we're close enough
						// Attempt only 1/3 of the time.
						// Don't attempt if we already failed the LOS check
						if ((!failedCalc) && (shouldAttack < 4) && (((Math.abs(distX) <= 2) && (Math.abs(distY) == 0)) ||
								((Math.abs(distX) == 0) && (Math.abs(distY) <= 2)))) {
							// Next, make sure there aren't any walls in the way
							hasAttLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
							
							if (hasAttLOS) {
								this.xMarkDir = dx;
								this.yMarkDir = dy;
								this.state = EliteBunnyWarrior.STATE_PREP_SWIPE;
							}
						}
					}
				}
				break;
			case EliteBunnyWarrior.STATE_PREP_STAB:
				// Mark tiles with damage indicators
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + (xMarkDir*2), yPos + (yMarkDir*2)));
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + (xMarkDir*3), yPos + (yMarkDir*3)));
				
				// Play sound
				SoundPlayer.playWAV(GPath.createSoundPath("whip_ATT.wav"));
				
				// Attack if next to player
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir) ||
						(plrX == xPos + (xMarkDir*2) && plrY == yPos + (yMarkDir*2)) ||
						(plrX == xPos + (xMarkDir*3) && plrY == yPos + (yMarkDir*3))) {
					playerInitiate();
				}
				
				this.state = EliteBunnyWarrior.STATE_ATT_STAB;
				break;
			case EliteBunnyWarrior.STATE_ATT_STAB:
				// Cooldown period for one turn
				this.state = EliteBunnyWarrior.STATE_PURSUE;
				break;
			case EliteBunnyWarrior.STATE_PREP_SWIPE:
				// Use direction from player to mark squares
				if (Math.abs(xMarkDir) > Math.abs(yMarkDir)) {
					// Player to left/right
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + 1));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos - 1));
					
					// Attack player if in affected space
					if ((plrX == xPos + xMarkDir) && (plrY == yPos || plrY == yPos - 1 || plrY == yPos + 1)) {
						playerInitiate();
					}
				} else {
					// Player above/below
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos, yPos + yMarkDir));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + 1, yPos + yMarkDir));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos - 1, yPos + yMarkDir));
					
					// Attack player if in affected space
					if ((plrY == yPos + yMarkDir) && (plrX == xPos || plrX == xPos - 1 || plrX == xPos + 1)) {
						playerInitiate();
					}
				}
				
				// Increment Combo counter
				this.comboCount += 1;
				
				// Play sound
				SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
				
				// Change state
				this.state = EliteBunnyWarrior.STATE_ATT_SWIPE;
				break;
			case EliteBunnyWarrior.STATE_ATT_SWIPE:
				if (comboCount < comboMax) {
					// Continue the combo
					int comboX = 0;
					int comboY = 0;
					
					// Recalculate relative direction to player
					// X-direction
					if (distX > 0) {
						comboX = 1;
					} else if (distX < 0) {
						comboX = -1;
					}
					// Y-direction
					if (distY > 0) {
						comboY = 1;
					} else if (distY < 0) {
						comboY = -1;
					}
					
					// If player is new relative direction from character, switch swinging directions
					if (((Math.abs(comboX) == 1) && (Math.abs(comboY) == 0)) ||
							((Math.abs(comboX) == 0) && (Math.abs(comboY) == 1))) {
						this.xMarkDir = comboX;
						this.yMarkDir = comboY;
					}
					
					// Play sound
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
					
					if (Math.abs(xMarkDir) > Math.abs(yMarkDir)) {
						// Player to left/right
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos));
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + 1));
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos - 1));
						
						// Attack player if in affected space
						if ((plrX == xPos + xMarkDir) && (plrY == yPos || plrY == yPos - 1 || plrY == yPos + 1)) {
							playerInitiate();
						}
					} else {
						// Player above/below
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos, yPos + yMarkDir));
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + 1, yPos + yMarkDir));
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos - 1, yPos + yMarkDir));
						
						// Attack player if in affected space
						if ((plrY == yPos + yMarkDir) && (plrX == xPos || plrX == xPos - 1 || plrX == xPos + 1)) {
							playerInitiate();
						}
					}
					
					// Increment Counter
					this.comboCount += 1;
				} else {
					// Once finished with the combo, reset Combo Counter
					this.comboCount = 0;
					
					// Cooldown period for one turn
					this.state = EliteBunnyWarrior.STATE_PURSUE;
				}
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}
	
}
