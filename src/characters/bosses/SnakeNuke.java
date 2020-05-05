package characters.bosses;

import ai.PatrolPattern;
import characters.GCharacter;
import characters.allies.Player;
import effects.FireEffect;
import effects.GEffect;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

/**
 * Class that represents Snake Nuke entity
 * @author jeoliva
 */
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
	
	//----------------------------
	
	// Additional parameters
	
	// Coordinates on which the Nuke hides when not active
	private final int xHide = 8;
	private final int yHide = 0;
	
	// X and Y directional speeds
	protected int xSpeed = 0;
	protected int ySpeed = 0;
	
	// Flag determining if Nuke was fired from SnakeTank
	protected boolean wasFired = false;
	
	// Flag determining whether or not light at end of bomb is glowing (Visual)
	private boolean isGlowing = false;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK);
	private String nkImage_base = "bomb";
	
	// Constructor
	public SnakeNuke(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SnakeNuke.STATE_HIDDEN;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.canFocus = false;
	}

	public String getName() {
		return "Reflectable Nuke";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + nkImage_base);
		String lightPath = "";
		String dirPath;
		
		switch (state) {
			case SnakeNuke.STATE_HIDDEN:
				return GPath.NULL;
			case SnakeNuke.STATE_FLIGHT:
				// Glowing light
				if (isGlowing) {
					lightPath = "_A";
				} else {
					lightPath = "_B";
				}
				
				// Get absolute value of speeds
				int absDX = Math.abs(xSpeed);
				int absDY = Math.abs(ySpeed);
				
				// Check which of the two relative directional speeds is greater
				if (absDX >= absDY) {
					// Check if arrow is flying left or right
					if (xSpeed >= 0) {
						dirPath = "_RIGHT";
					} else {
						dirPath = "_LEFT";
					}
				} else {
					// Check if arrow is flying up or down
					if (ySpeed >= 0) {
						dirPath = "_DOWN";
					} else {
						dirPath = "_UP";
					}
				}
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + lightPath + dirPath + ".png");
	}
	
	public String getCorpseImage() {
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ACID)));
		this.moveTypes = ((short) (moveTypes + (MovableType.PIT)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		attackPlayer();
	}
	
	@Override
	public void onDeath() {
		// Do nothing
	}
	
	// Override that deflects speed of Nuke on hit
	@Override
	public boolean damageCharacter(int damage) {
		playerDeflect();
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
		
		switch (state) {
			case SnakeNuke.STATE_HIDDEN:
				// If we were fired from the SnakeTank, appear on the screen and start flying
				if (wasFired) {
					// Reset fired flag
					this.wasFired = false;
					
					// Set focusable to true
					this.canFocus = true;
					
					// Set initial flying speed
					setDirection(-1, 0);
					
					// Set new X and Y position of Nuke based on position of SnakeTank
					EntityManager em = EntityManager.getInstance();
					for (GCharacter npc: em.getNPCManager().getCharacters()) {
						if (npc != this && npc instanceof SnakeTank) {
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
				if (!moveCharacter(xSpeed, ySpeed)) {
					// Do explosion, then hide away until active again
					
					// Play explosion sound TODO
					SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"));
					
					// Explode based of current position and speed of Nuke
					if (xPos <= 6) {
						// Check for special cases, but otherwise explode left side
						if (yPos == 2 && ySpeed == -1) {
							// Explode top of arena
							explodeArea(0, -1);
						} else if (yPos == 5 && ySpeed == 1) {
							// Explode bottom of arena
							explodeArea(0, 1);
						} else {
							// Explode left side of arena
							explodeArea(-1, 0);
						}
					} else {
						// Check for special cases, but otherwise explode right side
						if (yPos == 2 && ySpeed == -1) {
							// Explode top of arena
							explodeArea(0, -1);
						} else if (yPos == 5 && ySpeed == 1) {
							// Explode bottom of arena
							explodeArea(0, 1);
						} else {
							// Explode right side of arena
							explodeArea(1, 0);
						}
					}
					
					// Hide the Nuke
					hideNuke();
					
					// Change state
					this.state = SnakeNuke.STATE_HIDDEN;
				}
				
				// Toggle the glow of the light
				this.isGlowing = !(isGlowing);
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
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
		if (plrY > yPos) {
			setDirection(0, -1);
		} else if (plrY < yPos) {
			setDirection(0, 1);
		} else if (plrX > xPos) {
			setDirection(-1, 0);
		} else {
			setDirection(1, 0);
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
		this.xPos = xHide;
		this.yPos = yHide;
		
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
		for (GCharacter npc: em.getNPCManager().getCharacters()) {
			if (npc != this && npc instanceof SnakeTank) {
				tank = (SnakeTank) npc;
				break;
			}
		}
		
		// Fail and return if we found no instance of the tank
		if (tank == null) {
			System.out.println("Tank not found!");
			return;
		}
		
		// Let the tank know that the Nuke has exploded
		tank.informNukeDead();
		
		if (xSpot == 0 && ySpot == 1) {
			// Bottom area
			
			for (int x = 4; x < 9; x++) {
				for (int y = 4; y < 6; y++) {
					// Create effect
					addEffect(new FireEffect(x, y));
					
					// Check for player collision
					if (plrX == x && plrY == y) {
						playerInitiate();
					}
					
					// Check for SnakeTank collision
					if (tank.getXPos() == x && tank.getYPos() == y) {
						// Damage tank if we hit it
						tank.damageCharacter(10);
					}
				}
			}
			
		} else if (xSpot == 0 && ySpot == -1) {
			// Top area
			
			for (int x = 4; x < 9; x++) {
				for (int y = 2; y < 4; y++) {
					// Create effect
					addEffect(new FireEffect(x, y));
					
					// Check for player collision
					if (plrX == x && plrY == y) {
						playerInitiate();
					}
					
					// Check for SnakeTank collision
					if (tank.getXPos() == x && tank.getYPos() == y) {
						// Damage tank if we hit it
						tank.damageCharacter(10);
					}
				}
			}
			
		} else if (xSpot == 1 && ySpot == 0) {
			// Right area
			
			for (int x = 7; x < 9; x++) {
				for (int y = 2; y < 6; y++) {
					// Create effect
					addEffect(new FireEffect(x, y));
					
					// Check for player collision
					if (plrX == x && plrY == y) {
						playerInitiate();
					}
					
					// Check for SnakeTank collision
					if (tank.getXPos() == x && tank.getYPos() == y) {
						// Damage tank if we hit it
						tank.damageCharacter(10);
					}
				}
			}
			
		} else if(xSpot == -1 && ySpot == 0) {
			// Left area
			
			for (int x = 4; x < 6; x++) {
				for (int y = 2; y < 6; y++) {
					// Create effect
					addEffect(new FireEffect(x, y));
					
					// Check for player collision
					if (plrX == x && plrY == y) {
						playerInitiate();
					}
					
					// Check for SnakeTank collision
					if (tank.getXPos() == x && tank.getYPos() == y) {
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
	
	// Function used by SnakeTank to inform the SnakeNuke that it was fired
	protected void fireNuke() {
		this.wasFired = true;
	}
	
	// Shortening of adding effect for convenience and easy code reading
	private void addEffect(GEffect fx) {
		EntityManager.getInstance().getEffectManager().addEffect(fx);
	}
	
}
