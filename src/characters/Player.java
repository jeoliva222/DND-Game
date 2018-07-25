package characters;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

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

public class Player implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = -1569715616453904946L;
	
	// Player image paths
	private String playerImage_FULL = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_full.png");
	private String playerImage_BRUISED = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_bruised.png");
	private String playerImage_INJURED = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_injured.png");
	private String playerImage_FATAL = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_fatal2.png");
	private String playerImage_DEAD = GPath.createImagePath(GPath.ALLY, GPath.PLAYER, "frog_dead2.png");
	
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
	
	// List of tile categories you can move on
	private ArrayList<MovableType> moveTypes = new ArrayList<MovableType>();
	
	// Equipped Weapon
	private Weapon equippedWeapon = Armory.ironSword;
	
	// Sheathed Weapon
	private Weapon sheathedWeapon = Armory.bareFists;
	
	// Constructor
	public Player() {
		// Set player tile position
		// Default is 6, 2
		this.xPos = 6;
		this.yPos = 2;
		this.lastX = this.xPos;
		this.lastY = this.yPos;
		
		/// ***TEMP*** Set the area coordinates
		// Default is 0, 0
		this.areaX = 0;
		this.areaY = 0;
		
		/// ***TEMP*** Set the level coordinates
		// Default is 3, 2
		this.levelX = 5;
		this.levelY = 0;
		
		// Set health values
		this.maxHP = 15;
		this.currentHP = this.maxHP;
		
		// Dictate what player can move on
		this.populateMoveTypes();
	}
	
	///*** TEMPORARY IMPLEMENTATIONS USED
	// TODO
	public boolean movePlayer(int dx, int dy) {
		
		// Check if player is alive first
		if(!this.isAlive()) {
			// If player is dead, don't do anything
			return false;
		}
		
		///*** (TEMP) Marks the start of every turn in the log
		System.out.println("------------");
		
		// Next check to see if your weapon will hit anything
		if(this.equippedWeapon.tryAttack(dx, dy)) {
			// If we hit something, we don't move
			return false;
		}
		
		// Discharge weapon if player isn't attacking this turn
		this.equippedWeapon.dischargeWeapon();
		
		// Then check for barriers, out-of-bounds, and immovable spaces
		TileType tt = null;
		try {
			// Try to get the TileType of the GameTile
			tt = GameScreen.getTile(this.xPos + dx, this.yPos + dy).getTileType();
		} catch (IndexOutOfBoundsException e) {
			// If we're going out of bounds, that means we're switching screens
			
			// Try to swap levels
			if(GameWindow.getScreen().swapLevel(dx, dy)) {
				// Change the player's position
				
				// Get the new relative coordinates of the player
				int newX = this.xPos + dx;
				int newY = this.yPos + dy;
				
				// Load the border limits of the screen
				int xMax = GameInitializer.xDimen;
				int yMax = GameInitializer.yDimen;
				
				// Switch the player x-wise to other end of the screen
				// if past the limits
				if(newX >= xMax) {
					newX = 0;
				} else if(newX < 0) {
					newX = xMax - 1;
				}
				
				// Switch the player y-wise to other end of the screen
				// if past the limits
				if(newY >= yMax) {
					newY = 0;
				} else if(newY < 0) {
					newY = yMax - 1;
				}
				
				// Try to place player at the correct position on the new screen
				if(this.leapPlayer(newX, newY, true)) {
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
		if(tt == null || !this.moveTypes.contains(tt.getMovableType())) {
			return false;
		}
		
		// Mark old position
		this.lastX = this.xPos;
		this.lastY = this.yPos;
		
		// If all checks pass, update the player position
		this.xPos += dx;
		this.yPos += dy;
		
		// Grab any items at the new position
		boolean breakFlag = false;
		for(GPickup pu: EntityManager.getPickupManager().getPickups()) {
			if(pu.getXPos() == this.xPos && pu.getYPos() == this.yPos) {
				// Add item to inventory if we have inventory space
				if(InventoryScreen.addItem(pu.item)) {
				
					// Set focus on new item
					InfoScreen.setItemFocus(pu.item);
					
					// Log what player grabbed
					LogScreen.log("Player looted "+pu.item.getName()+".", GColors.ITEM);
					
					// Play a sound
					SoundPlayer.playWAV(GPath.createSoundPath("Item_GET.wav"));
					
					// Remove item from manager
					EntityManager.getPickupManager().removePickup(pu);
				}
				
				// Set break flag to break from 'for' loop
				breakFlag = true;
			}
			// Break if we collected an item already
			if(breakFlag) break;
		}
		
		// Trigger the TileType's onStep method
		tt.onStep();
		
		// Return true because we moved successfully
		return true;
	}
	
	// Leaps player to an entirely new position
	// If considered a 'teleport', this will potentially telefrag enemies.
	public boolean leapPlayer(int newX, int newY, boolean isTeleport) {
		// Check if player is alive first
		if(!this.isAlive()) {
			// If player is dead, don't do anything
			return false;
		}
		
		// Check for barriers, out-of-bounds, and immovable spaces
		TileType tt;
		try {
			// Try to get the TileType of the GameTile
			tt = GameScreen.getTile(newX, newY).getTileType();
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		
		// If immovable, return false without changing position
		if(!this.moveTypes.contains(tt.getMovableType())) {
			return false;
		}
		
		// Finally check for collision
		for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
			if((newX) == npc.xPos && (newY) == npc.yPos) {
				if(isTeleport) {
					npc.damageCharacter(1000);
					LogScreen.log("Player telefragged " + npc.getName() + ".", GColors.ATTACK);
				} else {
					return false;
				}
			}
		}
		
		// Set old position
		this.lastX = this.xPos;
		this.lastY = this.yPos;
		
		// If all checks pass, update the player position
		this.xPos = newX;
		this.yPos = newY;
		
		// Grab any items at the new position
		boolean breakFlag = false;
		for(GPickup pu: EntityManager.getPickupManager().getPickups()) {
			if(pu.getXPos() == this.xPos && pu.getYPos() == this.yPos) {
				// Add item to inventory if we have inventory space
				if(InventoryScreen.addItem(pu.item)) {
				
					// Set focus on new item
					InfoScreen.setItemFocus(pu.item);
					
					// Log what player grabbed
					LogScreen.log("Player looted "+pu.item.getName()+".", GColors.ITEM);
					
					// Play a sound
					SoundPlayer.playWAV(GPath.createSoundPath("Item_GET.wav"));
					
					// Remove item from manager
					EntityManager.getPickupManager().removePickup(pu);
				}
				
				// Set break flag to break from 'for' loop
				breakFlag = true;
			}
			// Break if we collected an item already
			if(breakFlag) break;
		}
		
		// Trigger the TileType's onStep method
		tt.onStep();
		
		// Return true because we successfully moved
		return true;
	}
	
	// Overload with default value of 'false' to isTeleport parameter
	public boolean leapPlayer(int newX, int newY) {
		return this.leapPlayer(newX, newY, false);
	}
	
	// Returns True if alive from damage, False if dead from damage
	public boolean damagePlayer(int damage) {
		this.currentHP = this.currentHP - damage;
		if(this.isAlive()) {
			SoundPlayer.playWAV(GPath.createSoundPath("Player_HURT.wav"), -15);
			return true;
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("Player_DEATH.wav"), -5);
			LogScreen.log("Player has died! Game over.", GColors.DAMAGE);
			return false;
		}
	}
	
	// Heal player: Up to, but not over, max if not over-heal /
	// Up and over max if over-heal
	public void healPlayer(int heal, boolean isOverHeal) {
		this.currentHP = this.currentHP + heal;
		if((!isOverHeal) && (this.currentHP > this.maxHP)) {
			this.currentHP = this.maxHP;
		}
	}
	
	// Overload that assumes heal isn't an over-heal
	public void healPlayer(int heal) {
		this.currentHP = this.currentHP + heal;
		if(this.currentHP > this.maxHP) {
			this.currentHP = this.maxHP;
		}
	}
	
	// Heals the player to full health
	public void fullyHeal() {
		this.healPlayer(this.maxHP);
	}
	
	// Populates the list of movable tiles
	private void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.WATER);
		this.moveTypes.add(MovableType.ACID);
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
	
	// Swaps equipped and sheathed weapon
	public void swapEquippedWeapon() {
		// Discharge current weapon
		this.equippedWeapon.dischargeWeapon();
		
		// Store reference to equipped weapon
		Weapon swapWep = this.equippedWeapon;
		
		// Equip sheathed weapon
		this.equippedWeapon = this.sheathedWeapon;
		
		// Sheath previously equipped weapon
		this.sheathedWeapon = swapWep;
		
		// Update status screen to show new weapon
		StatusScreen.updateWeapons();
	}
	
	// Checks if player is Alive
	public boolean isAlive() {
		return (this.currentHP > 0);
	}
	
	// *******************
	// Getters and setters
	
	// Gets player image based off health levels
	public String getPlayerImage() {
		int hp = this.currentHP;
		int max = this.maxHP;
		if(hp > (max*3/4)) {
			return this.playerImage_FULL;
		} else if(hp > (max/2)) {
			return this.playerImage_BRUISED;
		} else if(hp > (max/4)) {
			return this.playerImage_INJURED;
		} else if(hp > 0) {
			return this.playerImage_FATAL;
		} else {
			return this.playerImage_DEAD;
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
		this.setXPos(newX);
		this.setYPos(newY);
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
	
	public ArrayList<MovableType> getMoveTypes() {
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
	
}
