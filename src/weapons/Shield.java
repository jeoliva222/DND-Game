package weapons;

import characters.GCharacter;
import characters.allies.Player;
import gui.LogScreen;
import helpers.GColors;
import managers.EntityManager;

/**
 * Class representing the 'Shield' type weapons in-game.
 * Shields can raise the player's armor when charging
 * @author jeoliva
 */ 
public class Shield extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = -5108882837128848111L;
	
	// Amount of damage the shield blocks on charge
	public int blockValue;

	public Shield(String name, int minDmg, int maxDmg, int blockValue,
			double critChance, double critMult, double chargeMult, String desc, String imagePath) {
		super(name, desc, imagePath);
		
		this.minDmg = minDmg;
		this.maxDmg = maxDmg;
		this.blockValue = blockValue;
		this.critChance = critChance;
		this.critMult = critMult;
		this.chargeMult = chargeMult;
	}

	@Override
	public boolean tryAttack(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Attack multiplier
		double attMult = 1.0;
		
		// If we are charged, affect the damage multiplier of the attack
		if (isCharged) {
			// First, discharge shield
			dischargeWeapon();
			
			// Modify attack multiplier if charged
			attMult = chargeMult;
		}
		
		// Attack adjacent targets normally
		for (GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, complete attack
			if ((em.getPlayer().getXPos() + dx) == npc.getXPos()
					&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
				// Hit immediately adjacent characters
				int dmg = calculateDamage(attMult, npc);
				npc.damageCharacter(dmg);
				sendToLog("Player bashed and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
				
				// Play swing sound and return true
				playSwingSound();
				return true;
			}
			
		}
		
		// If we hit nothing, return false
		return false;
	}
	
	@Override
	public void chargeWeapon() {
		// Fetch reference to player
		Player plr = EntityManager.getInstance().getPlayer();
		
		// If player is dead, don't do anything
		if (!plr.isAlive()) {
			return;
		}
		
		if (isCharged) {
			// Do nothing extra
		} else {
			LogScreen.log("Raised shield for " + Integer.toString(blockValue) + " block!");
			plr.addArmor(blockValue);
			this.isCharged = true;
		}
	}
	
	@Override
	public void dischargeWeapon() {
		// If we're not charged, don't do anything
		if (!isCharged) {
			return;
		}
		
		// Get instance of player
		Player plr = EntityManager.getInstance().getPlayer();
		
		// Lower shield block
		LogScreen.log("Lowered shield block.");
		plr.addArmor(-(blockValue));
		this.isCharged = false;
	}

}
