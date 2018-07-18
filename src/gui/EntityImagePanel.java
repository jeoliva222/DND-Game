package gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

// Displays the player's image in the StatusScreen
public class EntityImagePanel extends JPanel {
	
	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Images to draw
	private Image tileImage;
	private Image entityImage;
	
	private boolean doTileRender = true;
	private boolean doEntityRender = true;
	
	// X and Y buffers to set image in center
	private int xBuffer = 10;
	private int yBuffer = 10;
	
	// Constructor
	protected EntityImagePanel(int xBuf, int yBuf) {
		super();
		
		// Adjust buffer size
		this.xBuffer = (int) (xBuf * GameInitializer.scaleFactor);
		this.yBuffer = (int) (yBuf * GameInitializer.scaleFactor);
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
        if(this.doTileRender)
        	g.drawImage(tileImage, this.xBuffer, this.yBuffer, this); 
        if(this.doEntityRender)
        	g.drawImage(entityImage, this.xBuffer, this.yBuffer, this);           
    }
}
