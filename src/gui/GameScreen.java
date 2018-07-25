package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import characters.Corpse;
import characters.GCharacter;
import helpers.GameState;
import helpers.SoundPlayer;
import items.GPickup;
import levels.AreaFetcher;
import levels.MapLevel;
import levels.WorldMap;
import managers.EntityManager;
import managers.ImageBank;
import tiles.Ground;
import tiles.TileType;

// Panel that shows the actual game action
public class GameScreen extends JPanel {

	// Prevents warnings in Eclipse
	private static final long serialVersionUID = 1L;
	
	// 2D Array of GameTiles
	private static GameTile[][] tiles;
	
	// Size of all tiles
	private static int tileSize = 80;
	
	// Height and width of GameScreen
	private static int gWidth, gHeight;
	
	// Constructor
	protected GameScreen() {
		super();
		
		double scaleFactor = GameInitializer.scaleFactor;
		int xDimen = GameInitializer.xDimen;
		int yDimen = GameInitializer.yDimen;
		
		GameScreen.tileSize = (int) (scaleFactor * GameInitializer.tileArtSize);
		
		GameScreen.gWidth = xDimen * GameScreen.tileSize;
		GameScreen.gHeight = yDimen * GameScreen.tileSize;
		System.out.println("GS Width = " + Integer.toString(GameScreen.gWidth));
		System.out.println("GS Height = " + Integer.toString(GameScreen.gHeight));
		
		// Set the size of the screen
		Dimension size = new Dimension(GameScreen.gWidth, GameScreen.gHeight);
		this.setPreferredSize(size);
		
		// Initialize Grid of GameTiles
		GameScreen.tiles = new GameTile[yDimen][xDimen];
		
		/// Set the active area
		// TODO
		int areaX = EntityManager.getPlayer().getAreaX();
		int areaY = EntityManager.getPlayer().getAreaY();
		int levelX = EntityManager.getPlayer().getLevelX();
		int levelY = EntityManager.getPlayer().getLevelY();
		String areaKey = WorldMap.getAreaKey(areaX, areaY);
		EntityManager.setActiveArea(AreaFetcher.fetchDefaultArea(areaKey));
		
		// Populate the grid with tiles, with placeholder as ground-type
		for(int y = 0; y < yDimen; y++) {
			for(int x = 0; x < xDimen; x++) {
				GameTile tile = new GameTile(x, y, new Ground(), scaleFactor);
				GameScreen.tiles[y][x] = tile;
				this.add(tile);
			}
		}
		
		// Load our level
		this.loadLevel(EntityManager.getActiveArea().getLevel(levelX, levelY));

		// Set various attributes of GameScreen
		this.setVisible(true);
		this.setLayout(new GridLayout(yDimen, xDimen));
	}
	
	// Swaps levels and effectively switches the screen of play
	// Returns whether we were successfully able to find a new
	// or not.
	public boolean swapLevel(int dx, int dy) {
		// Load player area/level coordinates
		int levelX = EntityManager.getPlayer().getLevelX();
		int levelY = EntityManager.getPlayer().getLevelY();
		
		try {			
			// First try to swap to a new level within the same area
			MapLevel nextLevel = EntityManager.getActiveArea().getLevel(levelX + dx, levelY + dy);
			
			// Check if the next level is null
			if(nextLevel == null) {
				// If null, we can't move to it, so return false
				return false;
			} else {
				// If we've got a level to move to, commence changing level
				
				// Save our current level
				this.saveLevel(true);
				
				// Defocus InfoScreen
				InfoScreen.defocusAll();
				
				// Clear the EntityManager
				EntityManager.removeEverything();
				
				// Clear the GameTile images
				for(int y = 0; y < GameInitializer.yDimen; y++) {
					for(int x = 0; x < GameInitializer.xDimen; x++) {
						GameScreen.tiles[y][x].clearAll();
					}
				}
				
				// Shift the player's level position
				EntityManager.getPlayer().shiftLevelPos(dx, dy);
				
				// Clears the stored bank of images
				ImageBank.clearBank();
				
				// Load the next level
				this.loadLevel(nextLevel);
				
				// Indicate to the main window that the screen was changed
				GameWindow.changedScreen = true;
				
				// Return true to indicate the player can update their position
				return true;
			}
		} catch(IndexOutOfBoundsException e) {
			// If we went out of bounds in the current area,
			// then try switching to a new area within the world
			return this.swapArea(dx, dy);
		}
	}
	
