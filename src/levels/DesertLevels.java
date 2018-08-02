package levels;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import ai.PatrolPattern;
import characters.ArrowTurret;
import characters.Bitester;
import characters.BreakableWall;
import characters.BunnyWarrior;
import characters.EliteArrowTurret;
import characters.EliteBeanpole;
import characters.EliteBitester;
import characters.EliteBunnyWarrior;
import characters.EliteSandBeep;
import characters.GCharacter;
import characters.SandBeep;
import characters.SandWurm;
import characters.SnakeSoldier;
import helpers.GPath;
import items.GPickup;
import items.MediumHealthPotion;
import items.SmallHealthPotion;
import items.SmallMaxPotion;
import tiles.AltGround;
import tiles.AltWall;
import tiles.ExtraTile;
import tiles.GButton;
import tiles.Ground;
import tiles.GroundButton;
import tiles.TriggerType;
import tiles.Wall;
import weapons.Armory;

// Contains definitions of all the Poacher's Desert levels
// as well as the connections between them
@SuppressWarnings("serial")
public class DesertLevels implements Serializable {

	// Serialization ID
	private static final long serialVersionUID = 3040652509608828991L;
	
	// Area definition
	public MapArea area_DESERT;
	
	//************************************************
	// CONSTRUCTOR: LEVEL DEFINITIONS
	
