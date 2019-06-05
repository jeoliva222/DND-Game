package levels;

import java.io.Serializable;

// Class representing a grid/suite of MapLevels
public class MapArea implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = 2111915163885816511L;

	// Name of the area
	protected String name;
	
	// Grid of screens/levels in that compose the area
	protected MapLevel[][] levels;
	
	// Theme that is given to the screens within the level
	// EX. Temple, Desert, Cave, Dungeon, etc.
	protected String theme;
	
	// Path to the song that plays in the area
	protected String musicPath;
	
	// Volume of the Midi track for the level
	protected int musicVolume;
	
	// Flag determining whether area will be dark or not
	protected boolean isDark = false;
	
	// Constructor
	protected MapArea(MapLevel[][] levels, String name, String theme, String musicPath, int musicVolume) {
		this.levels = levels;
		this.name = name;
		this.theme = theme;
		this.musicPath = musicPath;
		this.musicVolume = musicVolume;
	}
	
	// Alternate Constructor with different parameter order (Does same thing)
	protected MapArea(String name, String theme, String musicPath, int musicVolume, MapLevel[][] levels) {
		this.levels = levels;
		this.name = name;
		this.theme = theme;
		this.musicPath = musicPath;
		this.musicVolume = musicVolume;
	}
	
	// Constructor (With Dark parameter)
	protected MapArea(MapLevel[][] levels, String name, String theme, String musicPath, int musicVolume, boolean isDark) {
		this.levels = levels;
		this.name = name;
		this.theme = theme;
		this.musicPath = musicPath;
		this.musicVolume = musicVolume;
		this.isDark = isDark;
	}
	
	// Alternate Constructor with different parameter order (With Dark parameter)
	protected MapArea(String name, String theme, String musicPath, int musicVolume, boolean isDark, MapLevel[][] levels) {
		this.levels = levels;
		this.name = name;
		this.theme = theme;
		this.musicPath = musicPath;
		this.musicVolume = musicVolume;
		this.isDark = isDark;
	}
	
	//--------------------
	// Getters and Setters
	
	public MapLevel[][] getLevels() {
		return this.levels;
	}
	
	public MapLevel getLevel(int x, int y) {
		return this.levels[y][x];
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getTheme() {
		return this.theme;
	}
	
	public String getMusic() {
		return this.musicPath;
	}
	
	public int getMusicVolume() {
		return this.musicVolume;
	}
	
	public int getLength() {
		return this.levels[0].length;
	}
	
	public int getHeight() {
		return this.levels.length;
	}
	
	public boolean showDark() {
		return this.isDark;
	}

}
