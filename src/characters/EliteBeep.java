package characters;

import ai.IdleController;
import ai.PatrolPattern;
import effects.DamageIndicator;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

// Elite version of 'Beep' that can move through all terrain
public class EliteBeep extends Beep {
	
	// Serialization ID
	private static final long serialVersionUID = 6215526504200122899L;
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREP = 1;
	private static final int STATE_ATT = 2;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BEEP);
	private String bpImage_base = "elite_beep";
	
	private String beImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BEEP, "elite_beep_dead.png");
	private String beImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BEEP, "elite_beep_dead_CRIT.png");
	
	// Constructors
	public EliteBeep(int startX, int startY) {
		super(startX, startY);
	}
	
	public EliteBeep(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY, patpat);
	}
	
	@Override
	public String getName() {
		return "Elite Beep";
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
		case EliteBeep.STATE_IDLE:
			// No extra path
			break;
		case EliteBeep.STATE_PREP:
			statePath = "_PREP";
			break;
		case EliteBeep.STATE_ATT:
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
	
	@Override
	public String getCorpseImage() {
		if(this.currentHP < -(this.maxHP)) {
			return this.beImage_DEAD_CRIT;
		} else {
			return this.beImage_DEAD;
		}
	}
	
	@Override
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.WATER);
		this.moveTypes.add(MovableType.WALL);
		this.moveTypes.add(MovableType.AIR);
		this.moveTypes.add(MovableType.ACID);
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
			case EliteBeep.STATE_IDLE:
				// If the player is close enough, become alerted of their location
				if((Math.abs(distX) + Math.abs(distY)) <= 3) {
					SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"));
					this.state = EliteBeep.STATE_PREP;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case EliteBeep.STATE_PREP:
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

				this.state = EliteBeep.STATE_ATT;
				break;
			case EliteBeep.STATE_ATT:
				// Cooldown period
				
				// Increment cooldown counter
				this.cooldownCount += 1;
				
				// Check if we've completed our cooldown
				if(this.cooldownCount >= this.cooldownMax) {
					// If so, switch to preparation state
					this.cooldownCount = 0;
					this.state = EliteBeep.STATE_PREP;
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
}
