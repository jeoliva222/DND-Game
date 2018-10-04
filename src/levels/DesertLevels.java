package levels;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import ai.PatrolPattern;
import characters.ArrowTurret;
import characters.Beanpole;
import characters.Bitester;
import characters.BreakableWall;
import characters.BunnyWarrior;
import characters.Cactian;
import characters.EliteArrowTurret;
import characters.EliteBeanpole;
import characters.EliteBitester;
import characters.EliteBunnyWarrior;
import characters.GCharacter;
import characters.SandBeep;
import characters.SandWurm;
import characters.SaveCrystal;
import characters.SnakeCommander;
import characters.SnakeSoldier;
import characters.bosses.SnakeNuke;
import characters.bosses.SnakeTank;
import helpers.GPath;
import items.GKey;
import items.GPickup;
import items.LargeHealthPotion;
import items.MediumHealthPotion;
import items.SmallHealthPotion;
import items.SmallMaxPotion;
import tiles.AltGround;
import tiles.AltWall;
import tiles.ExtraTile;
import tiles.GButton;
import tiles.Ground;
import tiles.GroundButton;
import tiles.KeyDoor;
import tiles.TriggerType;
import tiles.WaterButton;
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
			add(new BreakableWall(4, 7));
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
			{1, 1, 1, 1, 1, 1, 3, 3, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 2, 3},
			{1, 1, 3, 1, 1, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandBeep(5, 4));
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
			add(new SandBeep(2, 3));
			add(new BreakableWall(7, 6));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(7, 6, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d21 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 4, 1, 1, 1, 4, 1, 1},
			{1, 1, 1, 4, 4, 4, 4, 4, 4, 4},
			{1, 1, 1, 1, 0, 0, 0, 4, 0, 0},
			{1, 1, 1, 0, 0, 0, 1, 4, 0, 0},
			{1, 1, 1, 4, 0, 0, 4, 1, 0, 0},
			{1, 1, 1, 4, 0, 0, 4, 0, 4, 0},
			{1, 3, 1, 4, 4, 0, 0, 0, 4, 4},
			{1, 1, 1, 4, 4, 4, 4, 4, 4, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Cactian(6, 4));
			add(new Cactian(2, 5));
			add(new SaveCrystal(8, 3));
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
			add(new SnakeSoldier(5, 5, PatrolPattern.SURFACE_CCW));
			add(new SnakeSoldier(6, 6, PatrolPattern.SURFACE_CCW));
			add(new ArrowTurret(7, 3, -1, 0, 6));
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
			add(new SandBeep(3, 3));
			add(new SandWurm(6, 7));
			add(new Bitester(4, 6, PatrolPattern.SURFACE_CCW));
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
			add(new SnakeCommander(5, 4, PatrolPattern.SURFACE_CW));
			add(new SnakeSoldier(6, 5, PatrolPattern.STATIONARY));
			add(new SnakeSoldier(3, 5, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d61 = new MapLevel(new int[][] { // TODO Marker
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{4, 4, 4, 4, 1, 1, 4, 4, 4, 4},
			{0, 0, 0, 4, 1, 1, 4, 0, 0, 4},
			{4, 4, 0, 4, 4, 4, 4, 4, 0, 4},
			{4, 0, 0, 4, 1, 1, 4, 0, 0, 4},
			{4, 0, 0, 4, 1, 1, 4, 0, 1, 4},
			{4, 4, 0, 4, 1, 1, 4, 0, 1, 4},
			{1, 4, 4, 4, 1, 1, 4, 0, 0, 4},
			{1, 1, 4, 4, 0, 0, 0, 0, 0, 4},
			{1, 1, 1, 4, 0, 0, 4, 4, 4, 4}
		}, new ExtraTile[] {
				new ExtraTile(2, 6, 
						new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
							add(new Dimension(4, 3));
							add(new Dimension(5, 3));
						}}))	
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(1, 5, PatrolPattern.STATIONARY));
			add(new SaveCrystal(7, 4));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 4, new MediumHealthPotion()));
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
			{1, 1, 4, 4, 0, 0, 0, 4, 4, 1},
			{3, 1, 4, 0, 0, 0, 0, 1, 4, 1}
		}, new ExtraTile[] {
				new ExtraTile(3, 5, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
							add(new Dimension(6, 4));
							add(new Dimension(5, 4));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(1, 8));
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
			add(new Cactian(2, 4));
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
			add(new Cactian(3, 2));
			add(new Cactian(6, 5));
			add(new Cactian(2, 7));
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
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 1},
			{1, 4, 0, 0, 0, 0, 0, 0, 4, 1},
			{1, 4, 4, 4, 4, 4, 4, 4, 4, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
				new ExtraTile(3, 6, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
							add(new SnakeSoldier(3, 2));
							add(new SnakeSoldier(2, 3));
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(5, 1, new AltWall()));
							add(new ExtraTile(6, 1, new AltWall()));
							add(new ExtraTile(3, 2, new AltGround()));
							add(new ExtraTile(2, 3, new AltGround()));
						}})),
				new ExtraTile(2, 2, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, true, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(5, 1, new AltGround()));
							add(new ExtraTile(6, 1, new AltGround()));
						}}))
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(3, 6, Armory.woodenTarge));
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
			add(new SnakeCommander(5, 0, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d62 = new MapLevel(new int[][] { // TODO Marker
			{1, 1, 1, 4, 0, 0, 4, 4, 4, 4},
			{4, 4, 4, 4, 0, 0, 4, 4, 4, 4},
			{4, 4, 4, 4, 0, 0, 5, 5, 0, 4},
			{4, 0, 0, 4, 0, 0, 5, 5, 0, 4},
			{4, 0, 0, 4, 0, 0, 5, 5, 0, 4},
			{4, 0, 0, 4, 0, 0, 5, 5, 0, 4},
			{4, 0, 0, 4, 4, 4, 4, 4, 4, 4},
			{0, 0, 0, 0, 4, 2, 2, 2, 2, 3},
			{4, 4, 4, 0, 1, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3}
		}, new ExtraTile[] {
				new ExtraTile(3, 8, new KeyDoor(KeyDoor.COMMANDER)),
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
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new SnakeTank(8, 2));
			add(new SnakeNuke(8, 0));
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
			{3, 0, 1, 0, 4, 0, 4, 4, 2, 1},
			{3, 3, 1, 0, 0, 0, 1, 2, 4, 1}
		}, new ExtraTile[] {
				new ExtraTile(1, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(1, 0, new Ground()));
						}})),
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
			add(new Cactian(4, 5));
			add(new Cactian(7, 7));
			add(new Cactian(6, 1));
			add(new SandBeep(6, 4));
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
			add(new Cactian(4, 5));
			add(new Cactian(1, 8));
			add(new EliteBunnyWarrior(6, 3, PatrolPattern.WANDER));
			add(new BunnyWarrior(7, 3, PatrolPattern.WANDER));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(7, 3, new SmallHealthPotion()));
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
			{1, 2, 2, 2, 1, 2, 2, 2, 1, 1},
			{1, 1, 2, 2, 2, 2, 2, 2, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBitester(4, 3));
			add(new EliteBitester(5, 6));
			add(new Bitester(6, 2));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 4, Armory.cactusClaws));
			add(new GPickup(4, 7, new SmallHealthPotion()));
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
			add(new Cactian(7, 2));
			add(new Cactian(8, 6));
			add(new SandWurm(4, 3));
			add(new SandBeep(6, 7));
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
			add(new Cactian(3, 3));
			add(new Cactian(4, 8));
			add(new Cactian(8, 4));
			add(new SandWurm(5, 5));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d63 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 3, 3, 3},
			{1, 1, 1, 3, 1, 1, 3, 1, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 3, 1, 3},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 3, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 1, 3, 1, 1, 3, 3, 1, 3},
			{3, 3, 1, 1, 1, 3, 3, 1, 1, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Cactian(8, 3));
			add(new Cactian(8, 8));
			add(new Cactian(6, 2));
			add(new SandBeep(3, 2));
			add(new SandBeep(4, 7));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 4, new SmallHealthPotion()));
		}});
		
		//------------------- // ROW 4
		
		MapLevel d04 = new MapLevel(new int[][] {
			{3, 3, 1, 0, 0, 0, 2, 2, 4, 1},
			{3, 1, 3, 1, 0, 1, 1, 2, 4, 1},
			{3, 1, 1, 0, 1, 1, 1, 4, 1, 1},
			{3, 1, 1, 0, 1, 0, 1, 4, 1, 1},
			{3, 1, 1, 1, 0, 1, 3, 3, 1, 1},
			{3, 3, 4, 4, 0, 1, 1, 3, 1, 1},
			{3, 3, 0, 0, 0, 1, 4, 1, 3, 1},
			{3, 1, 0, 1, 4, 4, 4, 1, 1, 1},
			{3, 0, 1, 2, 4, 2, 1, 1, 3, 1},
			{3, 4, 4, 4, 2, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(1, 7, PatrolPattern.STATIONARY));
			add(new SnakeSoldier(1, 1, PatrolPattern.STATIONARY));
			add(new SandBeep(2, 8));
			add(new Cactian(5, 2));
			add(new Cactian(2, 3));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 8, new GKey(KeyDoor.SNAKEBASE)));
		}});
		
		//-------------------
		
		MapLevel d14 = new MapLevel(new int[][] {
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 4, 1, 1},
			{1, 1, 1, 1, 4, 1, 1, 1, 1, 1},
			{1, 1, 1, 0, 0, 1, 1, 4, 1, 1},
			{1, 1, 1, 4, 0, 1, 0, 4, 1, 1},
			{1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
			{1, 1, 4, 1, 0, 0, 0, 1, 0, 1},
			{1, 1, 1, 4, 1, 1, 4, 4, 1, 1},
			{1, 1, 1, 1, 4, 0, 4, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(6, 5, PatrolPattern.SURFACE_CW));
			add(new EliteBeanpole(5, 2));
			add(new EliteBeanpole(3, 6));
			add(new Cactian(5, 5));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d24 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 4, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 4},
			{1, 1, 1, 1, 4, 4, 4, 4, 4, 4},
			{1, 3, 1, 1, 4, 0, 0, 0, 0, 0},
			{1, 1, 1, 4, 4, 0, 0, 0, 0, 0},
			{1, 1, 4, 4, 4, 4, 4, 0, 0, 4},
			{1, 4, 4, 0, 0, 0, 0, 0, 0, 4},
			{1, 4, 0, 0, 4, 0, 4, 4, 0, 4},
			{1, 4, 0, 0, 4, 0, 0, 0, 0, 4},
			{1, 4, 0, 0, 4, 4, 4, 4, 4, 4}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SnakeCommander(8, 8, PatrolPattern.SURFACE_CW));
			add(new SnakeCommander(8, 7, PatrolPattern.SURFACE_CW));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 8, new MediumHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d34 = new MapLevel(new int[][] {
			{1, 1, 2, 2, 2, 2, 2, 2, 1, 1},
			{4, 1, 1, 2, 2, 2, 2, 1, 1, 4},
			{4, 4, 1, 0, 0, 0, 0, 1, 4, 4},
			{0, 4, 4, 4, 1, 1, 4, 4, 4, 0},
			{0, 0, 0, 4, 1, 1, 4, 0, 0, 0},
			{4, 0, 0, 4, 0, 4, 4, 0, 0, 4},
			{4, 0, 0, 4, 0, 0, 4, 0, 0, 4},
			{4, 0, 0, 4, 0, 0, 4, 0, 0, 4},
			{4, 0, 0, 4, 4, 0, 4, 0, 0, 4},
			{4, 4, 4, 4, 0, 0, 4, 4, 4, 4}
		}, new ExtraTile[] {
				new ExtraTile(4, 5, new KeyDoor(KeyDoor.SNAKEBASE)),
				new ExtraTile(1, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(3, 7, new AltGround()));
						}})),
				new ExtraTile(8, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(6, 7, new AltGround()));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new SaveCrystal(5, 6));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(2, 8, new GKey(KeyDoor.COMMANDER)));
			add(new GPickup(7, 8, new GKey(KeyDoor.COMMANDER)));
		}});
		
		//-------------------
		
		MapLevel d44 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
			{4, 1, 1, 1, 1, 1, 1, 3, 1, 1},
			{4, 4, 4, 4, 4, 4, 1, 1, 3, 1},
			{0, 0, 0, 0, 0, 4, 1, 1, 1, 1},
			{0, 0, 0, 0, 0, 4, 4, 1, 1, 3},
			{4, 4, 4, 4, 0, 0, 4, 4, 1, 1},
			{4, 0, 0, 0, 0, 0, 4, 4, 4, 1},
			{4, 0, 0, 0, 0, 0, 4, 0, 4, 1},
			{4, 0, 0, 0, 0, 0, 4, 0, 4, 1},
			{4, 0, 0, 4, 4, 4, 4, 4, 4, 1}
		}, new ExtraTile[] {
				new ExtraTile(2, 7, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(6, 6, new AltGround()));
							add(new ExtraTile(6, 7, new AltGround()));
							add(new ExtraTile(6, 8, new AltGround()));
							add(new ExtraTile(4, 5, new AltWall()));
							add(new ExtraTile(5, 5, new AltWall()));
							add(new ExtraTile(3, 8, new AltGround()));
							add(new ExtraTile(4, 6, new AltGround()));
						}})),
				new ExtraTile(3, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(6, 6, new AltGround()));
							add(new ExtraTile(6, 7, new AltGround()));
							add(new ExtraTile(6, 8, new AltGround()));
							add(new ExtraTile(4, 5, new AltWall()));
							add(new ExtraTile(5, 5, new AltWall()));
							add(new ExtraTile(2, 7, new AltGround()));
							add(new ExtraTile(4, 6, new AltGround()));
						}})),
				new ExtraTile(4, 6, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(6, 6, new AltGround()));
							add(new ExtraTile(6, 7, new AltGround()));
							add(new ExtraTile(6, 8, new AltGround()));
							add(new ExtraTile(4, 5, new AltWall()));
							add(new ExtraTile(5, 5, new AltWall()));
							add(new ExtraTile(3, 8, new AltGround()));
							add(new ExtraTile(2, 7, new AltGround()));
						}})),
				new ExtraTile(7, 7, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, true, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 5, new AltGround()));
							add(new ExtraTile(5, 5, new AltGround()));
						}})),
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(7, 7, PatrolPattern.STATIONARY));
			add(new SnakeSoldier(7, 8, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(7, 8, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d54 = new MapLevel(new int[][] {
			{1, 3, 1, 1, 3, 1, 1, 1, 3, 3},
			{1, 1, 3, 1, 1, 1, 3, 1, 3, 3},
			{1, 1, 3, 3, 1, 3, 1, 1, 3, 1},
			{1, 1, 1, 3, 1, 1, 1, 3, 3, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 3, 1, 1, 1, 1, 1, 3, 1, 3},
			{1, 1, 1, 3, 1, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{1, 1, 3, 1, 1, 3, 1, 1, 3, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Beanpole(5, 5, PatrolPattern.SURFACE_CW));
			add(new Cactian(4, 7));
			add(new Cactian(6, 2));
			add(new Cactian(5, 4));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d64 = new MapLevel(new int[][] {
			{3, 3, 1, 1, 1, 3, 3, 1, 1, 3},
			{3, 1, 1, 1, 3, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 3, 1, 3, 1, 3},
			{1, 3, 3, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 3, 1, 1, 3},
			{3, 1, 1, 1, 3, 1, 1, 3, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{1, 1, 1, 1, 3, 1, 1, 3, 3, 3},
			{1, 3, 1, 1, 1, 1, 1, 1, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandWurm(2, 2));
			add(new SandWurm(6, 6));
			add(new Cactian(7, 3));
			add(new Cactian(7, 4));
			add(new Cactian(2, 7));
			add(new Cactian(5, 6));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//------------------- // ROW 5
		
		MapLevel d05 = new MapLevel(new int[][] {
			{3, 4, 4, 4, 2, 1, 1, 1, 1, 1},
			{3, 2, 2, 2, 2, 1, 1, 1, 1, 1},
			{3, 2, 2, 2, 1, 1, 3, 1, 1, 1},
			{3, 2, 2, 3, 1, 1, 1, 1, 1, 1},
			{3, 2, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBitester(2, 2, PatrolPattern.SURFACE_CW));
			add(new SandWurm(2, 4));
			add(new EliteBunnyWarrior(4, 8, PatrolPattern.SURFACE_CCW));
			add(new EliteBunnyWarrior(6, 3, PatrolPattern.SURFACE_CCW));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d15 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
			{1, 1, 3, 1, 1, 2, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 2, 2, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
			{1, 1, 1, 1, 1, 2, 2, 1, 1, 4},
			{1, 1, 1, 3, 1, 1, 2, 2, 1, 4},
			{1, 1, 1, 1, 1, 1, 1, 2, 4, 4},
			{3, 1, 1, 1, 1, 1, 2, 2, 2, 4},
			{1, 1, 1, 1, 3, 2, 2, 4, 4, 4}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(4, 7, PatrolPattern.SURFACE_CW));
			add(new Cactian(5, 4));
			add(new Cactian(7, 5));
			add(new BreakableWall(8, 8));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 8, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d25 = new MapLevel(new int[][] {
			{1, 4, 0, 0, 4, 4, 4, 4, 4, 4},
			{1, 4, 4, 0, 0, 0, 0, 0, 0, 4},
			{1, 2, 4, 4, 4, 0, 4, 4, 0, 4},
			{1, 1, 2, 2, 4, 0, 0, 4, 0, 4},
			{1, 1, 3, 2, 4, 0, 0, 4, 0, 4},
			{4, 4, 4, 4, 4, 0, 0, 4, 0, 4},
			{4, 0, 0, 0, 0, 0, 4, 4, 0, 4},
			{4, 0, 4, 0, 4, 4, 4, 0, 0, 4},
			{4, 0, 4, 0, 0, 0, 0, 0, 0, 4},
			{4, 0, 4, 4, 4, 4, 4, 4, 4, 4}
		}, new ExtraTile[] {
				new ExtraTile(3, 6, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 1, new AltWall()));
							add(new ExtraTile(2, 6, new AltWall()));
						}})),
				new ExtraTile(7, 7, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, true, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 1, new AltGround()));
							add(new ExtraTile(2, 6, new AltGround()));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(3, 4));
			add(new SnakeCommander(8, 1, PatrolPattern.PATROL_CW));
			add(new SnakeCommander(7, 8, PatrolPattern.PATROL_CW));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(3, 4, new SmallMaxPotion()));
		}});
		
		//-------------------
		
		MapLevel d35 = new MapLevel(new int[][] {
			{4, 4, 4, 4, 0, 0, 4, 4, 4, 4},
			{4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
			{4, 0, 0, 4, 4, 4, 4, 0, 0, 4},
			{4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
			{4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
			{4, 0, 4, 0, 0, 0, 0, 4, 0, 4},
			{4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
			{4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
			{4, 0, 0, 4, 0, 0, 4, 0, 0, 4},
			{4, 4, 4, 4, 4, 4, 4, 4, 4, 4}
		}, new ExtraTile[] {
				new ExtraTile(2, 1, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(7, 1, new AltGround()));
							add(new ExtraTile(3, 1, new AltWall()));
							add(new ExtraTile(6, 1, new AltWall()));
						}})),
				new ExtraTile(7, 1, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(2, 1, new AltGround()));
							add(new ExtraTile(3, 1, new AltWall()));
							add(new ExtraTile(6, 1, new AltWall()));
						}})),
				new ExtraTile(2, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(3, 1, new AltGround()));
							add(new ExtraTile(2, 5, new AltGround()));
							add(new ExtraTile(8, 9, new AltGround()));
						}})),
				new ExtraTile(7, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(6, 1, new AltGround()));
							add(new ExtraTile(7, 5, new AltGround()));
							add(new ExtraTile(1, 9, new AltGround()));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new SnakeCommander(4, 5, PatrolPattern.STATIONARY));
			add(new SnakeCommander(5, 5, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d45 = new MapLevel(new int[][] { // TODO
			{4, 0, 0, 4, 4, 4, 4, 4, 4, 1},
			{4, 0, 4, 0, 4, 0, 0, 0, 4, 1},
			{4, 0, 0, 0, 0, 0, 0, 0, 4, 1},
			{4, 0, 0, 0, 0, 0, 0, 0, 4, 1},
			{4, 5, 5, 5, 5, 5, 5, 5, 4, 1},
			{4, 0, 0, 0, 0, 0, 0, 0, 4, 4},
			{4, 0, 0, 0, 0, 0, 0, 0, 4, 4},
			{4, 0, 0, 0, 0, 0, 0, 0, 4, 4},
			{4, 0, 4, 4, 4, 4, 4, 4, 4, 4},
			{4, 0, 4, 1, 1, 1, 1, 0, 4, 4}
		}, new ExtraTile[] {
				new ExtraTile(1, 7, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(1, 8, new AltWall()));
						}})),
				new ExtraTile(3, 1, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(7, 8, new AltGround()));
						}})),
				new ExtraTile(7, 6, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(1, 6, 
									new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
									}}, new ArrayList<ExtraTile>() {{
										add(new ExtraTile(1, 8, new AltGround()));
										add(new ExtraTile(7, 4, new AltGround()));
									}})));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(3, 1, 20));
			add(new Cactian(3, 6));
			add(new Cactian(5, 1));
			add(new Cactian(6, 1));
			add(new Cactian(7, 1));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d55 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 2, 1, 1, 2, 2},
			{1, 1, 1, 1, 2, 2, 1, 2, 2, 2},
			{1, 1, 1, 1, 1, 2, 2, 2, 2, 2},
			{1, 1, 1, 1, 1, 1, 2, 2, 2, 2},
			{4, 1, 1, 2, 2, 2, 2, 2, 2, 2},
			{4, 4, 1, 1, 2, 2, 2, 1, 2, 2},
			{4, 4, 0, 1, 1, 2, 2, 1, 1, 2},
			{4, 4, 0, 2, 2, 2, 2, 2, 2, 2},
			{4, 4, 0, 0, 2, 2, 2, 2, 2, 2}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBitester(6, 6));
			add(new EliteBitester(8, 3));
			add(new EliteBitester(7, 8));
			add(new Cactian(5, 4));
			add(new Cactian(7, 7));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d65 = new MapLevel(new int[][] {
			{1, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{2, 1, 2, 1, 2, 1, 1, 1, 3, 3},
			{2, 2, 2, 2, 2, 2, 1, 3, 2, 3},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{2, 2, 2, 2, 2, 2, 2, 1, 1, 3},
			{2, 2, 2, 2, 1, 2, 2, 2, 1, 3},
			{2, 2, 2, 1, 1, 2, 2, 2, 2, 3},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{2, 2, 2, 2, 2, 2, 1, 1, 2, 3},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBitester(5, 8, PatrolPattern.SURFACE_CCW));
			add(new EliteBitester(3, 7, PatrolPattern.SURFACE_CW));
			add(new EliteBitester(2, 3));
			add(new Cactian(8, 4));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(4, 6, new SmallHealthPotion()));
			add(new GPickup(8, 2, new SmallHealthPotion()));
		}});
		
		//------------------- // ROW 6
		
		MapLevel d06 = new MapLevel(new int[][] { // TODO  Entrance to secret area
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 1},
			{3, 3, 3, 1, 3, 1, 1, 1, 1, 3},
			{3, 3, 0, 1, 1, 1, 0, 1, 1, 1},
			{3, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 1, 3, 1, 1, 1, 1, 1, 1},
			{3, 3, 1, 1, 1, 1, 1, 3, 1, 1},
			{3, 3, 0, 0, 0, 1, 3, 1, 1, 3},
			{3, 4, 0, 0, 0, 4, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d16 = new MapLevel(new int[][] {
			{1, 1, 1, 1, 3, 2, 2, 4, 4, 4},
			{3, 1, 1, 1, 2, 2, 4, 4, 4, 4},
			{1, 1, 3, 1, 2, 1, 1, 1, 4, 4},
			{3, 1, 3, 2, 2, 1, 4, 1, 4, 4},
			{1, 1, 1, 3, 2, 1, 1, 1, 1, 4},
			{1, 1, 1, 1, 2, 2, 1, 4, 1, 1},
			{1, 1, 1, 1, 3, 2, 1, 1, 1, 1},
			{1, 1, 1, 1, 2, 3, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandBeep(2, 7));
			add(new SnakeSoldier(7, 3, PatrolPattern.SURFACE_CW));
			add(new SnakeSoldier(8, 5, PatrolPattern.SURFACE_CW));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel d26 = new MapLevel(new int[][] {
			{4, 0, 4, 4, 4, 4, 4, 4, 4, 4},
			{4, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
			{4, 4, 1, 1, 1, 1, 1, 1, 4, 4},
			{4, 1, 1, 1, 1, 1, 1, 1, 1, 4},
			{1, 1, 1, 3, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
			{1, 1, 3, 1, 3, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandBeep(8, 5));
			add(new SnakeCommander(5, 4, PatrolPattern.SURFACE_CW));
			add(new Cactian(6, 8));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 8, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d36 = new MapLevel(new int[][] {
			{4, 0, 4, 4, 4, 4, 4, 4, 0, 4},
			{0, 0, 4, 4, 0, 0, 4, 4, 0, 0},
			{4, 4, 4, 0, 4, 4, 0, 4, 4, 4},
			{4, 4, 1, 1, 1, 1, 1, 2, 4, 4},
			{4, 1, 1, 1, 1, 1, 1, 2, 2, 4},
			{1, 1, 1, 1, 1, 3, 2, 2, 3, 1},
			{1, 1, 1, 1, 1, 2, 3, 3, 1, 3},
			{1, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{3, 1, 1, 1, 3, 1, 1, 1, 3, 1},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(8, 4, 
						new WaterButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 2, new AltGround()));
							add(new ExtraTile(5, 2, new AltGround()));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(4, 1));
			add(new BreakableWall(5, 1));
			add(new SandBeep(7, 7));
			add(new SnakeSoldier(3, 2, PatrolPattern.STATIONARY));
			add(new SnakeSoldier(6, 2, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(4, 1, new SmallHealthPotion()));
			add(new GPickup(5, 1, new SmallMaxPotion()));
		}});
		
		//-------------------
		
		MapLevel d46 = new MapLevel(new int[][] {
			{4, 0, 4, 1, 1, 1, 1, 0, 4, 4},
			{0, 0, 4, 1, 2, 1, 1, 0, 4, 4},
			{4, 4, 4, 1, 2, 2, 2, 0, 4, 4},
			{4, 4, 3, 2, 2, 2, 2, 2, 4, 4},
			{4, 3, 2, 3, 2, 2, 2, 2, 1, 4},
			{1, 1, 3, 2, 2, 2, 2, 2, 2, 4},
			{3, 1, 3, 2, 2, 2, 2, 2, 2, 4},
			{1, 1, 3, 3, 2, 2, 2, 2, 1, 4},
			{1, 3, 3, 2, 2, 2, 2, 2, 3, 4},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 6, Armory.spineShiv));
			add(new GPickup(8, 7, new LargeHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel d56 = new MapLevel(new int[][] {
			{4, 4, 0, 0, 2, 2, 2, 2, 2, 2},
			{4, 4, 4, 0, 2, 2, 2, 2, 2, 2},
			{4, 0, 0, 0, 2, 1, 1, 2, 2, 2},
			{4, 0, 4, 0, 2, 2, 1, 2, 2, 2},
			{4, 0, 0, 0, 2, 2, 2, 2, 2, 1},
			{4, 0, 0, 2, 2, 2, 2, 2, 1, 1},
			{4, 0, 4, 4, 2, 2, 2, 2, 2, 2},
			{4, 0, 0, 4, 4, 2, 2, 2, 2, 2},
			{4, 0, 0, 0, 4, 2, 3, 2, 2, 2},
			{4, 4, 4, 4, 4, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(1, 6, new KeyDoor(KeyDoor.COMMANDER))
		}, new ArrayList<GCharacter>() {{
			add(new SnakeCommander(2, 4, PatrolPattern.SURFACE_CW));
			add(new EliteBitester(5, 3, PatrolPattern.SURFACE_CCW));
			add(new EliteBitester(8, 7));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(3, 8, Armory.ceremonialSpear));
			add(new GPickup(2, 7, new MediumHealthPotion()));
			add(new GPickup(2, 8, new SmallMaxPotion()));
		}});
		
		//-------------------
		
		MapLevel d66 = new MapLevel(new int[][] {
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{2, 2, 2, 1, 3, 2, 2, 2, 2, 3},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{1, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{1, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{2, 2, 2, 2, 2, 2, 2, 1, 3, 3},
			{2, 2, 2, 1, 2, 2, 1, 1, 1, 3},
			{2, 2, 1, 3, 2, 2, 1, 3, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBitester(6, 4, PatrolPattern.SURFACE_CCW));
			add(new EliteBitester(4, 3));
			add(new Bitester(1, 8));
			add(new SandBeep(3, 7));
			add(new Cactian(8, 8));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 7, new GKey(KeyDoor.BLUE)));
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
			{d05, d15, d25, d35, d45, d55, d65},
			{d06, d16, d26, d36, d46, d56, d66}
			//
		});
	
	}
	
}
