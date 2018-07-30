package levels;

import java.io.Serializable;
import java.util.ArrayList;

import characters.Corpse;
import characters.GCharacter;
import gui.GameInitializer;
import items.GPickup;
import tiles.AltGround;
import tiles.AltWall;
import tiles.ExtraTile;
import tiles.Ground;
import tiles.Pit;
import tiles.TileType;
import tiles.Wall;
import tiles.Water;

// Class representing a screen/level in the game
public class MapLevel implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = -2879853752073596078L;

	// Grid of tiles that make up a screen/level
	protected TileType[][] tiles;
	
	// List of characters on screen that are still alive
	protected ArrayList<GCharacter> npcs;
	
	// List of corpses left on the screen
	protected ArrayList<Corpse> corpses;
	
	// List of items on the screen
	protected ArrayList<GPickup> pickups;
	
	// Constructor
	protected MapLevel(int[][] map, ExtraTile[] extras, ArrayList<GCharacter> npcs, ArrayList<GPickup> pickups) {
		// Initialize TileType grid
		this.tiles = new TileType[GameInitializer.yDimen][GameInitializer.xDimen];
		
		// Convert int-code to Tiles
		// 0 = Alternate Ground
		// 1 = Ground
		// 2 = Water
		// 3 = Wall
		// 4 = Alternate Wall
		// 5 = Pit
		for(int y = 0; y < GameInitializer.yDimen; y++) {
			for(int x = 0; x < GameInitializer.xDimen; x++) {
				TileType tt;
				if(map[y][x] == 0) {
					tt = new AltGround();
				} else if(map[y][x] == 1) {
					tt = new Ground();
				} else if (map[y][x] == 2) {
					tt = new Water();
				} else if (map[y][x] == 3) {
					tt = new Wall();
				} else if (map[y][x] == 4) {
					tt = new AltWall();
				} else if (map[y][x] == 5) {
					tt = new Pit();
				}  else {
					tt = new Wall();
				}
				
				this.tiles[y][x] = tt;
			}
		}
		
		// Add in extra tiles
		for(ExtraTile et: extras) {
			this.tiles[et.yPos][et.xPos] = et.tile;
		}
		
		// Add in enemies
		this.npcs = npcs;
		
		// Initialize corpse array
		this.corpses = new ArrayList<Corpse>();
		
		// Add in items
		this.pickups = pickups;
	}
	
	//--------------------
	// Getters and Setters
	
	public TileType[][] getTiles() {
		return this.tiles;
	}
	
	public TileType getTile(int x, int y) {
		return this.tiles[y][x];
	}
	
	public void setTiles(TileType[][] tiles) {
		this.tiles = tiles;
	}
	
	public ArrayList<GCharacter> getNPCs() {
		return this.npcs;
	}
	
	public void setNPCs(ArrayList<GCharacter> npcs) {
		this.npcs = npcs;
	}
	
	public ArrayList<Corpse> getCorpses() {
		return this.corpses;
	}
	
	public void setCorpses(ArrayList<Corpse> corpses) {
		this.corpses = corpses;
	}
	
	public ArrayList<GPickup> getPickups() {
		return this.pickups;
	}
	
	public void setPickups(ArrayList<GPickup> pickups) {
		this.pickups = pickups;
	}
	
}
