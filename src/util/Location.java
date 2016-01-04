package util;

import java.awt.geom.Point2D;

/**
 * This class represents a Location (in pixels) on the screen and 
 * adds some utility functions to the Point2D class.
 * Note, this might be overkill, it was just annoying that Point2D
 * did not implement translate :(
 */
public class Location extends Point2D.Double {
    // default serialization ID
    private static final long serialVersionUID = 1L;
    public static double[] location;
    

    /**
     * Create a location at the origin.
     */
    public Location () {
        super(0, 0);
        location = new double[2]; 
        location[0] = 0; location[1] = 0;
    }

    /**
     * Create a location at given (x, y) coordinates.
     */
    public Location (double x, double y) {
    	super(x, y);
    	location = new double[2]; 
    	location[0] = x; location[1] = y;
    }

    /**
     * Create a location that is identical to the given other location.
     */
    public Location (Point2D source) {
        super(source.getX(), source.getY());
    }
    
    public double[] getLocation() {
    	return location;
    }

    /**
     * Reset this location to origin.
     */
    public void reset () {
        setLocation(0, 0);
    }
    
}
