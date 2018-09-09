package tiles;

import helpers.GPath;
import managers.EntityManager;

// Class that defines a pitfall space on a screen
public class Pit extends TileType {

	// Serialization ID
	private static final long serialVersionUID = -4330118722143931032L;

	// Constructors
	public Pit() {	
		this.moveType = MovableType.AIR;
	}
	
	public Pit(String imagePath) {
		this();
		this.imagePath = imagePath;
	}
	
	@Override
	public String selectImage() {
		// Fetch the appropriate image path if we do not have one set
		if(this.imagePath == null) {
			// Fetch region path to display image from correct area
			String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
			
			this.imagePath = GPath.createImagePath(GPath.TILE, regionPath, "pit.png");
		}
		
		return this.imagePath;
	}

	@Override
	public void onStep() {
		// Shouldn't be able to step on this
	}

}
