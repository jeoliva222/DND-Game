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
 * Class that represents the Bunny Warrior enemy in-game
 * @author jeoliva
 */
public class BunnyWarrior extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 7502969959532923677L;
	
	// Modifiers/Statistics

	private static int MAX_HP = 8;
	
	private static int MIN_DMG = 3;
	private static int MAX_DMG = 4;
	
	private static double CRIT_CHANCE = 0.15;
	private static double CRIT_MULT = 1.25;
	
	//----------------------------
	
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
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BWARRIOR);
	private static String bwImage_base = "bunnywarrior";
	//--
	private static String bwImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BWARRIOR, bwImage_base+"_dead.png");

	// Constructor
	public BunnyWarrior(int startX, int startY) {
		this(startX, startY, PatrolPattern.PATROL);
	}
	
	public BunnyWarrior(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = BunnyWarrior.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Bunny Warrior";
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
			case BunnyWarrior.STATE_IDLE:
			case BunnyWarrior.STATE_PURSUE:
				// No extra path
				break;
			case BunnyWarrior.STATE_PREP_STAB:
				statePath = "_PREP_STAB";
				break;
			case BunnyWarrior.STATE_ALERTED:
			case BunnyWarrior.STATE_ATT_STAB:
				statePath = "_ALERT";
				break;
			case BunnyWarrior.STATE_PREP_SWIPE:
				statePath = "_PREP_SWING";
				break;
			case BunnyWarrior.STATE_ATT_SWIPE:
				statePath = "_ATT_SWING";
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
		// Randomly play one of two sounds
		int whichSound = new Random().nextInt(2);
		if (whichSound == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_DEATH.wav"), getXPos(), getYPos());
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_DEATH2.wav"), getXPos(), getYPos());
		}
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
			case BunnyWarrior.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					Random r = new Random();
					int whichSound = r.nextInt(2);
					if (whichSound == 0) {
						SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_ALERT.wav"), getXPos(), getYPos());
					} else {
						SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_ALERT2.wav"), getXPos(), getYPos());
					}
					this.state = BunnyWarrior.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case BunnyWarrior.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = BunnyWarrior.STATE_PURSUE;
				break;
			case BunnyWarrior.STATE_PURSUE:	
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
				
				Random r = new Random();
				// Change state to prepare a stab/swipe 100% of the time if next to player
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;
					
					// Decide whether to swipe or stab
					int swipeOrStab = r.nextInt(2);
					if (swipeOrStab == 0) {
						this.state = BunnyWarrior.STATE_PREP_STAB;
					} else {
						this.state = BunnyWarrior.STATE_PREP_SWIPE;
					}
					
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
					
					// Decide if bunny should attempt long-ranged stab prep
					// Punishes running away and eager approaches
					// Attempt only 1/4 of the time
					int shouldAttack = r.nextInt(4);
					if ((shouldAttack == 0) && (((Math.abs(distX) <= 3) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 3)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
						if (hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							this.state = BunnyWarrior.STATE_PREP_STAB;
						}
					}
				}
				break;
			case BunnyWarrior.STATE_PREP_STAB:
				// Mark tiles with damage indicators
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + (xMarkDir*2), yPos + (yMarkDir*2)));
				
				// Play sound
				SoundPlayer.playWAV(GPath.createSoundPath("whip_ATT.wav"), getXPos(), getYPos());
				
				// Attack if next to player
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir) ||
						(plrX == xPos + (xMarkDir*2) && plrY == yPos + (yMarkDir*2))) {
					playerInitiate();
				}
				
				this.state = BunnyWarrior.STATE_ATT_STAB;
				break;
			case BunnyWarrior.STATE_ATT_STAB:
				// Cooldown period for one turn
				this.state = BunnyWarrior.STATE_PURSUE;
				break;
			case BunnyWarrior.STATE_PREP_SWIPE:
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
				this.state = BunnyWarrior.STATE_ATT_SWIPE;
				break;
			case BunnyWarrior.STATE_ATT_SWIPE:
				// Cooldown period for one turn
				this.state = BunnyWarrior.STATE_PURSUE;
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}
			
	}
}
