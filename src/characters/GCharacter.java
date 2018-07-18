package characters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import ai.PatrolPattern;
import gui.GameScreen;
import gui.InfoScreen;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

// Abstract class containing common behavior for game characters
public abstract class GCharacter implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = 894471161442120373L;

	// File path of the character image (Not needed?)
	protected String imagePath;
	
	// Current and Max Health values
	protected int currentHP, maxHP;
	
	// X and Y position
	protected int xPos, yPos;
	
	// Last X and Y position
	protected int lastX, lastY;
	
	// Origin coordinates
	protected int xOrigin, yOrigin;
	
	// AI state of the character
	protected int state;
	
	// Variables used for patrolling characters
	protected int xPat = 0;
	protected int yPat = 0;
	
	// Range of damage a character can deal
	protected int minDmg, maxDmg;
	
	// Critical chance and critical damage multiplier
	protected double critChance, critMult;
	
	// Can the mouse focus on this enemy or not?
	protected boolean canFocus = true;
	
	// List of what types of tiles the character can move on
	protected ArrayList<MovableType> moveTypes = new ArrayList<MovableType>();
	
	// Patrol pattern governing what characters do when idle
	protected PatrolPattern patrolPattern;
	
	// Gets the character's current image
	abstract public String getImage();
	
	// Gets the image for the character's corpse
	abstract public String getCorpseImage();
	
	// Fetches the name of the character to be displayed in the GUI
	abstract public String getName();
	
	// What the character does when they interact with the player
	abstract public void playerInitiate();
	
	// Governs what the character does each turn they take
	abstract public void takeTurn();
	
	// What the character does when they die
	abstract public void onDeath();
	
	// ON CONSTRUCT: Fills out the information of what character can move on
	abstract public void populateMoveTypes();
	
	// Constructor makes sure moveTypes is populated
	public GCharacter(int startX, int startY) {
		this.xPos = startX;
		this.xOrigin = startX;
		this.lastX = startX;
		
		this.yPos = startY;
		this.yOrigin = startY;
		this.lastY = startY;
		
		this.populateMoveTypes();
	}

	// Moves the character: Returns false if blocked, Returns true if moved successfully
	public boolean moveCharacter(int dx, int dy) {
		
		// Check on collisions for other characters 
		for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
			if((this.xPos + dx) == npc.xPos && (this.yPos + dy) == npc.yPos) {
				return false;
			}
		}
		
		// Then check for barriers, out-of-bounds, and immovable spaces
		TileType tt;
		try {
			// Try to get the TileType of the GameTile
			tt = GameScreen.getTile(this.xPos + dx, this.yPos + dy).getTileType();
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		
		// If NPC can't move here, return without moving
		if(!this.moveTypes.contains(tt.getMovableType())) {
			return false;
		}
		
		// Otherwise, we can update the positions
		
		// Last position
		this.lastX = this.xPos;
		this.lastY = this.yPos;
		
		// New position
		this.xPos += dx;
		this.yPos += dy;
		
		return true;
	}
	
	// Initiates an attack on the player with damage multiplier
	public boolean attackPlayer(double dmgMult) {
		Random r = new Random();
		int dmg;
		int newMin = (int) Math.floor(this.minDmg * dmgMult);
		int newMax = (int) Math.floor(this.maxDmg * dmgMult);
		
		// If the enemy gets lucky, they crit the player
		// Otherwise, calculate damage normally
		if(Math.random() < this.critChance) {
			dmg = (int) Math.floor(newMax * this.critMult);
			//System.out.println(this.getName() + " crit the player for " + Integer.toString(dmg) + " damage.");
			LogScreen.log(this.getName() +
					" crit the player for " + Integer.toString(dmg) + " damage.", GColors.DAMAGE);
		} else {
			dmg = r.nextInt((newMax - newMin) + 1) + newMin;
			LogScreen.log(this.getName() + " did "
					+ Integer.toString(dmg) + " damage to the player.", GColors.DAMAGE);
		}
		
		return EntityManager.getPlayer().damagePlayer(dmg);
	}
	
	// Initiates an attack on the player assuming no damage multiplier
	public boolean attackPlayer() {
		return attackPlayer(1.0);
	}
	
	// Returns True if alive from damage, False if dead from damage
	public boolean damageCharacter(int damage) {
		this.currentHP = this.currentHP - damage;
		InfoScreen.setNPCFocus(this);
		return this.isAlive();
	}
	
	// Heal character: Up to, but not over, max if not over-heal /
	// Up and over max if over-heal
	public void healCharacter(int heal, boolean isOverHeal) {
		this.currentHP = this.currentHP + heal;
		if((!isOverHeal) && (this.currentHP > this.maxHP)) {
			this.currentHP = this.maxHP;
		}
	}
	
	// Overload that assumes heal isn't an over-heal
	public void healCharacter(int heal) {
		this.currentHP = this.currentHP + heal;
		if(this.currentHP > this.maxHP) {
			this.currentHP = this.maxHP;
		}
	}
	
	// Adds a MovableType
	public void addMoveType(MovableType mt) {
		this.moveTypes.add(mt);
	}
	
	// Removes a MovableType if it currently exists in the list
	public boolean removeMoveType(MovableType mt) {
		if(this.moveTypes.contains(mt)) {
			this.moveTypes.remove(mt);
			return true;
		} else {
			return false;
		}
	}
	
	// Checks if character is Alive
	public boolean isAlive() {
		return (this.currentHP > 0);
	}
	
	// Returns the character to their original position
	public void returnToOrigin() {
		///*** Bit of a hack
		/// Return state to idle
		this.state = 0;
		
		// Return Patrol directions to 0
		this.xPat = 0;
		this.yPat = 0;
		
		// Set position to original position
		this.xPos = this.xOrigin;
		this.yPos = this.yOrigin;
		
		// Fill back to full health
		this.currentHP = this.maxHP;
	}
	
	// *******************
	// Getters and Setters
	
	public String getBlankImage() {
		return GPath.NULL;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public int getLastX() {
		return this.lastX;
	}
	
	public int getLastY() {
		return this.lastY;
	}
	
	public int getXPatrol() {
		return this.xPat;
	}
	
	public void setXPatrol(int newVal) {
		// Safeguard for bad input values
		if(newVal > 1 || newVal < -1) {
			return;
		}
		this.xPat = newVal;
	}
	
	public int getYPatrol() {
		return this.yPat;
	}
	
	public void setYPatrol(int newVal) {
		// Safeguard for bad input values
		if(newVal > 1 || newVal < -1) {
			return;
		}
		this.yPat = newVal;
	}
	
	public ArrayList<MovableType> getMoveTypes() {
		return this.moveTypes;
	}
	
	public PatrolPattern getPatrolPattern() {
		return this.patrolPattern;
	}
	
	public int getCurrentHP() {
		return this.currentHP;
	}
	
	public int getMaxHP() {
		return this.maxHP;
	}
	
	public boolean getFocusable() {
		return this.canFocus;
	}
}
