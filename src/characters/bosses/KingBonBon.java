package characters.bosses;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.GCharacter;
import characters.allies.Player;
import effects.DamageIndicator;
import effects.FireEffect;
import effects.FloodEffect;
import effects.GEffect;
import effects.RubbleEffect;
import effects.SmallFireEffect;
import effects.ThunderEffect;
import effects.WarningIndicator;
import gui.GameScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.GProjectile;
import projectiles.KingFireball;
import tiles.MovableType;
import tiles.Wall;

/**
 * Class representing the King Bon Bon boss enemy
 * @author jeoliva
 */
public class KingBonBon extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -8322449286398198402L;
	
	// Modifiers/Statistics

	private int MAX_HP = 100;
	
	private int MIN_DMG = 3;
	private int MAX_DMG = 4;
	
	private double CRIT_CHANCE = 0.15;
	private double CRIT_MULT = 1.25;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_STAB = 3;
	private static final int STATE_ATT_STAB = 4;
	private static final int STATE_PREP_SWIPE = 5;
	private static final int STATE_ATT_SWIPE = 6;
	private static final int STATE_PREP_SLAM = 7;
	private static final int STATE_PREP_FIRE = 8;
	private static final int STATE_ATT_FIRE = 9;
	private static final int STATE_PREP_THUNDER = 10;
	private static final int STATE_ATT_THUNDER = 11;
	private static final int STATE_PREP_FLOOD = 12;
	private static final int STATE_ATT_FLOOD = 13;
	
	
	//----------------------------
	// Additional Behavior
	
	// Direction the screen will be marked up for damage indicators
	private int xMarkDir = 0;
	private int yMarkDir = 0;
	
	// Internal count on how long certain attacks/windups/cooldowns have lasted
	private int attCount = 0;
	
	// Internal count of how many times King Bon Bon has been damaged
	private int dmgCount = 0;
	
	// Turns since last special attack
	private int spcCount = 0;
	
	// Flags telling the snake whether we did certain special attacks
	private boolean didSpc0 = false;
	private boolean didSpc1 = false;
	private boolean didSpc2 = false;
	
	// Boolean indicating whether we were damaged last turn
	private boolean recentDmg = false;
	
	// Boolean whether King should warp at the next opportunity
	private boolean warpFlag = false;
	
	// Boolean indicating if the King just warped
	private boolean recentWarp = false;
	
	// Boolean indicating whether we've enter phase 2
	private boolean isPhase2 = false;
	
	// Variables to manage thunder attack
	private int thunderRow = 2;
	private int thunderCount = 0;
	private boolean thunderActive = false;
	
	// Corners to warp to
	private Dimension[] corners = new Dimension[] {
			new Dimension(1, 2),
			new Dimension(8, 2),
			new Dimension(1, 7),
			new Dimension(8, 7)
	};
	
	// Places to spawn fireballs
	private Dimension[] fireCoords = new Dimension[] {
			new Dimension(1, 2),
			new Dimension(2, 2),
			new Dimension(1, 4),
			new Dimension(2, 4),
			new Dimension(1, 6),
			new Dimension(2, 6),
			new Dimension(8, 3),
			new Dimension(7, 3),
			new Dimension(8, 5),
			new Dimension(7, 5),
			new Dimension(8, 7),
			new Dimension(7, 7)
	};
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON);
	private String bbImage_base = "kingbonbon";
	
	private String bbImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "kingbonbon_dead.png");
	
	public KingBonBon(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = KingBonBon.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
	}

	@Override
	public String getName() {
		return "King Bon Bon";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + bbImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (currentHP > 0) {
			hpPath = "_full";
		} else {
			hpPath = "_dead";
			return GPath.NULL;
		}
		
		switch (state) {
			case KingBonBon.STATE_IDLE:
			case KingBonBon.STATE_PURSUE:
				// No extra path
				break;
			case KingBonBon.STATE_PREP_STAB:
				if (attCount < 1) {
					statePath = "_PREP_STAB";
				} else {
					statePath = "_ALERT";
				}
				break;
			case KingBonBon.STATE_ALERTED:
				statePath = "_PREP_STAB";
				break;
			case KingBonBon.STATE_ATT_STAB:
				statePath = "_ALERT_ALT";
				break;
			case KingBonBon.STATE_PREP_SWIPE:
				if (attCount < 1) {
					statePath = "_PREP_SWING";
				} else {
					statePath = "_ATT_SWING";
				}
				break;
			case KingBonBon.STATE_ATT_SWIPE:
				statePath = "_ATT_SWING_ALT";
				break;
			case KingBonBon.STATE_PREP_SLAM:
				if (attCount <= 2) {
					statePath = "_PREP_SLAM";
				} else {
					statePath = "_ATT_SLAM";
				}
				break;
			case KingBonBon.STATE_PREP_THUNDER:
				statePath = "_PREP_THUNDER";
				break;
			case KingBonBon.STATE_PREP_FIRE:
				statePath = "_PREP_FIRE";
				break;
			case KingBonBon.STATE_PREP_FLOOD:
				statePath = "_PREP_FLOOD";
				break;
			case KingBonBon.STATE_ATT_THUNDER:
				statePath = "_ATT_THUNDER";
				break;
			case KingBonBon.STATE_ATT_FIRE:
				statePath = "_ATT_FIRE";
				break;
			case KingBonBon.STATE_ATT_FLOOD:
				statePath = "_ATT_FLOOD";
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}

	@Override
	public String getCorpseImage() {
		return this.bbImage_DEAD;
	}
	
	@Override
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
		// Play 'death' sound
		SoundPlayer.playWAV(GPath.createSoundPath("king_death.wav"));
		
		// Spawn the King's Head
		EntityManager.getInstance().getNPCManager().addCharacter(new KingsHead(xPos, yPos));
	}
	
	// Override that increments internal counter ands sets logic flags
	@Override
	public boolean damageCharacter(int damage) {
		boolean result = super.damageCharacter(damage);
		
		// Increment damage counter and set that we've recently been damaged
		this.dmgCount += 1;
		this.recentDmg = true;
		
		// If below 80% health, go to phase 2
		if ((!isPhase2) && (currentHP < (maxHP*4/5))) {
			this.isPhase2 = true;
			SoundPlayer.playWAV(GPath.createSoundPath("king_phase2.wav"));
		} else {
			// Play hurt sound
			playHurt();
		}
		return result;
	}
	
	@Override
	public void takeTurn() {
		// Fetch reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// Random number generator
		Random r = new Random();
		
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
		
		// Calculate whether a warp should be done at the next opportunity
		int shouldWarp = r.nextInt(5) + 1;
		if (recentDmg && (shouldWarp < dmgCount)) {
			this.warpFlag = true;
		}
		
		// Manage the thunder attack
		manageThunder(plrX, plrY);
		
		switch (state) {
			case KingBonBon.STATE_IDLE: //------------------------------------------------------------
				// If player enters the arena, alert and close the arena
				if (plrY <= 7) {		
					// Close the doors to the arena
					GameScreen.getTile(4, 8).setTileType(new Wall());
					GameScreen.getTile(5, 8).setTileType(new Wall());
					GameScreen.getTile(4, 1).setTileType(new Wall());
					GameScreen.getTile(5, 1).setTileType(new Wall());
					
					// Change music to Boss music
					SoundPlayer.changeMidi(GPath.createSoundPath("d_e2m8.mid"));
					
					// Play greeting
					SoundPlayer.playWAV(GPath.createSoundPath("king_greeting.wav"));
					
					// Change state to alerted
					this.state = KingBonBon.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case KingBonBon.STATE_ALERTED: //------------------------------------------------------------
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = KingBonBon.STATE_PURSUE;
				break;
			case KingBonBon.STATE_PURSUE: //------------------------------------------------------------
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
				
				// Check if we're pending a warp
				if (warpFlag) {
					// Do a warp
					warpKing(plrX, plrY);
					
					// End the turn
					resetFlags();
					incrementCounters();
					this.recentWarp = true;
					return;
				}
				
				// If we're in phase 2 and have recently warped, try special attack
				if (isPhase2 && recentWarp) {
					// Check if we should do special attack
					// Probability increases as turns pass without special attack
					int shouldSpecial = r.nextInt(40);
					if (shouldSpecial < spcCount) {
						// Reset the counters
						resetFlags();
						incrementCounters();
						this.spcCount = 0;
						
						// Decide on which special attack to use
						chooseSpecialAttack();
						return;
					}
				}
				
				// If next to the player, decide an appropriate course of action
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;
					
					// Decide whether to swipe, stab, or slam
					int whichAttack = r.nextInt(16);
					if (whichAttack < 6) {
						this.state = KingBonBon.STATE_PREP_STAB;
					} else if(whichAttack < 12) {
						// Add swipe warning effects
						addEffect(new SmallFireEffect(xPos + xMarkDir, yPos + yMarkDir));
						addEffect(new SmallFireEffect(xPos + xMarkDir + Math.abs(yMarkDir), yPos + yMarkDir + Math.abs(xMarkDir)));
						addEffect(new SmallFireEffect(xPos + xMarkDir - Math.abs(yMarkDir), yPos + yMarkDir - Math.abs(xMarkDir)));
						this.state = KingBonBon.STATE_PREP_SWIPE;
					} else {
						this.state = KingBonBon.STATE_PREP_SLAM;
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
					// Attempt only 1/5 of the time
					int shouldAttack = r.nextInt(10);
					if ((shouldAttack < 3) && (((Math.abs(distX) <= 4) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 4)))) {
						// Cue the running stab
						this.xMarkDir = dx;
						this.yMarkDir = dy;
						this.state = KingBonBon.STATE_PREP_STAB;
					} else if ((shouldAttack < 5) && (((Math.abs(distX) <= 2) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 2)))) {
						// If not doing running stab, try for a running swipe if we're close enough
						// Attempt only 1/5 of the time.
						this.xMarkDir = dx;
						this.yMarkDir = dy;
						
						// Add swipe warning effects
						addEffect(new SmallFireEffect(xPos + xMarkDir, yPos + yMarkDir));
						addEffect(new SmallFireEffect(xPos + xMarkDir + Math.abs(yMarkDir), yPos + yMarkDir + Math.abs(xMarkDir)));
						addEffect(new SmallFireEffect(xPos + xMarkDir - Math.abs(yMarkDir), yPos + yMarkDir - Math.abs(xMarkDir)));
						
						this.state = KingBonBon.STATE_PREP_SWIPE;
					}  else if ((shouldAttack < 7) && (((Math.abs(distX) <= 2) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 2)))) {
						// If not doing running swipe, try for a running slam if we're close enough
						// Attempt only 1/5 of the time.
						this.xMarkDir = dx;
						this.yMarkDir = dy;
						this.state = KingBonBon.STATE_PREP_SLAM;
					}
				}
				break;
			case KingBonBon.STATE_PREP_STAB: //------------------------------------------------------------
				if (attCount < 1) {
					// Mark tiles with damage indicators
					SoundPlayer.playWAV(GPath.createSoundPath("whip_ATT.wav"), getXPos(), getYPos());
					addEffect(new DamageIndicator(xPos + xMarkDir, yPos + yMarkDir));
					addEffect(new DamageIndicator(xPos + (xMarkDir*2), yPos + (yMarkDir*2)));
					addEffect(new DamageIndicator(xPos + (xMarkDir*3), yPos + (yMarkDir*3)));
					
					// Attack if next to player
					if ((plrX == xPos + xMarkDir && plrY == yPos + yMarkDir) ||
							(plrX == xPos + (xMarkDir*2) && plrY == yPos + (yMarkDir*2)) ||
							(plrX == xPos + (xMarkDir*3) && plrY == yPos + (yMarkDir*3))) {
						playerInitiate();
					}
					
					// Update attack counter
					this.attCount += 1;
				} else {
					// Use direction from player to mark squares
					SoundPlayer.playWAV(GPath.createSoundPath("whip_ATT.wav"), getXPos(), getYPos());
					if (Math.abs(xMarkDir) > Math.abs(yMarkDir)) {
						// Player to left/right
						addEffect(new DamageIndicator(xPos + xMarkDir, yPos + 1));
						addEffect(new DamageIndicator(xPos + xMarkDir, yPos - 1));
						addEffect(new DamageIndicator(xPos + (xMarkDir*2), yPos + 1));
						addEffect(new DamageIndicator(xPos + (xMarkDir*2), yPos - 1));
						
						// Attack player if in affected space
						if (((plrX == xPos + xMarkDir) || (plrX == xPos + (xMarkDir*2))) &&
								(plrY == yPos - 1 || plrY == yPos + 1)) {
							playerInitiate();
						}
					} else {
						// Player above/below
						addEffect(new DamageIndicator(xPos + 1, yPos + yMarkDir));
						addEffect(new DamageIndicator(xPos - 1, yPos + yMarkDir));
						addEffect(new DamageIndicator(xPos + 1, yPos + (yMarkDir*2)));
						addEffect(new DamageIndicator(xPos - 1, yPos + (yMarkDir*2)));
						
						// Attack player if in affected space
						if (((plrY == yPos + yMarkDir) || (plrY == yPos + (yMarkDir*2))) &&
								(plrX == xPos - 1 || plrX == xPos + 1)) {
							playerInitiate();
						}
					}
					
					this.attCount = 0;
					this.state = KingBonBon.STATE_ATT_STAB;
				}
				break;
			case KingBonBon.STATE_ATT_STAB: //------------------------------------------------------------
				// Cooldown period for one turn
				this.state = KingBonBon.STATE_PURSUE;
				break;
			case KingBonBon.STATE_PREP_SWIPE: //------------------------------------------------------------
				if (attCount < 1) {
					// Use direction from player to mark squares
					SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"), getXPos(), getYPos());
					if (Math.abs(xMarkDir) > Math.abs(yMarkDir)) {
						// Player to left/right
						addEffect(new FireEffect(xPos + xMarkDir, yPos));
						addEffect(new FireEffect(xPos + xMarkDir, yPos + 1));
						addEffect(new FireEffect(xPos + xMarkDir, yPos - 1));
						
						// Attack player if in affected space
						if ((plrX == xPos + xMarkDir) && (plrY == yPos || plrY == yPos - 1 || plrY == yPos + 1)) {
							playerInitiate();
						}
					} else {
						// Player above/below
						addEffect(new FireEffect(xPos, yPos + yMarkDir));
						addEffect(new FireEffect(xPos + 1, yPos + yMarkDir));
						addEffect(new FireEffect(xPos - 1, yPos + yMarkDir));
						
						// Attack player if in affected space
						if ((plrY == yPos + yMarkDir) && (plrX == xPos || plrX == xPos - 1 || plrX == xPos + 1)) {
							playerInitiate();
						}
					}
					
					// Add second swipe warning effects
					addEffect(new SmallFireEffect(xPos + (xMarkDir*2), yPos + (yMarkDir*2)));
					addEffect(new SmallFireEffect(xPos + (xMarkDir*2) + Math.abs(yMarkDir),
							yPos + (yMarkDir*2) + Math.abs(xMarkDir)));
					addEffect(new SmallFireEffect(xPos + (xMarkDir*2) - Math.abs(yMarkDir),
							yPos + (yMarkDir*2) - Math.abs(xMarkDir)));
					
					// Increase attack counter
					this.attCount += 1;
				} else {
					// Use direction from player to mark squares
					SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"), getXPos(), getYPos());
					if (Math.abs(xMarkDir) > Math.abs(yMarkDir)) {
						// Player to left/right
						addEffect(new FireEffect(xPos + (xMarkDir*2), yPos));
						addEffect(new FireEffect(xPos + (xMarkDir*2), yPos + 1));
						addEffect(new FireEffect(xPos + (xMarkDir*2), yPos - 1));
						
						// Attack player if in affected space
						if ((plrX == xPos + (xMarkDir*2)) && (plrY == yPos || plrY == yPos - 1 || plrY == yPos + 1)) {
							playerInitiate();
						}
					} else {
						// Player above/below
						addEffect(new FireEffect(xPos, yPos + (yMarkDir*2)));
						addEffect(new FireEffect(xPos + 1, yPos + (yMarkDir*2)));
						addEffect(new FireEffect(xPos - 1, yPos + (yMarkDir*2)));
						
						// Attack player if in affected space
						if ((plrY == yPos + (yMarkDir*2)) && (plrX == xPos || plrX == xPos - 1 || plrX == xPos + 1)) {
							playerInitiate();
						}
					}
					
					// Reset counter and change state
					this.attCount = 0;
					this.state = KingBonBon.STATE_ATT_SWIPE;
				}
				break;
			case KingBonBon.STATE_ATT_SWIPE: //------------------------------------------------------------
				// Cooldown period for one turn
				this.state = KingBonBon.STATE_PURSUE;
				break;
			case KingBonBon.STATE_PREP_SLAM: //------------------------------------------------------------
				// Increment the counter
				this.attCount += 1;
				
				// Prepare for the slam
				if (attCount <= 3) {
					// Do nothing if still prepping
				} else {
					// Slam the ground, marking it up
					SoundPlayer.playWAV(GPath.createSoundPath("slam_ATT.wav"));
					for (int x = (xPos - 3); x < (xPos + 4); x++) {
						int relX = Math.abs(xPos - x);
						for (int y = (yPos + (relX-3)); y < (yPos + (4-relX)); y++) {
							if (!(x == xPos && y == yPos)) {
								// Add effect if not at center
								addEffect(new RubbleEffect(x, y));
							}
							if (plrX == x && plrY == y) {
								playerInitiate();
							}
						}
					}
					
					// Reset counter and change state
					this.attCount = 0;
					this.state = KingBonBon.STATE_PURSUE;
				}
				break;
			case KingBonBon.STATE_PREP_FIRE: //------------------------------------------------------------
				if (attCount == 0) {
					// Mark all fire spawning tiles with warnings
					for (Dimension d: fireCoords) {
						addEffect(new WarningIndicator(d.width, d.height));
					}
					
					// Increment attack counter
					this.attCount += 1;
				} else {
					// Spawn fireballs at marked locations
					SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"));
					for (Dimension d: fireCoords) {
						int direction = 1 + (-2 * (d.height%2));
						addProjectile(new KingFireball(d.width, d.height, direction, 0, getClass()));
					}
					
					// Reset attack counter and change state
					this.attCount = 0;
					this.state = KingBonBon.STATE_ATT_FIRE;
				}
				break;
			case KingBonBon.STATE_ATT_FIRE: //------------------------------------------------------------
				// Cooldown period for one turn
				this.state = KingBonBon.STATE_PURSUE;
				break;
			case KingBonBon.STATE_PREP_THUNDER: //------------------------------------------------------------
				if (attCount == 0) {
					// Increment attack counter and do nothing
					this.attCount += 1;
				} else {
					// Set thunder down on the arena
					this.thunderActive = true;
					
					// Play spark sound
					SoundPlayer.playWAV(GPath.createSoundPath("electric_spark.wav"), getXPos(), getYPos());
					
					// Reset attack counter and change state
					this.attCount = 0;
					this.state = KingBonBon.STATE_ATT_THUNDER;
				}
				break;
			case KingBonBon.STATE_ATT_THUNDER: //------------------------------------------------------------
				// Cooldown period for one turn
				this.state = KingBonBon.STATE_PURSUE;
				break;
			case KingBonBon.STATE_PREP_FLOOD: //------------------------------------------------------------
				if (attCount == 0) {
					// Mark the side opposite the king with lots of warnings
					if (xPos < 5) {
						// Mark the right side
						for (int x = 8; x > 1; x--) {
							for (int y = 2; y < 8; y++) {
								addEffect(new WarningIndicator(x, y, 4));
							}
						}
					} else {
						// Mark the left side
						for (int x = 1; x < 8; x++) {
							for (int y = 2; y < 8; y++) {
								addEffect(new WarningIndicator(x, y, 4));
							}
						}
					}
				} else if (attCount == 5) {
					// Set the flood down on the arena
					SoundPlayer.playWAV(GPath.createSoundPath("flood_ATT.wav"));
					if (xPos < 5) {
						// Flood the right side
						for (int x = 8; x > 1; x--) {
							for (int y = 2; y < 8; y++) {
								if (x == 2) {
									addEffect(new FloodEffect(x, y, -1, true));
								} else {
									addEffect(new FloodEffect(x, y, -1, false));
								}
								if (plrX == x && plrY == y) {
									playerInitiate();
								}
							}
						}
					} else {
						// Flood the left side
						for (int x = 1; x < 8; x++) {
							for (int y = 2; y < 8; y++) {
								if (x == 7) {
									addEffect(new FloodEffect(x, y, 1, true));
								} else {
									addEffect(new FloodEffect(x, y, 1, false));
								}
								if (plrX == x && plrY == y) {
									playerInitiate();
								}
							}
						}
					}
					
					// Reset attack counter and change state
					this.attCount = 0;
					this.state = KingBonBon.STATE_ATT_FLOOD;
					return;
				}
				
				// Increment attack counter
				this.attCount += 1;
				break;
			case KingBonBon.STATE_ATT_FLOOD: //------------------------------------------------------------
				// Cooldown period for one turn
				this.state = KingBonBon.STATE_PURSUE;
				break;
			default: //------------------------------------------------------------------------------------
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
				
		} // END OF SWITCH STATEMENT ****
		
		// End of turn resetting of variables
		resetFlags();
		incrementCounters();
		return;
	}
	
	private void resetFlags() {
		this.recentDmg = false;
		this.recentWarp = false;
	}
	
	private void incrementCounters() {
		this.spcCount += 1;
	}
	
	// Shortening of adding effect for convenience and easy code reading
	private void addEffect(GEffect fx) {
		EntityManager.getInstance().getEffectManager().addEffect(fx);
	}
	
	// Shortening of adding projectile for convenience and easy code reading
	private void addProjectile(GProjectile proj) {
		EntityManager.getInstance().getProjectileManager().addProjectile(proj);
	}
	
	// Checks for best corner to warp to and returns it
	private void warpKing(int plrX, int plrY) {
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
		this.warpFlag = false;
		
		// Play warp sound
		SoundPlayer.playWAV(GPath.createSoundPath("warp.wav"));
		
		// Warp to the new coordinates
		this.xPos = bestCorner.width;
		this.yPos = bestCorner.height;
	}
	
	// Manage the thunder attack, which slowly crawls down the screen
	private void manageThunder(int plrX, int plrY) {
		// If thunder isn't active, return
		if (!thunderActive) {
			return;
		}
		
		// Alternate between marking rows and striking them with thunder
		if ((thunderCount % 2) == 0) {
			// Mark the current row as a warning
			for (int x = 1; x < 9; x++) {
				addEffect(new ThunderEffect(x, thunderRow, false));
			}
		} else {
			// Strike the thunder down at the current row
			SoundPlayer.playWAV(GPath.createSoundPath("thunder_ATT.wav"));
			for (int x = 1; x < 9; x++) {
				addEffect(new ThunderEffect(x, thunderRow));
				if (plrX == x && plrY == thunderRow) {
					playerInitiate();
				}
			}
			
			// Increment the row of thunder
			this.thunderRow += 1;
		}
		
		// Increment the thunder counter
		this.thunderCount += 1;
		
		// If we're done with the attack, reset everything
		if (thunderRow > 7) {
			this.thunderActive = false;
			this.thunderCount = 0;
			this.thunderRow = 2;
		}
	}
	
	private void playHurt() {
		Random r = new Random();
		if (r.nextInt(4) == 0) {
			int whichSound = (r.nextInt(2) + 1);
			SoundPlayer.playWAV(GPath.createSoundPath("king_hurt" + whichSound +".wav"), getXPos(), getYPos());
		}
	}
	
	// Chooses the next special attack for the King
	private void chooseSpecialAttack() {
		// Check and reset special attack flags if all are hit
		checkSpcAttacks();
		
		// Create and populate temp attack list
		ArrayList<Integer> attacks = new ArrayList<Integer>();
		
		if (!didSpc0) {
			attacks.add(KingBonBon.STATE_PREP_FIRE);
		}
		
		if (!didSpc1) {
			attacks.add(KingBonBon.STATE_PREP_THUNDER);
		}
		
		if (!didSpc2) {
			attacks.add(KingBonBon.STATE_PREP_FLOOD);
		}
		
		// Select the attack
		int whichAttack = new Random().nextInt(attacks.size());
		this.state = attacks.get(whichAttack);
		
		switch (state) {
			case KingBonBon.STATE_PREP_FIRE:
				this.didSpc0 = true;
				break;
			case KingBonBon.STATE_PREP_THUNDER:
				this.didSpc1 = true;
				break;
			case KingBonBon.STATE_PREP_FLOOD:
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

}
