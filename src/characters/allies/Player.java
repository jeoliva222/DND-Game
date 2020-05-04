package characters.allies;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import buffs.Buff;
import characters.GCharacter;
import gui.GameInitializer;
import gui.GameScreen;
import gui.GameWindow;
import gui.InfoScreen;
import gui.InventoryScreen;
import gui.LogScreen;
import gui.StatusScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import items.GPickup;
import levels.MapArea;
import levels.WorldMap;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;
import weapons.Armory;
import weapons.Weapon;

/**
 * Class that defines the game's player and their functionality
 * @author jeoliva
 */
public class Player implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = -1569715616453904946L;
	
	// Player image paths
	private final String playerImage_FULL = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_full.png");
	private final String playerImage_BRUISED = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_bruised.png");
	private final String playerImage_INJURED = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_injured.png");
	private final String playerImage_FATAL = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_fatal2.png");
	private final String playerImage_DEAD = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_dead2.png");
	private final String playerImage_DEAD_CRIT = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_dead2_crit.png");
	
	// X and Y positions on the current screen
	private int xPos, yPos;
	
	// Last X and Y Position on current screen
	private int lastX, lastY;
	
	// X and Y positions dictating what screen the player is on in their current area (grid of levels)
	private int levelX, levelY;
	
	// X and Y positions dictating which area the player is on in the world map (grid of areas)
	private int areaX, areaY;
	
	// Maximum and Current health amounts
	private int maxHP, currentHP;
	
	// Amount of armor that negates damage
	private int armor;
	
	// Number of tiles away player can see in dark areas
	private int vision;
	
	// List of tile categories you can move on
	private Short moveTypes = 0;
	
	// List of current buffs/debuffs
	private ArrayList<Buff> buffs = new ArrayList<Buff>();
	
	// Equipped Weapon (Active)
	// Default is Armory.bareFists
	private Weapon equippedWeapon = Armory.bareFists;
	
	// Sheathed Weapon (Inactive)
	// Default is Armory.bareFists
	private Weapon sheathedWeapon = Armory.bareFists;
	
	// Constructor
	public Player() {
		// Set player tile position
		// Default is 6, 2
		this.xPos = 6;
		this.yPos = 2;
		this.lastX = xPos;
		this.lastY = yPos;
		
		/// ***TEMP*** Set the area coordinates
		// Default is 0, 1
		this.areaX = 0;
		this.areaY = 1;
		
		/// ***TEMP*** Set the level coordinates
		// Default is 3, 2
		this.levelX = 3;
		this.levelY = 2;
		
		// Set health values
		// Default is 20
		this.maxHP = 20;
		this.currentHP = this.maxHP;
		
		// Set armor value
		// Default is 0
		this.armor = 0;
		
		// Set vision value
		// Default is 2
		this.vision = 2;
		
		// Dictate what player can move on
		populateMoveTypes();
	}
	
	/**
	 * Moves the player in a particular direction
	 * @param dx X-movement
	 * @param dy Y-movement
	 * @return True if player moved | False if player remained in place
	 */
	public boolean movePlayer(int dx, int dy) {
		
		// Check if player is alive first
		if (!isAlive()) {
			// If player is dead, don't do anything
			return false;
		}
		
		// Next check to see if your weapon will hit anything
		if (equippedWeapon.tryAttack(dx, dy)) {
			// If we hit something, we don't move
			return false;
		}
		
		// Check if we are rooted and can't move
		if (hasBuff(Buff.ROOTED)) {
			return false;
		}
		
		// Discharge weapons if player isn't attacking this turn
		dischargeWeapons();
		
		// Then check for barriers, out-of-bounds, and immovable spaces
		TileType tt = null;
		try {
			// Try to get the TileType of the GameTile
			tt = GameScreen.getTile(xPos + dx, yPos + dy).getTileType();
		} catch (IndexOutOfBoundsException e) {
			// If we're going out of bounds, that means we're switching screens
			
			// Try to swap levels
			if (GameWindow.getScreen().swapLevel(dx, dy)) {
				// Change the player's position
				
				// Get the new relative coordinates of the player
				int newX = (xPos + dx);
				int newY = (yPos + dy);
				
				// Load the border limits of the screen
				int xMax = GameInitializer.xDimen;
				int yMax = GameInitializer.yDimen;
				
				// Switch the player x-wise to other end of the screen
				// if past the limits
				if (newX >= xMax) {
					newX = 0;
				} else if (newX < 0) {
					newX = xMax - 1;
				}
				
				// Switch the player y-wise to other end of the screen
				// if past the limits
				if (newY >= yMax) {
					newY = 0;
				} else if (newY < 0) {
					newY = yMax - 1;
				}
				
				// Try to place player at the correct position on the new screen
				if (leapPlayer(newX, newY, true)) {
					return true;
				} else {
					System.out.println("Screen existed, but 'leap' function failed.");
					return false;
				}
				
			} else {
				// Return false to indicate we cannot move from the swap
				return false;
			}
		}
		
		// If immovable, return false without changing position
		if (tt == null || !canMove(tt.getMovableType())) {
			return false;
		}
		
		// If all checks pass, update the player position
		this.xPos += dx;
		this.yPos += dy;
		
		// Grab any items at the new position
		grabItems();
		
		// Trigger the TileType's onStep method
		tt.onStep();
		
		// Return true because we moved successfully
		return true;
	}
	
	/**
	 * Leaps player to an entirely new position.
	 * If considered a 'teleport', this will potentially tele-frag enemies.
	 * @param newX New X-position
	 * @param newY New Y-position
	 * @param isTeleport True = Can warp inside enemies and tele-frag them | False = Cannot move into enemies
	 */
	public boolean leapPlayer(int newX, int newY, boolean isTeleport) {
		// Check if player is alive first
		if (!isAlive()) {
			// If player is dead, don't do anything
			return false;
		}
		
		// Check if we are rooted and can't move
		if (hasBuff(Buff.ROOTED)) {
			return false;
		}
		
		// Discharge weapons if player isn't attacking this turn
		dischargeWeapons();
		
		// Check for barriers, out-of-bounds, and immovable spaces
		TileType tt;
		try {
			// Try to get the TileType of the GameTile
			tt = GameScreen.getTile(newX, newY).getTileType();
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		
		// If immovable, return false without changing position
		if (!canMove(tt.getMovableType())) {
			return false;
		}
		
		// Finally check for collision
		for (GCharacter npc : EntityManager.getInstance().getNPCManager().getCharacters()) {
			if (newX == npc.getXPos() && newY == npc.getYPos()) {
				if (isTeleport) {
					npc.damageCharacter(1000);
					LogScreen.log("Player telefragged " + npc.getName() + ".", GColors.ATTACK);
					break;
				} else {
					return false;
				}
			}
		}
		
		// If all checks pass, update the player position
		this.xPos = newX;
		this.yPos = newY;
		
		// Grab any items at the new position
		grabItems();
		
		// Trigger the TileType's onStep method
		tt.onStep();
		
		// Return true because we successfully moved
		return true;
	}
	
	/**
	 * Leaps player to an entirely new position.
	 * Cannot move to a space occupied by an enemy.
	 * @param newX New X-position
	 * @param newY New Y-position
	 */
	public boolean leapPlayer(int newX, int newY) {
		return leapPlayer(newX, newY, false);
	}
	
	/**
	 * Attempt to grab any items at the player's current position
	 */
	public void grabItems() {
		// Grab any items at the new position
		ArrayList<GPickup> grabbedItems = new ArrayList<GPickup>();
		for (GPickup pu: EntityManager.getInstance().getPickupManager().getPickups()) {
			if (pu.getXPos() == xPos && pu.getYPos() == yPos) {
				// Add item to inventory if we have inventory space
				if (InventoryScreen.addItem(pu.getItem())) {
				
					// Set focus on new item
					InfoScreen.setItemFocus(pu.getItem());
					
					// Log what player grabbed
					LogScreen.log("Player looted "+pu.getItem().getName()+".", GColors.ITEM);
					
					// Mark item as grabbed
					grabbedItems.add(pu);
				}
			}
		}
		
		// Remove grabbed items from level
		for (GPickup rpu: grabbedItems) {
			// Remove item from manager
			EntityManager.getInstance().getPickupManager().removePickup(rpu);
		}
		
		// If we grabbed at least one item, play item-get sound
		if (grabbedItems.size() > 0) {
			// Play a sound
			SoundPlayer.playWAV(GPath.createSoundPath("Item_GET.wav"));
		}
		
		// Dereference tracker list
		grabbedItems = null;
	}
	
	/**
	 * Subtracts health from player and kills them if they reach 0 health
	 * @param damage Damage to deal to player
	 * @return True if alive after damage, False if dead after damage
	 */
	public boolean damagePlayer(int damage) {
		// If damage is equal to or less than 0, do nothing and return
		if (damage <= 0) {
			return true;
		}
		
		// Otherwise, deal damage, play hurt sound, and declare whether play is dead
		this.currentHP = (currentHP - damage);
		if (isAlive()) {
			SoundPlayer.playWAV(GPath.createSoundPath("Player_HURT.wav"), -15);
			return true;
		} else {
			if (currentHP < -100) {
				// Play no death sound
			} else {
				// Play a death sound
				SoundPlayer.playWAV(GPath.createSoundPath("Player_DEATH.wav"), -5);
			}
			LogScreen.log("Player has died! Game over.", GColors.DAMAGE);
			return false;
		}
	}
	
	/**
	 * Persists all the buffs on the player for the turn
	 */
	public void persistBuffs() {
		ArrayList<Buff> hearse = new ArrayList<Buff>();
		for (Buff b : buffs) {
			// Does the buff's on-turn effect
			b.doTurnEffect();
			
			// Checks if buff is still active
			if (b.persist()) {
				LogScreen.log("Player's " + b.getName() + " wore off.");
				hearse.add(b);
			}
		}
		
		// Remove expired buffs
		for (Buff b : hearse) {
			removeBuff(b);
		}
	}
	
	/**
	 * Heals the player, with the option to over-heal
	 * @param heal Health points to heal the player
	 * @param isOverHeal True if healing can go above maximum HP | False if not
	 */
	public void healPlayer(int heal, boolean isOverHeal) {
		this.currentHP = currentHP + heal;
		if ((currentHP > maxHP) && (!isOverHeal)) {
			this.currentHP = maxHP;
		}
	}
	
	/**
	 * Heals this character (cannot go above maximum HP)
	 * @param heal Health points to heal the player
	 */
	public void healPlayer(int heal) {
		healPlayer(heal, false);
	}
	
	/**
	 * Heals the player to full health
	 */
	public void fullyHeal() {
		healPlayer(maxHP);
	}
	
	/**
	 * Populates the list of movable tiles
	 */
	private void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ACID)));
	}
	
	/**
	 * Checks if the player can move to a given MovableType
	 * @param mt MovableType to check
	 * @return True if the player can move to a given MovableType | False if not
	 */
	public boolean canMove(Short mt) {
		return MovableType.canMove(moveTypes, mt);
	}
	
	/**
	 * Adds an allowed MovableType to the player
	 * @param mt MovableType to add
	 */
	public void addMoveType(Short mt) {
		this.moveTypes = (short) (moveTypes | mt);
	}
	
	/**
	 * Removes an allowed MovableType from the player
	 * @param mt MovableType to remove
	 */
	public void removeMoveType(Short mt) {
		mt = (short) (~mt & Short.MAX_VALUE);
		this.moveTypes = (short) (moveTypes & mt);
	}
	
	/**
	 * Swaps equipped and sheathed weapon
	 */
	public void swapEquippedWeapon() {
		// Store reference to equipped weapon
		Weapon swapWep = equippedWeapon;
		
		// Equip sheathed weapon
		this.equippedWeapon = sheathedWeapon;
		
		// Sheath previously equipped weapon
		this.sheathedWeapon = swapWep;
		
		// Update status screen to show new weapon
		StatusScreen.updateWeapons();
	}
	
	/**
	 * Charges both weapons the player has equipped
	 */
	public void chargeWeapons() {
		// Charge both equipped weapons
		equippedWeapon.chargeWeapon();
		sheathedWeapon.chargeWeapon();
		
		// Do your offhand weapon's action
		sheathedWeapon.doOffhand();
		
		// Log a message
       	LogScreen.log("Charged weapons...");
	}
	
	/**
	 * Discharges both weapons the player has equipped
	 */
	public void dischargeWeapons() {
		equippedWeapon.dischargeWeapon();
		sheathedWeapon.dischargeWeapon();
	}
	
	/**
	 * Checks if player is Alive
	 * @return True if alive | False if dead
	 */
	public boolean isAlive() {
		return (currentHP > 0);
	}
	
	/**
	 * Updates coordinates the player was on last turn
	 */
	public void updateLastCoords() {
		this.lastX = xPos;
		this.lastY = yPos;
	}
	
	// *******************
	// Getters and setters
	
	// Gets player image based off health levels
	public String getPlayerImage() {
		int hp = currentHP;
		int max = maxHP;
		if (hp > (max*3/4)) {
			return playerImage_FULL;
		} else if (hp > (max/2)) {
			return playerImage_BRUISED;
		} else if (hp > (max/4)) {
			return playerImage_INJURED;
		} else if (hp > 0) {
			return playerImage_FATAL;
		} else if (hp < -100) {
			// Crit death
			return playerImage_DEAD_CRIT;
		} else {
			// Normal death
			return playerImage_DEAD;
		}
	}
	
	public String getBlankImage() {
		return GPath.NULL;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public void setXPos(int newX) {
		this.xPos = newX;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public void setYPos(int newY) {
		this.yPos = newY;
	}
	
	public int getLastX() {
		return this.lastX;
	}
	
	public int getLastY() {
		return this.lastY;
	}
	
	public int getLevelX() {
		return this.levelX;
	}
	
	public int getLevelY() {
		return this.levelY;
	}
	
	public void shiftLevelPos(int dx, int dy) {
		this.levelX += dx;
		this.levelY += dy;
	}
	
	public void setLevelPos(int newX, int newY) {
		this.levelX = newX;
		this.levelY = newY;
	}
	
	public int getAreaX() {
		return this.areaX;
	}
	
	public int getAreaY() {
		return this.areaY;
	}
	
	public MapArea fetchArea() {
		return WorldMap.getArea(this.areaX, this.areaY);
	}
	
	public void shiftAreaPos(int dx, int dy) {
		this.areaX += dx;
		this.areaY += dy;
	}
	
	public void setAreaPos(int newX, int newY) {
		this.areaX = newX;
		this.areaY = newY;
	}
	
	public Dimension getFullPos() {
		return new Dimension(this.xPos, this.yPos);
	}
	
	public void setFullPos(int newX, int newY) {
		setXPos(newX);
		setYPos(newY);
	}
	
	public int getMaxHP() {
		return this.maxHP;
	}
	
	public void increaseMaxHP(int dx) {
		this.maxHP += dx;
		this.currentHP += dx;
	}
	
	public void setMaxHP(int newMax) {
		this.maxHP = newMax;
	}
	
	public int getCurrentHP() {
		return this.currentHP;
	}
	
	public void setCurrentHP(int newHP) {
		this.currentHP = newHP;
	}
	
	public Short getMoveTypes() {
		return this.moveTypes;
	}
	
	public Weapon getWeapon() {
		return this.equippedWeapon;
	}
	
	public Weapon getSheathedWeapon() {
		return this.sheathedWeapon;
	}
	
	public void setWeapon(Weapon newWeapon) {
		this.equippedWeapon = newWeapon;
	}
	
	public int getArmor() {
		return this.armor;
	}
	
	public void addArmor(int armorValue) {
		this.armor += armorValue;
	}
	
	public int getVision() {
		return this.vision;
	}
	
	public void setVision(int vision) {
		this.vision = vision;
	}
	
	public void addVision(int dv) {
		this.vision += dv;
	}
	
	public ArrayList<Buff> getBuffs() {
		return this.buffs;
	}
	
	/**
	 * Adds a buff/debuff to the player
	 * @param buff Buff/Debuff to add
	 */
	public void addBuff(Buff buff) {
		if (hasBuff(buff.getName())) {
			// Extend the current buff
			extendBuff(buff);
		} else {
			// Add new buff
			buff.setPlayer(this);
			buff.activate();
			buffs.add(buff);
			if (buff.isDebuff()) {
				LogScreen.log("Player inflicted with " + buff.getName() + "!", GColors.DAMAGE);
			} else {
				LogScreen.log("Player gained " + buff.getName() + "!", GColors.ITEM);
			}
		}
	}
	
	/**
	 * Removes a buff/debuff from the player
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
	 * Removes a buff/debuff from the player
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
	 * Clears all buffs/debuffs from the player
	 */
	public void clearBuffs() {
		for (Buff b : buffs) {
			b.deactivate();
		}
		
		buffs.clear();
	}
	
	/**
	 * Extends a buff/debuff on the player
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
	 * @return True if player has buff/debuff | False if not
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
