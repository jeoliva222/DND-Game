package weapons;

import characters.GCharacter;
import effects.ChargeIndicator;
import gui.LogScreen;
import helpers.GColors;
import managers.EntityManager;

// Swords are the most basic weapon with a charged attack that deals extra damage
public class Sword extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = -2868755190028951400L;

	public Sword(String name, int minDmg, int maxDmg,
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
		// For every NPC, check if we're attacking in their direction next to them
		for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, do attack
			if((EntityManager.getPlayer().getXPos() + dx) == npc.getXPos()
					&& (EntityManager.getPlayer().getYPos() + dy) == npc.getYPos()) {
				// If weapon is charged, swipe 3 tiles and multiply damage
				if(this.isCharged) {
					// Discharge weapon
					this.dischargeWeapon();
					
					// Deal damage to middle target first
					int dmg = this.calculateDamage(this.chargeMult);
					npc.damageCharacter(dmg);
					EntityManager.getEffectManager().addEffect
						(new ChargeIndicator((EntityManager.getPlayer().getXPos() + dx),
							(EntityManager.getPlayer().getYPos() + dy)));
					LogScreen.log("Player swiped and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK);
					
					// Then check/attack for characters to relative left of attack
					this.findSwipeTargets(dx, dy);
				} else {
					// If not charged deal normal damage and attack normally
					int dmg = this.calculateDamage();
					npc.damageCharacter(dmg);
					LogScreen.log("Player swung and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK);
				}
				this.playSwingSound();
				return true;
			}
		}
		// If nothing connects, return false
		return false;
	}
	
	// Finds and attacks edge targets of swipe/charged sword attack
	private void findSwipeTargets(int dx, int dy) {
		if(Math.abs(dx) > Math.abs(dy)) {
			// Target is to left or right of player
			EntityManager.getEffectManager().addEffect(new ChargeIndicator(EntityManager.getPlayer().getXPos() + dx, EntityManager.getPlayer().getYPos() + 1));
			EntityManager.getEffectManager().addEffect(new ChargeIndicator(EntityManager.getPlayer().getXPos() + dx, EntityManager.getPlayer().getYPos() - 1));	
			
			// Find and attack upper/lower targets if they exist
			for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
				if((EntityManager.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (EntityManager.getPlayer().getYPos() + 1) == npc.getYPos()) {
					// Upper target attack
					int dmg = this.calculateDamage(this.chargeMult);
					npc.damageCharacter(dmg);
					LogScreen.log("Player swiped and dealt " + Integer.toString(dmg)
					+ " damage to " + npc.getName() + ".", GColors.ATTACK);
				} else if ((EntityManager.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (EntityManager.getPlayer().getYPos() - 1) == npc.getYPos()) {
					// Lower target
					int dmg = this.calculateDamage(this.chargeMult);
					npc.damageCharacter(dmg);
					LogScreen.log("Player swiped and dealt " + Integer.toString(dmg)
					+ " damage to " + npc.getName() + ".", GColors.ATTACK);
				}
			}
		} else {
			// Target is above/below the player
			EntityManager.getEffectManager().addEffect(new ChargeIndicator(EntityManager.getPlayer().getXPos() + 1, EntityManager.getPlayer().getYPos() + dy));
			EntityManager.getEffectManager().addEffect(new ChargeIndicator(EntityManager.getPlayer().getXPos() - 1, EntityManager.getPlayer().getYPos() + dy));	
			
			// Find and attack left/right targets if they exist
			for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
				if((EntityManager.getPlayer().getXPos() + 1) == npc.getXPos()
						&& (EntityManager.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// Right target
					int dmg = this.calculateDamage(this.chargeMult);
					npc.damageCharacter(dmg);
					LogScreen.log("Player swiped and dealt " + Integer.toString(dmg)
					+ " damage to " + npc.getName() + ".", GColors.ATTACK);
				} else if ((EntityManager.getPlayer().getXPos() - 1) == npc.getXPos()
						&& (EntityManager.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// Left target
					int dmg = this.calculateDamage(this.chargeMult);
					npc.damageCharacter(dmg);
					LogScreen.log("Player swiped and dealt " + Integer.toString(dmg)
					+ " damage to " + npc.getName() + ".", GColors.ATTACK);
				}
			}
		}
	}

}
