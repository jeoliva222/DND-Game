package weapons;

import characters.GCharacter;
import characters.allies.Player;
import helpers.GColors;
import managers.EntityManager;
import projectiles.Arrow;

/**
 * Class representing the 'Bow' weapons in-game
 * @author jeoliva
 */
public class Bow extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = 1207280989359432870L;


	// Constructor
	public Bow(String name, int minDmg, int maxDmg, double critChance, double critMult,
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
		
		if (isCharged && player.checkEnergy(chargeExhaust)) {
			// Fire an arrow in the direction the player attacked
			em.getProjectileManager()
				.addProjectile(new Arrow((em.getPlayer().getXPos() + dx),
										(em.getPlayer().getYPos() + dy),
										dx,
										dy, Player.class));
			
			// Deplete the player's energy
			player.exhaustPlayer(chargeExhaust);
			
			// We fired an arrow, so return true
			return true;
		} else {
			// If not charged, check for immediately adjacent NPCs to punch
			for (GCharacter npc : em.getNPCManager().getCharacters()) {
				if ((player.getXPos() + dx) == npc.getXPos() && (player.getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage and attack normally
					int dmg;
					if (player.exhaustPlayer(attackExhaust)) {
						dmg = calculateDamage(npc);
					} else {
						dmg = calculateDamage(EXHAUST_MULT, npc);
					}
					npc.damageCharacter(dmg);
					sendToLog("Player punched and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
					playSwingSound();
					return true;
				}
			}
		}
		
		// If we didn't connect with anything, return false
		return false;
	}
}
