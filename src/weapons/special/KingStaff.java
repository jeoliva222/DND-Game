package weapons.special;

import characters.GCharacter;
import effects.ChargeIndicator;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.KingStaffFlame;
import weapons.Weapon;

// Class representing the special weapon: King's Staff
public class KingStaff extends Weapon {
	
	// Serialization ID
	private static final long serialVersionUID = 5367784600352180602L;

	// Constructor
	public KingStaff() {
		super("King's Staff",
				"WEAPON (Special): A kingly staff of the elements. Occasional omits firey blasts!",
				GPath.createImagePath(GPath.TILE, GPath.GENERIC, "testProj.png"));
		
		// Set damage attributes
		this.minDmg = 1;
		this.maxDmg = 3;
		this.critChance = 0.15;
		this.critMult = 1.7;
		this.chargeMult = 1.0;
	}
	
	@Override
	public boolean tryAttack(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		for(GCharacter npc : em.getNPCManager().getCharacters()) {
			// If we're attacking at an NPC's position, complete attack
			if((em.getPlayer().getXPos() + dx) == npc.getXPos()
					&& (em.getPlayer().getYPos() + dy) == npc.getYPos()) {
				if(this.isCharged) { // CHARGED ----------------------------------
					// First, discharge weapon
					this.dischargeWeapon();
					
					// Determine randomly whether to spawn flames
					if(Math.random() < this.critChance) { // FLAME ---------------
						// Fire a flame in the direction the player attacked
						em.getProjectileManager()
							.addProjectile(new KingStaffFlame((em.getPlayer().getXPos() + dx),
													(em.getPlayer().getYPos() + dy),
													dx,
													dy, null));
						
						// Fire a flame to the relative left of the player's attack
						em.getProjectileManager()
							.addProjectile(new KingStaffFlame((em.getPlayer().getXPos() + dx - Math.abs(dy)),
													(em.getPlayer().getYPos() + dy - Math.abs(dx)),
													dx,
													dy, null));
						
						// Fire a flame to the relative right of the player's attack
						em.getProjectileManager()
							.addProjectile(new KingStaffFlame((em.getPlayer().getXPos() + dx + Math.abs(dy)),
													(em.getPlayer().getYPos() + dy + Math.abs(dx)),
													dx,
													dy, null));
						
						// Play fire sound
						SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"));
					} else { // REGULAR ------------------------------------------
						// If charged deal extra damage with standard attack
						int dmg = this.calculateDamage(this.chargeMult, npc);
						npc.damageCharacter(dmg);
						this.sendToLog("Player swiped and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
						
						// Mark location with effect
						em.getEffectManager().addEffect(new ChargeIndicator(em.getPlayer().getXPos() + dx, em.getPlayer().getYPos() +dy));
						
						// Then, deal damage to adjacent foes
						this.findSwipeTargets(dx, dy);
						
						// Play swipe sound
						this.playSwingSound();
					}
					

					
					
				} else { // NOT CHARGED ------------------------------------------
					
					// Determine randomly whether to spawn flame
					if(Math.random() < this.critChance) { // FLAME ---------------
						// Fire a flame in the direction the player attacked
						em.getProjectileManager()
							.addProjectile(new KingStaffFlame((em.getPlayer().getXPos() + dx),
													(em.getPlayer().getYPos() + dy),
													dx,
													dy, null));
						
						// Play fire sound
						SoundPlayer.playWAV(GPath.createSoundPath("fire_ATT.wav"));
						
					} else { // REGULAR ------------------------------------------
						// If not charged deal normal damage and attack normally
						int dmg = this.calculateDamage(npc);
						npc.damageCharacter(dmg);
						this.sendToLog("Player slashed and dealt " + Integer.toString(dmg)
							+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
						
						// Play swipe sound
						this.playSwingSound();
					}
				
				}
				// We hit something, so return true
				return true;
			}
			
		}
		
		// If we hit nothing, return false
		return false;
	}
	
	// Finds the swipe target coordinates, marks the tiles, then damages characters
	public void findSwipeTargets(int dx, int dy) {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Define attack coordinates
		int xPos1 = (em.getPlayer().getXPos() + dx + Math.abs(dy));
		int yPos1 = (em.getPlayer().getYPos() + dy + Math.abs(dx));
		int xPos2 = (em.getPlayer().getXPos() + dx - Math.abs(dy));
		int yPos2 = (em.getPlayer().getYPos() + dy - Math.abs(dx));
		
		// Mark damaged spots
		em.getEffectManager().addEffect(new ChargeIndicator(xPos1, yPos1));
		em.getEffectManager().addEffect(new ChargeIndicator(xPos2, yPos2));	
		
		// Damage affected characters
		for(GCharacter npc : em.getNPCManager().getCharacters()) {
			if((xPos1 == npc.getXPos() && yPos1 == npc.getYPos()) ||
					(xPos2 == npc.getXPos() && yPos2 == npc.getYPos())) {
				// Damage target and log result
				int dmg = this.calculateDamage(this.chargeMult, npc);
				npc.damageCharacter(dmg);
				this.sendToLog("Player swiped and dealt " + Integer.toString(dmg)
				+ " damage to " + npc.getName() + ".", GColors.ATTACK, npc);
			}
		}
	}

}
