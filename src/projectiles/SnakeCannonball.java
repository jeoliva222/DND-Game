package projectiles;

import characters.GCharacter;
import helpers.GPath;

// Class representing projectile fired from SnakeTank Desert boss
public class SnakeCannonball extends GProjectile {

	private static String baseImagePath = GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png");

	public SnakeCannonball(int xPos, int yPos, int dx, int dy, GCharacter owner) {
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
		
		return SnakeCannonball.baseImagePath;
		
//		if(dx < 0) {
//			return SnakeCannonball.baseImagePath + "_LEFT.png";
//		} else {
//			return SnakeCannonball.baseImagePath + "_RIGHT.png";
//		}
	}
	
}
