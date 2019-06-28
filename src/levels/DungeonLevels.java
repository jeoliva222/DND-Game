package levels;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import ai.PatrolPattern;
import characters.ArrowTurret;
import characters.Beanpole;
import characters.Beep;
import characters.Bitester;
import characters.BreakableWall;
import characters.BunnyWarrior;
import characters.EliteArrowTurret;
import characters.EliteBeanpole;
import characters.EliteBeep;
import characters.EliteBitester;
import characters.EliteBunnyWarrior;
import characters.EliteWatchman;
import characters.GCharacter;
import characters.Hoptooth;
import characters.SaveCrystal;
import characters.Watchman;
import characters.bosses.KingBonBon;
import helpers.GPath;
import items.GKey;
import items.GPickup;
import items.LargeHealthPotion;
import items.LargeMaxPotion;
import items.MediumHealthPotion;
import items.MediumMaxPotion;
import items.SmallHealthPotion;
import items.SmallMaxPotion;
import tiles.AltWall;
import tiles.ExtraTile;
import tiles.GButton;
import tiles.Ground;
import tiles.GroundButton;
import tiles.KeyDoor;
import tiles.TriggerType;
import tiles.Wall;
import tiles.Water;
import tiles.WaterButton;
import weapons.Armory;

// Contains definitions of all the Castle Dungeon levels
// as well as the connections between them
public class DungeonLevels implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = -2958593024651302190L;
	
	// Area definition
	public MapArea area_DUNGEON;

	//************************************************
	// CONSTRUCTOR: LEVEL DEFINITIONS
	
	@SuppressWarnings("serial")
	public DungeonLevels() {
	
		MapLevel entryway = new MapLevel(new int[][] {
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
			new ExtraTile(3, 3, 
					new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						add(new BreakableWall(1, 4));
					}}, new ArrayList<ExtraTile>() {{
						add(new ExtraTile(2, 3, new Wall()));
						add(new ExtraTile(1, 4, new Ground()));
					}})),
			new ExtraTile(8, 1, 
					new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
						add(new Dimension(7, 2));
						add(new Dimension(8, 2));
					}})),
			new ExtraTile(5, 7, 
					new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
						add(new Dimension(2, 5));
						add(new Dimension(2, 6));
						add(new Dimension(2, 7));
						add(new Dimension(2, 8));
					}}))
		}, new ArrayList<GCharacter>() {{
			add(new Beanpole(3, 1, PatrolPattern.STATIONARY));
			add(new Beanpole(7, 3, PatrolPattern.STATIONARY));
			add(new Beanpole(8, 3, PatrolPattern.STATIONARY));
			add(new Beanpole(1, 5, PatrolPattern.STATIONARY));
			add(new Beanpole(1, 6, PatrolPattern.STATIONARY));
			add(new Beanpole(1, 7, PatrolPattern.STATIONARY));
			add(new Beanpole(1, 8, PatrolPattern.STATIONARY));
			add(new Bitester(4, 6, PatrolPattern.PATROL));
			add(new Bitester(4, 8, PatrolPattern.PATROL));
			add(new BunnyWarrior(3, 5, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 3, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel start = new MapLevel(new int[][] {
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 3, 3, 1, 1, 1, 3, 3},
			{3, 3, 3, 3, 3, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 4, 3, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 3, 1, 3, 3, 1, 3, 3, 3},
			{3, 1, 1, 1, 3, 1, 1, 1, 3, 3},
			{3, 1, 3, 1, 3, 1, 1, 0, 3, 3},
			{3, 1, 3, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
			new ExtraTile(8, 2, 
					new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
						add(new Dimension(6, 3));
					}}))
		}, new ArrayList<GCharacter>() {{
			add(new Beanpole(8, 4, PatrolPattern.PATROL_CW));
			add(new Beanpole(3, 8, PatrolPattern.STATIONARY));
			add(new BreakableWall(8, 2));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 6, Armory.brokenSword));
			add(new GPickup(5, 7, new SmallHealthPotion()));
		}});
		
		
		//-------------------
		
		MapLevel halls = new MapLevel(new int[][] {
			{3, 3, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 3, 1, 1, 3, 3},
			{3, 1, 3, 3, 1, 3, 1, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 2, 2, 3, 3, 3, 1, 1, 3},
			{3, 1, 2, 2, 1, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 3, 1, 1, 1, 3},
			{3, 1, 3, 3, 1, 3, 3, 3, 1, 3},
			{3, 1, 1, 1, 1, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
			new ExtraTile(8, 7, 
					new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
						add(new Dimension(8, 1));
						add(new Dimension(8, 2));
						add(new Dimension(9, 1));
					}}))
		}, new ArrayList<GCharacter>() {{
			add(new Beanpole(2, 1, PatrolPattern.STATIONARY));
			add(new Beanpole(3, 1, PatrolPattern.STATIONARY));
			add(new BreakableWall(8, 7));
			add(new Beanpole(6, 6, PatrolPattern.PATROL));
			add(new Beanpole(6, 5, PatrolPattern.PATROL));
			add(new BunnyWarrior(6, 1, PatrolPattern.STATIONARY));
			add(new BunnyWarrior(7, 1, PatrolPattern.STATIONARY));
			add(new Bitester(3, 4));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(6, 5, new MediumHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel armory = new MapLevel(new int[][] {
			{3, 3, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 3, 1, 1, 3},
			{3, 1, 1, 1, 3, 1, 1, 1, 1, 3},
			{3, 3, 3, 1, 3, 1, 1, 1, 1, 3},
			{3, 1, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 1, 2, 2, 1, 2, 2, 3, 1},
			{4, 3, 1, 2, 2, 1, 2, 2, 3, 1},
			{3, 3, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
			new ExtraTile(5, 2, 
					new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
						add(new Dimension(5, 1));
						add(new Dimension(4, 1));
					}})),
			new ExtraTile(8, 1, 
					new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
						add(new Dimension(5, 1));
						add(new Dimension(4, 1));
						add(new Dimension(0, 6));
						add(new Dimension(0, 7));
						add(new Dimension(0, 8));
						add(new Dimension(1, 6));
						add(new Dimension(1, 8));
					}})),
			new ExtraTile(5, 6, 
					new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
						add(new Dimension(1, 3));
						add(new Dimension(2, 3));
						add(new Dimension(1, 1));
						add(new Dimension(1, 2));
					}}))
		}, new ArrayList<GCharacter>() {{
			add(new Beanpole(3, 5));
			add(new Beanpole(3, 8));
			add(new SaveCrystal(3, 3));
			add(new BreakableWall(8, 1));
			add(new EliteBeanpole(2, 2, PatrolPattern.PATROL_CCW));
			add(new EliteBeanpole(2, 1, PatrolPattern.PATROL_CCW));
			add(new BunnyWarrior(7, 1, PatrolPattern.STATIONARY));
			add(new Bitester(3, 6, PatrolPattern.PATROL));
			add(new Bitester(7, 7, PatrolPattern.PATROL));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 3, Armory.longStick));
		}});
		
		//-------------------
		
		MapLevel lootCloset = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 2, 3, 3, 3, 3},
			{3, 2, 2, 2, 2, 2, 2, 2, 3, 3},
			{3, 2, 2, 2, 2, 2, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 2, 3, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 1, 1, 1, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(7, 1));
			add(new SaveCrystal(2, 5));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(7, 1, new SmallMaxPotion()));
			add(new GPickup(7, 5, Armory.caestus));
			add(new GPickup(1, 2, Armory.glassShard));
		}});
		
		//-------------------
		
		MapLevel flooded = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 2, 1, 2, 2, 2, 1, 1, 1, 3},
			{3, 1, 1, 1, 2, 2, 1, 2, 2, 3},
			{3, 2, 1, 2, 2, 2, 2, 2, 2, 3},
			{3, 2, 2, 2, 1, 2, 2, 2, 2, 3},
			{3, 2, 2, 1, 1, 2, 2, 2, 2, 1},
			{3, 3, 2, 2, 2, 2, 2, 2, 1, 1},
			{3, 2, 3, 2, 1, 2, 2, 1, 1, 1},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(8, 1, 
						new WaterButton(TriggerType.ENEMY_AND_TILE, true, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(1, 7, new Water()));
							add(new ExtraTile(2, 8, new Water()));
							add(new ExtraTile(9, 6, new Wall()));
							add(new ExtraTile(9, 7, new Wall()));
							add(new ExtraTile(9, 8, new Wall()));
						}})),
				new ExtraTile(1, 8, 
						new WaterButton(TriggerType.SPAWN_ENEMY, GButton.VEILED, new ArrayList<GCharacter>() {{
							add(new EliteBitester(6, 4));
							add(new EliteBitester(8, 3));
							add(new EliteBitester(6, 1));
							add(new EliteBitester(8, 1));
							add(new EliteBitester(1, 4));
						}})),
		}, new ArrayList<GCharacter>() {{
			add(new Beep(1, 3));
			add(new Beep(2, 2));
			add(new Beep(2, 3));
			add(new Beep(8, 2));
			add(new Beep(7, 2));
			add(new Beep(6, 2));
			add(new Beep(4, 8));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 8, new SmallMaxPotion()));
		}});
		
		//-------------------
		
		MapLevel ambush = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 3, 3, 3, 4, 4, 4, 3, 3},
			{3, 1, 3, 3, 3, 1, 1, 1, 3, 3},
			{3, 1, 1, 1, 3, 1, 1, 1, 1, 3},
			{3, 3, 1, 1, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(2, 5, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 4, new Ground()));
							add(new ExtraTile(5, 4, new Ground()));
							add(new ExtraTile(7, 5, new Ground()));
							add(new ExtraTile(1, 6, new Wall()));
							add(new ExtraTile(8, 3, new Wall()));
							add(new ExtraTile(5, 6, new Ground()));
							add(new ExtraTile(6, 6, new Ground()));
							add(new ExtraTile(7, 6, new Ground()));
						}})),
				new ExtraTile(4, 4, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(2, 5, new Ground()));
							add(new ExtraTile(5, 4, new Ground()));
							add(new ExtraTile(7, 5, new Ground()));
							add(new ExtraTile(1, 6, new Wall()));
							add(new ExtraTile(8, 3, new Wall()));
							add(new ExtraTile(5, 6, new Ground()));
							add(new ExtraTile(6, 6, new Ground()));
							add(new ExtraTile(7, 6, new Ground()));
						}})),
				new ExtraTile(5, 4, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 4, new Ground()));
							add(new ExtraTile(2, 5, new Ground()));
							add(new ExtraTile(7, 5, new Ground()));
							add(new ExtraTile(1, 6, new Wall()));
							add(new ExtraTile(8, 3, new Wall()));
							add(new ExtraTile(5, 6, new Ground()));
							add(new ExtraTile(6, 6, new Ground()));
							add(new ExtraTile(7, 6, new Ground()));
						}})),
				new ExtraTile(7, 5, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 4, new Ground()));
							add(new ExtraTile(5, 4, new Ground()));
							add(new ExtraTile(2, 5, new Ground()));
							add(new ExtraTile(1, 6, new Wall()));
							add(new ExtraTile(8, 3, new Wall()));
							add(new ExtraTile(5, 6, new Ground()));
							add(new ExtraTile(6, 6, new Ground()));
							add(new ExtraTile(7, 6, new Ground()));
						}})),
				new ExtraTile(8, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(1, 6, new Ground()));
							add(new ExtraTile(8, 3, new Ground()));
						}})),
				new ExtraTile(8, 2, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
							add(new BreakableWall(8, 3));
						}}, new ArrayList<ExtraTile>() {{
						}})),
		}, new ArrayList<GCharacter>() {{
			add(new BunnyWarrior(6, 8, PatrolPattern.PATROL_CCW));
			add(new Beanpole(5, 8, PatrolPattern.PATROL_CCW));
			add(new Beep(6, 4));
			add(new Beep(6, 5));
			add(new Beep(8, 4));
			add(new Beep(8, 5));
			add(new BreakableWall(8, 8));
			add(new BunnyWarrior(1, 2, PatrolPattern.STATIONARY));
			add(new Beanpole(1, 1, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(7, 8, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel moat = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 2, 2, 1, 1, 3, 1, 1, 1},
			{3, 1, 2, 2, 3, 1, 3, 1, 3, 3},
			{3, 1, 2, 2, 3, 1, 1, 1, 1, 3},
			{3, 1, 2, 2, 3, 3, 3, 3, 3, 3},
			{3, 3, 2, 2, 3, 2, 2, 2, 2, 3},
			{3, 1, 2, 2, 1, 2, 2, 2, 2, 3},
			{3, 2, 2, 2, 0, 2, 2, 2, 1, 3},
			{3, 2, 2, 2, 3, 2, 2, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 1, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(4, 6, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 6, new Water()));
							add(new ExtraTile(4, 7, new Water()));
						}})),
				new ExtraTile(5, 3, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
							add(new EliteBeanpole(8, 3));
						}}, new ArrayList<ExtraTile>() {{
						}})),
				new ExtraTile(3, 1, 
						new WaterButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
							add(new BreakableWall(8, 4));
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(4, 1, new Wall()));
							add(new ExtraTile(8, 4, new Water()));
						}})),
		}, new ArrayList<GCharacter>() {{
			add(new Beanpole(5, 1, PatrolPattern.STATIONARY));
			add(new Beanpole(4, 6, PatrolPattern.STATIONARY));
			add(new Bitester(5, 6, PatrolPattern.PATROL_CW));
			add(new Bitester(7, 7, PatrolPattern.PATROL_CW));
			add(new Bitester(5, 8, PatrolPattern.PATROL_CW));
			add(new Bitester(1, 8));
			add(new Bitester(1, 7));
			add(new Bitester(2, 8));
			add(new BunnyWarrior(5, 5, PatrolPattern.STATIONARY));
			add(new BunnyWarrior(8, 5, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel shrine = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 4, 1, 1, 1, 1, 4, 1, 3},
			{3, 1, 1, 2, 2, 2, 2, 1, 1, 3},
			{3, 1, 1, 2, 1, 1, 2, 1, 1, 3},
			{3, 1, 1, 2, 1, 1, 2, 1, 1, 3},
			{3, 1, 1, 2, 2, 2, 2, 1, 1, 3},
			{3, 1, 4, 1, 1, 1, 1, 4, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(4, 5, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
							add(new BunnyWarrior(2, 2));
							add(new BunnyWarrior(7, 7));
							add(new Beanpole(2, 7));
							add(new Beep(7, 2));
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(7, 0, new AltWall()));
							add(new ExtraTile(2, 1, new AltWall()));
							add(new ExtraTile(1, 2, new AltWall()));
							add(new ExtraTile(2, 2, new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
								
							}}, new ArrayList<ExtraTile>() {{
								add(new ExtraTile(7, 7, new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
								}}, new ArrayList<ExtraTile>() {{
									add(new ExtraTile(0, 7, new Ground()));
									add(new ExtraTile(7, 0, new Ground()));
									add(new ExtraTile(2, 1, new Ground()));
									add(new ExtraTile(1, 2, new Ground()));
								}})));
							}})));
							add(new ExtraTile(2, 7, new Ground()));
							add(new ExtraTile(7, 2, new Ground()));
							add(new ExtraTile(7, 7, new Ground()));
						}})),
		}, new ArrayList<GCharacter>() {{
			add(new Beep(3, 3));
			add(new Beep(3, 6));
			add(new Beep(6, 3));
			add(new Beep(6, 6));
			add(new SaveCrystal(1, 1));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(5, 4, Armory.ironSword));
		}});
		
		//-------------------
		
		MapLevel crossroads = new MapLevel(new int[][] {
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 3, 3, 2, 2, 2, 3},
			{3, 1, 1, 1, 1, 3, 2, 1, 2, 3},
			{3, 1, 1, 1, 3, 3, 2, 2, 2, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 4, 4, 1, 1, 1, 3},
			{3, 1, 1, 1, 4, 4, 1, 1, 1, 1},
			{3, 1, 1, 1, 4, 4, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(8, 7, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(9, 7, new AltWall()));
							add(new ExtraTile(1, 0, new AltWall()));
							add(new ExtraTile(2, 0, new AltWall()));
							add(new ExtraTile(3, 0, new AltWall()));
							add(new ExtraTile(1, 9, new AltWall()));
							add(new ExtraTile(2, 9, new AltWall()));
							add(new ExtraTile(3, 9, new AltWall()));
							add(new ExtraTile(7, 2, new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
							}}, new ArrayList<ExtraTile>() {{
								add(new ExtraTile(9, 7, new Ground()));
								add(new ExtraTile(1, 0, new Ground()));
								add(new ExtraTile(2, 0, new Ground()));
								add(new ExtraTile(3, 0, new Ground()));
								add(new ExtraTile(1, 9, new Ground()));
								add(new ExtraTile(2, 9, new Ground()));
								add(new ExtraTile(3, 9, new Ground()));
							}})));
						}})),
				new ExtraTile(6, 8, 
						new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
							add(new Dimension(5, 8));
							add(new Dimension(5, 7));
							add(new Dimension(5, 6));
							add(new Dimension(4, 8));
							add(new Dimension(4, 7));
							add(new Dimension(4, 6));
						}})),
				new ExtraTile(0, 4, new KeyDoor(KeyDoor.YELLOW))
		}, new ArrayList<GCharacter>() {{
			add(new EliteBunnyWarrior(1, 4, PatrolPattern.STATIONARY));
			add(new EliteBunnyWarrior(1, 5, PatrolPattern.STATIONARY));
			add(new EliteBitester(8, 1, PatrolPattern.PATROL_CW));
			add(new EliteBitester(7, 3, PatrolPattern.PATROL_CW));
			add(new SaveCrystal(4, 2));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(4, 4, new SmallHealthPotion()));
			add(new GPickup(4, 5, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel bottomStart = new MapLevel(new int[][] {
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 4, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(8, 4, 
						new GroundButton(TriggerType.WALL_GROUND, false, new ArrayList<Dimension>() {{
							add(new Dimension(3, 9));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new Watchman(8, 5, new ArrayList<GCharacter>() {{ // New Watchman
				add(new EliteBeanpole(6, 5, PatrolPattern.PATROL_CCW));
				add(new Beanpole(7, 4, PatrolPattern.PATROL_CCW));
				add(new Beanpole(7, 6, PatrolPattern.PATROL_CCW));
			}},
			new ArrayList<ExtraTile>() {{
			}}, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel bottomCorner = new MapLevel(new int[][] {
			{3, 3, 3, 1, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 1, 1, 3, 1, 1, 3},
			{3, 1, 4, 4, 4, 1, 3, 0, 0, 3},
			{3, 1, 1, 1, 1, 1, 3, 1, 1, 1},
			{3, 3, 3, 1, 3, 3, 3, 1, 1, 1},
			{3, 1, 4, 1, 4, 1, 3, 1, 1, 3},
			{3, 1, 4, 1, 4, 1, 3, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 4, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 4, 4, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(1, 5, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(6, 7, new Ground()));
						}})),
				new ExtraTile(7, 1, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
							add(new Dimension(6, 1));
							add(new Dimension(6, 7));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new Watchman(5, 3, new ArrayList<GCharacter>() {{ // New Watchman
				add(new Beanpole(2, 6, PatrolPattern.PATROL));
				add(new Beanpole(4, 6, PatrolPattern.PATROL));
			}},
			new ArrayList<ExtraTile>() {{
				add(new ExtraTile(7, 2, new AltWall()));
				add(new ExtraTile(8, 2, new AltWall()));
				add(new ExtraTile(2, 2, new Ground()));
				add(new ExtraTile(3, 2, new Ground()));
				add(new ExtraTile(4, 2, new Ground()));
				add(new ExtraTile(2, 6, new Ground()));
				add(new ExtraTile(4, 6, new Ground()));
				add(new ExtraTile(2, 5, new Ground()));
				add(new ExtraTile(4, 5, new Ground()));
			}}, PatrolPattern.PATROL_CW));
			add(new EliteWatchman(7, 1, new ArrayList<GCharacter>() {{ // New Elite Watchman
				add(new Beanpole(7, 8));
				add(new Beanpole(8, 8));
			}},
			new ArrayList<ExtraTile>() {{
				add(new ExtraTile(6, 7, new AltWall()));
				add(new ExtraTile(7, 8, new Ground()));
				add(new ExtraTile(8, 8, new Ground()));
			}}, PatrolPattern.STATIONARY));
			add(new EliteBeanpole(5, 5, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(8, 1, new LargeHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel puzzle = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 1},
			{3, 1, 1, 3, 3, 3, 3, 3, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 3, 1, 3, 3, 3, 1, 3, 3},
			{3, 3, 3, 1, 3, 1, 3, 1, 3, 3},
			{3, 3, 3, 1, 3, 1, 3, 1, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(1, 2, 
						new GroundButton(TriggerType.WALL_GROUND, true, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(7, 5));
						}})),
				new ExtraTile(2, 2, 
						new GroundButton(TriggerType.WALL_GROUND, true, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(5, 5));
							add(new Dimension(7, 5));
						}})),
				new ExtraTile(1, 5, 
						new GroundButton(TriggerType.WALL_GROUND, true, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(3, 5));
							add(new Dimension(5, 5));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new Watchman(7, 8, new ArrayList<GCharacter>() {{ // New Watchman
				add(new BreakableWall(8, 3));
			}},
			new ArrayList<ExtraTile>() {{
				add(new ExtraTile(3, 5, new Ground()));
				add(new ExtraTile(5, 5, new Ground()));
				add(new ExtraTile(7, 5, new Ground()));
				add(new ExtraTile(4, 5, new Ground()));
				add(new ExtraTile(4, 6, new Ground()));
				add(new ExtraTile(4, 7, new Ground()));
				add(new ExtraTile(6, 5, new Ground()));
				add(new ExtraTile(6, 6, new Ground()));
				add(new ExtraTile(6, 7, new Ground()));
			}}, PatrolPattern.STATIONARY));
			add(new Watchman(5, 8, new ArrayList<GCharacter>() {{ // New Watchman
				add(new BreakableWall(8, 2));
			}},
			new ArrayList<ExtraTile>() {{
				add(new ExtraTile(3, 5, new Ground()));
				add(new ExtraTile(5, 5, new Ground()));
				add(new ExtraTile(7, 5, new Ground()));
				add(new ExtraTile(4, 5, new Ground()));
				add(new ExtraTile(4, 6, new Ground()));
				add(new ExtraTile(4, 7, new Ground()));
				add(new ExtraTile(6, 5, new Ground()));
				add(new ExtraTile(6, 6, new Ground()));
				add(new ExtraTile(6, 7, new Ground()));
			}}, PatrolPattern.STATIONARY));
			add(new Watchman(3, 8, new ArrayList<GCharacter>() {{ // New Watchman
				add(new BreakableWall(8, 1));
			}},
			new ArrayList<ExtraTile>() {{
				add(new ExtraTile(3, 5, new Ground()));
				add(new ExtraTile(5, 5, new Ground()));
				add(new ExtraTile(7, 5, new Ground()));
				add(new ExtraTile(4, 5, new Ground()));
				add(new ExtraTile(4, 6, new Ground()));
				add(new ExtraTile(4, 7, new Ground()));
				add(new ExtraTile(6, 5, new Ground()));
				add(new ExtraTile(6, 6, new Ground()));
				add(new ExtraTile(6, 7, new Ground()));
			}}, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel bottomCross = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 3, 1, 1, 1, 1, 3, 3, 3},
			{3, 1, 4, 1, 1, 1, 1, 4, 1, 3},
			{3, 1, 4, 1, 1, 1, 1, 4, 1, 3},
			{3, 1, 4, 1, 1, 1, 1, 4, 1, 3},
			{3, 1, 4, 1, 1, 1, 1, 4, 1, 3},
			{3, 1, 4, 1, 1, 1, 1, 4, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(4, 5, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(2, 3));
							add(new Dimension(2, 4));
							add(new Dimension(2, 5));
							add(new Dimension(2, 6));
							add(new Dimension(2, 7));
							add(new Dimension(7, 3));
							add(new Dimension(7, 4));
							add(new Dimension(7, 5));
							add(new Dimension(7, 6));
							add(new Dimension(7, 7));
							add(new Dimension(3, 1));
							add(new Dimension(4, 1));
							add(new Dimension(5, 1));
							add(new Dimension(6, 1));
							add(new Dimension(3, 2));
							add(new Dimension(4, 2));
							add(new Dimension(5, 2));
							add(new Dimension(6, 2));
						}})),
				new ExtraTile(8, 7, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(3, 2));
							add(new Dimension(4, 2));
							add(new Dimension(5, 2));
							add(new Dimension(6, 2));
						}})),
				new ExtraTile(1, 7, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(3, 1));
							add(new Dimension(4, 1));
							add(new Dimension(5, 1));
							add(new Dimension(6, 1));
						}})),
				new ExtraTile(5, 8, new KeyDoor(KeyDoor.GREEN))
		}, new ArrayList<GCharacter>() {{
			add(new Beep(1, 3));
			add(new Beep(1, 4));
			add(new Beep(1, 5));
			add(new Beep(1, 6));
			add(new Beanpole(1, 7));
			add(new Beep(8, 3));
			add(new Beep(8, 4));
			add(new Beep(8, 5));
			add(new Beep(8, 6));
			add(new Beanpole(8, 7));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 3, new MediumHealthPotion()));
			add(new GPickup(8, 3, Armory.ironSpear));
		}});
		
		//-------------------
		
		MapLevel pillarHide = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 3, 3, 3, 3, 1, 3, 1, 3},
			{3, 1, 3, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 0, 1, 1, 1, 1, 3, 1, 3},
			{3, 1, 0, 0, 1, 0, 1, 1, 1, 3},
			{3, 1, 0, 1, 1, 1, 1, 3, 1, 3},
			{3, 1, 3, 0, 3, 3, 1, 1, 1, 3},
			{3, 3, 3, 1, 3, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(5, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(5, 5, new AltWall()));
							add(new ExtraTile(3, 5, new AltWall()));
							add(new ExtraTile(7, 2, new Ground()));
							add(new ExtraTile(7, 4, new Ground()));
						}})),
				new ExtraTile(3, 3, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(2, 6));
							add(new Dimension(2, 5));
							add(new Dimension(2, 4));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new Watchman(1, 3, new ArrayList<GCharacter>() {{
				add(new EliteBeanpole(3, 1, PatrolPattern.PATROL_CCW));
				add(new EliteBeanpole(1, 1, PatrolPattern.PATROL_CCW));
			}},
			new ArrayList<ExtraTile>() {{
				add(new ExtraTile(5, 8, new Ground()));
				add(new ExtraTile(3, 3, new Ground()));
				add(new ExtraTile(3, 7, new AltWall()));
			}}));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(3, 8, new MediumHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel bottomCorner2 = new MapLevel(new int[][] {
			{3, 3, 3, 4, 4, 4, 4, 4, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 3, 3, 1, 3, 3, 1, 3},
			{3, 1, 1, 3, 1, 1, 1, 3, 1, 3},
			{3, 1, 1, 1, 1, 4, 1, 1, 1, 3},
			{3, 1, 1, 3, 1, 1, 1, 3, 1, 3},
			{3, 1, 1, 3, 3, 1, 3, 3, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(1, 1, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
							add(new Dimension(0, 1));
						}})),
				new ExtraTile(8, 8, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(0, 1));
							add(new Dimension(3, 0));
							add(new Dimension(4, 0));
							add(new Dimension(5, 0));
							add(new Dimension(6, 0));
							add(new Dimension(7, 0));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new Watchman(6, 4, new ArrayList<GCharacter>() {{
				add(new EliteBunnyWarrior(5, 5));
			}},
			new ArrayList<ExtraTile>() {{
				add(new ExtraTile(5, 5, new Ground()));
			}}));
			add(new EliteBeanpole(1, 7, PatrolPattern.STATIONARY));
			add(new EliteBeanpole(2, 7, PatrolPattern.STATIONARY));
			add(new Beanpole(1, 8, PatrolPattern.STATIONARY));
			add(new Beanpole(2, 8, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(4, 4, new SmallHealthPotion()));
			add(new GPickup(6, 6, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel bottomFinal = new MapLevel(new int[][] {
			{3, 3, 3, 3, 1, 1, 1, 3, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 4, 4, 1, 4, 4, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(5, 5, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VEILED, new ArrayList<GCharacter>() {{
							add(new EliteWatchman(3, 1));
							add(new EliteWatchman(7, 1));
							add(new EliteBeanpole(3, 8));
							add(new EliteBeanpole(7, 8));
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(3, 9, new Wall()));
							add(new ExtraTile(4, 9, new Wall()));
							add(new ExtraTile(5, 9, new Wall()));
							add(new ExtraTile(6, 9, new Wall()));
							add(new ExtraTile(7, 9, new Wall()));
							add(new ExtraTile(3, 5, new Ground()));
							add(new ExtraTile(4, 5, new Ground()));
							add(new ExtraTile(6, 5, new Ground()));
							add(new ExtraTile(7, 5, new Ground()));
							add(new ExtraTile(4, 0, new Wall()));
							add(new ExtraTile(5, 0, new Wall()));
							add(new ExtraTile(6, 0, new Wall()));
						}})),
				new ExtraTile(1, 2, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(3, 9));
							add(new Dimension(4, 9));
							add(new Dimension(5, 9));
							add(new Dimension(6, 9));
							add(new Dimension(7, 9));
							add(new Dimension(4, 0));
							add(new Dimension(5, 0));
							add(new Dimension(6, 0));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(2, 2));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel kingCorridor = new MapLevel(new int[][] {
			{3, 3, 3, 3, 1, 1, 1, 3, 3, 3},
			{3, 3, 3, 3, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 3, 4, 4, 4, 3, 3, 3},
			{3, 3, 3, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 3, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 3, 3, 4, 4, 4, 3, 3, 3},
			{3, 3, 3, 3, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 3, 1, 1, 1, 3, 3, 3},
			{3, 3, 3, 3, 1, 1, 1, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(7, 1, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(4, 2));
							add(new Dimension(5, 2));
							add(new Dimension(6, 2));
						}})),
				new ExtraTile(7, 7, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(4, 6));
							add(new Dimension(5, 6));
							add(new Dimension(6, 6));
						}})),
				new ExtraTile(2, 4, new KeyDoor(KeyDoor.RED))
		}, new ArrayList<GCharacter>() {{
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel topStart = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 4, 3, 3},
			{3, 3, 1, 3, 1, 3, 3, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 1, 3, 3, 3, 3, 1, 3, 3},
			{3, 1, 1, 1, 3, 3, 3, 1, 1, 3},
			{3, 3, 1, 3, 3, 3, 3, 1, 1, 3},
			{3, 1, 1, 1, 3, 1, 3, 3, 1, 3},
			{3, 3, 1, 3, 3, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 1, 3},
			{3, 1, 1, 1, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(5, 6, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(7, 0));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new EliteBeanpole(5, 7, PatrolPattern.STATIONARY));
			add(new ArrowTurret(2, 1, 0, 1, 6, 4));
			add(new ArrowTurret(8, 2, -1, 0, 7));
			add(new ArrowTurret(8, 8, 0, -1, 5));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 2, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel topCorner = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 1, 1, 1, 3, 1, 1, 1, 4, 1},
			{3, 3, 4, 3, 3, 1, 3, 1, 3, 3},
			{3, 3, 4, 3, 3, 1, 3, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 1, 3, 3, 1, 3, 1, 3, 3},
			{3, 3, 1, 3, 3, 1, 3, 1, 1, 3},
			{3, 1, 1, 3, 3, 3, 3, 1, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 1, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(8, 7, 
						new GroundButton(TriggerType.WALL_GROUND, true, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(7, 6));
						}})),
				new ExtraTile(5, 7, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(4, 2));
						}})),
				new ExtraTile(6, 5, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
							add(new Dimension(2, 3));
							add(new Dimension(2, 4));
						}})),
				new ExtraTile(1, 1, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(8, 2));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new ArrowTurret(1, 5, 1, 0, 2, 1));
			add(new EliteBunnyWarrior(1, 2, PatrolPattern.STATIONARY));
			add(new EliteBunnyWarrior(2, 2, PatrolPattern.STATIONARY));
			add(new BreakableWall(5, 6));
			add(new BreakableWall(2, 6));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 8, new SmallMaxPotion()));
		}});
		
		//-------------------
		
		MapLevel turretWave = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 2, 2, 2, 2, 2, 2, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(2, 6, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(0, 2));
							add(new Dimension(9, 2));
						}})),
				new ExtraTile(7, 6, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(1, 2));
							add(new Dimension(8, 2));
						}})),
				new ExtraTile(2, 2, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
							add(new Dimension(0, 2));
							add(new Dimension(9, 2));
							add(new Dimension(1, 2));
							add(new Dimension(8, 2));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new ArrowTurret(2, 8, 0, -1, 5, 3));
			add(new ArrowTurret(3, 8, 0, -1, 5, 2));
			add(new ArrowTurret(4, 8, 0, -1, 5, 0));
			add(new ArrowTurret(5, 8, 0, -1, 5, 0));
			add(new ArrowTurret(6, 8, 0, -1, 5, 2));
			add(new ArrowTurret(7, 8, 0, -1, 5, 3));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel topCross = new MapLevel(new int[][] {
			{3, 3, 3, 3, 1, 1, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 1, 3, 3, 3, 3},
			{1, 1, 1, 3, 1, 1, 1, 1, 1, 1},
			{3, 2, 2, 3, 1, 1, 1, 1, 3, 3},
			{3, 2, 2, 3, 1, 1, 1, 2, 3, 3},
			{3, 3, 2, 3, 3, 3, 1, 2, 3, 3},
			{3, 3, 2, 3, 3, 3, 2, 2, 3, 3},
			{3, 3, 2, 2, 2, 2, 2, 2, 3, 3},
			{3, 3, 2, 2, 2, 2, 2, 2, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
			new ExtraTile(5, 1, new KeyDoor(KeyDoor.BLUE))
		}, new ArrayList<GCharacter>() {{
			add(new EliteBeep(5, 2));
			add(new EliteBeep(4, 2));
			add(new EliteBeep(5, 3));
			add(new EliteBeep(4, 3));
			add(new EliteBeep(7, 7));
			add(new EliteBeep(7, 8));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(4, 4, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel topFlooded = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 2, 2, 2, 2, 3, 3, 3, 3, 3},
			{1, 1, 2, 2, 2, 3, 2, 2, 2, 3},
			{3, 3, 2, 2, 3, 3, 2, 2, 2, 3},
			{3, 3, 2, 2, 2, 3, 2, 2, 2, 3},
			{3, 3, 2, 2, 2, 3, 4, 4, 4, 3},
			{3, 3, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 4, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 4, 1, 1, 1, 2, 1, 1, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(3, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
							add(new ArrowTurret(1, 7, 1, 0, 6, 4));
							add(new ArrowTurret(1, 8, 1, 0, 6, 4));
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(1, 7, new Ground()));
							add(new ExtraTile(1, 8, new Ground()));
							add(new ExtraTile(2, 5, new Wall()));
							add(new ExtraTile(3, 5, new Wall()));
							add(new ExtraTile(4, 5, new Wall()));
							add(new ExtraTile(6, 5, new Water()));
							add(new ExtraTile(7, 5, new Water()));
							add(new ExtraTile(8, 5, new Water()));
						}})),
				new ExtraTile(7, 2, 
						new WaterButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
							add(new BreakableWall(5, 2));
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(2, 6, new Wall()));
							add(new ExtraTile(2, 7, new Wall()));
							add(new ExtraTile(2, 8, new Wall()));
							add(new ExtraTile(9, 7, new Ground()));
							add(new ExtraTile(9, 8, new Ground()));
							add(new ExtraTile(5, 2, new Water()));
							add(new ExtraTile(4, 4, new WaterButton(TriggerType.WALL_WATER, false, GButton.VEILED, new ArrayList<Dimension>() {{
								add(new Dimension(5, 9));
							}})));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new EliteBitester(7, 4, PatrolPattern.PATROL_CW));
			add(new EliteBitester(8, 4, PatrolPattern.PATROL_CW));
			add(new EliteBitester(8, 3, PatrolPattern.PATROL_CW));
			add(new EliteBitester(8, 2, PatrolPattern.PATROL_CW));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(4, 8, new SmallHealthPotion()));
		}});
		
		//-------------------
		
		MapLevel topCorner2 = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 3, 2, 1, 2, 2, 2, 2, 2, 3},
			{3, 3, 2, 2, 2, 1, 2, 2, 2, 3},
			{3, 3, 2, 2, 2, 2, 2, 1, 2, 3},
			{3, 3, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 3, 2, 2, 2, 2, 2, 2, 2, 3},
			{1, 1, 2, 2, 2, 2, 3, 3, 3, 3},
			{1, 1, 2, 2, 2, 2, 4, 4, 4, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 2, 3}
		}, new ExtraTile[] {
				new ExtraTile(2, 1, 
						new WaterButton(TriggerType.WALL_WATER, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(6, 8));
						}})),
				new ExtraTile(4, 4, 
						new WaterButton(TriggerType.WALL_WATER, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(7, 8));
						}})),
				new ExtraTile(8, 3, 
						new WaterButton(TriggerType.WALL_WATER, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(8, 8));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new EliteArrowTurret(3, 2, 1, 0, 6, 4, true));
			add(new EliteArrowTurret(5, 3, 1, 0, 6, 2, true));
			add(new EliteArrowTurret(7, 4, 1, 0, 6, 0, true));
		}}, new ArrayList<GPickup>() {{
			
		}});
		
		//-------------------
		
		MapLevel topFinal = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 2, 3},
			{3, 1, 1, 3, 1, 3, 1, 3, 2, 3},
			{3, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 2, 2, 2, 2, 2, 2, 2, 2, 3},
			{3, 2, 2, 2, 2, 2, 2, 3, 4, 3},
			{3, 2, 2, 2, 2, 2, 2, 3, 4, 3},
			{3, 2, 3, 1, 3, 1, 3, 3, 4, 3},
			{3, 2, 3, 3, 3, 3, 3, 3, 4, 3},
			{3, 2, 2, 2, 2, 2, 2, 1, 4, 3},
			{3, 3, 3, 3, 1, 1, 1, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(7, 8, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(8, 8));
							add(new Dimension(8, 7));
							add(new Dimension(8, 6));
							add(new Dimension(8, 5));
							add(new Dimension(8, 4));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new ArrowTurret(6, 1, 0, 1, 2, 0));
			add(new ArrowTurret(4, 1, 0, 1, 2, 0));
			add(new ArrowTurret(2, 1, 0, 1, 2, 0));
			add(new ArrowTurret(5, 6, 0, -1, 2, 1));
			add(new ArrowTurret(3, 6, 0, -1, 2, 1));
			add(new ArrowTurret(1, 1, 0, 1, 3, 0));
		}}, new ArrayList<GPickup>() {{
			
		}});
		
		//-------------------
		
		MapLevel finalHall = new MapLevel(new int[][] {
			{3, 3, 3, 3, 1, 0, 3, 3, 3, 3},
			{3, 3, 3, 3, 0, 1, 3, 3, 3, 3},
			{3, 3, 3, 3, 4, 4, 3, 3, 3, 3},
			{1, 1, 1, 1, 1, 0, 1, 1, 3, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{1, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 3, 0, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 1, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 1},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3}
		}, new ExtraTile[] {
				new ExtraTile(4, 6, new GroundButton(TriggerType.SOUND, false, GPath.createSoundPath("king_warning.wav"))),
				new ExtraTile(4, 7, 
						new GroundButton(TriggerType.WALL_GROUND, false, GButton.VISIBLE, new ArrayList<Dimension>() {{
							add(new Dimension(4, 2));
							add(new Dimension(5, 2));
						}}))
		}, new ArrayList<GCharacter>() {{
			add(new SaveCrystal(8, 4));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(3, 5, new GKey(KeyDoor.RED)));
			add(new GPickup(7, 3, new SmallHealthPotion()));
			add(new GPickup(7, 5, new MediumHealthPotion()));
		}});
		
		//-------------------
		
		// ***Boss Level***
		MapLevel throneRoom = new MapLevel(new int[][] {
			{3, 3, 3, 3, 1, 1, 3, 3, 3, 3},
			{3, 3, 3, 3, 1, 1, 3, 3, 3, 3},
			{3, 1, 0, 1, 1, 1, 0, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 0, 1, 1, 0, 1, 1, 1, 0, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 3},
			{3, 1, 1, 1, 1, 1, 0, 1, 1, 3},
			{3, 1, 0, 1, 1, 1, 1, 1, 1, 3},
			{3, 3, 3, 3, 0, 1, 3, 3, 3, 3},
			{3, 3, 3, 3, 1, 0, 3, 3, 3, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new KingBonBon(4, 3));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel treasureRoom = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 3, 1},
			{3, 1, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 1, 1, 1, 1, 1, 1, 3, 3},
			{3, 3, 3, 3, 1, 1, 3, 3, 3, 3},
			{3, 3, 3, 3, 1, 1, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(8, 4, new KeyDoor(KeyDoor.RED))
		}, new ArrayList<GCharacter>() {{
			add(new SaveCrystal(1, 3));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(1, 5, Armory.kingStaff));
			add(new GPickup(1, 6, new MediumHealthPotion()));
			add(new GPickup(4, 2, new GKey(KeyDoor.RED)));
			add(new GPickup(5, 2, new MediumMaxPotion()));
		}});
		
		//-------------------
		
		MapLevel secretTunnel = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 0, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 0, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 0, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 0, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 0, 3}
		}, new ExtraTile[] {
		}, new ArrayList<GCharacter>() {{
			add(new BreakableWall(8, 4));
		}}, new ArrayList<GPickup>() {{
		}});
		
		//-------------------
		
		MapLevel secretRoom = new MapLevel(new int[][] {
			{3, 3, 3, 3, 3, 3, 3, 3, 1, 3},
			{3, 1, 0, 0, 3, 1, 1, 3, 0, 3},
			{3, 3, 0, 3, 3, 1, 1, 1, 1, 3},
			{3, 3, 0, 3, 3, 3, 3, 3, 0, 3},
			{3, 3, 1, 3, 3, 3, 3, 3, 1, 3},
			{3, 0, 1, 1, 1, 0, 4, 0, 0, 3},
			{3, 0, 1, 0, 1, 0, 4, 0, 3, 3},
			{3, 1, 0, 1, 1, 1, 4, 1, 3, 3},
			{3, 0, 1, 1, 0, 1, 4, 1, 3, 3},
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
		}, new ExtraTile[] {
				new ExtraTile(7, 8, 
						new GroundButton(TriggerType.ENEMY_AND_TILE, false, GButton.VISIBLE, new ArrayList<GCharacter>() {{
						}}, new ArrayList<ExtraTile>() {{
							add(new ExtraTile(6, 5, new Ground()));
							add(new ExtraTile(6, 6, new Ground()));
							add(new ExtraTile(6, 7, new Ground()));
							add(new ExtraTile(6, 8, new Ground()));
							add(new ExtraTile(8, 4, new AltWall()));
							add(new ExtraTile(8, 5, new AltWall()));
						}})),
				new ExtraTile(1, 1, 
						new GroundButton(TriggerType.WALL_GROUND, true, new ArrayList<Dimension>() {{
							add(new Dimension(8, 4));
							add(new Dimension(8, 5));
						}})),
		}, new ArrayList<GCharacter>() {{
			add(new SaveCrystal(5, 1));
			add(new Hoptooth(1, 6, PatrolPattern.STATIONARY));
			add(new Hoptooth(1, 7, PatrolPattern.STATIONARY));
		}}, new ArrayList<GPickup>() {{
			add(new GPickup(3, 1, new LargeMaxPotion()));
		}});
		
		
		//************************************************
		// CONSTRUCTOR: AREA DEFINITION
		
		area_DUNGEON = new MapArea("Castle Dungeon", GPath.DUNGEON, GPath.createSoundPath("d_e2m6.mid"), 40,
				new MapLevel[][] {
			// Level grid definition
			{topCorner	 , 	turretWave,    topCross, 	topFlooded, 	topCorner2, 	treasureRoom},
			{topStart 	 , 		  moat, 	 ambush, 	lootCloset, 	  topFinal, 	  throneRoom},
			{crossroads	 , 		shrine, 	  halls,         start,	  kingCorridor, 	   finalHall},
			{bottomStart , 	   flooded, 	 armory,  	  entryway,    bottomFinal, 	secretTunnel},
			{bottomCorner, 		puzzle, bottomCross, 	pillarHide,  bottomCorner2, 	  secretRoom}
			//
		});
	
	}
	
}
