package characters;

import java.util.Random;

import ai.IdleController;
import ai.LineDrawer;
import ai.PatrolPattern;
import characters.allies.Player;
import effects.DamageIndicator;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

// Represents the 'Sand Beep' enemy in-game in the 'Poacher's Desert' area
public class SandBeep extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 4702359470958492375L;

	// Modifiers/Statistics
	
	private int MAX_HP = 6;
	
	private int MIN_DMG = 2;
	private int MAX_DMG = 4;
	
	private double CRIT_CHANCE = 0.1;
	private double CRIT_MULT = 1.5;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREP = 1;
	private static final int STATE_ATT = 2;
	
	//----------------------------
	
	// Additional parameters
	
	protected int cooldownCount = 0;
	protected final int cooldownMax = 2;
	
	// Marks whether the Beep will do a double hop next time it moves
	protected boolean doExtraHop = false;
	
	// Marked direction of hop
	// Used when doing a double hop
	protected int markedX;
	protected int markedY;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SAND_BEEP);
	private String bpImage_base = "sand_beep";
	
	private String beImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SAND_BEEP, "sand_beep_dead.png");
	private String beImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.SAND_BEEP, "sand_beep_dead_CRIT.png");

	// Constructors
	public SandBeep(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SandBeep.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.imagePath = this.getImage();
	}
	
	public SandBeep(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SandBeep.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Sand Beep";
	}
	
	@Override
	public String getImage() {
		// TODO : Needs custom sprites
		String imgPath = this.imageDir + this.bpImage_base;
		String hpPath = "";
		String statePath = "";
		String hopPath = "";
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return GPath.NULL;
		}
		
		if(this.doExtraHop) {
			hopPath = "_ALT";
		}
		
		switch(this.state) {
			case SandBeep.STATE_IDLE:
				// No extra path
				break;
			case SandBeep.STATE_PREP:
				statePath = "_PREP";
				break;
			case SandBeep.STATE_ATT:
				if(this.cooldownCount < 1) {
					statePath = "_ATT";
				}
				break;
			default:
				System.out.println
					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
				return GPath.NULL;
			}
		
		return (imgPath + hpPath + statePath + hopPath + ".png");
	}
	
	public String getCorpseImage() {
		if(this.currentHP < -(this.maxHP / 2)) {
			return this.beImage_DEAD_CRIT;
		} else {
			return this.beImage_DEAD;
		}
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_WATER)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		if(this.currentHP < -(this.maxHP / 2)) {
			SoundPlayer.playWAV(GPath.createSoundPath("Bitester_DEATH_CRIT.wav"));
		} else {
			this.playDeathSound();
		}
	}

	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		this.cooldownCount = 0;
		this.doExtraHop = false;
	}
	
	@Override
	public void takeTurn() {
		
		// Fetch the player for easy reference
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
			case SandBeep.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"));
					this.state = SandBeep.STATE_PREP;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case SandBeep.STATE_PREP:				
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
				
				// Get relative location to player
				distX = plrX - this.xPos;
				distY = plrY - this.yPos;
				
				// Initialize flag to indicate whether we've attack this turn
				boolean didAttack = false;
				
				// Attack if player is in one tile radius around player
				if((this.xPos + dx) == plrX && (this.yPos + dy) == plrY) {
					// Mark tiles with damage indicators
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
					this.playerInitiate();
					
					// Flip flag to indicate we've attacked this turn
					didAttack = true;
				} else {
					// If not attacking the player, hop closer to them
					
					// First, try to move diagonal
					// If unsuccessful, move in the direction that it is
					// further from the player.
					// Only does this on the first hop of the turn
					if(this.moveCharacter(dx, dy)) {
						this.markedX = dx;
						this.markedY = dy;
					} else if((Math.abs(distX)) > (Math.abs(distY))) {
							// If movement in the x direction fails, try the y direction
						if(this.moveCharacter(dx, 0)) {
							this.markedX = dx;
							this.markedY = 0;
						} else if (this.moveCharacter(0, dy)) {
							this.markedX = 0;
							this.markedY = dy;
						}
					} else {
						// If movement in the y direction fails, try the x direction
						if(this.moveCharacter(0, dy)) {
							this.markedX = 0;
							this.markedY = dy;
						} else if(this.moveCharacter(dx, 0)) {
							this.markedX = dx;
							this.markedY = 0;
						}
					}
				}
				
				// Do a second hop if we're queued up for it and haven't attacked this turn
				if(this.doExtraHop && !didAttack) {
					// Attack if player is our current hop path
					if((this.xPos + this.markedX) == plrX && (this.yPos + this.markedY) == plrY) {
						// Mark tiles with damage indicators
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
						this.playerInitiate();
					} else {
						// If we don't hit the payer, continue on our current hop path
						this.moveCharacter(this.markedX, this.markedY);
					}
				}
				
				// Play hop sound
				this.playHopSound();

				// Change state
				this.state = SandBeep.STATE_ATT;
				break;
			case SandBeep.STATE_ATT:
				// Cooldown period
				
				// Increment cooldown counter
				this.cooldownCount += 1;
				
				// Check if we've completed our cooldown
				if(this.cooldownCount >= this.cooldownMax) {
					// If so, switch to preparation state
					this.cooldownCount = 0;
					this.state = SandBeep.STATE_PREP;
				} else {
					// If not, toggle extra hop parameter
					this.doExtraHop = !(this.doExtraHop);
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