	public DesertLevels() {
		
		//------------------- // ROW 0
		
		MapLevel d00 = new MapLevel(new int[][] {
			{4, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{4, 3, 3, 1, 1, 1, 1, 3, 1, 1},
			{4, 3, 1, 3, 1, 1, 1, 1, 1, 1},
			{4, 0, 1, 1, 1, 3, 2, 1, 1, 1},
			{0, 0, 0, 1, 1, 2, 2, 2, 1, 1},
			{0, 0, 1, 1, 2, 2, 2, 3, 1, 1},
			{4, 0, 1, 1, 1, 2, 1, 1, 1, 1},
			{4, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{4, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{4, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Bitester(7, 4, PatrolPattern.SURFACE_CW));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d10 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 3, 1, 1, 1, 1, 3, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 3, 1},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 2, 3, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 3, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandBeep(4, 4));
			add(new SandBeep(5, 7));
			add(new SandBeep(7, 2));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d20 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 3, 3, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 4, 4, 4, 1, 3, 1},
			{1, 1, 1, 1, 1, 0, 4, 1, 1, 1},
			{1, 1, 1, 4, 0, 0, 4, 1, 1, 1},
			{1, 1, 1, 4, 1, 0, 0, 0, 1, 1},
			{1, 1, 1, 4, 4, 4, 4, 1, 1, 1},
			{1, 1, 1, 1, 1, 2, 4, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new BunnyWarrior(5, 5, PatrolPattern.PATROL_CW));
			add(new SandBeep(2, 7));
			add(new SandBeep(7, 6));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d30 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 2, 2, 2, 2, 2, 2, 2, 1},
			{1, 1, 1, 2, 2, 2, 2, 2, 1, 1},
			{1, 1, 1, 1, 2, 2, 1, 3, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 3, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(6, 3, PatrolPattern.WANDER));
			add(new BunnyWarrior(4, 7, PatrolPattern.WANDER));
			add(new BunnyWarrior(6, 6, PatrolPattern.WANDER));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d40 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 4, 4, 4, 4, 3},
			{1, 1, 1, 1, 1, 4, 0, 0, 4, 2},
			{1, 1, 3, 1, 1, 4, 0, 0, 4, 2},
			{1, 1, 3, 1, 1, 4, 0, 4, 4, 1},
			{1, 1, 1, 3, 1, 4, 0, 1, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 3, 3, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(3, 8, PatrolPattern.STATIONARY));
			add(new BunnyWarrior(3, 3, PatrolPattern.STATIONARY));
			add(new EliteBunnyWarrior(7, 2, PatrolPattern.PATROL_CW));
			add(new BunnyWarrior(7, 1, PatrolPattern.PATROL_CW));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(4, 6, new SmallHealthPotion()));
			add(new GPickup(7, 1, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d50 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{2, 2, 2, 2, 1, 1, 1, 1, 1, 1},
			{2, 2, 1, 1, 1, 3, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
			{1, 1, 1, 3, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 2, 3, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(7, 6, PatrolPattern.SURFACE_CW));
			add(new SandBeep(4, 5));
			add(new SandBeep(8, 3));
			add(new SandWurm(5, 1));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d60 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 3, 3, 1, 1, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 3, 3, 3},
			{1, 1, 1, 3, 2, 1, 1, 2, 3, 3},
			{1, 1, 1, 3, 3, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 3, 1, 3, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 2, 3},
			{1, 1, 3, 1, 1, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteSandBeep(5, 4));
			add(new SandWurm(2, 3));
			add(new SandWurm(6, 7));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(6, 2, new SmallMaxPotion()));
			add(new GPickup(7, 3, new SmallHealthPotion()));
		}});
		
		//------------------- // ROW 1
		
		MapLevel d01 = new MapLevel(new int[][] {
			{4, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{4, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{4, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{4, 2, 1, 1, 1, 3, 1, 1, 1, 1},
			{4, 2, 2, 1, 1, 1, 1, 1, 1, 1},
			{4, 4, 4, 4, 1, 1, 3, 1, 1, 1},
			{4, 0, 0, 4, 1, 1, 3, 2, 1, 1},
			{4, 0, 0, 1, 0, 1, 3, 2, 1, 1},
			{4, 4, 4, 4, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(2, 7, PatrolPattern.PATROL_CW));
			add(new BunnyWarrior(2, 6, PatrolPattern.PATROL_CW));
			add(new BunnyWarrior(2, 2, PatrolPattern.WANDER));
			add(new BunnyWarrior(7, 5, PatrolPattern.WANDER));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 6, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d11 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 3, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 4, 4, 4, 4, 4, 1, 1},
			{1, 1, 1, 4, 0, 0, 1, 4, 1, 1},
			{1, 1, 4, 4, 0, 0, 0, 1, 0, 1},
			{1, 1, 0, 0, 0, 1, 4, 4, 1, 1},
			{1, 1, 1, 0, 0, 1, 4, 1, 1, 1},
			{1, 1, 1, 4, 4, 1, 4, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteArrowTurret(4, 3, 1, 0, 2, true));
			add(new SandWurm(5, 5));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d21 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 4, 1, 1, 1, 4, 1, 1},
			{1, 1, 1, 4, 4, 4, 4, 4, 4, 4},
			{1, 1, 1, 1, 0, 0, 0, 4, 0, 0},
			{1, 1, 1, 0, 0, 0, 1, 4, 0, 0},
			{1, 1, 3, 4, 0, 0, 4, 1, 0, 0},
			{1, 1, 1, 4, 0, 0, 4, 0, 4, 0},
			{1, 3, 1, 4, 4, 0, 0, 0, 4, 4},
			{1, 1, 1, 4, 4, 4, 4, 4, 4, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d31 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 4, 4, 4, 1, 1, 4, 1, 1, 1},
			{4, 4, 0, 1, 4, 4, 4, 4, 1, 1},
			{0, 0, 0, 0, 0, 0, 0, 0, 4, 1},
			{0, 0, 4, 0, 0, 0, 0, 4, 4, 1},
			{0, 0, 4, 0, 0, 0, 0, 0, 4, 1},
			{0, 0, 4, 1, 0, 0, 0, 0, 4, 1},
			{4, 0, 0, 0, 0, 0, 0, 4, 4, 1},
			{1, 4, 4, 4, 0, 0, 0, 1, 4, 1},
			{1, 1, 1, 1, 4, 0, 0, 4, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d41 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{1, 3, 1, 1, 1, 1, 1, 1, 3, 1},
			{1, 1, 1, 1, 3, 3, 1, 1, 1, 1},
			{1, 1, 3, 1, 2, 2, 1, 1, 1, 1},
			{1, 1, 1, 1, 2, 2, 3, 1, 1, 1},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 3, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d51 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 4, 4, 4, 4},
			{1, 4, 4, 4, 4, 4, 4, 0, 0, 0},
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 4},
			{1, 4, 0, 4, 0, 0, 4, 0, 4, 4},
			{1, 4, 0, 0, 4, 4, 0, 0, 4, 4},
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 4},
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 1},
			{1, 4, 0, 4, 4, 4, 4, 0, 4, 1},
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(5, 4, PatrolPattern.SURFACE_CW));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d61 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{4, 4, 4, 4, 1, 1, 4, 4, 4, 4},
			{0, 0, 0, 4, 1, 1, 4, 0, 0, 4},
			{4, 4, 0, 4, 4, 4, 4, 4, 0, 4},
			{4, 0, 0, 4, 1, 1, 4, 0, 0, 4},
			{4, 0, 0, 4, 1, 1, 4, 0, 1, 4},
			{4, 4, 0, 4, 1, 1, 4, 0, 1, 4},
			{1, 4, 4, 4, 1, 1, 4, 0, 0, 4},
			{1, 1, 4, 4, 1, 1, 0, 0, 0, 4},
			{1, 1, 1, 1, 1, 1, 4, 0, 0, 4}
		}, new ExtraTile[] {
				new ExtraTile(2, 6, 
						new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
							add(new Dimension(4, 3));
							add(new Dimension(5, 3));
						}})),	
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//------------------- // ROW 2
		
		MapLevel d02 = new MapLevel(new int[][] {
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 4, 4, 4, 4, 4, 4, 4, 1},
			{3, 3, 4, 4, 1, 3, 0, 0, 4, 1},
			{3, 2, 2, 0, 0, 4, 4, 4, 4, 1},
			{3, 4, 4, 0, 0, 4, 0, 1, 4, 1},
			{3, 4, 0, 4, 0, 0, 1, 2, 4, 1},
			{3, 4, 4, 4, 4, 4, 0, 4, 2, 1},
			{3, 1, 4, 4, 0, 0, 0, 4, 4, 1},
			{3, 4, 4, 0, 0, 0, 0, 1, 4, 1}
		}, new ExtraTile[] {
				new ExtraTile(3, 5, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
							add(new Dimension(6, 4));
							add(new Dimension(5, 4));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(7, 3, PatrolPattern.STATIONARY));
			add(new SandBeep(2, 6));
			add(new Bitester(1, 4));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(3, 5, Armory.rustedSabre));
		}});
		
		//-------------------
		
		MapLevel d12 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 3, 1, 1, 1, 3, 1, 1},
			{1, 1, 1, 3, 1, 1, 3, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 3, 1, 1},
			{1, 3, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 3, 3, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandWurm(4, 3));
			add(new SandWurm(7, 4));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d22 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 3, 1, 1, 3, 1, 3, 1, 1},
			{1, 3, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 3, 1, 3, 1, 3, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
			{1, 1, 1, 3, 1, 1, 1, 1, 3, 1},
			{1, 1, 3, 1, 1, 1, 3, 1, 1, 1},
			{1, 1, 1, 3, 1, 3, 1, 1, 3, 1},
			{1, 3, 1, 1, 1, 3, 1, 3, 1, 1},
			{1, 3, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d32 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 4, 0, 0, 4, 1, 1},
			{1, 4, 4, 4, 4, 0, 0, 4, 4, 1},
			{1, 4, 0, 4, 4, 4, 0, 0, 4, 1},
			{1, 4, 4, 0, 0, 0, 0, 0, 4, 1},
			{1, 4, 0, 0, 0, 4, 0, 4, 4, 1},
			{1, 4, 0, 0, 0, 4, 0, 0, 4, 1},
			{1, 4, 0, 1, 0, 0, 0, 0, 4, 1},
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 1},
			{1, 4, 4, 4, 4, 4, 4, 4, 4, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d42 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 3, 1, 1, 1, 1, 2, 1},
			{1, 1, 1, 2, 2, 1, 3, 1, 2, 1},
			{1, 1, 1, 2, 2, 3, 2, 2, 1, 1},
			{1, 1, 2, 2, 2, 2, 2, 2, 1, 1},
			{1, 3, 2, 2, 2, 2, 2, 2, 1, 1},
			{1, 1, 1, 2, 2, 2, 2, 3, 1, 1},
			{1, 1, 1, 2, 2, 2, 2, 3, 1, 1},
			{1, 1, 1, 3, 2, 2, 1, 1, 3, 1},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(7, 2, PatrolPattern.WANDER));
			add(new SnakeSoldier(2, 6, PatrolPattern.WANDER));
			add(new SnakeSoldier(5, 5, PatrolPattern.WANDER));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d52 = new MapLevel(new int[][] {
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 1},
			{1, 4, 4, 4, 4, 4, 4, 4, 4, 4},
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 4},
			{1, 4, 4, 0, 0, 0, 0, 0, 0, 4},
			{1, 4, 4, 4, 4, 0, 4, 4, 4, 4},
			{1, 4, 4, 0, 0, 0, 0, 0, 0, 4},
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 4},
			{1, 4, 4, 0, 0, 0, 0, 0, 0, 0},
			{1, 3, 4, 4, 4, 4, 4, 4, 4, 4},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
				new ExtraTile(5, 4, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(5, 1, new AltGround()));
							add(new ExtraTile(8, 7, new AltWall()));
						}})),
				new ExtraTile(7, 2, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(8, 7, new AltGround()));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new ArrowTurret(2, 2, 1, 0, 5));
			add(new ArrowTurret(2, 6, 1, 0, 5));
			add(new ArrowTurret(8, 5, -1, 0, 5));
			add(new ArrowTurret(8, 3, -1, 0, 5));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d62 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 4, 0, 0, 4},
			{4, 4, 4, 4, 4, 4, 4, 0, 0, 4},
			{4, 4, 4, 4, 0, 5, 5, 0, 0, 4},
			{4, 0, 0, 4, 0, 5, 5, 0, 0, 4},
			{4, 0, 0, 4, 0, 5, 5, 0, 0, 4},
			{4, 0, 0, 4, 0, 5, 5, 0, 0, 4},
			{4, 0, 0, 4, 4, 4, 4, 4, 4, 4},
			{0, 0, 0, 0, 4, 2, 2, 2, 2, 3},
			{4, 4, 4, 0, 1, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3}
		}, new ExtraTile[] {
				new ExtraTile(2, 7, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
							add(new ArrowTurret(1, 2, 0, 1, 3, 2));
							add(new ArrowTurret(2, 2, 0, 1, 3));
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(3, 7, new AltWall()));
							add(new ExtraTile(0, 7, new AltWall()));
							add(new ExtraTile(1, 2, new AltGround()));
							add(new ExtraTile(2, 2, new AltGround()));
						}})),
				new ExtraTile(1, 3, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(3, 7, new AltGround()));
							add(new ExtraTile(0, 7, new AltGround()));
						}})),
				new ExtraTile(7, 2, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(7, 1, new AltWall()));
							add(new ExtraTile(8, 1, new AltWall()));
							add(new ExtraTile(8, 2, new AltGround()));
						}})),
				new ExtraTile(8, 2, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(7, 1, new AltWall()));
							add(new ExtraTile(8, 1, new AltWall()));
							add(new ExtraTile(7, 2, new AltGround()));
						}})),
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(4, 2, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//------------------- // ROW 3
		
		MapLevel d03 = new MapLevel(new int[][] {
			{3, 4, 4, 0, 0, 0, 1, 1, 4, 1},
			{3, 1, 1, 1, 0, 1, 0, 2, 4, 1},
			{3, 1, 3, 1, 0, 0, 4, 4, 2, 1},
			{3, 1, 1, 0, 0, 0, 4, 2, 2, 1},
			{3, 3, 0, 1, 0, 4, 2, 2, 2, 1},
			{3, 1, 1, 1, 0, 0, 0, 0, 1, 1},
			{3, 1, 3, 0, 0, 4, 2, 2, 1, 3},
			{3, 1, 0, 0, 0, 0, 4, 2, 2, 1},
			{3, 1, 1, 0, 4, 0, 4, 4, 2, 1},
			{3, 3, 1, 0, 0, 0, 1, 2, 4, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandBeep(2, 1));
			add(new SandBeep(3, 8));
			add(new SandBeep(2, 7));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d13 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 3, 3, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
			{1, 3, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 3, 1, 1, 1, 3, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
			{3, 1, 3, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 3, 1, 1, 1, 1, 3, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d23 = new MapLevel(new int[][] {
			{1, 3, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 3, 1, 1, 1, 3, 1, 3},
			{1, 1, 1, 1, 4, 0, 4, 4, 1, 1},
			{1, 1, 1, 1, 0, 1, 0, 0, 4, 1},
			{1, 1, 3, 1, 1, 1, 1, 0, 1, 1},
			{1, 1, 3, 1, 1, 3, 1, 4, 1, 1},
			{3, 1, 1, 1, 1, 4, 3, 1, 1, 1},
			{1, 3, 3, 1, 0, 1, 1, 1, 3, 1},
			{1, 1, 1, 1, 1, 1, 1, 4, 1, 1},
			{1, 1, 1, 1, 1, 1, 0, 4, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d33 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 2, 2, 2, 2, 1, 1, 1},
			{1, 1, 2, 2, 2, 2, 2, 2, 3, 1},
			{1, 1, 3, 2, 2, 2, 2, 2, 2, 2},
			{1, 2, 2, 2, 2, 1, 1, 2, 2, 1},
			{1, 1, 2, 2, 2, 1, 2, 2, 1, 1},
			{1, 1, 2, 2, 2, 2, 2, 1, 3, 1},
			{1, 2, 2, 1, 1, 2, 2, 3, 1, 1},
			{1, 2, 2, 2, 1, 1, 2, 2, 1, 1},
			{1, 1, 2, 2, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBitester(4, 3));
			add(new EliteBitester(5, 6));
			add(new Bitester(6, 2));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 4, Armory.cactusClaws));
			add(new GPickup(5, 5, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d43 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 3, 1, 1, 1, 1, 1, 3, 1, 1},
			{1, 1, 1, 3, 1, 1, 3, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandWurm(4, 3));
			add(new EliteSandBeep(6, 7));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d53 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 3, 3, 1, 1, 1, 1, 3, 1, 3},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 3, 1, 1, 3, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 3, 3, 1, 1, 1},
			{1, 3, 1, 1, 3, 1, 1, 1, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d63 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 3, 1, 3},
			{1, 1, 1, 3, 1, 1, 3, 1, 3, 3},
			{3, 1, 1, 1, 1, 3, 1, 3, 1, 3},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 3, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 1, 3, 1, 1, 3, 3, 3, 3},
			{3, 3, 1, 1, 1, 3, 3, 1, 1, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//------------------- // ROW 4
		
		MapLevel d04 = new MapLevel(new int[][] {
			{3, 3, 1, 0, 0, 0, 2, 2, 4, 1},
			{3, 1, 3, 1, 0, 1, 1, 2, 4, 1},
			{3, 1, 1, 0, 1, 3, 1, 4, 1, 1},
			{3, 1, 3, 0, 1, 0, 1, 4, 1, 1},
			{3, 1, 1, 1, 0, 1, 3, 3, 1, 1},
			{3, 3, 4, 4, 0, 1, 1, 3, 1, 1},
			{3, 3, 0, 0, 0, 1, 4, 1, 3, 1},
			{3, 1, 0, 1, 4, 4, 4, 1, 1, 1},
			{3, 0, 1, 2, 4, 2, 1, 1, 3, 1},
			{3, 4, 4, 4, 2, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
//			add(new SandBeep(2, 1));
//			add(new SandBeep(3, 8));
//			add(new SandBeep(2, 7));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d14 = new MapLevel(new int[][] {
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 4, 1, 1},
			{1, 1, 1, 1, 4, 1, 1, 1, 1, 1},
			{1, 1, 1, 0, 0, 1, 1, 4, 1, 1},
			{1, 1, 1, 4, 0, 1, 0, 4, 1, 1},
			{1, 1, 1, 1, 1, 3, 0, 1, 1, 1},
			{1, 1, 4, 1, 0, 0, 0, 1, 0, 1},
			{1, 1, 1, 4, 1, 1, 4, 4, 1, 1},
			{1, 1, 1, 1, 4, 0, 4, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(6, 5, PatrolPattern.SURFACE_CW));
			add(new EliteBeanpole(5, 2));
			add(new EliteBeanpole(3, 6));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d24 = new MapLevel(new int[][] {//
			{1, 3, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 3, 1, 1, 1, 3, 1, 3},
			{1, 1, 1, 1, 4, 0, 4, 4, 1, 1},
			{1, 1, 1, 1, 0, 1, 0, 0, 4, 1},
			{1, 1, 3, 1, 1, 1, 1, 0, 1, 1},
			{1, 1, 3, 1, 1, 3, 1, 4, 1, 1},
			{3, 1, 1, 1, 1, 4, 3, 1, 1, 1},
			{1, 3, 3, 1, 0, 1, 1, 1, 3, 1},
			{1, 1, 1, 1, 1, 1, 1, 4, 1, 1},
			{1, 1, 1, 1, 1, 1, 0, 4, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d34 = new MapLevel(new int[][] {//
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 2, 2, 2, 2, 1, 1, 1},
			{1, 1, 2, 2, 2, 2, 2, 2, 3, 1},
			{1, 1, 3, 2, 2, 2, 2, 2, 2, 2},
			{1, 2, 2, 2, 2, 1, 1, 2, 2, 1},
			{1, 1, 2, 2, 2, 1, 2, 2, 1, 1},
			{1, 1, 2, 2, 2, 2, 2, 1, 3, 1},
			{1, 2, 2, 1, 1, 2, 2, 3, 1, 1},
			{1, 2, 2, 2, 1, 1, 2, 2, 1, 1},
			{1, 1, 2, 2, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBitester(4, 3));
			add(new EliteBitester(5, 6));
			add(new Bitester(6, 2));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 4, Armory.cactusClaws));
			add(new GPickup(5, 5, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d44 = new MapLevel(new int[][] {//
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 3, 1, 1, 1, 1, 1, 3, 1, 1},
			{1, 1, 1, 3, 1, 1, 3, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandWurm(4, 3));
			add(new EliteSandBeep(6, 7));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d54 = new MapLevel(new int[][] {//
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 3, 3, 1, 1, 1, 1, 3, 1, 3},
			{1, 1, 1, 1, 3, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 3, 1, 1, 3, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 3, 3, 1, 1, 1},
			{1, 3, 1, 1, 3, 1, 1, 1, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d64 = new MapLevel(new int[][] {//
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 3, 1, 3},
			{1, 1, 1, 3, 1, 1, 3, 1, 3, 3},
			{3, 1, 1, 1, 1, 3, 1, 3, 1, 3},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 3, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 1, 3, 1, 1, 3, 3, 3, 3},
			{3, 3, 1, 1, 1, 3, 3, 1, 1, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		
		//************************************************
		// AREA DEFINITION
		
		area_DESERT = new MapArea("Poacher's Desert", GPath.DESERT, GPath.createSoundPath("d_e2m1.mid"),
				new MapLevel[][] {
			// Level grid definition
			{d00, d10, d20, d30, d40, d50, d60},
			{d01, d11, d21, d31, d41, d51, d61},
			{d02, d12, d22, d32, d42, d52, d62},
			{d03, d13, d23, d33, d43, d53, d63},
			{d04, d14, d24, d34, d44, d54, d64},
			//
		});
	
	}
	
}
