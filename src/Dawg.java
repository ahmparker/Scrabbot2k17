import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Dawg {
	private ArrayList<DawgNode> nodes;
	DawgNode root;

	public Dawg() {
		nodes = new ArrayList<DawgNode>();
		root = new DawgNode(0);
		nodes.add(root);
	}

	public void addWord(String word) {
		addWord(word, root);
	}
	
	public void reduceGraph(){
		ArrayList<Boolean> search= new ArrayList<Boolean>(); 
		for (int i = 0; i < nodes.size(); i++) {
			search.add(false);
		}
		
		int i = 0;
		while(!nodes.get(i).isLeaf()){
			i++;
		}
		StdOut.println(nodes.get(i).getNodeId());
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

	public String toString() {
		String str = "";
		for (int i = 0; i < nodes.size(); i++) {
			str += "Node " + i + ":\n";
			if (nodes.get(i).isTerminal()) {
				str += "TERMINAL" + "\n";
			}
			for (DawgEdge e : nodes.get(i).edges) {
				str += "\t" + e.getEdgeName() + " - " + i + " to "
						+ e.getTo().getNodeId() + "\n";
			}
		}
		return str;
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
		int i =0;
		try (BufferedReader br = new BufferedReader(new FileReader(
				"/Users/Ben/Desktop/Workspace/Scrabbot2k17/src/testdict.txt"))) {
			String line;
			while ((line = br.readLine()) != null && i<2000) {
				dictionary.add(line.toLowerCase());
				i+=1;
			}
		} catch (Exception e) {
			System.err.println("Poop");
		}
		
		Dawg d = new Dawg();
		
		for (String word: dictionary){
			d.addWord(word);
		}

		StdOut.println(d.toString());
		
		d.reduceGraph();
	}
}
