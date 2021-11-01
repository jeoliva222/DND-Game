package levels;

import java.io.File;

import levels.WorldMap.AreaEnum;
import helpers.GPath;
import helpers.GameState;

// Class that helps fetch areas from save files based off of their name
public class AreaFetcher {
	
	/** Fetch the relevant area by it's name. <br/><br/>
	 * If never visited, generate a new one / <br/>
	 * If visited between last save, load from temp save folder / <br/>
	 * Otherwise, load from the main save folder
	 * @param areaEnum Area Name enumeration to fetch
	 * @return MapArea
	 */
	protected static MapArea fetchArea(AreaEnum areaEnum) {
		// Null check
		if (areaEnum == null) {
			return null;
		}
		
		// Decides which area to load based off the name
		String areaName = areaEnum.getThemePath();
		MapArea fetchedArea = null;
		if (new File(GPath.SAVE + "temp" + File.separator + areaName + ".ser").exists()) {
			System.out.println("Loaded TEMP");
			fetchedArea = GameState.loadArea(areaName, true);
		} else if (new File(GPath.SAVE + areaName + ".ser").exists()) {
			System.out.println("Loaded MAIN");
			fetchedArea = GameState.loadArea(areaName, false);
		} else {
			System.out.println("Loaded DEFAULT");
			fetchedArea = AreaFetcher.fetchDefaultArea(areaEnum);
		}
		
		// Return the area we fetched
		return fetchedArea;
		
	}
	
	/**
	 * Always fetches the default area layout (Doesn't look for saves)
	 * @param areaEnum Area Name enumeration to fetch
	 * @return Default MapArea for areaName
	 */
	public static MapArea fetchDefaultArea(AreaEnum areaEnum) {
		// Null check
		if (areaEnum == null) {
			return null;
		}
		
		// Decides which area to load based off the name
		MapArea fetchedArea = null;
		switch(areaEnum) {
			case DUNGEON:
				fetchedArea = new DungeonLevels().area_DUNGEON;
				break;
			case DESERT:
				fetchedArea = new DesertLevels().area_DESERT;
				break;
			case MUSEUM:
				fetchedArea = new MuseumLevels().area_MUSEUM;
				break;
			case SEWER:
				fetchedArea = new SewerLevels().area_SEWER;
				break;
			case LILY:
				fetchedArea = new LilyLevels().area_LILY;
				break;
			default:
				System.out.println("Failed to fetch an area: " + areaEnum);
				break;
		}
		
		// Return the fetched area
		return fetchedArea;
	}

}
