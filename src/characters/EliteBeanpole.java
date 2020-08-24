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

/**
 * Class representing the Elite Beanpole enemy,
 * which does not have a cooldown period between attacks
 * @author jeoliva
 */
public class EliteBeanpole extends Beanpole {
	
	// Serialization ID
	private static final long serialVersionUID = -6473607655035515501L;
	
	// State Indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP = 3;
	private static final int STATE_ATT = 4;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BEANPOLE);
	private static String bpImage_base = "elite_beanpole";

	private static String bpImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BEANPOLE, "elite_beanpole_dead.png");
	private static String bpImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BEANPOLE, "elite_beanpole_dead_CRIT.png");


	public EliteBeanpole(int startX, int startY) {
		super(startX, startY);
	}
	
	public EliteBeanpole(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY, patpat);
	}
	
	public String getName() {
		return "Elite Beanpole";
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
			case EliteBeanpole.STATE_IDLE:
			case EliteBeanpole.STATE_PURSUE:
				// No extra path
				break;
			case EliteBeanpole.STATE_ALERTED:
			case EliteBeanpole.STATE_PREP:
				statePath = "_PREP";
				break;
			case EliteBeanpole.STATE_ATT:
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

	@Override
	public void takeTurn() {
		// Fetch reference to player
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
			case EliteBeanpole.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("Beanpole_ALERT.wav"), getXPos(), getYPos());
					this.state = EliteBeanpole.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case EliteBeanpole.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = EliteBeanpole.STATE_PURSUE;
				break;
			case EliteBeanpole.STATE_PURSUE:	
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
					this.state = EliteBeanpole.STATE_PREP;
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
			case EliteBeanpole.STATE_PREP:
				// Mark tile with damage indicator
				EntityManager.getInstance().getEffectManager().addEffect(new DamageIndicator(xPos + markX, yPos + markY));
				
				// Attack in marked direction
				if ((xPos + markX) == plrX && (yPos + markY) == plrY) {
					playerInitiate();
				}
				this.state = EliteBeanpole.STATE_ATT;
				break;
			case EliteBeanpole.STATE_ATT:
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
				
				// If immediately next to play after attack, cue up another attack
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// Mark direction to attack next turn
					this.markX = dx;
					this.markY = dy;
					
					// Change state to attack again
					this.state = EliteBeanpole.STATE_PREP;
				} else {
					// Otherwise, go back to pursuing the player
					this.state = EliteBeanpole.STATE_PURSUE;
				}
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}

}
