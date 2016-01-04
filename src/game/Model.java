package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;

import util.Location;
import util.Pixmap;
import util.Sound;
//import util.Sound;
import util.ValueText;
import util.Text;
import view.Canvas;
import util.FileToDict;

 /**
 * Represents a simple game of Magical Words.
 * It enforces the rules of the game each moment in the game as well as award points and
 * change levels.
 */
public class Model {
    // resources used in the game
	private static final Pixmap BACKGROUND = new Pixmap("spookybackground.jpg");
	private static final Pixmap WITCH_IMAGE = new Pixmap("witchbroomcat.gif");
	private static final Pixmap WITCH_DIE = new Pixmap("explosion.gif");
	private static final Pixmap MAGICALGIRL_IMAGE = new Pixmap("magical-girl.gif");
	private static final Pixmap MAGICALGIRL_HURT = new Pixmap("magical-girl-hurt.gif");
	private static final Pixmap MAGICALGIRL_ATTACK = new Pixmap("magical-girl-attack.gif");
	private static final Pixmap BOSS_IMAGE = new Pixmap("magical-boss-4.gif");
	//private static final Pixmap BOSS_DIE = new Pixmap("explosion.gif");
	
    //game input
    public static final int ENTER = KeyEvent.VK_ENTER;
    public static final int ESC = KeyEvent.VK_ESCAPE;
    
    // game play values
    private static final int STARTING_LIVES = 3;
    private static final int WITCH_COUNT = 4;
    
    // heads up display values
    private static final int LABEL_X_OFFSET = 52;
    private static final int LABEL_Y_OFFSET = 25;
    private static final Color LABEL_COLOR = Color.WHITE;
    private static final String SCORE_LABEL = "Score: ";
    private static final String HEALTH_LABEL = "HEALTH: ";
    private static final double LEVEL_TIME = 30.0;
    private static final int BOSS_LIVES = 10;
    
    //game sound
    private static final Sound BACKGROUND_MUSIC = new Sound("MagicalStrings.wav");
        
    private static Random rand = new Random();
    
    //game state
    private boolean gameWon = false;
    private boolean gameLost = false;
    public int level = 1;
    private boolean gameisLoading = true;
    private boolean gameStart = false;
    private boolean pressEsc = true;
    private double soundTime = 0;
    private boolean firstTime;
   
    //game objects
    private MagicalGirl madoka;
    private ArrayList<Witch> witches = new ArrayList<Witch>();
    private Boss myBoss;
    private Canvas myView;
    private Background myBackground;
    private Background myBackgroundLoop;
    private ValueText myScore;
    private ValueText finalScore;
    private ValueText myLevel;
    private ValueText myHealth;
    private ValueText valueTime;
    private Map<Integer, ArrayList<String>> dictionary;
    public ValueText wordTyped;
    private ArrayList<String> currentWitchWords = new ArrayList<String>();
    private ArrayList<String> currentTypeList = new ArrayList<String>();
    private String currentWordToType;
    private Text typedVChars;
    private String typedChars;
    private int[] levelTime = {8, 8, 8};
    private int[] levelPoints = {5, 10, 15};
    private double currentTime;
    private int bossLives;

    /**
     * Create a game of the given size with the given display for its shapes.
     */
    public Model (Canvas canvas) {
        myView = canvas;
        initStatus();
        initDict();
        initGirl(canvas.getSize());
        initWitch(canvas.getSize());
        initBackground(canvas.getSize());
    	//BACKGROUND_MUSIC.play();
    	gameisLoading = false;
    }

    /**
     * Return the Spaceship object created
     * @return myShip;
     */
    public MagicalGirl getGirl() {
    	return madoka;
    }
    
    public ArrayList<String> getWordList() {
    	return currentWitchWords;
    }
    
    /**
     *	Return if the game is still loading assets 
     *	@return gameisLoading
     */	
    public boolean getLoadingStatus(){
    	return gameisLoading;
    }
    
    public double getCurrentTime() {
    	return currentTime;
    }
    
    /**
     * Return if the game has started after loading assets
     * @return gameStart
     */
    public boolean hasGameStarted(){
    	return gameStart;
    }
    
