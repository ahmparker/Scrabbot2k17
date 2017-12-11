import java.util.ArrayList;

public class TrieNode {
	public String nodesOutOf;
	public ArrayList<TrieEdge> edgesOutOf;
	public ArrayList<TrieEdge> edgesInto;
	public char[] edgeLetters;
	public int distanceFromRoot;

	private boolean terminal;
	private int nodeId;

	/**
	 * Initializes the TrieNode
	 * 
	 * @param id
	 *            int representing the specific ID of this node
	 */
	public TrieNode(int id) {
		edgesOutOf = new ArrayList<TrieEdge>();
		edgesInto = new ArrayList<TrieEdge>();
		nodesOutOf = "";
		edgeLetters = new char[27];
		terminal = false;
		nodeId = id;
		distanceFromRoot = 0;
	}

	/**
	 * Adds a TrieEdge to the node, making sure that the nextNode isn't already
	 * connected to this TrieNode
	 * 
	 * @param edgeLetter
	 *            char representing the EdgeName of the TrieEdge being added
	 * @param nextNode
	 *            TrieNode, the added edge will go from this node to the
	 *            nextNode
	 */
	public void addEdge(char edgeLetter, TrieNode nextNode) {
		int temp = (int) edgeLetter;
		int temp_integer = 96; // for lower case
		int index = 0;
		if (temp <= 122 & temp >= 97) {
			index = temp - temp_integer;
		}

		// If there is not already an edge going to the nextNode, or an edge
		// with the letter value being added, add a new edge to this edgesOutOf
		// list and the nextNode's edgesInto list, marking the distance to
		// the root on the nextNode
		if (!nodesOutOf.contains(String.valueOf(nextNode.nodeId + ", "))
				&& edgeLetters[index] != edgeLetter) {
			edgesOutOf.add(new TrieEdge(edgeLetter, nodeId, nextNode.nodeId));
			nodesOutOf += nextNode.nodeId + ", ";
			nextNode.edgesInto.add(new TrieEdge(edgeLetter, nodeId,
					nextNode.nodeId));
			edgeLetters[index] = edgeLetter;
			nextNode.distanceFromRoot = distanceFromRoot + 1;
		}

	}

	/**
	 * Returns true if this node contains an edge with this char in the
	 * edgesOutOf list
	 * 
	 * @param edgeletter
	 *            char representing the edgeName being considered
	 */
	public boolean contains(char edgeLetter) {
		for (TrieEdge e : edgesOutOf) {
			if (e.getEdgeName() == edgeLetter) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Given a char, if there is an edge in the edgesOutOf list with that
	 * edgeName, return the connected node's ID
	 */
	public int getNextNodeFromEdge(char edgeLetter) {
		for (TrieEdge e : edgesOutOf) {
			if (e.getEdgeName() == edgeLetter) {
				return e.getTo();
			}
		}
		return 0;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	public String toString() {
		String str = "";
		str += "Node ID: " + nodeId;
		str += "\t Edges Into: " + edgesInto + "\n";
		str += "\t Edges Out Of: " + edgesOutOf + "\n";
		str += "\t Terminal: " + terminal + "\n";
		return str;
	}

}
