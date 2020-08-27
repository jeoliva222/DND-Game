package weapons;

import characters.GCharacter;
import characters.allies.Player;
import effects.ChargeIndicator;
import gui.GameScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

/**
 * Class that represents the 'Crossbow' weapons in-game
 * @author jeoliva
 */
public class Crossbow extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = -8718640333057697034L;

	// Constructor
	public Crossbow(String name, int minDmg, int maxDmg, double critChance, double critMult,
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
			// While we still have a open bullet path, check for NPCs to hit
			int nextX = (player.getXPos() + dx);
			int nextY = (player.getYPos() + dy);
			boolean isEndHit = false;
			boolean isNPCHit = false;
			while (!isEndHit) { // Begin While --------------------------------------
				// First check for NPCs to hit
				for (GCharacter npc : em.getNPCManager().getCharacters()) {
					if (nextX == npc.getXPos() && nextY == npc.getYPos()) {
						// Damage the NPC
						int dmg = calculateDamage(chargeMult, npc);
						npc.damageCharacter(dmg);
						sendToLog("Player sniped " + npc.getName() + " and dealt "
								+ Integer.toString(dmg) + " damage.", GColors.ATTACK, npc);
						
						// Play arrow firing sound
						SoundPlayer.playWAV(GPath.createSoundPath("arrow_SHOT.wav"));
						
						// Add on-hit effect
						em.getEffectManager().addEffect(new ChargeIndicator(npc.getXPos(), npc.getYPos()));
						
						// Exhaust the player
						player.exhaustPlayer(chargeExhaust);
						
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
