package characters;

import java.awt.Dimension;
import java.util.Random;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import effects.DamageIndicator;
import effects.WarningIndicator;
import gui.LogScreen;
import helpers.GColors;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

/**
 * Class representing the Hoptooth enemy
 * @author jeoliva
 */
public class Hoptooth extends GCharacter {

	// Serialization ID
	private static final long serialVersionUID = 7655232320739396671L;
	
	// Modifiers/Statistics

	private static int MAX_HP = 20;
	
	private static int MIN_DMG = 4;
	private static int MAX_DMG = 6;
	
	private static double CRIT_CHANCE = 0.1;
	private static double CRIT_MULT = 1.5;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP_SWING = 3;
	private static final int STATE_ATT_SWING = 4;
	private static final int STATE_PREP_CHOMP = 5;
	private static final int STATE_ATT_CHOMP = 6;
	
	//----------------------------
	
	// Additional parameters
	
	// Indicate which direction the NPC is launching its attack
	protected int markX = 0;
	protected int markY = 0;
	
	// Attack counter
	private int attCount = 0;
	
	// Number of turns needed to perform chomp attack
	private final int windupMax = 2;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.HOPTOOTH);
	private static String htImage_base = "hoptooth";
	
	private static String htImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.HOPTOOTH, "hoptooth_dead.png");
	private static String htImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.HOPTOOTH, "hoptooth_dead.png");

	// Constructor(s)
	public Hoptooth(int startX, int startY) {
		this(startX, startY, PatrolPattern.WANDER);
	}
	
	public Hoptooth(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Hoptooth.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Hoptooth";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + htImage_base);
		String hpPath = "";
		String statePath = "";

		if (currentHP > (maxHP / 2)) {
			hpPath = "_full";
		} else if (currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		switch (state) {
			case Hoptooth.STATE_IDLE:
				statePath = "_IDLE";
				break;
			case Hoptooth.STATE_PURSUE:
				// No extra path
				break;
			case Hoptooth.STATE_ALERTED:
			case Hoptooth.STATE_PREP_CHOMP:
				if(this.attCount == 0) {
					statePath = "_PREP_CHOMP";
				} else {
					statePath = "_PREP_CHOMP2";
				}
				break;
			case Hoptooth.STATE_ATT_CHOMP:
				statePath = "_ATT_CHOMP";
				break;
			case Hoptooth.STATE_PREP_SWING:
				statePath = "_PREP_SWING";
				break;
			case Hoptooth.STATE_ATT_SWING:
				statePath = "_ATT_SWING";
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
	
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if (currentHP < -(maxHP / 2)) {
			return htImage_DEAD_CRIT;
		} else {
			return htImage_DEAD;
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
		attackPlayer();
	}
	
	@Override
	public void onDeath() {
		SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Death.wav"), 3f);
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
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Get relative location to player
		int distX = (plrX - xPos);
		int distY = (plrY - yPos);
		
		// Relative movement direction (Initialize at 0)
		int dx = 0;
		int dy = 0;
		
		switch (state) {
			case Hoptooth.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Alert.wav"));
					this.state = Hoptooth.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Hoptooth.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = Hoptooth.STATE_PURSUE;
				break;
			case Hoptooth.STATE_PURSUE:	
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
				
				// Change state to prep if next to player
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// Mark direction to attack next turn
					this.markX = dx;
					this.markY = dy;
					
					// Choose a new melee attack and change states
					chooseMeleeAttack();
				} else {
					// Path-find to the player if we can
					Dimension nextStep = PathFinder.findPath(xPos, yPos, plrX, plrY, this);
					if (nextStep == null) {
						// Blindly pursue the target
						DumbFollow.blindPursue(distX, distY, dx, dy, this);
					} else {
						int changeX = (nextStep.width - xPos);
						int changeY = (nextStep.height - yPos);
						moveCharacter(changeX, changeY);
					}
					
					// Recalculate relative location to player
					distX = (plrX - xPos);
					distY = (plrY - yPos);
					
					// Relative movement direction (Initialize at 0)
					dx = 0;
					dy = 0;
					
					// Recalculate relative movement directions
					// X-movement
					if (distX > 0) {
						dx = 1;
					} else if (distX < 0) {
						dx = -1;
					}
					// Y-movement
					if (distY > 0) {
						dy = 1;
					} else if (distY < 0) {
						dy = -1;
					}
					
					// Decide if Hoptooth should attempt a running chomp prep
					// Attempt only 1/2 of the time
					int shouldAttack = new Random().nextInt(2);
					if ((shouldAttack == 0) && (((Math.abs(distX) <= 1) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) <= 1)))) {
						// Next, make sure there aren't any walls in the way
						boolean hasAttLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
						if (hasAttLOS) {
							this.markX = dx;
							this.markY = dy;
							
							SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Breath1.wav"), -5f);
							this.state = Hoptooth.STATE_PREP_CHOMP;
						}
					}
				}
				break;
			case Hoptooth.STATE_PREP_SWING:
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + markX, yPos + markY));
				
				// Attack in marked direction
				if ((xPos + markX) == plrX && (yPos + markY) == plrY) {
					playerInitiate();
				}
				this.state = Hoptooth.STATE_ATT_SWING;
				break;
			case Hoptooth.STATE_ATT_SWING:
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
				
				// If immediately next to player after attack, cue up another attack
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// Mark direction to attack next turn
					this.markX = dx;
					this.markY = dy;
					
					// Change state to attack again
					this.state = Hoptooth.STATE_PREP_SWING;
				} else {
					// Otherwise, go back to pursuing the player
					this.state = Hoptooth.STATE_PURSUE;
				}
				break;
			case Hoptooth.STATE_PREP_CHOMP:
				// Increment attack counter
				this.attCount += 1;
				
				// Only perform attack if we've done our full wind-up
				if (attCount == (windupMax - 1)) {
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new WarningIndicator(xPos + markX, yPos + markY));
					
					// Play warning sound
					SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Breath2.wav"));
				} else if (attCount >= windupMax) {
					// Mark tile with damage indicator
					EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + markX, yPos + markY));
					
					// Play chomp sound
					SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Chomp.wav"));
					
					// Attack in marked direction
					if ((xPos + markX) == plrX && (yPos + markY) == plrY) {
						chompPlayer();
					}

					// Reset attack counter and switch states
					this.attCount = 0;
					this.state = Hoptooth.STATE_ATT_CHOMP;
				}
				break;
			case Hoptooth.STATE_ATT_CHOMP:
				// Cooldown period for one turn
				this.state = Hoptooth.STATE_PURSUE;
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}
	
	// Choose a random melee attack
	private void chooseMeleeAttack() {
		int whichAttack = new Random().nextInt(3);
		if (whichAttack == 0) {
			SoundPlayer.playWAV(GPath.createSoundPath("Hoptooth_Breath1.wav"), -5f);
			this.state = Hoptooth.STATE_PREP_CHOMP;
		} else {
			this.state = Hoptooth.STATE_PREP_SWING;
		}
	}
	
	// Execute the player with a fatal chomp
	private void chompPlayer() {
		SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH_CRIT.wav"));
		LogScreen.log(getName() + " bit the player's head off.", GColors.DAMAGE);
		EntityManager.getInstance().getPlayer().damagePlayer(300);
	}
	
}
