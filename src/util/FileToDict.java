package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class FileToDict {

    public Map<Integer, ArrayList<String>> readLines(String filename) {
        FileReader fileReader = null;
		try {
			fileReader = new FileReader(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<String> length4 = new ArrayList<String>();
        ArrayList<String> length5 = new ArrayList<String>();
        ArrayList<String> length6 = new ArrayList<String>();
        ArrayList<String> length7 = new ArrayList<String>();
        String line = null;
        try {
			while ((line = bufferedReader.readLine()) != null) {
				line.trim();
				if (line.matches("[a-z]+")) {
					int size = line.length();
					if (size == 4) {
						length4.add(line);
					} else if (size == 5) {
						length5.add(line);
					} else if (size == 6){
						length6.add(line);
					} else if (size == 7) {
						length7.add(line);
					}
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Map<Integer, ArrayList<String>> dictionary = new HashMap<Integer, ArrayList<String>>();
        dictionary.put(1, length4);
        dictionary.put(2, length5);
        dictionary.put(3, length6);
        dictionary.put(4, length7);
        
        return dictionary;
    }
}