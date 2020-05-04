package managers;

import java.util.ArrayList;

import gui.GameScreen;
import items.GPickup;

public class PickupManager {

	// ArrayList containing instances of all existing pickups
	private ArrayList<GPickup> pickups = new ArrayList<GPickup>();
	
	// Constructor that starts with pickups in it
	public PickupManager(ArrayList<GPickup> pickups) {
		this.pickups = pickups;
	}
	
	// Blank constructor
	public PickupManager() {
	}
	
	// Adds a pickup to the list
	public void addPickup(GPickup pu) {
		this.pickups.add(pu);
		try {
			// Paints pickup onto its tile
			GameScreen.getTile(pu.getXPos(), pu.getYPos()).setPickupImage(pu.getImage());
		} catch (ArrayIndexOutOfBoundsException e) {
			// Do nothing if out of bounds
		}
	}
	
	// Checks if a pickup exists within the manager
	// Returns true if found and removed
	// Returns false if not found
	public boolean removePickup(GPickup pu) {
		if(this.pickups.contains(pu)) {
			this.pickups.remove(pu);
			
			// Search for remaining pickups left on this tile
			for(GPickup item: this.pickups) {
				if(item.getXPos() == pu.getXPos() && item.getYPos() == pu.getYPos()) {
					GameScreen.getTile(pu.getXPos(), pu.getYPos()).setPickupImage(item.getImage());
					return true;
				}
			}
			
			// If no pickups remaining on tile, clear the tile of pickup images
			GameScreen.getTile(pu.getXPos(), pu.getYPos()).clearPickup();
			return true;
		} else {
			return false;
		}
	}
	
	// Removes all pickups from list
	public void removeAll() {
		this.pickups.clear();
	}
	
	// Returns all the pickups in the manager
	public ArrayList<GPickup> getPickups() {
		return this.pickups;
	}
	
	// Sets the list of corpses to an input list value
	public void setPickups(ArrayList<GPickup> pickups) {
		this.pickups = pickups;
	}
	
}
