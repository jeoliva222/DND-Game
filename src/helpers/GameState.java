package helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import characters.Player;
import items.GItem;
import levels.MapArea;

// Contains all the important pieces of data to remember for the game state
// to be successfully saved and loaded from memory in between runs of the game.
public class GameState implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = -4716175631471462427L;
	
	// Player data
	public Player player;
	
	// State of the world
	public MapArea[][] gameWorld;
	
	// Inventory items
	public GItem[] inventory;

	// Constructor
	public GameState(GItem[] inventory, Player player, MapArea[][] gameWorld) {
		this.inventory = inventory;
		this.player = player;
		this.gameWorld = gameWorld;
	}
	
	// Serializes the important save data into a save file
	public void saveGame() {
		try
		{
		   FileOutputStream myFileOutputStream = new FileOutputStream("test_save.ser");
		   ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
		   myObjectOutputStream.writeObject(this);
		   myObjectOutputStream.close();
		}
		catch (Exception e)
		{
			System.out.println("Error saving data! See stack trace:");
		    e.printStackTrace();
		}
	}
	
	// Loads the important save data from the save file
	public static GameState loadGame() {
		GameState newState = null;
		
	    try {
	        FileInputStream fis = new FileInputStream("test_save.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        newState = (GameState) in.readObject();
	        in.close();
	      }
	      catch (Exception e) {
	          System.out.println("Error loading data! See stack trace:");
	          e.printStackTrace();
	      }
	    
	    return newState;
	}
	
}

