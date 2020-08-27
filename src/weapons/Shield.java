package weapons;

import characters.GCharacter;
import characters.allies.Player;
import effects.ChargeIndicator;
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

	public Shield(String name, int minDmg, int maxDmg, int blockValue, double critChance, double critMult,
			double chargeMult, int attackExhaust, int chargeExhaust, String desc, String imagePath) {
		super(name, desc, imagePath);
		
		this.minDmg = minDmg;
		this.maxDmg = maxDmg;
		this.blockValue = blockValue;
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
					sendToLog("Player bashed and dealt " + Integer.toString(dmg)
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
					sendToLog("Player bashed and dealt " + Integer.toString(dmg)
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
