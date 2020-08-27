package weapons;

import characters.GCharacter;
import characters.allies.Player;
import effects.ChargeIndicator;
import gui.GameScreen;
import gui.GameTile;
import helpers.GColors;
import managers.EntityManager;
import tiles.MovableType;

/**
 * Class representing the 'Spear' type weapons in-game.
 * Spears are weapons with that can attack from a range with charged stabs.
 * @author jeoliva
 */
public class Spear extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = -2142487932348659953L;

	public Spear(String name, int minDmg, int maxDmg, double critChance, double critMult,
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
			// Checks if we hit at least one target
			boolean foundTarget = false;
			
			// Checks if immediately adjacent space is a wall
			boolean nextToWall;
			try {
				GameTile tile = GameScreen.getTile((player.getXPos() + dx), (player.getYPos() + dy));
				nextToWall = MovableType.isWall(tile.getTileType().getMovableType());
			} catch (ArrayIndexOutOfBoundsException e) {
				nextToWall = true;
			}
			
			// If charged, check for NPCs two spaces away from player to attack
			for (GCharacter npc : em.getNPCManager().getCharacters()) {
				// Check for enemies either one or two tiles away to attack
				if ((player.getXPos() + dx) == npc.getXPos() && (player.getYPos() + dy) == npc.getYPos()) {
					// Deal multiplier on regular damage
					int dmg = calculateDamage(chargeMult, npc);
					npc.damageCharacter(dmg);
					sendToLog("Player lanced forward and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);				
					foundTarget = true;
				} else if ((player.getXPos() + (dx*2)) == npc.getXPos() && (player.getYPos() + (dy*2)) == npc.getYPos()) {
					// Check that previous space isn't a wall
					if (!nextToWall) {
						// Deal multiplier on regular damage
						int dmg = calculateDamage(chargeMult, npc);
						npc.damageCharacter(dmg);
						sendToLog("Player lanced forward and dealt " + Integer.toString(dmg)
								+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);				
						foundTarget = true;
					}
				}
			}
			
			// If we found a target, return true and mark tiles with attack effects
			if (foundTarget) {
				// Mark first tile
				em.getEffectManager().addEffect(new ChargeIndicator(player.getXPos() + dx, player.getYPos() + dy));
				
				// Only mark second tile if we're not attacking through a wall
				if (!nextToWall) {
					em.getEffectManager().addEffect(new ChargeIndicator(player.getXPos() + (dx*2), player.getYPos() + (dy*2)));
				}
				
				// Exhaust the player
				player.exhaustPlayer(chargeExhaust);
				
				// Play attack sound and return true to indicate successful hit
				playSwingSound();
				return true;
			}
		} else {
			// If not charged, check for immediately adjacent NPCs to attack
			for (GCharacter npc : em.getNPCManager().getCharacters()) {
				if ((player.getXPos() + dx) == npc.getXPos() && (player.getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage
					int dmg;
					if (player.exhaustPlayer(attackExhaust)) {
						dmg = calculateDamage(npc);
					} else {
						dmg = calculateDamage(EXHAUST_MULT, npc);
					}
					npc.damageCharacter(dmg);
					sendToLog("Player stabbed and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);	
					playSwingSound();
					return true;
				}
			}
		}

		// If nothing connects, return false
		return false;
	}

}
