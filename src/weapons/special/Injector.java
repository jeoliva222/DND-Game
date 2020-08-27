package weapons.special;

import characters.GCharacter;
import characters.allies.Player;
import effects.ChargeIndicator;
import gui.GameScreen;
import gui.GameTile;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import managers.EntityManager;
import tiles.MovableType;
import weapons.Weapon;

/**
 * Class representing the special weapon: 'The Injector'
 * @author jeoliva
 */
public class Injector extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = 347577165375189843L;

	// Constructor
	public Injector() {
		super("The Injector",
				"WEAPON (Special): An unholy tool used by The Collector. Charge attacks are weak, but heal the player per successful kill.",
				GPath.createImagePath(GPath.PICKUP, GPath.WEAPON, "injector.png"));
		
		// Set damage attributes
		this.minDmg = 2;
		this.maxDmg = 4;
		this.critChance = 0.05;
		this.critMult = 1.5;
		this.chargeMult = 0.5;
		this.attackExhaust = 0;
		this.chargeExhaust = 8;
	}
	
	@Override
	public boolean tryAttack(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		Player player = em.getPlayer();
		
		if (isCharged && player.checkEnergy(chargeExhaust)) {
			// Checks if we hit at least one target
			boolean foundTarget = false;
			
			// Number of targets killed (Used for lifesteal)
			int targetsKilled = 0;
			
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
					int dmg = calculateDamage(chargeMult, npc);
					if (!npc.damageCharacter(dmg)) {
						targetsKilled++;
					}
					sendToLog("Player siphoned and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);				
					foundTarget = true;
				} else if ((player.getXPos() + (dx*2)) == npc.getXPos() && (player.getYPos() + (dy*2)) == npc.getYPos()) {
					// Check that previous space isn't a wall
					if (!nextToWall) {
						// Deal multiplier on regular damage and discharge weapon
						int dmg = calculateDamage(chargeMult, npc);
						if (!npc.damageCharacter(dmg)) {
							targetsKilled++;
						}
						sendToLog("Player siphoned and dealt " + Integer.toString(dmg)
								+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);				
						foundTarget = true;
					}
				}
			}
			
			// If we found a target, return true and mark tiles with attack effects
			if (foundTarget) {
				// Mark the first tile
				em.getEffectManager().addEffect(new ChargeIndicator(player.getXPos() + dx, player.getYPos() + dy));
				
				// Only mark 2nd tile if we're not attacking through a wall
				if (!nextToWall) {
					em.getEffectManager().addEffect(new ChargeIndicator(player.getXPos() + (dx*2), player.getYPos() + (dy*2)));
				}
				
				// Apply lifesteal from kills if we killed at least one target
				if (targetsKilled > 0) {
					// Heal player and log result
					player.healPlayer(targetsKilled);
					LogScreen.log("Player drank " + Integer.toString(targetsKilled)
							+ " health from his enemies.", GColors.HEAL);
				}
				
				// Exhaust the player
				player.exhaustPlayer(chargeExhaust);
				
				// Play attack sound and return true to indicate we hit something
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
