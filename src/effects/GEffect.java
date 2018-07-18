package effects;

public class GEffect {
	
	// X and Y position
	protected int xPos, yPos;

	// Path to image file
	protected String filepath;
	
	// Counter indicating how many turns left the effect will exist
	protected int countDown;
	
	public GEffect(int xPos, int yPos, String filepath, int countDown) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.filepath = filepath;
		this.countDown = countDown;
	}
	
	public GEffect(int xPos, int yPos, String filepath) {
		this(xPos, yPos, filepath, 1);
	}
	
	public boolean persist() {
		this.countDown += -1;
		//System.out.println("Countdown went from "+Integer.toString(this.countDown + 1)+" to "+Integer.toString(this.countDown));
		return (this.countDown < 0);
	}
	
	//----------------------
	// Getters and setters
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public String getImagePath() {
		return this.filepath;
	}
	
	public int getCountDown() {
		return this.countDown;
	}
	
}
