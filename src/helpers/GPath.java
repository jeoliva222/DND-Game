package helpers;

import java.io.File;

// "Static" class that contains file directory
// paths to game images/objects
//
public class GPath {
	
	//--------- HELPER METHODS
	
	// Returns a valid image file path based off of connector string inputs
	public static String createImagePath(String ... strings) {
		String output = GPath.IMAGE;
		
	    for (String s : strings)
	         output = output + s;
	    
		return output;
	}
	
	// Returns a valid sound file path based off of connector string inputs
	public static String createSoundPath(String ... strings) {
		String output = GPath.SOUND;
		
	    for (String s : strings)
	         output = output + s;
	    
		return output;
	}
	
	
	//--------- COMMON
	
	public static String IMAGE = "res" + File.separator + "images" + File.separator;
	
	public static String SOUND = "res" + File.separator + "sounds" + File.separator;
	
	public static String SAVE = "save" + File.separator;
	
	public static String NULL = GPath.IMAGE + "null.png";
	
	public static String BORDER = GPath.IMAGE + "template.png";
	
	//--------- BASE FOLDERS

	public static String ENEMY = "enemies" + File.separator;
	
	public static String TILE = "tiles" + File.separator;
	
	public static String ALLY = "allies" + File.separator;
	
	public static String PICKUP = "pickups" + File.separator;
	
	//--------- PICKUP TYPES
	
	public static String WEAPON = "weapons" + File.separator;
	
	public static String CONSUME = "consumables" + File.separator;
	
	public static String KEY = "keys" + File.separator;
	
	//--------- TILE TYPES / LEVEL REGIONS
	
	public static String GENERIC = "generic" + File.separator;
	
	public static String DOOR = "door" + File.separator;
	
	public static String DUNGEON = "dungeon" + File.separator;
	
	public static String DESERT = "desert" + File.separator;
	
	public static String JUNGLE = "jungle" + File.separator;
	
	public static String MUSEUM = "museum" + File.separator;
	
	public static String TEMPLE = "temple" + File.separator;
	
	//--------- CHARACTER NAMES
	
	public static String PLAYER = "player" + File.separator;
	
	public static String ARROW_TURRET = "arrow_turret" + File.separator;
	
	public static String BEANPOLE = "beanpole" + File.separator;
	
	public static String ELITE_BEANPOLE = "elite_beanpole" + File.separator;
	
	public static String BITESTER = "bitester" + File.separator;
	
	public static String ELITE_BITESTER = "elite_bitester" + File.separator;
	
	public static String BWARRIOR = "b_warrior" + File.separator;
	
	public static String ELITE_BWARRIOR = "elite_b_warrior" + File.separator;
	
	public static String BREAKABLE_WALL = "breakable_wall" + File.separator;
	
	public static String BEEP = "beep" + File.separator;
	
	public static String ELITE_BEEP = "elite_beep" + File.separator;
	
	public static String CACTIAN = "cactian" + File.separator;
	
	public static String GAZER = "its_gaze" + File.separator;
	
	public static String HOPTOOTH = "hoptooth" + File.separator;
	
	public static String KINGBONBON = "kingbonbon" + File.separator;
	
	public static String KINGS_HEAD = "kings_head" + File.separator;
	
	public static String SAND_BEEP = "sand_beep" + File.separator;
	
	public static String ELITE_SAND_BEEP = "elite_sand_beep" + File.separator;
	
	public static String SANDWURM = "sandwurm" + File.separator;
	
	public static String ELITE_SANDWURM = "elite_sandwurm" + File.separator;
	
	public static String SAVE_CRYSTAL = "save_crystal" + File.separator;
	
	public static String SNAKE_COMMANDER = "snake_commander" + File.separator;
	
	public static String SNAKE_SOLDIER = "snake_soldier" + File.separator;
	
	public static String SNAKETANK = "snaketank" + File.separator;
	
	public static String WATCHMAN = "watchman" + File.separator;
	
	public static String ELITE_WATCHMAN = "elite_watchman" + File.separator;
	
}
