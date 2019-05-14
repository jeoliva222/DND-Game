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
import characters.Signpost;
import characters.TormentedSoul;
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
// ** DARK AREA **
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
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 1},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			//add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel m20 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel m01 = new MapLevel(new int[][] {
			{3, 3, 1, 1, 1, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(2, 3, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png"))),
				new ExtraTile(2, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png"))),
				new ExtraTile(7, 3, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png"))),
				new ExtraTile(7, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png")))
		}, new ArrayList<GCharacter>() {{
			add(new Signpost(2, 3, "statue1.png", "Endless Grin"));
			add(new Signpost(2, 6, "statue2.png", "Self Betrayal"));
			add(new Signpost(7, 3, "statue4.png", "Mister Spider"));
			add(new TormentedSoul(7, 6, "soul1"));
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
			{3, 3, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(2, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png"))),
				new ExtraTile(3, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png"))),
				new ExtraTile(4, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png"))),
				new ExtraTile(5, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png"))),
				new ExtraTile(6, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png"))),
				new ExtraTile(7, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png")))
		}, new ArrayList<GCharacter>() {{
			add(new Signpost(2, 6, "statue1.png", "Evolution of Decay: Part 1"));
			add(new Signpost(3, 6, "statue1.png", "Evolution of Decay: Part 2"));
			add(new Signpost(4, 6, "statue1.png", "Evolution of Decay: Part 3"));
			add(new Signpost(5, 6, "statue1.png", "Evolution of Decay: Part 4"));
			add(new Signpost(6, 6, "statue1.png", "Evolution of Decay: Part 5"));
			add(new Signpost(7, 6, "statue1.png", "Evolution of Decay: Part 6"));
		}}, new ArrayList<GPickup>() {{
			//add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel m21 = new MapLevel(new int[][] {
			{3, 1, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 3, 3, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 1, 3, 3, 3, 1, 3, 3, 3},
			{3, 1, 1, 1, 3, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			//add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		
		//************************************************
		// CONSTRUCTOR: AREA DEFINITION
		
		area_MUSEUM = new MapArea("Skin Museum", GPath.MUSEUM, GPath.createSoundPath("d2_m30.mid"), true,
				new MapLevel[][] {
			// Level grid definition
			{m00, m10, m20},
			{m01, m11, m21}
			//
		});
	
	}

}
