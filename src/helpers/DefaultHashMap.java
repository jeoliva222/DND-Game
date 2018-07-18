package helpers;

import java.util.HashMap;

// Simple override to regular HashMap that has default values
// Code by 'maerics'
@SuppressWarnings("serial")
public class DefaultHashMap<K,V> extends HashMap<K,V> {

	// Default value for the HashMap if key doesn't exist
	protected V defaultValue;
  
	// Constructor
	public DefaultHashMap(V defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	// Override for 'get' returns default value if it doesn't exist
	@Override
	public V get(Object k) {
		return containsKey(k) ? super.get(k) : defaultValue;
	}
	
}
