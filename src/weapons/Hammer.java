package weapons;

import java.util.Random;

import buffs.Buff;
import buffs.RageBuff;
import characters.GCharacter;
import characters.allies.Player;
import effects.ChargeIndicator;
import helpers.GColors;
import managers.EntityManager;

/**
 * Class representing the Hammer-class weapons usable by the player
 * @author jeoliva
 */
public class Hammer extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = -7445620100936275560L;

	// Constructor
	public Hammer(String name, int minDmg, int maxDmg, double critChance, double critMult,
			double chargeMult, int attackExhaust, int chargeExhaust, String desc, String imagePath) {
		super(name, desc, imagePath);
		
		this.minDmg = minDmg;
		this.maxDmg = maxDmg;
		this.critChance = critChance;
		this.critMult = critMult;
		this.chargeMult = chargeMult;
		this.attackExhaust = attackExhaust;
		this.chargeExhaust = chargeExhaust;
	}
	
	@Override
	public boolean tryAttack(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		Player player = em.getPlayer();
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		for (GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, complete attack
			if ((plrX + dx) == npc.getXPos() && (plrY + dy) == npc.getYPos()) {
				if (isCharged && player.checkEnergy(chargeExhaust)) {
					// Exhaust the player
					player.exhaustPlayer(chargeExhaust);
					
					// If charged, hit all adjacent targets with piercing damage
					hitAdjacents(plrX, plrY);
					
					// Mark the tiles
					em.getEffectManager().addEffect(new ChargeIndicator(plrX + 1, plrY));
					em.getEffectManager().addEffect(new ChargeIndicator(plrX - 1, plrY));
					em.getEffectManager().addEffect(new ChargeIndicator(plrX, plrY + 1));
					em.getEffectManager().addEffect(new ChargeIndicator(plrX, plrY - 1));
				} else {
					// If not charged deal normal damage and attack normally
					int dmg;
					if (player.exhaustPlayer(attackExhaust)) {
						dmg = calculateDamage(npc);
					} else {
						dmg = calculateDamage(EXHAUST_MULT, npc);
					}
					npc.damageCharacter(dmg);
					sendToLog("Player smashed and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
				}
				
				// We hit something, so return true
				playSwingSound();
				return true;
			}
		}
		
		// If we hit nothing, return false
		return false;
	}
	
	// Calculate damage for the weapon with damage multiplier
	// Ignore half of the enemy's armor, rounded down
	private int calculatePierceDamage(double dmgMult, GCharacter npc) {
		Random r = new Random();
		int dmg;
		int halfArmor = npc.getArmor() / 2;
		
		// If player has rage, buff the damage multiplier
		if (EntityManager.getInstance().getPlayer().hasBuff(Buff.RAGE)) {
			dmgMult = dmgMult * RageBuff.dmgBoost;
		}
		
		int newMin = (int) Math.floor(minDmg * dmgMult);
		int newMax = (int) Math.floor((maxDmg * dmgMult) - halfArmor);
		
		// Maximum damage cannot drop below 0
		if (newMax < 0) {
			newMax = 0;
		}
		
		// If new maximum damage is below minimum damage, drop the minimum damage to match
		if (newMin > newMax) {
			newMin = newMax;
		}
		
		// If the player gets lucky, they crit the enemy
		// Otherwise, calculate damage normally
		if (Math.random() < critChance) {
			dmg = (int) Math.floor(newMax * critMult);
		} else {
			dmg = r.nextInt((newMax - newMin) + 1) + newMin;
		}
		
		// Limit minimum damage at 0
		if (dmg < 0) {
			dmg = 0;
		}
		
		// Return damage value
		return dmg;
	}
	
	private void hitAdjacents(int plrX, int plrY) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		for (GCharacter npc : em.getNPCManager().getCharacters()) {
			// Get relative location to player
			int distX = plrX - npc.getXPos();
			int distY = plrY - npc.getYPos();
			
			if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
					((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
				int dmg = calculatePierceDamage(chargeMult, npc);
				npc.damageCharacter(dmg);
				sendToLog("Player slammed and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
			}
		}
	}
	
}
