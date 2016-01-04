package util;


/**
 * This class represents text that is a labeled numeric value.
 */
public class ValueText extends Text {
    private String myLabel;
    private int myValue;
    private int myInitialValue;
    private char myCValue;
    private double myDValue;

    /**
     * Create with its label and an initial value.
     */
    public ValueText(String label, int initialValue) {
        super(label + " " + initialValue);
        myValue = myInitialValue = initialValue;
        myLabel = label;
    }
    
    public ValueText(String label, char initialValue) {
        super(label + " " + initialValue);
        myCValue = initialValue;
        myLabel = label;
    }
    
    public ValueText (String label, double initialValue) {
        super(label + " " + initialValue);
        myDValue = initialValue;
        myLabel = label;
    }

    /**
     * Returns displayed value.
     */
    public int getIValue() {
        return myValue;
    }
    
    public char getCValue() {
        return myCValue;
    }
    
    public double getDValue() {
        return myDValue;
    }
    
    /**
     * Sets a value instead of adding it 
     * 
     */
    public void setValue(int x){
    	myValue = x;
    	setText(myLabel + " " + myValue);
    }
    
    public void setValue(char x){
    	myCValue = x;
    	setText(myLabel + " " + myCValue);
    }
    
    public void setValue(double x){
    	myDValue = x;
    	setText(myLabel + " " + (double)Math.round(myDValue * 10d) / 10d);
    }


    /**
     * Update displayed value.
     */
    public void updateValue (int value) {
        myValue += value;
        setText(myLabel + " " + myValue);
    }
    
    public void updateValue(double value) {
        myDValue += value;
        setText(myLabel + " " + (double)Math.round(myDValue * 10d) / 10d);
    }
    
    public void updateValue (char value) {
        myCValue = value;
        setText(myLabel + " " + myCValue);
    }

    /**
     * Reset displayed value to its initial value
     */
    public void resetValue () {
        myValue = myInitialValue;
        setText(myLabel + " " + myValue);
    }
}
