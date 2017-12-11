import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class Dawg {
	private ArrayList<DawgNode> nodes;
	private DawgNode root;
	private boolean[] search;
	private ArrayList<DawgNode> leaves;
	private int[] distanceToRoot;
	private ArrayList<String> dictionary;
	private ArrayList<ArrayList<Integer>> distLoc;

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
	public Dawg(String filename, int maxLength) {
		nodes = new ArrayList<DawgNode>();
		root = new DawgNode(0);
		nodes.add(root);
		initDict(filename, maxLength);
	}

	/**
	 * Creates an ArrayList of ArrayLists<Integer>, distLoc Each index of the
	 * distLoc ArrayList contains an ArrayList containing the indices of other
	 * nodes at the same distance from the root
	 */
	public void updateDistLoc() {
		distLoc = new ArrayList<ArrayList<Integer>>();

		// Assumes the longest word is 16 letters, arbitrary
		for (int i = 0; i < 16; i++) {
			distLoc.add(new ArrayList<Integer>());
		}

		// Maps the indices of elements of nodes to their corresponding distance
		// to the root
		for (int i = 0; i < distanceToRoot.length; i++) {
			distLoc.get(distanceToRoot[i]).add(i);
		}
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
		int i = 0;
		File f = new File("src/" + filename);
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() < 8) {
					getDictionary().add(line.toLowerCase());
					i += 1;
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
	public void addWord(String word, DawgNode node) {

		// If this is the end of the word, create a new node and mark it as the
		// end of a word
		if (word.length() == 1) {
			DawgNode nextNode = new DawgNode(nodes.size());
			nodes.add(nextNode);
			node.addEdge(word.charAt(0), nextNode);
			nextNode.setTerminal(true);
			return;
		}

		// If there is a branch that this word can go down, follow that branch
		if (node.contains(word.charAt(0))) {
			DawgNode next = nodes.get(node.getNextNodeFromEdge(word.charAt(0)));
			addWord(word.substring(1), next);
		}
		// If this is not the end of a word, and there are no branches of the
		// trie that contain the first letter of the word, create a new branch
		else {
			DawgNode nextNode = new DawgNode(nodes.size());
			nodes.add(nextNode);
			node.addEdge(word.charAt(0), nextNode);
			addWord(word.substring(1), nextNode);
		}
	}

	/**
	 * Reduces the trie to a DAWG by combining nodes that have the same
	 * characteristics The characteristics considered: isTerminal, edgesIn, and
	 * edgesOut
	 */
	public void reduceGraph() {
		reduceTrieAgain();
		// distanceToRoot = new int[nodes.size()];
		// leaves = new ArrayList<DawgNode>();
		// search = new boolean[nodes.size()];
		//
		// // Finds the distance from each node to the root
		// for (int i = 0; i < distanceToRoot.length; i++) {
		// distanceToRoot[i] = distanceToRoot(nodes.get(i));
		// }
		//
		// // Uses the distance information to sort the nodes into bins based on
		// // distance to the root
		// updateDistLoc();
		//
		// // Marks all of the nodes that are leaves of their branches
		// // These will be the starting points for merging branches
		// markLeaves(nodes, 0);
		//
		// // Trace each branch backwards, merging elements that can be merged
		// for (int i = 0; i < leaves.size(); i++) {
		// StdOut.println(i + "/" + leaves.size() + "\t"
		// + ((int) 1000 * i / leaves.size()) / 10.0
		// + "% of the way through the leaves merge");
		// for (int j = i + 1; j < leaves.size(); j++) {
		// changePointers(leaves.get(i), leaves.get(j));
		// }
		// }
		// // print("HERE");
		//
		// // Condense edgesInto
		// crunchLeaves();
		//
		// // Delete all nodes that have no edges into them
		// cleanup();
	}

	public int distanceToRoot(DawgNode n) {
		if (n.equals(root) || n.edgesInto.isEmpty()) {
			distanceToRoot[n.getNodeId()] = 0;
			return 0;
		}
		if (!n.equals(root) && n.edgesInto.get(0).getFrom() == 0) {
			distanceToRoot[n.getNodeId()] = 1;
			return 1;
		}
		for (DawgEdge e : n.edgesInto) {
			if (e.getFrom() == 0 || distanceToRoot[n.getNodeId()] > 0) {
				return distanceToRoot[n.getNodeId()];
			} else {
				distanceToRoot[n.getNodeId()] = distanceToRoot(nodes.get(e
						.getFrom())) + 1;
				return distanceToRoot[n.getNodeId()];
			}
		}
		return 0;
	}

	public void changePointers(DawgNode m) {
		for (DawgNode n : nodes) {
			if(m.equals(n)){
				continue;
			}
			if (m.hashCode() == n.hashCode()) {
				if (!n.edgesInto.isEmpty()) {
					ArrayList<DawgEdge> t = new ArrayList<DawgEdge>(n.edgesInto);
					for (DawgEdge e : t) {
						for (DawgEdge f : nodes.get(e.getFrom()).edgesOutOf) {
							if (e.getFrom()==f.getFrom()&&e.getEdgeName()==f.getEdgeName()&&e.getTo()==e.getTo()) {
								f.setToId(m.getNodeId());
								m.edgesInto.add(new DawgEdge(e.getEdgeName(), e.getFrom(), m.getNodeId()));
								print("CHANGED: "+f);
							}
						}

					}
					n.edgesInto.clear();
				}
				if (!n.edgesOutOf.isEmpty()) {
					ArrayList<DawgEdge> t = new ArrayList<DawgEdge>(n.edgesOutOf);
					for (DawgEdge e : t) {
						//e.setFrom(m.getNodeId());
						//m.edgesOutOf.add(e);

						for (DawgEdge f : nodes.get(e.getTo()).edgesInto) {
							if (e.getFrom()==f.getFrom()&&e.getEdgeName()==f.getEdgeName()&&e.getTo()==e.getTo()) {
								f.setFrom(m.getNodeId());
								m.edgesInto.add(new DawgEdge(e.getEdgeName(),m.getNodeId(), e.getTo()));
								print("CHANGED: "+f);
							}
						}
					}
					n.edgesOutOf.clear();
				}
				//print(m + "\n"+n);
			}
		}
	}

	public void reduceTrieAgain() {
		for (DawgNode m : nodes) {
			changePointers(m);
		}
	}

	public void crunchLeaves() {
		printDawg();
		for (DawgNode n : nodes) {
			ArrayList<DawgEdge> t = new ArrayList<DawgEdge>();
			for (DawgEdge e : n.edgesInto) {
				DawgNode temp = nodes.get(e.getFrom());
				if (!temp.equals(root) && temp.edgesInto.isEmpty()) {
					t.add(e);
				}
			}
			for (DawgEdge e : t) {
				n.edgesInto.remove(e);
			}
		}

	}

	public void cleanup() {
		ArrayList<DawgNode> temp = new ArrayList<DawgNode>();
		for (DawgNode n : nodes) {
			if (n.getEdgesInto().isEmpty() && !n.equals(root)) {
				temp.add(n);
			}
		}
		for (DawgNode n : temp) {
			if (nodes.contains(n)) {
				nodes.remove(n);
			}
			if (leaves.contains(n)) {
				leaves.remove(n);
			}
		}
	}

	public boolean containsWord(String word) {
		return containsWord(root, word, false);
	}

	public int indexAfterCleanup(int nodeId) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getNodeId() == nodeId) {
				return i;
			}
		}
		return 0;
	}

	public boolean containsWord(DawgNode n, String word, boolean tf) {
		//print(n + "\t" + word);
		for (DawgEdge e : n.edgesOutOf) {
			if (e.getEdgeName() == word.charAt(0)) {
				if (word.length() > 1) {
					int adjustedIndex = indexAfterCleanup(e.getTo());
					tf = containsWord(nodes.get(adjustedIndex),
							word.substring(1), tf);
				} else {
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

	public void changePointers(DawgNode i, DawgNode j) {

		// if (i.edgesInto.isEmpty() || j.edgesInto.isEmpty()) {
		// return;
		// }
		// // print("i Letters: "+i.getSortedEdgeLetters());
		// // print("j Letters: "+i.getSortedEdgeLetters());
		// if (distanceToRoot[i.getNodeId()] == distanceToRoot[j.getNodeId()]) {
		// print(i);
		// print(j);
		// for (DawgEdge e : i.edgesInto) {
		// for (DawgEdge f : j.edgesInto) {
		// if (e.getEdgeName() == f.getEdgeName()
		// && !i.edgesInto.isEmpty() && !j.edgesInto.isEmpty()) {
		// //&& ((i.isTerminal() && j.isTerminal())||(!i.isTerminal() &&
		// !j.isTerminal()))) {
		// //print("H\n\n");
		// changePointers(nodes.get(e.getFrom()),
		// nodes.get(f.getFrom()));
		// break;
		// }
		// }
		// }
		// //boolean clearJ = false;
		// for (DawgEdge g : j.edgesInto) {
		// DawgNode n = nodes.get(g.getFrom());
		// for (DawgEdge e : n.edgesOutOf) {
		// if (e.getTo() == j.getNodeId() && !i.equals(e.getFrom())) {
		// e.setToId(i.getNodeId());
		// i.edgesInto.add(e);
		// //clearJ = true;
		// }
		// }
		// }
		// // if (clearJ) {
		// // j.edgesInto.clear();
		// // }
		// }
	}

	public void markLeaves(ArrayList<DawgNode> nodes, int i) {
		search[i] = true;
		if (nodes.get(i).isLeaf()) {
			leaves.add(nodes.get(i));
		}
		for (DawgEdge e : nodes.get(i).getEdges()) {
			if (!search[e.getTo()]) {
				markLeaves(nodes, e.getTo());
			}
		}
	}

	public void printDawg() {
		for (DawgNode n : nodes) {
			StdOut.println(n.getNodeId());
			print("Edges Into: " + n.getEdgesInto());
			print("Edges Out Of: " + n.getEdgesOutOf());
			if (n.isTerminal()) {
				print("Terminal");
			}
		}
	}

	public ArrayList<DawgNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<DawgNode> nodes) {
		this.nodes = nodes;
	}

	public static void main(String args[]) {
		Dawg d = new Dawg("testdict.txt", 15);
		//print(d.dictionary.toString());
		//d.printDawg();
		d.reduceGraph();
		d.printDawg();
		// for (int i = 0; i < 15; i++) {
		// print(d.nodes.get(i));
		// }
		// print(d.root.edgesOutOf);
		// print(d.nodes.get(d.root.edgesOutOf.get(0).getTo()));

	}

	public ArrayList<String> getDictionary() {
		return dictionary;
	}

	public void setDictionary(ArrayList<String> dictionary) {
		this.dictionary = dictionary;
	}
}
