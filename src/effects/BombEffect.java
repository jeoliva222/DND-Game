package effects;

import helpers.GPath;

public class BombEffect extends GEffect {

	private static String onImage = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_GENERAL, "Mine_ATTACK.png");
	private static String offImage = GPath.createImagePath(GPath.ENEMY, GPath.SNAKE_GENERAL, "Mine_IDLE.png");
	
	public BombEffect(int xPos, int yPos) {
		super(xPos, yPos, onImage);
	}
	
	public BombEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, onImage, countDown);
	}
	
	public BombEffect(int xPos, int yPos, boolean isOn) {
		super(xPos, yPos, onImage);
		if(isOn) {
			// Do nothing
		} else {
			this.filepath = offImage;
		}
	}
	
	public BombEffect(int xPos, int yPos, int countDown, boolean isOn) {
		super(xPos, yPos, onImage, countDown);
		if(isOn) {
			// Do nothing
		} else {
			this.filepath = offImage;
		}
	}
	
}
