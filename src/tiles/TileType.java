package tiles;

import java.io.Serializable;

// Abstract class that helps determine what rules a GameTile should abide by
public abstract class TileType implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = 2863973892921145206L;

	protected String imagePath;
	
	// Determines which entities can move on this type of tile
	protected MovableType moveType;
	
	public String getImagePath() {
		return this.imagePath;
	}
	
	public MovableType getMovableType() {
		return this.moveType;
	}
	
	public abstract String selectImage();
	
	public abstract void onStep();

}
