package characters;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.swing.Timer;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import characters.allies.Player;
import effects.EyeEffect;
import gui.GameWindow;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EffectManager;
import managers.EntityManager;
import tiles.MovableType;

// Class representing the Watcher Eye that stalks players
public class WatcherEye extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = -490999656815769167L;

	// Modifiers/Statistics

	private int MAX_HP = 100;
	
	private int ARMOR_VAL = 100;
	
	private int MIN_DMG = 0;
	private int MAX_DMG = 0;
	
	private double CRIT_CHANCE = 0.0;
	private double CRIT_MULT = 1.0;
	
	//----------------------------
	
	// State indicators
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_ALERTED = 1;
	private static final int STATE_PURSUE = 2;
	private static final int STATE_SEARCH = 3;
	
	//----------------------------
	
	// Additional parameters
	
	// Indicate the coordinates which eye last spotted the player
	protected int markX = 0;
	protected int markY = 0;
	
	//----------------------------
	
	// File paths to images
	private String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.GAZER);
	private String weImage_base = "Gazer";


	public WatcherEye(int startX, int startY) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = WatcherEye.STATE_IDLE;
		this.patrolPattern = PatrolPattern.WANDER;
		
		this.imagePath = this.getImage();
	}
	
	public WatcherEye(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = this.maxHP;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = WatcherEye.STATE_IDLE;
		this.patrolPattern = patpat;
		
		this.imagePath = this.getImage();
	}
	
	public String getName() {
		return "Its Gaze";
	}
	
	@Override
	public String getImage() {
		String imgPath = this.imageDir + this.weImage_base;
		String statePath = "";
		
		switch(this.state) {
		case WatcherEye.STATE_PURSUE:
			int xMov = this.xPos - this.lastX;
			if(xMov < 0) {
				statePath = "_PURSUE_LEFT";
			} else if (xMov > 0) {
				statePath = "_PURSUE_RIGHT";
			} else {
				statePath = "_IDLE";
			}
			break;
		case WatcherEye.STATE_ALERTED:
		case WatcherEye.STATE_IDLE:
		case WatcherEye.STATE_SEARCH:
			statePath = "_IDLE";
			break;
		default:
			System.out.println
				(this.getName() + " couldn't find a proper image: " + Integer.toString(this.state));
			return GPath.NULL;
		}
		
		return (imgPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		// Can't die
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		this.moveTypes.add(MovableType.GROUND);
		this.moveTypes.add(MovableType.WATER);
		this.moveTypes.add(MovableType.AIR);
		this.moveTypes.add(MovableType.ACID);
	}

	@Override
	public void playerInitiate() {
		// Crash the game - TODO
		
		// Creates a special file
		String filePath = GPath.EYE_PATH;
		File madFile = new File(filePath);
		if (!madFile.exists()) {
			try {
				List<String> lines = Arrays.asList("HAHAHAHAHAHAHAHAHA",
						"HAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHA",
						"HAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHA");
				Path file = Paths.get(filePath);
				Files.write(file, lines, Charset.forName("UTF-8"));
			} catch (IOException e) {
				System.out.println("File didn't generate properly");
				e.printStackTrace();
			}
		}
		
		// Stop Music
		SoundPlayer.stopMidi();
		
		// Fill the screen with eyes
		EffectManager em = EntityManager.getInstance().getEffectManager();
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				em.addEffect(new EyeEffect(x, y, 5));
			}
		}
		
		// Play the classic "Gotcha" sound
		SoundPlayer.playWAV(GPath.createSoundPath("Eye_Gotcha.wav"));
		
		// Create timer for small period that closes pop-up on finish
		int delay = 400; // Milliseconds
		ActionListener taskPerformer = new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		          GameWindow.getInstance().dispose();
		          GameWindow.getInstance().dispatchEvent(new WindowEvent(GameWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
		      }
		};
		new Timer(delay, taskPerformer).start();
	}
	
	@Override
	public void onDeath() {
		// Can't die
	}

	@Override
	public void takeTurn() {
		// Get reference to the player
		Player player = EntityManager.getInstance().getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if(!this.isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Initialize LOS
		boolean hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
		if(hasLOS) {
			// Mark the last position we saw the player
			this.markX = plrX;
			this.markY = plrY;
		}
		
		switch(this.state) {
			case WatcherEye.STATE_IDLE:
				if(hasLOS) {
					// Alert to the player
					SoundPlayer.playWAV(GPath.createSoundPath("Eye_Scream.wav"));
					this.state = WatcherEye.STATE_ALERTED;
				} else {
					// Handle movement for Idling
					IdleController.moveIdle(this);
				}
				break;
			case WatcherEye.STATE_ALERTED:
				// Rest for one turn, then transition to the chase
				this.state = WatcherEye.STATE_PURSUE;
				break;
			case WatcherEye.STATE_PURSUE:	
				
				// If we arrived at last spotted position and still can't see the player,
				// then enter search mode
				if(!hasLOS && this.xPos == this.markX && this.yPos == this.markY) {
					this.state = WatcherEye.STATE_SEARCH;
					break;
				}
				
				// Relative movement direction (Initialize at 0)
				int dx = 0;
				int dy = 0;
				
				// Get relative location to position we spotted player last
				int viewDistX = this.markX - this.xPos;
				int viewDistY = this.markY - this.yPos;
				
				// Calculate relative movement directions to get to last spotted location
				if(viewDistX > 0) {
					dx = 1;
				} else if (viewDistX < 0) {
					dx = -1;
				}
				
				if(viewDistY > 0) {
					dy = 1;
				} else if (viewDistY < 0) {
					dy = -1;
				}
				
				// Get relative location to player's position (Used for attack check)
				int distX = plrX - this.xPos;
				int distY = plrY - this.yPos;
				
				// Change state to prep if next to player
				if(((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// End the player's life
					this.playerInitiate();
				} else {
					// Path-find to the last position we saw the player at
					Dimension nextStep = PathFinder.findPath(this.xPos, this.yPos, this.markX, this.markY, this);
					if(nextStep == null) {
						// Blindly pursue the target
						DumbFollow.blindPursue(viewDistX, viewDistY, dx, dy, this);
					} else {
						int changeX = nextStep.width - this.xPos;
						int changeY = nextStep.height - this.yPos;
						this.moveCharacter(changeX, changeY);
					}
				}
				
				// Recalculate LOS after move
				hasLOS = LineDrawer.hasSight(this.xPos, this.yPos, plrX, plrY);
				if(hasLOS) {
					// Mark the last position we saw the player
					this.markX = plrX;
					this.markY = plrY;
				}
				
				break;
			case WatcherEye.STATE_SEARCH:
				if(hasLOS) {
					// Alert to the player
					SoundPlayer.playWAV(GPath.createSoundPath("Eye_Scream.wav"));
					this.state = WatcherEye.STATE_ALERTED;
				} else  {	
					// Return to idling
					SoundPlayer.playWAV(GPath.createSoundPath("Eye_Breath.wav"));
					this.state = WatcherEye.STATE_IDLE;
				}
				break;
			default:
				System.out.println(this.getName() +
						" couldn't take its turn. State = " + Integer.toString(this.state));
				return;
		}
			
	}
}
