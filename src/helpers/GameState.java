package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import characters.allies.Player;
import gui.InventoryScreen;
import items.GItem;
import levels.MapArea;
import levels.WorldMap;
import levels.WorldMap.AreaEnum;
import managers.EntityManager;

/**
 * Contains all the important pieces of data to remember for the game state
 * to be successfully saved and loaded from memory in between runs of the game.
 * @author jeoliva
 */
public class GameState {
	
	// Important definitions
	public static final String TEMP_SAVE = (GPath.SAVE + GPath.TEMP);
	public static final String PLAYER = "player";
	public static final String INVENTORY = "inventory";
	public static final String SUFFIX = ".ser";
	public static final String README_SUFFIX = ".md";
	
	/**
	 * Serializes the game data into a save files.
	 * @param inventory Inventory state to save
	 * @param player Player state to save
	 */
	public static void saveGame(GItem[] inventory, Player player) {
		try {
			// Write the inventory
			FileOutputStream myFileOutputStream = new FileOutputStream(GPath.SAVE + INVENTORY + SUFFIX);
			ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
			myObjectOutputStream.writeObject(inventory);
			myObjectOutputStream.close();
		   
			// Write the player
			myFileOutputStream = new FileOutputStream(GPath.SAVE + PLAYER + SUFFIX);
			myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
			myObjectOutputStream.writeObject(player);
			myObjectOutputStream.close();
			
			// Move all area save files from the temp folder to the main folder
			File tempFolder = new File(GPath.SAVE + "temp");
			File[] areaFileList = tempFolder.listFiles();
			for (File areaFile : areaFileList) {
				// If the file is the ReadMe file, ignore it
				if (areaFile.getName().endsWith(README_SUFFIX)) {
					continue;
				}
				
				// Delete the older file in main if it exists to make room for the new one
				File oldFile = new File(GPath.SAVE + areaFile.getName());
				if (oldFile.exists()) {
					oldFile.delete();
				}
				
				// Move the new file over from temp
				areaFile.renameTo(new File(GPath.SAVE + areaFile.getName()));
			}
		} catch (Exception ex) {
			System.out.println("Error saving game data! See stack trace:");
		    ex.printStackTrace();
		}
	}
	
	/**
	 * Loads the game data from the save files.
	 * @return Boolean indicating success (True) or failure (False) in loading save files
	 */
	public static boolean loadGame() {
	    try {
			// Delete all files from the temp folder
	    	GameState.deleteTempSaves();
	    	
	    	// Get inventory
	        FileInputStream fis = new FileInputStream(GPath.SAVE + INVENTORY + SUFFIX);
	        ObjectInputStream in = new ObjectInputStream(fis);
	        GItem[] loadedInv = (GItem[]) in.readObject();
	        in.close();
	        
	        // Get player
	        fis = new FileInputStream(GPath.SAVE + PLAYER + SUFFIX);
	        in = new ObjectInputStream(fis);
	        Player loadedPlayer = (Player) in.readObject();
	        in.close();
	        
	        //------------
	        
	    	// Set inventory
	    	InventoryScreen.setItemArray(loadedInv);
	    	
	    	// Reset any weird states that may have been saved
	    	loadedPlayer.dischargeWeapons();
	    	loadedPlayer.resetWeapons();
	    	
	    	// Set player
	    	EntityManager.getInstance().setPlayer(loadedPlayer);
	    } catch (Exception ex) {
	    	System.out.println("Error loading game data! See stack trace:");
	    	ex.printStackTrace();
	          
	    	// Return false if we failed to load
	    	return false;
	    }
	    
	    // Return true if successful
	    return true;
	}
	
	/**
	 * Save the active area to a save file based off a provided name.
	 * @param areaEnum Area Name enumeration to save the area under
	 */
	public static void saveArea(AreaEnum areaEnum) {
		// Write the area to temp save folder
		try {
			String areaName = areaEnum.getThemePath();
			FileOutputStream myFileOutputStream = new FileOutputStream(TEMP_SAVE + areaName + SUFFIX);
			ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
			myObjectOutputStream.writeObject(EntityManager.getInstance().getActiveArea());
			myObjectOutputStream.close();
		} catch (IOException ex) {
			System.out.println("Saving the area '" + areaEnum + "' failed!");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Saves the area the player is currently in.
	 */
	public static void saveCurrentArea() {
		// Fetch reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// Get the area key of the current are we're in
		AreaEnum areaKey = WorldMap.getAreaKey(player.getAreaX(), player.getAreaY());
		
		// Save the area
		GameState.saveArea(areaKey);
	}
	
	/**
	 * Loads an area from the save folder based off the provided name.
	 * @param areaName Area name to load
	 * @param fromTemp True = Load from temp save files | False = Load from standard save files
	 * @return The loaded MapArea
	 */
	public static MapArea loadArea(String areaName, boolean fromTemp) {
		// Decide what path we are loading the area from
		String filePath;
        if (fromTemp) {
        	filePath = (TEMP_SAVE + areaName + SUFFIX);
        } else {
        	filePath = (GPath.SAVE + areaName + SUFFIX);
        }
        
        // Load the area from the file
        MapArea loadedArea = null;
        try {
        	FileInputStream fis = new FileInputStream(filePath);
        	ObjectInputStream in = new ObjectInputStream(fis);
        	loadedArea = (MapArea) in.readObject();
        	in.close();
        } catch (Exception ex) {
			System.out.println("Loading the area '" + areaName + "' failed!");
			ex.printStackTrace();
        }
        
        // Return the loaded area
        return loadedArea;
	}
	
	/**
	 * Deletes all the temp save files.
	 */
	public static void deleteTempSaves() {
		File tempFolder = new File(GPath.SAVE + "temp");
		File[] areaFileList = tempFolder.listFiles();
		if (areaFileList != null) {
			for (File areaFile : areaFileList) {
				if (areaFile.getName().endsWith(SUFFIX)) {
					areaFile.delete();
				}	
			}
		}
	}
	
}

