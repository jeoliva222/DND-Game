package projectiles;

import characters.GCharacter;
import helpers.GPath;

public class KingFireball extends GProjectile {
	
	private static String baseImagePath = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "fireball");

	public KingFireball(int xPos, int yPos, int dx, int dy, GCharacter owner) {
		super("King's Fire", xPos, yPos, dx, dy, owner);
		
		// Damage values
		this.minDmg = 2;
		this.maxDmg = 3;
		
		// Critical Values
		this.critChance = 0.1;
		this.critMult = 1.4;
		
		// Piercing values
		this.entityPiercing = true;
		this.wallPiercing = false;
	}
	
	@Override
	public String getImage() {
		if(dx < 0) {
			return KingFireball.baseImagePath + "_LEFT.png";
		} else {
			return KingFireball.baseImagePath + "_RIGHT.png";
		}
	}

}
