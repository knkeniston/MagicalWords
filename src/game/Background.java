package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;

/**
 *  Represents the background of the game.
 */

public class Background extends Sprite
{
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(1075, 600);
    
    /*
     * Creates a background based on given image, location, and size
     */
    public Background (Pixmap Image, Location center, Dimension size) {
    	super (Image, center, size);
    }
}