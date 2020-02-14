package tiles;

import helpers.GPath;
import managers.EntityManager;

// Ground type with alternate variant image
public class AltGround extends TileType {

	// Serialization ID
	private static final long serialVersionUID = -692992248022598471L;

	// Constructor
	public AltGround() {
		this.moveTypes = MovableType.ALT_GROUND;
	}
	
	// Constructor
	public AltGround(String imagePath) {
		this();
		this.imagePath = imagePath;
	}
	
	@Override
	public String selectImage() {
		// Fetch the appropriate image path if we do not have one set
		if(this.imagePath == null) {
			// Fetch region path to display image from correct area
			String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
		
			this.imagePath = GPath.createImagePath(GPath.TILE, regionPath, "altground.png");
		}
		
		return this.imagePath;
	}

	
	
	@Override
	public void onStep() {
		// Nothing happens
	}

}
