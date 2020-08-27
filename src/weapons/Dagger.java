package weapons;

import characters.GCharacter;
import characters.allies.Player;
import gui.GameScreen;
import helpers.GColors;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

/**
 * Class representing 'Dagger' type weapons which can appear in-game
 * @author jeoliva
 */
public class Dagger extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = 1198769164795197369L;

	public Dagger(String name, int minDmg, int maxDmg, double critChance, double critMult,
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
		
		// Fetch reference to the player
		Player player = em.getPlayer();
		
		for (GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, complete attack
			if ((player.getXPos() + dx) == npc.getXPos() && (player.getYPos() + dy) == npc.getYPos()) {
				if (isCharged && player.checkEnergy(chargeExhaust)) {
					// Fetch tile the targeted enemy is on
					TileType tt = GameScreen.getTile(player.getXPos() + dx, player.getYPos() + dy).getTileType();
					
					// Exhaust the player
					player.exhaustPlayer(chargeExhaust);
					
					if (MovableType.canMove(player.getMoveTypes(), tt.getMovableType()) &&
							player.leapPlayer(player.getXPos() + (dx*2), player.getYPos() + (dy*2))) {
						// If space to backstab, deal modified damage and step behind target
						int dmg = calculateDamage(chargeMult, npc);
						npc.damageCharacter(dmg);
						sendToLog("Player backstabbed "+ npc.getName() +" and dealt " + Integer.toString(dmg)
								+ " damage.", GColors.ATTACK, npc);
					} else {
						// If no space to backstab, deal normal damage and attack normally
						int dmg = calculateDamage(npc);
						npc.damageCharacter(dmg);
						sendToLog("Player cut "+ npc.getName() +" and dealt " + Integer.toString(dmg)
								+ " damage.", GColors.ATTACK, npc);
					}
				} else {
					// If not charged deal normal damage and attack normally
					int dmg;
					if (player.exhaustPlayer(attackExhaust)) {
						dmg = calculateDamage(npc);
					} else {
						dmg = calculateDamage(EXHAUST_MULT, npc);
					}
					npc.damageCharacter(dmg);
					sendToLog("Player cut "+ npc.getName() +" and dealt " + Integer.toString(dmg)
							+ " damage.", GColors.ATTACK, npc);
				}
				
				// We hit something, so return true
				playSwingSound();
				return true;
			}
		}
		
		// If we hit nothing, return false
		return false;
	}

}
