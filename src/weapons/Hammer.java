package weapons;

import java.util.Random;

import characters.GCharacter;
import helpers.GColors;
import managers.EntityManager;

// Class representing the Hammer-class weapons usable by the player
public class Hammer extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = -7445620100936275560L;

	// Constructor
	public Hammer(String name, int minDmg, int maxDmg,
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
					int dmg = this.calculatePierceDamage(this.chargeMult, npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player smashed and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
				} else {
					// If not charged deal normal damage and attack normally
					int dmg = this.calculateDamage(npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player slammed and dealt " + Integer.toString(dmg)
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
	
	// Calculate damage for the weapon with damage multiplier
	// Ignore half of the enemy's armor, rounded down
	public int calculatePierceDamage(double dmgMult, GCharacter npc) {
		Random r = new Random();
		int dmg;
		int halfArmor = npc.getArmor() / 2;
		int newMin = (int) Math.floor(this.minDmg * dmgMult);
		int newMax = (int) Math.floor((this.maxDmg - halfArmor) * dmgMult);
		
		// Maximum damage cannot drop below 0
		if(newMax < 0) {
			newMax = 0;
		}
		
		// If new maximum damage is below minimum damage, drop the minimum damage to match
		if(newMin > newMax) {
			newMin = newMax;
		}
		
		// If the player gets lucky, they crit the enemy
		// Otherwise, calculate damage normally
		if(Math.random() < this.critChance) {
			dmg = (int) Math.floor(newMax * this.critMult);
		} else {
			dmg = r.nextInt((newMax - newMin) + 1) + newMin;
		}
		
		// Limit minimum damage at 0
		if(dmg < 0) {
			dmg = 0;
		}
		
		// Return damage value
		return dmg;
	}
	
}
