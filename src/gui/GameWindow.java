package gui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import characters.GCharacter;
import characters.Player;
import helpers.GameState;
import helpers.SoundPlayer;
import items.GItem;
import levels.WorldMap;
import managers.EntityManager;
import projectiles.GProjectile;

// Window of the game, containing all the important game GUI elements as well as the game screen
public class GameWindow extends JFrame implements KeyListener {
	
	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Indicates whether a key is being pressed or not
	private static boolean isKeyDown = false;
	
	// Flag indicating whether the screen was changed on the current turn
	protected static boolean changedScreen = false;
	
	// Flag to save the game
	public static boolean shouldSave = false;
	
	// Manager to hold and deal with all game entities
	private static EntityManager entityManager;
	
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

	// Constructor
	public GameWindow() {
		super();

		///*** No loading functionality at the moment ***
		// Initializes the entity manager
		GameWindow.entityManager = new EntityManager();
		
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
		int xOffset = GameInitializer.xOffset;
		int yOffset = GameInitializer.yOffset;
		Dimension size = new Dimension((GameScreen.getGWidth() + StatusScreen.getStatusWidth() + InventoryScreen.getInvWidth() + xOffset),
				(GameScreen.getGHeight() + LogScreen.getLHeight() + yOffset));
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		
		// Set some extra parameters and then make visible
		this.setTitle("DnD Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);
		this.setVisible(true);
		this.pack();
		
		// "Caches" sound playing code by playing a silent sound.
		SoundPlayer.cacheSoundPlaying();
		
		// Play music
		SoundPlayer.playMidi(EntityManager.getPlayer().fetchArea().getMusic(), 30);
	}
	
	// Moves the player x-wise/y-wise then updates the screen to show it
	public void movePlayer(int dx, int dy) {
		// Get player position before switch
		int currentX = EntityManager.getPlayer().getXPos();
		int currentY = EntityManager.getPlayer().getYPos();
		
		// Move player if possible
		EntityManager.getPlayer().movePlayer(dx, dy);
		
		// Make the changes to the board
		this.shiftEntity(currentX, currentY);
		
	}
	
	public void moveCharacters() {
		// Iterate through all characters
		for(GCharacter gchar : EntityManager.getNPCManager().getCharacters()) {
			// Get character position before switch
			int currentX = gchar.getXPos();
			int currentY = gchar.getYPos();
			
			// Move character if possible
			gchar.takeTurn();
			
			// Make the changes to the board
			this.shiftEntity(currentX, currentY);
		}
	}
	
	// Takes turns for all the projectiles
	public void moveProjectiles() {
		// Iterate through all projectiles
		for(GProjectile proj : EntityManager.getProjectileManager().getProjectiles()) {
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
		if(!(playerDx == 0 && playerDy == 0))
			this.movePlayer(playerDx, playerDy);
		
		// If we changed screen, enemies and projectiles don't
		// act for this turn.
		if(GameWindow.changedScreen) {
			GameWindow.changedScreen = false;
			return;
		} else {
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
		// Get player image path
		String playerImage = EntityManager.getPlayer().getPlayerImage();
		
		// Get player position before switch
		int currentX = EntityManager.getPlayer().getXPos();
		int currentY = EntityManager.getPlayer().getYPos();
		
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
		for(GCharacter gchar : EntityManager.getNPCManager().getCharacters()) {
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
		for(GProjectile proj : EntityManager.getProjectileManager().getProjectiles()) {
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
		GameWindow.entityManager.manageAll();
		if(GameWindow.shouldSave) {
			this.saveGame();
			GameWindow.shouldSave = false;
		}
		this.updatePlayer();
		this.updateCharacters();
		this.updateProjectiles();
		this.updateGUI();
	}
	
	public void updateGUI() {
		LogScreen.displayLogs();
		StatusScreen.updateStatusScreen();
		InfoScreen.updateInfoScreen();
	}
	
	public void saveGame() {
		// Don't save if player is dead
    	if(EntityManager.getPlayer().isAlive()) {
    		// Grab items from inventory
        	GItem[] inv = InventoryScreen.getItemArray();
        	
        	// Get current player state
        	Player player = EntityManager.getPlayer();
        	
        	// Save the current level state
           	GameWindow.getScreen().saveLevel(false);
        	
           	// Grab the current world state
        	GameState gState = new GameState(inv, player, WorldMap.gameWorld);
        	
        	// Save the game to a save file
        	gState.saveGame();
        	
        	// Log that we've saved the game, then update the screen
        	LogScreen.log("Saved game!");
    	}
	}
	
	public void loadGame() {
		// Gets game save from file
    	GameState gState = GameState.loadGame();
    	
    	// If loading failed, don't do anything
    	if(gState == null) {
    		return;
    	}
    	
    	// Load the world from the save
    	WorldMap.setWorld(gState.gameWorld);
    	
    	// Load our inventory from the save
    	InventoryScreen.setItemArray(gState.inventory);
    	
    	// Load player from the save
    	EntityManager.setPlayer(gState.player);
    	
		// Defocus InfoScreen
		InfoScreen.defocusAll();
		
		// Clear the EntityManager
		EntityManager.removeEverything();
		
		// Clear the GameTile images
		for(int y = 0; y < GameInitializer.yDimen; y++) {
			for(int x = 0; x < GameInitializer.xDimen; x++) {
				GameScreen.getTile(x, y).clearAll();
			}
		}
		
		// Clear out the memory of the GameState
		gState.gameWorld = null;
		gState.inventory = null;
		gState.player = null;
    	
		// Load the level our player is at
		int areaX = EntityManager.getPlayer().getAreaX();
		int areaY = EntityManager.getPlayer().getAreaY();
		int levelX = EntityManager.getPlayer().getLevelX();
		int levelY = EntityManager.getPlayer().getLevelY();
		GameWindow.getScreen().loadLevel(WorldMap.getArea(areaX, areaY).getLevel(levelX, levelY));
		
		// Change music
		SoundPlayer.changeMidi(EntityManager.getPlayer().fetchArea().getMusic(), 30);
		
		// Log that we loaded the screen
		LogScreen.log("Loaded game...");
		
		// Updated the screen to show the changes
		StatusScreen.updateWeapons();
		this.updateAll();
	}
	
	// ****************
	// KeyListener Functions
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Disable key repeat
		if(GameWindow.isKeyDown) {
			return;
		} else {
			GameWindow.isKeyDown = true;
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
        	EntityManager.getPlayer().getWeapon().chargeWeapon();
        	this.completeTurn(0, 0);
        	this.updateAll();
        } 
        else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
        	// Hold position for the turn
        	EntityManager.getPlayer().getWeapon().dischargeWeapon();
        	this.completeTurn(0, 0);
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_M) {
        	///*** DEBUG: Damages player by 1
        	EntityManager.getPlayer().damagePlayer(1);
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_N) {
        	///*** DEBUG: Heals player by 1
        	EntityManager.getPlayer().healPlayer(1, false);
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_Z) {
        	// Shift inventory selector to the left
        	InventoryScreen.shiftSelected(-1);
        }
        else if(e.getKeyCode() == KeyEvent.VK_X) {
        	// Shift inventory selector to the right
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
        	// Discards selected item
        	InventoryScreen.discardSelected();
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
        	// Swaps active weapon
        	EntityManager.getPlayer().swapEquippedWeapon();
        	this.completeTurn(0, 0);
        	this.updateAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_F9) {
        	// Load the player's save file
        	this.loadGame();
        }
//      else if(e.getKeyCode() == KeyEvent.VK_F5) {
//    	/// TEST ***
//    	this.saveGame();
//    	this.updateAll();
//    }
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
	
	public static EntityManager getEntityManager() {
		return GameWindow.entityManager;
	}
	
	
}
