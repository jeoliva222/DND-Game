package effects;

import helpers.GPath;

// Effect used by SnakeGeneral in the Assassinate attack
public class FakeSnakeEffect extends GEffect {

	private static String fakeImageBase = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_GENERAL, "fake");
	
	public FakeSnakeEffect(int xPos, int yPos, FakeSnakeType fakeType) {
		super(xPos, yPos, getFakeImage(fakeType));
	}
	
	public FakeSnakeEffect(int xPos, int yPos, int countDown, FakeSnakeType fakeType) {
		super(xPos, yPos, getFakeImage(fakeType), countDown);
	}
	
	private static String getFakeImage(FakeSnakeType fakeType) {
		return (fakeImageBase + fakeType.imgSuffix + ".png");
	}
	
	public enum FakeSnakeType {
		CANNON_PREP("_PREP_CANNON"),
		CANNON_ATT("_ATT_CANNON"),
		FIRE_PREP("_PREP_FIRE"),
		SWIPE_ATT("_ATT_COMBO_SWIPE");
		
		public String imgSuffix;
		
		FakeSnakeType(String imgSuffix) {
			this.imgSuffix = imgSuffix;
		}
	}
	
}
