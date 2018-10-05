package projectiles;

import characters.GCharacter;
import helpers.GPath;

// Projectile fired from KingStaff special weapon
public class KingStaffFlame extends GProjectile {

	private static String imagePath = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "fire_effect.png");
	
	private int turnCount = 2;
	
	public KingStaffFlame(int xPos, int yPos, int dx, int dy, GCharacter owner) {
		super("Staff Flame", xPos, yPos, dx, dy, owner);
		
		// Damage values
		this.minDmg = 1;
		this.maxDmg = 3;
		
		// Critical Values
		this.critChance = 0.15;
		this.critMult = 1.7;
		
		// Piercing values
		this.entityPiercing = true;
		this.wallPiercing = false;
	}

	@Override
	public String getImage() {
		return KingStaffFlame.imagePath;
	}
	
	@Override
	protected boolean updateProjectile() {
		boolean result = super.updateProjectile();
		
		this.turnCount += -1;
		if(turnCount <= 0) {
			return true;
		} else {
			return result;
		}
	}
	

}
