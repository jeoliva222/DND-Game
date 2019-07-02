package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import characters.Player;
import levels.MapArea;
import managers.EntityManager;

public class MapScreen extends JPanel {

	// Prevents warnings in Eclipse
	private static final long serialVersionUID = 1L;
	
	// Size of all tiles
	private static int tileSize = 80;
	
	// Height and width of GameScreen
	private static int mWidth, mHeight;
	
	protected MapScreen() {
		super();
		
		double scaleFactor = GameInitializer.scaleFactor;
		int xDimen = GameInitializer.xDimen;
		int yDimen = GameInitializer.yDimen;
		
		// Calculate tile size
		MapScreen.tileSize = (int) (scaleFactor * GameInitializer.tileArtSize);
		
		// Initialize screen size
		MapScreen.mWidth = xDimen * MapScreen.tileSize;
		MapScreen.mHeight = yDimen * MapScreen.tileSize;
		
		// Set the size of the screen
		Dimension size = new Dimension(MapScreen.mWidth, MapScreen.mHeight);
		this.setPreferredSize(size);
		
		// Set layout
		this.setLayout(new BorderLayout());
		
		// Set various attributes of MapScreen
		this.setVisible(false);
	}
	
	// Display the map of the current area
	protected void displayMap() {
		// Retrieve instance of EntityManager
		EntityManager em = EntityManager.getInstance();
		
		// Retrieve instance of player
		Player player = em.getPlayer();
		
		// Retrieve instance of current area
		MapArea area = em.getActiveArea();
		
		// Fetch length and height of area
		int aLength = area.getLength();
		int aHeight = area.getHeight();
		
		// Initialize interior map grid
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(aHeight, aLength));
		grid.setPreferredSize(this.getPreferredSize());
		
		// Populate map with data
		for(int y = 0; y < aHeight; y++) {
			String line = "";
			for(int x = 0; x < aLength; x++) {
				// Initialize panel
				JPanel p = new JPanel();
				p.setPreferredSize(new Dimension(MapScreen.tileSize, MapScreen.tileSize));
				p.setOpaque(true);
				p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
				
				// Set panel color based on area exploration and player position
				if(player.getLevelX() == x && player.getLevelY() == y) {
					line += "P - ";
					p.setBackground(Color.GREEN.brighter());
				} else {
					if(area.getLevel(x, y) == null) {
						line += "N - ";
						p.setBackground(Color.BLACK);
					} else if(area.getLevel(x, y).wasExplored()) {
						line += "X - ";
						p.setBackground(Color.BLUE.brighter());
					} else {
						line += "O - ";
						p.setBackground(Color.GRAY);
					}
				}
				
				// Add the new panel to the map grid
				grid.add(p);
			}
			System.out.println(line.substring(0, (line.length() - 3)));
		}
		
		System.out.println("------------");
		
		// Add the map grid to the screen
		this.add(grid, BorderLayout.CENTER);
		
		// Make screen visible
		this.setVisible(true);
	}
	
	// Hide the MapScreen from view
	protected void hideMap() {
		this.removeAll();
		this.setVisible(false);
	}
	
	public static int getMWidth() {
		return MapScreen.mWidth;
	}
	
	public static int getMHeight() {
		return MapScreen.mHeight;
	}
	
}
