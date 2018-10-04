package weapons;

import characters.GCharacter;
import characters.Player;
import gui.GameScreen;
import helpers.GColors;
import managers.EntityManager;
import tiles.TileType;

// Class representing 'Dagger' type weapons which can appear in-game
public class Dagger extends Weapon {

	// Serialization ID
	private static final long serialVersionUID = 1198769164795197369L;

	public Dagger(String name, int minDmg, int maxDmg,
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
		
		// Fetch reference to the player
		Player player = em.getPlayer();
		
		for(GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, complete attack
			if((player.getXPos() + dx) == npc.getXPos()
					&& (player.getYPos() + dy) == npc.getYPos()) {
				if(this.isCharged) {
					// First, discharge weapon
					this.dischargeWeapon();
					
					// Fetch tile the targeted enemy is on
					TileType tt = GameScreen.getTile(player.getXPos() + dx, player.getYPos() + dy).getTileType();
					
					if(player.getMoveTypes().contains(tt.getMovableType()) &&
							player.leapPlayer(player.getXPos() + (dx*2), player.getYPos() + (dy*2))) {
						///*** THIS LINE SOMETIMES CAUSES GRAPHICAL BUGS TODO
						//tt.onStep();
						
						// If space to backstab, deal modified damage and steb behind target
						int dmg = this.calculateDamage(this.chargeMult, npc);
						npc.damageCharacter(dmg);
						this.sendToLog("Player backstabbed "+ npc.getName() +" and dealt " + Integer.toString(dmg)
						+ " damage.", GColors.ATTACK, npc);
					} else {
						// If no space to backstab, deal normal damage and attack normally
						int dmg = this.calculateDamage(npc);
						npc.damageCharacter(dmg);
						this.sendToLog("Player cut "+ npc.getName() +" and dealt " + Integer.toString(dmg)
						+ " damage.", GColors.ATTACK, npc);
					}
				} else {
					// If not charged deal normal damage and attack normally
					int dmg = this.calculateDamage(npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player cut "+ npc.getName() +" and dealt " + Integer.toString(dmg)
						+ " damage.", GColors.ATTACK, npc);
				}
				// We hit something, so return true
				this.playSwingSound();
				return true;
			}
			
		}
		
		// If we hit nothing, return false
		return false;
	}

}
