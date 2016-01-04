package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;

/**
 *  Represents a spaceship that the player controls.
 */
public class Witch extends Sprite {
    
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(175, 175);
    public static final int DEFAULT_OFFSET = 50;
    public String myWord;
    public double time;
    public double[] location;
    private Pixmap myImage;
    
    /**
     * Create a paddle at the given position, with the given size.
     */
    public Witch(Pixmap image, Location center, Dimension size, String word, double setTime) {
		super(image, center, size);
		myImage = image;
		location = center.getLocation();
		myWord = word;
		time = setTime;
	}
    
    public double[] getLocation() {
    	return location;
    }
    
    public String getImage() {
    	return myImage.getFileName();
    }
    
    public boolean isDying() {
    	if(myImage.getFileName().contains("explosion")) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public String getWord() {
    	return myWord;
    }
    
    public double getTime() {
    	return time;
    }
    
    public void updateTime(double dt) {
    	time -= dt;
    }
    
    /**
     * Describes how to "animate" the shape by changing its state.
     * Also updates Spaceship state
     * Currently, moves the shape based on the keys pressed.
     */
    public void update (double elapsedTime, Dimension bounds) {     
    }
}