    /**
     * Return the Model
     * @return this
     */
    
    public Model getModel(){
    	return this;
    }

	/**
     * Draw all elements of the game.
     */
    
    public void paintGame (Graphics2D pen){
    	if (gameStart == true){
    		paintInit(pen);
    	}
    	if (gameStart == false) {
    		paintEnter(pen);
    	}
    	if(gameStart == true && gameLost == false && gameWon == false && level < 4){
    		paintWitches(pen);
    	} else if (gameStart == true && gameLost == false && gameWon == false && level == 4) {
    		paintBoss(pen);
    	}
    	else if (gameLost == true && gameWon == false){
    		gameLost(pen);
    	}
    	else if (gameLost == false && gameWon == true){
    		gameWon(pen);
    	}
    }
    
    public void gameLost(Graphics2D pen){
    	paintGameOver(pen);
	    paintLevelStatus(pen);
	    paintLastStatus(pen);
    }
    
    public void gameWon(Graphics2D pen){
    	paintGameWon(pen);
    	paintLevelStatus(pen);
    	paintLastStatus(pen);
    }
    
    public void paintInit(Graphics2D pen){
		paintBackground(pen);
		paintStatus(pen);
		paintLevelStatus(pen);
		paintGirl(pen);
		paintTyped(pen);
    }
    
    public void paintBackground (Graphics2D pen){
    	myBackground.paint(pen);
    	myBackgroundLoop.paint(pen);
    }
    
    public void paintGirl (Graphics2D pen) {
    	madoka.paint(pen);
    }
    
    public void paintBoss (Graphics2D pen) {
    	myBoss.paint(pen);
    	double[] location = myBoss.getLocation();
		Text myWord = new Text(myBoss.getCurrentWord());
		myWord.paintSpeechBubble(pen, new Point((int)location[0], 
				(int)location[1] - 100), Color.BLACK, false);
    }
    
    public void paintGameOver (Graphics2D pen) {
    	Image over = new ImageIcon(getClass().getResource("../images/endgame.jpg")).getImage();
    	pen.drawImage(over, 0, 0, myView);
    }
    
    public void paintGameWon (Graphics2D pen) {
    	Image won = new ImageIcon(getClass().getResource("../images/wingame.jpg")).getImage();
    	pen.drawImage(won, 0, 0, myView);    
    }
    
    /**
     * Create a Spaceship object controlled by user
     * @param bounds
     */
    private void initGirl (Dimension bounds) {     
    	madoka = new MagicalGirl(MAGICALGIRL_IMAGE, new Location(170, bounds.height - 200),
        		MagicalGirl.DEFAULT_SIZE);
    	madoka.setAttackTime(0);
    	madoka.setHurtTime(0);
    }  
    
    /**
     * Create Locust enemy ships that are added to a collection 
     * @param bounds
     */
    private void initWitch (Dimension bounds) {
    	ArrayList<String> myList = dictionary.get(level);
    	for(int i = 0; i < WITCH_COUNT / 2; i++) {	
    		int num = rand.nextInt(myList.size()) + 1;
    		String myWord = myList.get(num - 1);
    		myList.remove(myWord);
    		currentWitchWords.add(myWord);
    		witches.add(new Witch (WITCH_IMAGE, new Location( (i*300) + 500, 300), 
    				Witch.DEFAULT_SIZE, myWord, levelTime[level - 1]));
    	}
    	
    	for(int i = 0; i < WITCH_COUNT / 2; i++) {	
    		int num = rand.nextInt(myList.size()) + 1;
    		String myWord = myList.get(num);
    		myList.remove(myWord);
    		currentWitchWords.add(myWord);
    		witches.add(new Witch (WITCH_IMAGE, new Location((i*300) + 700, 500), 
    				Witch.DEFAULT_SIZE, myWord, levelTime[level- 1]));
    	}
    }
    
