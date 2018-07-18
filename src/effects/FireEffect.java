package effects;

import helpers.GPath;

public class FireEffect extends GEffect {

	private static String fireImage = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "fire_effect.png");
	
	public FireEffect(int xPos, int yPos) {
		super(xPos, yPos, fireImage);
	}
	
	public FireEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, fireImage, countDown);
	}

}
