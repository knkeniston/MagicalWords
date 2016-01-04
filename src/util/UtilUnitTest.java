package util;
import util.FileToDict;
import util.Location;
import util.Pixmap;
import util.Sound;
import util.Sprite;
import util.Text;
import util.ValueText;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Dimension;
import org.junit.After;
import org.junit.Before;

public class UtilUnitTest {
	
	public FileToDict file;
	public Location location;
	public Pixmap pixmap;
	public Sound sound;
	public Text text;
	public ValueText inttext;
	public ValueText chartext;
	public ValueText doubletext;
	
	@Before
	public void setUp() throws Exception {
		text = new Text("test");
		file = new FileToDict();
		inttext = new ValueText("test1", 1);
		chartext = new ValueText("test2", 'a');
		doubletext = new ValueText("test3", 1.0);
		pixmap = new Pixmap("magical-girl.gif");
	}
	
	/* #########################################################################
	 * Text.java tests
	 * #########################################################################
	 */
	@Test
	public void testGetText() {
		text = new Text("test");
		assertEquals("test", text.getText());
	}
	
	@Test
	public void testSetText() {
		text.setText("testing");
		assertEquals("testing", text.getText());
	}
	
	/* #########################################################################
	 * ValueText.java tests
	 * #########################################################################
	 */
	@Test
	public void testGetValue() {
		assertEquals(1, inttext.getIValue());
		assertEquals('a', chartext.getCValue());
		//assertEquals(1.0, doubletext.getDValue());
	}
	
	@Test
	public void testSetValue() {
		inttext.setValue(2);
		chartext.setValue('b');
		doubletext.setValue(2.0);
		assertEquals(2, inttext.getIValue());
		assertEquals('b', chartext.getCValue());
		//assertEquals(2.0, doubletext.getDValue());
		//set back for rest of tests
		setBack();
	}
	
	@Test
	public void testUpdateValue() {
		inttext.updateValue(1);
		chartext.updateValue('b');
		doubletext.updateValue(1.0);
		assertEquals(2, inttext.getIValue());
		assertEquals('b', chartext.getCValue());
		//assertEquals(2.0, doubletext.getDValue());
		setBack();
	}
	
	public void setBack() {
		//set back for rest of tests
		inttext.setValue(1);
		chartext.setValue('a');
		doubletext.setValue(1.0);
	}
	
	/* #########################################################################
	 * PixMap.java tests
	 * #########################################################################
	 */
	
	public void setImage() {
		pixmap.setImage("witchbroomcat.png");
	}
}