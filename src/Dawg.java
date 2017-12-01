import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Dawg {
	private ArrayList<DawgNode> nodes;
	private DawgNode root;
	private boolean[] search;
	private ArrayList<DawgNode> leaves;
	private int[] distanceToRoot;
	private ArrayList<ArrayList<Integer>> distLoc;

	public static void print(Object s) {
		StdOut.println(s);
	}

	public Dawg() {
		nodes = new ArrayList<DawgNode>();
		root = new DawgNode(0);
		nodes.add(root);
	}

	public void addWord(String word) {
		addWord(word, root);
	}

	public void updateDistLoc() {
		distLoc = new ArrayList<ArrayList<Integer>>();
		int longestWord = findLongestWord();
		for (int i = 0; i < longestWord + 1; i++) {
			distLoc.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < distanceToRoot.length; i++) {
			distLoc.get(distanceToRoot[i]).add(i);
		}
	}

	public void reduceGraph() {
		distanceToRoot = new int[nodes.size()];
		leaves = new ArrayList<DawgNode>();
		search = new boolean[nodes.size()];
		long time = System.currentTimeMillis();
		for (int i = 0; i < distanceToRoot.length; i++) {
			distanceToRoot[i] = distanceToRoot(nodes.get(i));
		}
		updateDistLoc();
		int longest = findLongestWord();
		for (int i = longest; i > 0; i--) {
			print(i);
			crunchLeaves(i);
		}
		cleanup();
		// print("Time to measure all distances: "+(System.currentTimeMillis() -
		// time));
		// time = System.currentTimeMillis();
		// markLeaves(nodes, 0);
		// print(findLongestWord());

		// for (int i = 0; i < leaves.size(); i++) {
		// for (int j = i + 1; j < leaves.size(); j++) {
		// try {
		// if (leaves.get(i).getEdgesInto().get(0).getEdgeName() == (leaves
		// .get(j).getEdgesInto().get(0).getEdgeName())) {
		// changePointers(leaves.get(i), leaves.get(j));
		// }
		// } catch (Exception e) {
		// continue;
		// }
		// }
		// }

		// print("Time to mush leaves: "+(System.currentTimeMillis() - time));
		// time = System.currentTimeMillis() - time;
		// //crunchLeaves();
		// print("Time to crunch leaves: "+(System.currentTimeMillis() - time));
		// time = System.currentTimeMillis();
		// cleanup();
		// print("Time to cleanup: "+(System.currentTimeMillis() - time));
	}

	public int findLongestWord() {
		int longest_word = 0;
		for (int i = 0; i < distanceToRoot.length; i++) {
			if (distanceToRoot[i] > longest_word) {
				longest_word = distanceToRoot[i];
			}
		}
		return longest_word;
	}

	public int distanceToRoot(DawgNode n) {
		if (n.equals(root) || n.edgesInto.isEmpty()) {
			distanceToRoot[n.getNodeId()] = 0;
			return 0;
		}
		if (!n.equals(root) && n.edgesInto.get(0).getFrom().equals(root)) {
			distanceToRoot[n.getNodeId()] = 1;
			return 1;
		}
		for (DawgEdge e : n.edgesInto) {
			if (e.getFrom().equals(root) || distanceToRoot[n.getNodeId()] > 0) {
				return distanceToRoot[n.getNodeId()];
			} else {
				distanceToRoot[n.getNodeId()] = distanceToRoot(e.getFrom()) + 1;
				return distanceToRoot[n.getNodeId()];
			}
		}
		return 0;
	}

	// print(n.getNodeId() + " = "+ d);
	// for (DawgEdge e : n.edgesInto) {
	// if (e.getFrom().equals(root)
	// || distanceToRoot[n.getNodeId()] > 0) {
	// return distanceToRoot[n.getNodeId()]+1;
	// } else {
	// distanceToRoot[n.getNodeId()] = distanceToRoot(
	// e.getFrom(), d + 1);
	// return distanceToRoot[n.getNodeId()];
	//
	// }
	// }
	// return 0;

	private void connectAllNecessary(int n, DawgNode m) {
		for (int l : distLoc.get(n)) {
			if (l == m.getNodeId()) {
				continue;
			}
			print(nodes.get(l) + "\n" + m);

			ArrayList<DawgEdge> tempIn = new ArrayList<DawgEdge>();
			for (DawgEdge e : m.edgesInto) {
				for (DawgEdge f : nodes.get(l).edgesInto) {
					if (e.getEdgeName() == f.getEdgeName()|| (m.isTerminal() && nodes.get(l).isTerminal())) {
						if (!tempIn.contains(f)) {
							tempIn.add(f);
						}
						
						ArrayList<DawgEdge> tempOut = new ArrayList<DawgEdge>();
						for (DawgEdge g : m.edgesOutOf) {
							for (DawgEdge h : nodes.get(l).edgesOutOf) {
								if (((g.getEdgeName() == h.getEdgeName())
										|| (g.getTo().isTerminal()
										&& h.getTo().isTerminal())) && !tempOut
										.contains(h)) {
									print(h);
									tempOut.add(h);
								}
							}
						}
						for (DawgEdge t : tempOut) {
							print("OUT:"+tempOut);
							DawgEdge newEdge = new DawgEdge(t.getEdgeName(), m,
									m.getNextNodeFromEdge(t.getEdgeName()));
							print("Out: Removing " + t.getEdgeName() + " from "
									+ nodes.get(l).getNodeId()
									+ " and adding it to " + m.getNodeId());
							if (!m.edgesOutOf.contains(newEdge)) {
								m.edgesOutOf.add(newEdge);
								print(l+" Before "+nodes.get(l).edgesOutOf);
								nodes.get(l).getNextNodeFromEdge(t.getEdgeName()).edgesInto.remove(t);
								nodes.get(l).edgesOutOf.remove(t);
								print(l+" After " + nodes.get(l).edgesOutOf);
								

							}
						}
					}
				}
			}
			for (DawgEdge t : tempIn) {
				print("IN: "+tempIn);
				print("In: Removing " + t.getEdgeName() + " from "
						+ nodes.get(l).getNodeId() + " and adding it to "
						+ m.getNodeId());
				for(DawgEdge e: nodes.get(l).getPrevNodeFromEdge(t.getEdgeName()).edgesInto){
					print("Here");
					DawgEdge newEdge = new DawgEdge(e.getEdgeName(),e.getFrom(),m.getPrevNodeFromEdge(t.getEdgeName()));
					print(m.getPrevNodeFromEdge(t.getEdgeName()).getNodeId()+" Before "+m.getPrevNodeFromEdge(t.getEdgeName()).edgesInto);
					m.getPrevNodeFromEdge(t.getEdgeName()).edgesInto.add(newEdge);
					print(m.getPrevNodeFromEdge(t.getEdgeName()).getNodeId()+" After "+m.getPrevNodeFromEdge(t.getEdgeName()).edgesInto);
					print(l+" Before "+nodes.get(l).edgesInto);
					nodes.get(l).getPrevNodeFromEdge(t.getEdgeName()).edgesOutOf.remove(t);
					nodes.get(l).edgesInto.remove(t);
					print(l+" After "+nodes.get(l).edgesInto);
				}
				
			}
			print(nodes.get(l) + "\n" + m);
		}
	}

	public void crunchLeaves(int n) {
		for (DawgNode m : nodes) {
			double per = (((int) (nodes.indexOf(m) / (double) nodes.size() * 1000000)) / 10000.0);
			if ((per * 100) % 100 == 0) {
				print(per + " percent of the way through " + n);
			}
			if (distanceToRoot[m.getNodeId()] == n) {
				connectAllNecessary(n, m);
			}
		}

	}

	public boolean containsWord(DawgNode n, String word) {
		boolean tf = false;
		for (DawgEdge e : n.edgesOutOf) {
			if (e.getEdgeName() == word.charAt(0)) {
				if (word.length() > 1) {
					tf = containsWord(e.getTo(), word.substring(1));
				} else {
					print("Yes this word is contained within the Dawg");
					return true;
				}
			}
		}
		return tf;
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

	public void changePointers(DawgNode i, DawgNode j) {

		for (DawgEdge e : i.edgesInto) {
			for (DawgEdge f : j.edgesInto) {
				if (e.getEdgeName() == f.getEdgeName()
						&& !i.edgesInto.isEmpty() && !j.edgesInto.isEmpty()) {
					changePointers(e.getFrom(), f.getFrom());
				}
			}
		}

		for (DawgEdge e : j.edgesInto) {
			if (!i.equals(e.getFrom())) {
				e.setToId(i);
				i.edgesInto.add(e);
				j.edgesInto.clear();
			}

		}
	}

	public void markLeaves(ArrayList<DawgNode> nodes, int i) {
		search[i] = true;
		if (nodes.get(i).isLeaf()) {
			leaves.add(nodes.get(i));
		}
		for (DawgEdge e : nodes.get(i).getEdges()) {
			if (!search[e.getTo().getNodeId()]) {
				markLeaves(nodes, e.getTo().getNodeId());
			}
		}
	}

	public void addWord(String word, DawgNode node) {
		if (word.length() == 1) {
			DawgNode nextNode = new DawgNode(nodes.size());
			nodes.add(nextNode);
			node.addEdge(word.charAt(0), nextNode);
			nextNode.setTerminal(true);
			return;
		}
		if (node.contains(word.charAt(0))) {
			addWord(word.substring(1), node.getNextNodeFromEdge(word.charAt(0)));
		} else {
			DawgNode nextNode = new DawgNode(nodes.size());
			nodes.add(nextNode);
			node.addEdge(word.charAt(0), nextNode);
			addWord(word.substring(1), node.getNextNodeFromEdge(word.charAt(0)));
		}
	}

	public void printDawg() {
		for (DawgNode n : nodes) {
			StdOut.println(n.getNodeId());
			n.printEdgesOutOf();
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
		ArrayList<String> dictionary = new ArrayList<String>();
		dictionary.clear();
		int i = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(
				"/Users/Ben/Desktop/Workspace/Scrabbot2k17/src/testdict.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				dictionary.add(line.toLowerCase());
				i += 1;
			}
		} catch (Exception e) {
			System.err.println("Poop");
		}

		Dawg d = new Dawg();
		long time = System.currentTimeMillis();
		for (String word : dictionary) {
			d.addWord(word);
		}

		d.reduceGraph();
		d.printDawg();
	}
}
