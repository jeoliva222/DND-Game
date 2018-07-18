package tiles;

import java.io.Serializable;

// Class that contains information for adding extra (more detailed) tiles to the grid of tiles
// in a level
public class ExtraTile implements Serializable {

	// Serialization ID
	private static final long serialVersionUID = -8626343746616835172L;

	// TileType we are adding
	public TileType tile;
	
	// Position on the level the tile is being added
	public int xPos, yPos;
	
	// Constructor
	public ExtraTile(int xPos, int yPos, TileType tt) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.tile = tt;
	}
	
}
