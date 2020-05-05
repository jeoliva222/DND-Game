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
import effects.FireEffect;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

/**
 * Class representing the Snake Commander enemy
 * @author jeoliva
 */
public class SnakeCommander extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -1866293970157816628L;

	// Modifiers/Statistics
	
	private static int MAX_HP = 13;
	
	private static int ARMOR_VAL = 1;
	
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
	private static final int STATE_PREP_FIRE = 10;
	private static final int STATE_ATT_FIRE = 11;
	
	//----------------------------
	// Additional Behavior
	
	// Direction the screen will be marked up for damage indicators
	private int xMarkDir = 0;
	private int yMarkDir = 0;
	
	private int attCount = 0;
	private int swipeMaxCount = 2;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_COMMANDER);
	private static String scImage_base = "snakecommander";
	
	private static String scImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_COMMANDER, "snakecommander_dead.png");

	// Constructors
	public SnakeCommander(int startX, int startY) {
		this(startX, startY, PatrolPattern.PATROL);
	}
	
	public SnakeCommander(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeCommander.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Snake Commander";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + scImage_base);
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
			case SnakeCommander.STATE_IDLE:
			case SnakeCommander.STATE_PURSUE:
				// No extra path
				break;
			case SnakeCommander.STATE_ALERTED:
				statePath = "_ALERT";
				break;
			case SnakeCommander.STATE_PREP_BITE:
				statePath = "_PREP_BITE";
				break;
			case SnakeCommander.STATE_ATT_BITE:
				statePath = "_ATT_BITE";
				break;
			case SnakeCommander.STATE_PREP_SWIPE:
				statePath = "_PREP_SWIPE";
				break;
			case SnakeCommander.STATE_ATT_SWIPE:
				if (attCount % 2 == 0) {
					statePath = "_ATT_SWIPE_ALT";
				} else {
					statePath = "_ATT_SWIPE";
				}
				break;
			case SnakeCommander.STATE_PREP_SLAM:
				statePath = "_PREP_SLAM";
				break;
			case SnakeCommander.STATE_MID_SLAM:
				statePath = "_MID_SLAM";
				break;
			case SnakeCommander.STATE_ATT_SLAM:
				statePath = "_ATT_SLAM";
				break;
			case SnakeCommander.STATE_PREP_FIRE:
				statePath = "_PREP_FIRE";
				break;
			case SnakeCommander.STATE_ATT_FIRE:
				if (attCount % 2 == 0) {
					statePath = "_ATT_FIRE";
				} else {
					statePath = "_ATT_FIRE_ALT";
				}
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return scImage_DEAD;
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_WATER)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		attackPlayer();
	}
	
	@Override
	public void onDeath() {
		SoundPlayer.playWAV(GPath.createSoundPath("Snake_Death.wav"));
	}
	
	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		this.attCount = 0;
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
			case SnakeCommander.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					Random r = new Random();
					int whichSound = r.nextInt(2);
					if (whichSound == 0) {
						SoundPlayer.playWAV(GPath.createSoundPath("snake1_greet1.wav"));
					} else {
						SoundPlayer.playWAV(GPath.createSoundPath("snake1_warn1.wav"));
					}
					this.state = SnakeCommander.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case SnakeCommander.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = SnakeCommander.STATE_PURSUE;
				break;
			case SnakeCommander.STATE_PURSUE:	
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

					// Choose a melee-based attack
					chooseMeleeAttack();
					
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
					
					// Decide if snake should attempt a ranged attack
					// Attempt only 1/2 of the time
					int shouldShoot = new Random().nextInt(2);
					if ((shouldShoot == 0) && (((Math.abs(distX) <= 4) && (Math.abs(distX) >= 2) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) >= 2) && (Math.abs(distY) <= 4)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
						if (hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							
							// Choose a ranged attack
							chooseRangedAttack();
						}
					}
					
					// Decide if snake should attempt a moving melee attack
					// Punishes running away and eager approaches
					// Attempt only 1/2 of the time
					int shouldMelee = new Random().nextInt(2);
					if ((shouldMelee == 0) && (((Math.abs(distX) <= 2) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 2)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
						if (hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							
							// Choose a melee-based attack
							chooseMeleeAttack();
						}
					}
				}
				break;
			case SnakeCommander.STATE_PREP_BITE:
				// Step in marked direction if possible
				moveCharacter(xMarkDir, yMarkDir);
				
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + (xMarkDir*2), yPos + (yMarkDir*2)));
				
				// Play sound
				SoundPlayer.playWAV(GPath.createSoundPath("Snake_Bite.wav"));
				
				// Attack if two tiles away from player (In a straight line)
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir) ||
						(plrX == xPos + (xMarkDir*2) && plrY == yPos + (yMarkDir*2))) {
					playerInitiate();
				}
				
				this.state = SnakeCommander.STATE_ATT_BITE;
				break;
			case SnakeCommander.STATE_ATT_BITE:
				// Cooldown period for one turn
				this.state = SnakeCommander.STATE_PURSUE;
				break;
			case SnakeCommander.STATE_PREP_SWIPE:
				// Use direction from player to mark squares
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
				
				// Change state and increment attack counter
				this.attCount += 1;
				this.state = SnakeCommander.STATE_ATT_SWIPE;
				break;
			case SnakeCommander.STATE_ATT_SWIPE:
				// Attack a specified number of times (swipeMaxCount)
				if (attCount >= swipeMaxCount) {
					// Reset attack counter
					this.attCount = 0;
					
					// Change state + Cooldown period
					this.state = SnakeCommander.STATE_PURSUE;
					break;
				} else {
					// Increment attack counter
					this.attCount += 1;
				}
				
				// Use direction from player to mark squares
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
				
				break;
			case SnakeCommander.STATE_PREP_SLAM:
				// Attack if next to player. Otherwise, continue rushing in the current direction
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir)) {
					playerInitiate();
					
					// Play hit sound
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
					
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
					
					// Change state to confirm that we hit
					this.state = SnakeCommander.STATE_ATT_SLAM;
				} else {
					// Start to charge in player's direction
					
					// Try to move in the player's direction
					if (moveCharacter(xMarkDir, yMarkDir)) {
						// If successful, change state
						this.state = SnakeCommander.STATE_MID_SLAM;
					} else {
						// If not successful, end the charge already
						
						// Mark tile with damage indicator
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
						
						this.state = SnakeCommander.STATE_ATT_SLAM;
					}
				}
				
				break;
			case SnakeCommander.STATE_MID_SLAM:
				// Try to turn to hit the player if they're next to us, but only do this once
				
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
				
				// Try to change direction to chase player, but only once
				if (attCount == 0 && (xMarkDir != dx || yMarkDir != dy) && (dx == 0 || dy == 0)) {
					this.attCount += 1;
					this.xMarkDir = dx;
					this.yMarkDir = dy;
				}
				
				// Attack if next to player. Otherwise, continue rushing in the current direction
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir)) {
					playerInitiate();
					
					// Play hit sound
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
					
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
					
					// Reset attack counter and change state to confirm that we hit
					this.attCount = 0;
					this.state = SnakeCommander.STATE_ATT_SLAM;
				} else {
					// Keep charging in player's direction
					
					// Try to move in the player's direction
					if (moveCharacter(xMarkDir, yMarkDir)) {
						// If successful continue onwards in this state
					} else {
						// If not successful, end the charge
						
						// Mark tile with damage indicator
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
						
						// Reset attack counter and switch states
						this.attCount = 0;
						this.state = SnakeCommander.STATE_ATT_SLAM;
					}
				}

				break;
			case SnakeCommander.STATE_ATT_SLAM:
				// Cooldown period for one turn
				this.state = SnakeCommander.STATE_PURSUE;
				break;
			case SnakeCommander.STATE_PREP_FIRE:
				// Mark tile(s) with damage indicators
				EntityManager.getInstance().getEffectManager().addEffect(new FireEffect(xPos + xMarkDir, yPos + yMarkDir, 3));
				
				// Check if player is in damaging spot(s)
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir)) {
					playerInitiate();
				}
				
				// Change states
				this.state = SnakeCommander.STATE_ATT_FIRE;
				break;
			case SnakeCommander.STATE_ATT_FIRE:
				// Fetch reference to EntityManager
				EntityManager em = EntityManager.getInstance();
				
				if (attCount == 0) {
					// Mark tile(s) with damage indicators
					em.getEffectManager().addEffect(new FireEffect(xPos + (xMarkDir*2), yPos + (yMarkDir*2), 2));
					em.getEffectManager().addEffect(new FireEffect(xPos + (xMarkDir*2) + Math.abs(yMarkDir), yPos + (yMarkDir*2) + Math.abs(xMarkDir), 2));
					em.getEffectManager().addEffect(new FireEffect(xPos + (xMarkDir*2) - Math.abs(yMarkDir), yPos + (yMarkDir*2) - Math.abs(xMarkDir), 2));
					
					// Check if player is in damaging spot(s)
					if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir) ||
							(plrX == xPos + (xMarkDir*2) && plrY == yPos + (yMarkDir*2)) ||
							(plrX == xPos + (xMarkDir*2) + Math.abs(yMarkDir) && plrY == yPos + (yMarkDir*2) + Math.abs(xMarkDir)) ||
							(plrX == xPos + (xMarkDir*2) - Math.abs(yMarkDir) && plrY == yPos + (yMarkDir*2) - Math.abs(xMarkDir))) {
						playerInitiate();
					}
				} else if (attCount == 1) {
					// Mark tile(s) with damage indicators
					em.getEffectManager().addEffect(new FireEffect(xPos + (xMarkDir*3), yPos + (yMarkDir*3), 1));
					em.getEffectManager().addEffect(new FireEffect(xPos + (xMarkDir*3) + Math.abs(yMarkDir), yPos + (yMarkDir*3) + Math.abs(xMarkDir), 1));
					em.getEffectManager().addEffect(new FireEffect(xPos + (xMarkDir*3) - Math.abs(yMarkDir), yPos + (yMarkDir*3) - Math.abs(xMarkDir), 1));
					em.getEffectManager().addEffect(new FireEffect(xPos + (xMarkDir*3) + (Math.abs(yMarkDir)*2), yPos + (yMarkDir*3) + (Math.abs(xMarkDir)*2), 1));
					em.getEffectManager().addEffect(new FireEffect(xPos + (xMarkDir*3) - (Math.abs(yMarkDir)*2), yPos + (yMarkDir*3) - (Math.abs(xMarkDir)*2), 1));
					
					// Check if player is in damaging spot(s)
					if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir) ||
							(plrX == xPos + (xMarkDir*2) && plrY == yPos + (yMarkDir*2)) ||
							(plrX == xPos + (xMarkDir*2) + Math.abs(yMarkDir) && plrY == yPos + (yMarkDir*2) + Math.abs(xMarkDir)) ||
							(plrX == xPos + (xMarkDir*2) - Math.abs(yMarkDir) && plrY == yPos + (yMarkDir*2) - Math.abs(xMarkDir)) ||
							(plrX == xPos + (xMarkDir*3) && plrY == yPos + (yMarkDir*3)) ||
							(plrX == xPos + (xMarkDir*3) + Math.abs(yMarkDir) && plrY == yPos + (yMarkDir*3) + Math.abs(xMarkDir)) ||
							(plrX == xPos + (xMarkDir*3) - Math.abs(yMarkDir) && plrY == yPos + (yMarkDir*3) - Math.abs(xMarkDir)) ||
							(plrX == xPos + (xMarkDir*3) + (Math.abs(yMarkDir)*2) && plrY == yPos + (yMarkDir*3) + (Math.abs(xMarkDir)*2)) ||
							(plrX == xPos + (xMarkDir*3) - (Math.abs(yMarkDir)*2) && plrY == yPos + (yMarkDir*3) - (Math.abs(xMarkDir)*2))) {
						playerInitiate();
					}
				} else {
					// Reset attack counter and change states
					this.attCount = 0;
					this.state = SnakeCommander.STATE_PURSUE;
					break;
				}

				// Increment attack counter
				this.attCount += 1;
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}
	
	// Chooses the next melee attack for the Snake
	private void chooseMeleeAttack() {
		// Decide whether to swipe, charge, or bite
		int swipeBiteSlam = new Random().nextInt(10);
		if (swipeBiteSlam <= 2) {
			this.state = SnakeCommander.STATE_PREP_BITE;
		} else if (swipeBiteSlam <= 6) {
			this.state = SnakeCommander.STATE_PREP_SWIPE;
		} else {
			this.state = SnakeCommander.STATE_PREP_SLAM;
		}
	}
	
	// Chooses the next ranged attack for the Snake
	private void chooseRangedAttack() {
		this.state = SnakeCommander.STATE_PREP_FIRE;
	}
	
}
