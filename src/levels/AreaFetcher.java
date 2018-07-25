package levels;

import java.io.File;

import helpers.GPath;
import helpers.GameState;

// Class that helps fetch areas from save files based off of their name
public class AreaFetcher {
	
	// Fetch the relevant area by it's name
	// ---------
	// If never visited, generate a new one
	// If visited between last save, load from temp save folder
	// Otherwise, load from the main save folder
	protected static MapArea fetchArea(String areaName) {
		
		// Decides which area to load based off the name
		MapArea fetchedArea = null;

		if(new File(GPath.SAVE + "temp" + File.separator + areaName + ".ser").exists()) {
			System.out.println("Loaded TEMP");
			fetchedArea = GameState.loadArea(areaName, true);
		} else if(new File(GPath.SAVE + areaName + ".ser").exists()) {
			System.out.println("Loaded MAIN");
			fetchedArea = GameState.loadArea(areaName, false);
		} else {
			System.out.println("Loaded DEFAULT");
			fetchedArea = AreaFetcher.fetchDefaultArea(areaName);
		}
		
		// Return the area we fetched
		return fetchedArea;
		
	}
	
	// Always fetches the default area layout (Doesn't look for saves)
	public static MapArea fetchDefaultArea(String areaName) {
		// Decides which area to load based off the name
		MapArea fetchedArea = null;
		switch(areaName) {
			case "dungeon":
				fetchedArea = new DungeonLevels().area_DUNGEON;
				break;
			case "desert":
				fetchedArea = new DesertLevels().area_DESERT;
				break;
			default:
				System.out.println("Failed to fetch an area: " + areaName);
				break;
		}
		
		// Return the fetched area
		return fetchedArea;
	}

}
