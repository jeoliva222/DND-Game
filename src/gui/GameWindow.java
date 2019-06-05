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

import javax.swing.JFrame;

import characters.GCharacter;
import characters.Player;
import helpers.GPath;
import helpers.GameState;
import helpers.SoundPlayer;
import items.GItem;
import managers.EntityManager;
import projectiles.GProjectile;

// Window of the game, containing all the important game GUI elements as well as the game screen
public class GameWindow extends JFrame implements KeyListener {
	
	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Indicates whether a key is being pressed or not
	private static boolean isKeyDown = false;
	
	// Indicates whether a turn is currently in progress, preventing buffering inputs
	private static boolean turnInProgress = false;
	
	// Flag indicating whether the screen was changed on the current turn
	protected static boolean changedScreen = false;
	
	// Flag to save the game
	public static boolean shouldSave = false;
	
	// Flag indicating whether game is in debug mode (VM ARG: -Ddebug="T")
	public static boolean isDebug = false;
	
	// GameScreen for displaying the game
	private static GameScreen screen;
	
	// LogScreen for displaying logs about in-game turn details
	private static LogScreen logs;
	
	// StatusScreen for showing player information
	private static StatusScreen statusHUD;
	
	// DebuffScreen for displaying information about current buffs/debuffs
	private static DebuffScreen debuffHUD;
	
	// InventoryScreen for displaying the player's inventory items
	private static InventoryScreen inventoryHUD;
	
	// InfoScreen for displaying info about various items/enemies
	private static InfoScreen infoHUD;
	
	// Adapter used to regain focus on the game when screen is clicked
	private MouseAdapter focusAdapter;

	// Constructor
	public GameWindow() {
		super();
		
		// Initialize GameScreen within window
		GameWindow.screen = new GameScreen();
		
		// Initialize LogScreen within window
		GameWindow.logs = new LogScreen();
		
		// Initialize StatusScreen within window
		GameWindow.statusHUD = new StatusScreen();
		
		// Initialize DebuffScreen within window
		GameWindow.debuffHUD = new DebuffScreen();
		
		// Initialize InventoryScreen within window
		GameWindow.inventoryHUD = new InventoryScreen();
		
		// Initialize InfoScreen within the window
		GameWindow.infoHUD = new InfoScreen();
		
		/// Spawn Player and Character **TEMPORARY**
		this.updateAll();

		// Set content layout to null
		this.setLayout(null);
		
		// Add screens to game --------------
		
		// Main screen 
		this.add(GameWindow.screen);
		GameWindow.screen.setBounds(StatusScreen.getStatusWidth(), 0, GameScreen.getGWidth(), GameScreen.getGHeight());
		
		// Log Screen
		this.add(GameWindow.logs);
		GameWindow.logs.setBounds(StatusScreen.getStatusWidth(), GameScreen.getGHeight(), LogScreen.getLWidth(), LogScreen.getLHeight());
		
		// Status Screen
		this.add(GameWindow.statusHUD);
		GameWindow.statusHUD.setBounds(0, 0, StatusScreen.getStatusWidth(), StatusScreen.getStatusHeight());
		
		// Buff/Debuff Screen
		this.add(GameWindow.debuffHUD);
		GameWindow.debuffHUD.setBounds(0, StatusScreen.getStatusHeight(), DebuffScreen.getDebuffWidth(), DebuffScreen.getDebuffHeight());
		
		// Inventory Screen
		this.add(GameWindow.inventoryHUD);
		GameWindow.inventoryHUD.setBounds((StatusScreen.getStatusWidth() + GameScreen.getGWidth()), 0, InventoryScreen.getInvWidth(), InventoryScreen.getInvHeight());
		
		// Info Screen
		this.add(GameWindow.infoHUD);
		GameWindow.infoHUD.setBounds((StatusScreen.getStatusWidth() + GameScreen.getGWidth()), InventoryScreen.getInvHeight(), InfoScreen.getInfoWidth(), InfoScreen.getInfoHeight());
		
		// Set window size parameters
		int xOffset = (int) (GameInitializer.xOffset * GameInitializer.scaleFactor);
		int yOffset = (int) (GameInitializer.yOffset * GameInitializer.scaleFactor);
		Dimension size = new Dimension((GameScreen.getGWidth() + StatusScreen.getStatusWidth() + InventoryScreen.getInvWidth() + xOffset),
				(GameScreen.getGHeight() + LogScreen.getLHeight() + yOffset));
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		
		// Set on-close listener
	    this.addWindowListener(new WindowAdapter() {
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
	    		GameWindow.this.requestFocus();
	    		System.out.println("Click regained focus.");
	    	}
	    };
	    this.addMouseListener(this.focusAdapter);
	    GameWindow.infoHUD.addMouseListener(this.focusAdapter);
	    
	    // Checks for debug mode
	    String debug = System.getProperty("debug");
	    if(debug != null && debug.equals("T")) {
	    	GameWindow.isDebug = true;
	    }
		
		// "Caches" sound playing code by playing a silent sound.
	    // This allows the first sound to play without a noticeable delay
		SoundPlayer.cacheSoundPlaying();
		
		// Start playing music
		SoundPlayer.playMidi(EntityManager.getInstance().getActiveArea().getMusic(),
				EntityManager.getInstance().getActiveArea().getMusicVolume());
		
		// Set some extra parameters and then make visible
		this.setTitle("Frog VS World");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addKeyListener(this);
		this.setVisible(true);
		this.pack();
	}
	