    public void paintWitches (Graphics2D pen) {
		for (int i = 0; i < witches.size(); i++){
			Color color = Color.BLACK;
			Witch witch = witches.get(i);
			if (witch.getTime() <= 2.0 && !witch.isDying()) {
				color = Color.RED;
			} else if (witch.getTime() <= 4.0 && !witch.isDying()) {
				color = Color.ORANGE;
			}
			witch.paint(pen);
			double[] witchLocation = witch.getLocation();
			Text myWord = new Text(witch.getWord());
			if (!witch.isDying()) {
				myWord.paintSpeechBubble(pen, new Point((int)witchLocation[0], 
						(int)witchLocation[1] - 100), color, false);
			}
		}
    }
    
    /**
     * Create background for game
     * @param bounds
     */
    private void initBackground (Dimension bounds) {
    	myBackground = new Background(BACKGROUND, new Location((bounds.width/2), 
    			(bounds.height/2-768)), Background.DEFAULT_SIZE);
    	myBackgroundLoop = new Background(BACKGROUND, new Location((bounds.width/2), 
    			bounds.height/2), Background.DEFAULT_SIZE);
    }
      
    /**
     * Create initial "heads up display", i.e., status labels that will appear in the game.
     */
    private void initStatus () {
    	myLevel = new ValueText("Level: ", 1);
        myScore = new ValueText(SCORE_LABEL, 0);
        myHealth = new ValueText(HEALTH_LABEL, STARTING_LIVES);
        wordTyped = new ValueText("WORD TYPED: ", 'a');
        valueTime = new ValueText("LEVEL TIME: ", LEVEL_TIME);
        currentWordToType = "";
        currentTime = LEVEL_TIME;
        firstTime = true;
        soundTime = 0;
        typedChars = "";
        typedVChars = new Text("");
    }
    
    private void initDict () {
    	FileToDict f = new FileToDict();
    	URL url = getClass().getResource("/dictionary.txt");
    	dictionary = f.readLines(url.getPath());
    }
    
    /**
     * Create a final "heads up display" that displays user final score
     */
    private void finalStatus(){
    	finalScore = new ValueText("FINAL SCORE: ", myScore.getIValue());
    }
    
    /**
     * Start the game
     * @param key
     */
    public void startGame(int key){
    	if (key == ENTER) {
    		gameStart = true;
    	}
    }
    
    /**
	 *  game for this moment, given the time since the last moment.
	 */
	public void update (double elapsedTime) {
		if (!gameisLoading) {
			char key = myView.getLastKeyPressed();
			startGame(key);
			resetGame(key);
			if (gameStart) {
				Dimension bounds = myView.getSize();
				checkRules(bounds);
				updateSprites(elapsedTime, bounds);
				currentTime = currentTime - elapsedTime;
				valueTime.updateValue(-elapsedTime);
				if (gameWon == true || gameLost == true) {
					resetGame(key);
				}
				soundTime += elapsedTime;
				if (soundTime > 70) {
					soundTime = 0.0;
					BACKGROUND_MUSIC.play();
				}
			}
		}
	}
	
	public void resetGame(int key) {
		if (key == ESC && pressEsc) {
			restart();
			pressEsc = false;
		} else if (key != ESC) {
			pressEsc = true;
		}
	}
	
	public void restart() {
		gameStart = false;
		gameWon = false;
        gameLost = false;
		level = 1;
		currentWitchWords.clear();
		currentTypeList.clear();
		witches.clear();
    	initStatus();
        initDict();
        initGirl(myView.getSize());
        initWitch(myView.getSize());
        initBackground(myView.getSize());
	}
	
	public void compareTyped(char c) {
		findWord(c);
		String current = typedChars + String.valueOf(c);
		if ((currentTypeList.size() <= WITCH_COUNT && currentTypeList.size() > 0 && 
				checkCurrentTypeList(current)) || currentWordToType.startsWith(current)) {
			typedChars = current;
		}
		if (!currentWordToType.equals("") && currentWordToType.equals(typedChars) && level < 4) {
			killWitch();
		}
		if (!currentWordToType.equals("") && currentWordToType.equals(typedChars) && level == 4) {
			attackBoss();
		}
		typedVChars.setText(typedChars);
		System.out.println("typedChars: " + typedChars);
		System.out.println("currentWordToType: " + currentWordToType);
		System.out.println("currentTypeList: " + currentTypeList.toString());
	}
	
