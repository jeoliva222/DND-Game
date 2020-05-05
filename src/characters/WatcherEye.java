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
import java.util.Random;

import javax.swing.Timer;

import ai.DumbFollow;
import ai.IdleController;
import ai.LineDrawer;
import ai.PathFinder;
import ai.PatrolPattern;
import buffs.Buff;
import characters.allies.Player;
import effects.EyeEffect;
import gui.GameWindow;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EffectManager;
import managers.EntityManager;
import tiles.MovableType;

/**
 * Class representing the Watcher Eye enemy that stalks players
 * @author jeoliva
 */
public class WatcherEye extends GCharacter {
	
	// Serialization ID
	private static final long serialVersionUID = -490999656815769167L;

	// Modifiers/Statistics

	private static int MAX_HP = 100;
	
	private static int ARMOR_VAL = 100;
	
	private static int MIN_DMG = 0;
	private static int MAX_DMG = 0;
	
	private static double CRIT_CHANCE = 0.0;
	private static double CRIT_MULT = 1.0;
	
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
	
	// Chance to cause an illusion to appear on the player's vision
	protected static final double ILLUSION_CHANCE = 0.02;
	
	//----------------------------
	
	// File paths to images
	private static String imageDir = GPath.createImagePath(GPath.ENEMY, GPath.GAZER);
	private static String weImage_base = "Gazer";


	public WatcherEye(int startX, int startY) {
		this(startX, startY, PatrolPattern.WANDER);
	}
	
	public WatcherEye(int startX, int startY, PatrolPattern patpat) {
		super(startX, startY);
		
		this.maxHP = MAX_HP;
		this.currentHP = maxHP;
		
		this.armor = ARMOR_VAL;
		
		this.minDmg = MIN_DMG;
		this.maxDmg = MAX_DMG;
		
		this.critChance = CRIT_CHANCE;
		this.critMult = CRIT_MULT;
		
		this.state = WatcherEye.STATE_IDLE;
		this.patrolPattern = patpat;
	}
	
	public String getName() {
		return "Its Gaze";
	}
	
	@Override
	public String getImage() {
		String imgPath = (imageDir + weImage_base);
		String statePath = "";
		
		switch (state) {
			case WatcherEye.STATE_PURSUE:
				int xMov = (xPos - lastX);
				if (xMov < 0) {
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
				System.out.println(getName() + " couldn't find a proper image: " + Integer.toString(state));
				return GPath.NULL;
		}
		
		return (imgPath + statePath + ".png");
	}
	
	public String getCorpseImage() {
		// Can't die
		return GPath.NULL;
	}
	
	public void populateMoveTypes() {
		this.moveTypes = ((short) (moveTypes + (MovableType.GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_GROUND)));
		this.moveTypes = ((short) (moveTypes + (MovableType.WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ALT_WATER)));
		this.moveTypes = ((short) (moveTypes + (MovableType.ACID)));
		this.moveTypes = ((short) (moveTypes + (MovableType.PIT)));
	}

	@Override
	public void playerInitiate() {
		// Crash the game
		
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
			} catch (IOException ex) {
				System.out.println("File didn't generate properly");
				ex.printStackTrace();
			}
		}
		
		// Stop Music
		SoundPlayer.stopMidi();
		
