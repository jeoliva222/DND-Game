package effects;

import helpers.GPath;

// Class that represents a Music Note damage effect
public class MusicEffect extends GEffect {

	private static String musicImage = GPath.createImagePath(GPath.ENEMY, GPath.WATCHMAN, "music_effect.png");
	
	public MusicEffect(int xPos, int yPos) {
		super(xPos, yPos, musicImage);
	}
	
	public MusicEffect(int xPos, int yPos, int countDown) {
		super(xPos, yPos, musicImage, countDown);
	}
	
}
