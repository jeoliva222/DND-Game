package weapons;

import characters.GCharacter;
import characters.allies.Player;
import effects.ChargeIndicator;
import helpers.GColors;
import managers.EntityManager;

/**
 * Basic weapon class that can only attack adjacent foes
 * @author jeoliva
 */
public class Fists extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = 287165971255332203L;

	public Fists(String name, int minDmg, int maxDmg, double critChance, double critMult,
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
		
		for (GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, complete attack
			if ((player.getXPos() + dx) == npc.getXPos() && (player.getYPos() + dy) == npc.getYPos()) {
				if (isCharged && player.checkEnergy(chargeExhaust)) {
					// Exhaust the player
					player.exhaustPlayer(chargeExhaust);
					
					// If charged deal extra damage with standard attack
					int dmg = calculateDamage(chargeMult, npc);
					npc.damageCharacter(dmg);
					
					// Add effect on attacked tile and relay log message
					em.getEffectManager().addEffect(new ChargeIndicator(player.getXPos() + dx, player.getYPos() + dy));
					sendToLog("Player punched and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
				} else {
					// If not charged deal normal damage and attack normally
					int dmg;
					if (player.exhaustPlayer(attackExhaust)) {
						dmg = calculateDamage(npc);
					} else {
						dmg = calculateDamage(EXHAUST_MULT, npc);
					}
					npc.damageCharacter(dmg);
					sendToLog("Player slapped and dealt " + Integer.toString(dmg)
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

}
