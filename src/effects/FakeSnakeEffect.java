package effects;

import helpers.GPath;

// Effect used by SnakeGeneral in the Assassinate attack
public class FakeSnakeEffect extends GEffect {

	private static String fakeImage = GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png");
	
	public FakeSnakeEffect(int xPos, int yPos) {
		super(xPos, yPos, fakeImage);
	}
	
	public FakeSnakeEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, fakeImage, countDown);
	}
	
}
