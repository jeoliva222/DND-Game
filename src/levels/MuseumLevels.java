package levels;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import ai.PatrolPattern;
import characters.Beanpole;
import characters.Bitester;
import characters.BreakableWall;
import characters.BunnyWarrior;
import characters.GCharacter;
import helpers.GPath;
import items.GPickup;
import items.SmallHealthPotion;
import tiles.ExtraTile;
import tiles.GButton;
import tiles.Ground;
import tiles.GroundButton;
import tiles.TriggerType;
import tiles.Wall;
import weapons.Armory;

// Class representing level(s)/area definition for Skin Museum area
public class MuseumLevels implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = -8365130399974474597L;
	
	// Area definition
	public MapArea area_MUSEUM;

	//************************************************
	// CONSTRUCTOR: LEVEL DEFINITIONS
	
	@SuppressWarnings("serial")
	public MuseumLevels() {
	
		MapLevel m00 = new MapLevel(new int[][] {
			{3, 3, 1, 1, 1, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 1, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 3, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 1, 1, 1, 3, 3, 1, 3, 3},
			{3, 3, 1, 3, 1, 3, 1, 1, 1, 3},
			{3, 3, 1, 3, 1, 3, 1, 1, 1, 3},
			{3, 3, 1, 1, 1, 3, 1, 1, 1, 3},
			{3, 3, 1, 1, 1, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			//add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel m10 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel m20 = new MapLevel(new int[][] {
			{3, 1, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 3, 1, 1, 1, 3, 4, 4, 3},
			{3, 1, 1, 1, 1, 1, 3, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 1, 1, 3},
			{1, 1, 4, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 4, 1, 2, 2, 2, 1, 1, 3},
			{1, 1, 4, 1, 2, 1, 2, 1, 1, 3},
			{1, 1, 4, 1, 2, 2, 2, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel m01 = new MapLevel(new int[][] {
			{3, 3, 1, 1, 1, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 3, 1, 3, 3, 1, 3, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 3, 1, 3, 3, 1, 3, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			//add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel m11 = new MapLevel(new int[][] {
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel m21 = new MapLevel(new int[][] {
			{3, 1, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 3, 1, 1, 1, 3, 4, 4, 3},
			{3, 1, 1, 1, 1, 1, 3, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 1, 1, 3},
			{1, 1, 4, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 4, 1, 2, 2, 2, 1, 1, 3},
			{1, 1, 4, 1, 2, 1, 2, 1, 1, 3},
			{1, 1, 4, 1, 2, 2, 2, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		
		//************************************************
		// CONSTRUCTOR: AREA DEFINITION
		
		area_MUSEUM = new MapArea("Skin Museum", GPath.MUSEUM, GPath.createSoundPath("d2_m30.mid"),
				new MapLevel[][] {
			// Level grid definition
			{m00, m10, m20},
			{m01, m11, m21}
			//
		});
	
	}

}
