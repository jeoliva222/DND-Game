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

/**
 * Class that represents Beep enemy
 * @author jeoliva
 */
public class Beep extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = -1509982012963799316L;
	
	// Modifiers/Statistics
	
	private static int MAX_HP = 2;
	
	private static int MIN_DMG = 1;
	private static int MAX_DMG = 1;
	
	private static double CRIT_CHANCE = 0.2;
	private static double CRIT_MULT = 2.0;
	
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
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BEEP);
	private static String bpImage_base = "beep";
	
	private static String beImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BEEP, "beep_dead.png");
	private static String beImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BEEP, "beep_dead_CRIT.png");

	// Constructors
	public Beep(int startX, int startY) {
		this(startX, startY, PatrolPattern.STATIONARY);
	}
	
	public Beep(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Beep.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Beep";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + bpImage_base);
		String hpPath = "";
		String statePath = "";
		
		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return GPath.NULL;
		}
		
		switch (state) {
			case Beep.STATE_IDLE:
				// No extra path
				break;
			case Beep.STATE_PREP:
				statePath = "_PREP";
				break;
			case Beep.STATE_ATT:
				if (cooldownCount < 1) {
					statePath = "_ATT";
				}
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if (currentHP < -(maxHP)) {
			return beImage_DEAD_CRIT;
		} else {
			return beImage_DEAD;
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
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ATTACK.wav"), getXPos(), getYPos());
		attackPlayer();
	}
	
	@Override
	public void onDeath() {
		if (currentHP < -(maxHP)) {
			SoundPlayer.playWAV(GPath.createSoundPath("Bitester_DEATH_CRIT.wav"), getXPos(), getYPos());
		} else {
			playDeathSound();
		}
	}
	
	// Override that resets a few extra parameters
	@Override
	public void resetParams() {
		this.cooldownCount = 0;
	}

	@Override
	public void takeTurn() {
		
		// Fetch the player for easy reference
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if (!isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = (plrX - xPos);
		int distY = (plrY - yPos);
		
		switch (state) {
			case Beep.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"), getXPos(), getYPos());
					this.state = Beep.STATE_PREP;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Beep.STATE_PREP:
				// Attack if player is in one tile radius around player
				if (Math.abs(distX) <= 1 && Math.abs(distY) <= 1) {
					// Mark tiles with damage indicators
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
					playerInitiate();
				} else {
					// If not attacking the player, hop closer to them
					
					// Relative movement direction (Initialize at 0)
					int dx = 0;
					int dy = 0;
					
					// Calculate relative movement directions
					if (distX > 0) {
						dx = 1;
					} else if (distX < 0) {
						dx = -1;
					}
					
					if (distY > 0) {
						dy = 1;
					} else if (distY < 0) {
						dy = -1;
					}
					
					// First, try to move diagonal
					// If unsuccessful, move in the direction that it is
					// further from the player.
					if (!moveCharacter(dx, dy)) {
						if ((Math.abs(distX)) > (Math.abs(distY))) {
							// If movement in the x direction fails, try the y direction
							if (!moveCharacter(dx, 0)) {
								moveCharacter(0, dy);
							}
						} else {
							// If movement in the y direction fails, try the x direction
							if (!moveCharacter(0, dy)) {
								moveCharacter(dx, 0);
							}
						}
					}
				}
				
				// Play hop sound
				playHopSound();

				this.state = Beep.STATE_ATT;
				break;
			case Beep.STATE_ATT:
				// Cooldown period
				
				// Increment cooldown counter
				this.cooldownCount += 1;
				
				// Check if we've completed our cooldown
				if (cooldownCount >= cooldownMax) {
					// If so, switch to preparation state
					this.cooldownCount = 0;
					this.state = Beep.STATE_PREP;
				} else {
					// If not, do nothing
				}
				
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}

	}
	
	protected void playHopSound() {
		int whichSound = (new Random().nextInt(4) + 1);
		SoundPlayer.playWAV(GPath.createSoundPath("beep_hop" + whichSound + ".wav"), -10, getXPos(), getYPos());
	}
	
	protected void playDeathSound() {
		int whichSound = (new Random().nextInt(3) + 1);
		SoundPlayer.playWAV(GPath.createSoundPath("beep_death" + whichSound +".wav"), getXPos(), getYPos());
	}
	
}
