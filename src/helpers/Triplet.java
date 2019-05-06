package helpers;

/**
 * Class intended to hold three variable members of any type.
 */
public class Triplet<X, Y, Z> {

	public X x;
	public Y y;
	public Z z;
	
	public Triplet(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "(" + x.toString() + ", " + y.toString() + ", " + z.toString() + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		
		if(!(obj instanceof Triplet)) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		Triplet<X, Y, Z> triplet = (Triplet<X, Y, Z>) obj;
		
		return ((this.x.equals(triplet.x)) && 
				(this.y.equals(triplet.y)) &&
				(this.z.equals(triplet.z)));
	}
	
	@Override
	public int hashCode() {
		final int prime = 41;
		int hashcode = 1;
		hashcode = hashcode * prime * this.x.hashCode();
		hashcode = hashcode * prime * this.y.hashCode();
		hashcode = hashcode * prime * this.z.hashCode();
		return hashcode;
	}
}
