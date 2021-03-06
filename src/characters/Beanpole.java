package characters;

import java.awt.Dimension;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import effects.DamageIndicator;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

/**
 * Class representing the Beanpole enemy character
 * @author jeoliva
 */
public class Beanpole extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = 3052578680731251089L;
	
	// Modifiers/Statistics

	private static int MAX_HP = 5;
	
	private static int MIN_DMG = 1;
	private static int MAX_DMG = 2;
	
	private static double CRIT_CHANCE = 0.1;
	private static double CRIT_MULT = 1.5;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP = 3;
	private static final int STATE_ATT = 4;
	
	//----------------------------
	
	// Additional parameters
	
	// Indicate which direction the NPC is launching its attack
	protected int markX = 0;
	protected int markY = 0;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE);
	private String bpImage_base = "beanpole";
	
	private String bpImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead.png");
	private String bpImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BEANPOLE, "beanpole_dead_CRIT.png");


	public Beanpole(int startX, int startY) {
		this(startX, startY, PatrolPattern.WANDER);
	}
	
	public Beanpole(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = Beanpole.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Beanpole";
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
			return (imgPath + hpPath + ".png");
		}
		
		switch (state) {
			case Beanpole.STATE_IDLE:
			case Beanpole.STATE_PURSUE:
				// No extra path
				break;
			case Beanpole.STATE_ALERTED:
			case Beanpole.STATE_PREP:
				statePath = "_PREP";
				break;
			case Beanpole.STATE_ATT:
				statePath = "_ATT";
				break;
			default:
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if (currentHP < -(maxHP / 2)) {
			return bpImage_DEAD_CRIT;
		} else {
			return bpImage_DEAD;
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
			SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH_CRIT.wav"), getXPos(), getYPos());
		} else {
			SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_DEATH.wav"), getXPos(), getYPos());
		}
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
		
		switch (state) {
			case Beanpole.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ALERT.wav"), getXPos(), getYPos());
					this.state = Beanpole.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case Beanpole.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = Beanpole.STATE_PURSUE;
				break;
			case Beanpole.STATE_PURSUE:	
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
				
				// Change state to prep if next to player
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// Mark direction to attack next turn
					this.markX = dx;
					this.markY = dy;
					
					// Change states
					this.state = Beanpole.STATE_PREP;
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
				}
				break;
			case Beanpole.STATE_PREP:
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + markX, yPos + markY));
				
				// Attack in marked direction
				if ((xPos + markX) == plrX && (yPos + markY) == plrY) {
					playerInitiate();
				}
				this.state = Beanpole.STATE_ATT;
				break;
			case Beanpole.STATE_ATT:
				// Cooldown period for one turn
				this.state = Beanpole.STATE_PURSUE;
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}
	
}
