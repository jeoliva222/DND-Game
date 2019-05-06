package characters.bosses;

import java.awt.Dimension;

import ai.PatrolPattern;
import characters.GCharacter;
import characters.Player;
import effects.DamageIndicator;
import effects.FloodEffect;
import effects.GEffect;
import effects.ThunderEffect;
import effects.WarningIndicator;
import gui.GameScreen;
import gui.InfoScreen;
import gui.LogScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.GProjectile;
import projectiles.KingFireball;
import tiles.Ground;
import tiles.MovableType;

// The King's Head represents the last phase of the King Bon Bon fight.
public class KingsHead extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 7373103821186379733L;
	
	// Modifiers/Statistics

	private int MAX_HP = 30;
	
	private int MIN_DMG = 3;
	private int MAX_DMG = 4;
	
	private double CRIT_CHANCE = 0.15;
	private double CRIT_MULT = 1.25;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_ALERTED = 0;
	private static final int STATE_PURSUE = 1;
	private static final int STATE_REST = 2;
	private static final int STATE_STORM = 3;
	
	//----------------------------
	
	// Additional fields
	
	// Fields determining chase and rest times
	private int attCount = 0;
	private int interval = 2;
	private int altInterval = 3;
	
	// Indicator to show whether head nearly died
	// and should start storm attack
	private boolean startStorm = false;
	
	// Variables to manage top thunder attack
	private int thunderTopRow = 2;
	private int thunderTopCount = 0;
	private boolean thunderTopActive = false;
	private int thunderBotRow = 7;
	private int thunderBotCount = 0;
	private boolean thunderBotActive = false;
	
	// Corners to warp to
	private Dimension[] corners = new Dimension[] {
			new Dimension(4, 4),
			new Dimension(4, 5),
			new Dimension(5, 4),
			new Dimension(5, 5)
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
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.KINGS_HEAD);
	private String khImage_base = "kingshead";
	
	private String khImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.KINGS_HEAD, "kingshead_dead.png");


	public KingsHead(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = KingsHead.STATE_ALERTED;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.imagePath = this.getImage();
	}
	
	public KingsHead(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = KingsHead.STATE_ALERTED;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "King's Head";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.khImage_base;
		String hpPath = "";
		String statePath = "";
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch(this.state) {
			case KingsHead.STATE_STORM:
				if(this.attCount == 0) {
					statePath = "_REST";
				} else if (this.attCount == 1) {
					statePath = "_RAGING";
				} else if (this.attCount == 2) {
					statePath = "_CALMING";
				} else if (this.attCount >= 34) {
					statePath = "_REST";
				} else {
					statePath = "_CHASE";
				}
				break;
			case KingsHead.STATE_PURSUE:
				if(this.attCount == this.interval) {
					statePath = "_CALMING";
				} else {
					statePath = "_CHASE";
				}
				break;
			case KingsHead.STATE_ALERTED:
				statePath = "_CALMING";
				break;
			case KingsHead.STATE_REST:
				if(this.attCount == this.interval) {
					statePath = "_RAGING";
				} else {
					statePath = "_REST";
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
		return this.khImage_DEAD;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.WATER);
		this.moveTypes.add(MovableType.AIR);
		this.moveTypes.add(MovableType.ACID);
	}

	@Override
	public void playerInitiate() {
		if(this.state != KingsHead.STATE_STORM) {
			SoundPlayer.playWAV(GPath.createSoundPath("head_chomp.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		}
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		// Play death sound
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH_CRIT.wav"));
		
		// Open the doors to the arena
		GameScreen.getTile(4, 8).setTileType(new Ground());
		GameScreen.getTile(5, 8).setTileType(new Ground());
		GameScreen.getTile(4, 1).setTileType(new Ground());
		GameScreen.getTile(5, 1).setTileType(new Ground());
		
		// Change music to regular music
		SoundPlayer.changeMidi(GPath.createSoundPath("d_e2m6.mid"), 30);
		
		// Log a final death message
		LogScreen.log("The king is finally dead...");
	}
	
	// Override that prevents death once and starts storm attack
	@Override
	public boolean damageCharacter(int damage) {
		if((this.currentHP - damage) <= 0 && (!this.startStorm)) {
			this.currentHP = 1;
			this.startStorm = true;
			return true;
		} else {
			this.currentHP = this.currentHP - damage;
			InfoScreen.setNPCFocus(this);
			return this.isAlive();
		}
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
				
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = plrX - this.xPos;
		int distY = plrY - this.yPos;
		
		switch(this.state) {
			case KingsHead.STATE_ALERTED:
				// Check for start of storm attack, and then prepare for it if needed
				if(this.checkStorm()) {
					return;
				}
				
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = KingsHead.STATE_PURSUE;
				break;
			case KingsHead.STATE_PURSUE:
				// Check for start of storm attack, and then prepare for it if needed
				if(this.checkStorm()) {
					return;
				}
				
				// If we've pursued the player for long enough, go into rest mode temporarily
				if(this.attCount >= this.interval) {
					// Reset counter, play sound, and switch states
					this.attCount = 0;
					SoundPlayer.playWAV(GPath.createSoundPath("head_rest.wav"));
					this.state = KingsHead.STATE_REST;
					return;
				}
				
				// Attack if player is in one tile radius around player
				if(Math.abs(distX) <= 1 && Math.abs(distY) <= 1) {
					// Mark tiles with damage indicators
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
					this.playerInitiate();
				} else {
					// If not attacking the player, hope closer to them
					
					// Relative movement direction (Initialize at 0)
					int dx = 0;
					int dy = 0;
					
					// Calculate relative movement directions
					if(distX > 0) {
						dx = 1;
					} else if (distX < 0) {
						dx = -1;
					}
					
					if(distY > 0) {
						dy = 1;
					} else if (distY < 0) {
						dy = -1;
					}
					
					// First, try to move diagonal
					// If unsuccessful, move in the direction that it is
					// further from the player.
					if(!this.moveCharacter(dx, dy)) {
						if((Math.abs(distX)) > (Math.abs(distY))) {
							// If movement in the x direction fails, try the y direction
							if(!this.moveCharacter(dx, 0)) {
								this.moveCharacter(0, dy);
							}
						} else {
							// If movement in the y direction fails, try the x direction
							if(!this.moveCharacter(0, dy)) {
								this.moveCharacter(dx, 0);
							}
						}
					}
					
				}
				
				// Increment the counter
				this.attCount += 1;
				break;
			case KingsHead.STATE_REST:
				// Check for start of storm attack, and then prepare for it if needed
				if(this.checkStorm()) {
					return;
				}
				
				// Don't change state back to pursue until we've fully rested
				if(this.attCount >= this.interval) {
					// Alternate between intervals
					int temp = this.interval;
					this.interval = this.altInterval;
					this.altInterval = temp;
					
					// Reset counter, play sound, and switch states
					this.attCount = 0;
					SoundPlayer.playWAV(GPath.createSoundPath("head_chase.wav"));
					this.state = KingsHead.STATE_PURSUE;
					return;
				}
				
				// Increment the counter
				this.attCount += 1;
				break;
			case KingsHead.STATE_STORM:
				// Manage thunder if active
				this.manageThunder(plrX, plrY);
				
				// Command all elements
				if(this.attCount == 0) {
					// Start top thunder
					this.thunderTopActive = true;
				} else if(this.attCount == 2) {
					// Mark all fire spawning tiles with warnings
					this.warnFire();
				} else if(this.attCount == 3) {
					// Spawn fireballs at marked locations
					this.launchFire();
				} else if(this.attCount == 6) {
					// Start bottom thunder
					this.thunderBotActive = true;
				} else if(this.attCount == 7) {
					// Mark all fire spawning tiles with warnings
					this.warnFire();
				} else if(this.attCount == 8) {
					// Spawn fireballs at marked locations
					this.launchFire();
				} else if(this.attCount == 20) {
					// Warn for a left-wise flood
					this.warnFlood(-1, 5);
				} else if(this.attCount == 26) {
					// Launch a left-wise flood
					this.launchFlood(plrX, plrY, -1);
				} else if(this.attCount == 28) {
					// Warn for a right-wise flood
					this.warnFlood(1, 5);
				} else if(this.attCount == 34) {
					// Launch a right-wise flood
					this.launchFlood(plrX, plrY, 1);
				} else if(this.attCount == 35) {
					// Warp to the center of the room
					this.warpKing(plrX, plrY);
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
	
	// Manage the thunder attack, which slowly crawls down the screen
	private void manageThunder(int plrX, int plrY) {
		// If thunder isn't active, return
		if(this.thunderTopActive) {
			// Alternate between marking rows and striking them with thunder
			if((this.thunderTopCount % 2) == 0) {
				// Mark the current row as a warning
				for(int x = 1; x < 9; x++) {
					this.addEffect(new ThunderEffect(x, thunderTopRow, false));
				}
			} else {
				// Strike the thunder down at the current row
				SoundPlayer.playWAV(GPath.createSoundPath("thunder_ATT.wav"));
				for(int x = 1; x < 9; x++) {
					this.addEffect(new ThunderEffect(x, this.thunderTopRow));
					if(plrX == x && plrY == this.thunderTopRow) {
						this.playerInitiate();
					}
				}
				
				// Increment the row of thunder
				this.thunderTopRow += 1;
			}
			
			// Increment the thunder counter
			this.thunderTopCount += 1;
			
			// If we're done with the attack, reset everything
			if(this.thunderTopRow > 7) {
				this.thunderTopActive = false;
				this.thunderTopCount = 0;
				this.thunderTopRow = 2;
			}
		}
		
		if(this.thunderBotActive) {
			// Alternate between marking rows and striking them with thunder
			if((this.thunderBotCount % 2) == 0) {
				// Mark the current row as a warning
				for(int x = 1; x < 9; x++) {
					this.addEffect(new ThunderEffect(x, thunderBotRow, false));
				}
			} else {
				// Strike the thunder down at the current row
				SoundPlayer.playWAV(GPath.createSoundPath("thunder_ATT.wav"));
				for(int x = 1; x < 9; x++) {
					this.addEffect(new ThunderEffect(x, this.thunderBotRow));
					if(plrX == x && plrY == this.thunderBotRow) {
						this.playerInitiate();
					}
				}
				
				// Increment the row of thunder
				this.thunderBotRow += -1;
			}
			
			// Increment the thunder counter
			this.thunderBotCount += 1;
			
			// If we're done with the attack, reset everything
			if(this.thunderBotRow < 2) {
				this.thunderBotActive = false;
				this.thunderBotCount = 0;
				this.thunderBotRow = 7;
			}
		}
	}
	
	// Creates warning of fireballs
	private void warnFire() {
		// Mark all fire spawning tiles with warnings
		for(Dimension d: this.fireCoords) {
			this.addEffect(new WarningIndicator(d.width, d.height));
		}
	}
	
	// Launches fireballs
	private void launchFire() {
		// Spawn fireballs at marked locations
		SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"));
		for(Dimension d: this.fireCoords) {
			int direction = 1 + (-2 * (d.height%2));
			this.addProjectile(new KingFireball(d.width, d.height, direction, 0, this));
		}
	}
	
	// Warns player of flood
	private void warnFlood(int side, int duration) {
		if(side > 0) {
			// Mark the right side
			for(int x = 8; x > 1; x--) {
				for(int y = 2; y < 8; y++) {
					this.addEffect(new WarningIndicator(x, y, duration));
				}
			}
		} else {
			// Mark the left side
			for(int x = 1; x < 8; x++) {
				for(int y = 2; y < 8; y++) {
					this.addEffect(new WarningIndicator(x, y, duration));
				}
			}
		}
	}
	
	// Launches the flood
	private void launchFlood(int plrX, int plrY, int side) {
		// Set the flood down on the arena
		SoundPlayer.playWAV(GPath.createSoundPath("flood_ATT.wav"));
		if(side > 0) {
			// Flood the right side
			for(int x = 8; x > 1; x--) {
				for(int y = 2; y < 8; y++) {
					if(x == 2) {
						this.addEffect(new FloodEffect(x, y, -1, true));
					} else {
						this.addEffect(new FloodEffect(x, y, -1, false));
					}
					if(plrX == x && plrY == y) {
						this.playerInitiate();
					}
				}
			}
		} else {
			// Flood the left side
			for(int x = 1; x < 8; x++) {
				for(int y = 2; y < 8; y++) {
					if(x == 7) {
						this.addEffect(new FloodEffect(x, y, 1, true));
					} else {
						this.addEffect(new FloodEffect(x, y, 1, false));
					}
					if(plrX == x && plrY == y) {
						this.playerInitiate();
					}
				}
			}
		}
	}
	
	// Checks for best corner to warp to and returns it
	private void warpKing(int plrX, int plrY) {
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
		
		// Play warp sound
		SoundPlayer.playWAV(GPath.createSoundPath("warp.wav"));
		
		// Warp to the new coordinates
		this.xPos = bestCorner.width;
		this.yPos = bestCorner.height;
	}
	
	// Checks whether to start the final storm attack, and then prepares for it if needed
	private boolean checkStorm() {
		if(this.startStorm) {
			// Change position
			this.xPos = 4;
			this.yPos = 0;
			
			// Play sound
			SoundPlayer.playWAV(GPath.createSoundPath("head_storm.wav"));
			
			// Reset attack counter and switch state
			this.attCount = 0;
			this.state = KingsHead.STATE_STORM;
			return true;
		} else {
			return false;
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
