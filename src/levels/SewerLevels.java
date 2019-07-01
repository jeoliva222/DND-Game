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
import characters.ChagrinShadow;
import characters.EliteArrowTurret;
import characters.EliteBeanpole;
import characters.EliteBeep;
import characters.EliteBitester;
import characters.EliteBunnyWarrior;
import characters.GCharacter;
import characters.SandBeep;
import characters.SandWurm;
import characters.SaveCrystal;
import characters.Signpost;
import characters.SnakeCommander;
import characters.SnakeSoldier;
import characters.bosses.SnakeNuke;
import characters.bosses.SnakeTank;
import helpers.GPath;
import items.GKey;
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
import tiles.KeyDoor;
import tiles.TriggerType;
import weapons.Armory;

public class SewerLevels implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = 5488803669922680191L;
	
	// Area definition
	public MapArea area_SEWER;

	//************************************************
	// CONSTRUCTOR: LEVEL DEFINITIONS
	
	@SuppressWarnings("serial")
	public SewerLevels() {
	
		//------------------- // ROW 0
		
		MapLevel s00 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Bitester(7, 4, PatrolPattern.SURFACE_CW));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s10 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandBeep(4, 4));
			add(new SandBeep(5, 7));
			add(new SandBeep(7, 2));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s20 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new BunnyWarrior(5, 5, PatrolPattern.PATROL_CW));
			add(new SandBeep(2, 7));
			add(new SandBeep(7, 6));
			add(new BreakableWall(4, 7));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s30 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(6, 3, PatrolPattern.WANDER));
			add(new BunnyWarrior(4, 7, PatrolPattern.WANDER));
			add(new BunnyWarrior(6, 6, PatrolPattern.WANDER));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s40 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
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
		
		MapLevel s50 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(7, 6, PatrolPattern.SURFACE_CW));
			add(new SandBeep(4, 5));
			add(new SandBeep(8, 3));
			add(new SandWurm(5, 1));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//------------------- // ROW 1
		
		MapLevel s01 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
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
		
		MapLevel s11 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
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
		
		MapLevel s21 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Cactian(6, 4));
			add(new Cactian(2, 5));
			add(new SaveCrystal(8, 3));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s31 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(5, 5, PatrolPattern.SURFACE_CCW));
			add(new SnakeSoldier(6, 6, PatrolPattern.SURFACE_CCW));
			add(new ArrowTurret(7, 3, -1, 0, 6));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s41 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SandBeep(3, 3));
			add(new SandWurm(6, 7));
			add(new Bitester(4, 6, PatrolPattern.SURFACE_CCW));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s51 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SnakeCommander(5, 4, PatrolPattern.SURFACE_CW));
			add(new SnakeSoldier(6, 5, PatrolPattern.STATIONARY));
			add(new SnakeSoldier(3, 5, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//------------------- // ROW 2
		
		MapLevel s02 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
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
		
		MapLevel s12 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Cactian(2, 4));
			add(new SandWurm(4, 3));
			add(new SandWurm(7, 4));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s22 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Cactian(3, 2));
			add(new Cactian(6, 5));
			add(new Cactian(2, 7));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s32 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
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
		
		MapLevel s42 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new SnakeSoldier(7, 2, PatrolPattern.WANDER));
			add(new SnakeSoldier(2, 6, PatrolPattern.WANDER));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s52 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
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
		
		//------------------- // ROW 3
		
		MapLevel s03 = new MapLevel(new int[][] {
			{3, 3, 1, 1, 3, 3, 1, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{4, 2, 2, 1, 1, 1, 1, 2, 2, 2},
			{3, 1, 2, 1, 1, 1, 1, 2, 1, 3},
			{3, 1, 2, 1, 3, 3, 1, 2, 1, 3},
			{3, 1, 2, 1, 1, 1, 1, 2, 1, 3},
			{3, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 2, 2, 2, 2, 0, 2, 2, 2, 3},
			{3, 0, 2, 0, 2, 2, 2, 2, 3, 3},
			{3, 3, 3, 3, 3, 3, 0, 3, 0, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new ChagrinShadow(0, 0, 8, 7));
			add(new BreakableWall(9, 2));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s13 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 2, 3, 1, 1, 1, 1, 1, 1},
			{2, 2, 2, 3, 1, 1, 1, 1, 1, 1},
			{3, 2, 2, 3, 1, 1, 1, 1, 1, 3},
			{3, 2, 2, 3, 1, 1, 1, 1, 1, 3},
			{3, 2, 3, 3, 1, 1, 1, 1, 1, 3},
			{3, 2, 3, 3, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s23 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 1, 1, 1, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s33 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
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
		
		MapLevel s43 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Cactian(7, 2));
			add(new Cactian(8, 6));
			add(new SandWurm(4, 3));
			add(new SandBeep(6, 7));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s53 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Cactian(3, 3));
			add(new Cactian(4, 8));
			add(new Cactian(8, 4));
			add(new SandWurm(5, 5));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//------------------- // ROW 4
		
		MapLevel s04 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 0, 4, 0, 3},
			{3, 2, 2, 2, 4, 1, 2, 2, 2, 3},
			{3, 2, 3, 2, 2, 2, 2, 2, 1, 3},
			{3, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 2, 1, 2, 2, 1, 2, 2, 2, 3},
			{3, 2, 2, 2, 3, 3, 2, 1, 4, 3},
			{3, 2, 2, 2, 2, 2, 3, 3, 1, 3},
			{3, 2, 1, 2, 2, 2, 2, 2, 1, 1},
			{3, 2, 2, 2, 1, 2, 2, 1, 1, 1},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new ChagrinShadow(0, 0, 1, 1));
			add(new BreakableWall(2, 5));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 2, new SmallMaxPotion()));
		}});
		
		//-------------------
		
		MapLevel s14 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 2, 2, 2, 3},
			{3, 1, 2, 2, 2, 1, 2, 2, 2, 3},
			{3, 1, 2, 2, 2, 1, 1, 2, 2, 3},
			{3, 1, 2, 4, 2, 1, 1, 1, 1, 1},
			{3, 1, 2, 2, 2, 1, 2, 1, 1, 1},
			{3, 1, 2, 2, 2, 1, 2, 1, 1, 1},
			{1, 1, 2, 4, 2, 2, 2, 2, 2, 3},
			{1, 1, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s24 = new MapLevel(new int[][] { // Starting Screen TODO - Marker
			{3, 3, 3, 1, 1, 1, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{1, 1, 1, 3, 1, 1, 3, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 0, 0, 0, 0, 1, 1, 1},
			{3, 1, 1, 0, 0, 0, 0, 1, 1, 1},
			{3, 1, 1, 0, 0, 0, 0, 1, 1, 3},
			{3, 3, 3, 3, 0, 0, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new EliteBeep(4, 3));
			add(new EliteBeep(5, 2));
			add(new EliteBeep(3, 6));
			add(new EliteBeep(8, 5));
			add(new EliteBeep(5, 6));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 8, new MediumHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel s34 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 3, 3, 3},
			{3, 1, 3, 3, 3, 3, 1, 3, 1, 1},
			{3, 1, 3, 1, 1, 1, 1, 3, 1, 1},
			{3, 1, 3, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{1, 1, 3, 3, 3, 3, 1, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s44 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 1, 3, 1, 1, 3, 1, 3},
			{3, 3, 1, 1, 3, 1, 1, 3, 1, 3},
			{1, 1, 1, 1, 3, 1, 3, 3, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel s54 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new Beanpole(5, 5, PatrolPattern.SURFACE_CW));
			add(new Cactian(4, 7));
			add(new Cactian(6, 2));
			add(new Cactian(5, 4));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		
		//************************************************
		// CONSTRUCTOR: AREA DEFINITION
		
		area_SEWER = new MapArea("Abandoned Tunnels", GPath.TEMPLE, GPath.createSoundPath("d_e2m6.mid"), 30,
				new MapLevel[][] {
			// Level grid definition
			{s00, s10, s20, s30, s40, s50},
			{s01, s11, s21, s31, s41, s51},
			{s02, s12, s22, s32, s42, s52},
			{s03, s13, s23, s33, s43, s53},
			{s04, s14, s24, s34, s44, s54}
			//
		});
	
	}

}
