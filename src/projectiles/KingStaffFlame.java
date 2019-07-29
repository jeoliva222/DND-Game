package projectiles;

import characters.GCharacter;
import debuffs.BurnDebuff;
import helpers.GPath;
import weapons.special.KingStaff;

// Projectile fired from KingStaff special weapon
public class KingStaffFlame extends GProjectile {

	private static String imagePath = GPath.createImagePath(GPath.ENEMY, GPath.KINGBONBON, "fire_effect.png");
	
	// Number of turns the flame lasts
	private int turnCount = 2;
	
	public KingStaffFlame(int xPos, int yPos, int dx, int dy, Class<?> owner) {
		super("Staff Flame", xPos, yPos, dx, dy, owner);
		
		// Damage values
		this.minDmg = KingStaff.STAFF_MIN_DMG;
		this.maxDmg = KingStaff.STAFF_MAX_DMG;
		
		// Critical Values
		this.critChance = KingStaff.STAFF_CRIT_CHANCE;
		this.critMult = KingStaff.STAFF_CRIT_MULT;
		
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
		
		// Decrease turn counter
		this.turnCount += -1;
		if(turnCount <= 0) {
			// Remove projectile when turn counter is depleted
			return true;
		} else {
			// Otherwise, use regular results
			return result;
		}
	}
	
	@Override
	public int calculateDamage(double dmgMult, GCharacter npc) {
		int output = super.calculateDamage(dmgMult, npc);
		npc.addDebuff(new BurnDebuff(3));
		return output;
	}
	

}
