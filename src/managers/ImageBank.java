package managers;

import java.awt.Image;
import java.util.HashMap;

// Holds a map of loaded images that are fetched with their corresponding image paths
// Used to optimize image loading in the game
public class ImageBank {
	
	public static HashMap<String, Image> images = new HashMap<String, Image>();

	// Fetches an image based on its imagePath
	public static Image lookup(String imgKey) {
		if(ImageBank.images.containsKey(imgKey)) {
			return ImageBank.images.get(imgKey);
		} else {
			return null;
		}
	}
	
	// Stores an image into the bank
	public static void storeImage(String imgKey, Image img) {
		ImageBank.images.put(imgKey, img);
	}
	
	// Clears the bank of images
	public static void clearBank() {
		ImageBank.images.clear();
		ImageBank.images = null;
		ImageBank.images = new HashMap<String, Image>();
	}
}
