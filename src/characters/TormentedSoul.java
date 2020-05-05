package characters;

import java.util.Random;

import ai.PatrolPattern;
import characters.allies.Player;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

/**
 * Class that represents the Tormented Soul character
 * @author jeoliva
 */
public class TormentedSoul extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -206016404982284368L;
	
	// Modifiers/Statistics

	private static int MAX_HP = 10;
	
	private static int MIN_DMG = 0;
	private static int MAX_DMG = 0;
	
	private static double CRIT_CHANCE = 0.0;
	private static double CRIT_MULT = 1.0;
	
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
	
	// Cooldown counter to ensure soul doesn't spam sounds
	private int soundCooldown = 0;
	
	// Number of turns to not play a sound after playing a sound
	private final int soundCooldownMax = 3;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.TORMENTED_SOUL);
	
	// Non-static image file paths
	private String tsImage_base = "";
	private String tsImage_DEAD = "";


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
		this.tsImage_DEAD = (imageDir + tsImage_base + "_dead.png");
		
		// Can't focus on this at first
		this.canFocus = false;
	}
	
	public String getName() {
		return "Tormented Soul";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + tsImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (currentHP > 0) {
			hpPath = "_full";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch (state) {
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
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		return this.tsImage_DEAD;
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
	}

	@Override
	public void playerInitiate() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"));
		attackPlayer();
	}
	
	@Override
	public void onDeath() {
		SoundPlayer.playWAV(GPath.createSoundPath("Soul_Death1.wav"));
	}

	@Override
	public void takeTurn() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if (!isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		switch(this.state) {
			case TormentedSoul.STATE_IDLE:
				if (currentHP != MAX_HP) {
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
				// Move around in erratic manner
				if (shouldMove) {
					Random r = new Random();
					// Gets a number 0 - 1: Determines if NPC should move or not
					int shouldMove = r.nextInt(2);
					
					// Most of the time, don't even try to move
					if (shouldMove != 0) {
						break;
					}
					
					// Only move 1/2 of the time
					
					// Gets 0 or 1
					int xOrY = r.nextInt(2);
					// Gets -1 or 1
					int posOrNeg = 1 + (-2 * r.nextInt(2));
					
					// Randomly move X-wise or Y-wise
					if (xOrY == 0) {
						// X-wise
						if (!moveCharacter(posOrNeg, 0)) {
							moveCharacter((-posOrNeg), 0);
						}
					} else {
						// Y-wise
						if (!moveCharacter(0, posOrNeg)) {
							moveCharacter(0, (-posOrNeg));
						}
					}
					
					// Toggle step animation
					this.whichStep = !(whichStep);
					
					// Don't move next turn
					this.shouldMove = false;
				} else {
					// After resting one turn, we can move next turn
					this.shouldMove = true;
				}
				
				// Try to play a sound
				playSound();
				
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}
	
	private void playSound() {
		// Initialize randomizer
		Random r = new Random();
		
		// If we're on cooldown, decrement cooldown counter and don't play sound
		if (soundCooldown > 0) {
			this.soundCooldown += -1;
			return;
		}
		
		// Only play sounds 1/3 of the time if not on cooldown
		int shouldPlay = r.nextInt(3);
		if (shouldPlay != 0) {
			return;
		}
		
		// Set cooldown for three turns if we're playing a sound
		this.soundCooldown = soundCooldownMax;
		
		// Play one of the four crying sounds
		int whichSound = r.nextInt(4) + 2;
		SoundPlayer.playWAV(GPath.createSoundPath("Soul_Cry" + whichSound + ".wav"));
	}
	
}
