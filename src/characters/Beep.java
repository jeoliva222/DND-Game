package characters;

import java.util.Random;

import ai.IdleController;
import ai.LineDrawer;
import ai.PatrolPattern;
import effects.DamageIndicator;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

// Class that represents 'Beep' enemy
public class Beep extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -1509982012963799316L;
	
	// Modifiers/Statistics
	
	private int MAX_HP = 2;
	
	private int MIN_DMG = 1;
	private int MAX_DMG = 1;
	
	private double CRIT_CHANCE = 0.2;
	private double CRIT_MULT = 2.0;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREP = 1;
	private static final int STATE_ATT = 2;
	
	//----------------------------
	
	// Additional parameters
	
	protected int cooldownCount = 0;
	protected final int cooldownMax = 2;
	
	
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BEEP);
	private String bpImage_base = "beep";
	
	private String beImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BEEP, "beep_dead.png");
	private String beImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BEEP, "beep_dead_CRIT.png");

	// Constructors
	public Beep(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Beep.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.imagePath = this.getImage();
	}
	
	public Beep(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Beep.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Beep";
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
			return GPath.NULL;
		}
		
		switch(this.state) {
		case Beep.STATE_IDLE:
			// No extra path
			break;
		case Beep.STATE_PREP:
			statePath = "_PREP";
			break;
		case Beep.STATE_ATT:
			if(this.cooldownCount < 1) {
				statePath = "_ATT";
			}
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if(this.currentHP < -(this.maxHP)) {
			return this.beImage_DEAD_CRIT;
		} else {
			return this.beImage_DEAD;
		}
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.WATER);
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		if(this.currentHP < -(this.maxHP)) {
			SoundPlayer.playWAV(GPath.createSoundPath("Bitester_DEATH_CRIT.wav"));
		} else {
			this.playDeathSound();
		}
	}

	@Override
	public void takeTurn() {
		
		// Fetch the player for easy reference
		Player player = EntityManager.getPlayer();
		
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
			case Beep.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"));
					this.state = Beep.STATE_PREP;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Beep.STATE_PREP:
				// Attack if player is in one tile radius around player
				if(Math.abs(distX) <= 1 && Math.abs(distY) <= 1) {
					// Mark tiles with damage indicators
					EntityManager.getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
					this.playerInitiate();
				} else {
					// If not attacking the player, hop closer to them
					
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
					
					// First, try to move diagonal
					// If unsuccessful, move in the direction that it is
					// further from the player.
					if(!this.moveCharacter(dx, dy)) {
						if((Math.abs(distX)) > (Math.abs(distY))) {
							// If movement in the x direction fails, try the y direction
							if(!this.moveCharacter(dx, 0)) {
								this.moveCharacter(0, dy);
							}
						} else {
							// If movement in the y direction fails, try the x direction
							if(!this.moveCharacter(0, dy)) {
								this.moveCharacter(dx, 0);
							}
						}
					}
					
				}
				
				// Play hop sound
				this.playHopSound();

				this.state = Beep.STATE_ATT;
				break;
			case Beep.STATE_ATT:
				// Cooldown period
				
				// Increment cooldown counter
				this.cooldownCount += 1;
				
				// Check if we've completed our cooldown
				if(this.cooldownCount >= this.cooldownMax) {
					// If so, switch to preparation state
					this.cooldownCount = 0;
					this.state = Beep.STATE_PREP;
				} else {
					// If not, do nothing
				}
				
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}

	}
	
	protected void playHopSound() {
		Random r = new Random();
		int whichSound = r.nextInt(4);
		if(whichSound == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("beep_hop1.wav"), -10);
		} else if(whichSound == 1) {
			SoundPlayer.playWAV(GPath.createSoundPath("beep_hop2.wav"), -10);
		} else if(whichSound == 2) {
			SoundPlayer.playWAV(GPath.createSoundPath("beep_hop3.wav"), -10);
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("beep_hop4.wav"), -10);
		}
	}
	
	protected void playDeathSound() {
		Random r = new Random();
		int whichSound = r.nextInt(3);
		if(whichSound == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("beep_death1.wav"));
		} else if(whichSound == 1) {
			SoundPlayer.playWAV(GPath.createSoundPath("beep_death2.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("beep_death3.wav"));
		}
	}
}