	// Moves the player x-wise/y-wise then updates the screen to show it
	public void movePlayer(int dx, int dy) {
		// Fetch reference to player
		Player plr = EntityManager.getInstance().getPlayer();
		
		// Update last position
		plr.updateLastCoords();
		
		// Only move player if we have a direction
		if(!(dx == 0 && dy == 0)) {
			// Get player position before switch
			int currentX = plr.getXPos();
			int currentY = plr.getYPos();
			
			// Move player if possible
			plr.movePlayer(dx, dy);
			
			// Make the changes to the board
			this.shiftEntity(currentX, currentY);
		}
		
	}
	
	// Iterates through all 
	public void moveCharacters() {
		// Iterate through all characters
		for(GCharacter gchar : EntityManager.getInstance().getNPCManager().getCharacters()) {
			// Get character position before switch
			int currentX = gchar.getXPos();
			int currentY = gchar.getYPos();
			
			// Update last positions
			gchar.updateLastCoords();
			
			// Move character if possible
			gchar.takeTurn();
			
			// Make the changes to the board
			this.shiftEntity(currentX, currentY);
		}
	}
	
	// Takes turns for all the projectiles
	public void moveProjectiles() {
		// Iterate through all projectiles
		for(GProjectile proj : EntityManager.getInstance().getProjectileManager().getProjectiles()) {
			// Get projectile position before switch
			int currentX = proj.getXPos();
			int currentY = proj.getYPos();
			
			// Move projectile if possible
			proj.takeTurn();
			
			// Make the changes to the board
			this.shiftProjectile(currentX, currentY, proj);
		}
	}
	
	// Takes turns for all entities on screen and moves player by specified amount
	public void completeTurn(int playerDx, int playerDy) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		System.out.println(dateFormat.format(new Date()) + " " + "Moving Player");
		// Move the player
		this.movePlayer(playerDx, playerDy);
		
		// If we changed screen, enemies and projectiles don't
		// act for this turn.
		if(GameWindow.changedScreen) {
			GameWindow.changedScreen = false;
			return;
		} else {
			System.out.println(dateFormat.format(new Date()) + " " + "Moving Characters/Projectiles");
    		this.moveCharacters();
    		this.moveProjectiles();
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
		if(updateTile != null)
			updateTile.setFG(playerImage);
	}
	
	// Update image[s] for characters
	public void updateCharacters() {
		// Iterate through all characters
		for(GCharacter gchar : EntityManager.getInstance().getNPCManager().getCharacters()) {
			// Get character image path
			String charImage = gchar.getImage();
			
			// Get npc position before switch
			int currentX = gchar.getXPos();
			int currentY = gchar.getYPos();
			
			// Fetch tile the npc is on
			GameTile updateTile = null;
			try {
				updateTile = GameScreen.getTile(currentX, currentY);
			} catch (IndexOutOfBoundsException e) {
				// If npc is out-of-bounds, don't set tile value
			}
			
			// Set new image path for npc's tile if not null
			if(updateTile != null)
				updateTile.setFG(charImage);
		}
	}
	
	// Update image[s] for projectiles
	public void updateProjectiles() {
		// Iterate through all projectiles
		for(GProjectile proj : EntityManager.getInstance().getProjectileManager().getProjectiles()) {
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
			if(updateTile != null)
				updateTile.setProjectileImage(projImage, proj);
		}
	}
	
