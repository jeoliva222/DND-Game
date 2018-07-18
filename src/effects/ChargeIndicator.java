package effects;

import helpers.GPath;

// Charge Indicator to let player know what tiles their special/charge attacks
// are hitting.
public class ChargeIndicator extends GEffect {

	private static String chargeImage = GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_charge.png");
	
	public ChargeIndicator(int xPos, int yPos) {
		super(xPos, yPos, chargeImage);
	}
	
	public ChargeIndicator(int xPos, int yPos, int countDown) {
		super(xPos, yPos, chargeImage, countDown);
	}
	
}
