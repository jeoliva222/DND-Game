package tiles;

import helpers.GPath;
import managers.EntityManager;

// Class defining alternate variation of wall tile on screen
public class AltWall extends TileType {

	// Serialization ID
	private static final long serialVersionUID = 8261031517654925504L;
	
	public AltWall() {
		this.moveType = MovableType.WALL;
	}

	@Override
	public String selectImage() {
		// Fetch region path to display image from correct area
		String regionPath = EntityManager.getActiveArea().getTheme();
		
		this.imagePath = GPath.createImagePath(GPath.TILE, regionPath, "altwall.png");
		return this.imagePath;
	}

	@Override
	public void onStep() {
		// Do nothing
	}

}
