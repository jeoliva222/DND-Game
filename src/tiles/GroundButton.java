package tiles;

import java.awt.Dimension;
import java.util.ArrayList;

import characters.GCharacter;
import helpers.GPath;
import managers.EntityManager;

public class GroundButton extends GButton {
	
	// Serialization ID
	private static final long serialVersionUID = 237906288946013176L;
	
	///*** TEMPORARY REFERENCES
	protected int rarity = 10;

	public GroundButton(TriggerType trigger, boolean multiTrigger) {
		super(trigger, multiTrigger);
		this.moveTypes = MovableType.GROUND;
		this.visibility = GButton.VISIBLE;
	}
	
	public GroundButton(TriggerType trigger, boolean multiTrigger, int visibility) {
		super(trigger, multiTrigger, visibility);
		this.moveTypes = MovableType.GROUND;
	}
	
	public GroundButton(TriggerType trigger,
			boolean multiTrigger, ArrayList<Dimension> coordList) {
		this(trigger, multiTrigger);
		this.coordList = coordList;
	}
	
	public GroundButton(TriggerType trigger,
			boolean multiTrigger, int visibility, ArrayList<Dimension> coordList) {
		this(trigger, multiTrigger, visibility);
		this.coordList = coordList;
	}
	
	public GroundButton(TriggerType trigger, ArrayList<GCharacter> npcList) {
		this(trigger, false);
		this.npcList = npcList;
	}
	
	public GroundButton(TriggerType trigger, int visibility, ArrayList<GCharacter> npcList) {
		this(trigger, false, visibility);
		this.npcList = npcList;
	}
	
	public GroundButton(TriggerType trigger, boolean multiTrigger, int visibility,
			ArrayList<GCharacter> npcList, ArrayList<ExtraTile> extraList) {
		this(trigger, multiTrigger, visibility);
		this.npcList = npcList;
		this.extraList = extraList;
	}
	
	public GroundButton(TriggerType trigger, boolean multiTrigger, String soundPath) {
		super(trigger, multiTrigger, soundPath);
		this.moveTypes = MovableType.GROUND;
	}
	
	@Override
	public String selectImage() {
		// Fetch region path to display image from correct area
		String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
		
		// Initialize string variables
		String basePath = GPath.createImagePath(GPath.TILE, regionPath);
		String entityPath = "groundbutton";
		String hitPath, visiblePath;
		
		// Button hit or not
		if(this.isHit) {
			hitPath = "_HIT";
		} else {
			hitPath = "_NOTHIT";
		}
		
		// Visibility of button
		switch(this.visibility) {
			case GButton.HIDDEN:
				return GPath.createImagePath(GPath.TILE, regionPath, "ground.png");
			case GButton.VEILED:
				visiblePath = "_VEILED";
				break;
			case GButton.VISIBLE:
				visiblePath = "_VISIBLE";
				break;
			default:
				System.out.println("Couldn't identify visibility for GroundButton");
				visiblePath = "_VISIBLE";
				break;
		}
		
		// Return the full image path
		this.imagePath = basePath + entityPath + hitPath + visiblePath + ".png";
		return this.imagePath;
	}

}
