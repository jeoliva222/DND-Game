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
	
	@Override
	public int hashCode() {
		return this.filepath.hashCode() + (this.xPos * 23) + (this.yPos * 31);
	}
	
	@Override
	public boolean equals(Object obj) {
		// Return true if compared with self
		if(obj == this) {
			return true;
		}
		
		// Returns false if null or not a GEffect
		if(!(obj instanceof GEffect)) {
			return false;
		}
		
		// Object is a GEffect
		GEffect fx = (GEffect) obj;
		
		// Otherwise performs checks various parameters to check equality
		return (this.filepath.equals(fx.filepath) &&
				this.xPos == fx.xPos &&
				this.yPos == fx.yPos &&
				this.countDown == fx.countDown);
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
