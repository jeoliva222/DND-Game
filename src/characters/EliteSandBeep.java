package characters;

import ai.IdleController;
import ai.LineDrawer;
import ai.PatrolPattern;
import effects.DamageIndicator;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;

// Class that defines the Elite Sand beep enemy found in the Desert area
public class EliteSandBeep extends SandBeep {

	// Serialization ID
	private static final long serialVersionUID = 2874308749190475510L;
	
	// State indicators
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREP = 1;
	private static final int STATE_ATT = 2;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.BEEP);
	private String bpImage_base = "beep";
	
	private String beImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.BEEP, "beep_dead.png");
	private String beImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.BEEP, "beep_dead_CRIT.png");

	//------------------------------------------
	
	// Constructors
	
	public EliteSandBeep(int startX, int startY) {
		super(startX, startY);
	}
	
	public EliteSandBeep(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY, patpat);
	}
	
	public String getName() {
		return "Elite Sand Beep";
	}
	
	@Override
	public String getImage() {
		// TODO : Needs custom sprites
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
		case EliteSandBeep.STATE_IDLE:
			// No extra path
			break;
		case EliteSandBeep.STATE_PREP:
			statePath = "_PREP";
			break;
		case EliteSandBeep.STATE_ATT:
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
			case EliteSandBeep.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					SoundPlayer.playWAV(GPath.createSoundPath("beep_ALERT.wav"));
					this.state = EliteSandBeep.STATE_PREP;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case EliteSandBeep.STATE_PREP:				
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
					EntityManager.getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
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
				// If we did a second hop, do a third hop if we haven't attacked this turn
				for(int i = 0; i < 2; i++) {
					if(this.doExtraHop && !didAttack) {
						// Attack if player is our current hop path
						if((this.xPos + this.markedX) == plrX && (this.yPos + this.markedY) == plrY) {
							// Mark tiles with damage indicators
							EntityManager.getEffectManager().addEffect(new DamageIndicator(plrX, plrY));
							this.playerInitiate();
							didAttack = true;
						} else {
							// If we don't hit the payer, continue on our current hop path
							this.moveCharacter(this.markedX, this.markedY);
						}
					}
				}
				
				// Play hop sound
				this.playHopSound();

				// Change state and toggle whether we do a double hop next time
				this.doExtraHop = !(this.doExtraHop);
				this.state = EliteSandBeep.STATE_ATT;
				break;
			case EliteSandBeep.STATE_ATT:
				// Cooldown period
				
				// Increment cooldown counter
				this.cooldownCount += 1;
				
				// Check if we've completed our cooldown
				if(this.cooldownCount >= this.cooldownMax) {
					// If so, switch to preparation state
					this.cooldownCount = 0;
					this.state = EliteSandBeep.STATE_PREP;
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
