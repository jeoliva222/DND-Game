package weapons;

import characters.GCharacter;
import helpers.GColors;
import managers.EntityManager;

// Basic weapon class that can only attack adjacent foes
public class Fists extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = 287165971255332203L;

	public Fists(String name, int minDmg, int maxDmg,
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
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		for(GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, complete attack
			if((em.getPlayer().getXPos() + dx) == npc.getXPos()
					&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
				if(this.isCharged) {
					// First, discharge weapon
					this.dischargeWeapon();
					
					// If charged deal extra damage with standard attack
					int dmg = this.calculateDamage(this.chargeMult, npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player punched and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
				} else {
					// If not charged deal normal damage and attack normally
					int dmg = this.calculateDamage(npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player slapped and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
				}
				// We hit something, so return true
				this.playSwingSound();
				return true;
			}
			
		}
		
		// If we hit nothing, return false
		return false;
	}

}
