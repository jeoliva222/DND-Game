package characters.bosses;

import java.util.Random;

import ai.DumbFollow;
import ai.PatrolPattern;
import buffs.Buff;
import characters.ArrowTurret;
import characters.GCharacter;
import characters.allies.Player;
import effects.BulletEffect;
import effects.GEffect;
import gui.GameScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.GProjectile;
import projectiles.SnakeCannonball;
import tiles.AltGround;
import tiles.AltWall;
import tiles.MovableType;

/**
 * Class representing the Snake Tank boss enemy
 * @author jeoliva
 */
public class SnakeTank extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 3464578731628352317L;

	// Modifiers/Statistics
	
	private int MAX_HP = 20;
	
	private int ARMOR_VAL = 10;
	
	private int MIN_DMG = 4;
	private int MAX_DMG = 8;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.5;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_CHAINGUN = 3;
	private static final int STATE_ATT_CHAINGUN = 4;
	private static final int STATE_PREP_NUKE = 5;
	private static final int STATE_ATT_NUKE = 6;
	
	//----------------------------
	
	// Additional parameters
	
	// Flag indicating whether tank fired its cannon this turn
	private boolean firedCannon = false;
	
	// If tank did a double shot, indicates that it should put a one
	// turn delay before firing again
	private boolean delayNextShot = false;
	
	// If tank hit player with chaingun barrage, sets flag
	// so the tank does not move next turn
	private boolean hitShots = false;
	
	// Flag to alternate between chaingun and nuke attack
	private boolean shouldNuke = false;
	
	// Flag marking whether Tank's nuke has blown up yet
	protected boolean nukeDead = false;
	
	// Flag indicating whether we've been damaged by the Nuke and will try to deflect it next time
	private boolean willDeflect = false;
	
	// Flag indicating whether we've just deflected the Nuke (Used for rendering)
	private boolean deflected = false;
	
	// Attack counter for deciding behavior
	private int attCount = 0;
	
	// Times nuke has been sent over to player without them deflecting successfully
	private int nukeMissCount = 0;
	
	// Maximum count that nukeMissCount can achieve
	private final int nukeMissMax = 3;
	
	// Determines time spent on cannon phase before switching
	private final int cannonMax = 24;
	
	// Determines time spent on chaingun phase before switching
	private final int chaingunMax = 2;
	
	// Determines time spent prepping the Nuke to fire
	private final int nukeMax = 3;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK);
	private String stImage_base = "snaketank";
	
	private String stImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK, "snaketank_dead.png");

	// Constructor
	public SnakeTank(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeTank.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
	}
	
	public String getName() {
		return "Der Froschm\u00F6rder";
	}
	
	@Override
	public String getImage() {	
		String imgPath = (imageDir + stImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch (state) {
			case SnakeTank.STATE_IDLE:
			case SnakeTank.STATE_PURSUE:
				if (firedCannon) {
					statePath = "_CANNON";
				}
				break;
			case SnakeTank.STATE_ALERTED:
				statePath = "_ALERT";
				break;
			case SnakeTank.STATE_PREP_CHAINGUN:
				if (attCount < 3) {
					statePath = "_PREP_GUN";
				} else {
					statePath = "_PREP_GUN2";
				}
				break;
			case SnakeTank.STATE_ATT_CHAINGUN:
				statePath = "_ATT_GUN";
				break;
			case SnakeTank.STATE_PREP_NUKE:
				if (attCount == 2) {
					statePath = "_ALERT";
				}
				break;
			case SnakeTank.STATE_ATT_NUKE:
				if (attCount == 0) {
					statePath = "_CANNON";
				} else if (willDeflect) {
					if (deflected) {
						statePath = "_ATT_DEFLECT";
					} else {
						statePath = "_PREP_DEFLECT";
					}
				}
				
				// Otherwise nothing
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return this.stImage_DEAD;
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		attackPlayer();
	}
	
	@Override
	public void addBuff(Buff debuff) {
		// Don't add the buff
		return;
	}
	
	@Override
	public void onDeath() {
		// Play death sound
		// TODO
		
		// Close the pits in the arena
		for (int x = 6; x < 8; x++) {
			for (int y = 2; y < 6; y++) {
				GameScreen.getTile(x, y).setTileType(new AltGround());
			}
		}
		
		// Spawn General Hanz (Enter 2nd form)
		EntityManager.getInstance().getNPCManager().addCharacter(new SnakeGeneral(xPos, yPos));
	}
	
	// Override that triggers Tank to deflect next redirected Nuke
	// Also tells tank to do chaingun as next attack
	@Override
	public boolean damageCharacter(int damage) {
		if (damage > 0) {
			this.nukeMissCount = 0;
			this.shouldNuke = false;
			this.willDeflect = true;
		}
		return super.damageCharacter(damage);
	}

	@Override
	public void takeTurn() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if (!isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Initialize randomizer
		Random r = new Random();
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = (plrX - xPos);
		int distY = (plrY - yPos);
		
		// Set fired flag to false
		this.firedCannon = false;
		
		switch (state) {
			case SnakeTank.STATE_IDLE: //----------------------------------------------
				// If player enters the arena, alert and close the arena
				if ((plrX == 4 || plrX == 5) && plrY == 2) {		
					// Close the doors to the arena
					GameScreen.getTile(4, 1).setTileType(new AltWall());
					GameScreen.getTile(5, 1).setTileType(new AltWall());
					
					// Change music to Boss music
					SoundPlayer.changeMidi(GPath.createSoundPath("d_e3m8.mid"), 70);
					
					// Turn off the arrow turrets on the same screen
					for (GCharacter npc: EntityManager.getInstance().getNPCManager().getCharacters()) {
						if (npc != this && npc instanceof ArrowTurret) {
							// Stop turret from firing
							ArrowTurret turret = (ArrowTurret) npc;
							turret.setInactive();
						}
					}
					
					// Change state to alerted
					this.state = SnakeTank.STATE_ALERTED;
				}
				break;
			case SnakeTank.STATE_ALERTED: //--------------------------------------------
				// Start to chase player
				//this.currentHP = 0; // TESTER TODO - Remove this
				this.state = SnakeTank.STATE_PURSUE;
				break;
			case SnakeTank.STATE_PURSUE: //---------------------------------------------
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
				
				// Blindly pursue the target (No need for path-finding)
				DumbFollow.blindPursue(distX, distY, dx, dy, this);
				
				// If we're done firing our cannon, switch to another phase
				if (attCount >= (cannonMax / (nukeMissCount + 1))) {
					// Reset attack counter
					this.attCount = 0;
					
					// Change state to chaingun or nuke (alternate between)
					if (shouldNuke) {
						this.state = SnakeTank.STATE_PREP_NUKE;
					} else {
						this.state = SnakeTank.STATE_PREP_CHAINGUN;
					}
					
					// Set flag to do Nuke attack next time
					this.shouldNuke = true;
					
					break;
				}
				
				// Controls firing of cannonballs
				if (!delayNextShot) {
					// Randomize our shot pattern
					int shouldFire = r.nextInt(6);
					
					if (shouldFire < 2) {
						// Don't shoot, and increment counter by 1
						this.attCount += 1;
					} else if (shouldFire < 5) {
						// Play sound
						playCannonSound();
						
						// Flip fired flag
						this.firedCannon = true;
						
						// Shoot a cannonball, and increment counter by 2
						addProjectile(new SnakeCannonball(xPos - 1, yPos, -1, 0, getClass()));
						this.attCount += 2;
					} else {
						// Shoot 2 cannonballs (Randomly on higher or lower lane)
						// Increment counter by 3
						
						// Play sound
						playCannonSound();
						
						// Flip fired flag
						this.firedCannon = true;
						
						// Shoot one of the two straight ahead
						addProjectile(new SnakeCannonball(xPos - 1, yPos, -1, 0, getClass()));
						
						// Shoot the other on either the higher or lower lane
						// Checks to make sure it doesn't shoot it into a wall
						// If no walls to hit, decided randomly
						if (yPos == 5) {
							addProjectile(new SnakeCannonball(xPos - 1, yPos - 1, -1, 0, getClass()));
						} else if (yPos == 2) {
							addProjectile(new SnakeCannonball(xPos - 1, yPos + 1, -1, 0, getClass()));
						} else if (r.nextInt(1) == 0) {
							addProjectile(new SnakeCannonball(xPos - 1, yPos - 1, -1, 0, getClass()));
						} else {
							addProjectile(new SnakeCannonball(xPos - 1, yPos + 1, -1, 0, getClass()));
						}
						
						// Delay our next shot to avoid unfair scenarios
						this.delayNextShot = true;
						this.attCount += 3;
					}
				} else {
					// Reset flag to indicate that our shot has been delayed
					this.delayNextShot = false;
				}
				
				break;
			case SnakeTank.STATE_PREP_CHAINGUN: //---------------------------------------------
				if (attCount <= 1) {
					// Move, but do nothing yet
					// Blindly pursue the target (No need for path-finding)
					DumbFollow.blindPursue(distX, distY, this);
				} else if (attCount == 2) {
					// Play reving sound
					SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Rev.wav"), getXPos(), getYPos());
				} else {
					// Fire the guns, and then change state
					for (int i = 7; i >= 4; i--) {
						addEffect(new BulletEffect(i, yPos, -1, 0));
						if (plrX == i && plrY == yPos) {
							playerInitiate();
							this.hitShots = true;
						}
					}
					
					// Play firing sound
					SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Fire.wav"), getXPos(), getYPos());
					
					// Reset attack counter and change state
					this.attCount = 0;
					this.state = SnakeTank.STATE_ATT_CHAINGUN;
					break;
				}
				
				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeTank.STATE_ATT_CHAINGUN: //---------------------------------------------
				// If we didn't hit last barrage, move towards player
				if (!hitShots) {
					DumbFollow.blindPursue(distX, distY, this);
				} else {
					this.hitShots = false;
				}
				
				// Check if we're done using the chaingun
				if (attCount >= (chaingunMax - 1)) {
					// Reset attack counter and change state
					this.attCount = 0;
					this.state = SnakeTank.STATE_PURSUE;
					break;
				}
				
				// Play firing sound
				SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Fire.wav"), getXPos(), getYPos());
				
				// Continue firing the guns
				for (int i = 7; i >= 4; i--) {
					addEffect(new BulletEffect(i, yPos, -1, 0));
					if (plrX == i && plrY == yPos) {
						playerInitiate();
						this.hitShots = true;
					}
				}

				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeTank.STATE_PREP_NUKE: //---------------------------------------------
				// If first turn, issue a warning - TODO
				if (attCount == 1) {
					SoundPlayer.playWAV(GPath.createSoundPath("snake1_warn1.wav"));
				}
				
				// Move to middle of arena if on edges
				if (yPos == 2) {
					moveCharacter(0, 1);
				} else if (yPos == 5) {
					moveCharacter(0, -1);
				}
				
				// Check if we have charged our Nuke long enough
				if (attCount >= nukeMax) {
					// Reset attack counter
					this.attCount = 0;
					
					// Play sound TODO
					playCannonSound();
					
					// If we've charged long enough, then fire the Nuke
					for (GCharacter npc: EntityManager.getInstance().getNPCManager().getCharacters()) {
						if (npc != this && npc instanceof SnakeNuke) {
							// Inform Nuke that it was fired, and then break from 'for' loop
							SnakeNuke nuke = (SnakeNuke) npc;
							nuke.fireNuke();
							break;
						}
					}
					
					// Set that we've fired one nuke
					if (nukeMissCount < nukeMissMax) {
						this.nukeMissCount += 1;
					}
					
					// Change state and break
					this.state = SnakeTank.STATE_ATT_NUKE;
					break;
				}
				
				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeTank.STATE_ATT_NUKE: //---------------------------------------------
				// Swat Nuke to the side if it is coming at us and we've been hit once before
				// Otherwise, just wait until the Nuke has exploded
				
				// Only look for Nuke if we're going to try to deflect it
				if (willDeflect) {	
					// Reset deflected flag
					this.deflected = false;
					
					// Get reference to the Nuke
					for (GCharacter npc: EntityManager.getInstance().getNPCManager().getCharacters()) {
						if (npc != this && npc instanceof SnakeNuke) {
							// Get Nuke Instance from NPC
							SnakeNuke nuke = (SnakeNuke) npc;
							
							// Swat the Nuke if next to us and headed in our direction
							if (nuke.getXPos() == (xPos - 1) && nuke.getYPos() == yPos && nuke.xSpeed == 1) {
								
								// Mark deflection
								this.deflected = true;
								
								// Play hit sound
								SoundPlayer.playWAV(GPath.createSoundPath("player_SWING.wav"), 5f, getXPos(), getYPos());
								
								// Set new Nuke direction
								if (yPos >= 4) {
									nuke.setDirection(0, -1);
								} else {
									nuke.setDirection(0, 1);
								}
							}
							
							// Break from character iterating loop
							break;
						}
					} // End of character iterating 'for' loop
				} // End of 'willDeflect' segment
				
				// If our nuke is dead, then reset flags and switch back to pursue state
				// This variable is set by SnakeNuke using the 'informNukeDead' function
				if (nukeDead) {
					this.nukeDead = false;
					this.attCount = 0;
					this.state = SnakeTank.STATE_PURSUE;
					break;
				}
				
				// Increment attack counter
				this.attCount += 1;
				break;
			default: //---------------------------------------------
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}
	
	// Choose random cannon fire sound to play
	private void playCannonSound() {
		// Decide sound to play
		int whichSound = (new Random().nextInt(4) + 1);
		
		// Play the sound
		SoundPlayer.playWAV(GPath.createSoundPath("CannonFire" + whichSound + ".wav"), -10f);
	}
	
	// Used by SnakeNuke to inform the Tank that it blew up
	protected void informNukeDead() {
		this.nukeDead = true;
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
