package effects;

import helpers.GPath;

public class ThunderEffect extends GEffect {

	private static String onImage = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "thunder_ON.png");
	private static String offImage = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "thunder_OFF.png");
	
	public ThunderEffect(int xPos, int yPos) {
		super(xPos, yPos, onImage);
	}
	
	public ThunderEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, onImage, countDown);
	}
	
	public ThunderEffect(int xPos, int yPos, boolean isOn) {
		super(xPos, yPos, onImage);
		if(isOn) {
			// Do nothing
		} else {
			this.filepath = offImage;
		}
	}
	
	public ThunderEffect(int xPos, int yPos, int countDown, boolean isOn) {
		super(xPos, yPos, onImage, countDown);
		if(isOn) {
			// Do nothing
		} else {
			this.filepath = offImage;
		}
	}
}
