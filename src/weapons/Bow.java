package weapons;

import characters.GCharacter;
import gui.LogScreen;
import helpers.GColors;
import managers.EntityManager;
import projectiles.Arrow;

// Class representing the 'Bow' weapons in-game
public class Bow extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = 1207280989359432870L;


	// Constructor
	public Bow(String name, int minDmg, int maxDmg,
			double critChance, double critMult, double chargeMult, String desc, String imagePath) {
		super(name, desc, imagePath);
		
		this.minDmg = minDmg;
		this.maxDmg = maxDmg;
		this.critChance = critChance;
		this.critMult = critMult;
		this.chargeMult = chargeMult;
	}

	
	@Override
	public boolean tryAttack(int dx, int dy) {
		if(this.isCharged) {
			// Discharge weapon
			this.dischargeWeapon();
			
			// Fire an arrow in the direction the player attacked
			EntityManager.getProjectileManager()
				.addProjectile(new Arrow((EntityManager.getPlayer().getXPos() + dx),
										(EntityManager.getPlayer().getYPos() + dy),
										dx,
										dy, null));
			
			// We fired an arrow, so return true
			return true;
		} else {
			// If not charged, check for immediately adjacent NPCs to punch
			for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
				if((EntityManager.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (EntityManager.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage and attack normally
					int dmg = this.calculateDamage();
					npc.damageCharacter(dmg);
					LogScreen.log("Player punched and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK);
					this.playSwingSound();
					return true;
				}
			}
			
		}
		
		// If we didn't connect with anything, return false
		return false;
	}
}
