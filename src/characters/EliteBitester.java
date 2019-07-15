package characters;

import java.awt.Dimension;

import ai.DumbFollow;
import ai.IdleController;
import ai.IslandChecker;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import gui.GameScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import tiles.MovableType;
import tiles.TileType;

// Elite Bitesters move twice as fast as regular bitesters
public class EliteBitester extends Bitester {
	
	// Serialization ID
	private static final long serialVersionUID = 6538294691729065041L;
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_PURSUE = 1;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BITESTER);
	private String btImage_base = "elite_bitester";
	
	private String btImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BITESTER, "elite_bitester_dead.png");
	private String btImage_DEAD_CRIT = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_BITESTER, "elite_bitester_dead_CRIT.png");

	public EliteBitester(int startX, int startY) {
		super(startX, startY);
		this.chaseCount = 0;
		this.chaseMax = 4;
	}
	
	public EliteBitester(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY, patpat);
		this.chaseCount = 0;
		this.chaseMax = 4;
		
	}
	
	public String getName() {
		return "Elite Bitester";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.btImage_base;
		String hpPath = "";
		String statePath = "";
		
		// Add path modifier based on current health level
		if(this.currentHP > (this.maxHP / 2)) {
			hpPath = "_full";
		} else if(this.currentHP > 0) {
			hpPath = "_fatal";
		} else {
			hpPath = "_dead";
			return (imgPath + hpPath + ".png");
		}
		
		// Add path modifier based on current AI state
		switch(this.state) {
		case EliteBitester.STATE_PURSUE:
			statePath = "_ALERT";
			break;
		case EliteBitester.STATE_IDLE:
			// No extra path
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + hpPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		if(this.currentHP < -(this.maxHP / 2)) {
			return this.btImage_DEAD_CRIT;
		} else {
			return this.btImage_DEAD;
		}
	}
	
	@Override
	public void takeTurn() {
		
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
		
		TileType tt = GameScreen
				.getTile(player.getXPos(), player.getYPos()).getTileType();
		
		switch(this.state) {
			case EliteBitester.STATE_PURSUE:	
				
				// Move twice
				for(int moveCount = 0; moveCount < 2; moveCount++) {
					// Get relative location to player
					distX = plrX - this.xPos;
					distY = plrY - this.yPos;
					
					// If Bitester has fully lost interest, then return to idle
					if(this.chaseCount >= this.chaseMax) {
						this.chaseCount = 0;
						this.state = EliteBitester.STATE_IDLE;
						return;
					}
					
					// If player hops out of water, start to lose interest
					if(tt.getMovableType() != MovableType.WATER) {
						this.chaseCount++;
					} else {
						this.chaseCount = 0;
					}
					
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
				
					// Attack if next to player and they're in water
					if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
							((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
						if(tt.getMovableType() == MovableType.WATER) {
							this.playerInitiate();
							return;
						}
					} else {
						// Path-find to the player if we can
						Dimension nextStep = PathFinder.findPath(this.xPos, this.yPos, plrX, plrY, this);
						if(nextStep == null) {
							// Blindly pursue the target
							DumbFollow.blindPursue(distX, distY, dx, dy, this);
						} else {
							int changeX = nextStep.width - this.xPos;
							int changeY = nextStep.height - this.yPos;
							this.moveCharacter(changeX, changeY);
						}
					}
					
				} // End of 'For' loop
				break;
			case EliteBitester.STATE_IDLE:
				// Do nothing, until player steps in same pool of water
				if(tt.getMovableType() == MovableType.WATER &&
						IslandChecker.virusStart(this.xPos, this.yPos, plrX, plrY, MovableType.WATER)) {
					// Alert and pursue player if in same pool
					SoundPlayer.playWAV(GPath.createSoundPath("Bitester_ALERT.wav"));
					this.state = EliteBitester.STATE_PURSUE;
					return;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}

}
