package characters.bosses;


import ai.PatrolPattern;
import characters.GCharacter;
import characters.Player;
import effects.FireEffect;
import effects.GEffect;
import gui.InfoScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

public class SnakeNuke extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -6136587834525261512L;
	
	// Modifiers/Statistics

	private int MAX_HP = 10;
	
	private int ARMOR_VAL = 100;
	
	private int MIN_DMG = 10;
	private int MAX_DMG = 10;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_HIDDEN = 0;
	private static final int STATE_FLIGHT = 1;
	//private static final int STATE_BOOM = 2; TODO - Maybe not needed
	
	//----------------------------
	
	// Additional parameters
	
	// Coordinates on which the Nuke hides when not active
	private final int xHide = 8;
	private final int yHide = 0;
	
	// X and Y directional speeds
	private int xSpeed = 0;
	private int ySpeed = 0;
	
	// Flag determining if Nuke was fired from SnakeTank
	protected boolean wasFired = false;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK);
	private String nkImage_base = "nuke";
	
	// Constructor
	public SnakeNuke(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeNuke.STATE_HIDDEN;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.canFocus = false;
		
		this.imagePath = this.getImage();
	}

	public String getName() {
		return "Reflectable Nuke";
	}
	
	@Override
	public String getImage() {
		
		// TEMP TODO
		//return this.imageDir + this.nkImage_base + "_full.png";
		if(this.state == SnakeNuke.STATE_FLIGHT) {
			return GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png");
		} else {
			return GPath.NULL;
		}
		
//		String imgPath = this.imageDir + this.stImage_base;
//		String hpPath = "";
//		String statePath = "";
//		if(this.currentHP > 0) {
//			hpPath = "_full";
//		} else {
//			hpPath = "_dead";
//			return (imgPath + hpPath + ".png");
//		}
		
//		switch(this.state) {
//			case SnakeTank.STATE_IDLE:
//			case SnakeTank.STATE_PURSUE:
//				// No extra path
//				break;
//			case SnakeTank.STATE_ALERTED:
//				statePath = "_ALERT";
//				break;
//			case SnakeTank.STATE_PREP_CHAINGUN:
//				statePath = "_PREP_SHOOT";
//				break;
//			case SnakeTank.STATE_ATT_CHAINGUN:
//				statePath = "_ATT_SHOOT";
//				break;
//			case SnakeTank.STATE_PREP_NUKE:
//				statePath = "_PREP_SHOOT";
//				break;
//			case SnakeTank.STATE_ATT_NUKE:
//				statePath = "_ATT_SHOOT";
//				break;
//			default:
//				System.out.println
//					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
//				return GPath.NULL;
//		}
//		
//		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.AIR);
		this.moveTypes.add(MovableType.WATER);
		this.moveTypes.add(MovableType.ACID);
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		// Do nothing
	}
	
	// Override that deflects speed of Nuke on hit
	@Override
	public boolean damageCharacter(int damage) {
		this.playerDeflect();
		this.currentHP = this.currentHP - damage;
		InfoScreen.setNPCFocus(this);
		return this.isAlive();
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
		
		switch(this.state) {
			case SnakeNuke.STATE_HIDDEN:
				// If we were fired from the SnakeTank, appear on the screen and start flying
				if(this.wasFired) {
					// Reset fired flag
					this.wasFired = false;
					
					// Set focusable to true
					this.canFocus = true;
					
					// Set initial flying speed
					this.setDirection(-1, 0);
					
					// Set new X and Y position of Nuke based on position of SnakeTank
					EntityManager em = EntityManager.getInstance();
					for(GCharacter npc: em.getNPCManager().getCharacters()) {
						if(npc != this && npc instanceof SnakeTank) {
							// Set position to left of SnakeTank
							this.xPos = (npc.getXPos() - 1);
							this.yPos = npc.getYPos();
							break; // Breaks out of 'for' loop
						}
					}
					
					// Change state to flight
					this.state = SnakeNuke.STATE_FLIGHT;
				}
				
				break; // Breaks out of 'switch'
			case SnakeNuke.STATE_FLIGHT:
				// If we cannot move in our given direction, then we must have blown up
				if(!this.moveCharacter(this.xSpeed, this.ySpeed)) {
					// Do explosion, then hide away until active again
					
					// Explode based of current position and speed of Nuke
					if(this.xPos <= 6) {
						// Check for special cases, but otherwise explode left side
						if(this.yPos == 2 && this.ySpeed == -1) {
							// Explode top of arena
							this.explodeArea(0, -1);
						} else if(this.yPos == 5 && this.ySpeed == 1) {
							// Explode bottom of arena
							this.explodeArea(0, 1);
						} else {
							// Explode left side of arena
							this.explodeArea(-1, 0);
						}
					} else {
						// Check for special cases, but otherwise explode right side
						if(this.yPos == 2 && this.ySpeed == -1) {
							// Explode top of arena
							this.explodeArea(0, -1);
						} else if(this.yPos == 5 && this.ySpeed == 1) {
							// Explode bottom of arena
							this.explodeArea(0, 1);
						} else {
							// Explode right side of arena
							this.explodeArea(1, 0);
						}
					}
					
					// Hide the nuke
					this.hideNuke();
					
					// Change state
					this.state = SnakeNuke.STATE_HIDDEN;
				}
				
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
	// Sets new direction of the Nuke based on relative direction from player 
	private void playerDeflect() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		int plrX = player.getXPos();
		int plrY = player.getYPos(); 
		
		// Set new direction of Nuke based on player's relative position to Nuke
		if(plrY > this.yPos) {
			this.setDirection(0, -1);
		} else if(plrY < this.yPos) {
			this.setDirection(0, 1);
		} else if(plrX > this.xPos) {
			this.setDirection(-1, 0);
		} else {
			this.setDirection(1, 0);
		}
	}
	
	// Sets new speed directions for Nuke
	protected void setDirection(int newXSpeed, int newYSpeed) {
		this.xSpeed = newXSpeed;
		this.ySpeed = newYSpeed;
	}
	
	// Hides the Nuke from player
	private void hideNuke() {
		// Set position at given 'hide' coordinates
		this.xPos = this.xHide;
		this.yPos = this.yHide;
		
		// Disallow focusing
		this.canFocus = false;
	}
	
	// Explodes a particular area of the boss arena
	private void explodeArea(int xSpot, int ySpot) {
		// Fetch reference to EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Fetch reference to player and their coordinates
		Player player = em.getPlayer();
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Fetch reference to SnakeTank and its coordinates
		SnakeTank tank = null;
		for(GCharacter npc: em.getNPCManager().getCharacters()) {
			if(npc != this && npc instanceof SnakeTank) {
				tank = (SnakeTank) npc;
				break;
			}
		}
		
		// Fail and return if we found no instance of the tank
		if(tank == null) {
			System.out.println("Tank not found!");
			return;
		}
		
		// Let the tank know that the Nuke has exploded
		tank.nukeDead = true;
		
		if(xSpot == 0 && ySpot == 1) {
			// Bottom area
			
			for(int x = 4; x < 9; x++) {
				for(int y = 4; y < 6; y++) {
					// Create effect
					this.addEffect(new FireEffect(x, y));
					
					// Check for player collision
					if(plrX == x && plrY == y) {
						this.playerInitiate();
					}
					
					// Check for SnakeTank collision
					if(tank.getXPos() == x && tank.getYPos() == y) {
						// Damage tank if we hit it
						tank.damageCharacter(10);
					}
				}
			}
			
		} else if(xSpot == 0 && ySpot == -1) {
			// Top area
			
			for(int x = 4; x < 9; x++) {
				for(int y = 2; y < 4; y++) {
					// Create effect
					this.addEffect(new FireEffect(x, y));
					
					// Check for player collision
					if(plrX == x && plrY == y) {
						this.playerInitiate();
					}
					
					// Check for SnakeTank collision
					if(tank.getXPos() == x && tank.getYPos() == y) {
						// Damage tank if we hit it
						tank.damageCharacter(10);
					}
				}
			}
			
		} else if(xSpot == 1 && ySpot == 0) {
			// Right area
			
			for(int x = 7; x < 9; x++) {
				for(int y = 2; y < 6; y++) {
					// Create effect
					this.addEffect(new FireEffect(x, y));
					
					// Check for player collision
					if(plrX == x && plrY == y) {
						this.playerInitiate();
					}
					
					// Check for SnakeTank collision
					if(tank.getXPos() == x && tank.getYPos() == y) {
						// Damage tank if we hit it
						tank.damageCharacter(10);
					}
				}
			}
			
		} else if(xSpot == -1 && ySpot == 0) {
			// Left area
			
			for(int x = 4; x < 6; x++) {
				for(int y = 2; y < 6; y++) {
					// Create effect
					this.addEffect(new FireEffect(x, y));
					
					// Check for player collision
					if(plrX == x && plrY == y) {
						this.playerInitiate();
					}
					
					// Check for SnakeTank collision
					if(tank.getXPos() == x && tank.getYPos() == y) {
						// Damage tank if we hit it
						tank.damageCharacter(10);
					}
				}
			}
			
		} else {
			// Print debug message if passed incorrect parameters
			System.out.println("Explosion failed! X=" + xSpot + " Y=" + ySpot);
		}
	}
	
	// Shortening of adding effect for convenience and easy code reading
	private void addEffect(GEffect fx) {
		EntityManager.getInstance().getEffectManager().addEffect(fx);
	}
	
}
