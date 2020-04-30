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
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		if (isCharged) {
			// Discharge weapon
			dischargeWeapon();
			
			// Fire an arrow in the direction the player attacked
			em.getProjectileManager()
				.addProjectile(new Arrow((em.getPlayer().getXPos() + dx),
										(em.getPlayer().getYPos() + dy),
										dx,
										dy, Player.class));
			
			// We fired an arrow, so return true
			return true;
		} else {
			// If not charged, check for immediately adjacent NPCs to punch
			for (GCharacter npc : em.getNPCManager().getCharacters()) {
				if ((em.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage and attack normally
					int dmg = calculateDamage(npc);
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
