package managers;

import java.util.ArrayList;

import characters.Corpse;
import characters.GCharacter;
import gui.GameScreen;
import helpers.GPath;

public class CorpseManager {
	
	// ArrayList containing instances of all existing corpses
	private ArrayList<Corpse> corpses = new ArrayList<Corpse>();
	
	// Constructor that starts with corpses in it
	public CorpseManager(ArrayList<Corpse> corpses) {
		this.corpses = corpses;
	}
	
	// Blank constructor
	public CorpseManager() {
	}
	
	// Adds a corpse to the list
	public void addCorpse(Corpse newCorpse) {
		
		// Search for a existing corpse at the same location
		Corpse overridenCorpse = null;
		for(Corpse corpse : this.corpses) {
			if(newCorpse.getXPos() == corpse.getXPos() && newCorpse.getYPos() == corpse.getYPos()) {
				overridenCorpse = corpse;
				break;
			}
		}
		
		// If there's another corpse at this location, write over it
		if(overridenCorpse != null) {
			this.corpses.remove(overridenCorpse);
		}
		
		// Add new corpse to manager
		this.corpses.add(newCorpse);
		
		try {
			// Repaints tile with corpse's image
			GCharacter deadNPC = newCorpse.getNPC();
			String corpseImg = deadNPC.getCorpseImage();
			GameScreen.getTile(deadNPC.getXPos(), deadNPC.getYPos()).setCorpseImage(corpseImg);
		} catch (ArrayIndexOutOfBoundsException e) {
			// Do nothing if out of bounds
		}
	}
	
	// Checks if a corpse exists within the manager
	// Returns true if found and removed
	// Returns false if not found
	public boolean removeCorpse(Corpse c) {
		if(this.corpses.contains(c)) {
			this.corpses.remove(c);
			GameScreen.getTile(c.getXPos(), c.getYPos()).setCorpseImage(GPath.NULL);
			return true;
		} else {
			return false;
		}
	}
	
	// Removes all corpses from list
	public void removeAll() {
		this.corpses.clear();
	}
	
	// Returns all the corpses in an ArrayList
	public ArrayList<Corpse> getCorpses() {
		return this.corpses;
	}
	
	// Sets the list of corpses to an input list value
	public void setCorpses(ArrayList<Corpse> corpses) {
		this.corpses = corpses;
	}
	
	// Returns an ArrayList of all of the GCharacters that are currently corpses
	public ArrayList<GCharacter> getCorpseCharacters() {
		ArrayList<GCharacter> output = new ArrayList<GCharacter>();
		for(Corpse c : this.corpses) {
			output.add(c.getNPC());
		}
		return output;
	}

}
