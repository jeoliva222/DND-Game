package characters;

import java.util.Random;

import ai.PatrolPattern;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

public class TormentedSoul extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -206016404982284368L;
	
	// Modifiers/Statistics

	private int MAX_HP = 16;
	
	private int MIN_DMG = 0;
	private int MAX_DMG = 0;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_FREAK_OUT = 2;
	
	//----------------------------
	
	// Additional parameters
	
	// Indicate whether soul should try to move
	private boolean shouldMove = false;

	// Flag indicating which step soul is on
	private boolean whichStep = false;
	
	//----------------------------
	
	// File paths to images TODO
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.TORMENTED_SOUL);
	private String tsImage_base = "";
	
	private String tsImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.TORMENTED_SOUL, (this.tsImage_base + "_dead.png"));


	public TormentedSoul(int startX, int startY, String imageBase) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = TormentedSoul.STATE_IDLE;
		this.patrolPattern = PatrolPattern.STATIONARY;
		
		this.tsImage_base = imageBase;
		
		// Can't focus on this at first
		this.canFocus = false;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Tormented Soul";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.tsImage_base;
		String hpPath = "";
		String statePath = "";
		
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_full";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch(this.state) {
		case TormentedSoul.STATE_IDLE:
			// No extra path
			break;
		case TormentedSoul.STATE_FREAK_OUT:
			if(whichStep) {
				statePath = "_STEP1";
			} else {
				statePath = "_STEP2";
			}
			break;
		case TormentedSoul.STATE_ALERTED:
			statePath = "_ALERT";
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		this.attackPlayer();
	}
	
	@Override
	public void onDeath() {
		int whichSound = new Random().nextInt(2);
		if(whichSound == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("Soul_Death1.wav"));
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("Soul_Death2.wav"));
		}
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
			case TormentedSoul.STATE_IDLE:
				if(this.currentHP != MAX_HP) {
					SoundPlayer.playWAV(GPath.createSoundPath("Soul_Cry1.wav"));
					this.canFocus = true;
					this.state = TormentedSoul.STATE_ALERTED;
				} else {
					// Don't move
				}
				break;
			case TormentedSoul.STATE_ALERTED:
				// Rest for one turn, making an anguished face
				// then transition to freaking out
				this.state = TormentedSoul.STATE_FREAK_OUT;
				break;
			case TormentedSoul.STATE_FREAK_OUT:	
				// Move around in eratic manner

				if(this.shouldMove) {
					Random r = new Random();
					// Gets a number 0 - 1: Determines if NPC should move or not
					int shouldMove = r.nextInt(2);
					
					// Most of the time, don't even try to move
					if(shouldMove != 0) {
						break;
					}
					
					// Only move 1/2 of the time
					
					// Gets 0 or 1
					int xOrY = r.nextInt(2);
					// Gets -1 or 1
					int posOrNeg = 1 + (-2 * r.nextInt(2));
					
					// Randomly move X-wise or Y-wise
					if(xOrY == 0) {
						//X-wise
						if(!this.moveCharacter(posOrNeg, 0)) {
							this.moveCharacter((-posOrNeg), 0);
						}
					} else {
						//Y-wise
						if(!this.moveCharacter(0, posOrNeg)) {
							this.moveCharacter(0, (-posOrNeg));
						}
					}
					
					// Toggle step animation
					this.whichStep = !this.whichStep;
					
					// Don't move next turn
					this.shouldMove = false;
				} else {
					// After resting one turn, we can move next turn
					this.shouldMove = true;
				}
				
				// Try to play a sound
				this.playSound();
				
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
	
	private void playSound() {
		// Initialize randomizer
		Random r = new Random();
		
		// Only play sounds 1/4 of the time
		int shouldPlay = r.nextInt(4);
		if(shouldPlay != 0) {
			return;
		}
		
		// Play one of the four crying sounds
		int whichSound = r.nextInt(4) + 2;
		SoundPlayer.playWAV(GPath.createSoundPath("Soul_Cry" + whichSound + ".wav"));
	}
	
}
