package projectiles;

import helpers.GPath;

// Class representing projectile fired from SnakeTank Desert boss
public class SnakeCannonball extends GProjectile {

	private static String baseImagePath = GPath.createImagePath(GPath.ENEMY, GPath.SNAKETANK, "cannonball");

	public SnakeCannonball(int xPos, int yPos, int dx, int dy, Class<?> owner) {
		super("Cannonball", xPos, yPos, dx, dy, owner);
		
		// Damage values
		this.minDmg = 6;
		this.maxDmg = 8;
		
		// Critical Values
		this.critChance = 0.1;
		this.critMult = 1.5;
		
		// Piercing values
		this.entityPiercing = false;
		this.wallPiercing = false;
	}
	
	@Override
	public String getImage() {
		if(dx < 0) {
			return SnakeCannonball.baseImagePath + "_LEFT.png";
		} else {
			return SnakeCannonball.baseImagePath + "_RIGHT.png";
		}
	}
	
}
