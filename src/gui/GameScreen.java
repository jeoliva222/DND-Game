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
import levels.WorldMap.AreaEnum;
import managers.EntityManager;
import managers.ImageBank;
import tiles.Ground;
import tiles.TileType;

/**
 * Panel class that shows the game action
 * @author jeoliva
 */
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
		
		// Calculate tile size
		tileSize = (int) (scaleFactor * GameInitializer.tileArtSize);
		
		// Initialize screen size
		gWidth = xDimen * tileSize;
		gHeight = yDimen * tileSize;
		System.out.println("GS Width = " + Integer.toString(gWidth));
		System.out.println("GS Height = " + Integer.toString(gHeight));
		
		// Set the size of the screen
		Dimension size = new Dimension(gWidth, gHeight);
		setPreferredSize(size);
		
		// Initialize Grid of GameTiles
		tiles = new GameTile[yDimen][xDimen];
		
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		/// Set the active area
		int areaX = em.getPlayer().getAreaX();
		int areaY = em.getPlayer().getAreaY();
		int levelX = em.getPlayer().getLevelX();
		int levelY = em.getPlayer().getLevelY();
		AreaEnum areaKey = WorldMap.getAreaKey(areaX, areaY);
		em.setActiveArea(AreaFetcher.fetchDefaultArea(areaKey));
		
		// Populate the grid with tiles, with placeholder as ground-type
		for (int y = 0; y < yDimen; y++) {
			for (int x = 0; x < xDimen; x++) {
				GameTile tile = new GameTile(x, y, new Ground(), scaleFactor);
				tiles[y][x] = tile;
				add(tile);
			}
		}
		
		// Load our level
		loadLevel(em.getActiveArea().getLevel(levelX, levelY));

		// Set various attributes of GameScreen
		setVisible(true);
		setLayout(new GridLayout(yDimen, xDimen));
	}
	
	// Swaps levels and effectively switches the screen of play
	// Returns whether we were successfully able to find a new
	// or not.
	public boolean swapLevel(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Load player area/level coordinates
		int levelX = em.getPlayer().getLevelX();
		int levelY = em.getPlayer().getLevelY();
		
		try {			
			// First try to swap to a new level within the same area
			MapLevel nextLevel = em.getActiveArea().getLevel(levelX + dx, levelY + dy);
			
			// Check if the next level is null
			if (nextLevel == null) {
				// If null, we can't move to it, so return false
				return false;
			} else {
				// If we've got a level to move to, commence changing level
				
				// Save our current level
				saveLevel(true);
				
				// Defocus InfoScreen
				InfoScreen.defocusAll();
				
				// Clear the EntityManager
				em.removeEverything();
				
				// Clear the GameTile images
				for (int y = 0; y < GameInitializer.yDimen; y++) {
					for (int x = 0; x < GameInitializer.xDimen; x++) {
						tiles[y][x].clearAll();
					}
				}
				
				// Shift the player's level position
				em.getPlayer().shiftLevelPos(dx, dy);
				
				// Clears the stored bank of images
				ImageBank.clearBank();
				
				// Load the next level
				loadLevel(nextLevel);
				
				// Indicate to the main window that the screen was changed
				GameWindow.getInstance().markChangingScreen();
				
				// Return true to indicate the player can update their position
				return true;
			}
		} catch (IndexOutOfBoundsException ex) {
			// If we went out of bounds in the current area,
			// then try switching to a new area within the world
			return swapArea(dx, dy);
		}
	}
	
	// Tries to swap to a level in a new area
	public boolean swapArea(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Load player area/level coordinates
		int areaX = em.getPlayer().getAreaX();
		int areaY = em.getPlayer().getAreaY();
		int levelX = em.getPlayer().getLevelX();
		int levelY = em.getPlayer().getLevelY();
		
		try {	
			// Fetch the next area key
			AreaEnum newAreaKey = WorldMap.getAreaKey(areaX + dx, areaY + dy);
			
			// Check if the next area is null
			if (newAreaKey == null) {
				// If null, we can't move to it, so return false
				return false;
			} else {
				// Get the new relative coordinates of the player
				int newLevelX = levelX + dx;
				int newLevelY = levelY + dy;
				
				// Load the border limits of the old area
				int xOldMax = em.getActiveArea().getLength();
				int yOldMax = em.getActiveArea().getHeight();
				
				// Save our current level
				saveLevel(true);
				
				// Save the current area
				GameState.saveCurrentArea();
				
				// Load in the new area
				em.setActiveArea(WorldMap.getArea(areaX + dx, areaY + dy));
				
				// Load the border limits of the new area
				int xNewMax = em.getActiveArea().getLength();
				int yNewMax = em.getActiveArea().getHeight();
				
				// Switch the player x-wise to other end of the screen
				// if past the limits
				if (newLevelX >= xOldMax) {
					newLevelX = 0;
				} else if (newLevelX < 0) {
					newLevelX = xNewMax - 1;
				}
				
				// Switch the player y-wise to other end of the screen
				// if past the limits
				if (newLevelY >= yOldMax) {
					newLevelY = 0;
				} else if (newLevelY < 0) {
					newLevelY = yNewMax - 1;
				}
				
				// Fetch the level from the new area we're heading into
				MapLevel nextLevel = em.getActiveArea().getLevel(newLevelX, newLevelY);
				
				if (nextLevel == null) {
					// Level in next area is null, so return false
					return false;
				} else {
					// If we've got a level to move to, commence changing level
					
					// Defocus InfoScreen
					InfoScreen.defocusAll();
					
					// Clear the EntityManager
					em.removeEverything();
					
					// Clear the GameTile images
					for (int y = 0; y < GameInitializer.yDimen; y++) {
						for (int x = 0; x < GameInitializer.xDimen; x++) {
							tiles[y][x].clearAll();
						}
					}
					
					// Shift the player's area position
					em.getPlayer().shiftAreaPos(dx, dy);
					
					// Shift the player's level position
					em.getPlayer().setLevelPos(newLevelX, newLevelY);
					
					// Clears the stored bank of images
					ImageBank.clearBank();
					
					// Load the next level
					loadLevel(nextLevel);
					
					// Try to garbage collect when switching areas
					System.gc();
					
					// Change music
					SoundPlayer.changeMidi(em.getActiveArea().getMusic(), em.getActiveArea().getMusicVolume());
					
					// Indicate to the main window that the screen was changed
					GameWindow.getInstance().markChangingScreen();
					
					// Log area-entering message
					LogScreen.log("Now Entering: "+ em.getActiveArea().getName());
					
					// Return true to indicate the player can update their position
					return true;
				}
			}
		} catch (IndexOutOfBoundsException ex) {
			return false;
		}
	}
	
	public void loadLevel(MapLevel level) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Set TileType for each GameTile
		for (int y = 0; y < GameInitializer.yDimen; y++) {
			for (int x = 0; x < GameInitializer.xDimen; x++) {
				// Set matching TileType for the GameTile
				tiles[y][x].setTileType(level.getTile(x, y));
			}
		}
		
		// Set Enemies
		for (GCharacter npc: level.getNPCs()) {
			em.getNPCManager().addCharacter(npc);
		}
		
		// Set Corpses
		for (Corpse cps: level.getCorpses()) {
			em.getCorpseManager().addCorpse(cps);
		}
		
		// Set Pickups
		for (GPickup pu: level.getPickups()) {
			em.getPickupManager().addPickup(pu);
		}
		
		// Mark the level as explored
		level.markExplored();
	}
	
	// Saves the entities of the current level we are on
	public void saveLevel(boolean resetNPCs) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Get player level coordinates
		int levelX = em.getPlayer().getLevelX();
		int levelY = em.getPlayer().getLevelY();
		
		// Fetch the level the player is on
		MapLevel currentLevel = em.getActiveArea().getLevel(levelX, levelY);
		
		// Save the tiles
		TileType[][] tts = new TileType[GameInitializer.yDimen][GameInitializer.xDimen];
		for (int y = 0; y < GameInitializer.yDimen; y++) {
			for (int x = 0; x < GameInitializer.xDimen; x++) {
				tts[y][x] = tiles[y][x].getTileType();
			}
		}
		currentLevel.setTiles(tts);
		
		// Save the NPCs
		ArrayList<GCharacter> newNPCList = new ArrayList<GCharacter>();
		for (GCharacter npc: em.getNPCManager().getCharacters()) {
			if (resetNPCs) {
				npc.returnToOrigin();
			}
			newNPCList.add(npc);
		}
		currentLevel.setNPCs(newNPCList);
		
		// Save the corpses
		ArrayList<Corpse> newCorpseList = new ArrayList<Corpse>();
		for (Corpse corpse: em.getCorpseManager().getCorpses()) {
			newCorpseList.add(corpse);
		}
		currentLevel.setCorpses(newCorpseList);
		
		// Save the pickups
		ArrayList<GPickup> newPickupList = new ArrayList<GPickup>();
		for (GPickup pickup: em.getPickupManager().getPickups()) {
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
		return tiles;
	}
	
	public static GameTile getTile(int x, int y) {
		return tiles[y][x];
	}
	
	public static int getGWidth() {
		return gWidth;
	}
	
	public static int getGHeight() {
		return gHeight;
	}

}
