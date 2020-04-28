package effects;

import helpers.GPath;

// Effect used by SnakeGeneral in the Assassinate attack
public class FakeSnakeEffect extends GEffect {

	private static String fakeImageBase = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_GENERAL, "fake");
	
	public FakeSnakeEffect(int xPos, int yPos, FakeSnakeType fakeType, boolean isHealthy) {
		super(xPos, yPos, getFakeImage(fakeType, isHealthy));
	}
	
	public FakeSnakeEffect(int xPos, int yPos, int countDown, FakeSnakeType fakeType, boolean isHealthy) {
		super(xPos, yPos, getFakeImage(fakeType, isHealthy), countDown);
	}
	
	private static String getFakeImage(FakeSnakeType fakeType, boolean isHealthy) {
		String healthStr = (isHealthy ? "_full" : "_fatal");
		return (fakeImageBase + healthStr + fakeType.imgSuffix + ".png");
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
