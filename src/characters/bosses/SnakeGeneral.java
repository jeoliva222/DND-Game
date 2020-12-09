package characters.bosses;

import items.GPickup;
import items.MediumHealthPotion;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PatrolPattern;
import characters.GCharacter;
import characters.allies.Player;
import effects.BombEffect;
import effects.BulletEffect;
import effects.DamageIndicator;
import effects.FakeSnakeEffect;
import effects.FakeSnakeEffect.FakeSnakeType;
import effects.FireEffect;
import effects.GEffect;
import effects.WarningIndicator;
import effects.WarpFizzleEffect;
import gui.GameScreen;
import gui.LogScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import helpers.Triplet;
import managers.EntityManager;
import managers.PickupManager;
import projectiles.GProjectile;
import projectiles.SnakeCannonball;
import tiles.AltGround;
import tiles.MovableType;
import weapons.Armory;

// Class representing the 2nd Phase of the Desert area boss
public class SnakeGeneral extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 4882048520306329195L;
	
	// Modifiers/Statistics

	private int MAX_HP = 120;
	
	private int ARMOR_VAL = 0;
	
	private int MIN_DMG = 4;
	private int MAX_DMG = 6;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.4;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_COMBO = 3;
	private static final int STATE_ATT_COMBO = 4;
	private static final int STATE_PREP_RETREAT = 5;
	private static final int STATE_ATT_RETREAT = 6;
	private static final int STATE_PREP_CHAINGUN = 7;
	private static final int STATE_ATT_CHAINGUN = 8;
	private static final int STATE_PREP_MINE = 9;
	private static final int STATE_ATT_MINE = 10;
	private static final int STATE_WARP_AWAY = 11;
	private static final int STATE_SPC_ASSASSINATE = 12;
	private static final int STATE_SPC_ASSASSINATE_ATT = 13;
	private static final int STATE_SPC_BLITZ = 14;
	private static final int STATE_SPC_CANNON = 15;
	private static final int STATE_STUN = 16;
	
	//----------------------------
	// Additional Behavior
	
	// Direction the screen will be marked up for damage indicators
	private int xMarkDir = 0;
	private int yMarkDir = 0;
	
	// Attack counter for multi-turn attacks
	private int attCount = 0;
	
	// Amount of times the general swipes in combo
	private int swipeMaxCount = 1;
	
	// Flag determining whether snake stops retreating
	private boolean breakRetreat = false;
	
	// Flag indicating whether General hit his chaingun attack
	private boolean hitShots = false;
	
	// Number of times damaged (gets reset periodically)
	private int dmgCount = 0;
	
	// Counter that determines whether General should do special attack
	private int spcCount = 0;
	private final int SPC_MAX = 30;
	
	// Limits defining Cannon attack/cooldown time
	private final int CANNON_MAX = 8;
	private final int CANNON_COOLDOWN = 3;
	
	// Limit defining stun time
	private final int STUN_TIME = 2;
	
	// Flags telling the snake whether we did certain special attacks
	private boolean didSpc0 = false;
	private boolean didSpc1 = false;
	private boolean didSpc2 = false;
	
	// Flag indicating whether General is in phase 2 of the fight
	private boolean isPhase2 = false;
	
	// Flag indicating whether the General recently did a retreat
	private boolean recentRetreat = false;
	
	// Flag indicating whether snake was damaged this turn
	private boolean recentDmg = false;
	
	// List containing bomb information (X-position, Y-position, Timer)
	private ArrayList<Triplet<Integer, Integer, Integer>> placedBombs = new ArrayList<Triplet<Integer, Integer, Integer>>();
	
	// Corners to warp to
	private Dimension[] corners = new Dimension[] {
			new Dimension(4, 2),
			new Dimension(8, 2),
			new Dimension(4, 5),
			new Dimension(8, 5)
	};
	
	// Order of rows to attack in Blitz/Cannon special attacks
	private int[] rowOrder = new int[] {2, 3, 4, 5};
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_GENERAL);
	private String sgImage_base = "snakegeneral";
	//--
	private String sgImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_GENERAL, "snakegeneral_dead.png");

	// Constructors
	public SnakeGeneral(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeGeneral.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
	}
	
	public String getName() {
		return "General Hanz";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + sgImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (isHealthy()) {
			hpPath = "_full";
		} else if (isAlive()) {
			hpPath = "_fatal";
		} else {
			return GPath.NULL;
		}
		
		switch (state) {
			case SnakeGeneral.STATE_IDLE:
			case SnakeGeneral.STATE_PURSUE:
				// No extra path
				break;
			case SnakeGeneral.STATE_ALERTED:
				statePath = "_ALERT";
				break;
			case SnakeGeneral.STATE_PREP_COMBO:
				statePath = "_PREP_COMBO";
				break;
			case SnakeGeneral.STATE_ATT_COMBO:
				if (attCount % 2 == 0) {
					statePath = "_ATT_COMBO_STAB";
				} else {
					statePath = "_ATT_COMBO_SWIPE";
				}
				break;
			case SnakeGeneral.STATE_PREP_RETREAT:
				if (attCount <= 1) {
					statePath = "_PREP_FIRE";
				} else if (attCount % 2 == 1) {
					statePath = "_ATT_FIRE";
				} else {
					statePath = "_ATT_FIRE_ALT";
				}
				break;
			case SnakeGeneral.STATE_ATT_RETREAT:
				if (attCount % 2 == 1) {
					statePath = "_ATT_FIRE";
				} else {
					statePath = "_ATT_FIRE_ALT";
				}
				break;
			case SnakeGeneral.STATE_PREP_CHAINGUN:
				if (attCount == 0) {
					statePath = "_PREP_GUN";
				} else {
					statePath = "_ATT_GUN";
				}
				break;
			case SnakeGeneral.STATE_ATT_CHAINGUN:
				statePath = "_ATT_GUN";
				break;
			case SnakeGeneral.STATE_PREP_MINE:
				statePath = "_PREP_MINE";
				break;
			case SnakeGeneral.STATE_ATT_MINE:
				statePath = "_ATT_MINE";
				break;
			case SnakeGeneral.STATE_WARP_AWAY:
				statePath = "_WARP";
				break;
			case SnakeGeneral.STATE_SPC_ASSASSINATE:
				if (attCount <= 4) {
					return GPath.NULL;
				} else {
					statePath = "_PREP_FIRE";
				}
				break;
			case SnakeGeneral.STATE_SPC_ASSASSINATE_ATT:
				statePath = "_ATT_FIRE_ALT";
				break;
			case SnakeGeneral.STATE_SPC_BLITZ:
				if (attCount <= 7) {
					return GPath.NULL;
				} else if (attCount == 8) {
					statePath = "_ATT_COMBO_SWIPE";
				} else {
					// No extra path
				}
				break;
			case SnakeGeneral.STATE_SPC_CANNON:
				if (attCount <= 1) {
					return GPath.NULL;
				} else if (attCount > (CANNON_MAX + 1)) {
					statePath = "_PREP_CANNON";
				} else if (xPos == 9) {
					if (attCount % 2 == 0) {
						statePath = "_PREP_CANNON";
					} else {
						statePath = "_ATT_CANNON";
					}
				} else {
					if (attCount % 2 == 0) {
						statePath = "_ATT_CANNON";
					} else {
						statePath = "_PREP_CANNON";
					}
				}
				break;
			case SnakeGeneral.STATE_STUN:
				if (attCount % 2 == 0) {
					statePath = "_STUN";
				} else {
					statePath = "_STUN_ALT";
				}
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return this.sgImage_DEAD;
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
		// Play death sound
		SoundPlayer.playWAV(GPath.createSoundPath("Snake_Death.wav"), getXPos(), getYPos());
		
		// Clear any remaining bombs
		placedBombs.clear();
		
		// Open the doors to the arena
		GameScreen.getTile(4, 1).setTileType(new AltGround());
		GameScreen.getTile(5, 1).setTileType(new AltGround());
		
		// Open loot compartment
		GameScreen.getTile(7, 1).setTileType(new AltGround());
		GameScreen.getTile(8, 1).setTileType(new AltGround());
		
		// Drop loot
		PickupManager puManager = EntityManager.getInstance().getPickupManager();
		puManager.addPickup(new GPickup(7, 1, new MediumHealthPotion()));
		puManager.addPickup(new GPickup(8, 1, Armory.venomGun));
		
		// Change music to regular music
		SoundPlayer.changeMidi(EntityManager.getInstance().getActiveArea().getMusic(),
				EntityManager.getInstance().getActiveArea().getMusicVolume());
		
		// Log a final death message
		LogScreen.log("The general and his tank lie broken and defeated...");
	}
	
	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		resetFlags();
	}
	
	// Resets all flags/counters
	private void resetFlags() {
		this.attCount = 0;
		this.dmgCount = 0;
		this.breakRetreat = false;
		this.recentDmg = false;
		this.hitShots = false;
	}
	
	// Returns whether General is considered healthy or not
	// Used for determining image rendering
	private boolean isHealthy() {
		return (currentHP > (maxHP / 3));
	}
	
	// Override that increments internal counter ands sets logic flags
	@Override
	public boolean damageCharacter(int damage) {
		boolean result = super.damageCharacter(damage);
		
		// Increment damage counter and set that we've recently been damaged
		this.dmgCount += damage;
		this.recentDmg = true;
		
		// If below (5/6) health, go to phase 2
		if ((!isPhase2) && (currentHP <= (maxHP*5/6))) {
			SoundPlayer.playWAV(GPath.createSoundPath("snake1_warn1.wav"));
			this.isPhase2 = true;
		} else {
			playHurt();
		}
		
		return result;
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
		
		// Relative movement direction (Initialize at 0)
		int dx = 0;
		int dy = 0;
		
		// Manage the bombs
		manageBombs(plrX, plrY);
		
		switch (state) {
			case SnakeGeneral.STATE_IDLE: //------------------------------------------------------------
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					Random r = new Random();
					int whichSound = r.nextInt(2);
					if (whichSound == 0) {
						SoundPlayer.playWAV(GPath.createSoundPath("snake1_greet1.wav"));
					} else {
						SoundPlayer.playWAV(GPath.createSoundPath("snake1_warn1.wav"));
					}
					this.state = SnakeGeneral.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case SnakeGeneral.STATE_ALERTED: //------------------------------------------------------------
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = SnakeGeneral.STATE_PURSUE;
				break;
			case SnakeGeneral.STATE_ATT_MINE:
				// Continue through to pursue logic
				this.state = SnakeGeneral.STATE_PURSUE;
				// ----
			case SnakeGeneral.STATE_PURSUE:	 //------------------------------------------------------------
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
				
				// If we're in phase 2 and have recently warped, try special attack
				if (isPhase2 && recentRetreat) {
					// Check if we should do special attack
					// Probability increases as turns pass without special attack
					Random r = new Random();
					int shouldSpecial = r.nextInt(SPC_MAX);
					if (shouldSpecial < spcCount) {
						// Reset the counters/flags
						this.recentRetreat = false;
						this.spcCount = 0;
						
						// Set state to warp away
						this.state = SnakeGeneral.STATE_WARP_AWAY;
						break;
					}
				}
				
				// Change state to prepare a stab/swipe 100% of the time if next to player
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;

					// Choose a close-quarters melee-based attack
					chooseCQMeleeAttack();
					
					// Else, try to move x-wise or y-wise closer to the player
					// based on which dimension you are further distance from them
				} else {
					// Blindly pursue player (Square arena; No need for path-finding)
					DumbFollow.blindPursue(distX, distY, dx, dy, this);
					
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
					
					// Decide if snake should attempt a moving melee attack
					// Punishes running away and eager approaches
					// Attempt 2/3 of the time
					int shouldMelee = new Random().nextInt(3);
					if ((shouldMelee <= 1) && (((Math.abs(distX) <= 3) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 3)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
						if (hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							
							// Choose a ranged melee-based attack
							chooseRangedMeleeAttack();
						}
					}
				}
				
				// Reset recently retreated flag
				this.recentRetreat = false;
				break;
			case SnakeGeneral.STATE_PREP_COMBO: //------------------------------------------------------------
				// If player is directly diagonal from General, don't lunge forward
				if (!((Math.abs(distX) == 1) && (Math.abs(distY) == 1))) {
					// Move General in marked direction
					moveCharacter(xMarkDir, yMarkDir);
				}
				
				// Use direction from player to mark squares
				SoundPlayer.playWAV(GPath.createSoundPath("Snake_Bite.wav"), getXPos(), getYPos());
				addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
				addEffect(new DamageIndicator(xPos + (xMarkDir*2), yPos + (yMarkDir*2)));
				
				// Hit player in affected spaces
				if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir) ||
						(plrX == xPos + (xMarkDir*2) && plrY == yPos + (yMarkDir*2))) {
					playerInitiate();
				}
				
				// Change state and increment attack counter
				this.state = SnakeGeneral.STATE_ATT_COMBO;
				break;
			case SnakeGeneral.STATE_ATT_COMBO: //------------------------------------------------------------
				// Attack a specified number of times (swipeMaxCount)
				if (attCount >= swipeMaxCount) {
					// Reset attack counter
					this.attCount = 0;
					
					// Change state + Cooldown period
					this.state = SnakeGeneral.STATE_PURSUE;
					break;
				} else {
					// Increment attack counter
					this.attCount += 1;
				}
				
				// Recalculate relative location to player
				distX = (plrX - xPos);
				distY = (plrY - yPos);
				
				// Do side-swipe attack towards direction player moved
				SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"), getXPos(), getYPos());
				if (xMarkDir != 0) {
					// Recalculate Y attack direction
					if (distY > 0) {
						this.yMarkDir = 1;
					} else if (distY < 0) {
						this.yMarkDir = -1;
					} else {
						this.yMarkDir = 1 - (2 * new Random().nextInt(2));
					}
					
					// Player to left/right
					addEffect(new DamageIndicator(xPos + xMarkDir, yPos));
					addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
					
					// Attack player if in affected space
					if ((plrX == xPos + xMarkDir) && (plrY == yPos || plrY == yPos + yMarkDir)) {
						playerInitiate();
					}
				} else {
					// Recalculate X attack direction
					if (distX > 0) {
						this.xMarkDir = 1;
					} else if (distX < 0) {
						this.xMarkDir = -1;
					} else {
						this.xMarkDir = 1 - (2 * new Random().nextInt(2));
					}
					
					// Player above/below
					addEffect(new DamageIndicator(xPos, yPos + yMarkDir));
					addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
					
					// Attack player if in affected space
					if ((plrY == yPos + yMarkDir) && (plrX == xPos || plrX == xPos + xMarkDir)) {
						playerInitiate();
					}
				}
				
				break;
			case SnakeGeneral.STATE_PREP_RETREAT: //------------------------------------------------------------
				if (attCount == 0) {
					// Do nothing for first turn
				} else {
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
					
					// Move away from player, checking for whether we actually moved or not
					// If we moved, swing in the opposite direction
					int tempX = xPos;
					int tempY = yPos;
					DumbFollow.blindPursue(distX, distY, -dx, -dy, this);
					if (tempX == xPos && tempY == yPos) {
						this.breakRetreat = true;
					} else {
						this.xMarkDir = (tempX - xPos);
						this.yMarkDir = (tempY - yPos);
					}
					
					// Play sound if first turn retreating
					if (attCount == 1) {
						SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"), getXPos(), getYPos());
					}
					
					// Use direction from player to mark squares
					if (Math.abs(xMarkDir) > Math.abs(yMarkDir)) {
						// Player to left/right
						addEffect(new FireEffect(xPos + xMarkDir, yPos));
						addEffect(new FireEffect(xPos + xMarkDir, yPos + 1));
						addEffect(new FireEffect(xPos + xMarkDir, yPos - 1));
						addEffect(new FireEffect(xPos + (xMarkDir*2), yPos));
						addEffect(new FireEffect(xPos + (xMarkDir*2), yPos + 1));
						addEffect(new FireEffect(xPos + (xMarkDir*2), yPos - 1));
						
						// Attack player if in affected space
						if (((plrX == xPos + xMarkDir) || (plrX == xPos + (xMarkDir*2))) &&
								(plrY == yPos || plrY == yPos - 1 || plrY == yPos + 1)) {
							playerInitiate();
						}
					} else {
						// Player above/below
						addEffect(new FireEffect(xPos, yPos + yMarkDir));
						addEffect(new FireEffect(xPos + 1, yPos + yMarkDir));
						addEffect(new FireEffect(xPos - 1, yPos + yMarkDir));
						addEffect(new FireEffect(xPos, yPos + (yMarkDir*2)));
						addEffect(new FireEffect(xPos + 1, yPos + (yMarkDir*2)));
						addEffect(new FireEffect(xPos - 1, yPos + (yMarkDir*2)));
						
						// Attack player if in affected space
						if (((plrY == yPos + yMarkDir) || (plrY == yPos + (yMarkDir*2))) &&
								(plrX == xPos || plrX == xPos - 1 || plrX == xPos + 1)) {
							playerInitiate();
						}
					}
					
					// Check to break out
					if (breakRetreat) {
						// If breaking out, reset break flag and change state
						this.breakRetreat = false;
						this.state = SnakeGeneral.STATE_ATT_RETREAT;
					}
				}
				
				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeGeneral.STATE_ATT_RETREAT: //--------------------------------------------------------------
				// Reset counters and change state
				this.recentRetreat = true;
				this.attCount = 0;
				this.dmgCount = 0;
				this.state = SnakeGeneral.STATE_PURSUE;
				break;
			case SnakeGeneral.STATE_PREP_CHAINGUN: //------------------------------------------------------------
				if (attCount > 0) {
					// Try to move at the player if we didn't hit chaingun shots
					if (!hitShots) {
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
						
						// Check which direction to move
						if (xMarkDir != 0) {
							// Move vertically towards player
							moveCharacter(0, dy);
						} else {
							// Move horizontally towards player
							moveCharacter(dx, 0);
						}
					}
					
					// Change state for next turn
					this.state = SnakeGeneral.STATE_ATT_CHAINGUN;
				}
				
				// Play firing sound
				SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Fire.wav"), getXPos(), getYPos());

				// Safety check to prevent infinite loop
				if (xMarkDir == 0 && yMarkDir == 0) {
					this.xMarkDir = 1;
				}
				
				// Fire at player
				int nextX = (getXPos() + xMarkDir);
				int nextY = (getYPos() + yMarkDir);
				boolean isEndHit = false;
				while (!isEndHit) { // BEGIN While ------------------------
					
					// Check if we hit player
					if (nextX == plrX && nextY == plrY) {
						this.hitShots = true;
						playerInitiate();
					}
					
					// Check for walls or OOB to see if we need to keep checking tiles
					try {
						isEndHit = MovableType.isWall(GameScreen.getTile(nextX, nextY).getTileType().getMovableType());
					} catch (ArrayIndexOutOfBoundsException e) {
						isEndHit = true;
					}
					
					// Mark the tile (if we didn't hit something)
					if (!isEndHit) {
						addEffect(new BulletEffect(nextX, nextY, xMarkDir, yMarkDir));
					}
					
					// Then update the next coordinates to check
					nextX += xMarkDir;
					nextY += yMarkDir;
					
				} // END While Loop --------------------------------------
				
				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeGeneral.STATE_ATT_CHAINGUN: //------------------------------------------------------------
				// Reset attack counter and change state
				this.attCount = 0;
				this.hitShots = false;
				this.state = SnakeGeneral.STATE_PURSUE;
				break;
			case SnakeGeneral.STATE_PREP_MINE: //---------------------------------------------------------------
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
				
				// Move away from player
				DumbFollow.blindPursue(distX, distY, -dx, -dy, this);
				
				// Lay down bomb
				addEffect(new BombEffect(xPos + dx, yPos + dy, false));
				placedBombs.add(new Triplet<Integer, Integer, Integer>(xPos + dx, yPos + dy, 5));

				// Switch back to mine attack state (AKA: pursuit)
				this.state = SnakeGeneral.STATE_ATT_MINE;
				break;
			case SnakeGeneral.STATE_WARP_AWAY: //------------------------------------------------------------
				// Warp away to top-right corner (Outside of arena)
				this.xPos = 9;
				this.yPos = 0;
				
				// Decide which special attack to use
				chooseSpecialAttack();
				break;
			case SnakeGeneral.STATE_SPC_ASSASSINATE: //------------------------------------------------------------
				if (attCount == 0) {
					// Decide which direction to attack
					Random r = new Random();
					int whichDir = r.nextInt(4);
					if (whichDir == 0) {
						// Choose Up
						this.xMarkDir = 0;
						this.yMarkDir = -1;
					} else if (whichDir == 1) {
						// Choose Right
						this.xMarkDir = 1;
						this.yMarkDir = 0;
					} else if (whichDir == 2) {
						// Choose Down
						this.xMarkDir = 0;
						this.yMarkDir = 1;
					} else {
						// Choose Left
						this.xMarkDir = -1;
						this.yMarkDir = 0;
					}
				} else if (attCount <= 3) {
					// Show illusion images around the player
					addEffect(new WarpFizzleEffect(plrX, plrY - 1));
					addEffect(new WarpFizzleEffect(plrX + 1, plrY));
					addEffect(new WarpFizzleEffect(plrX, plrY + 1));
					addEffect(new WarpFizzleEffect(plrX - 1, plrY));
				} else if (attCount == 4) {
					// Warp next to player
					this.xPos = (plrX + xMarkDir);
					this.yPos = (plrY + yMarkDir);
					
					// Display fakes in other directions (if general is not there)
					boolean hpFlag = isHealthy();
					
					// Up direction
					if (!((plrX == xPos) && (plrY - 1 == yPos))) {
						addEffect(new FakeSnakeEffect(plrX, plrY - 1, FakeSnakeType.FIRE_PREP, hpFlag));
					}
					
					// Right direction
					if (!((plrX + 1 == xPos) && (plrY == yPos))) {
						addEffect(new FakeSnakeEffect(plrX + 1, plrY, FakeSnakeType.FIRE_PREP, hpFlag));
					}
					
					// Down direction
					if (!((plrX == xPos) && (plrY + 1 == yPos))) {
						addEffect(new FakeSnakeEffect(plrX, plrY + 1, FakeSnakeType.FIRE_PREP, hpFlag));
					}
					
					// Left direction
					if (!((plrX - 1 == xPos) && (plrY == yPos))) {
						addEffect(new FakeSnakeEffect(plrX - 1, plrY, FakeSnakeType.FIRE_PREP, hpFlag));
					}
				} else {
					// Reset attack count
					this.attCount = 0;
					
					// Check if we've taken damage recently
					if (recentDmg) {
						// Stun the general and break
						stunGeneral();
						break;
					} else {
						// Correct attacking direction
						this.xMarkDir = xMarkDir * -1;
						this.yMarkDir = yMarkDir * -1;
						
						// Mark Tiles for damage
						addEffect(new FireEffect(xPos + xMarkDir, yPos + yMarkDir));
						addEffect(new FireEffect(xPos + (xMarkDir*2), yPos + (yMarkDir*2)));
						addEffect(new FireEffect(xPos + xMarkDir - (yMarkDir), yPos + yMarkDir - (xMarkDir)));
						addEffect(new FireEffect(xPos + xMarkDir + (yMarkDir), yPos + yMarkDir + (xMarkDir)));
						
						// Play fire sound
						SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"), getXPos(), getYPos());
						
						// Attack player if in affected space
						if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir) ||
								(plrX == xPos + (xMarkDir*2) && plrY == yPos + (yMarkDir*2)) ||
								(plrX == xPos + xMarkDir - (yMarkDir) && plrY == yPos + yMarkDir - (xMarkDir)) ||
								(plrX == xPos + xMarkDir + (yMarkDir) && plrY == yPos + yMarkDir + (xMarkDir))) {
							playerInitiate();
						}
						
						// Change state to attacked
						this.state = SnakeGeneral.STATE_SPC_ASSASSINATE_ATT;
						break;
					}
				}

				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeGeneral.STATE_SPC_ASSASSINATE_ATT: //------------------------------------------------------------
				boolean inWallAtt = false;
				try {
					inWallAtt = MovableType.isWall(GameScreen.getTile(xPos, yPos).getTileType().getMovableType());
				} catch (ArrayIndexOutOfBoundsException e) {
					inWallAtt = true;
				}
				
				if (inWallAtt) {
					warpGeneral(plrX, plrY);
				}
				
				// Reset attack/special counter and change state
				this.attCount = 0;
				this.spcCount = 0;
				this.state = SnakeGeneral.STATE_PURSUE;
				break;
			case SnakeGeneral.STATE_SPC_BLITZ: //------------------------------------------------------------
				if (attCount == 0) {
					// Determine attack order
					Random r = new Random();
					for (int i = 3; i > 0; i--) {
						// Randomize swap index
						int shuffCoord = r.nextInt(i + 1);
						
						// Swap the spots
						int temp = rowOrder[i];
						rowOrder[i] = rowOrder[shuffCoord];
						rowOrder[shuffCoord] = temp;
					}
				}
				
				if (attCount <= 3) {
					// Flash warning across attacking rows
					for (int i = 4; i <= 8; i++) {
						addEffect(new WarningIndicator(i, rowOrder[attCount]));
					}
				} else if (attCount <= 7) {
					// Attack the rows in order they were previously warned
					
					// Put attack effects and check to damage player
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
					for (int i = 4; i <= 8; i++) {
						addEffect(new DamageIndicator(i, rowOrder[(attCount-4)]));
						if (plrX == i && plrY == rowOrder[(attCount-4)]) {
							playerInitiate();
						}
					}
					
					// Display the General attacking the current row
					if (attCount == 7) {
						// Warp real General to last attack location (Right side)
						this.xPos = 9;
						this.yPos = rowOrder[(attCount-4)];
					} else {
						// Display a fake General at the location
						if (attCount % 2 == 0) {
							// Left side
							addEffect(new FakeSnakeEffect(3, rowOrder[(attCount-4)], FakeSnakeType.SWIPE_ATT, isHealthy()));
						} else {
							// Right side
							addEffect(new FakeSnakeEffect(9, rowOrder[(attCount-4)], FakeSnakeType.SWIPE_ATT, isHealthy()));
						}
					}
					
				} else if (attCount <= 10) {
					// Cooldown for three turns
					
					// If we're hit during cooldown, enter stun
					if (recentDmg) {
						// Reset attack/special count
						this.attCount = 0;
						this.spcCount = 0;
						
						// Stun the general and break
						stunGeneral();
						break;
					}
					
				} else {
					// Reset attack/special count
					this.attCount = 0;
					this.spcCount = 0;
					
					// If we're hit before warp, enter stun
					if (recentDmg) {
						// Stun the general and break
						stunGeneral();
						break;
					} else {
						// Warp General back to arena
						warpGeneral(plrX, plrY);
	
						// Change state to pursue
						this.state = SnakeGeneral.STATE_PURSUE;
						break;	
					}
				}

				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeGeneral.STATE_SPC_CANNON: //------------------------------------------------------------
				if (attCount == 0) {
					// Determine attack directions
					Random r = new Random();
					for (int i = 3; i > 0; i--) {
						// Randomize swap index
						int shuffCoord = r.nextInt(i + 1);
						
						// Swap the spots
						int temp = rowOrder[i];
						rowOrder[i] = rowOrder[shuffCoord];
						rowOrder[shuffCoord] = temp;
					}
					
					// Mark the spots for warning
					addEffect(new WarningIndicator(4, rowOrder[0]));
					addEffect(new WarningIndicator(4, rowOrder[1]));
					addEffect(new WarningIndicator(8, rowOrder[2]));
					addEffect(new WarningIndicator(8, rowOrder[3]));
					
					// Show General warping in
					addEffect(new WarpFizzleEffect(3, rowOrder[0]));
					addEffect(new WarpFizzleEffect(3, rowOrder[1]));
					addEffect(new WarpFizzleEffect(9, rowOrder[2]));
					addEffect(new WarpFizzleEffect(9, rowOrder[3]));
				} else if (attCount <= CANNON_MAX) {
					// Warp in on the first turn of attack
					if (attCount == 1) {
						int whichPos = new Random().nextInt(4);
						if (whichPos <= 1) {
							// Left positions
							this.xPos = 3;
							this.yPos = rowOrder[whichPos];
						} else {
							// Right positions
							this.xPos = 9;
							this.yPos = rowOrder[whichPos];
						}
					}
					
					// If we're hit during cannon firing, enter stun
					if (recentDmg) {
						// Reset attack/special count
						this.attCount = 0;
						this.spcCount = 0;
						
						// Stun the general and break
						stunGeneral();
						break;
					}
					
					// Flag indicating if we're attacking on the left side
					// True = Left side | False = Right side
					boolean isLeftSideTurn = (attCount % 2 == 1);
					
					// Health flag for displaying fakes
					boolean hpFlag = isHealthy();
					
					// Mark other firing spots with Fake Generals
					// First row (Left)
					if (!((xPos == 3) && (yPos == rowOrder[0]))) {
						if (isLeftSideTurn) {
							addEffect(new FakeSnakeEffect(3, rowOrder[0], FakeSnakeType.CANNON_ATT, hpFlag));
						} else {
							addEffect(new FakeSnakeEffect(3, rowOrder[0], FakeSnakeType.CANNON_PREP, hpFlag));
						}
					}
					
					// Second row (Left)
					if (!((xPos == 3) && (yPos == rowOrder[1]))) {
						if (isLeftSideTurn) {
							addEffect(new FakeSnakeEffect(3, rowOrder[1], FakeSnakeType.CANNON_ATT, hpFlag));
						} else {
							addEffect(new FakeSnakeEffect(3, rowOrder[1], FakeSnakeType.CANNON_PREP, hpFlag));
						}
					}
					
					// Third row (Right)
					if (!((xPos == 9) && (yPos == rowOrder[2]))) {
						if (isLeftSideTurn) {
							addEffect(new FakeSnakeEffect(9, rowOrder[2], FakeSnakeType.CANNON_PREP, hpFlag));
						} else {
							addEffect(new FakeSnakeEffect(9, rowOrder[2], FakeSnakeType.CANNON_ATT, hpFlag));
						}
					}
					
					// Fourth row (Right)
					if (!((xPos == 9) && (yPos == rowOrder[3]))) {
						if (isLeftSideTurn) {
							addEffect(new FakeSnakeEffect(9, rowOrder[3], FakeSnakeType.CANNON_PREP, hpFlag));
						} else {
							addEffect(new FakeSnakeEffect(9, rowOrder[3], FakeSnakeType.CANNON_ATT, hpFlag));
						}
					}
					
					// Play cannon sound
					playCannonSound();
					
					// Alternate cannon-firing side
					if (isLeftSideTurn) {
						// Fire left cannons for the turn
						addProjectile(new SnakeCannonball(4, rowOrder[0], 1, 0, getClass()));
						addProjectile(new SnakeCannonball(4, rowOrder[1], 1, 0, getClass()));
						addEffect(new DamageIndicator(4, rowOrder[0]));
						addEffect(new DamageIndicator(4, rowOrder[1]));
					} else {
						// Fire right cannons for the turn
						addProjectile(new SnakeCannonball(8, rowOrder[2], -1, 0, getClass()));
						addProjectile(new SnakeCannonball(8, rowOrder[3], -1, 0, getClass()));
						addEffect(new DamageIndicator(8, rowOrder[2]));
						addEffect(new DamageIndicator(8, rowOrder[3]));
					}
				} else if (attCount <= (CANNON_MAX + CANNON_COOLDOWN)) { 
					// Cooldown period
					
					// If we're hit during cooldown, enter stun
					if (recentDmg) {
						// Reset attack/special count
						this.attCount = 0;
						this.spcCount = 0;
						
						// Stun the general and break
						stunGeneral();
						break;
					}
				} else {
					// Reset attack/special count
					this.attCount = 0;
					this.spcCount = 0;
					
					// If we're hit before warp, enter stun
					if (recentDmg) {
						// Stun the general and break
						stunGeneral();
						break;
					} else {
						// Warp General back to arena
						warpGeneral(plrX, plrY);
	
						// Change state to pursue
						this.state = SnakeGeneral.STATE_PURSUE;
						break;	
					}	
				}

				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeGeneral.STATE_STUN: //------------------------------------------------------------
				// Stay stunned for a few turns
				if (attCount <= STUN_TIME) {
					// Do nothing
					this.attCount += 1;
				} else {
					// Warp to a corner in the arena if we're inside a wall
					boolean inWallStn = false;
					try {
						inWallStn = MovableType.isWall(GameScreen.getTile(xPos, yPos).getTileType().getMovableType());
					} catch (ArrayIndexOutOfBoundsException e) {
						inWallStn = true;
					}
					
					if (inWallStn) {
						warpGeneral(plrX, plrY);
					}
					
					// Reset attack/special counter and change state
					this.attCount = 0;
					this.spcCount = 0;
					this.state = SnakeGeneral.STATE_PURSUE;
				}
				break;
			default: //---------------------------------------------------------------------------------
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		} // END OF SWITCH STATEMENT ****
		
		// End of turn incrementing/resetting of certain variables
		this.spcCount += 1;
		this.recentDmg = false;
		return;	
	}
	
	// Checks for best corner to warp to and returns it
	private void warpGeneral(int plrX, int plrY) {
		Dimension bestCorner = null;
		int bestTotal = -1;
		
		// Check for corner furthest away from player
		for (Dimension d: corners) {
			int xDist = Math.abs(d.width - plrX);
			int yDist = Math.abs(d.height - plrY);
			if (bestTotal < (xDist + yDist)) {
				bestCorner = d;
				bestTotal = (xDist + yDist);
			}
		}
		
		// Reset damage counter and warp flag
		this.dmgCount = 0;
		
		// Play warp sound
		SoundPlayer.playWAV(GPath.createSoundPath("warp.wav"));
		
		// Warp to the new coordinates
		this.xPos = bestCorner.width;
		this.yPos = bestCorner.height;
	}
	
	// Plays special sound and transitions General into Stun state
	private void stunGeneral() {
		// Play 'ouch' sound - TODO Temp
		SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"), getXPos(), getYPos());
		
		// Change state to stunned
		this.state = SnakeGeneral.STATE_STUN;
	}
	
	// Manage the bombs we have on screen (if there are any)
	private void manageBombs(int plrX, int plrY) {
		// Check if we have any bombs to manage
		if (placedBombs.size() <= 0) {
			// If no bombs, do nothing
			return;
		}
		
		ArrayList<Triplet<Integer, Integer, Integer>> bombsToRemove = new ArrayList<Triplet<Integer, Integer, Integer>>();
		for (Triplet<Integer, Integer, Integer> bomb: placedBombs) {
			// Tick down the bomb's timer by 1
			bomb.z += -1;
			
			// Check bomb's timer
			if (bomb.z <= 0) {
				// Detonate bomb if timer at 0
				addEffect(new FireEffect(bomb.x, bomb.y));				// Center
				addEffect(new FireEffect(bomb.x + 1, bomb.y - 1));		// Top-right
				addEffect(new FireEffect(bomb.x + 1, bomb.y + 1));		// Bottom-right
				addEffect(new FireEffect(bomb.x - 1, bomb.y - 1));		// Top-left
				addEffect(new FireEffect(bomb.x - 1, bomb.y + 1));		// Bottom-left
				
				// Play bomb detonation
				SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"), bomb.x, bomb.y);
				
				// Check if bomb hit the player
				if ((plrX == bomb.x && plrY == bomb.y) ||
						((plrX == bomb.x + 1) && (plrY == bomb.y + 1 || plrY == bomb.y - 1)) ||
						((plrX == bomb.x - 1) && (plrY == bomb.y + 1 || plrY == bomb.y - 1))) {
					playerInitiate();
				}
				
				// Add bomb to clean-up list
				bombsToRemove.add(bomb);
			} else {
				// Continue to display bomb effect
				if (bomb.z == 1) {
					// If timer is at 1, display different effect
					addEffect(new BombEffect(bomb.x, bomb.y, true));
				} else {
					// Otherwise, display regular bomb effect
					addEffect(new BombEffect(bomb.x, bomb.y, false));
				}
			}
		}
		
		// Remove detonated bombs from tracked list
		for (Triplet<Integer, Integer, Integer> deadBomb: bombsToRemove) {
			placedBombs.remove(deadBomb);
		}
		bombsToRemove = null;
	}

	// Chooses the next close-quarters melee attack for the General
	private void chooseCQMeleeAttack() {
		int whichAttack = new Random().nextInt(30);
		if (whichAttack <= dmgCount) {
			this.state = SnakeGeneral.STATE_PREP_RETREAT;
		} else if (whichAttack <= 18) {
			this.state = SnakeGeneral.STATE_PREP_COMBO;
		} else {
			this.state = SnakeGeneral.STATE_PREP_MINE;
		}
	}
	
	// Chooses the next ranged melee attack for the General
	private void chooseRangedMeleeAttack() {
		int whichAttack = new Random().nextInt(45);
		if (whichAttack <= dmgCount) {
			this.state = SnakeGeneral.STATE_PREP_RETREAT;
		} else if (whichAttack <= 15) {
			this.state = SnakeGeneral.STATE_PREP_COMBO;
		} else if (whichAttack <= 35) {
			// Play reving sound
			SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Rev.wav"), getXPos(), getYPos());
			this.state = SnakeGeneral.STATE_PREP_CHAINGUN;
		} else {
			this.state = SnakeGeneral.STATE_PREP_MINE;
		}
	}
	
	// Chooses the next special attack for the General
	private void chooseSpecialAttack() {
		// Check and reset special attack flags if all are hit
		checkSpcAttacks();
		
		// Create and populate temp attack list
		ArrayList<Integer> attacks = new ArrayList<Integer>();
		
		if (!didSpc0) {
			attacks.add(SnakeGeneral.STATE_SPC_ASSASSINATE);
		}
		
		if (!didSpc1) {
			attacks.add(SnakeGeneral.STATE_SPC_BLITZ);
		}
		
		if (!didSpc2) {
			attacks.add(SnakeGeneral.STATE_SPC_CANNON);
		}
		
		// Select the attack
		int whichAttack = new Random().nextInt(attacks.size());
		this.state = attacks.get(whichAttack);
		
		switch (state) {
			case SnakeGeneral.STATE_SPC_ASSASSINATE:
				this.didSpc0 = true;
				break;
			case SnakeGeneral.STATE_SPC_BLITZ:
				this.didSpc1 = true;
				break;
			case SnakeGeneral.STATE_SPC_CANNON:
				this.didSpc2 = true;
				break;
			default:
				System.out.println("Special attack not recognized: " + state);
				break;
		}
		
		// Clear temp attack list
		attacks.clear();
		attacks = null;
	}
	
	// Plays a hurt sound when the General takes damage TODO
	private void playHurt() {
//		Random r = new Random();
//		if (r.nextInt(4) == 0) {
//			int whichSound = r.nextInt(2);
//			if (whichSound == 0) {
//				SoundPlayer.playWAV(GPath.createSoundPath("king_hurt1.wav"));
//			} else {
//				SoundPlayer.playWAV(GPath.createSoundPath("king_hurt2.wav"));
//			}
//		}
	}
	
	// Choose random cannon fire sound to play
	private void playCannonSound() {
		// Decide sound to play
		int whichSound = (new Random().nextInt(4) + 1);
		
		// Play the sound
		SoundPlayer.playWAV(GPath.createSoundPath("CannonFire" + whichSound + ".wav"), -10f);
	}
	
	// Checks to see if each special attack has been used once.
	// If so, reset the flags and allow them to all be used again.
	private void checkSpcAttacks() {
		if (didAllSpcAttacks()) {
			resetSpcAttacks();
		}
	}
	
	private boolean didAllSpcAttacks() {
		return (didSpc0 && didSpc1 && didSpc2);
	}
	
	private void resetSpcAttacks() {
		this.didSpc0 = false;
		this.didSpc1 = false;
		this.didSpc2 = false;
	}
	
	// Shortening of adding effect for convenience and easy code reading
	private void addEffect(GEffect fx) {
		EntityManager.getInstance().getEffectManager().addEffect(fx);
	}
	
	// Shortening of adding projectile for convenience and easy code reading
	private void addProjectile(GProjectile proj) {
		EntityManager.getInstance().getProjectileManager().addProjectile(proj);
	}
	
}
