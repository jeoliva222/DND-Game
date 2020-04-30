package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;

import helpers.GPath;
import helpers.GameState;

public class MainMenuFrame extends JFrame implements KeyListener {

	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Singleton instance
	private static MainMenuFrame instance = null;
	
	// Frame width and height
	private static int screenWidth = 800;
	private static int screenHeight = 800;
	
	private JButton newGameButton;
	private JButton loadGameButton;
	
	//private int buttonIndex = 0;
	//private JButton[] buttonList;
	
	private MainMenuFrame() {
		super();
		
		// Initialize buttons
		this.newGameButton = new JButton("New Game");
		this.loadGameButton = new JButton("Load Game");
		
		// Add button listeners - TEMP TODO
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewGame();
				dispose();
			}
		});
		loadGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean result = loadGame();
				if (result) {
					dispose();
				}
			}
		});
		
		// Set the frame size
		screenWidth = (int) (screenWidth * GameInitializer.scaleFactor);
		screenHeight = (int) (screenHeight * GameInitializer.scaleFactor);
		Dimension size = new Dimension(screenWidth, screenHeight);
		setPreferredSize(size);
		setMinimumSize(size);
		
		// Set content layout to null
		setLayout(null);
		
		// Set component positioning
		newGameButton.setBounds(screenWidth / 4, screenHeight / 2, screenWidth / 2, screenHeight / 8);
		loadGameButton.setBounds(screenWidth / 4, screenHeight * 3/4, screenWidth / 2, screenHeight / 8);
		
		// Add components
		add(newGameButton);
		add(loadGameButton);
		
		// Set some extra parameters and then make visible
		setTitle("Frog VS World");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		pack();
		setVisible(true);
	}
	
	private void createNewGame() {
		// Create Game Window with new game session
		@SuppressWarnings("unused")
		GameWindow window = GameWindow.getInstance();
	}
	
	private boolean loadGame() {
		// Check for a player save file
		if (!(new File(GPath.SAVE + GameState.PLAYER + GameState.SUFFIX).exists())) {
			return false;
		}
		
		// Create Game Window and then immediately load the save file
		GameWindow window = GameWindow.getInstance();
		window.loadGame();
		return true;
	}
	
	// Singleton instance getter
	public static MainMenuFrame getInstance() {
		if (instance == null) {
			instance = new MainMenuFrame();
		}
		
		return instance;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Help navigate the menu - TODO
		if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode()== KeyEvent.VK_D) {
			// Navigate right
		}
        else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
        		e.getKeyCode()== KeyEvent.VK_A) {
        	// Navigate left
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
        		e.getKeyCode()== KeyEvent.VK_S) {
        	// Navigate down
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP ||
        		e.getKeyCode()== KeyEvent.VK_W) {
        	// Navigate up
        } 
        else if (e.getKeyCode() == KeyEvent.VK_SPACE ||
        		e.getKeyCode() == KeyEvent.VK_ENTER) {
        	// Make the current selection
        } 
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Do nothing
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// Do nothing
	}

}
