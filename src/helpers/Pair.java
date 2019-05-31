package helpers;

/**
 * Class intended to hold two variable members of any type.
 */
public class Pair<X, Y> {

	public X x;
	public Y y;
	
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x.toString() + ", " + y.toString() + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		
		if(!(obj instanceof Pair)) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		Pair<X, Y> pair = (Pair<X, Y>) obj;
		
		return ((this.x.equals(pair.x)) && 
				(this.y.equals(pair.y)));
	}
	
	@Override
	public int hashCode() {
		final int prime = 37;
		int hashcode = 1;
		hashcode = hashcode * prime * this.x.hashCode();
		hashcode = hashcode * prime * this.y.hashCode();
		return hashcode;
	}
	
}
