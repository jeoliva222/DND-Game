package tiles;

import helpers.GPath;
import managers.EntityManager;

// Class that defines a pitfall space on a screen
public class Pit extends TileType {

	// Serialization ID
	private static final long serialVersionUID = -4330118722143931032L;

	// Constructor
	public Pit() {	
		this.moveType = MovableType.AIR;
	}
	
	@Override
	public String selectImage() {
		// TODO Make pit image
		
		// Fetch region path to display image from correct area
		String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
		this.imagePath = GPath.createImagePath(GPath.TILE, regionPath, "ground.png");
		return this.imagePath;
	}

	@Override
	public void onStep() {
		// Shouldn't be able to step on this
	}

}
