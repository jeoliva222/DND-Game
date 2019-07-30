package debuffs;

/**
 * Debuff that prevents affected characters from acting
 * @author joliva
 */
public class StunDebuff extends Buff {

	// Serialization ID
	private static final long serialVersionUID = -2669886272745915099L;
	
	// Description
	private static final String DESC = "You are stunned, preventing all actions!";
	
	public StunDebuff(int duration) {
		super(STUNNED, DESC, duration);
		this.isDebuff = true;
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
