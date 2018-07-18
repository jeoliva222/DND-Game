package characters;

import java.io.Serializable;

public class Corpse implements Serializable {
	
	// Serialization ID
	private static final long serialVersionUID = 3616722059678081884L;
	
	private GCharacter npc;
	private String imagePath;
	
	private int xPos, yPos;
	
	public Corpse(GCharacter npc) {
		this.npc = npc;
		this.imagePath = npc.getCorpseImage();
		this.xPos = npc.getXPos();
		this.yPos = npc.getYPos();
	}
	
	// Returns the NPC associated with the corpse
	public GCharacter getNPC() {
		return npc;
	}
	
	// Returns the image path associated with the corpse
	public String getImage() {
		return this.imagePath;
	}
	
	// Returns the corpse's x position
	public int getXPos() {
		return this.xPos;
	}
	
	// Returns the corpse's y position
	public int getYPos() {
		return this.yPos;
	}
}
