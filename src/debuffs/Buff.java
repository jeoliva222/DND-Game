package debuffs;

import java.io.Serializable;

import characters.GCharacter;
import characters.allies.Player;

public abstract class Buff implements Serializable {

	// Serialization ID
	private static final long serialVersionUID = 3390406349056475262L;
	
	// Reference to all buff names
	public static final String RAGE = "Rage";
	
	// Reference to all debuff names
	public static final String BLINDNESS = "Blindness";
	public static final String BURNING = "Burning";
	public static final String ROOTED = "Rooted";
	public static final String STUNNED = "Stunned";
	
	// Reference to affected player
	protected Player player;
	
	// Reference to affected NPC
	protected GCharacter npc;
	
	// Name of the debuff effect
	protected String name;
	
	// Description of the debuff effect
	protected String description;
	
	// Number of turns the effect persists
	protected int duration;
	
	// Determines whether 'buff' is a positive or negative condition (Assumed positive by default).
	protected boolean isDebuff = false;
	
	// Constructor
	protected Buff(String name, String description, int duration) {
		this.name = name;
		this.description = description;
		this.duration = duration;
	}
	
	/**
	 * Decreases the duration of a debuff by 1.
	 * @return True if duration is over / False if still active
	 */
	public boolean persist() {
		// Decrease remaining duration by 1 turn
		this.duration += -1;
		
		// Return whether duration is over
		return (this.duration <= 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		// Return true if compared with self
		if(obj == this) {
			return true;
		}
		
		// Returns false if null or not a Debuff
		if(!(obj instanceof Buff)) {
			return false;
		}
		
		// Object is a Debuff
		Buff db = (Buff) obj;
		
		// Otherwise performs checks various parameters to check equality
		return (this.name.equals(db.name));
	}
	
	/**
	 * Does a per-turn effect for the debuff.
	 */
	public abstract void doTurnEffect();
	
	/**
	 * Does an effect when the character receives the debuff.
	 */
	public abstract void activate();
	
	/**
	 * Does an effect when the debuff is removed.
	 */
	public abstract void deactivate();
	
	// ------------------------------------
	
	public String getName() {
		return this.name;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public boolean isDebuff() {
		return this.isDebuff;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setCharacter(GCharacter npc) {
		this.npc = npc;
	}
	
}
