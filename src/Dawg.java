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

	public void reduceGraph() {
		distanceToRoot = new int[nodes.size()];
		leaves = new ArrayList<DawgNode>();
		search = new boolean[nodes.size()];
		for (int i = 0; i < distanceToRoot.length; i++) {
			distanceToRoot[i] = distanceToRoot(nodes.get(i));
		}
		markLeaves(nodes, 0);
		for (int i = 0; i < leaves.size(); i++) {
			//StdOut.println(i +"/"+leaves.size()+"\t"+ ((int)1000*i/leaves.size())/10.0+ "% of the way through the leaves merge");
			for (int j = i + 1; j < leaves.size(); j++) {
				try {
					if (leaves.get(i).getEdgesInto().get(0).getEdgeName() == (leaves
							.get(j).getEdgesInto().get(0).getEdgeName())) {
						changePointers(leaves.get(i), leaves.get(j));
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		print("HERE");
		crunchLeaves();
		
		cleanup();
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

	public void crunchLeaves() {
		for (DawgNode n : nodes) {
			//StdOut.println(((int)1000*nodes.indexOf(n)/nodes.size())/10.0);
			for (DawgNode m : nodes) {
				if (n.equals(m)) {
					continue;
				} else if (distanceToRoot[n.getNodeId()] == distanceToRoot[m
						.getNodeId()]
						&& distanceToRoot[n.getNodeId()] > 0
						&& m.isTerminal() && n.isTerminal()) {
					for (DawgEdge e : m.edgesInto) {
						DawgNode f = e.getFrom();
						f.edgesOutOf.remove(f.getEdge(e.getEdgeName()));
						e.setToId(n);
						e.getFrom().edgesOutOf.add(e);
						n.edgesInto.add(e);
					}
					m.edgesInto.clear();
				}
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

	public void changePointers(DawgNode i, DawgNode j) {
		if (distanceToRoot[i.getNodeId()] == distanceToRoot[j.getNodeId()]) {
			for (DawgEdge e : i.edgesInto) {
				for (DawgEdge f : j.edgesInto) {
					if (e.getEdgeName() == f.getEdgeName()
							&& !i.edgesInto.isEmpty() && !j.edgesInto.isEmpty()) {
						changePointers(e.getFrom(), f.getFrom());
					}
				}
			}

			for (DawgNode n : j.nodesInto) {
					for (DawgEdge e : n.edgesOutOf) {
						if (e.getTo().getNodeId() == j.getNodeId()
								&& !i.equals(e.getFrom())) {

							e.setToId(i);
							i.edgesInto.add(e);
							j.edgesInto.clear();
						}
					}
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
				"/Users/Ben/Desktop/Workspace/Scrabbot2k17/src/dict.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				dictionary.add(line.toLowerCase());
				i += 1;
			}
		} catch (Exception e) {
			System.err.println("Poop");
		}

		Dawg d = new Dawg();
		for (String word : dictionary) {
			d.addWord(word);
		}
		d.reduceGraph();
		d.printDawg();
//		for (int j = 0; j < d.distanceToRoot.length; j++) {
//			StdOut.println("Node " + j + " = " + d.distanceToRoot[j]);
//		}
	}
}
