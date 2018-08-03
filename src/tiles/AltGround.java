package tiles;

import helpers.GPath;
import managers.EntityManager;

// Ground type with alternate variant image
public class AltGround extends TileType {

	// Serialization ID
	private static final long serialVersionUID = -692992248022598471L;

	// Constructor
	public AltGround() {
		this.moveType = MovableType.GROUND;
	}
	
	@Override
	public String selectImage() {
		// Fetch region path to display image from correct area
		String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
		
		this.imagePath = GPath.createImagePath(GPath.TILE, regionPath, "altground.png");
		return this.imagePath;
	}

	
	
	@Override
	public void onStep() {
		// Nothing happens
	}

}
