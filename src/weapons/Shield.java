package weapons;

import characters.GCharacter;
import characters.allies.Player;
import gui.LogScreen;
import helpers.GColors;
import managers.EntityManager;

// Class representing the Shield weapons
// These weapons can block damage when charging and 
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
		
		// If we are charged, lower block and try attack with off-hand weapon
		if(this.isCharged) {
			// First, discharge shield
			this.dischargeWeapon();
			
			// Modify attack multiplier if charged
			attMult = this.chargeMult;
		}
		
		// If not charged, attack normally
		for(GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, complete attack
			if((em.getPlayer().getXPos() + dx) == npc.getXPos()
					&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
				// Hit immediately adjacent characters
				int dmg = this.calculateDamage(attMult, npc);
				npc.damageCharacter(dmg);
				this.sendToLog("Player bashed and dealt " + Integer.toString(dmg)
					+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
				
				// Play swing sound and return true
				this.playSwingSound();
				return true;
			}
			
		}
		
		// If we hit nothing, return false
		return false;
	}
	
	@Override
	public void chargeWeapon() {
		// Fetech reference to player
		Player plr = EntityManager.getInstance().getPlayer();
		
		// If player is dead, don't do anything
		if(!plr.isAlive()) {
			return;
		}
		
		if(this.isCharged) {
			// Do nothing extra
		} else {
			LogScreen.log("Raised shield for " + Integer.toString(this.blockValue) + " block!");
			plr.addArmor(this.blockValue);
			this.isCharged = true;
		}
	}
	
	@Override
	public void dischargeWeapon() {
		// If we're not charged, don't do anything
		if(!this.isCharged) return;
		
		// Get instance of player
		Player plr = EntityManager.getInstance().getPlayer();
		
		// Lower shield block
		LogScreen.log("Lowered shield block.");
		plr.addArmor(-(this.blockValue));
		this.isCharged = false;
	}
	
	@Override
	public void doOffhand() {
		// Charges the weapon in offhand
		this.chargeWeapon();
	}

}
