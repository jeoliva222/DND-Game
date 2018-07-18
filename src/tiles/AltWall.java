package tiles;

import helpers.GPath;
import levels.WorldMap;
import managers.EntityManager;

public class AltWall extends TileType {

	// Serialization ID
	private static final long serialVersionUID = 8261031517654925504L;
	
	public AltWall() {
		this.moveType = MovableType.WALL;
	}

	@Override
	public String selectImage() {
		// Fetch region path to display image from correct area
		int areaX = EntityManager.getPlayer().getAreaX();
		int areaY = EntityManager.getPlayer().getAreaY();
		String regionPath = WorldMap.getArea(areaX, areaY).getTheme();
		
		this.imagePath = GPath.createImagePath(GPath.TILE, regionPath, "altwall.png");
		return this.imagePath;
	}

	@Override
	public void onStep() {
		// Do nothing
	}

}
