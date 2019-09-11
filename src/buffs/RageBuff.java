package buffs;

public class RageBuff extends Buff {

	// Serialization ID
	private static final long serialVersionUID = 3921806047962060239L;
	
	// Damage multiplier
	public static final double dmgBoost = 1.5;
	
	// Description
	private static final String DESC = "Great anger powers your attacks, multiplying damage by " + dmgBoost + "x!";
	
	public RageBuff(int duration) {
		super(RAGE, DESC, duration);
	}

	@Override
	public void doTurnEffect() {
		// No on-turn effect
	}

	@Override
	public void activate() {
		// No effect on activate
	}

	@Override
	public void deactivate() {
		// No effect on deactivate
	}
	
}
