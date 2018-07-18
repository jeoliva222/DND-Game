package characters;

import java.awt.Dimension;
import java.util.ArrayList;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import gui.GameScreen;
import gui.GameTile;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;
import projectiles.MusicNote;
import tiles.ExtraTile;
import tiles.TileType;

public class EliteWatchman extends Watchman {
	
	// Serialization ID
	private static final long serialVersionUID = -2662715705401430187L;
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_PREP = 3;
	private static final int STATE_ATT = 4;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_WATCHMAN);
	private String wmImage_base = "elite_watchman";
	
	private String wmImage_DEAD = GPath.createImagePath(GPath.ENEMY, GPath.ELITE_WATCHMAN, "elite_watchman_dead.png");
	
	// Constructors
	public EliteWatchman(int startX, int startY, ArrayList<GCharacter> npcList, ArrayList<ExtraTile> tileList) {
		super(startX, startY, npcList, tileList);
	}
	
	public EliteWatchman(int startX, int startY) {
		super(startX, startY);
	}
	
	public EliteWatchman(int startX, int startY, ArrayList<GCharacter> npcList, ArrayList<ExtraTile> tileList, PatrolPattern patpat) {
		super(startX, startY, npcList, tileList, patpat);
	}
	
	public EliteWatchman(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY, patpat);
	}
	
	@Override
	public String getName() {
		return "Elite Watchman";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.wmImage_base;
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
			case EliteWatchman.STATE_IDLE:
				// No extra path
				break;
			case EliteWatchman.STATE_PURSUE:
				statePath = "_PURSUE";
				break;
			case EliteWatchman.STATE_PREP:
				statePath = "_PREP";
				break;
			case EliteWatchman.STATE_ALERTED:
			case EliteWatchman.STATE_ATT:
				statePath = "_ALERT";
				break;
			default:
				System.out.println
					(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
				return GPath.NULL;
		}
		
		// Return the full path
		return (imgPath + hpPath + statePath + ".png");
	}
	
	@Override
	public String getCorpseImage() {
		return this.wmImage_DEAD;
	}
	
	@Override
	public void takeTurn() {
		// Get reference to the player
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
			case EliteWatchman.STATE_IDLE:
				boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					if(!this.soundedAlarm) {
						// For every NPC in the list, spawn it
						for(GCharacter npc: this.npcList) {
							EntityManager.getNPCManager().addPendingCharacter(npc);
						}
						
						for(ExtraTile et: this.tileList) {
							// Get the tile and old tile type
							GameTile tile = GameScreen.getTile(et.xPos, et.yPos);
							TileType oldType = tile.getTileType();
							
							// Exchange the two types
							tile.setTileType(et.tile);
							et.tile = oldType;
						}
						
						// Flag that we've already sounded our alarm once
						this.soundedAlarm = true;
					}
					
					// Play alert sound and change state to pursue player
					SoundPlayer.playWAV(GPath.createSoundPath("Watchman_ALERT.wav"));
					this.state = EliteWatchman.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case EliteWatchman.STATE_ALERTED:
				// Rest for one turn, making an angry face
				// then transition to the chase
				this.state = EliteWatchman.STATE_PURSUE;
				break;
			case EliteWatchman.STATE_PURSUE:	
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
				
				// Change state to prep if next to player
				if((Math.abs(distY) == 0) || (Math.abs(distX) == 0)) {
					this.xMarkDir = dx;
					this.yMarkDir = dy;
					this.state = EliteWatchman.STATE_PREP;
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
				break;
			case EliteWatchman.STATE_PREP:
				// Fire two Music notes at player in a line
				EntityManager.getProjectileManager()
					.addProjectile(new MusicNote((this.xPos + this.xMarkDir),
											(this.yPos + this.yMarkDir),
											this.xMarkDir,
											this.yMarkDir, this));
				EntityManager.getProjectileManager()
				.addProjectile(new MusicNote((this.xPos + (this.xMarkDir*2)),
										(this.yPos + (this.yMarkDir*2)),
										this.xMarkDir,
										this.yMarkDir, this));
				
				
				// Play attack sound and change state
				SoundPlayer.playWAV(GPath.createSoundPath("Watchman_ATTACK.wav"));
				this.state = EliteWatchman.STATE_ATT;
				break;
			case EliteWatchman.STATE_ATT:
				// Cooldown period for one turn
				this.state = EliteWatchman.STATE_PURSUE;
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
	}

}
