package tiles;

import helpers.GPath;
import managers.EntityManager;

// Class defining alternate variation of wall tile on screen
public class AltWall extends TileType {

	// Serialization ID
	private static final long serialVersionUID = 8261031517654925504L;
	
	// Constructors
	public AltWall() {
		this.moveTypes = MovableType.ALT_WALL;
	}
	
	public AltWall(String imagePath) {
		this();
		this.imagePath = imagePath;
	}

	@Override
	public String selectImage() {
		// Fetch the appropriate image path if we do not have one set
		if(this.imagePath == null) {
			// Fetch region path to display image from correct area
			String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
		
			this.imagePath = GPath.createImagePath(GPath.TILE, regionPath, "altwall.png");
		}
		
		return this.imagePath;
	}

	@Override
	public void onStep() {
		// Do nothing
	}

}
