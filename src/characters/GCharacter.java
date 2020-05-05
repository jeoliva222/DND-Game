package characters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import ai.PatrolPattern;
import buffs.Buff;
import buffs.RageBuff;
import characters.allies.Player;
import gui.GameScreen;
import gui.InfoScreen;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

/**
 * Abstract class containing common behavior for game characters
 * @author jeoliva
 */
public abstract class GCharacter implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = 894471161442120373L;
	
	// Current and Max Health values
	protected int currentHP, maxHP;
	
	// Amount of damage blocked on each attack (0 by default)
	protected int armor = 0;
	
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
	
	// Is the character simply an interactable object?
	protected boolean isInteractable = false;
	
	// List of what types of tiles the character can move on
	protected Short moveTypes = 0;
	
	// List of current buffs/debuffs
	private ArrayList<Buff> buffs = new ArrayList<Buff>();
	
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
		
		populateMoveTypes();
	}

	/**
	 * Moves the character
	 * @param dx X Movement
	 * @param dy Y Movement
	 * @return True if moved successfully | False if blocked 
	 */
	public boolean moveCharacter(int dx, int dy) {
		// Check if we are rooted
		if (hasBuff(Buff.ROOTED)) {
			return false;
		}
		
		// Get the new relative coordinates of the character
		int newX = (xPos + dx);
		int newY = (yPos + dy);
		
		// Check for collision with player
		Player plr = EntityManager.getInstance().getPlayer();
		if (newX == plr.getXPos() && newY == plr.getYPos()) {
			return false;
		}
		
		// Check on collisions for other characters 
		for (GCharacter npc : EntityManager.getInstance().getNPCManager().getCharacters()) {
			if (newX == npc.xPos && newY == npc.yPos) {
				return false;
			}
		}
		
		// Then check for barriers, out-of-bounds, and immovable spaces
		TileType tt;
		try {
			// Try to get the TileType of the GameTile
			tt = GameScreen.getTile(newX, newY).getTileType();
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		
		// If NPC can't move here, return without moving
		if (!canMove(tt.getMovableType())) {
			return false;
		}
		
		// Otherwise, we can update the positions
		
		// New position
		this.xPos += dx;
		this.yPos += dy;
		
		return true;
	}
	
	/**
	 * Initiates an attack on the player with a damage multiplier
	 * @param dmgMult Damage multiplier
	 * @return True if player alive after damage | False if not
	 */
	public boolean attackPlayer(double dmgMult) {
		// Fetch reference to the player
		Player plr = EntityManager.getInstance().getPlayer();
		
		Random r = new Random();
		int dmg;
		int targetArmor = plr.getArmor();
		
		// If character has rage, buff the damage multiplier
		if (hasBuff(Buff.RAGE)) {
			dmgMult = dmgMult * RageBuff.dmgBoost;
		}
		
		int newMin = (int) Math.floor(minDmg * dmgMult);
		int newMax = (int) Math.floor((maxDmg * dmgMult) - targetArmor);
		
		// Maximum damage cannot drop below 0
		if (newMax < 0) {
			newMax = 0;
		}
		
		// If new maximum damage is below minimum damage, drop the minimum damage to match
		if (newMin > newMax) {
			newMin = newMax;
		}
		
		// If the enemy gets lucky, they crit the player
		// Otherwise, calculate damage normally
		if (Math.random() < critChance) {
			// Get critical damage value
			dmg = (int) Math.floor(newMax * critMult);
		} else {
			// Get normal damage value
			dmg = r.nextInt((newMax - newMin) + 1) + newMin;
		}
		
		// Limit minimum damage value at 0
		if (dmg < 0) {
			dmg = 0;
		}
		
		// Log result
		if (dmg == 0) {
			LogScreen.log(getName() + " tickled the player's defenses", GColors.BASIC);
		} else {
			LogScreen.log(getName() + " did " + Integer.toString(dmg) + " damage to the player.", GColors.DAMAGE);
		}
		
		// Damage the player and return whether the player survived or not
		return plr.damagePlayer(dmg);
	}
	
	/**
	 * Initiates an attack on the player
	 */ 
	public boolean attackPlayer() {
		return attackPlayer(1.0);
	}
	
	/**
	 * Persists all the buffs on the character for the turn
	 */
	public void persistBuffs() {
		ArrayList<Buff> hearse = new ArrayList<Buff>();
		for (Buff b : this.buffs) {
			// Does the buff's on-turn effect
			b.doTurnEffect();
			
			// Checks if buff is still active
			if (b.persist()) {
				LogScreen.log(getName() + "'s " + b.getName() + " wore off.");
				hearse.add(b);
			}
		}
		
		// Remove expired buffs
		for (Buff b : hearse) {
			removeBuff(b);
		}
	}
	
	/**
	 * Damages this character
	 * @param damage Damage dealt to this character
	 * @return True if alive after damage | False if dead after damage
	 */
	public boolean damageCharacter(int damage) {
		this.currentHP = (currentHP - damage);
		InfoScreen.setNPCFocus(this);
		return isAlive();
	}
	
	/**
	 * Heals this character, with the option to over-heal
	 * @param heal Health points to heal the character
	 * @param isOverHeal True if healing can go above maximum HP | False if not
	 */
	public void healCharacter(int heal, boolean isOverHeal) {
		this.currentHP = currentHP + heal;
		if ((!isOverHeal) && (currentHP > maxHP)) {
			this.currentHP = maxHP;
		}
	}
	
	/**
	 * Heals this character (cannot go above maximum HP)
	 * @param heal Healing done to character
	 */
	public void healCharacter(int heal) {
		healCharacter(heal, false);
	}
	
	/**
	 * Fully heals this character
	 */
	public void fullyHeal() {
		this.currentHP = maxHP;
	}
	
	/**
	 * Checks if this character can move to a given MovableType
	 * @param mt MovableType to check
	 * @return True if the character can move to a given MovableType | False if not
	 */
	public boolean canMove(Short mt) {
		return MovableType.canMove(moveTypes, mt);
	}
	
	/**
	 * Adds an allowed MovableType to this character
	 * @param mt MovableType to add
	 */
	public void addMoveType(Short mt) {
		this.moveTypes = (short) (moveTypes | mt);
	}
	
	/**
	 * Removes an allowed MovableType from this character
	 * @param mt MovableType to remove
	 */
	public void removeMoveType(Short mt) {
		mt = (short) (~mt & Short.MAX_VALUE);
		this.moveTypes = (short) (moveTypes & mt);
	}
	
	/**
	 * Checks if character is Alive
	 * @return True if alive | False if dead
	 */
	public boolean isAlive() {
		return (currentHP > 0);
	}
	
	/**
	 * Returns the character to their original position/state
	 */
	public void returnToOrigin() {
		// Return state to idle
		this.state = 0;
		
		// Return Patrol directions to 0
		this.xPat = 0;
		this.yPat = 0;
		
		// Set position to original position
		this.xPos = xOrigin;
		this.yPos = yOrigin;
		
		// Clear all debuffs
		clearBuffs();
		
		// Fill back to full health
		fullyHeal();
		
		// Reset other parameters
		resetParams();
	}
	
	public void resetParams() {
		// Nothing by default
	}
	
	/**
	 * Updates the coordinates that the character was on last turn
	 */
	public void updateLastCoords() {
		this.lastX = xPos;
		this.lastY = yPos;
	}
	
	/**
	 * Outputs a formatted string of the character's statistics (to be used by InfoScreen GUI)
	 * @return Formatted String of this character's statistics
	 */
	public String getStatDescString() {
		String output = "DMG: ";
		output += (minDmg + " - " + maxDmg + "\n");
		output += ("CRITS: " + toPercent(critChance) + " / " + critMult + "x");
		return output;
	}
	
	private static String toPercent(double num) {
		// Convert double to int percent
		int percent = (int) (num * 100);
		
		// Convert to string
		String output = Integer.toString(percent) + "%";
		
		// Return output
		return output;
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
		if (newVal > 1 || newVal < -1) {
			return;
		}
		this.xPat = newVal;
	}
	
	public int getYPatrol() {
		return this.yPat;
	}
	
	public void setYPatrol(int newVal) {
		// Safeguard for bad input values
		if (newVal > 1 || newVal < -1) {
			return;
		}
		this.yPat = newVal;
	}
	
	public Short getMoveTypes() {
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
	
	public int getArmor() {
		return this.armor;
	}
	
	public void addArmor(int armorValue) {
		this.armor += armorValue;
	}
	
	public boolean getFocusable() {
		return this.canFocus;
	}
	
	public boolean getIfInteractable() {
		return this.isInteractable;
	}
	
	public ArrayList<Buff> getBuffs() {
		return this.buffs;
	}
	
	/**
	 * Adds a buff/debuff to this character
	 * @param buff Buff/Debuff to add
	 */
	public void addBuff(Buff buff) {
		if (hasBuff(buff.getName())) {
			// Extend the current buff
			extendBuff(buff);
		} else {
			// Add new buff
			buff.setCharacter(this);
			buff.activate();
			buffs.add(buff);
			
			if (buff.isDebuff()) {
				LogScreen.log(getName() + " inflicted with " + buff.getName() + "!", GColors.ATTACK);
			} else {
				LogScreen.log(getName() + " gained " + buff.getName() + "!");
			}
		}
	}
	
	/**
	 * Removes a buff/debuff from this character
	 * @param buff Buff/Debuff to remove
	 */
	public boolean removeBuff(Buff buff) {
		if (buffs.remove(buff)) {
			buff.deactivate();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes a buff/debuff from this character
	 * @param name Name of buff/debuff to remove
	 */
	public boolean removeBuff(String name) {
		Buff removeBuff = null;
		for (Buff b : buffs) {
			if (b.getName().equals(name)) {
				removeBuff = b;
				break;
			}
		}
		
		if (removeBuff != null) {
			removeBuff.deactivate();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Clears all buffs/debuffs from this character
	 */
	public void clearBuffs() {
		for (Buff b : buffs) {
			b.deactivate();
		}
		
		buffs.clear();
	}
	
	/**
	 * Extends a buff/debuff on this character
	 * @param buff Buff/Debuff to extend
	 */
	public void extendBuff(Buff buff) {
		Buff extendBuff = null;
		for (Buff b : buffs) {
			if (b.getName().equals(buff.getName())) {
				extendBuff = b;
				break;
			}
		}
		
		if (extendBuff != null) {
			if (buff.getDuration() > extendBuff.getDuration()) {
				extendBuff.setDuration(buff.getDuration());
			}
		}
	}
	
	/**
	 * Checks if this character has a particular buff/debuff
	 * @param name Name of buff/debuff to check
	 * @return True if character has buff/debuff | False if not
	 */
	public boolean hasBuff(String name) {
		for (Buff b : buffs) {
			if (b.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}
}
