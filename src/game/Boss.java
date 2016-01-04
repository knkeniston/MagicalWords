package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;

/**
 *  Represents a spaceship that the player controls.
 */
public class Boss extends Sprite {
    
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(500, 333);
    public static final int DEFAULT_OFFSET = 50;
    public String currentWord;
    public double time;
    public double[] location;
    
    /**
     * Create a paddle at the given position, with the given size.
     */
    public Boss(Pixmap image, Location center, Dimension size, String word, double setTime) {
		super(image, center, size);
		location = center.getLocation();
		currentWord = word;
		time = setTime;
	}
    
    public double[] getLocation() {
    	return location;
    }
    
    public String getCurrentWord() {
    	return currentWord;
    }
    
    public double getTime() {
    	return time;
    }
    
    public void updateTime(double dt) {
    	time -= dt;
    }
}
