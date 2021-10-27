package helpers;

import java.io.File;

/**
 * Class that contains file directory definitions
 * for paths to game images/objects
 * @author jeoliva
 */
public class GPath {
	
	//--------- HELPER METHODS
	
	/**
	 * Returns a valid image file path based off of connector string inputs
	 * @param strings String inputs to build file path
	 * @return Fully concatenated image file path String
	 */
	public static String createImagePath(String ... strings) {
		String output = GPath.IMAGE;
		
	    for (String s : strings) {
	    	output = output + s;
	    }    
	    
		return output;
	}
	
	/**
	 * Returns a valid sound file path based off of connector string inputs
	 * @param strings String inputs to build file path
	 * @return Fully concatenated sound file path String
	 */
	public static String createSoundPath(String ... strings) {
		String output = GPath.SOUND;
		
	    for (String s : strings) {
	    	output = output + s;
	    }    
	    
		return output;
	}
	
	
	//--------- COMMON
	
	public static final String IMAGE = "res" + File.separator + "images" + File.separator;
	
	public static final String SOUND = "res" + File.separator + "sounds" + File.separator;
	
	public static final String SAVE = "save" + File.separator;
	
	public static final String TEMP = "temp" + File.separator;
	
	public static final String NULL = GPath.IMAGE + "null.png";
	
	public static final String BORDER = GPath.IMAGE + "template.png";
	
	//--------- BASE FOLDERS

	public static final String ENEMY = "enemies" + File.separator;
	
	public static final String TILE = "tiles" + File.separator;
	
	public static final String ALLY = "allies" + File.separator;
	
	public static final String PICKUP = "pickups" + File.separator;
	
	//--------- PICKUP TYPES
	
	public static final String WEAPON = "weapons" + File.separator;
	
	public static final String CONSUME = "consumables" + File.separator;
	
	public static final String KEY = "keys" + File.separator;
	
	//--------- TILE TYPES / LEVEL REGIONS
	
	public static final String GENERIC = "generic" + File.separator;
	
	public static final String DOOR = "door" + File.separator;
	
	public static final String DUNGEON = "dungeon" + File.separator;
	
	public static final String DESERT = "desert" + File.separator;
	
	public static final String JUNGLE = "jungle" + File.separator;
	
	public static final String MUSEUM = "museum" + File.separator;
	
	public static final String TEMPLE = "temple" + File.separator;
	
	public static final String SPECIAL = "special" + File.separator;
	
	//--------- CHARACTER NAMES
	
	public static final String PLAYER = "player" + File.separator;
	
	public static final String ARROW_TURRET = "arrow_turret" + File.separator;
	
	public static final String BEANPOLE = "beanpole" + File.separator;
	
	public static final String ELITE_BEANPOLE = "elite_beanpole" + File.separator;
	
	public static final String BITESTER = "bitester" + File.separator;
	
	public static final String ELITE_BITESTER = "elite_bitester" + File.separator;
	
	public static final String BWARRIOR = "b_warrior" + File.separator;
	
	public static final String ELITE_BWARRIOR = "elite_b_warrior" + File.separator;
	
	public static final String BREAKABLE_WALL = "breakable_wall" + File.separator;
	
	public static final String BEEP = "beep" + File.separator;
	
	public static final String ELITE_BEEP = "elite_beep" + File.separator;
	
	public static final String CACTIAN = "cactian" + File.separator;
	
	public static final String GAZER = "its_gaze" + File.separator;
	
	public static final String HOPTOOTH = "hoptooth" + File.separator;
	
	public static final String KINGBONBON = "kingbonbon" + File.separator;
	
	public static final String KINGS_HEAD = "kings_head" + File.separator;
	
	public static final String SAND_BEEP = "sand_beep" + File.separator;
	
	public static final String ELITE_SAND_BEEP = "elite_sand_beep" + File.separator;
	
	public static final String SANDWURM = "sandwurm" + File.separator;
	
	public static final String ELITE_SANDWURM = "elite_sandwurm" + File.separator;
	
	public static final String SAVE_CRYSTAL = "save_crystal" + File.separator;
	
	public static final String SIGNPOST = "signpost" + File.separator;
	
	public static final String SILKFISH = "silkfish" + File.separator;
	
	public static final String SNAKE_COMMANDER = "snake_commander" + File.separator;
	
	public static final String SNAKE_GENERAL = "snake_general" + File.separator;
	
	public static final String SNAKE_SOLDIER = "snake_soldier" + File.separator;
	
	public static final String SNAKETANK = "snaketank" + File.separator;
	
	public static final String TORMENTED_SOUL = "tormented_soul" + File.separator;
	
	public static final String WATCHMAN = "watchman" + File.separator;
	
	public static final String ELITE_WATCHMAN = "elite_watchman" + File.separator;
	
	//--------- SPECIAL
	
	public static final String EYE_PATH = "save/corruption.mad";
	
	//--------- TESTING
	
	public static final String PLACEHOLDER = GPath.IMAGE + GPath.TILE + GPath.GENERIC + "testProj.png";
	
}
