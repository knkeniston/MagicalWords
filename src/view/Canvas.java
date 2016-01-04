package view;

import game.Model;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.event.KeyAdapter;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Creates an area of the screen in which the game will be drawn that supports:
 *   <LI>animation via the Timer
 *   <LI>mouse input via the MouseListener
 *   <LI>keyboard input via the KeyListener
 */
public class Canvas extends JComponent {
	
    // default serialization ID
    private static final long serialVersionUID = 1L;
    // animate 25 times per second if possible
    public static final int FRAMES_PER_SECOND = 30;
    // better way to think about timed events (in milliseconds)
    public static final int ONE_SECOND = 1000;
    public static final int DEFAULT_DELAY = ONE_SECOND / FRAMES_PER_SECOND;
    // input state
    public static final char NO_KEY_PRESSED = 0;
    
    public static final int ENTER = KeyEvent.VK_ENTER;
    
    // drives the animation
    private Timer myTimer;
    // game to be animated
    private Model myGame;
    // input state
    private char keyTyped;    
    
    /**
     * Create a panel so that it knows its size
     */
    public Canvas (Dimension size) {
        // set size (a bit of a pain)
        setPreferredSize(size);
        setSize(size);
        // prepare to receive input
        setFocusable(true);
        requestFocus();
        setInputListeners();
      
    }
    
    public Timer getTimer(){
    	return myTimer;
    }

    /**
     * Paint the contents of the canvas.
     * 
     * Never called by you directly, instead called by Java runtime
     * when area of screen covered by this container needs to be
     * displayed (i.e., creation, uncovering, change in status)
     * 
     * @param pen used to paint shape on the screen
     */
    @Override
    public void paintComponent (Graphics pen) {
    	paintSplash((Graphics2D)pen, "../images/loading.jpg");
    	paintGame(pen);
    }
    
    /**
     * Paint components in game
     * @param pen
     */
    public void paintGame(Graphics pen) {
    	if(myGame != null){
    		if(!myGame.getLoadingStatus()){
    			myGame.paintGame((Graphics2D) pen);
    		}
    	}
    }
    
    /**
     * Paint loading screen
     * @param pen
     */
    public void paintSplash(Graphics2D pen, String image) {
		Image over = new ImageIcon(getClass().getResource(image)).getImage();
		pen.drawImage(over, 0, 0, this);
	
    }

    /**
     * Returns last key pressed by the user.
     */
    public char getLastKeyPressed () {
        return keyTyped;
    }
    
    /**
     * Return game model created
     * @return myGame
     */
    public Model getGame() {
    	return myGame;
    }

    /**
     * Start the animation.
     * @throws IOException 
     */
    public void start () throws IOException {
        final int stepTime = DEFAULT_DELAY;
        // create a timer to animate the canvas
        Timer timer1 = new Timer(stepTime, 
            new ActionListener() {
                public void actionPerformed (ActionEvent e) {
                    myGame.update((double) stepTime / ONE_SECOND);
                    repaint();
                    //System.out.println(stepTime);
                }
            });
        
        // start animation
        myGame = new Model(this);
        timer1.start();
        repaint();
    }
    /**
     * Stop the animation.
     */
    public void stop () {
        myTimer.stop();
    }
    
    /**
     * Create listeners that will update state based on user input.
     */
    private void setInputListeners () {
        // initialize input state
        keyTyped = NO_KEY_PRESSED;
        // MULTIPLE KEY SUPPORT
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                keyTyped = e.getKeyChar();
                myGame.compareTyped(keyTyped);                
            }
        });
    }
}