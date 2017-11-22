import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Scrabbot {

	private ArrayList<String> dictionary;
	private Map<String, Integer> wordValues;
	private Map<Character, Integer> letterValues;
	private ArrayList<Character> letterBag;

	public Scrabbot() {
		initializeGameDictionary();
		initializeBag();
		initializeletterValues();
		fillWordValues();
		for (String word: dictionary){
			StdOut.println(word + ": " + wordValues.get(word));
		}
	}
	
	private void fillWordValues(){
		wordValues = new HashMap<String, Integer>();
		for (String word: dictionary){
			int value = 0;
			for (char letter: word.toCharArray()){
				value+=getLetterValue(letter);
			}
			wordValues.put(word, value);
		}
	}

	private void initializeletterValues() {
		letterValues = new HashMap<Character, Integer>();
		letterValues.clear();
		letterValues.put('a', 1);
		letterValues.put('b', 3);
		letterValues.put('c', 3);
		letterValues.put('d', 2);
		letterValues.put('e', 1);
		letterValues.put('f', 4);
		letterValues.put('g', 2);
		letterValues.put('h', 4);
		letterValues.put('i', 1);
		letterValues.put('j', 8);
		letterValues.put('k', 5);
		letterValues.put('l', 1);
		letterValues.put('m', 3);
		letterValues.put('n', 1);
		letterValues.put('o', 1);
		letterValues.put('p', 3);
		letterValues.put('q', 10);
		letterValues.put('r', 1);
		letterValues.put('s', 1);
		letterValues.put('t', 1);
		letterValues.put('u', 1);
		letterValues.put('v', 4);
		letterValues.put('w', 4);
		letterValues.put('x', 8);
		letterValues.put('y', 4);
		letterValues.put('z', 10);
		letterValues.put('_', 0);
	}

	public void initializeBag() {
		letterBag = new ArrayList<Character>();
		letterBag.clear();
		letterBag.add('k');
		letterBag.add('j');
		letterBag.add('x');
		letterBag.add('q');
		letterBag.add('z');
		for (int i = 0; i < 12; i++) {
			if (i < 2) {
				letterBag.add('b');
				letterBag.add('c');
				letterBag.add('m');
				letterBag.add('p');
				letterBag.add('f');
				letterBag.add('h');
				letterBag.add('w');
				letterBag.add('v');
				letterBag.add('y');
			}
			if (i < 3) {
				letterBag.add('g');
			}
			if (i < 4) {
				letterBag.add('l');
				letterBag.add('s');
				letterBag.add('u');
				letterBag.add('d');
			}
			if (i < 6) {
				letterBag.add('n');
				letterBag.add('r');
				letterBag.add('t');
			}
			if (i < 8) {
				letterBag.add('o');
			}
			if (i < 9) {
				letterBag.add('a');
				letterBag.add('i');
			}
			letterBag.add('e');
		}
	}

	public void initializeGameDictionary() {
		dictionary = new ArrayList<String>();
		dictionary.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(
				"/Users/Ben/Desktop/Workspace/Scrabbot2k17/src/twl06.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				dictionary.add(line.toLowerCase());
			}
		} catch (Exception e) {
			System.err.println("Poop");
		}
	}

	public int getLetterValue(char letter) {
		return letterValues.get(letter);
	}

	public static void main(String[] args) {
		Scrabbot s = new Scrabbot();
	}

}
