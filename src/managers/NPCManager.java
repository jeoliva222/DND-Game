package managers;

import java.util.ArrayList;

import characters.GCharacter;

public class NPCManager {
	
	// ArrayList containing instances of all existing characters
	private ArrayList<GCharacter> npcs = new ArrayList<GCharacter>();
	
	private ArrayList<GCharacter> pending = new ArrayList<GCharacter>();
	
	// Constructor that starts with characters in it
	public NPCManager(ArrayList<GCharacter> npcs) {
		this.npcs = npcs;
	}
	
	// Blank constructor
	public NPCManager() {
	}
	
	// Adds a character to the list
	public void addCharacter(GCharacter npc) {
		this.npcs.add(npc);
	}
	
	// Adds character to pending list
	public void addPendingCharacter(GCharacter npc) {
		this.pending.add(npc);
	}
	
	// Checks if a character exists within the manager
	// Returns true if found and removed
	// Returns false if not found
	public boolean removeCharacter(GCharacter npc) {
		if(this.npcs.contains(npc)) {
			this.npcs.remove(npc);
			return true;
		} else {
			return false;
		}
	}
	
	// Clears all NPCs from the list
	public void removeAll() {
		this.npcs.clear();
	}
	
	// Moves all pending NPCs into the game, then clears pending list
	protected void movePending() {
		for(GCharacter pendNPC: this.pending) {
			this.npcs.add(pendNPC);
		}
		
		this.pending.clear();
	}
	
	// Returns all the corpses in an ArrayList
	public ArrayList<GCharacter> getCharacters() {
		return this.npcs;
	}
	
	// Sets a new list of NPCs
	public void setCharacters(ArrayList<GCharacter> npcs) {
		this.npcs = npcs;
	}

}
