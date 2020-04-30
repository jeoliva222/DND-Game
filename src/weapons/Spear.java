package weapons;

import characters.GCharacter;
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

	public Spear(String name, int minDmg, int maxDmg,
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
			// First, discharge the weapon
			dischargeWeapon();
			
			// Checks if we hit at least one target
			boolean foundTarget = false;
			
			// Checks if immediately adjacent space is a wall
			boolean nextToWall;
			try {
				GameTile tile = GameScreen.getTile((em.getPlayer().getXPos() + dx), (em.getPlayer().getYPos() + dy));
				nextToWall = MovableType.isWall(tile.getTileType().getMovableType());
			} catch (ArrayIndexOutOfBoundsException e) {
				nextToWall = true;
			}
			
			// If charged, check for NPCs two spaces away from player to attack
			for (GCharacter npc : em.getNPCManager().getCharacters()) {
				// Check for enemies either one or two tiles away to attack
				if ((em.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// Deal multiplier on regular damage
					int dmg = calculateDamage(chargeMult, npc);
					npc.damageCharacter(dmg);
					sendToLog("Player lanced forward and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);				
					foundTarget = true;
				} else if ((em.getPlayer().getXPos() + (dx*2)) == npc.getXPos()
						&& (em.getPlayer().getYPos() + (dy*2)) == npc.getYPos()) {
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
				em.getEffectManager().addEffect(new ChargeIndicator(em.getPlayer().getXPos() + dx,
						em.getPlayer().getYPos() + dy));
				
				// Only mark second tile if we're not attacking through a wall
				if (!nextToWall) {
					em.getEffectManager().addEffect(new ChargeIndicator(em.getPlayer().getXPos() + (dx*2),
							em.getPlayer().getYPos() + (dy*2)));
				}
				
				// Play attack sound and return true to indicate successful hit
				playSwingSound();
				return true;
			}
		} else {
			// If not charged, check for immediately adjacent NPCs to attack
			for (GCharacter npc : em.getNPCManager().getCharacters()) {
				if ((em.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage
					int dmg = calculateDamage(npc);
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
