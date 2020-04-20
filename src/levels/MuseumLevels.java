package levels;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import ai.PatrolPattern;
import characters.GCharacter;
import characters.Hoptooth;
import characters.SaveCrystal;
import characters.Signpost;
import characters.TormentedSoul;
import characters.special.MuseumMoveStatue;
import helpers.GPath;
import items.GPickup;
import items.LargeHealthPotion;
import items.SmallHealthPotion;
import items.SmallMaxPotion;
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
			{3, 3, 3, 3, 3, 3, 1, 1, 1, 3},
			{3, 3, 1, 3, 1, 3, 1, 1, 1, 3},
			{3, 3, 1, 1, 1, 3, 1, 1, 1, 3},
			{3, 3, 1, 1, 1, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(6, 8, 
						new GroundButton(TriggerType.WALL_GROUND, true, new ArrayList<Dimension>() {{
							add(new Dimension(2, 6));
							add(new Dimension(4, 6));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new SaveCrystal(6, 6));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 6, new SmallHealthPotion()));
			add(new GPickup(8, 8, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel m10 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 4, 4, 1, 1, 1, 1},
			{3, 1, 1, 1, 4, 4, 1, 1, 1, 3},
			{3, 1, 1, 1, 4, 4, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(4, 5, 
						new Wall(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "sculpture_00.png"))),
				new ExtraTile(4, 4, 
						new Wall(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "sculpture_01.png"))),
				new ExtraTile(4, 3, 
						new Wall(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "sculpture_02.png"))),
				new ExtraTile(5, 5, 
						new Wall(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "sculpture_10.png"))),
				new ExtraTile(5, 4, 
						new Wall(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "sculpture_11.png"))),
				new ExtraTile(5, 3, 
						new Wall(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "sculpture_12.png")))
		}, new ArrayList<GCharacter>() {{
			add(new Hoptooth(7, 2));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel m20 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 0, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 1, 1, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(8, 5, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png")))
		}, new ArrayList<GCharacter>() {{
			add(new MuseumMoveStatue(9, 0, "statue1.png", "...", 8, 5, 3, 4));
			add(new MuseumMoveStatue(9, 0, "statue4.png", "...", 8, 5, 3, 5));
			add(new MuseumMoveStatue(9, 0, "statue3.png", "...", 8, 5, 3, 6));
			add(new MuseumMoveStatue(9, 0, "statue2.png", "...", 8, 5, 4, 3));
			add(new MuseumMoveStatue(9, 0, "statue_decay3.png", "...", 8, 5, 4, 4));
			add(new MuseumMoveStatue(9, 0, "statue3.png", "...", 8, 5, 4, 5));
			add(new MuseumMoveStatue(9, 0, "statue2.png", "...", 8, 5, 4, 6));
			add(new MuseumMoveStatue(9, 0, "statue_decay1.png", "...", 8, 5, 4, 7));
			add(new MuseumMoveStatue(9, 0, "statue5.png", "...", 8, 5, 5, 3));
			add(new MuseumMoveStatue(9, 0, "statue1.png", "...", 8, 5, 5, 7));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 5, new LargeHealthPotion()));
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
		}});
		
		//-------------------
		
		MapLevel m11 = new MapLevel(new int[][] {
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 3, 3, 3, 3, 3, 3},
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
			add(new MuseumMoveStatue(2, 6, "statue_decay1.png", "Evolution of Decay: Part 1", 8, 6, 2, 4));
			add(new Signpost(3, 6, "statue_decay2.png", "Evolution of Decay: Part 2"));
			add(new Signpost(4, 6, "statue_decay3.png", "Evolution of Decay: Part 3"));
			add(new Signpost(5, 6, "statue_decay4.png", "Evolution of Decay: Part 4"));
			add(new Signpost(6, 6, "statue_decay5.png", "Evolution of Decay: Part 5"));
			add(new Signpost(7, 6, "statue_decay6.png", "Evolution of Decay: Part 6"));
		}}, new ArrayList<GPickup>() {{
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
				new ExtraTile(6, 2, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
							add(new Dimension(4, 8));
						}})),
				new ExtraTile(2, 6, 
						new Ground(GPath.createImagePath(GPath.TILE, GPath.MUSEUM, "spotlight.png")))
		}, new ArrayList<GCharacter>() {{
			add(new Hoptooth(8, 2, PatrolPattern.STATIONARY));
			add(new Hoptooth(8, 7, PatrolPattern.STATIONARY));
			add(new MuseumMoveStatue(2, 6, "statue5.png", "Pipe Dream", 4, 8, 4, 2));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(6, 3, new SmallMaxPotion()));
			add(new GPickup(8, 8, new SmallMaxPotion()));
			add(new GPickup(1, 7, Armory.injector));
		}});
		
		//-------------------
		
		
		//************************************************
		// CONSTRUCTOR: AREA DEFINITION
		
		area_MUSEUM = new MapArea("Skin Museum", GPath.MUSEUM, GPath.createSoundPath("d2_m30.mid"), 100, true,
				new MapLevel[][] {
			// Level grid definition
			{m00, m10, m20},
			{m01, m11, m21}
			//
		});
	
	}

}
