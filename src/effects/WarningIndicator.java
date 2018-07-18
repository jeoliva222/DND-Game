package effects;

import helpers.GPath;

public class WarningIndicator extends GEffect {

	private static String warningImage = GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_warning.png");
	
	public WarningIndicator(int xPos, int yPos) {
		super(xPos, yPos, warningImage);
	}
	
	public WarningIndicator(int xPos, int yPos, int countDown) {
		super(xPos, yPos, warningImage, countDown);
	}
	
}
