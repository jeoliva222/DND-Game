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
 * Class representing the Sand Beep enemy in the 'Poacher's Desert' area
 * @author jeoliva
 */
public class SandBeep extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 4702359470958492375L;

	// Modifiers/Statistics
	
	private static int MAX_HP = 6;
	
	private static int MIN_DMG = 2;
	private static int MAX_DMG = 4;
	
	private static double CRIT_CHANCE = 0.1;
	private static double CRIT_MULT = 1.5;
	
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
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.SAND_BEEP);
	private static String bpImage_base = "sand_beep";
	
	private static String beImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.SAND_BEEP, "sand_beep_dead.png");
	private static String beImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.SAND_BEEP, "sand_beep_dead_CRIT.png");

	// Constructors
	public SandBeep(int startX, int startY) {
		this(startX, startY, PatrolPattern.STATIONARY);
	}
	
	public SandBeep(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = SandBeep.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Sand Beep";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + bpImage_base);
		String hpPath = "";
		String statePath = "";
		String hopPath = "";
		
		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return GPath.NULL;
		}
		
		if (doExtraHop) {
			hopPath = "_ALT";
		}
		
		switch (state) {
			case SandBeep.STATE_IDLE:
				// No extra path
				break;
			case SandBeep.STATE_PREP:
				statePath = "_PREP";
				break;
			case SandBeep.STATE_ATT:
				if (cooldownCount < 1) {
					statePath = "_ATT";
				}
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + hopPath + ".png");
	}
	
	public String getCorpseImage() {
		if (currentHP < -(maxHP / 2)) {
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
		if (currentHP < -(maxHP / 2)) {
			SoundPlayer.playWAV(GPath.createSoundPath("Bitester_DEATH_CRIT.wav"), getXPos(), getYPos());
		} else {
			playDeathSound();
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
			case SandBeep.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"), getXPos(), getYPos());
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
				
				// Get relative location to player
				distX = (plrX - xPos);
				distY = (plrY - yPos);
				
				// Initialize flag to indicate whether we've attack this turn
				boolean didAttack = false;
				
				// Attack if player is in one tile radius around player
				if ((xPos + dx) == plrX && (yPos + dy) == plrY) {
					// Mark tiles with damage indicators
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
					playerInitiate();
					
					// Flip flag to indicate we've attacked this turn
					didAttack = true;
				} else {
					// If not attacking the player, hop closer to them
					
					// First, try to move diagonal
					// If unsuccessful, move in the direction that it is
					// further from the player.
					// Only does this on the first hop of the turn
					if (moveCharacter(dx, dy)) {
						this.markedX = dx;
						this.markedY = dy;
					} else if ((Math.abs(distX)) > (Math.abs(distY))) {
						// If movement in the x direction fails, try the y direction
						if (moveCharacter(dx, 0)) {
							this.markedX = dx;
							this.markedY = 0;
						} else if (moveCharacter(0, dy)) {
							this.markedX = 0;
							this.markedY = dy;
						}
					} else {
						// If movement in the y direction fails, try the x direction
						if (moveCharacter(0, dy)) {
							this.markedX = 0;
							this.markedY = dy;
						} else if (moveCharacter(dx, 0)) {
							this.markedX = dx;
							this.markedY = 0;
						}
					}
				}
				
				// Do a second hop if we're queued up for it and haven't attacked this turn
				if (doExtraHop && !didAttack) {
					// Attack if player is our current hop path
					if ((xPos + markedX) == plrX && (yPos + markedY) == plrY) {
						// Mark tiles with damage indicators
						EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
						playerInitiate();
					} else {
						// If we don't hit the payer, continue on our current hop path
						moveCharacter(markedX, markedY);
					}
				}
				
				// Play hop sound
				playHopSound();

				// Change state
				this.state = SandBeep.STATE_ATT;
				break;
			case SandBeep.STATE_ATT:
				// Cooldown period
				
				// Increment cooldown counter
				this.cooldownCount += 1;
				
				// Check if we've completed our cooldown
				if (cooldownCount >= cooldownMax) {
					// If so, switch to preparation state
					this.cooldownCount = 0;
					this.state = SandBeep.STATE_PREP;
				} else {
					// If not, toggle extra hop parameter
					this.doExtraHop = !(doExtraHop);
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
