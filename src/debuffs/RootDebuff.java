package debuffs;

public class RootDebuff extends Debuff {

	// Serialization ID
	private static final long serialVersionUID = -3003827801278730468L;
	
	// Description
	private static final String DESC = "You are rooted to your location, preventing all movement!";
	
	public RootDebuff(int duration) {
		super(ROOTED, DESC, duration);
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
