package tiles;

import java.awt.Dimension;
import java.util.ArrayList;

import characters.GCharacter;
import helpers.GPath;
import managers.EntityManager;

public class WaterButton extends GButton {

	// Serialization ID
	private static final long serialVersionUID = -289555333335405429L;
	
	int rarity = 20;
	String type1 = GPath.createImagePath(GPath.TILE, GPath.DUNGEON, "dun_water1.png");
	String type2 = GPath.createImagePath(GPath.TILE, GPath.DUNGEON, "dun_water2.png");

	public WaterButton(TriggerType trigger, boolean multiTrigger) {
		super(trigger, multiTrigger);
		this.moveTypes = MovableType.WATER;
		this.visibility = GButton.VISIBLE;
	}
	
	public WaterButton(TriggerType trigger, boolean multiTrigger, int visibility) {
		super(trigger, multiTrigger);
		this.moveTypes = MovableType.WATER;
		this.visibility = visibility;
	}
	
	public WaterButton(TriggerType trigger,
			boolean multiTrigger, ArrayList<Dimension> coordList) {
		this(trigger, multiTrigger);
		this.coordList = coordList;
	}
	
	public WaterButton(TriggerType trigger,
			boolean multiTrigger, int visibility, ArrayList<Dimension> coordList) {
		this(trigger, multiTrigger, visibility);
		this.coordList = coordList;
	}
	
	public WaterButton(TriggerType trigger, ArrayList<GCharacter> npcList) {
		this(trigger, false);
		this.npcList = npcList;
	}
	
	public WaterButton(TriggerType trigger, int visibility, ArrayList<GCharacter> npcList) {
		this(trigger, false, visibility);
		this.npcList = npcList;
	}
	
	public WaterButton(TriggerType trigger, boolean multiTrigger, int visibility,
			ArrayList<GCharacter> npcList, ArrayList<ExtraTile> extraList) {
		this(trigger, multiTrigger, visibility);
		this.npcList = npcList;
		this.extraList = extraList;
	}
	
	public WaterButton(TriggerType trigger, boolean multiTrigger, String soundPath) {
		super(trigger, multiTrigger, soundPath);
		this.moveTypes = MovableType.WATER;
	}
	
	@Override
	public String selectImage() {
		// Fetch region path to display image from correct area
		String regionPath = EntityManager.getInstance().getActiveArea().getTheme();
		
		// Initialize String variables
		String basePath = GPath.createImagePath(GPath.TILE, regionPath);
		String entityPath = "waterbutton";
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
				return GPath.createImagePath(GPath.TILE, regionPath, "water.png");
			case GButton.VEILED:
				visiblePath = "_VEILED";
				break;
			case GButton.VISIBLE:
				visiblePath = "_VISIBLE";
				break;
			default:
				System.out.println("Couldn't identify visibility for WaterButton");
				visiblePath = "_VISIBLE";
				break;
		}
		
		// Return the full path
		this.imagePath = basePath + entityPath + hitPath + visiblePath + ".png";
		return this.imagePath;
	}
	
}
