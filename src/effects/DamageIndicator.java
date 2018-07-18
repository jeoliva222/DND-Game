package effects;

import helpers.GPath;

public class DamageIndicator extends GEffect {

	private static String damageImage = GPath.createImagePath(GPath.TILE, GPath.GENERIC, "area_damage.png");
	
	public DamageIndicator(int xPos, int yPos) {
		super(xPos, yPos, damageImage);
	}
	
	public DamageIndicator(int xPos, int yPos, int countDown) {
		super(xPos, yPos, damageImage, countDown);
	}
	
}
