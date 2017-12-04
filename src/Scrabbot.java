
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Scrabbot {

	public ArrayList<String> dictionary;
	public Map<String, Integer> wordValues;
	public Map<Character, Integer> letterValues;
	public ArrayList<Character> letterBag;
	public ArrayList<Character> letterRack;
	public char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z' };

	public Scrabbot() {
		initializeGameDictionary();
		initializeBag();
		initializeletterValues();
		fillWordValues();
		String rack = generateRandomRack();
		StdOut.println("Random rack: " + rack.toUpperCase());
		if(letterRack.contains('_')){
			for (int i = 0; i < alphabet.length; i++) {
				String temp_rack = rack.replace('_', alphabet[i]);
				StdOut.println(temp_rack);
				permutation(temp_rack);
			}
		}
		permutation(rack);
	}

	
	private void fillWordValues() {
		wordValues = new HashMap<String, Integer>();
		for (String word : dictionary) {
			int value = 0;
			for (char letter : word.toCharArray()) {
				value += getLetterValue(letter);
			}
			wordValues.put(word, value);
		}
	}

	public void permutation(String s) {
		permutation("", s);
	}

	private void permutation(String prefix, String s) {
		int n = s.length();
		if (n == 0 && dictionary.contains(prefix))
			StdOut.println(prefix);
		else {
			for (int i = 0; i < n; i++)
				permutation(prefix + s.charAt(i),
						s.substring(0, i) + s.substring(i + 1, n));
		}

	}

	private void printBagState() { //prints out the number of each letter left inside the bag
		StdOut.println("Number of Tiles in the bag: " + letterBag.size());
		StdOut.println("Letter count: ");
		char temp = '_';
		int charCount = 0;
		Collections.sort(letterBag);
		for (char c : letterBag) {
			if (c == temp) {
				charCount++;
			} else {
				String ptst = "\t" + temp + ": " + charCount;
				StdOut.println(ptst.toUpperCase());
				temp = c;
				charCount = 1;
			}
		}
	}

	private String generateRandomRack() { //creates a random rack of letters for our bot
		letterRack = new ArrayList<Character>(); //create our list of chars as our "rack"
		String view = "";
		for (int i = 0; i < 7; i++) {
			int randomIndex = (int) (Math.random() * (letterBag.size() - i)); //pick a random number to use as our index
			letterRack.add(letterBag.get(randomIndex)); //add the letter from letterBag w/ the corresponding index to letterRack
			view += letterBag.get(randomIndex); //add the letter to our rack
			letterBag.remove(randomIndex); //remove the chosen letter from the bag
		}
		return view; //return the string of chars that is our "rack"
	}

	private void initializeletterValues() { //create our hashmap connecting each letter to the amount of points it's worth
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

	public void initializeBag() { //add all the letters to the bag
		letterBag = new ArrayList<Character>();
		letterBag.clear(); //clear everything
		letterBag.add('k'); //these letters only get added once
		letterBag.add('j');
		letterBag.add('x');
		letterBag.add('q');
		letterBag.add('z');
		for (int i = 0; i < 12; i++) {//these letters get put in 2 times
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
				letterBag.add('_');
			}
			if (i < 3) { //3 g's
				letterBag.add('g'); 
			}
			if (i < 4) { // these letters get put in 4 times
				letterBag.add('l');
				letterBag.add('s');
				letterBag.add('u');
				letterBag.add('d');
			}
			if (i < 6) { //these ones in 6
				letterBag.add('n');
				letterBag.add('r');
				letterBag.add('t');
			}
			if (i < 8) { //8 times
				letterBag.add('o');
			}
			if (i < 9) {//and these ones 9 times
				letterBag.add('a');
				letterBag.add('i');
			}
			letterBag.add('e'); //12 e's
			

		}
	}

	public void initializeGameDictionary() {
		dictionary = new ArrayList<String>(); //new list of strings as our dictionary
		dictionary.clear(); //clear it 
		try (BufferedReader br = new BufferedReader(new FileReader( //create our buffered reader to read our giant txt file
				"dict.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				dictionary.add(line.toLowerCase()); //case doesn't matter in scrabble so we default to lowercase
			}
		} catch (Exception e) {
			System.err.println("Poop"); //error has occurred 
		}
	}

	public int getLetterValue(char letter) { //returns the points of a letter from our letterValues map
		return letterValues.get(letter);
	}
	
	public int getWordValue(String word) { //returns the points of a letter from our letterValues map
		return wordValues.get(word);
	}

	public static void main(String[] args) {
		Scrabbot s = new Scrabbot();
	}

}
