package gui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.UIManager;

import characters.GCharacter;
import characters.allies.Player;
import helpers.GPath;
import helpers.GameState;
import helpers.SoundPlayer;
import items.GItem;
import managers.EntityManager;
import projectiles.GProjectile;

/**
 * Main frame class of the application, containing all the important game GUI elements as well as the game screen
 * @author jeoliva
 */
public class GameWindow extends JFrame implements KeyListener {
	
	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Singleton instance of window
	private static GameWindow instance = null;
	
	// Indicates whether a key is being pressed or not
	private HashSet<Integer> keysDown = new HashSet<>();
	
	// Indicates whether the spacebar is being pressed or not
	private boolean isSpaceDown = false;
	
	// Indicates whether a turn is currently in progress, preventing buffering inputs
	private boolean turnInProgress = false;
	
	// Flag indicating whether the screen was changed on the current turn
	protected boolean changedScreen = false;
	
	// Flag indicating whether the map is currently up on the screen
	protected boolean mapDisplayed = false;
	
	// Flag to save the game
	public boolean shouldSave = false;
	
	// Flag indicating whether game is in debug mode (VM ARG: -Ddebug="T")
	public boolean isDebug = false;
	
	// MapScreen for displaying map of the current area
	private MapScreen map;
	
	// GameScreen for displaying the game
	private GameScreen screen;
	
	// LogScreen for displaying logs about in-game turn details
	private LogScreen logs;
	
	// StatusScreen for showing player information
	private StatusScreen statusHUD;
	
	// DebuffScreen for displaying information about current buffs/debuffs
	private DebuffScreen debuffHUD;
	
	// InventoryScreen for displaying the player's inventory items
	private InventoryScreen inventoryHUD;
	
	// InfoScreen for displaying info about various items/enemies
	private InfoScreen infoHUD;
	
	// Adapter used to regain focus on the game when screen is clicked
	private MouseAdapter focusAdapter;

