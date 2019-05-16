package weapons.special;

import characters.GCharacter;
import effects.ChargeIndicator;
import gui.GameScreen;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import managers.EntityManager;
import tiles.MovableType;
import weapons.Weapon;

// Class representing the special weapon: The Injector
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
	}
	
	@Override
	public boolean tryAttack(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		if(this.isCharged) {
			// First, discharge weapon
			this.dischargeWeapon();
			
			// Checks if we hit at least one target
			boolean foundTarget = false;
			
			// Number of targets killed (Used for lifesteal)
			int targetsKilled = 0;
			
			// Checks if immediately adjacent space is a wall
			boolean nextToWall;
			try {
			nextToWall = GameScreen
					.getTile((em.getPlayer().getXPos() + dx),
							(em.getPlayer().getYPos() + dy))
					.getTileType()
					.getMovableType() == MovableType.WALL;
			} catch (ArrayIndexOutOfBoundsException e) {
				nextToWall = true;
			}
			
			// If charged, check for NPCs two spaces away from player to attack
			for(GCharacter npc : em.getNPCManager().getCharacters()) {
				// First check for immediately adjacent positions to attack
				if((em.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
					int dmg = this.calculateDamage(this.chargeMult, npc);
					if(!npc.damageCharacter(dmg)) targetsKilled++;
					this.sendToLog("Player siphoned and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);				
					foundTarget = true;
				// Then check for positions two tiles away to attack
				} else if((em.getPlayer().getXPos() + (dx*2)) == npc.getXPos()
						&& (em.getPlayer().getYPos() + (dy*2)) == npc.getYPos()) {
					// Check that previous space isn't a wall
					if(!nextToWall) {
						// Deal multiplier on regular damage and discharge weapon
						int dmg = this.calculateDamage(this.chargeMult, npc);
						if(!npc.damageCharacter(dmg)) targetsKilled++;
						this.sendToLog("Player siphoned and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);				
						foundTarget = true;
					}
				}
			}
			
			// If we found a target, return true and mark tiles with attack effects
			if(foundTarget) {
				em.getEffectManager()
					.addEffect(new ChargeIndicator(em.getPlayer().getXPos() + dx,
							em.getPlayer().getYPos() + dy));
				if(!nextToWall) {
					// Only mark 2nd tile if we're not attacking through a wall
					em.getEffectManager()
						.addEffect(new ChargeIndicator(em.getPlayer().getXPos() + (dx*2),
								em.getPlayer().getYPos() + (dy*2)));
				}
				
				// Apply lifesteal from kills if we killed at least one target
				if(targetsKilled > 0) {
					// Heal player and log result
					em.getPlayer().healPlayer(targetsKilled);
					LogScreen.log("Player drank " + Integer.toString(targetsKilled)
					+ " health from his enemies.", GColors.HEAL);
				}
				
				// Play attack sound and return true to indicate we hit something
				this.playSwingSound();
				return true;
			}
		} else {
			// If not charged, check for immediately adjacent NPCs to attack
			for(GCharacter npc : em.getNPCManager().getCharacters()) {
				if((em.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage
					int dmg = this.calculateDamage(npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player stabbed and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);	
					this.playSwingSound();
					return true;
				}
			}
		}

		// If nothing connects, return false
		return false;
	}
	
}
