import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Trie {
	private ArrayList<TrieNode> nodes;
	private TrieNode root;
	private ArrayList<String> dictionary;

	/**
	 * Just a simplified print function, prevents the exhaustion that comes from
	 * repeatedly typing 'StdOut.println'
	 * 
	 * @param Object
	 *            to be printed
	 */
	public static void print(Object s) {
		StdOut.println(s);
	}

	/**
	 * Constructor, fills the initial trie with all node from the lexicon
	 * provided
	 * 
	 * @param filename
	 *            String indicating the location of the lexicon file (assumed to
	 *            be in the /src folder)
	 * @param maxLength
	 *            int used to limit the maximum allowed length of a word in the
	 *            lexicon
	 */
	public Trie(String filename, int maxLength) {
		nodes = new ArrayList<TrieNode>();
		root = new TrieNode(0);
		nodes.add(root);
		initDict(filename, maxLength);
	}

	/**
	 * Reads the lexicon file, adds each word to the initial trie
	 * 
	 * @param filename
	 *            String indicating the location of the lexicon file (assumed to
	 *            be in the /src folder)
	 * @param maxLength
	 *            int used to limit the maximum allowed length of a word in the
	 *            lexicon
	 */
	public void initDict(String filename, int max_length) {
		setDictionary(new ArrayList<String>());
		File f = new File("src/" + filename);
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() < max_length) {
					getDictionary().add(line.toLowerCase());
				}
			}
		} catch (Exception e) {
			System.err.println("Poop");
		}

		for (String word : getDictionary()) {
			addWord(word);
		}
	}

	/**
	 * Allows this recursive function to be called with just one parameter
	 * 
	 * @param word
	 *            String to be added to the trie
	 */
	public void addWord(String word) {
		addWord(word, root);
	}

	/**
	 * Recursively checks if the word requires a new branch on the trie
	 * 
	 * @param word
	 *            String to be added to the trie
	 * @param node
	 *            Node being checked to determine branching behavior
	 */
	public void addWord(String word, TrieNode node) {

		// If this is the end of the word, create a new node and mark it as the
		// end of a word
		if (word.length() == 1) {
			TrieNode nextNode = new TrieNode(nodes.size());
			nodes.add(nextNode);
			node.addEdge(word.charAt(0), nextNode);
			nextNode.setTerminal(true);
			return;
		}

		// If there is a branch that this word can go down, follow that branch
		if (node.contains(word.charAt(0))) {
			TrieNode next = nodes.get(node.getNextNodeFromEdge(word.charAt(0)));
			addWord(word.substring(1), next);
		}
		// If this is not the end of a word, and there are no branches of the
		// trie that contain the first letter of the word, create a new branch
		else {
			TrieNode nextNode = new TrieNode(nodes.size());
			nodes.add(nextNode);
			node.addEdge(word.charAt(0), nextNode);
			addWord(word.substring(1), nextNode);
		}
	}

	/**
	 * Allows the recursive function to be called with one variable
	 * 
	 * @param word
	 *            String representing the word being searched for
	 * @return boolean true if the trie contains the string, false if not
	 */
	public boolean containsWord(String word) {
		return containsWord(root, word, false);
	}

	/**
	 * Allows for error correction, if one of the nodes changes position within
	 * the nodes list
	 * 
	 * @param nodeId
	 *            int representing the original nodeId
	 * @return current nodeId, allowing for a shift in node position within the
	 *         list
	 */
	public int indexAfterCleanup(int nodeId) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getNodeId() == nodeId) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Recursively checks if the word is contained within the trie
	 * 
	 * @param n
	 *            TrieNode, the current node on the branch being followed
	 * @param word
	 *            String representing the remainder of the word to look for
	 * @param tf
	 *            boolean representing whether the word has been found in the
	 *            function
	 * @return tf True if word is contained in the trie, False if not
	 */
	public boolean containsWord(TrieNode n, String word, boolean tf) {
		for (TrieEdge e : n.edgesOutOf) {
			if (e.getEdgeName() == word.charAt(0)) {
				// if the word is longer than one character, check if there is
				// an edge leading out of the node that has the same first
				// character
				if (word.length() > 1) {
					int adjustedIndex = indexAfterCleanup(e.getTo());
					tf = containsWord(nodes.get(adjustedIndex),
							word.substring(1), tf);
				}
				// Base case, indicating that the word is in the trie, as long
				// as the following node is marked as terminal
				else {
					int adjustedIndex = indexAfterCleanup(n
							.getNextNodeFromEdge(word.charAt(0)));
					if (nodes.get(adjustedIndex).isTerminal()) {
						tf = true;
						return true;
					}
				}
			}
		}
		return tf;
	}

	/** Returns the trie as a string */
	public String toString() {
		String str = "";
		for (TrieNode n : nodes) {
			str += n.toString();
		}
		return str;
	}

	public ArrayList<TrieNode> getNodes() {
		return nodes;
	}

	public ArrayList<String> getDictionary() {
		return dictionary;
	}

	public void setDictionary(ArrayList<String> dictionary) {
		this.dictionary = dictionary;
	}

	public void setNodes(ArrayList<TrieNode> nodes) {
		this.nodes = nodes;
	}

	public static void main(String args[]) {
		Trie t = new Trie("testdict.txt", 15);
		print(t);
	}

}
