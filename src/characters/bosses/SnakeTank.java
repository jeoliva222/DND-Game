package characters.bosses;

import java.util.Random;

import ai.DumbFollow;
import ai.PatrolPattern;
import characters.ArrowTurret;
import characters.GCharacter;
import characters.Player;
import effects.DamageIndicator;
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

// Class representing the Boss enemy of the Desert area (Tank form)
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
	
	// Attack counter for deciding behavior
	private int attCount = 0;
	
	// Times nuke has been sent over to player without them deflecting successfully
	private int nukeMissCount = 0;
	
	// Maximum count that nukeMissCount can achieve
	private final int nukeMissMax = 3;
	
	// Determines time spent on cannon phase before switching
	private final int cannonMax = 30;
	
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
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeTank.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Der Froschmorder";
	}
	
	@Override
	public String getImage() {
		
		// TEMP TODO
		//return this.imageDir + this.stImage_base + "_full.png";
		
		String imgPath = this.imageDir + this.stImage_base;
		String hpPath = "";
		String statePath = "";
		if(this.currentHP > 10) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch(this.state) {
			case SnakeTank.STATE_IDLE:
			case SnakeTank.STATE_PURSUE:
				if(this.firedCannon) {
					statePath = "_CANNON";
				}
				break;
			case SnakeTank.STATE_ALERTED:
				statePath = "_ALERT";
				break;
			case SnakeTank.STATE_PREP_CHAINGUN:
				statePath = "_PREP_GUN";
				break;
			case SnakeTank.STATE_ATT_CHAINGUN:
				statePath = "_ATT_GUN";
				break;
			case SnakeTank.STATE_PREP_NUKE:
				//statePath = "_PREP_SHOOT";
				break;
			case SnakeTank.STATE_ATT_NUKE:
				//statePath = "_ATT_SHOOT";
				break;
			default:
				System.out.println
					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	// TODO
	public String getCorpseImage() {
		//return this.stImage_DEAD; 
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH.wav"));
		
		// Close the pits in the arena
		for(int x = 6; x < 8; x++) {
			for(int y = 2; y < 6; y++) {
				GameScreen.getTile(x, y).setTileType(new AltGround());
			}
		}
		
		// Spawn General Hanz (Enter 2nd form)
		EntityManager.getInstance().getNPCManager().addCharacter(new SnakeGeneral(this.xPos, this.yPos));
	}
	
	// Override that triggers Tank to deflect next redirected Nuke
	// Also tells tank to do chaingun as next attack
	@Override
	public boolean damageCharacter(int damage) {
		this.nukeMissCount = 0;
		this.shouldNuke = false;
		if(damage > 0) this.willDeflect = true;
		return super.damageCharacter(damage);
	}

	@Override
	public void takeTurn() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if(!this.isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Initialize randomizer
		Random r = new Random();
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = plrX - this.xPos;
		int distY = plrY - this.yPos;
		
		// Set fired flag to false
		this.firedCannon = false;
		
		switch(this.state) {
			case SnakeTank.STATE_IDLE: //----------------------------------------------
				// If player enters the arena, alert and close the arena
				if((plrX == 4 || plrX == 5) && plrY == 2) {		
					// Close the doors to the arena
					GameScreen.getTile(4, 1).setTileType(new AltWall());
					GameScreen.getTile(5, 1).setTileType(new AltWall());
					
					// Change music to Boss music
					SoundPlayer.changeMidi(GPath.createSoundPath("d_e3m8.mid"), 70);
					
					// Turn off the arrow turrets on the same screen
					for(GCharacter npc: EntityManager.getInstance().getNPCManager().getCharacters()) {
						if(npc != this && npc instanceof ArrowTurret) {
							// Set was fired to true, and break from 'for' loop
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
				
				// Blindly pursue the target (No need for path-finding)
				DumbFollow.blindPursue(distX, distY, dx, dy, this);
				
				// If we're done firing our cannon, switch to another phase
				if(this.attCount >= (this.cannonMax / (this.nukeMissCount + 1))) {
					// Reset attack counter
					this.attCount = 0;
					
					// Change state to chaingun or nuke (alternate between)
					if(this.shouldNuke) {
						this.state = SnakeTank.STATE_PREP_NUKE;
					} else {
						this.state = SnakeTank.STATE_PREP_CHAINGUN;
					}
					
					// Set flag to do Nuke attack next time
					this.shouldNuke = true;
					
					break;
				}
				
				// Controls firing of cannonballs
				if(!this.delayNextShot) {
					// Randomize our shot pattern
					int shouldFire = r.nextInt(6);
					
					if(shouldFire < 2) {
						// Don't shoot, and increment counter by 1
						this.attCount += 1;
					} else if (shouldFire < 5) {
						// Play sound
						this.playCannonSound();
						
						// Flip fired flag
						this.firedCannon = true;
						
						// Shoot a cannonball, and increment counter by 2
						this.addProjectile(new SnakeCannonball(this.xPos - 1, this.yPos, -1, 0, this));
						this.attCount += 2;
					} else {
						// Shoot 2 cannonballs (Randomly on higher or lower lane)
						// Increment counter by 3
						
						// Play sound
						this.playCannonSound();
						
						// Flip fired flag
						this.firedCannon = true;
						
						// Shoot one of the two straight ahead
						this.addProjectile(new SnakeCannonball(this.xPos - 1, this.yPos, -1, 0, this));
						
						// Shoot the other on either the higher or lower lane
						// Checks to make sure it doesn't shoot it into a wall
						// If no walls to hit, decided randomly
						if(this.yPos == 5) {
							this.addProjectile(new SnakeCannonball(this.xPos - 1, this.yPos - 1, -1, 0, this));
						} else if(this.yPos == 2) {
							this.addProjectile(new SnakeCannonball(this.xPos - 1, this.yPos + 1, -1, 0, this));
						} else if(r.nextInt(1) == 0) {
							this.addProjectile(new SnakeCannonball(this.xPos - 1, this.yPos - 1, -1, 0, this));
						} else {
							this.addProjectile(new SnakeCannonball(this.xPos - 1, this.yPos + 1, -1, 0, this));
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
				if(this.attCount <= 1) {
					// Move, but do nothing yet
					// Blindly pursue the target (No need for path-finding)
					DumbFollow.blindPursue(distX, distY, this);
				} else if(this.attCount == 2) {
					// Play reving sound
					SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Rev.wav"));
				} else {
					// Fire the guns, and then change state
					for(int i = 7; i >= 4; i--) {
						this.addEffect(new DamageIndicator(i, this.yPos));
						if(plrX == i && plrY == this.yPos) {
							this.playerInitiate();
							this.hitShots = true;
						}
					}
					
					// Play firing sound
					SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Fire.wav"));
					
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
				if(!this.hitShots) {
					DumbFollow.blindPursue(distX, distY, this);
				} else {
					this.hitShots = false;
				}
				
				// Check if we're done using the chaingun
				if(this.attCount >= (this.chaingunMax - 1)) {
					// Reset attack counter and change state
					this.attCount = 0;
					this.state = SnakeTank.STATE_PURSUE;
					break;
				}
				
				// Play firing sound
				SoundPlayer.playWAV(GPath.createSoundPath("Chaingun_Fire.wav"));
				
				// Continue firing the guns
				for(int i = 7; i >= 4; i--) {
					this.addEffect(new DamageIndicator(i, this.yPos));
					if(plrX == i && plrY == this.yPos) {
						this.playerInitiate();
						this.hitShots = true;
					}
				}

				// Increment attack counter
				this.attCount += 1;
				break;
			case SnakeTank.STATE_PREP_NUKE: //---------------------------------------------
				// Move to middle of arena if on edges
				if(this.yPos == 2) {
					this.moveCharacter(0, 1);
				} else if (this.yPos == 5) {
					this.moveCharacter(0, -1);
				}
				
				// Check if we have charged our Nuke long enough
				if(this.attCount >= this.nukeMax) {
					// Reset attack counter
					this.attCount = 0;
					
					//If we've charged long enough, then fire the Nuke
					for(GCharacter npc: EntityManager.getInstance().getNPCManager().getCharacters()) {
						if(npc != this && npc instanceof SnakeNuke) {
							// Set was fired to true, and break from 'for' loop
							SnakeNuke nuke = (SnakeNuke) npc;
							nuke.wasFired = true;
							break;
						}
					}
					
					// Set that we've fired one nuke
					if(this.nukeMissCount < this.nukeMissMax) {
						this.nukeMissCount += 1;
					}
					
					// Change state and break
					this.state = SnakeTank.STATE_ATT_NUKE;
					break;
				}
				
				this.attCount += 1;
				break;
			case SnakeTank.STATE_ATT_NUKE: //---------------------------------------------
				// Swat Nuke to the side if it is coming at us and we've been hit once before
				
				// Get reference to the Nuke
				for(GCharacter npc: EntityManager.getInstance().getNPCManager().getCharacters()) {
					if(npc != this && npc instanceof SnakeNuke) {
						// Get Nuke Instance from NPC
						SnakeNuke nuke = (SnakeNuke) npc;
						
						// Swat the Nuke if next to us and headed in our direction
						if(this.willDeflect && nuke.getXPos() == (this.xPos - 1) && 
								nuke.getYPos() == this.yPos && nuke.xSpeed == 1) {
							if(this.yPos >= 4) {
								nuke.setDirection(0, -1);
							} else {
								nuke.setDirection(0, 1);
							}
						}
					}
				}
				
				// If our nuke is dead, then reset flags and switch back to pursue state
				if(this.nukeDead) {
					this.nukeDead = false;
					this.attCount = 0;
					this.state = SnakeTank.STATE_PURSUE;
					break;
				}
				
				this.attCount += 1;
				break;
			default: //---------------------------------------------
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
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
