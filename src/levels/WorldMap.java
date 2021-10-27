package levels;

// Contains the definition for the relationships and
// connections in between MapAreas. Relationships used by AreaFetcher to 
// fetch instances of areas when moving between two areas
public class WorldMap {
	
	//************************************************
	// WORLD DEFINITION
	
	public static String[][] gameWorld = new String[][] {
		{"sewer"  , null	},
		{"dungeon", "desert"},
		{"lily"   , "museum"}
	};
	
	//************************************************
	// FUNCTIONS
	
	// Returns the area in the world based off of its index
	public static MapArea getArea(int x, int y) {
		return AreaFetcher.fetchArea(WorldMap.getAreaKey(x, y));
	}
	
	// Returns the string key of an area in the world based off its index
	public static String getAreaKey(int x, int y) {
		try {
			String returnKey = WorldMap.gameWorld[y][x];
			return returnKey;
		} catch(IndexOutOfBoundsException e) {
			return "";
		}
	}
	
	// Sets world to particular instance.
	public static void setWorld(String[][] newWorld) {
		WorldMap.gameWorld = null;
		WorldMap.gameWorld = newWorld;
	}
	
}