	// Tries to swap to a level in a new area
	public boolean swapArea(int dx, int dy) {
		// Load player area/level coordinates
		int areaX = EntityManager.getPlayer().getAreaX();
		int areaY = EntityManager.getPlayer().getAreaY();
		int levelX = EntityManager.getPlayer().getLevelX();
		int levelY = EntityManager.getPlayer().getLevelY();
		
		try {	
			// Fetch the next area key
			String newAreaKey = WorldMap.getAreaKey(areaX + dx, areaY + dy);
			
			// Check if the next area is null
			if(newAreaKey == "") {
				// If null, we can't move to it, so return false
				return false;
			} else {
				// Get the new relative coordinates of the player
				int newLevelX = levelX + dx;
				int newLevelY = levelY + dy;
				
				// Load the border limits of the old area
				int xOldMax = EntityManager.getActiveArea().getLength();
				int yOldMax = EntityManager.getActiveArea().getHeight();
				
				// Save our current level
				this.saveLevel(true);
				
				// Save the current area
				GameState.saveCurrentArea();
				
				// Load in the new area
				EntityManager.setActiveArea(WorldMap.getArea(areaX + dx, areaY + dy));
				
				// Load the border limits of the new area
				int xNewMax = EntityManager.getActiveArea().getLength();
				int yNewMax = EntityManager.getActiveArea().getHeight();
				
				// Switch the player x-wise to other end of the screen
				// if past the limits
				if(newLevelX >= xOldMax) {
					newLevelX = 0;
				} else if(newLevelX < 0) {
					newLevelX = xNewMax - 1;
				}
				
				// Switch the player y-wise to other end of the screen
				// if past the limits
				if(newLevelY >= yOldMax) {
					newLevelY = 0;
				} else if(newLevelY < 0) {
					newLevelY = yNewMax - 1;
				}
				
				// Fetch the level from the new area we're heading into
				MapLevel nextLevel = EntityManager.getActiveArea().getLevel(newLevelX, newLevelY);
				
				if(nextLevel == null) {
					// Level in next area is null, so return false
					return false;
				} else {
					// If we've got a level to move to, commence changing level
					
					// Defocus InfoScreen
					InfoScreen.defocusAll();
					
					// Clear the EntityManager
					EntityManager.removeEverything();
					
					// Clear the GameTile images
					for(int y = 0; y < GameInitializer.yDimen; y++) {
						for(int x = 0; x < GameInitializer.xDimen; x++) {
							GameScreen.tiles[y][x].clearAll();
						}
					}
					
					// Shift the player's area position
					EntityManager.getPlayer().shiftAreaPos(dx, dy);
					
					// Shift the player's level position
					EntityManager.getPlayer().setLevelPos(newLevelX, newLevelY);
					
					// Clears the stored bank of images
					ImageBank.clearBank();
					
					// Load the next level
					this.loadLevel(nextLevel);
					
					// Change music
					SoundPlayer.changeMidi(EntityManager.getActiveArea().getMusic(), 30);
					
					// Indicate to the main window that the screen was changed
					GameWindow.changedScreen = true;
					
					LogScreen.log("Now Entering: "+ EntityManager.getActiveArea().getName());
					
					// Return true to indicate the player can update their position
					return true;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public void loadLevel(MapLevel level) {
		// Set TileType for each GameTile
		for(int y = 0; y < GameInitializer.yDimen; y++) {
			for(int x = 0; x < GameInitializer.xDimen; x++) {
				// Set matching TileType for the GameTile
				GameScreen.tiles[y][x].setTileType(level.getTile(x, y));
			}
		}
		
		// Set Enemies
		for(GCharacter npc: level.getNPCs()) {
			EntityManager.getNPCManager().addCharacter(npc);
		}
		
		// Set Corpses
		for(Corpse cps: level.getCorpses()) {
			EntityManager.getCorpseManager().addCorpse(cps);
		}
		
		// Set Pickups
		for(GPickup pu: level.getPickups()) {
			EntityManager.getPickupManager().addPickup(pu);
		}
	}
	
	// Saves the entities of the current level we are on
	public void saveLevel(boolean resetNPCs) {
		// Get player level coordinates
		int levelX = EntityManager.getPlayer().getLevelX();
		int levelY = EntityManager.getPlayer().getLevelY();
		
		// Fetch the level the player is on
		MapLevel currentLevel = EntityManager.getActiveArea().getLevel(levelX, levelY);
		
		// Save the tiles
		TileType[][] tts = new TileType[GameInitializer.yDimen][GameInitializer.xDimen];
		for(int y = 0; y < GameInitializer.yDimen; y++) {
			for(int x = 0; x < GameInitializer.xDimen; x++) {
				tts[y][x] = GameScreen.tiles[y][x].getTileType();
			}
		}
		currentLevel.setTiles(tts);
		
		// Save the NPCs
		ArrayList<GCharacter> newNPCList = new ArrayList<GCharacter>();
		for(GCharacter npc: EntityManager.getNPCManager().getCharacters()) {
			if(resetNPCs)
				npc.returnToOrigin();
			newNPCList.add(npc);
		}
		currentLevel.setNPCs(newNPCList);
		
		// Save the corpses
		ArrayList<Corpse> newCorpseList = new ArrayList<Corpse>();
		for(Corpse corpse: EntityManager.getCorpseManager().getCorpses()) {
			newCorpseList.add(corpse);
		}
		currentLevel.setCorpses(newCorpseList);
		
		// Save the pickups
		ArrayList<GPickup> newPickupList = new ArrayList<GPickup>();
		for(GPickup pickup: EntityManager.getPickupManager().getPickups()) {
			newPickupList.add(pickup);
		}
		currentLevel.setPickups(newPickupList);
	}
	
	public void refreshTiles() {
		for (int y = 0; y < GameInitializer.yDimen; y++) {
			for (int x = 0; x < GameInitializer.xDimen; x++) {
				tiles[y][x].repaint();
			}
		}
	}
	
	// ***********
	// Getters and setters
	
	public static GameTile[][] getAllTiles() {
		return GameScreen.tiles;
	}
	
	public static GameTile getTile(int x, int y) {
		return GameScreen.tiles[y][x];
	}
	
	public static int getGWidth() {
		return GameScreen.gWidth;
	}
	
	public static int getGHeight() {
		return GameScreen.gHeight;
	}

}
