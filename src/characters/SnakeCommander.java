package characters;

import java.awt.Dimension;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import effects.DamageIndicator;
import effects.FireEffect;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

public class SnakeCommander extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -1866293970157816628L;

	// Modifiers/Statistics
	
	private int MAX_HP = 13;
	
	private int ARMOR_VAL = 1;
	
	private int MIN_DMG = 3;
	private int MAX_DMG = 5;
	
	private double CRIT_CHANCE = 0.15;
	private double CRIT_MULT = 1.2;
	
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
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_COMMANDER);
	private String scImage_base = "snakecommander";
	
	private String scImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BWARRIOR, "bunnywarrior_dead.png");

	// Constructors
	public SnakeCommander(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeCommander.STATE_IDLE;
		this.patrolPattern = PatrolPattern.PATROL;
		
		this.imagePath = this.getImage();
	}
	
	public SnakeCommander(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeCommander.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Snake Commander";
	}
	
	// TODO
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.scImage_base;
		String hpPath = "";
		String statePath = "";
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_full";
		} else {
			return GPath.NULL;
		}
		
		switch(this.state) {
		case SnakeCommander.STATE_IDLE:
		case SnakeCommander.STATE_PURSUE:
			// No extra path
			break;
		case SnakeCommander.STATE_PREP_BITE:
			statePath = "_ALERT";
			break;
		case SnakeCommander.STATE_ALERTED:
		case SnakeCommander.STATE_ATT_BITE:
			statePath = "_ALERT";
			break;
		case SnakeCommander.STATE_PREP_SWIPE:
			statePath = "_PREP_SWIPE";
			break;
		case SnakeCommander.STATE_ATT_SWIPE:
			if(this.attCount % 2 == 0) {
				statePath = "_ATT_SWIPE_ALT";
			} else {
				statePath = "_ATT_SWIPE";
			}
			break;
		case SnakeCommander.STATE_PREP_SLAM:
			statePath = "_ALERT";
			break;
		case SnakeCommander.STATE_MID_SLAM:
			statePath = "_PREP_SWIPE";
			break;
		case SnakeCommander.STATE_ATT_SLAM:
			statePath = "_ATT_SWIPE";
			break;
		case SnakeCommander.STATE_PREP_FIRE:
			statePath = "_PREP_FIRE";
			break;
		case SnakeCommander.STATE_ATT_FIRE:
			if(this.attCount % 2 == 0) {
				statePath = "_ATT_FIRE";
			} else {
				statePath = "_ATT_FIRE_ALT";
			}
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return this.scImage_DEAD;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.WATER);
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		// Randomly play one of two sounds
		Random r = new Random();
		int whichSound = r.nextInt(2);
		if(whichSound == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_DEATH.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("BunnyWarrior_DEATH2.wav"));
		}
	}
	
	// Override that resets a few extra parameters
	@Override
	public void returnToOrigin() {
		super.returnToOrigin();
		this.attCount = 0;
	}
	
	@Override
	public void takeTurn() {
		// Fetch reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if(!this.isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = plrX - this.xPos;
		int distY = plrY - this.yPos;
		
		switch(this.state) {
			case SnakeCommander.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					Random r = new Random();
					int whichSound = r.nextInt(2);
					if(whichSound == 0) {
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
				if(distX > 0) {
					dx = 1;
				} else if (distX < 0) {
					dx = -1;
				}
				// Y-movement
				if(distY > 0) {
					dy = 1;
				} else if (distY < 0) {
					dy = -1;
				}
				
				// Change state to prepare a stab/swipe 100% of the time if next to player
				if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;

					// Choose a melee-based attack
					this.chooseMeleeAttack();
					
					// Else, try to move x-wise or y-wise closer to the player
					// based on which dimension you are further distance from them
				} else {
					// Path-find to the player if we can
					Dimension nextStep = PathFinder.findPath(this.xPos, this.yPos, plrX, plrY, this);
					if(nextStep == null) {
						// Blindly pursue the target
						DumbFollow.blindPursue(distX, distY, dx, dy, this);
					} else {
						int changeX = nextStep.width - this.xPos;
						int changeY = nextStep.height - this.yPos;
						this.moveCharacter(changeX, changeY);
					}
					
					// Recalculate relative location to player
					distX = plrX - this.xPos;
					distY = plrY - this.yPos;
					
					// Relative movement direction (Initialize at 0)
					dx = 0;
					dy = 0;
					
					// Recalculate relative movement directions
					// X-movement
					if(distX > 0) {
						dx = 1;
					} else if (distX < 0) {
						dx = -1;
					}
					// Y-movement
					if(distY > 0) {
						dy = 1;
					} else if (distY < 0) {
						dy = -1;
					}
					
					// Decide if snake should attempt a ranged attack
					// Attempt only 1/2 of the time
					int shouldShoot = new Random().nextInt(2);
					if((shouldShoot == 0) && (((Math.abs(distX) <= 4) && (Math.abs(distX) >= 2) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) >= 2) && (Math.abs(distY) <= 4)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
						if(hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							
							// Choose a melee-based attack
							this.chooseRangedAttack();
						}
					}
					
					// Decide if snake should attempt a moving melee attack
					// Punishes running away and eager approaches
					// Attempt only 1/2 of the time
					int shouldMelee = new Random().nextInt(2);
					if((shouldMelee == 0) && (((Math.abs(distX) <= 2) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 2)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
						if(hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							
							// Choose a melee-based attack
							this.chooseMeleeAttack();
						}
					}
				}
				break;
			case SnakeCommander.STATE_PREP_BITE:
				// Step in marked direction if possible
				this.moveCharacter(this.xMarkDir, this.yMarkDir);
				
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + (this.xMarkDir*2), this.yPos + (this.yMarkDir*2)));
				
				// Play sound
				SoundPlayer.playWAV(GPath.createSoundPath("whip_ATT.wav"));
				
				// Attack if two tiles away from player (In a straight line)
				if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir) ||
						(plrX == this.xPos + (this.xMarkDir*2) && plrY == this.yPos + (this.yMarkDir*2))) {
					this.playerInitiate();
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
				if(Math.abs(this.xMarkDir) > Math.abs(this.yMarkDir)) {
					// Player to left/right
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + 1));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos - 1));
					
					// Attack player if in affected space
					if((plrX == this.xPos + this.xMarkDir) &&
							(plrY == this.yPos || plrY == this.yPos - 1 || plrY == this.yPos + 1)) {
						this.playerInitiate();
					}
				} else {
					// Player above/below
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos, this.yPos + this.yMarkDir));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + 1, this.yPos + this.yMarkDir));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos - 1, this.yPos + this.yMarkDir));
					
					// Attack player if in affected space
					if((plrY == this.yPos + this.yMarkDir) &&
							(plrX == this.xPos || plrX == this.xPos - 1 || plrX == this.xPos + 1)) {
						this.playerInitiate();
					}
				}
				
				// Change state and increment attack counter
				this.attCount += 1;
				this.state = SnakeCommander.STATE_ATT_SWIPE;
				break;
			case SnakeCommander.STATE_ATT_SWIPE:
				// Attack a specified number of times (swipeMaxCount)
				if(this.attCount >= this.swipeMaxCount) {
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
				if(Math.abs(this.xMarkDir) > Math.abs(this.yMarkDir)) {
					// Player to left/right
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + 1));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos - 1));
					
					// Attack player if in affected space
					if((plrX == this.xPos + this.xMarkDir) &&
							(plrY == this.yPos || plrY == this.yPos - 1 || plrY == this.yPos + 1)) {
						this.playerInitiate();
					}
				} else {
					// Player above/below
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos, this.yPos + this.yMarkDir));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + 1, this.yPos + this.yMarkDir));
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos - 1, this.yPos + this.yMarkDir));
					
					// Attack player if in affected space
					if((plrY == this.yPos + this.yMarkDir) &&
							(plrX == this.xPos || plrX == this.xPos - 1 || plrX == this.xPos + 1)) {
						this.playerInitiate();
					}
				}
				
				break;
			case SnakeCommander.STATE_PREP_SLAM:
				// Attack if next to player. Otherwise, continue rushing in the current direction
				if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir)) {
					this.playerInitiate();
					
					// Play hit sound
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
					
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
					
					// Change state to confirm that we hit
					this.state = SnakeCommander.STATE_ATT_SLAM;
				} else {
					// Start to charge in player's direction
					
					// Try to move in the player's direction
					if(this.moveCharacter(this.xMarkDir, this.yMarkDir)) {
						// If successful, change state
						this.state = SnakeCommander.STATE_MID_SLAM;
					} else {
						// If not successful, end the charge already
						
						// Mark tile with damage indicator
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
						
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
				if(distX > 0) {
					dx = 1;
				} else if (distX < 0) {
					dx = -1;
				}
				// Y-movement
				if(distY > 0) {
					dy = 1;
				} else if (distY < 0) {
					dy = -1;
				}
				
				// Try to change direction to chase player, but only once
				if(this.attCount == 0 && (this.xMarkDir != dx || this.yMarkDir != dy) && (dx == 0 || dy == 0)) {
					this.attCount += 1;
					this.xMarkDir = dx;
					this.yMarkDir = dy;
				}
				
				// Attack if next to player. Otherwise, continue rushing in the current direction
				if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir)) {
					this.playerInitiate();
					
					// Play hit sound
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
					
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
					
					// Reset attack counter and change state to confirm that we hit
					this.attCount = 0;
					this.state = SnakeCommander.STATE_ATT_SLAM;
				} else {
					// Keep charging in player's direction
					
					// Try to move in the player's direction
					if(this.moveCharacter(this.xMarkDir, this.yMarkDir)) {
						// If successful continue onwards in this state
					} else {
						// If not successful, end the charge
						
						// Mark tile with damage indicator
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
						
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
				EntityManager.getInstance().getEffectManager().addEffect(new FireEffect(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir, 3));
				
				// Check if player is in damaging spot(s)
				if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir)) {
					this.playerInitiate();
				}
				
				// Change states
				this.state = SnakeCommander.STATE_ATT_FIRE;
				break;
			case SnakeCommander.STATE_ATT_FIRE:
				// Fetch reference to EntityManager
				EntityManager em = EntityManager.getInstance();
				
				if(this.attCount == 0) {
					// Mark tile(s) with damage indicators
					em.getEffectManager().addEffect(new FireEffect(this.xPos + (this.xMarkDir*2), this.yPos + (this.yMarkDir*2), 2));
					em.getEffectManager().addEffect(new FireEffect(this.xPos + (this.xMarkDir*2) + Math.abs(this.yMarkDir), this.yPos + (this.yMarkDir*2) + Math.abs(this.xMarkDir), 2));
					em.getEffectManager().addEffect(new FireEffect(this.xPos + (this.xMarkDir*2) - Math.abs(this.yMarkDir), this.yPos + (this.yMarkDir*2) - Math.abs(this.xMarkDir), 2));
					
					// Check if player is in damaging spot(s)
					if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir) ||
							(plrX == this.xPos + (this.xMarkDir*2) && plrY == this.yPos + (this.yMarkDir*2)) ||
							(plrX == this.xPos + (this.xMarkDir*2) + Math.abs(this.yMarkDir) && plrY == this.yPos + (this.yMarkDir*2) + Math.abs(this.xMarkDir)) ||
							(plrX == this.xPos + (this.xMarkDir*2) - Math.abs(this.yMarkDir) && plrY == this.yPos + (this.yMarkDir*2) - Math.abs(this.xMarkDir))) {
						this.playerInitiate();
					}
				} else if(this.attCount == 1) {
					// Mark tile(s) with damage indicators
					em.getEffectManager().addEffect(new FireEffect(this.xPos + (this.xMarkDir*3), this.yPos + (this.yMarkDir*3), 1));
					em.getEffectManager().addEffect(new FireEffect(this.xPos + (this.xMarkDir*3) + Math.abs(this.yMarkDir), this.yPos + (this.yMarkDir*3) + Math.abs(this.xMarkDir), 1));
					em.getEffectManager().addEffect(new FireEffect(this.xPos + (this.xMarkDir*3) - Math.abs(this.yMarkDir), this.yPos + (this.yMarkDir*3) - Math.abs(this.xMarkDir), 1));
					em.getEffectManager().addEffect(new FireEffect(this.xPos + (this.xMarkDir*3) + (Math.abs(this.yMarkDir)*2), this.yPos + (this.yMarkDir*3) + (Math.abs(this.xMarkDir)*2), 1));
					em.getEffectManager().addEffect(new FireEffect(this.xPos + (this.xMarkDir*3) - (Math.abs(this.yMarkDir)*2), this.yPos + (this.yMarkDir*3) - (Math.abs(this.xMarkDir)*2), 1));
					
					// Check if player is in damaging spot(s)
					if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir) ||
							(plrX == this.xPos + (this.xMarkDir*2) && plrY == this.yPos + (this.yMarkDir*2)) ||
							(plrX == this.xPos + (this.xMarkDir*2) + Math.abs(this.yMarkDir) && plrY == this.yPos + (this.yMarkDir*2) + Math.abs(this.xMarkDir)) ||
							(plrX == this.xPos + (this.xMarkDir*2) - Math.abs(this.yMarkDir) && plrY == this.yPos + (this.yMarkDir*2) - Math.abs(this.xMarkDir)) ||
							(plrX == this.xPos + (this.xMarkDir*3) && plrY == this.yPos + (this.yMarkDir*3)) ||
							(plrX == this.xPos + (this.xMarkDir*3) + Math.abs(this.yMarkDir) && plrY == this.yPos + (this.yMarkDir*3) + Math.abs(this.xMarkDir)) ||
							(plrX == this.xPos + (this.xMarkDir*3) - Math.abs(this.yMarkDir) && plrY == this.yPos + (this.yMarkDir*3) - Math.abs(this.xMarkDir)) ||
							(plrX == this.xPos + (this.xMarkDir*3) + (Math.abs(this.yMarkDir)*2) && plrY == this.yPos + (this.yMarkDir*3) + (Math.abs(this.xMarkDir)*2)) ||
							(plrX == this.xPos + (this.xMarkDir*3) - (Math.abs(this.yMarkDir)*2) && plrY == this.yPos + (this.yMarkDir*3) - (Math.abs(this.xMarkDir)*2))) {
						this.playerInitiate();
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
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
	// Chooses the next melee attack for the Snake
	private void chooseMeleeAttack() {
		// Decide whether to swipe, charge, or bite
		int swipeBiteSlam = new Random().nextInt(10);
		if(swipeBiteSlam <= 2) {
			this.state = SnakeCommander.STATE_PREP_BITE;
		} else if(swipeBiteSlam <= 6) {
			this.state = SnakeCommander.STATE_PREP_SWIPE;
		} else {
			this.state = SnakeCommander.STATE_PREP_SLAM;
		}
	}
	
	// Chooses the next ranged attack for the Snake
	private void chooseRangedAttack() {
		// Decide whether to use Flamethrower or Luger
		int flameOrGun = new Random().nextInt(2);
		if(flameOrGun == 0) {
			this.state = SnakeCommander.STATE_PREP_FIRE;
		} else {
			this.state = SnakeCommander.STATE_PREP_FIRE;
		}
	}
	
}