	// Constructor
	private GameWindow() {
		super();
		
		// Sets cross-platform look and feel
        try {
        	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
        	System.out.println("Error initializing look and feel in GameWindow!");
        	ex.printStackTrace();
        }
		
		// Initialize MapScreen within window
		map = new MapScreen();
		
		// Initialize GameScreen within window
		screen = new GameScreen();
		
		// Initialize LogScreen within window
		logs = new LogScreen();
		
		// Initialize StatusScreen within window
		statusHUD = new StatusScreen();
		
		// Initialize DebuffScreen within window
		debuffHUD = new DebuffScreen();
		
		// Initialize InventoryScreen within window
		inventoryHUD = new InventoryScreen();
		
		// Initialize InfoScreen within the window
		infoHUD = new InfoScreen();
		
		/// Draw Player and Character
		updateAll();

		// Set content layout to null
		setLayout(null);
		
		// Add screens to game --------------
		
		// Map screen 
		add(map);
		map.setBounds(StatusScreen.getStatusWidth(), 0, MapScreen.getMWidth(), MapScreen.getMHeight());
		
		// Main screen 
		add(screen);
		screen.setBounds(StatusScreen.getStatusWidth(), 0, GameScreen.getGWidth(), GameScreen.getGHeight());
		
		// Log Screen
		add(logs);
		logs.setBounds(StatusScreen.getStatusWidth(), GameScreen.getGHeight(), LogScreen.getLWidth(), LogScreen.getLHeight());
		
		// Status Screen
		add(statusHUD);
		statusHUD.setBounds(0, 0, StatusScreen.getStatusWidth(), StatusScreen.getStatusHeight());
		
		// Buff/Debuff Screen
		add(debuffHUD);
		debuffHUD.setBounds(0, StatusScreen.getStatusHeight(), DebuffScreen.getDebuffWidth(), DebuffScreen.getDebuffHeight());
		
		// Inventory Screen
		add(inventoryHUD);
		inventoryHUD.setBounds((StatusScreen.getStatusWidth() + GameScreen.getGWidth()), 0, InventoryScreen.getInvWidth(), InventoryScreen.getInvHeight());
		
		// Info Screen
		add(infoHUD);
		infoHUD.setBounds((StatusScreen.getStatusWidth() + GameScreen.getGWidth()), InventoryScreen.getInvHeight(), InfoScreen.getInfoWidth(), InfoScreen.getInfoHeight());
		
		// Set window size parameters
		int xOffset = (int) (GameInitializer.xOffset * GameInitializer.scaleFactor);
		int yOffset = (int) (GameInitializer.yOffset * GameInitializer.scaleFactor);
		Dimension size = new Dimension((GameScreen.getGWidth() + StatusScreen.getStatusWidth() + InventoryScreen.getInvWidth() + xOffset),
				(GameScreen.getGHeight() + LogScreen.getLHeight() + yOffset));
		setPreferredSize(size);
		setMinimumSize(size);
		
		// Set on-close listener
	    addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent event) {
	        	// Delete all temporary files and exit the frame on close
	        	GameState.deleteTempSaves();
	            System.exit(0);
	        }
	    });
	    
	    // TODO WIP: Click-listener to regain focus on window after hitting tab over InfoScreen / clicking out
	    this.focusAdapter = new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent me) {
	    		requestFocus();
	    		System.out.println("Click regained focus.");
	    	}
	    };
	    addMouseListener(focusAdapter);
	    infoHUD.addMouseListener(focusAdapter);
	    
	    // Checks for debug mode
	    String debug = System.getProperty("debug");
	    if (debug != null && debug.equals("T")) {
	    	isDebug = true;
	    }
		
		// "Caches" sound playing code by playing a silent sound.
	    // This allows the first sound to play without a noticeable delay
		SoundPlayer.cacheSoundPlaying();
		
		// Start playing music
		SoundPlayer.playMidi(EntityManager.getInstance().getActiveArea().getMusic(),
				EntityManager.getInstance().getActiveArea().getMusicVolume());
		
		// Set some extra parameters and then make visible
		setTitle("Frog VS World");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addKeyListener(this);
		pack();
		setVisible(true);
	}
	
	// Moves the player x-wise/y-wise then updates the screen to show it
	public void movePlayer(int dx, int dy) {
		// Fetch reference to player
		Player plr = EntityManager.getInstance().getPlayer();
		
		// Get player position before switch
		int currentX = plr.getXPos();
		int currentY = plr.getYPos();
		
		// Move player if possible
		plr.movePlayer(dx, dy);
		
		// Make the changes to the board
		shiftEntity(currentX, currentY);
		
		// Persist debuffs
		plr.persistBuffs();
	}
	
	// Iterates through all 
	public void moveCharacters() {
		// Iterate through all characters
		for (GCharacter gchar : EntityManager.getInstance().getNPCManager().getCharacters()) {
			// Get character position before switch
			int currentX = gchar.getXPos();
			int currentY = gchar.getYPos();
			
			// Update last positions
			gchar.updateLastCoords();
			
			// Move character if possible
			gchar.takeTurn();
			
			// Persist debuffs for the character
			gchar.persistBuffs();
			
			// Make the changes to the board
			shiftEntity(currentX, currentY);
		}
	}
	
	// Takes turns for all the projectiles
	public void moveProjectiles() {
		// Iterate through all projectiles
		for (GProjectile proj : EntityManager.getInstance().getProjectileManager().getProjectiles()) {
			// Get projectile position before switch
			int currentX = proj.getXPos();
			int currentY = proj.getYPos();
			
			// Move projectile if possible
			proj.takeTurn();
			
			// Make the changes to the board
			shiftProjectile(currentX, currentY, proj);
		}
	}
	
	// Takes turns for all entities on screen and moves player by specified amount
	public void completeTurn(int playerDx, int playerDy) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		System.out.println(dateFormat.format(new Date()) + " " + "Moving Player");
		
		// Move the player
		movePlayer(playerDx, playerDy);
		
		// If we changed screen, enemies and projectiles don't
		// act for this turn.
		if (changedScreen) {
			changedScreen = false;
			return;
		} else {
			System.out.println(dateFormat.format(new Date()) + " " + "Moving Characters/Projectiles");
    		moveCharacters();
    		moveProjectiles();
		}
	}
	
	// Clears an entity's old position
	public void shiftEntity(int oldX, int oldY) {
		GameTile oldTile = GameScreen.getTile(oldX, oldY);
		oldTile.clearFG();
	}
	
	// Clears a projectile's old position
	public void shiftProjectile(int oldX, int oldY, GProjectile proj) {
		GameTile oldTile = GameScreen.getTile(oldX, oldY);	
		oldTile.clearProj(proj);
	}
	
	// Updates image for player
	public void updatePlayer() {
		// Fetch instance of player
		Player plr = EntityManager.getInstance().getPlayer();
		
		// Get player image path
		String playerImage = plr.getPlayerImage();
		
		// Get player position before switch
		int currentX = plr.getXPos();
		int currentY = plr.getYPos();
		
		// Fetch tile the player is on
		GameTile updateTile = null;
		try {
			updateTile = GameScreen.getTile(currentX, currentY);
		} catch (IndexOutOfBoundsException e) {
			// If player is out-of-bounds, don't set tile value
		}
		
		// Set new image path for player's tile
		if (updateTile != null) {
			updateTile.setFG(playerImage);
		}
	}
	
	// Update image[s] for characters
	public void updateCharacters() {
		// Iterate through all characters
		for (GCharacter gchar : EntityManager.getInstance().getNPCManager().getCharacters()) {
			// Get character image path
			String charImage = gchar.getImage();
			
			// Get NPC position before switch
			int currentX = gchar.getXPos();
			int currentY = gchar.getYPos();
			
			// Fetch tile the NPC is on
			GameTile updateTile = null;
			try {
				updateTile = GameScreen.getTile(currentX, currentY);
			} catch (IndexOutOfBoundsException e) {
				// If NPC is out-of-bounds, don't set tile value
			}
			
			// Set new image path for NPC's tile if not null
			if (updateTile != null) {
				updateTile.setFG(charImage);
			}
		}
	}
	
	// Update image[s] for projectiles
	public void updateProjectiles() {
		// Iterate through all projectiles
		for (GProjectile proj : EntityManager.getInstance().getProjectileManager().getProjectiles()) {
			// Get projectile image path
			String projImage = proj.getImage();
			
			// Get projectile position before switch
			int currentX = proj.getXPos();
			int currentY = proj.getYPos();
			
			// Fetch tile the projectile is on
			GameTile updateTile = null;
			try {
				updateTile = GameScreen.getTile(currentX, currentY);
			} catch (IndexOutOfBoundsException e) {
				// If projectile is out-of-bounds, don't set tile value
			}
			
			// Set new image path for projectile's tile if not null
			if (updateTile != null) {
				updateTile.setProjectileImage(projImage, proj);
			}
		}
	}
	
	// Updates everything in the window
	public void updateAll() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		//--
		System.out.println(dateFormat.format(new Date()) + " " + "Managing entities");
		EntityManager.getInstance().manageAll();
		if (shouldSave) {
			saveGame();
			shouldSave = false;
		}
		//--
		System.out.println(dateFormat.format(new Date()) + " " + "Rendering changes");
		updatePlayer();
		updateCharacters();
		updateProjectiles();
		updateGUI();
		//--
		System.out.println(dateFormat.format(new Date()) + " " + "Turn finished!");
		printMemoryUsage();
		System.out.println("------------");
	}
	
	private void printMemoryUsage() {
		final long ONE_MB = 1048576L;
		Runtime runtime = Runtime.getRuntime();
		long maxHeapSize = runtime.maxMemory() / ONE_MB;
		long currentHeapSize = runtime.totalMemory() / ONE_MB;
		long freeHeapSize = runtime.freeMemory() / ONE_MB;
		long usedHeapSize = currentHeapSize - freeHeapSize;
		System.out.println("Using " + usedHeapSize + "M of " + currentHeapSize + "M");
		System.out.println("Maximum heap size is " + maxHeapSize + "M");
	}
	
	// Updates all of the GUI on the screen
	public void updateGUI() {
		LogScreen.displayLogs();
		StatusScreen.updateStatusScreen();
		InfoScreen.updateInfoScreen();
		StatusScreen.updateWeapons();
	}
	
	// Saves the game to a save file
	public void saveGame() {
		// Don't save if player is dead
    	if (EntityManager.getInstance().getPlayer().isAlive()) {
    		// Grab items from inventory
        	GItem[] inv = InventoryScreen.getItemArray();
        	
        	// Get current player state
        	Player player = EntityManager.getInstance().getPlayer();
        	
        	// Save the current level state without reseting NPC locations
           	getScreen().saveLevel(false);
        	
           	// Save the current area to a file in the temp folder
           	GameState.saveCurrentArea();
           	
           	// Save the current world state
        	GameState.saveGame(inv, player);
        	
        	// Log that we've saved the game, then update the screen
        	LogScreen.log("Saved game!");
    	}
	}
	
	// Loads the game from the save file
	public void loadGame() {
		// We don't want to load without all of the files!
		// Only load if we have a player save file
		if (!(new File(GPath.SAVE + GameState.PLAYER + GameState.SUFFIX).exists())) {
			return;
		}
		
		// Fetch instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Gets game save from file (Sets player and inventory from save file)
    	GameState.loadGame();
    	
		// Defocus InfoScreen
		InfoScreen.defocusAll();
		
		// Clear the EntityManager
		em.removeEverything();
		
		// Clear the GameTile images
		for (int y = 0; y < GameInitializer.yDimen; y++) {
			for (int x = 0; x < GameInitializer.xDimen; x++) {
				GameScreen.getTile(x, y).clearAll();
			}
		}
    	
		// Set the new active area
		EntityManager.getInstance().setActiveArea(em.getPlayer().fetchArea());
		
		// Load the level our player is at
		getScreen().loadLevel(em.getCurrentLevel());
		
		// Change music
		SoundPlayer.changeMidi(em.getActiveArea().getMusic(), em.getActiveArea().getMusicVolume());
		
		// Log that we loaded the screen
		LogScreen.log("Loaded game...");
		
		// Updated the screen to show the changes
		updateAll();
	}
	
	// ****************
	// KeyListener Functions
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Map mode
		if (mapDisplayed) {
			mapDisplayed = false;
			map.hideMap();
			return;
		}
		
		Integer keyCode = Integer.valueOf(e.getKeyCode());
        if (!isSpaceDown && e.getKeyCode() == KeyEvent.VK_SPACE) {
        	// Charge active weapon
        	isSpaceDown = true;
        	EntityManager.getInstance().getPlayer().chargeActiveWeapon();
        	StatusScreen.updateWeapons();
        	return;
        } 
		
		//---------------
		
		// Disable key repeat
		if (keysDown.contains(keyCode) || turnInProgress) {
			return;
		}
		
		// Mark turn in progress
		keysDown.add(keyCode);
		turnInProgress = true;
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode()== KeyEvent.VK_D) {
			// Move right
			completeTurn(1, 0);
			updateAll();
		}
        else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
        		e.getKeyCode()== KeyEvent.VK_A) {
        	// Move left
        	completeTurn(-1, 0);
        	updateAll();
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
        		e.getKeyCode()== KeyEvent.VK_S) {
        	// Move down
        	completeTurn(0, 1);
        	updateAll();
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP ||
        		e.getKeyCode()== KeyEvent.VK_W) {
        	// Move up
        	completeTurn(0, -1);
        	updateAll();
        } 
		if (!isSpaceDown) {
	        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
	        	// Hold position for the turn, discharging player weapons
	        	EntityManager.getInstance().getPlayer().dischargeWeapons();
	        	completeTurn(0, 0);
	        	updateAll();
	        }
	        else if (e.getKeyCode() == KeyEvent.VK_Z) {
	        	// Shift inventory selector to the left without consuming turn
	        	InventoryScreen.shiftSelected(-1);
	        }
	        else if (e.getKeyCode() == KeyEvent.VK_X) {
	        	// Shift inventory selector to the right without consuming turn
	        	InventoryScreen.shiftSelected(1);
	        }
	        else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	        	// Uses selected item
	        	if (InventoryScreen.useSelected()) {
	            	completeTurn(0, 0);
	        	}
	        	updateAll();
	        }
	        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	        	// Discards selected item in inventory without consuming turn
	        	InventoryScreen.discardSelected();
	        	updateAll();
	        }
	        else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
	        	// Swaps active weapon with offhand weapon without consuming turn
	        	EntityManager.getInstance().getPlayer().swapEquippedWeapon();
	        }
	        else if (e.getKeyCode() == KeyEvent.VK_M) {
	        	// Display the map of the area
	        	mapDisplayed = true;
	        	map.displayMap();
	        	LogScreen.log("Green = You / Blue = Explored");
	        	updateGUI();
	        }
	        else if (e.getKeyCode() == KeyEvent.VK_F9) {
	        	// Load the player's save file
	        	loadGame();
	        }
	        else if (isDebug && e.getKeyCode() == KeyEvent.VK_9) {
	        	// *** DEBUG: Damages player by 1
	        	EntityManager.getInstance().getPlayer().damagePlayer(1);
	        	updateAll();
	        }
	        else if (isDebug && e.getKeyCode() == KeyEvent.VK_0) {
	        	// *** DEBUG: Heals player by 1
	        	EntityManager.getInstance().getPlayer().healPlayer(1, false);
	        	updateAll();
	        }
		    else if (isDebug && e.getKeyCode() == KeyEvent.VK_F5) {
		    	// *** DEBUG: Saves the game
		    	saveGame();
		    	updateAll();
		    }
		}
		
		// If we're in a dark area or level, refresh all tiles every move
		if (EntityManager.getInstance().isDark()) {
			screen.refreshTiles();
		}
		
		// Indicate turn is finished
		turnInProgress = false;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// Check for space key release
		if (isSpaceDown && e.getKeyCode() == KeyEvent.VK_SPACE) {
    		// Discharge our weapons
			isSpaceDown = false;
        	EntityManager.getInstance().getPlayer().dischargeWeapons();
        	StatusScreen.updateWeapons();
        }
		
		// Remove memory of key press
		keysDown.remove(Integer.valueOf(e.getKeyCode()));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing
	}
	
	// ****************
	// Getters and setters
	
	public static GameWindow getInstance() {
		if (instance == null) {
			instance = new GameWindow();
		}
		
		return instance;
	}
	
	public static GameScreen getScreen() {
		return GameWindow.getInstance().screen;
	}
	
	/**
	 * Indicates to the GameWindow that the game should save after this turn.
	 */
	public void markSavingGame() {
		this.changedScreen = true;
	}
	
	/**
	 * Indicates to the GameWindow that the screen will be changing this turn.
	 */
	protected void markChangingScreen() {
		this.changedScreen = true;
	}
	
}
