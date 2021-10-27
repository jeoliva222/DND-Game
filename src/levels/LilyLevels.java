package levels;

import helpers.GPath;
import items.GPickup;
import items.LargeHealthPotion;
import items.SmallHealthPotion;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import tiles.ExtraTile;
import tiles.Ground;
import tiles.GroundButton;
import tiles.TriggerType;
import weapons.Armory;
import ai.PatrolPattern;
import characters.Beanpole;
import characters.Beep;
import characters.Bitester;
import characters.BreakableWall;
import characters.BunnyWarrior;
import characters.GCharacter;
import characters.Hoptooth;
import characters.SaveCrystal;
import characters.Signpost;

public class LilyLevels implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = -177643533735729842L;
	
	// Area definition
	public MapArea area_LILY;

	//************************************************
	// CONSTRUCTOR: LEVEL DEFINITIONS
	
	@SuppressWarnings("serial")
	public LilyLevels() {
	
		//------------------- // ROW 0
		
		MapLevel s00 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 3, 2, 1, 2, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 4, 1, 1},
			{3, 3, 4, 3, 3, 3, 1, 2, 1, 1},
			{3, 1, 1, 2, 1, 3, 1, 2, 1, 3},
			{3, 1, 1, 2, 2, 3, 1, 2, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 4, 1, 3},
			{3, 1, 3, 3, 1, 1, 1, 2, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 2, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
			new ExtraTile(4, 2, 
					new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
						add(new Dimension(2, 3));
					}}))
			}, new ArrayList<GCharacter>() {{
			add(new Beanpole(2, 8, PatrolPattern.STATIONARY));
			add(new Beanpole(8, 2, PatrolPattern.SURFACE_CW));
			add(new BreakableWall(4, 2, 2));
			add(new BreakableWall(5, 1, 2));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(4, 4, Armory.brokenSword));
			add(new GPickup(5, 1, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel s10 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 2, 2, 1, 3},
			{1, 1, 3, 3, 1, 1, 2, 2, 2, 3},
			{1, 1, 3, 1, 1, 1, 2, 2, 2, 3},
			{3, 1, 3, 3, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{3, 1, 2, 1, 1, 2, 2, 2, 2, 3},
			{3, 2, 1, 2, 2, 1, 1, 2, 1, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Bitester(6, 2, PatrolPattern.SURFACE_CW));
			add(new BunnyWarrior(3, 3, PatrolPattern.STATIONARY));
			add(new Beep(4, 8));
			add(new Beep(1, 7));
			add(new Beep(1, 8));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(6, 5, new SmallHealthPotion()));
			add(new GPickup(8, 1, Armory.longStick));
		}});
		
		//------------------- // ROW 1
		
		MapLevel s01 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 3, 3, 1, 3},
			{3, 1, 1, 3, 1, 1, 1, 3, 1, 3},
			{3, 1, 1, 3, 1, 3, 1, 3, 1, 3},
			{3, 1, 3, 3, 1, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 3, 3, 1, 1, 3},
			{3, 1, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(4, 1, 2));
			add(new SaveCrystal(7, 1));
			add(new Hoptooth(2, 1));
			add(new Hoptooth(1, 2));
			add(new Signpost(1, 1, "jovi.png", "Jovi loves you even more."));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 1, Armory.ironSpear));
			add(new GPickup(8, 2, new LargeHealthPotion()));
		}}, true);
		
		//-------------------
		
		MapLevel s11 = new MapLevel(new int[][] {
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3},
			{4, 2, 2, 2, 2, 3, 3, 3, 3, 3},
			{3, 2, 1, 1, 2, 2, 3, 3, 3, 3},
			{3, 2, 1, 1, 1, 2, 3, 3, 3, 3},
			{3, 2, 2, 2, 2, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 1, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 4, 4, 3, 4, 4},
			{3, 3, 3, 3, 3, 4, 4, 4, 4, 4},
			{3, 3, 3, 3, 3, 3, 4, 4, 4, 3},
			{3, 3, 3, 3, 3, 3, 3, 4, 3, 3}
		}, new ExtraTile[] {
			new ExtraTile(2, 2, 
					new Ground(GPath.createImagePath(GPath.TILE, GPath.SPECIAL, "be.png"))),
			new ExtraTile(3, 2, 
					new Ground(GPath.createImagePath(GPath.TILE, GPath.SPECIAL, "my.png"))),
			new ExtraTile(2, 3, 
					new Ground(GPath.createImagePath(GPath.TILE, GPath.SPECIAL, "gf0.png"))),
			new ExtraTile(3, 3, 
					new Ground(GPath.createImagePath(GPath.TILE, GPath.SPECIAL, "gf1.png"))),
			new ExtraTile(4, 3, 
					new Ground(GPath.createImagePath(GPath.TILE, GPath.SPECIAL, "gf2.png"))),
			new ExtraTile(4, 5, 
					new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
						add(new Dimension(0, 1));
					}}))
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(4, 5, 2));
			add(new Signpost(5, 2, "jovi.png", "Jovi loves you."));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		
		//************************************************
		// CONSTRUCTOR: AREA DEFINITION
		
		area_LILY = new MapArea("The Beginning", GPath.TEMPLE, GPath.createSoundPath("d_e2m6.mid"), 30,
				new MapLevel[][] {
			// Level grid definition
			{s00, s10},
			{s01, s11}
			//
		});
	
	}
	
}
