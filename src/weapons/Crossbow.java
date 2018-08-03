package weapons;

import java.util.ArrayList;

import characters.GCharacter;
import effects.ChargeIndicator;
import gui.LogScreen;
import helpers.GColors;
import managers.EntityManager;

// Class that represents the 'Crossbow' weapons in-game
public class Crossbow extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = -8718640333057697034L;

	// Constructor
	public Crossbow(String name, int minDmg, int maxDmg,
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
		if(this.isCharged) {
			// Discharge weapon
			this.dischargeWeapon();
			
			// Find all enemies that are on player's shot path
			ArrayList<GCharacter> inlineEnemies = new ArrayList<GCharacter>();
			if(Math.abs(dx) > Math.abs(dy)) {
				// X-wise shot
				if(dx > 0) {
					// Right-wise shot
					// Check for NPCs in the same line as your attack
					for(GCharacter npc : em.getNPCManager().getCharacters()) {
						if((em.getPlayer().getXPos()) < npc.getXPos()
								&& (em.getPlayer().getYPos()) == npc.getYPos()) {
							inlineEnemies.add(npc);
						}
					}
				} else {
					// Left-wise shot
					// Check for NPCs in the same line as your attack
					for(GCharacter npc : em.getNPCManager().getCharacters()) {
						if((em.getPlayer().getXPos()) > npc.getXPos()
								&& (em.getPlayer().getYPos()) == npc.getYPos()) {
							inlineEnemies.add(npc);
						}
					}
				}
			} else {
				// Y-wise shot
				if(dy > 0) {
					// Downward shot
					// Check for NPCs in the same line as your attack
					for(GCharacter npc : em.getNPCManager().getCharacters()) {
						if((em.getPlayer().getXPos()) == npc.getXPos()
								&& (em.getPlayer().getYPos()) < npc.getYPos()) {
							inlineEnemies.add(npc);
						}
					}
				} else {
					// Upward shot
					// Check for NPCs in the same line as your attack
					for(GCharacter npc : em.getNPCManager().getCharacters()) {
						if((em.getPlayer().getXPos()) == npc.getXPos()
								&& (em.getPlayer().getYPos()) > npc.getYPos()) {
							inlineEnemies.add(npc);
						}
					}
				}
			}
			
			// Then check for closest NPC in the line of enemies
			if(inlineEnemies.isEmpty()) {
				// If no shot hit, return false
				return false;
			} else {
				// Initialize 'closest' trackers
				GCharacter closestNPC = null;
				int closestDist = 1000;
				
				// Find the closest NPC in our line of fire
				for(GCharacter npc: inlineEnemies) {
					int dist = 0;
					
					// Total the NPC's distance from the player
					dist += Math.abs(em.getPlayer().getXPos() - npc.getXPos());
					dist += Math.abs(em.getPlayer().getYPos() - npc.getYPos());
					
					// If it's smaller than our current closest NPC, record this
					if(dist < closestDist) {
						closestNPC = npc;
						closestDist = dist;
					}
				}
				
				// Damage the closest NPC
				int dmg = this.calculateDamage(this.chargeMult);
				closestNPC.damageCharacter(dmg);
				LogScreen.log("Player sniped " + closestNPC.getName() + " and dealt "
				+ Integer.toString(dmg) + " damage.", GColors.ATTACK);
				
				// Add on-hit effect
				em.getEffectManager()
					.addEffect(new ChargeIndicator(closestNPC.getXPos(), closestNPC.getYPos()));
				
				// Return true to indicate we hit a target
				return true;
			}
			
		} else {
			// If not charged, check for immediately adjacent NPCs to punch
			for(GCharacter npc : em.getNPCManager().getCharacters()) {
				if((em.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage and attack normally
					int dmg = this.calculateDamage();
					npc.damageCharacter(dmg);
					LogScreen.log("Player punched and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK);
					this.playSwingSound();
					return true;
				}
			}
		}
		return false;
	}

}
