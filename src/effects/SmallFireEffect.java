package effects;

import helpers.GPath;

public class SmallFireEffect extends GEffect {

	private static String sfireImage = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "fire_effect_small.png");
	
	public SmallFireEffect(int xPos, int yPos) {
		super(xPos, yPos, sfireImage);
	}
	
	public SmallFireEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, sfireImage, countDown);
	}

}