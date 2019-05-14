package characters.bosses;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PatrolPattern;
import characters.GCharacter;
import characters.Player;
import effects.BombEffect;
import effects.DamageIndicator;
import effects.FakeSnakeEffect;
import effects.FireEffect;
import effects.GEffect;
import effects.WarningIndicator;
import effects.WarpFizzleEffect;
import gui.GameScreen;
import gui.InfoScreen;
import gui.LogScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import helpers.Triplet;
import managers.EntityManager;
import projectiles.GProjectile;
import projectiles.SnakeCannonball;
import tiles.AltGround;
import tiles.MovableType;

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
	private static final int STATE_SET_BOMB = 9;
	private static final int STATE_WARP_AWAY = 10;
	private static final int STATE_SPC_ASSASSINATE = 11;
	private static final int STATE_SPC_ASSASSINATE_ATT = 12;
	private static final int STATE_SPC_BLITZ = 13;
	private static final int STATE_SPC_CANNON = 14;
	private static final int STATE_STUN = 15;
	
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
	
	// Order of rows to attack in Blitz special attack
	private int[] rowOrder = new int[] {2, 3, 4, 5};
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_COMMANDER);
	private String sgImage_base = "snakecommander";
	
	private String sgImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_COMMANDER, "snakecommander_dead.png");

	// Constructors
	public SnakeGeneral(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeGeneral.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "General Hanz";
	}
	
	// TODO
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.sgImage_base;
		String hpPath = "";
		String statePath = "";
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			return GPath.NULL;
		}
		
		switch(this.state) {
		case SnakeGeneral.STATE_IDLE:
		case SnakeGeneral.STATE_PURSUE:
			// No extra path
			break;
		case SnakeGeneral.STATE_ALERTED:
			statePath = "_ALERT";
			break;
		case SnakeGeneral.STATE_PREP_COMBO:
			statePath = "_PREP_BITE";
			break;
		case SnakeGeneral.STATE_ATT_COMBO:
			if(this.attCount % 2 == 0) {
				statePath = "_ATT_BITE";
			} else {
				statePath = "_ATT_SWIPE";
			}
			break;
		case SnakeGeneral.STATE_PREP_RETREAT:
			if(this.attCount <= 1) {
				statePath = "_PREP_FIRE";
			} else if (this.attCount % 2 == 1) {
				statePath = "_ATT_FIRE";
			} else {
				statePath = "_ATT_FIRE_ALT";
			}
			break;
		case SnakeGeneral.STATE_ATT_RETREAT:
			if (this.attCount % 2 == 1) {
				statePath = "_ATT_FIRE";
			} else {
				statePath = "_ATT_FIRE_ALT";
			}
			break;
		case SnakeGeneral.STATE_PREP_CHAINGUN:
			if(this.attCount == 0) {
				statePath = "_PREP_FIRE";
			} else {
				statePath = "_ATT_FIRE";
			}
			break;
		case SnakeGeneral.STATE_ATT_CHAINGUN:
			statePath = "_ATT_FIRE_ALT";
			break;
		case SnakeGeneral.STATE_SET_BOMB:
			statePath = "_ATT_SWIPE";
			break;
		case SnakeGeneral.STATE_WARP_AWAY:
			statePath = "_ATT_SWIPE_ALT";
			break;
		case SnakeGeneral.STATE_SPC_ASSASSINATE:
			if(this.attCount <= 4) {
				return GPath.NULL;
			} else {
				statePath = "_PREP_FIRE";
			}
			break;
		case SnakeGeneral.STATE_SPC_ASSASSINATE_ATT:
			statePath = "_ATT_FIRE_ALT";
			break;
		case SnakeGeneral.STATE_SPC_BLITZ:
			if(this.attCount <= 7) {
				return GPath.NULL;
			} else if(this.attCount == 8) {
				statePath = "_ATT_SWIPE";
			} else {
				// No extra path
			}
			break;
		case SnakeGeneral.STATE_SPC_CANNON:
			if(this.attCount <= 1) {
				return GPath.NULL;
			} else {
				statePath = "_ATT_FIRE";
			}
			break;
		case SnakeGeneral.STATE_STUN:
			statePath = "_PREP_SLAM";
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return this.sgImage_DEAD;
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
		
		// Clear any remaining bombs
		this.placedBombs.clear();
		
		// Open the doors to the arena
		GameScreen.getTile(4, 1).setTileType(new AltGround());
		GameScreen.getTile(5, 1).setTileType(new AltGround());
		
		// Change music to regular music
		SoundPlayer.changeMidi(GPath.createSoundPath("d_e2m1.mid"), 30);
		
		// Log a final death message
		LogScreen.log("The general and his tank lie defeated and broken...");
	}
	
	// Override that resets a few extra parameters
	@Override
	public void returnToOrigin() {
		super.returnToOrigin();
		this.resetFlags();
	}
	
	// Resets all flags/counters
	private void resetFlags() {
		this.attCount = 0;
		this.dmgCount = 0;
		this.breakRetreat = false;
		this.recentDmg = false;
		this.hitShots = false;
	}
	
	// Override that increments internal counter ands sets logic flags
	@Override
	public boolean damageCharacter(int damage) {
		this.currentHP = this.currentHP - damage;
		InfoScreen.setNPCFocus(this);
		
		// Increment damage counter and set that we've recently been damaged
		this.dmgCount += 1;
		this.recentDmg = true;
		
		// If below (5/6) health, go to phase 2
		if((!this.isPhase2) && (this.currentHP <= (this.maxHP*5/6))) {
			SoundPlayer.playWAV(GPath.createSoundPath("snake1_warn1.wav"));
			this.isPhase2 = true;
		} else {
			this.playHurt();
		}
		
		return this.isAlive();
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
		
		// Relative movement direction (Initialize at 0)
		int dx = 0;
		int dy = 0;
		
		// Manage the bombs
		this.manageBombs(plrX, plrY);
		
		switch(this.state) {
			case SnakeGeneral.STATE_IDLE: //------------------------------------------------------------
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					Random r = new Random();
					int whichSound = r.nextInt(2);
					if(whichSound == 0) {
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
			case SnakeGeneral.STATE_PURSUE:	 //------------------------------------------------------------
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
				
				// If we're in phase 2 and have recently warped, try special attack
				if(this.isPhase2 && this.recentRetreat) {
					// Check if we should do special attack
					// Probability increases as turns pass without special attack
					Random r = new Random();
					int shouldSpecial = r.nextInt(SPC_MAX);
					if(shouldSpecial < this.spcCount) {
						// Reset the counters/flags
						this.recentRetreat = false;
						this.spcCount = 0;
						
						// Set state to warp away
						this.state = SnakeGeneral.STATE_WARP_AWAY;
						break;
					}
				}
				
				// Change state to prepare a stab/swipe 100% of the time if next to player
				if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;

					// Choose a close-quarters melee-based attack
					this.chooseCQMeleeAttack();
					
					// Else, try to move x-wise or y-wise closer to the player
					// based on which dimension you are further distance from them
				} else {
					// Blindly pursue player (Square arena; No need for path-finding)
					DumbFollow.blindPursue(distX, distY, dx, dy, this);
					
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
					
					// Decide if snake should attempt a moving melee attack
					// Punishes running away and eager approaches
					// Attempt 2/3 of the time
					int shouldMelee = new Random().nextInt(3);
					if((shouldMelee <= 1) && (((Math.abs(distX) <= 3) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 3)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
						if(hasAttLOS) {
							this.xMarkDir = dx;
							this.yMarkDir = dy;
							
							// Choose a ranged melee-based attack
							this.chooseRangedMeleeAttack();
						}
					}
				}
				
				// Reset recently retreated flag
				this.recentRetreat = false;
				break;
			case SnakeGeneral.STATE_PREP_COMBO: //------------------------------------------------------------
				// If player is directly diagonal from General, don't lunge forward
				if(!((Math.abs(distX) == 1) && (Math.abs(distY) == 1))) {
					// Move General in marked direction
					this.moveCharacter(this.xMarkDir, this.yMarkDir);
				}
				
				// Use direction from player to mark squares
				SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
				this.addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
				this.addEffect(new DamageIndicator(this.xPos + (this.xMarkDir*2), this.yPos + (this.yMarkDir*2)));
				
				// Hit player in affected spaces
				if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir) ||
						(plrX == this.xPos + (this.xMarkDir*2) && plrY == this.yPos + (this.yMarkDir*2))) {
					this.playerInitiate();
				}
				
				// Change state and increment attack counter
				this.state = SnakeGeneral.STATE_ATT_COMBO;
				break;
			case SnakeGeneral.STATE_ATT_COMBO: //------------------------------------------------------------
				// Attack a specified number of times (swipeMaxCount)
				if(this.attCount >= this.swipeMaxCount) {
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
				distX = plrX - this.xPos;
				distY = plrY - this.yPos;
				
				// Do side-swipe attack towards direction player moved
				SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
				if(this.xMarkDir != 0) {
					// Recalculate Y attack direction
					if(distY > 0) {
						this.yMarkDir = 1;
					} else if (distY < 0) {
						this.yMarkDir = -1;
					} else {
						this.yMarkDir = 1 - (2 * new Random().nextInt(2));
					}
					
					// Player to left/right
					this.addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos));
					this.addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
					
					// Attack player if in affected space
					if((plrX == this.xPos + this.xMarkDir) &&
							(plrY == this.yPos || plrY == this.yPos + this.yMarkDir)) {
						this.playerInitiate();
					}
				} else {
					// Recalculate X attack direction
					if(distX > 0) {
						this.xMarkDir = 1;
					} else if (distX < 0) {
						this.xMarkDir = -1;
					} else {
						this.xMarkDir = 1 - (2 * new Random().nextInt(2));
					}
					
					// Player above/below
					this.addEffect(new DamageIndicator(this.xPos, this.yPos + this.yMarkDir));
					this.addEffect(new DamageIndicator(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
					
					// Attack player if in affected space
					if((plrY == this.yPos + this.yMarkDir) &&
							(plrX == this.xPos || plrX == this.xPos + this.xMarkDir)) {
						this.playerInitiate();
					}
				}
				
				break;
			case SnakeGeneral.STATE_PREP_RETREAT: //------------------------------------------------------------
				if(this.attCount == 0) {
					// Do nothing for first turn
				} else {
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
					
					// Move away from player, checking for whether we actually moved or not
					// If we moved, swing in the opposite direction
					int tempX = this.xPos;
					int tempY = this.yPos;
					DumbFollow.blindPursue(distX, distY, -dx, -dy, this);
					if(tempX == this.xPos && tempY == this.yPos) {
						this.breakRetreat = true;
					} else {
						this.xMarkDir = tempX - this.xPos;
						this.yMarkDir = tempY - this.yPos;
					}
					
					// Play sound if first turn retreating
					if(this.attCount == 1) {
						SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"));
					}
					
					// Use direction from player to mark squares
					if(Math.abs(this.xMarkDir) > Math.abs(this.yMarkDir)) {
						// Player to left/right
						this.addEffect(new FireEffect(this.xPos + this.xMarkDir, this.yPos));
						this.addEffect(new FireEffect(this.xPos + this.xMarkDir, this.yPos + 1));
						this.addEffect(new FireEffect(this.xPos + this.xMarkDir, this.yPos - 1));
						this.addEffect(new FireEffect(this.xPos + (this.xMarkDir*2), this.yPos));
						this.addEffect(new FireEffect(this.xPos + (this.xMarkDir*2), this.yPos + 1));
						this.addEffect(new FireEffect(this.xPos + (this.xMarkDir*2), this.yPos - 1));
						
						// Attack player if in affected space
						if(((plrX == this.xPos + this.xMarkDir) || (plrX == this.xPos + (this.xMarkDir*2))) &&
								(plrY == this.yPos || plrY == this.yPos - 1 || plrY == this.yPos + 1)) {
							this.playerInitiate();
						}
					} else {
						// Player above/below
						this.addEffect(new FireEffect(this.xPos, this.yPos + this.yMarkDir));
						this.addEffect(new FireEffect(this.xPos + 1, this.yPos + this.yMarkDir));
						this.addEffect(new FireEffect(this.xPos - 1, this.yPos + this.yMarkDir));
						this.addEffect(new FireEffect(this.xPos, this.yPos + (this.yMarkDir*2)));
						this.addEffect(new FireEffect(this.xPos + 1, this.yPos + (this.yMarkDir*2)));
						this.addEffect(new FireEffect(this.xPos - 1, this.yPos + (this.yMarkDir*2)));
						
						// Attack player if in affected space
						if(((plrY == this.yPos + this.yMarkDir) || (plrY == this.yPos + (this.yMarkDir*2))) &&
								(plrX == this.xPos || plrX == this.xPos - 1 || plrX == this.xPos + 1)) {
							this.playerInitiate();
						}
					}
					
					// Check to break out
					if(this.breakRetreat) {
						// If breaking out, reset break flag and change state
						this.breakRetreat = false;
						this.state = SnakeGeneral.STATE_ATT_RETREAT;
					}
				}
				
				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeGeneral.STATE_ATT_RETREAT: //------------------------------------------------------------
				// Reset counters and change state
				this.recentRetreat = true;
				this.attCount = 0;
				this.dmgCount = 0;
				this.state = SnakeGeneral.STATE_PURSUE;
				break;
			case SnakeGeneral.STATE_PREP_CHAINGUN: //------------------------------------------------------------
				if(this.attCount > 0) {
					// Try to move at the player if we didn't hit chaingun shots
					if(!this.hitShots) {
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
						
						// Check which direction to move
						if(this.xMarkDir != 0) {
							// Move vertically towards player
							this.moveCharacter(0, dy);
						} else {
							// Move horizontally towards player
							this.moveCharacter(dx, 0);
						}
					}
					
					// Change state for next turn
					this.state = SnakeGeneral.STATE_ATT_CHAINGUN;
				}
				
				// Play firing sound
				SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Fire.wav"));

				// Safety check to prevent infinite loop
				if(this.xMarkDir == 0 && this.yMarkDir == 0) {
					this.xMarkDir = 1;
				}
				
				// Fire at player
				int nextX = (this.getXPos() + this.xMarkDir);
				int nextY = (this.getYPos() + this.yMarkDir);
				boolean isEndHit = false;
				while(!isEndHit) { // BEGIN While ------------------------

					// Mark the tile
					this.addEffect(new DamageIndicator(nextX, nextY));
					
					// Check if we hit player
					if(nextX == plrX && nextY == plrY) {
						this.hitShots = true;
						this.playerInitiate();
					}
					
					// Check for walls or OOB to see if we need to keep checking tiles
					try {
						isEndHit = GameScreen
								.getTile(nextX, nextY)
								.getTileType()
								.getMovableType() == MovableType.WALL;
					} catch (ArrayIndexOutOfBoundsException e) {
						isEndHit = true;
					}
					
					// Then update the next coordinates to check
					nextX += this.xMarkDir;
					nextY += this.yMarkDir;
					
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
			case SnakeGeneral.STATE_SET_BOMB: //------------------------------------------------------------
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
				
				// Move away from player
				DumbFollow.blindPursue(distX, distY, -dx, -dy, this);
				
				// Lay down bomb
				this.addEffect(new BombEffect(this.xPos + dx, this.yPos + dy, false));
				this.placedBombs.add(new Triplet<Integer, Integer, Integer>(this.xPos + dx, this.yPos + dy, 5));

				// Switch back to pursuit
				this.state = SnakeGeneral.STATE_PURSUE;
				break;
			case SnakeGeneral.STATE_WARP_AWAY: //------------------------------------------------------------
				// Warp away to top-right corner (Outside of arena)
				this.xPos = 9;
				this.yPos = 0;
				
				// Decide which special attack to use
				this.chooseSpecialAttack();
				break;
			case SnakeGeneral.STATE_SPC_ASSASSINATE: //------------------------------------------------------------
				if(this.attCount == 0) {
					// Decide which direction to attack
					Random r = new Random();
					int whichDir = r.nextInt(4);
					if(whichDir == 0) {
						// Choose Up
						this.xMarkDir = 0;
						this.yMarkDir = -1;
					} else if(whichDir == 1) {
						// Choose Right
						this.xMarkDir = 1;
						this.yMarkDir = 0;
					} else if(whichDir == 2) {
						// Choose Down
						this.xMarkDir = 0;
						this.yMarkDir = 1;
					} else {
						// Choose Left
						this.xMarkDir = -1;
						this.yMarkDir = 0;
					}
				} else if(this.attCount <= 3) {
					
					// Show illusion images around the player
					this.addEffect(new WarpFizzleEffect(plrX, plrY - 1));
					this.addEffect(new WarpFizzleEffect(plrX + 1, plrY));
					this.addEffect(new WarpFizzleEffect(plrX, plrY + 1));
					this.addEffect(new WarpFizzleEffect(plrX - 1, plrY));
					
				} else if(this.attCount == 4) {
					// Warp next to player
					this.xPos = plrX + this.xMarkDir;
					this.yPos = plrY + this.yMarkDir;
					
					// Display fakes in other directions (if general is not there)
					
					// Up direction
					if(!((plrX == this.xPos) && (plrY - 1 == this.yPos))) {
						this.addEffect(new FakeSnakeEffect(plrX, plrY - 1));
					}
					
					// Right direction
					if(!((plrX + 1 == this.xPos) && (plrY == this.yPos))) {
						this.addEffect(new FakeSnakeEffect(plrX + 1, plrY));
					}
					
					// Down direction
					if(!((plrX == this.xPos) && (plrY + 1 == this.yPos))) {
						this.addEffect(new FakeSnakeEffect(plrX, plrY + 1));
					}
					
					// Left direction
					if(!((plrX - 1 == this.xPos) && (plrY == this.yPos))) {
						this.addEffect(new FakeSnakeEffect(plrX - 1, plrY));
					}
					
				} else {
					// Reset attack count
					this.attCount = 0;
					
					// Check if we've taken damage recently
					if(this.recentDmg) {
						// Stun the general and break
						this.stunGeneral();
						break;
					} else {
						// Correct attacking direction
						this.xMarkDir = this.xMarkDir * -1;
						this.yMarkDir = this.yMarkDir * -1;
						
						// Mark Tiles for damage
						this.addEffect(new FireEffect(this.xPos + this.xMarkDir, this.yPos + this.yMarkDir));
						this.addEffect(new FireEffect(this.xPos + (this.xMarkDir*2), this.yPos + (this.yMarkDir*2)));
						this.addEffect(new FireEffect(this.xPos + this.xMarkDir - (this.yMarkDir), this.yPos + this.yMarkDir - (this.xMarkDir)));
						this.addEffect(new FireEffect(this.xPos + this.xMarkDir + (this.yMarkDir), this.yPos + this.yMarkDir + (this.xMarkDir)));
						
						// Attack player if in affected space
						if((plrX == this.xPos + this.xMarkDir && plrY == this.yPos + this.yMarkDir) ||
								(plrX == this.xPos + (this.xMarkDir*2) && plrY == this.yPos + (this.yMarkDir*2)) ||
								(plrX == this.xPos + this.xMarkDir - (this.yMarkDir) && plrY == this.yPos + this.yMarkDir - (this.xMarkDir)) ||
								(plrX == this.xPos + this.xMarkDir + (this.yMarkDir) && plrY == this.yPos + this.yMarkDir + (this.xMarkDir))) {
							this.playerInitiate();
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
					inWallAtt = GameScreen
							.getTile(this.xPos, this.yPos)
							.getTileType()
							.getMovableType() == MovableType.WALL;
				} catch (ArrayIndexOutOfBoundsException e) {
					inWallAtt = true;
				}
				
				if(inWallAtt) {
					this.warpGeneral(plrX, plrY);
				}
				
				// Reset attack/special counter and change state
				this.attCount = 0;
				this.spcCount = 0;
				this.state = SnakeGeneral.STATE_PURSUE;
				break;
			case SnakeGeneral.STATE_SPC_BLITZ: //------------------------------------------------------------
				if(this.attCount == 0) {
					// Determine attack order
					Random r = new Random();
					for(int i = 3; i > 0; i--) {
						// Randomize swap index
						int shuffCoord = r.nextInt(i + 1);
						
						// Swap the spots
						int temp = rowOrder[i];
						rowOrder[i] = rowOrder[shuffCoord];
						rowOrder[shuffCoord] = temp;
					}
				}
				
				if(this.attCount <= 3) {
					// Flash warning across attacking rows
					for(int i = 4; i <= 8; i++) {
						this.addEffect(new WarningIndicator(i, this.rowOrder[this.attCount]));
					}
				} else if(this.attCount <= 7) {
					// Attack the rows in order they were previously warned
					
					// Put attack effects and check to damage player
					SoundPlayer.playWAV(GPath.createSoundPath("swing_ATT.wav"));
					for(int i = 4; i <= 8; i++) {
						this.addEffect(new DamageIndicator(i, this.rowOrder[(this.attCount-4)]));
						if(plrX == i && plrY == this.rowOrder[(this.attCount-4)]) {
							this.playerInitiate();
						}
					}
					
					// Display the General attacking the current row
					if(this.attCount == 7) {
						// Warp real General to last attack location (Right side)
						this.xPos = 9;
						this.yPos = this.rowOrder[(this.attCount-4)];
					} else {
						// Display a fake General at the location
						if(this.attCount % 2 == 0) {
							// Left side
							this.addEffect(new FakeSnakeEffect(3, this.rowOrder[(this.attCount-4)]));
						} else {
							// Right side
							this.addEffect(new FakeSnakeEffect(9, this.rowOrder[(this.attCount-4)]));
						}
					}
					
				} else if(this.attCount <= 10) {
					// Cooldown for three turns
					
					// If we're hit during cooldown, enter stun
					if(this.recentDmg) {
						// Reset attack/special count
						this.attCount = 0;
						this.spcCount = 0;
						
						// Stun the general and break
						this.stunGeneral();
						break;
					}
					
				} else {
					// Reset attack/special count
					this.attCount = 0;
					this.spcCount = 0;
					
					// If we're hit before warp, enter stun
					if(this.recentDmg) {
						// Stun the general and break
						this.stunGeneral();
						break;
					} else {
						// Warp General back to arena
						this.warpGeneral(plrX, plrY);
	
						// Change state to pursue
						this.state = SnakeGeneral.STATE_PURSUE;
						break;	
					}
				}

				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeGeneral.STATE_SPC_CANNON: //------------------------------------------------------------
				if(this.attCount == 0) {
					// Determine attack directions
					Random r = new Random();
					for(int i = 3; i > 0; i--) {
						// Randomize swap index
						int shuffCoord = r.nextInt(i + 1);
						
						// Swap the spots
						int temp = rowOrder[i];
						rowOrder[i] = rowOrder[shuffCoord];
						rowOrder[shuffCoord] = temp;
					}
					
					// Mark the spots for warning
					this.addEffect(new WarningIndicator(4, this.rowOrder[0]));
					this.addEffect(new WarningIndicator(4, this.rowOrder[1]));
					this.addEffect(new WarningIndicator(8, this.rowOrder[2]));
					this.addEffect(new WarningIndicator(8, this.rowOrder[3]));
					
					// Show General warping in
					this.addEffect(new WarpFizzleEffect(3, this.rowOrder[0]));
					this.addEffect(new WarpFizzleEffect(3, this.rowOrder[1]));
					this.addEffect(new WarpFizzleEffect(9, this.rowOrder[2]));
					this.addEffect(new WarpFizzleEffect(9, this.rowOrder[3]));
				} else if(this.attCount <= 8) {
					
					// Warp in on the first turn of attack
					if(this.attCount == 1) {
						int whichPos = new Random().nextInt(4);
						if(whichPos <= 1) {
							// Left positions
							this.xPos = 3;
							this.yPos = this.rowOrder[whichPos];
						} else {
							// Right positions
							this.xPos = 9;
							this.yPos = this.rowOrder[whichPos];
						}
					}
					
					// If we're hit during cannon firing, enter stun
					if(this.recentDmg) {
						// Reset attack/special count
						this.attCount = 0;
						this.spcCount = 0;
						
						// Stun the general and break
						this.stunGeneral();
						break;
					}
					
					// Mark other firing spots with Fake Generals
					// First row (Left)
					if(!((this.xPos == 3) && (this.yPos == this.rowOrder[0]))) {
						this.addEffect(new FakeSnakeEffect(3, this.rowOrder[0]));
					}
					
					// Second row (Left)
					if(!((this.xPos == 3) && (this.yPos == this.rowOrder[1]))) {
						this.addEffect(new FakeSnakeEffect(3, this.rowOrder[1]));
					}
					
					// Third row (Right)
					if(!((this.xPos == 9) && (this.yPos == this.rowOrder[2]))) {
						this.addEffect(new FakeSnakeEffect(9, this.rowOrder[2]));
					}
					
					// Fourth row (Right)
					if(!((this.xPos == 9) && (this.yPos == this.rowOrder[3]))) {
						this.addEffect(new FakeSnakeEffect(9, this.rowOrder[3]));
					}
					
					// Play cannon sound
					this.playCannonSound();
					
					// Alternate cannon-firing side
					if(this.attCount % 2 == 1) {
						// Fire left cannons for the turn
						this.addProjectile(new SnakeCannonball(4, this.rowOrder[0], 1, 0, this));
						this.addProjectile(new SnakeCannonball(4, this.rowOrder[1], 1, 0, this));
						this.addEffect(new DamageIndicator(4, this.rowOrder[0]));
						this.addEffect(new DamageIndicator(4, this.rowOrder[1]));
					} else {
						// Fire right cannons for the turn
						this.addProjectile(new SnakeCannonball(8, this.rowOrder[2], -1, 0, this));
						this.addProjectile(new SnakeCannonball(8, this.rowOrder[3], -1, 0, this));
						this.addEffect(new DamageIndicator(8, this.rowOrder[2]));
						this.addEffect(new DamageIndicator(8, this.rowOrder[3]));
					}
				} else if(this.attCount <= 11) { 
					// Cooldown for three turns
					
					// If we're hit during cooldown, enter stun
					if(this.recentDmg) {
						// Reset attack/special count
						this.attCount = 0;
						this.spcCount = 0;
						
						// Stun the general and break
						this.stunGeneral();
						break;
					}
				} else {

				// Reset attack/special count
				this.attCount = 0;
				this.spcCount = 0;
				
				// If we're hit before warp, enter stun
				if(this.recentDmg) {
					// Stun the general and break
					this.stunGeneral();
					break;
				} else {
					// Warp General back to arena
					this.warpGeneral(plrX, plrY);

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
				if(this.attCount <= 2) {
					// Do nothing
					this.attCount += 1;
				} else {
					// Warp to a corner in the arena if we're inside a wall
					boolean inWallStn = false;
					try {
						inWallStn = GameScreen
								.getTile(this.xPos, this.yPos)
								.getTileType()
								.getMovableType() == MovableType.WALL;
					} catch (ArrayIndexOutOfBoundsException e) {
						inWallStn = true;
					}
					
					if(inWallStn) {
						this.warpGeneral(plrX, plrY);
					}
					
					// Reset attack/special counter and change state
					this.attCount = 0;
					this.spcCount = 0;
					this.state = SnakeGeneral.STATE_PURSUE;
				}
				break;
			default: //---------------------------------------------------------------------------------
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
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
		for(Dimension d: this.corners) {
			int xDist = Math.abs(d.width - plrX);
			int yDist = Math.abs(d.height - plrY);
			if(bestTotal < (xDist + yDist)) {
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
		SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"));
		
		// Change state to stunned
		this.state = SnakeGeneral.STATE_STUN;
	}
	
	// Manage the bombs we have on screen (if there are any)
	private void manageBombs(int plrX, int plrY) {
		// Check if we have any bombs to manage
		if(this.placedBombs.size() <= 0) {
			// If no bombs, do nothing
			return;
		}
		
		ArrayList<Triplet<Integer, Integer, Integer>> bombsToRemove = new ArrayList<Triplet<Integer, Integer, Integer>>();
		for(Triplet<Integer, Integer, Integer> bomb: this.placedBombs) {
			// Tick down the bomb's timer by 1
			bomb.z += -1;
			
			// Check bomb's timer
			if(bomb.z <= 0) {
				// Detonate bomb if timer at 0
				this.addEffect(new FireEffect(bomb.x, bomb.y));				// Center
				this.addEffect(new FireEffect(bomb.x + 1, bomb.y - 1));		// Top-right
				this.addEffect(new FireEffect(bomb.x + 1, bomb.y + 1));		// Bottom-right
				this.addEffect(new FireEffect(bomb.x - 1, bomb.y - 1));		// Top-left
				this.addEffect(new FireEffect(bomb.x - 1, bomb.y + 1));		// Bottom-left
				
				// Play bomb detonation
				SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"));
				
				// Check if bomb hit the player
				if((plrX == bomb.x && plrY == bomb.y) ||
						((plrX == bomb.x + 1) && (plrY == bomb.y + 1 || plrY == bomb.y - 1)) ||
						((plrX == bomb.x - 1) && (plrY == bomb.y + 1 || plrY == bomb.y - 1))) {
					this.playerInitiate();
				}
				
				// Add bomb to clean-up list
				bombsToRemove.add(bomb);
			} else {
				// Continue to display bomb effect
				if(bomb.z == 1) {
					// If timer is at 1, display different effect
					this.addEffect(new BombEffect(bomb.x, bomb.y, true));
				} else {
					// Otherwise, display regular bomb effect
					this.addEffect(new BombEffect(bomb.x, bomb.y, false));
				}
			}
		}
		
		// Remove detonated bombs from tracked list
		for(Triplet<Integer, Integer, Integer> deadBomb: bombsToRemove) {
			this.placedBombs.remove(deadBomb);
		}
		bombsToRemove = null;
	}

	// Chooses the next close-quarters melee attack for the General
	private void chooseCQMeleeAttack() {
		int whichAttack = new Random().nextInt(18);
		if(whichAttack <= this.dmgCount) {
			this.state = SnakeGeneral.STATE_PREP_RETREAT;
		} else if(whichAttack <= 12) {
			this.state = SnakeGeneral.STATE_PREP_COMBO;
		} else {
			this.state = SnakeGeneral.STATE_SET_BOMB;
		}
	}
	
	// Chooses the next ranged melee attack for the General
	private void chooseRangedMeleeAttack() {
		int whichAttack = new Random().nextInt(45);
		if(whichAttack <= this.dmgCount) {
			this.state = SnakeGeneral.STATE_PREP_RETREAT;
		} else if(whichAttack <= 15) {
			this.state = SnakeGeneral.STATE_PREP_COMBO;
		} else if(whichAttack <= 30) {
			// Play reving sound
			SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Rev.wav"));
			this.state = SnakeGeneral.STATE_PREP_CHAINGUN;
		} else {
			this.state = SnakeGeneral.STATE_SET_BOMB;
		}
	}
	
	// Chooses the next special attack for the General
	private void chooseSpecialAttack() {
		int whichAttack = new Random().nextInt(3);
		if(whichAttack == 0) {
			this.state = SnakeGeneral.STATE_SPC_ASSASSINATE;
		} else if(whichAttack == 1) {
			this.state = SnakeGeneral.STATE_SPC_BLITZ;
		} else {
			this.state = SnakeGeneral.STATE_SPC_CANNON;
		}
	}
	
	// Plays a hurt sound when the General takes damage TODO
	private void playHurt() {
//		Random r = new Random();
//		if(r.nextInt(4) == 0) {
//			int whichSound = r.nextInt(2);
//			if(whichSound == 0) {
//				SoundPlayer.playWAV(GPath.createSoundPath("king_hurt1.wav"));
//			} else {
//				SoundPlayer.playWAV(GPath.createSoundPath("king_hurt2.wav"));
//			}
//		}
	}
	
	// Choose random cannon fire sound to play
	private void playCannonSound() {
		// Decide sound to play
		Random r = new Random();
		int whichSound = r.nextInt(4);
		
		// Play the sound
		switch(whichSound) {
			case 0:
				SoundPlayer.playWAV(GPath.createSoundPath("CannonFire1.wav"));
				break;
			case 1:
				SoundPlayer.playWAV(GPath.createSoundPath("CannonFire2.wav"));
				break;
			case 2:
				SoundPlayer.playWAV(GPath.createSoundPath("CannonFire3.wav"));
				break;
			case 3:
				SoundPlayer.playWAV(GPath.createSoundPath("CannonFire4.wav"));
				break;
			default:
				SoundPlayer.playWAV(GPath.createSoundPath("CannonFire1.wav"));
				break;
		}
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
