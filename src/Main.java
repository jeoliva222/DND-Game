import javax.swing.SwingUtilities;

import gui.GameWindow;

@SuppressWarnings("unused")
public class Main {
	
	// Main function that initializes and runs the game
	public static void main(String[] args) {
		// Launch the game through creating a new GameWindow
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameWindow window = GameWindow.getInstance();
			}
		});
		
	}

}
