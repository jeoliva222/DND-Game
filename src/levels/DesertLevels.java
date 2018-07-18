package levels;

import java.awt.Dimension;
import java.util.ArrayList;

import ai.PatrolPattern;
import characters.Bitester;
import characters.BunnyWarrior;
import characters.EliteArrowTurret;
import characters.EliteBunnyWarrior;
import characters.GCharacter;
import characters.SandBeep;
import characters.SandWurm;
import characters.SnakeSoldier;
import helpers.GPath;
import items.GPickup;
import items.MediumHealthPotion;
import items.SmallHealthPotion;
import tiles.ExtraTile;
import tiles.GButton;
import tiles.GroundButton;
import tiles.TriggerType;

// Contains definitions of all the Poacher's Desert levels
// as well as the connections between them
@SuppressWarnings("serial")
public class DesertLevels {

	//-------------------
	
	public static MapLevel d00 = new MapLevel(new int[][] {
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
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d10 = new MapLevel(new int[][] {
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
	
	public static MapLevel d20 = new MapLevel(new int[][] {
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
	
	public static MapLevel d30 = new MapLevel(new int[][] {
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
	
	public static MapLevel d40 = new MapLevel(new int[][] {
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
	
	public static MapLevel d50 = new MapLevel(new int[][] {
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
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d60 = new MapLevel(new int[][] {
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
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d01 = new MapLevel(new int[][] {
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
	
	public static MapLevel d11 = new MapLevel(new int[][] {
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
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d21 = new MapLevel(new int[][] {
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
	
	public static MapLevel d31 = new MapLevel(new int[][] {
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
	
	public static MapLevel d41 = new MapLevel(new int[][] {
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
	
	public static MapLevel d51 = new MapLevel(new int[][] {
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
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d61 = new MapLevel(new int[][] {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
		{4, 4, 4, 4, 1, 1, 4, 4, 4, 4},
		{0, 0, 0, 4, 1, 1, 4, 0, 0, 4},
		{4, 4, 0, 4, 4, 4, 4, 4, 0, 4},
		{4, 0, 0, 4, 1, 1, 4, 0, 0, 4},
		{4, 0, 0, 4, 1, 1, 4, 0, 0, 4},
		{4, 4, 0, 4, 1, 1, 4, 0, 0, 4},
		{1, 4, 4, 4, 1, 1, 4, 0, 0, 4},
		{1, 1, 4, 4, 1, 1, 0, 0, 0, 4},
		{1, 1, 1, 1, 1, 1, 4, 0, 0, 4}
	}, new ExtraTile[] {
	}, new ArrayList<GCharacter>() {{
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d02 = new MapLevel(new int[][] {
		{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{3, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{3, 3, 4, 4, 4, 4, 4, 4, 4, 1},
		{3, 3, 4, 4, 1, 3, 0, 0, 4, 1},
		{3, 2, 2, 0, 0, 0, 4, 4, 4, 1},
		{3, 4, 4, 0, 0, 4, 0, 1, 4, 1},
		{3, 4, 0, 4, 0, 0, 1, 2, 4, 1},
		{3, 4, 4, 4, 4, 4, 0, 4, 2, 1},
		{3, 1, 4, 4, 0, 0, 0, 4, 4, 1},
		{3, 4, 4, 0, 0, 0, 0, 1, 4, 1}
	}, new ExtraTile[] {
			new ExtraTile(3, 5, 
					new GroundButton(TriggerType.WALL_GROUND, false, GButton.VEILED, new ArrayList<Dimension>() {{
						add(new Dimension(6, 4));
					}}))
	}, new ArrayList<GCharacter>() {{
		add(new SnakeSoldier(7, 3, PatrolPattern.STATIONARY));
		add(new SandBeep(2, 6));
		add(new Bitester(1, 4));
	}}, new ArrayList<GPickup>() {{
		add(new GPickup(3, 5, new MediumHealthPotion()));
	}});
	
	//-------------------
	
	public static MapLevel d12 = new MapLevel(new int[][] {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 1, 1, 1, 1, 1, 3, 1, 1, 1},
		{1, 1, 3, 1, 1, 1, 1, 1, 1, 1},
		{1, 1, 1, 3, 1, 1, 1, 3, 1, 1},
		{1, 1, 1, 3, 1, 1, 3, 1, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 3, 1, 1},
		{1, 1, 1, 1, 1, 3, 1, 3, 1, 1},
		{1, 3, 1, 1, 3, 1, 1, 1, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	}, new ExtraTile[] {
	}, new ArrayList<GCharacter>() {{
		add(new SandWurm(4, 3));
		add(new SandWurm(7, 4));
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d22 = new MapLevel(new int[][] {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 1, 3, 1, 1, 3, 1, 3, 1, 1},
		{1, 3, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 1, 1, 3, 1, 3, 1, 3, 1, 1},
		{1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
		{1, 1, 1, 3, 1, 1, 1, 1, 3, 1},
		{1, 1, 3, 1, 1, 1, 3, 1, 1, 1},
		{1, 1, 1, 3, 1, 3, 1, 1, 3, 1},
		{1, 3, 1, 1, 1, 3, 1, 3, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	}, new ExtraTile[] {
	}, new ArrayList<GCharacter>() {{
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d32 = new MapLevel(new int[][] {
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
	
	public static MapLevel d42 = new MapLevel(new int[][] {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 1, 1, 3, 1, 1, 1, 1, 2, 1},
		{1, 1, 1, 2, 2, 1, 3, 1, 2, 1},
		{1, 1, 1, 2, 2, 3, 2, 2, 1, 1},
		{1, 1, 2, 2, 2, 2, 2, 2, 1, 1},
		{1, 3, 2, 2, 2, 2, 2, 2, 1, 1},
		{1, 1, 1, 2, 2, 2, 2, 3, 1, 1},
		{1, 1, 1, 2, 2, 2, 2, 3, 1, 1},
		{1, 1, 1, 3, 2, 2, 1, 1, 3, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	}, new ExtraTile[] {
	}, new ArrayList<GCharacter>() {{
		add(new SnakeSoldier(7, 2, PatrolPattern.WANDER));
		add(new SnakeSoldier(2, 6, PatrolPattern.WANDER));
		add(new SnakeSoldier(5, 5, PatrolPattern.WANDER));
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d52 = new MapLevel(new int[][] {
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
	}}, new ArrayList<GPickup>() {{
	}});
	
	//-------------------
	
	public static MapLevel d62 = new MapLevel(new int[][] {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
		{4, 4, 4, 4, 1, 1, 4, 4, 4, 4},
		{0, 0, 0, 4, 1, 1, 4, 0, 0, 4},
		{4, 4, 0, 4, 4, 4, 4, 4, 0, 4},
		{4, 0, 0, 4, 1, 1, 4, 0, 0, 4},
		{4, 0, 0, 4, 1, 1, 4, 0, 0, 4},
		{4, 4, 0, 4, 1, 1, 4, 0, 0, 4},
		{1, 4, 4, 4, 1, 1, 4, 0, 0, 4},
		{1, 1, 4, 4, 1, 1, 0, 0, 0, 4},
		{1, 1, 1, 1, 1, 1, 4, 0, 0, 4}
	}, new ExtraTile[] {
	}, new ArrayList<GCharacter>() {{
	}}, new ArrayList<GPickup>() {{
	}});
	
	
	//************************************************
	// AREA DEFINITION
	
	public static MapArea area_DESERT = new MapArea("Poacher's Desert", GPath.DESERT, GPath.createSoundPath("d_e2m1.mid"),
			new MapLevel[][] {
		// Level grid definition
		{DesertLevels.d00, DesertLevels.d10, DesertLevels.d20, DesertLevels.d30, DesertLevels.d40, DesertLevels.d50, DesertLevels.d60},
		{DesertLevels.d01, DesertLevels.d11, DesertLevels.d21, DesertLevels.d31, DesertLevels.d41, DesertLevels.d51, DesertLevels.d61},
		{DesertLevels.d02, DesertLevels.d12, DesertLevels.d22, DesertLevels.d32, DesertLevels.d42, DesertLevels.d52, DesertLevels.d61}
		//
	});
	
}
