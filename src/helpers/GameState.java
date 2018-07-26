package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import characters.Player;
import gui.InventoryScreen;
import items.GItem;
import levels.MapArea;
import levels.WorldMap;
import managers.EntityManager;

// Contains all the important pieces of data to remember for the game state
// to be successfully saved and loaded from memory in between runs of the game.
public class GameState {
	
	// Serializes the important save data into a save file
	public static void saveGame(GItem[] inventory, Player player) {
		try
		{
			// Write the inventory
			FileOutputStream myFileOutputStream = new FileOutputStream(GPath.SAVE + "inventory.ser");
			ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
			myObjectOutputStream.writeObject(inventory);
			myObjectOutputStream.close();
		   
			// Write the player
			myFileOutputStream = new FileOutputStream(GPath.SAVE + "player.ser");
			myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
			myObjectOutputStream.writeObject(player);
			myObjectOutputStream.close();
			
			// Move all area save files from the temp folder to the main folder
			File tempFolder = new File(GPath.SAVE + "temp");
			File[] areaFileList = tempFolder.listFiles();
			for(File areaFile : areaFileList) {
				
				// Delete the older file in main if it exists to make room for the new one
				File oldFile = new File(GPath.SAVE + areaFile.getName());
				if(oldFile.exists()) {
					oldFile.delete();
				}
				
				// Move the new file over from temp
				areaFile.renameTo(new File(GPath.SAVE + areaFile.getName()));
			}
			
			
		}
		catch (Exception e)
		{
			System.out.println("Error saving data! See stack trace:");
		    e.printStackTrace();
		}
	}
	
	// Loads the important save data from the save file
	public static boolean loadGame() {
		
	    try {
			// Delete all files from the temp folder
	    	GameState.deleteTempSaves();
	    	
	    	// Get inventory
	        FileInputStream fis = new FileInputStream(GPath.SAVE + "inventory.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        GItem[] loadedInv = (GItem[]) in.readObject();
	        in.close();
	        
	        // Get player
	        fis = new FileInputStream(GPath.SAVE + "player.ser");
	        in = new ObjectInputStream(fis);
	        Player loadedPlayer = (Player) in.readObject();
	        in.close();
	        
	    	// Set inventory
	    	InventoryScreen.setItemArray(loadedInv);
	    	
	    	// Set Player
	    	EntityManager.setPlayer(loadedPlayer);
	      }
	      catch (Exception e) {
	          System.out.println("Error loading data! See stack trace:");
	          e.printStackTrace();
	          
	          // Return false if we failed to load
	          return false;
	      }
	    
	    // Return true if successful
	    return true;
	    
	}
	
	// Save an area to a save file based off of its name
	public static void saveArea(String areaName) {
		// Write the area to temp save folder
		try {
			FileOutputStream myFileOutputStream =
					new FileOutputStream(GPath.SAVE + "temp" + File.separator + areaName + ".ser");
			ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
			myObjectOutputStream.writeObject(EntityManager.getActiveArea());
			myObjectOutputStream.close();
		} catch (IOException e) {
			System.out.println("Saving the area " + areaName + " failed!");
			e.printStackTrace();
		}
		
	}
	
	// Save the area we are currently in
	public static void saveCurrentArea() {
		// Fetch reference to the player
		Player player = EntityManager.getPlayer();
		
		// Get the area key of the current are we're in
		String areaKey = WorldMap.getAreaKey(player.getAreaX(), player.getAreaY());
		
		// Save the area
		GameState.saveArea(areaKey);
		
	}
	
	// Load an area based off of its name from the save folder
	public static MapArea loadArea(String areaName, boolean fromTemp) {
		// Decide what path we are loading the area from
		String filePath;
        if(fromTemp) {
        	filePath = GPath.SAVE + "temp" + File.separator + areaName + ".ser";
        } else {
        	filePath = GPath.SAVE + areaName + ".ser";
        }
        
        // Load the area from the file
        MapArea loadedArea = null;
        try {
        	FileInputStream fis = new FileInputStream(filePath);
        	ObjectInputStream in = new ObjectInputStream(fis);
        	loadedArea = (MapArea) in.readObject();
        	in.close();
        } catch (Exception e) {
			System.out.println("Loading the area " + areaName + " failed!");
			e.printStackTrace();
        }
        
        // Return the loaded area
        return loadedArea;
	}
	
	// Delete all the temp save files
	public static void deleteTempSaves() {
		File tempFolder = new File(GPath.SAVE + "temp");
		File[] areaFileList = tempFolder.listFiles();
		for(File areaFile : areaFileList) {
			if(areaFile.getName().endsWith(".ser"))
				areaFile.delete();
		}
	}
	
}