	private boolean checkCurrentTypeList(String current) {
		for (String w: currentTypeList) {
			if (w.startsWith(current)) {
				return true;
			}
		}
		return false;
	}
	
	private void findWord(char c) {
		if (currentTypeList.isEmpty() && currentWordToType.equals("")) {
			for (String w: currentWitchWords) {
				if (w.startsWith(String.valueOf(c))) {
					currentTypeList.add(w);
				}
			}
		} else if (currentWordToType.equals("")) {
			for (String w: currentTypeList) {
				if (!w.startsWith(typedChars)) {
					currentTypeList.remove(w);
					break;
				}
			}
		}
		if (currentTypeList.size() == 1) {
			currentWordToType = currentTypeList.get(0);
			currentTypeList.clear();
		}
	}
	
	private void attackBoss() {
		if (bossLives > 0){
			int num = rand.nextInt(dictionary.get(level).size());
			String newWord = dictionary.get(level).get(num);
			dictionary.get(level).remove(newWord);
			myBoss.currentWord = newWord;
			currentWordToType = myBoss.currentWord;
			typedChars = "";
			bossLives -= 1;
		} else {
			initGameWon();
		}
	}
	
	private void killWitch() {
		double[] oldLocation = new double[2];
		String oldWord = "";
		for (Witch w: witches) {
			if (w.getWord().equals(currentWordToType)) {
				oldLocation = w.getLocation();
				oldWord = w.getWord();
				witches.remove(w);
				break;
			}
		}
		witches.add(new Witch(WITCH_DIE, new Location(oldLocation[0], oldLocation[1]), 
				Witch.DEFAULT_SIZE, oldWord, .7));
		madoka.setAttackTime(.5);
		madoka.setView(MAGICALGIRL_ATTACK);
		myScore.updateValue(levelPoints[level - 1]);
		currentWitchWords.remove(currentWordToType);
		currentWordToType = ""; typedChars = "";
	}
	
	private void witchRespawn(double[] oldLocation) {
		int num = rand.nextInt(dictionary.get(level).size());
		String newWord = dictionary.get(level).get(num);
		currentWitchWords.add(newWord);
		dictionary.get(level).remove(newWord);
		witches.add(new Witch (WITCH_IMAGE, new Location(oldLocation[0], oldLocation[1]), 
				Witch.DEFAULT_SIZE, newWord, levelTime[level- 1]));
	}

	/**
     * Update sprites based on time since the last update.
     * Based on level status
     */
    private void updateSprites (double elapsedTime, Dimension bounds) {
    	myBackground.update(elapsedTime, bounds);
    	myBackgroundLoop.update(elapsedTime, bounds);
    	for (Witch w: witches) {
			w.updateTime(elapsedTime);
		}
    	if (madoka.getAttackTime() > 0) {
    		madoka.updateAttackTime(elapsedTime);
    	} else {
    		madoka.setAttackTime(0);
    	}
    	if (madoka.getHurtTime() > 0) {
    		madoka.updateHurtTime(elapsedTime);
    	} else {
    		madoka.setHurtTime(0);
    	}
    	if (madoka.getAttackTime() == 0 && madoka.getHurtTime() == 0) {
    		madoka.setView(MAGICALGIRL_IMAGE);
    	}
    }

	/**
     * Check the game level and if the game is won or lost
     * @param bounds
     */
    private void checkRules (Dimension bounds) {
    	if (level == 1){
    		initLevel1();
    		initializeMusic();
    	}
    	if (myHealth.getIValue() <= 0) {
    		initGameLost();
    		BACKGROUND_MUSIC.stop();
    	}
    	if (level == 1 && currentTime <= 0) {
    		initLevel2();
    	}   
    	if (level == 2 && currentTime <= 0) {
    		initLevel3();
    	}
    	if (level == 3 && currentTime <= 0) {
    		initBoss();
    	}
    	for (int i = 0; i < witches.size(); i++) {
    		Witch w = witches.get(i);
    		if (w.getTime() < 0 && w.isDying()) {
    			double[] oldLocation = w.getLocation();
    			witches.remove(w);
    			witchRespawn(oldLocation);
    		} else if (w.getTime() < 0) {
    			witchAttack(w);
    			break;
    		}
		}
    }
    
