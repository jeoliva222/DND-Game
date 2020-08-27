package weapons;

import characters.GCharacter;
import characters.allies.Player;
import effects.ChargeIndicator;
import helpers.GColors;
import managers.EntityManager;

/**
 * Class representing the 'Sword' type weapons in-game.
 * Swords are a basic weapon with a charged attack that cleaves the three spaces in
 * front of the user.
 * @author jeoliva
 */
public class Sword extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = -2868755190028951400L;

	public Sword(String name, int minDmg, int maxDmg, double critChance, double critMult,
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
		
		// For every NPC, check if we're attacking in their direction next to them
		for (GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, do attack
			if ((player.getXPos() + dx) == npc.getXPos() && (player.getYPos() + dy) == npc.getYPos()) {
				// If weapon is charged, swipe 3 tiles and multiply damage
				if (isCharged && player.checkEnergy(chargeExhaust)) {
					// Exhaust the player
					player.exhaustPlayer(chargeExhaust);
					
					// Deal damage to middle target first
					int dmg = calculateDamage(chargeMult, npc);
					npc.damageCharacter(dmg);
					em.getEffectManager().addEffect(new ChargeIndicator((player.getXPos() + dx), (player.getYPos() + dy)));
					sendToLog("Player swiped and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
					
					// Then check/attack for characters to relative left/right of initial attack
					findSwipeTargets(dx, dy);
				} else {
					// If not charged deal normal damage and attack normally
					int dmg;
					if (player.exhaustPlayer(attackExhaust)) {
						dmg = calculateDamage(npc);
					} else {
						dmg = calculateDamage(EXHAUST_MULT, npc);
					}
					npc.damageCharacter(dmg);
					sendToLog("Player swung and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
				}
				
				playSwingSound();
				return true;
			}
		}
		// If nothing connects, return false
		return false;
	}
	
	// Finds the swipe target coordinates, marks the tiles, then damages characters
	public void findSwipeTargets(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Define attack coordinates
		int xPos1 = (em.getPlayer().getXPos() + dx + Math.abs(dy));
		int yPos1 = (em.getPlayer().getYPos() + dy + Math.abs(dx));
		int xPos2 = (em.getPlayer().getXPos() + dx - Math.abs(dy));
		int yPos2 = (em.getPlayer().getYPos() + dy - Math.abs(dx));
		
		// Mark damaged spots
		em.getEffectManager().addEffect(new ChargeIndicator(xPos1, yPos1));
		em.getEffectManager().addEffect(new ChargeIndicator(xPos2, yPos2));	
		
		// Damage affected characters
		for (GCharacter npc : em.getNPCManager().getCharacters()) {
			if ((xPos1 == npc.getXPos() && yPos1 == npc.getYPos()) || (xPos2 == npc.getXPos() && yPos2 == npc.getYPos())) {
				// Damage target and log result
				int dmg = calculateDamage(chargeMult, npc);
				npc.damageCharacter(dmg);
				sendToLog("Player swiped and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
			}
		}
	}

}
