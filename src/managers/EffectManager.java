package managers;

import java.util.ArrayList;

import effects.GEffect;
import gui.GameScreen;

// Manager that holds list of currently active effects
public class EffectManager {

	// ArrayList containing instances of all existing corpses
	private ArrayList<GEffect> fxList = new ArrayList<GEffect>();
	
	// Constructor with pre-existing effects
	public EffectManager(ArrayList<GEffect> fxList) {
		this.fxList = fxList;
	}
	
	// Blank Constructor
	public EffectManager() {
	}
	
	// Add an effect to the manager
	public void addEffect(GEffect fx) {
		this.fxList.add(fx);
		try {
			// Sets the image for the effect
			GameScreen.getTile(fx.getXPos(), fx.getYPos()).setEffectImage(fx.getImagePath(), fx);
		} catch (ArrayIndexOutOfBoundsException e) {
			// Do nothing if out of bounds
		}
	}
	
	// Checks if a effect exists within the manager
	// Returns true if found and removed
	// Returns false if not found
	public boolean removeEffect(GEffect fx) {
		if(this.fxList.contains(fx)) {
			this.fxList.remove(fx);
			return true;
		} else {
			return false;
		}
	}
	
	// Removes all effects from list
	public void removeAll() {
		this.fxList.clear();
	}
	
	//------------------------
	// Getters and setters
	
	public ArrayList<GEffect> getEffects() {
		return this.fxList;
	}
}
