package projectiles;

import helpers.GPath;

// Class representing the projectile fired from the Cactian enemy in the Poacher's Desert area
public class CactianNeedle extends GProjectile {

	private String basePath = GPath.createImagePath(GPath.ENEMY, GPath.CACTIAN);

	public CactianNeedle(int xPos, int yPos, int dx, int dy, Class<?> owner) {
		super("Cactus Needles", xPos, yPos, dx, dy, owner);
		
		// Damage values
		this.minDmg = 3;
		this.maxDmg = 4;
		
		// Critical Values
		this.critChance = 0.1;
		this.critMult = 1.5;
		
		// Piercing values
		this.entityPiercing = false;
		this.wallPiercing = false;
	}
	
	public String getImage() {
		// Get absolute value of speeds
		int absDX = Math.abs(this.dx);
		int absDY = Math.abs(this.dy);
		
		// Check which of the two relative directional speeds is greater
		if(absDX >= absDY) {
			// Check if needle is flying left or right
			if(this.dx >= 0) {
				return (this.basePath + "needles_RIGHT.png");
			} else {
				return (this.basePath + "needles_LEFT.png");
			}
		} else {
			// Check if needle is flying up or down
			if(this.dy >= 0) {
				return (this.basePath + "needles_DOWN.png");
			} else {
				return (this.basePath + "needles_UP.png");
			}
		}
	}
	
}
