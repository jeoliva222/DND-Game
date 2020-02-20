package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

// Displays the player's image in the StatusScreen
public class EntityImagePanel extends JPanel {
	
	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Images to draw
	private Image tileImage;
	private Image entityImage;
	
	private boolean doTileRender = false;
	private boolean doEntityRender = false;
	
	// Constructor
	protected EntityImagePanel(int xBuf, int yBuf) {
		super();
		
		// Transparent panel
		this.setOpaque(false);
	}

	// Sets tile image for the panel
	protected void setTImage(Image tileImage) {
		if(tileImage == null) {
			this.doTileRender = false;
		} else {
			this.doTileRender = true;
			this.tileImage = tileImage;
		}
	}
	
	// Sets the player image for the panel
	protected void setEImage(Image entityImage) {
		if(entityImage == null) {
			this.doEntityRender = false;
		} else {
			this.doEntityRender = true;
			this.entityImage = entityImage;
		}
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (this.doTileRender) {
	        int x = (this.getWidth() - tileImage.getWidth(null)) / 2;
	        int y = (this.getHeight() - tileImage.getHeight(null)) / 2;
        	g2d.drawImage(tileImage, x, y, this); 
        }
        if (this.doEntityRender) {
	        int x = (this.getWidth() - entityImage.getWidth(null)) / 2;
	        int y = (this.getHeight() - entityImage.getHeight(null)) / 2;
        	g.drawImage(entityImage, x, y, this);    
        }
    }
}
