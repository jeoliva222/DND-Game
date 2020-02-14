package tiles;

import helpers.GPath;
import managers.EntityManager;

// Class representing a Wall tile type in the game
public class Wall extends TileType {
	
	// Serialization ID
	private static final long serialVersionUID = 7537040445662266550L;

	// Constructors
	public Wall() {
		this.moveTypes = MovableType.WALL;
	}
	
	public Wall(String imagePath) {
		this();
		this.imagePath = imagePath;
	}
	
	@Override
	public String selectImage() {
		// Fetch the appropriate image path if we do not have one set
		if(this.imagePath == null) {
			// Fetch region path to display image from correct area
			String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
			
			this.imagePath = GPath.createImagePath(GPath.TILE, regionPath, "wall.png");
		}
		
		return imagePath;
	}

	@Override
	public void onStep() {
		// Nothing here
	}

}