	// Updates everything in the window
	public void updateAll() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		System.out.println(dateFormat.format(new Date()) + " " + "Managing entities");
		EntityManager.getInstance().manageAll();
		if(GameWindow.shouldSave) {
			this.saveGame();
			GameWindow.shouldSave = false;
		}
		System.out.println(dateFormat.format(new Date()) + " " + "Rendering changes");
		this.updatePlayer();
		this.updateCharacters();
		this.updateProjectiles();
		this.updateGUI();
		System.out.println(dateFormat.format(new Date()) + " " + "Turn finished!");
		System.out.println("------------");
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
    	if(EntityManager.getInstance().getPlayer().isAlive()) {
    		// Grab items from inventory
        	GItem[] inv = InventoryScreen.getItemArray();
        	
        	// Get current player state
        	Player player = EntityManager.getInstance().getPlayer();
        	
        	// Save the current level state without reseting NPC locations
           	GameWindow.getScreen().saveLevel(false);
        	
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
		// Only load if we have a save file
		if(!(new File(GPath.SAVE + "player.ser").exists())) {
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
		for(int y = 0; y < GameInitializer.yDimen; y++) {
			for(int x = 0; x < GameInitializer.xDimen; x++) {
				GameScreen.getTile(x, y).clearAll();
			}
		}
    	
		// Set the new active area
		EntityManager.getInstance().setActiveArea(em.getPlayer().fetchArea());
		
		// Load the level our player is at
		int levelX = em.getPlayer().getLevelX();
		int levelY = em.getPlayer().getLevelY();
		GameWindow.getScreen().loadLevel(em.getActiveArea().getLevel(levelX, levelY));
		
		// Change music
		SoundPlayer.changeMidi(em.getActiveArea().getMusic(), 15);
		
		// Log that we loaded the screen
		LogScreen.log("Loaded game...");
		
		// Updated the screen to show the changes
		this.updateAll();
	}
	
	// ****************
	// KeyListener Functions
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Disable key repeat
		if(GameWindow.isKeyDown || GameWindow.turnInProgress) {
			return;
		} else {
			GameWindow.isKeyDown = true;
			GameWindow.turnInProgress = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode()== KeyEvent.VK_D) {
			// Move right
			this.completeTurn(1, 0);
			this.updateAll();
		}
        else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
        		e.getKeyCode()== KeyEvent.VK_A) {
        	// Move left
        	this.completeTurn(-1, 0);
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
        		e.getKeyCode()== KeyEvent.VK_S) {
        	// Move down
        	this.completeTurn(0, 1);
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP ||
        		e.getKeyCode()== KeyEvent.VK_W) {
        	// Move up
        	this.completeTurn(0, -1);
        	this.updateAll();
        } 
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
        	// Charge weapon and hold position for the turn
        	EntityManager.getInstance().getPlayer().chargeWeapons();
        	this.completeTurn(0, 0);
        	this.updateAll();
        } 
        else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
        	// Hold position for the turn, discharging player weapons
        	EntityManager.getInstance().getPlayer().dischargeWeapons();
        	this.completeTurn(0, 0);
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_Z) {
        	// Shift inventory selector to the left without consuming turn
        	InventoryScreen.shiftSelected(-1);
        }
        else if(e.getKeyCode() == KeyEvent.VK_X) {
        	// Shift inventory selector to the right without consuming turn
        	InventoryScreen.shiftSelected(1);
        }
        else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        	// Uses selected item
        	if(InventoryScreen.useSelected()) {
            	this.completeTurn(0, 0);
        	}
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        	// Discards selected item in inventory without consuming turn
        	InventoryScreen.discardSelected();
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
        	// Swaps active weapon with offhand weapon without consuming turn
        	EntityManager.getInstance().getPlayer().swapEquippedWeapon();
        }
        else if(e.getKeyCode() == KeyEvent.VK_F9) {
        	// Load the player's save file
        	this.loadGame();
        }
        else if(GameWindow.isDebug && e.getKeyCode() == KeyEvent.VK_M) {
        	// *** DEBUG: Damages player by 1
        	EntityManager.getInstance().getPlayer().damagePlayer(1);
        	this.updateAll();
        }
        else if(GameWindow.isDebug && e.getKeyCode() == KeyEvent.VK_N) {
        	// *** DEBUG: Heals player by 1
        	EntityManager.getInstance().getPlayer().healPlayer(1, false);
        	this.updateAll();
        }
	    else if(GameWindow.isDebug && e.getKeyCode() == KeyEvent.VK_F5) {
	    	// *** DEBUG: Saves the game
	    	this.saveGame();
	    	this.updateAll();
	    }
		
		// If we're in a dark area, refresh all tiles every move
		if(EntityManager.getInstance().getActiveArea().showDark()) {
			GameWindow.screen.refreshTiles();
		}
		
		// Indicate turn is finished
		GameWindow.turnInProgress = false;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// Mark that the key has been released and
		// that we can hit another key
		GameWindow.isKeyDown = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing
	}
	
	// ****************
	// Getters and setters
	
	public static GameScreen getScreen() {
		return GameWindow.screen;
	}
	
	@SuppressWarnings("unused")
	private static void setScreen(GameScreen setter) {
		GameWindow.screen = setter;
	}
	
}
