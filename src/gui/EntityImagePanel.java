package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * Panel class that displays a foreground and background image
 * @author jeoliva
 */
public class EntityImagePanel extends JPanel {
	
	// Prevents warnings
	private static final long serialVersionUID = 1L;
	
	// Images to draw
	private Image backgroundImage;
	private Image foregroundImage;
	
	// Flags indicating whether to paint background/foreground images
	private boolean doBackgroundRender = false;
	private boolean doForegroundRender = false;
	
	// Constructor
	protected EntityImagePanel() {
		super();
		
		// Transparent panel
		setOpaque(false);
	}

	// Sets background image for the panel
	protected void setBackgroundImage(Image tileImage) {
		if (tileImage == null) {
			this.doBackgroundRender = false;
		} else {
			this.doBackgroundRender = true;
			this.backgroundImage = tileImage;
		}
	}
	
	// Sets the foreground image for the panel
	protected void setForegroundImage(Image entityImage) {
		if (entityImage == null) {
			this.doForegroundRender = false;
		} else {
			this.doForegroundRender = true;
			this.foregroundImage = entityImage;
		}
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (doBackgroundRender) {
	        int x = (getWidth() - backgroundImage.getWidth(null)) / 2;
	        int y = (getHeight() - backgroundImage.getHeight(null)) / 2;
        	g2d.drawImage(backgroundImage, x, y, this); 
        }
        if (doForegroundRender) {
	        int x = (getWidth() - foregroundImage.getWidth(null)) / 2;
	        int y = (getHeight() - foregroundImage.getHeight(null)) / 2;
        	g.drawImage(foregroundImage, x, y, this);    
        }
    }
}
