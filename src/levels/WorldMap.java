package levels;

// Contains the definition for the relationships and
// connections in between MapAreas. Relationships used by AreaFetcher to 
// fetch instances of areas when moving between two areas
public class WorldMap {
	
	//************************************************
	// WORLD DEFINITION
	
	public static AreaEnum[][] gameWorld = new AreaEnum[][] {
		{AreaEnum.SEWER  , null	},
		{AreaEnum.DUNGEON, AreaEnum.DESERT},
		{AreaEnum.LILY   , AreaEnum.MUSEUM}
	};
	
	//************************************************
	// FUNCTIONS
	
	/**
	 * Returns the area in the world based off of its index
	 * @param x Area's X index
	 * @param y Area's Y index
	 * @return MapArea at provided indices
	 */
	public static MapArea getArea(int x, int y) {
		return AreaFetcher.fetchArea(WorldMap.getAreaKey(x, y));
	}
	
	/**
	 * Returns the enum key of an area in the world based off its index
	 * @param x Area's X index
	 * @param y Area's Y index
	 * @return AreaEnum specifying a MapArea at the provided indices
	 */
	public static AreaEnum getAreaKey(int x, int y) {
		try {
			AreaEnum returnKey = WorldMap.gameWorld[y][x];
			return returnKey;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * Sets world to particular instance.
	 * @param newWorld AreaEnum 2D array representing a world
	 */
	public static void setWorld(AreaEnum[][] newWorld) {
		WorldMap.gameWorld = null;
		WorldMap.gameWorld = newWorld;
	}
	
	/**
	 * Area Name enumeration that represents areas within the game.
	 * @author jeoliva
	 */
	public static enum AreaEnum {
		
		DUNGEON("dungeon"),
		DESERT("desert"),
		MUSEUM("museum"),
		SEWER("sewer"),
		LILY("lily");
		
		// Field
		private final String themePath;
		
		private AreaEnum(String themePath) {
			this.themePath = themePath;
		}
		
		/**
		 * Returns a theme path used for file naming conventions.
		 * @return String
		 */
		public String getThemePath() {
			return this.themePath;
		}
		
	}
	
}
