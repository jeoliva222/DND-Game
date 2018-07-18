package effects;

import helpers.GPath;

public class RubbleEffect extends GEffect {

	private static String rocksImage = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "fallingRocks.png");
	
	public RubbleEffect(int xPos, int yPos) {
		super(xPos, yPos, rocksImage);
	}
	
	public RubbleEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, rocksImage, countDown);
	}
	
}
