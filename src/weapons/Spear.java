package weapons;

import characters.GCharacter;
import effects.ChargeIndicator;
import gui.GameScreen;
import helpers.GColors;
import managers.EntityManager;
import tiles.MovableType;

// Spears are weapons with a special mid-ranged charge attack
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
		
		if(this.isCharged) {
			// Checks if we hit at least one target
			boolean foundTarget = false;
			
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
					// If not at proper range, don't do multiplier, but discharge weapon
					this.dischargeWeapon();
					int dmg = this.calculateDamage(npc);
					npc.damageCharacter(dmg);
					this.sendToLog("Player lanced forward and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);				
					foundTarget = true;
				// Then check for positions two tiles away to attack
				} else if((em.getPlayer().getXPos() + (dx*2)) == npc.getXPos()
						&& (em.getPlayer().getYPos() + (dy*2)) == npc.getYPos()) {
					// Check that previous space isn't a wall
					if(!nextToWall) {
						// Deal multiplier on regular damage and discharge weapon
						this.dischargeWeapon();
						int dmg = this.calculateDamage(this.chargeMult, npc);
						npc.damageCharacter(dmg);
						this.sendToLog("Player lanced forward and dealt " + Integer.toString(dmg)
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