    private void initializeMusic() {
    	if (firstTime) {
    		BACKGROUND_MUSIC.play();
    		firstTime = false;
    	}
    }
    
    private void witchAttack(Witch w) {
    	double[] oldLocation = w.getLocation();
    	myHealth.updateValue(-1);
    	witches.remove(w);
    	madoka.setAttackTime(1);
		madoka.setView(MAGICALGIRL_HURT);
    	if (!currentTypeList.isEmpty()) {
    		for (String s: currentTypeList) {
    			if (s.equals(w.getWord())) {
    				currentTypeList.remove(s);
    			}
    		}
    		if (currentTypeList.size() == 1) {
    			currentWordToType = currentTypeList.get(0);
    			currentTypeList.clear();
    		}
    	} else if (currentWordToType.equals(w.getWord())) {
    		currentWitchWords.remove(currentWordToType);
    		currentWordToType = ""; typedChars = "";
    	}
    	witchRespawn(oldLocation);
    }
  
    /**
     * Calls the final status and changes game status to "lost"
     */
    private void initGameLost(){
    	finalStatus();
    	gameLost = true;
    	BACKGROUND_MUSIC.stop();
    }
    
    /**
     * Calls final status and changes game status to "won"
     */
    private void initGameWon(){
		finalStatus();
		gameWon = true;
		BACKGROUND_MUSIC.stop();
    }
    
    /**
     * Initiates level 1
     * @param bounds
     */
    private void initLevel1(){
	     level = 1;
	}
    
    /**
     * Initiates level 2
     * @param bounds
     */
	private void initLevel2(){
    	myLevel.setValue(2);
    	level = 2;
    	currentTime = LEVEL_TIME;
    	valueTime = new ValueText("TIME LEFT: ", LEVEL_TIME);
    }
	
	private void initLevel3(){
    	myLevel.setValue(3);
    	level = 3;
    	currentTime = LEVEL_TIME;
    	valueTime = new ValueText("TIME LEFT: ", LEVEL_TIME);
    }
	
	private void initBoss() {
		myLevel.setValue(4);
		level = 4;
		currentTime = LEVEL_TIME;
		witches.clear();
		currentWordToType = ""; typedChars = "";
		currentTypeList.clear();
		bossLives = BOSS_LIVES;
    	valueTime = new ValueText("TIME LEFT: ", LEVEL_TIME);
    	int num = rand.nextInt(dictionary.get(level).size());
		String newWord = dictionary.get(level).get(num);
		dictionary.get(level).remove(newWord);
    	myBoss = new Boss(BOSS_IMAGE, new Location(600, 300), Boss.DEFAULT_SIZE, newWord, LEVEL_TIME);
		currentWordToType = newWord;
	}

    /**
     * Display labels on screen
     */
    private void paintStatus (Graphics2D pen) {
        myScore.paint(pen, new Point(LABEL_X_OFFSET + 25, LABEL_Y_OFFSET), LABEL_COLOR);
        myHealth.paint(pen, new Point(350, LABEL_Y_OFFSET), LABEL_COLOR);   
        valueTime.paint(pen, new Point(700, LABEL_Y_OFFSET), LABEL_COLOR); 
    }
    private void paintLevelStatus (Graphics2D pen) {
    	myLevel.paint(pen, new Point(1000, LABEL_Y_OFFSET), LABEL_COLOR);
    }
    private void paintLastStatus (Graphics2D pen) {
    	finalScore.paint(pen, new Point(LABEL_X_OFFSET + 100, LABEL_Y_OFFSET), LABEL_COLOR);
    }
    public void paintEnter(Graphics2D pen) {
		Image enter = new ImageIcon(getClass().getResource("../images/opening.jpg")).getImage();
		pen.drawImage(enter, 0, 0, myView);
    }   
    private void paintTyped (Graphics2D pen) {
    	if (typedChars.length() > 0) {
    		typedVChars.paintSpeechBubble(pen, new Point(170, 200), Color.RED, true);
    	}
    }    
}
