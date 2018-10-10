package weapons;

import java.util.ArrayList;

import characters.GCharacter;
import effects.ChargeIndicator;
import gui.GameScreen;
import helpers.GColors;
import managers.EntityManager;
import tiles.MovableType;

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
			// While we still have a open bullet path, check for NPCs to hit
			int nextX = (em.getPlayer().getXPos() + dx);
			int nextY = (em.getPlayer().getYPos() + dy);
			boolean isEndHit = false;
			while(!isEndHit) { // Begin While --------------------------------------
				// First check for NPCs to add
				for(GCharacter npc : em.getNPCManager().getCharacters()) {
					if(nextX == npc.getXPos() && nextY == npc.getYPos()) {
						inlineEnemies.add(npc);
					}
				}
				
				// Then check for walls or OOB
				try {
					isEndHit = GameScreen
							.getTile(nextX, nextY)
							.getTileType()
							.getMovableType() == MovableType.WALL;
				} catch (ArrayIndexOutOfBoundsException e) {
					isEndHit = true;
				}
				
				// Then update the next coordinates to check
				nextX += dx;
				nextY += dy;
				
			} // END While Loop ---------------------------------------------
			
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
				int dmg = this.calculateDamage(this.chargeMult, closestNPC);
				closestNPC.damageCharacter(dmg);
				this.sendToLog("Player sniped " + closestNPC.getName() + " and dealt "
				+ Integer.toString(dmg) + " damage.", GColors.ATTACK, closestNPC);
				
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
					int dmg = this.calculateDamage(npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player punched and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
					this.playSwingSound();
					return true;
				}
			}
		}
		return false;
	}

}
