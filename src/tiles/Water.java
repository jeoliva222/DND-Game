package tiles;

import java.util.Random;

import helpers.GPath;
import managers.EntityManager;

// Call representing a Water tile type in the game
public class Water extends TileType {

	// Serialization ID
	private static final long serialVersionUID = 5125458236570755603L;
	
	// Rarity of alternate tile variants
	private int rarity = 20;
	
	public Water() {
		this.moveType = MovableType.WATER;
	}
	
	@Override
	public String selectImage() {
		// Fetch region path to display image from correct area
		String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
		String imagePath = null;
		
		// Randomly select one of two images, with one
		// variation being common and the other variation having
		// a rarity of 1/this.rarity
		Random rand = new Random();
		int selector = rand.nextInt(this.rarity);
		if(selector == (this.rarity - 1)) {
			imagePath = GPath.createImagePath(GPath.TILE, regionPath, "water2.png");
		} else {
			imagePath = GPath.createImagePath(GPath.TILE, regionPath, "water.png");
		}
		
		// Return the full path
		this.imagePath = imagePath;
		return imagePath;
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		//System.out.println("**splash**");
	}

}