		// Fill the screen with eyes
		EffectManager em = EntityManager.getInstance().getEffectManager();
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				em.addEffect(new EyeEffect(x, y, 5));
			}
		}
		
		// Play the classic "Gotcha" sound
		SoundPlayer.playWAV(GPath.createSoundPath("Eye_Gotcha.wav"));
		
		// Create timer for small period that closes pop-up on finish
		int delay = 400; // Milliseconds
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				GameWindow.getInstance().dispose();
				GameWindow.getInstance().dispatchEvent(new WindowEvent(GameWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
			}
		};
		new Timer(delay, taskPerformer).start();
	}
	
	@Override
	public void addBuff(Buff debuff) {
		// Don't add the buff
		return;
	}
	
	@Override
	public void onDeath() {
		// Can't die
	}

	@Override
	public void takeTurn() {
		// Get reference to the EntityManager / Player
		EntityManager em = EntityManager.getInstance();
		Player player = em.getPlayer();
		
		// If this is dead or the player is dead, don't do anything
		if (!isAlive() || !player.isAlive()) {
			// Do nothing
			return;
		}
		
		// Get player's location
		int plrX = player.getXPos();
		int plrY = player.getYPos();
		
		// Initialize LOS
		boolean hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
		if (hasLOS) {
			// Mark the last position we saw the player
			this.markX = plrX;
			this.markY = plrY;
		}
		
		// Creates an illusion sometimes if it is dark
		// Illusion chance increases exponentially when the player is more hurt
		int playerHP = player.getCurrentHP();
		if (playerHP <= 1) {
			if (em.isDark()) {
				// If dark and player at 1 HP, generate a ring of illusions
				createMadness();
			}
		} else {
			double chanceMult =  ((double) player.getMaxHP() / playerHP);
			if (Math.random() < (ILLUSION_CHANCE * chanceMult) && em.isDark()) {
				createIllusion();
			}
		}
		
		switch (state) {
			case WatcherEye.STATE_IDLE:
				if (hasLOS) {
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
				if (!hasLOS && xPos == markX && yPos == markY) {
					this.state = WatcherEye.STATE_SEARCH;
					break;
				}
				
				// Relative movement direction (Initialize at 0)
				int dx = 0;
				int dy = 0;
				
				// Get relative location to position we spotted player last
				int viewDistX = (markX - xPos);
				int viewDistY = (markY - yPos);
				
				// Calculate relative movement directions to get to last spotted location
				if (viewDistX > 0) {
					dx = 1;
				} else if (viewDistX < 0) {
					dx = -1;
				}
				
				if (viewDistY > 0) {
					dy = 1;
				} else if (viewDistY < 0) {
					dy = -1;
				}
				
				// Get relative location to player's position (Used for attack check)
				int distX = (plrX - xPos);
				int distY = (plrY - yPos);
				
				// Change state to prep if next to player
				if (((Math.abs(distX) == 1) && (Math.abs(distY) == 0)) ||
						((Math.abs(distX) == 0) && (Math.abs(distY) == 1))) {
					// End the player's life
					playerInitiate();
				} else {
					// Path-find to the last position we saw the player at
					Dimension nextStep = PathFinder.findPath(xPos, yPos, markX, markY, this);
					if (nextStep == null) {
						// Blindly pursue the target
						DumbFollow.blindPursue(viewDistX, viewDistY, dx, dy, this);
					} else {
						int changeX = (nextStep.width - xPos);
						int changeY = (nextStep.height - yPos);
						moveCharacter(changeX, changeY);
					}
				}
				
				// Recalculate LOS after move
				hasLOS = LineDrawer.hasSight(xPos, yPos, plrX, plrY);
				if (hasLOS) {
					// Mark the last position we saw the player
					this.markX = plrX;
					this.markY = plrY;
				}
				
				break;
			case WatcherEye.STATE_SEARCH:
				if (hasLOS) {
					// Alert to the player
					SoundPlayer.playWAV(GPath.createSoundPath("Eye_Scream.wav"));
					this.state = WatcherEye.STATE_ALERTED;
				} else {	
					// Return to idling
					SoundPlayer.playWAV(GPath.createSoundPath("Eye_Breath.wav"));
					this.state = WatcherEye.STATE_IDLE;
				}
				break;
			default:
				System.out.println(getName() + " couldn't take its turn. State = " + Integer.toString(state));
				return;
		}	
	}
	
	// Creates a single illusion right on the edge of the player's vision
	private void createIllusion() {
		EntityManager em = EntityManager.getInstance();
		Player player = em.getPlayer();
		Random r = new Random();
		
		// Randomly determine the indexed spot of the location
		int edgeRange = (player.getVision() + 1);
		int numSpots = edgeRange * 4;
		int spot = r.nextInt(numSpots);
		
		// Determine the 'clock' quadrant and quadrant index
		int quad = (spot / edgeRange);
		int quadIndex = spot % edgeRange;
		
		// Determine the angular directions based on the indexed position
		Dimension primeDir = null;
		Dimension sideDir = null;
		if (quad == 0) {
			primeDir = new Dimension(0, -1);
			sideDir = new Dimension(1, 0);
		} else if (quad == 1) {
			primeDir = new Dimension(1, 0);
			sideDir = new Dimension(0, 1);
		} else if (quad == 2) {
			primeDir = new Dimension(0, 1);
			sideDir = new Dimension(-1, 0);
		} else {
			primeDir = new Dimension(-1, 0);
			sideDir = new Dimension(0, -1);
		}
		
		// Determine the spot of the illusion
		int spotX = player.getXPos();
		int spotY = player.getYPos();
		spotX += (primeDir.width * (edgeRange - quadIndex)) + (sideDir.width * quadIndex);
		spotY += (primeDir.height * (edgeRange - quadIndex)) + (sideDir.height * quadIndex);
		
		// Add illusion effect
		em.getEffectManager().addEffect(new EyeEffect(spotX, spotY));
	}
	
	// Creates a ring of illusions right on the edge of the player's vision
	private void createMadness() {
		EntityManager em = EntityManager.getInstance();
		Player player = em.getPlayer();
		
		// Determine edge range
		int edgeRange = (player.getVision() + 1);
		int numSpots = edgeRange * 4;
		
		// Surround the player with illusions
		for (int spot = 0; spot < numSpots; spot++) {
			// Determine the 'clock' quadrant and quadrant index
			int quad = (spot / edgeRange);
			int quadIndex = spot % edgeRange;
			
			// Determine the angular directions based on the indexed position
			Dimension primeDir = null;
			Dimension sideDir = null;
			if (quad == 0) {
				primeDir = new Dimension(0, -1);
				sideDir = new Dimension(1, 0);
			} else if (quad == 1) {
				primeDir = new Dimension(1, 0);
				sideDir = new Dimension(0, 1);
			} else if (quad == 2) {
				primeDir = new Dimension(0, 1);
				sideDir = new Dimension(-1, 0);
			} else {
				primeDir = new Dimension(-1, 0);
				sideDir = new Dimension(0, -1);
			}
			
			// Determine the spot of the illusion
			int spotX = player.getXPos();
			int spotY = player.getYPos();
			spotX += (primeDir.width * (edgeRange - quadIndex)) + (sideDir.width * quadIndex);
			spotY += (primeDir.height * (edgeRange - quadIndex)) + (sideDir.height * quadIndex);
			
			// Add illusion effect
			em.getEffectManager().addEffect(new EyeEffect(spotX, spotY));
		}
	}
	
}
