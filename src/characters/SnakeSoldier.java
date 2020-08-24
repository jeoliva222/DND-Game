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
import tiles.MovableType;

/**
 * Class that represents the Snake Soldier enemy
 * @author jeoliva
 */
public class SnakeSoldier extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = 1940891311386091518L;

	// Modifiers/Statistics
	
	private static int MAX_HP = 13;
	
	private static int MIN_DMG = 3;
	private static int MAX_DMG = 5;
	
	private static double CRIT_CHANCE = 0.15;
	private static double CRIT_MULT = 1.2;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_BITE = 3;
	private static final int STATE_ATT_BITE = 4;
	private static final int STATE_PREP_SWIPE = 5;
	private static final int STATE_ATT_SWIPE = 6;
	private static final int STATE_PREP_SLAM = 7;
	private static final int STATE_MID_SLAM = 8;
	private static final int STATE_ATT_SLAM = 9;
	
	//----------------------------
	// Additional Behavior
	
	// Direction the screen will be marked up for damage indicators
	private int xMarkDir = 0;
	private int yMarkDir = 0;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_SOLDIER);
	private static String ssImage_base = "snakesoldier";
	
	private static String ssImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_SOLDIER, "snakesoldier_dead.png");

	// Constructors
	public SnakeSoldier(int startX, int startY) {
		this(startX, startY, PatrolPattern.PATROL);
	}
	
	public SnakeSoldier(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeSoldier.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Snake Soldier";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + ssImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			return GPath.NULL;
		}
		
		switch (state) {
			case SnakeSoldier.STATE_IDLE:
			case SnakeSoldier.STATE_PURSUE:
				// No extra path
				break;
			case SnakeSoldier.STATE_ALERTED:
				statePath = "_ALERT";
				break;
			case SnakeSoldier.STATE_PREP_BITE:
				statePath = "_PREP_BITE";
				break;
			case SnakeSoldier.STATE_ATT_BITE:
				statePath = "_ATT_BITE";
				break;
			case SnakeSoldier.STATE_PREP_SWIPE:
				statePath = "_PREP_SWIPE";
				break;
			case SnakeSoldier.STATE_ATT_SWIPE:
				statePath = "_ATT_SWIPE";
				break;
			case SnakeSoldier.STATE_PREP_SLAM:
				statePath = "_PREP_SLAM";
				break;
			case SnakeSoldier.STATE_MID_SLAM:
				statePath = "_MID_SLAM";
				break;
			case SnakeSoldier.STATE_ATT_SLAM:
				statePath = "_ATT_SLAM";
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return ssImage_DEAD;
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_WATER)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"), getXPos(), getYPos());
		attackPlayer();
	}
	
	@Override
	public void onDeath() {
		SoundPlayer.playWAV(GPath.createSoundPath("Snake_Death.wav"), getXPos(), getYPos());
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
			case SnakeSoldier.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					Random r = new Random();
					int whichSound = r.nextInt(2);
					if (whichSound == 0) {
						SoundPlayer.playWAV(GPath.createSoundPath("snake1_greet1.wav"), getXPos(), getYPos());
					} else {
						SoundPlayer.playWAV(GPath.createSoundPath("snake1_warn1.wav"), getXPos(), getYPos());
					}
					this.state = SnakeSoldier.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case SnakeSoldier.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = SnakeSoldier.STATE_PURSUE;
				break;
			case SnakeSoldier.STATE_PURSUE:	
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
					
					// Chooses an attack
					chooseAttack();
					
					// Else, try to move x-wise or y-wise closer to the player
					// based on which dimension you are further distance from them
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
					
					// Decide if snake should attempt a ranged swipe/bite/charge prep
					// Punishes running away and eager approaches
					// Attempt only 1/2 of the time
					int shouldAttack = new Random().nextInt(2);
					if ((shouldAttack == 0) && (((Math.abs(distX) <= 2) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 2)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
						if (hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							
							// Chooses an attack
							chooseAttack();
						}
					}
				}
				break;
			case SnakeSoldier.STATE_PREP_BITE:
				// Step in marked direction if possible
				moveCharacter(xMarkDir, yMarkDir);
				
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
				
				// Play sound
				SoundPlayer.playWAV(GPath.createSoundPath("Snake_Bite.wav"), getXPos(), getYPos());
				
				// Attack if next to player
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir)) {
					playerInitiate();
				}
				
				this.state = SnakeSoldier.STATE_ATT_BITE;
				break;
			case SnakeSoldier.STATE_ATT_BITE:
				// Cooldown period for one turn
				this.state = SnakeSoldier.STATE_PURSUE;
				break;
			case SnakeSoldier.STATE_PREP_SWIPE:
				// Use direction from player to mark squares
				SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"), getXPos(), getYPos());
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
				
				// Change state
				this.state = SnakeSoldier.STATE_ATT_SWIPE;
				break;
			case SnakeSoldier.STATE_ATT_SWIPE:
				// Cooldown period for one turn
				this.state = SnakeSoldier.STATE_PURSUE;
				break;
			case SnakeSoldier.STATE_PREP_SLAM:
				// Attack if next to player. Otherwise, continue rushing in the current direction
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir)) {
					playerInitiate();
					
					// Play hit sound
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"), getXPos(), getYPos());
					
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
					
					// Change state to confirm that we hit
					this.state = SnakeSoldier.STATE_ATT_SLAM;
				} else {
					// Start to charge in player's direction
					
					// Try to move in the player's direction
					if (moveCharacter(xMarkDir, yMarkDir)) {
						// If successful, change state
						this.state = SnakeSoldier.STATE_MID_SLAM;
					} else {
						// If not successful, end the charge already
						
						// Mark tile with damage indicator
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
						
						this.state = SnakeSoldier.STATE_ATT_SLAM;
					}
				}
				
				break;
			case SnakeSoldier.STATE_MID_SLAM:
				// Attack if next to player. Otherwise, continue rushing in the current direction
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir)) {
					playerInitiate();
					
					// Play hit sound
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"), getXPos(), getYPos());
					
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
					
					// Change state to confirm that we hit
					this.state = SnakeSoldier.STATE_ATT_SLAM;
				} else {
					// Start to charge in player's direction
					
					// Try to move in the player's direction
					if (moveCharacter(xMarkDir, yMarkDir)) {
						// If successful continue onwards in this state
					} else {
						// If not successful, end the charge
						
						// Mark tile with damage indicator
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
						
						this.state = SnakeSoldier.STATE_ATT_SLAM;
					}
				}

				break;
			case SnakeSoldier.STATE_ATT_SLAM:
				// Cooldown period for one turn
				this.state = SnakeSoldier.STATE_PURSUE;
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}
	
	// Chooses the next attack for the Snake
	private void chooseAttack() {
		// Decide whether to swipe, charge, or bite
		int swipeBiteSlam = new Random().nextInt(10);
		if (swipeBiteSlam <= 2) {
			this.state = SnakeSoldier.STATE_PREP_BITE;
		} else if (swipeBiteSlam <= 6) {
			this.state = SnakeSoldier.STATE_PREP_SWIPE;
		} else {
			this.state = SnakeSoldier.STATE_PREP_SLAM;
		}
	}

}
