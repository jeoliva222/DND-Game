package tiles;

import java.awt.Dimension;
import java.util.ArrayList;

import characters.GCharacter;
import gui.GameScreen;
import gui.GameTile;
import gui.LogScreen;
import helpers.GPath;
import helpers.SoundPlayer;
import managers.EntityManager;

public abstract class GButton extends TileType {

	// Serialization ID
	private static final long serialVersionUID = -7706338442266011938L;

	// What operation the button performs
	protected TriggerType trigger;
	
	// Enumerators to determine how a button is shown
	public static final int HIDDEN = 1;
	public static final int VEILED = 2;
	public static final int VISIBLE = 3;
	
	// Determines how visible the button is to the player
	// HIDDEN = No indication
	// VEILED = Indicated, but hard to spot
	// VISIBLE = Fully indicated and visible
	protected int visibility;
	
	// Whether or not the button has already been triggered once
	protected boolean wasTriggered = false;
	
	// Determines whether switch is is on/off state
	protected boolean isHit = false;
	
	// Whether the button allows for multiple triggers
	protected boolean multiTrigger = true;
	
	// List of enemies to spawn
	protected ArrayList<GCharacter> npcList = new ArrayList<GCharacter>();
	
	// List of GameTile coordinates to modify
	protected ArrayList<Dimension> coordList = new ArrayList<Dimension>();
	
	// List of GameTiles coordinates to modify (with specific changes)
	protected ArrayList<ExtraTile> extraList = new ArrayList<ExtraTile>();
	
	// Sound to play when button is hit
	protected String soundPath;

	public GButton(TriggerType trigger, boolean multiTrigger, int visibility) {
		this.trigger = trigger;
		this.multiTrigger = multiTrigger;
		this.visibility = visibility;
	}
	
	public GButton(TriggerType trigger, boolean multiTrigger) {
		this.trigger = trigger;
		this.multiTrigger = multiTrigger;
	}
	
	public GButton(TriggerType trigger,
			boolean multiTrigger, ArrayList<Dimension> coordList) {
		this(trigger, multiTrigger);
		this.coordList = coordList;
	}
	
	public GButton(TriggerType trigger, ArrayList<GCharacter> npcList) {
		this(trigger, false);
		this.npcList = npcList;
	}
	
	public GButton(TriggerType trigger, boolean multiTrigger, int visibility, ArrayList<GCharacter> npcList, ArrayList<ExtraTile> extraList) {
		this(trigger, multiTrigger, visibility);
		this.npcList = npcList;
		this.extraList = extraList;
	}
	
	// Constructor used for sound playing events only
	public GButton(TriggerType trigger, boolean multiTrigger, String soundPath) {
		this(trigger, multiTrigger);
		this.visibility = GButton.HIDDEN;
		this.soundPath = soundPath;
	}
	
	// Do extra (special) things for the particular button press
	// Should be overridden if needed
	public void doExtra() {
		// Nothing by default
	}
	
	@Override
	// Gets image for the tile
	abstract public String selectImage();

	@Override
	public void onStep() {
		// If already triggered and not multi-trigger, do nothing more
		if(this.wasTriggered && (!this.multiTrigger)) {
			return;
		}
		
		// Toggle if button was hit
		this.isHit = !(this.isHit);
		
		// Reselect image
		this.imagePath = this.selectImage();
		
		// Refresh tile the player was on
		int plrX = EntityManager.getInstance().getPlayer().getXPos();
		int plrY = EntityManager.getInstance().getPlayer().getYPos();
		GameScreen.getTile(plrX, plrY).setBG(this.imagePath);
		GameScreen.getTile(plrX, plrY).repaint();
		
		// Perform button function
		switch(this.trigger) {
			case WALL_GROUND:
				// For every set of coordinates
				for(Dimension coord: this.coordList) {
					// Get the TileType of the coord
					GameTile tile = GameScreen.getTile(coord.width, coord.height);
					TileType tt = tile.getTileType();
					if(tt instanceof Wall || tt instanceof AltWall) {
						tile.setTileType(new Ground());
					} else if (tt instanceof Ground || tt instanceof AltGround) {
						tile.setTileType(new Wall());
					}
				}
				break;
			case WALL_WATER:
				// For every set of coordinates
				for(Dimension coord: this.coordList) {
					// Get the TileType of the coord
					GameTile tile = GameScreen.getTile(coord.width, coord.height);
					TileType tt = tile.getTileType();
					if(tt instanceof Wall || tt instanceof AltWall) {
						tile.setTileType(new Water());
					} else if (tt instanceof Water) {
						tile.setTileType(new Wall());
					}
				}
				break;
			case GROUND_WATER:
				// For every set of coordinates
				for(Dimension coord: this.coordList) {
					// Get the TileType of the coord
					GameTile tile = GameScreen.getTile(coord.width, coord.height);
					TileType tt = tile.getTileType();
					if(tt instanceof Ground || tt instanceof AltGround) {
						tile.setTileType(new Water());
					} else if (tt instanceof Water) {
						tile.setTileType(new Ground());
					}
				}
				break;
			case SPAWN_ENEMY:
				// For every NPC in the list, spawn it
				for(GCharacter npc: this.npcList) {
					EntityManager.getInstance().getNPCManager().addCharacter(npc);
				}
				break;
			case ENEMY_AND_TILE:
				// For every NPC in the list, spawn it
				for(GCharacter npc: this.npcList) {
					EntityManager.getInstance().getNPCManager().addCharacter(npc);
				}
				
				for(ExtraTile et: this.extraList) {
					// Get the tile and old tile type
					GameTile tile = GameScreen.getTile(et.xPos, et.yPos);
					TileType oldType = tile.getTileType();
					
					// Exchange the two types
					tile.setTileType(et.tile);
					et.tile = oldType;
				}
				break;
			case SOUND:
				// Play the sound
				SoundPlayer.playWAV(this.soundPath);
				break;
			default:
				// No behavior
				System.out.println("Behavior not created yet.");
				break;
		}
		
		// Do extra behavior
		this.doExtra();
		
		// Log a relevant message and play a sound
		switch(this.visibility) {
			case GButton.HIDDEN:
				// Do nothing
				break;
			case GButton.VEILED:
				SoundPlayer.playWAV(GPath.createSoundPath("SmallButton_PRESS.wav"));
				LogScreen.log("A hidden button was pressed!");
				break;
			case GButton.VISIBLE:
				SoundPlayer.playWAV(GPath.createSoundPath("LargeButton_PRESS.wav"));
				LogScreen.log("A button was pressed...");
				break;
			default:
				// Do nothing
				break;
		}
		
		// Button is now triggered
		this.wasTriggered = true;
		
	}
	
}
