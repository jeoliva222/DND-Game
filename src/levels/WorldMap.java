package levels;

// Contains the definition for the relationships and
// connections in between MapAreas
public class WorldMap {
	
	//************************************************
	// WORLD DEFINITION
	
	public static MapArea[][] gameWorld = new MapArea[][] {
		{DungeonLevels.area_DUNGEON, DesertLevels.area_DESERT}
	};
	
	//************************************************
	// FUNCTIONS
	
	// Returns the index of an area in the world
	public static MapArea getArea(int x, int y) {
		return WorldMap.gameWorld[y][x];
	}
	
	// Sets world to particular instance. Used with Saving/Loading
	public static void setWorld(MapArea[][] newWorld) {
		WorldMap.gameWorld = null;
		WorldMap.gameWorld = newWorld;
	}
	
}
