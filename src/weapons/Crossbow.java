package weapons;

import characters.GCharacter;
import effects.ChargeIndicator;
import gui.GameScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
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
			
			// While we still have a open bullet path, check for NPCs to hit
			int nextX = (em.getPlayer().getXPos() + dx);
			int nextY = (em.getPlayer().getYPos() + dy);
			boolean isEndHit = false;
			boolean isNPCHit = false;
			while(!isEndHit) { // Begin While --------------------------------------
				// First check for NPCs to hit
				for(GCharacter npc : em.getNPCManager().getCharacters()) {
					if(nextX == npc.getXPos() && nextY == npc.getYPos()) {
						// Damage the NPC
						int dmg = this.calculateDamage(this.chargeMult, npc);
						npc.damageCharacter(dmg);
						this.sendToLog("Player sniped " + npc.getName() + " and dealt "
						+ Integer.toString(dmg) + " damage.", GColors.ATTACK, npc);
						
						// Play arrow firing sound
						SoundPlayer.playWAV(GPath.createSoundPath("arrow_SHOT.wav"));
						
						// Add on-hit effect
						em.getEffectManager()
							.addEffect(new ChargeIndicator(npc.getXPos(), npc.getYPos()));
						
						// We hit an NPC
						isEndHit = true;
						isNPCHit = true;
						break;
					}
				}
				
				// Then check for walls or OOB
				try {
					isEndHit = MovableType.isWall(GameScreen.getTile(nextX, nextY).getTileType().getMovableType());
				} catch (ArrayIndexOutOfBoundsException e) {
					// We went out-of-bounds
					isEndHit = true;
				}
				
				// Then update the next coordinates to check
				nextX += dx;
				nextY += dy;
				
			} // END While Loop ---------------------------------------------
			
			// Return whether or not we hit an NPC
			return isNPCHit;
			
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
