package tiles;

import helpers.GPath;
import levels.WorldMap;
import managers.EntityManager;

// Class representing a Wall tile type in the game
public class Wall extends TileType {
	
	// Serialization ID
	private static final long serialVersionUID = 7537040445662266550L;

	public Wall() {
		this.moveType = MovableType.WALL;
	}
	
	@Override
	public String selectImage() {
		// Fetch region path to display image from correct area
		int areaX = EntityManager.getPlayer().getAreaX();
		int areaY = EntityManager.getPlayer().getAreaY();
		String regionPath = WorldMap.getArea(areaX, areaY).getTheme();
		String imagePath = GPath.createImagePath(GPath.TILE, regionPath, "wall.png");
		
		// Return the full image path
		this.imagePath = imagePath;
		return imagePath;
	}

	@Override
	public void onStep() {
		// Nothing here
	}

}
