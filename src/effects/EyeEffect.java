package effects;

import helpers.GPath;

// Eyeball effect used by WatcherEye when crashing the game
public class EyeEffect extends GEffect {

	private static String imagePath = GPath.createImagePath(GPath.ENEMY, GPath.GAZER, "Gazer_IDLE.png");
	
	public EyeEffect(int xPos, int yPos) {
		super(xPos, yPos, imagePath);
	}
	
	public EyeEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, imagePath, countDown);
	}
	
}
