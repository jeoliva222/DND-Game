package projectiles;

import helpers.GPath;

// Class representing spit projectile fired by SandWurm enemies in the Desert area
public class SandwurmSpit extends GProjectile {

	private String basePath = GPath.createImagePath(GPath.ENEMY, GPath.SANDWURM);

	public SandwurmSpit(int xPos, int yPos, int dx, int dy, Class<?> owner) {
		super("Bile Spit", xPos, yPos, dx, dy, owner);
		
		// Damage values
		this.minDmg = 4;
		this.maxDmg = 5;
		
		// Critical Values
		this.critChance = 0.1;
		this.critMult = 1.6;
		
		// Piercing values
		this.entityPiercing = false;
		this.wallPiercing = true;
	}
	
	public String getImage() {
		// Get absolute value of speeds
		int absDX = Math.abs(this.dx);
		int absDY = Math.abs(this.dy);
		
		// Check which of the two relative directional speeds is greater
		if(absDX >= absDY) {
			// Check if spit is flying left or right
			if(this.dx >= 0) {
				return (this.basePath + "Spit_RIGHT.png");
			} else {
				return (this.basePath + "Spit_LEFT.png");
			}
		} else {
			// Check if spit is flying up or down
			if(this.dy >= 0) {
				return (this.basePath + "Spit_DOWN.png");
			} else {
				return (this.basePath + "Spit_UP.png");
			}
		}
	}
	
}
