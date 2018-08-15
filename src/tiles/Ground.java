package tiles;

import java.util.Random;

import helpers.GPath;
import managers.EntityManager;

public class Ground extends TileType {
	
	// Serialization ID
	private static final long serialVersionUID = -6775552653085295185L;
	
	// Rarity of alternate images
	private int rarity = 15;

	// Constructors
	public Ground() {
		this.moveType = MovableType.GROUND;
	}
	
	public Ground(String imagePath) {
		this();
		this.imagePath = imagePath;
	}
	
	@Override
	public String selectImage() {
		// If we already have an imagePath, use that instead
		if(this.imagePath != null) {
			return this.imagePath;
		}
		
		// Fetch region path to display image from correct area
		String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
		String imagePath = null;
		
		// Fetch one of three images randomly, with
		// one variation being common, another being 1/this.rarity,
		// and the final variation being 1/(this.rarity^2)
		Random rand = new Random();
		int selector1 = rand.nextInt(this.rarity);
		int selector2 = rand.nextInt(this.rarity);
		if(selector1 == (this.rarity - 1)) {
			if(selector2 == (this.rarity - 1)) {
				imagePath = GPath.createImagePath(GPath.TILE, regionPath, "ground3.png");
			} else {
				imagePath = GPath.createImagePath(GPath.TILE, regionPath, "ground2.png");
			}
		} else {
			imagePath = GPath.createImagePath(GPath.TILE, regionPath, "ground.png");
		}
		
		// Return full image path
		this.imagePath = imagePath;
		return imagePath;
	}

	@Override
	public void onStep() {
		// Do nothing
	}

}
