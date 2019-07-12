package characters;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import effects.DamageIndicator;
import effects.WarningIndicator;
import gui.LogScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

// Class representing the 'Angel' enemy, who serves to revive allies from the dead
public class Angel extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 8928094548353105482L;
	
	// Modifiers/Statistics

	private int MAX_HP = 15;
	
	private int MIN_DMG = 4;
	private int MAX_DMG = 6;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.4;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_ATT = 3;
	private static final int STATE_ATT = 4;
	private static final int STATE_PREP_REZ = 5;
	private static final int STATE_REZ = 6;
	
	//----------------------------
	
	// Additional parameters
	
	// Indicate which direction the NPC is launching its attack
	protected int markX = 0;
	protected int markY = 0;
	
	// Counter/Limit for number of turns spent resurrecting
	private int rezCount = 0;
	private final int rezMax = 3;
	
	// Corpse which Angel is trying to revive
	private Corpse corpse = null;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE);
	private String bpImage_base = "beanpole";
	
	private String bpImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead.png");
	private String bpImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead_CRIT.png");


	public Angel(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Angel.STATE_IDLE;
		this.patrolPattern = PatrolPattern.WANDER;
		
		this.imagePath = this.getImage();
	}
	
	public Angel(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Angel.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Angel";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.bpImage_base;
		String hpPath = "";
		String statePath = "";
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch(this.state) {
		case Angel.STATE_IDLE:
		case Angel.STATE_PURSUE:
			// No extra path
			break;
		case Angel.STATE_ALERTED:
		case Angel.STATE_PREP_ATT:
		case Angel.STATE_PREP_REZ:
			statePath = "_PREP";
			break;
		case Angel.STATE_ATT:
		case Angel.STATE_REZ:
			statePath = "_ATT";
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if(this.currentHP < -(this.maxHP / 2)) {
			return this.bpImage_DEAD_CRIT;
		} else {
			return this.bpImage_DEAD;
		}
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.WATER);
		this.moveTypes.add(MovableType.AIR);
		this.moveTypes.add(MovableType.ACID);
	}

	@Override
	public void playerInitiate() {
		// Play sound
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		
		// Attack player
		this.attackPlayer();
		
		// Lifesteal on attack
		int lifeSteal = 5;
		this.healCharacter(lifeSteal);
		LogScreen.log(this.getName() + " recovered " + lifeSteal + " health when attacking!");
	}
	
	@Override
	public void onDeath() {
		if(this.currentHP < -(this.maxHP / 2)) {
			SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH_CRIT.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH.wav"));
		}
	}
	
	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		this.rezCount = 0;
		this.corpse = null;
	}

	@Override
	public void takeTurn() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if(!this.isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = plrX - this.xPos;
		int distY = plrY - this.yPos;
		
		switch(this.state) {
			case Angel.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ALERT.wav"));
					this.state = Angel.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Angel.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = Angel.STATE_PURSUE;
				break;
			case Angel.STATE_PURSUE:
				// Relative movement direction (Initialize at 0)
				int dx = 0;
				int dy = 0;
				
				// Calculate relative movement directions
				if(distX > 0) {
					dx = 1;
				} else if (distX < 0) {
					dx = -1;
				}
				
				if(distY > 0) {
					dy = 1;
				} else if (distY < 0) {
					dy = -1;
				}
				
				// Change state to prep if next to player
				if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// Mark direction to attack next turn
					this.markX = dx;
					this.markY = dy;
					
					// Change states
					this.state = Angel.STATE_PREP_ATT;
					break;
				}
				
				// Fetch current corpse list
				ArrayList<Corpse> corpses = EntityManager.getInstance().getCorpseManager().getCorpses();
				
				// If corpse we're chasing has been removed, stop chasing it
				if (this.corpse != null && !corpses.contains(this.corpse)) {
					this.corpse = null;
				}
				
				// If we have no corpse to chase, try to find one
				if (this.corpse == null && corpses.size() > 0) {
					// Set sights on random corpse
					this.corpse = corpses.get(new Random().nextInt(corpses.size()));
				}
					
				if (corpse != null) {
					// CORPSE EXISTS ---------------------------------------------------------
					// Get relative location to position of corpse we want to revive
					int corpseDistX = this.corpse.getXPos() - this.xPos;
					int corpseDistY = this.corpse.getYPos() - this.yPos;
					
					// Calculate relative movement directions to get to corpse
					if(corpseDistX > 0) {
						dx = 1;
					} else if (corpseDistX < 0) {
						dx = -1;
					}
					
					if(corpseDistY > 0) {
						dy = 1;
					} else if (corpseDistY < 0) {
						dy = -1;
					}
					
					
					if(((Math.abs(corpseDistX) == 1) && (Math.abs(corpseDistY) == 0)) ||
							((Math.abs(corpseDistX) == 0) && (Math.abs(corpseDistY) == 1))) {
						// Change states to revive
						this.rezCount += 1;
						this.state = Angel.STATE_PREP_REZ;
					} else {
						// Path-find to the corpse if we can
						Dimension nextStep = PathFinder.findPath(this.xPos, this.yPos, this.corpse.getXPos(), this.corpse.getYPos(), this);
						if(nextStep == null) {
							// Blindly pursue the corpse
							System.out.println("CORPSE BLIND");
							DumbFollow.blindPursue(corpseDistX, corpseDistY, dx, dy, this);
						} else {
							int changeX = nextStep.width - this.xPos;
							int changeY = nextStep.height - this.yPos;
							
							// If we are on top of the corpse, then shift off to the side randomly
							if (changeX == 0 && changeY == 0) {
								this.shiftRandom();
							} else {
								this.moveCharacter(changeX, changeY);
							}
						}
					}
				} else {
					// NO CORPSE EXISTS ------------------------------------------------------
					// Path-find to the player if we can
					Dimension nextStep = PathFinder.findPath(this.xPos, this.yPos, plrX, plrY, this);
					if(nextStep == null) {
						// Blindly pursue the target
						DumbFollow.blindPursue(distX, distY, dx, dy, this);
					} else {
						int changeX = nextStep.width - this.xPos;
						int changeY = nextStep.height - this.yPos;
						this.moveCharacter(changeX, changeY);
					}
				}
				
				break;
			case Angel.STATE_PREP_ATT:
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(this.xPos + this.markX, this.yPos + this.markY));
				
				// Attack in marked direction
				if((this.xPos + this.markX) == plrX && (this.yPos + this.markY) == plrY)
					this.playerInitiate();
				this.state = Angel.STATE_ATT;
				break;
			case Angel.STATE_ATT:
				// Cooldown period for one turn
				this.state = Angel.STATE_PURSUE;
				break;
			case Angel.STATE_PREP_REZ:
				
				// If we have no corpse, go back to pursuit
				if (corpse == null) {
					this.rezCount = 0;
					this.state = Angel.STATE_PURSUE;
				}
				
				// If player is on corpse, switch to attack mode
				if(this.corpse.getXPos() == plrX && this.corpse.getYPos() == plrY) {
					this.rezCount = 0;
					
					this.markX = (plrX - this.xPos);
					this.markY = (plrY - this.yPos);
					
					this.state = Angel.STATE_PREP_ATT;
					break;
				}
				
				// Spend 'rezMax' turns doing the resurrection
				if (this.rezCount < this.rezMax) {
					this.rezCount += 1;
					break;
				}
				
				// Do resurrection
				if (corpse != null) {
					// Fetch EntityManager
					EntityManager em = EntityManager.getInstance();
					
					// If another NPC is on the corpse, then cancel the rez
					boolean npcBlock = false;
					for(GCharacter npc : em.getNPCManager().getCharacters()) {
						if(npc.getXPos() == this.corpse.getXPos() && npc.getYPos() == this.corpse.getYPos()) {
							npcBlock = true;
							break; // Breaks out of GCharacter loop
						}
					}
					
					if(npcBlock) {
						this.rezCount = 0;
						this.corpse = null;
						this.state = Angel.STATE_PURSUE;
						break; // Breaks out of State loop
					}
					
					// Mark tile with indicator
					em.getEffectManager().addEffect(new WarningIndicator(this.corpse.getXPos(), this.corpse.getYPos()));
					
					// Resurrect the corpse ----
					// Reset the corpse's NPC
					this.corpse.getNPC().fullyHeal();
					this.corpse.getNPC().state = 0;
					this.corpse.getNPC().resetParams();
					
					// Add character and then remove corpse
					em.getNPCManager().addPendingCharacter(this.corpse.getNPC());
					em.getCorpseManager().removeCorpse(this.corpse);
					
					// Log the result
					LogScreen.log(this.corpse.getNPC().getName() + " was resurrected!");
					
					// Reset rez parameters
					this.rezCount = 0;
					this.corpse = null;
				}
				
				// Change states
				this.state = Angel.STATE_REZ;
				break;
			case Angel.STATE_REZ:
				// Cooldown period for one turn
				this.state = Angel.STATE_PURSUE;
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
	private void shiftRandom() {
		Dimension[] moveSpots = new Dimension[] {
				new Dimension(1, 0),
				new Dimension(-1, 0),
				new Dimension(0, 1),
				new Dimension(0, -1)
		};
		
		// Shuffle the potential move coordinates
		Random r = new Random();
		for(int i = 3; i > 0; i--) {
			// Randomize swap index
			int shuffCoord = r.nextInt(i + 1);
			
			// Swap the spots
			Dimension temp = moveSpots[i];
			moveSpots[i] = moveSpots[shuffCoord];
			moveSpots[shuffCoord] = temp;
		}
		
		// Try to shift to the side
		for(int j = 0; j < moveSpots.length; j++) {
			if(this.moveCharacter(moveSpots[j].width, moveSpots[j].height)) {
				return;
			}
		}
	}
	
}
