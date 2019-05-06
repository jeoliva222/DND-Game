package effects;

import helpers.GPath;

// Effect used by SnakeGeneral when warping
public class WarpFizzleEffect extends GEffect {

	private static String fizzleImage = GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png");
	
	public WarpFizzleEffect(int xPos, int yPos) {
		super(xPos, yPos, fizzleImage);
	}
	
	public WarpFizzleEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, fizzleImage, countDown);
	}
}
