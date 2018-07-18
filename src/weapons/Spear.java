package weapons;

import characters.GCharacter;
import effects.ChargeIndicator;
import gui.GameScreen;
import gui.LogScreen;
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
		if(this.isCharged) {
			// Checks if we hit at least one target
			boolean foundTarget = false;
			
			// Checks if immediately adjacent space is a wall
			boolean nextToWall;
			try {
			nextToWall = GameScreen
					.getTile((EntityManager.getPlayer().getXPos() + dx),
							(EntityManager.getPlayer().getYPos() + dy))
					.getTileType()
					.getMovableType() == MovableType.WALL;
			} catch (ArrayIndexOutOfBoundsException e) {
				nextToWall = true;
			}
			
			// If charged, check for NPCs two spaces away from player to attack
			for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
				// First check for immediately adjacent positions to attack
				if((EntityManager.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (EntityManager.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// If not at proper range, don't do multiplier, but discharge weapon
					this.dischargeWeapon();
					int dmg = this.calculateDamage();
					npc.damageCharacter(dmg);
					LogScreen.log("Player lanced forward and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK);				
					foundTarget = true;
				// Then check for positions two tiles away to attack
				} else if((EntityManager.getPlayer().getXPos() + (dx*2)) == npc.getXPos()
						&& (EntityManager.getPlayer().getYPos() + (dy*2)) == npc.getYPos()) {
					// Check that previous space isn't a wall
					if(!nextToWall) {
						// Deal multiplier on regular damage and discharge weapon
						this.dischargeWeapon();
						int dmg = this.calculateDamage(this.chargeMult);
						npc.damageCharacter(dmg);
						LogScreen.log("Player lanced forward and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK);				
						foundTarget = true;
					}
				}
			}
			
			// If we found a target, return true and mark tiles with attack effects
			if(foundTarget) {
				EntityManager.getEffectManager()
					.addEffect(new ChargeIndicator(EntityManager.getPlayer().getXPos() + dx,
							EntityManager.getPlayer().getYPos() + dy));
				if(!nextToWall) {
					// Only mark 2nd tile if we're not attacking through a wall
					EntityManager.getEffectManager()
						.addEffect(new ChargeIndicator(EntityManager.getPlayer().getXPos() + (dx*2),
								EntityManager.getPlayer().getYPos() + (dy*2)));
				}
				this.playSwingSound();
				return true;
			}
		} else {
			// If not charged, check for immediately adjacent NPCs to attack
			for(GCharacter npc : EntityManager.getNPCManager().getCharacters()) {
				if((EntityManager.getPlayer().getXPos() + dx) == npc.getXPos()
						&& (EntityManager.getPlayer().getYPos() + dy) == npc.getYPos()) {
					// If not charged deal normal damage
					int dmg = this.calculateDamage();
					npc.damageCharacter(dmg);
					LogScreen.log("Player stabbed and dealt " + Integer.toString(dmg)
						+ " damage to " + npc.getName() + ".", GColors.ATTACK);	
					this.playSwingSound();
					return true;
				}
			}
		}

		// If nothing connects, return false
		return false;
	}

}
