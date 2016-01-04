package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;
import java.util.*;

/**
 *  Represents a spaceship that the player controls.
 */
public class MagicalGirl extends Sprite {
    
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(265, 297);
    public static final int DEFAULT_OFFSET = 50;
	public ArrayList<Character> keysPressed = new ArrayList<Character>();
	private double attackTime;
	private double hurtTime;
    
    /**
     * Create a paddle at the given position, with the given size.
     */
    public MagicalGirl (Pixmap image, Location center, Dimension size)
    {
        super(image, center, size);
    }
    
    public double getAttackTime() {
    	return attackTime;
    }
    
    public double getHurtTime() {
    	return hurtTime;
    }
    
    public void setAttackTime(double time) {
    	attackTime = time;
    }
    
    public void setHurtTime(double time) {
    	hurtTime = time;
    }
    
    public void updateAttackTime(double dt) {
    	attackTime -= dt;
    }
    
    public void updateHurtTime(double dt) {
    	hurtTime -= dt;
    }
    /**
     * Return missiles created by ship;
     * @return
     */
    public ArrayList<Character> getKeys() {
    	return keysPressed;
    }
}
