package projectiles;

import helpers.GPath;


public class Arrow extends GProjectile {
	
	private String basePath = GPath.createImagePath(GPath.ENEMY, GPath.ARROW_TURRET);

	public Arrow(int xPos, int yPos, int dx, int dy, Class<?> owner) {
		super("Arrow", xPos, yPos, dx, dy, owner);
		
		// Damage values
		this.minDmg = 3;
		this.maxDmg = 3;
		
		// Critical Values
		this.critChance = 0.05;
		this.critMult = 1.7;
		
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
			// Check if arrow is flying left or right
			if(this.dx >= 0) {
				return (this.basePath + "arrow_RIGHT.png");
			} else {
				return (this.basePath + "arrow_LEFT.png");
			}
		} else {
			// Check if arrow is flying up or down
			if(this.dy >= 0) {
				return (this.basePath + "arrow_DOWN.png");
			} else {
				return (this.basePath + "arrow_UP.png");
			}
		}
	}

}
