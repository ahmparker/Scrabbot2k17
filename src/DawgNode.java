import java.util.ArrayList;

public class DawgNode {
	public ArrayList<DawgNode> nodesOutOf;
	public ArrayList<DawgNode> nodesInto;
	public ArrayList<DawgEdge> edgesOutOf;
	public ArrayList<DawgEdge> edgesInto;
	public ArrayList<Character> edgeLetters;

	public ArrayList<Character> getEdgeLetters() {
		return edgeLetters;
	}

	public void setEdgeLetters(ArrayList<Character> edgeLetters) {
		this.edgeLetters = edgeLetters;
	}

	private boolean terminal;
	private int nodeId;

	public DawgNode(int id) {
		edgesOutOf = new ArrayList<DawgEdge>();
		edgesInto = new ArrayList<DawgEdge>();
		nodesInto = new ArrayList<DawgNode>();
		nodesOutOf = new ArrayList<DawgNode>();
		edgeLetters = new ArrayList<Character>();
		terminal = false;
		nodeId = id;
	}

	public ArrayList<DawgEdge> getEdgesOutOf() {
		return edgesOutOf;
	}

	public void setEdgesOutOf(ArrayList<DawgEdge> edgesOutOf) {
		this.edgesOutOf = edgesOutOf;
	}

	public ArrayList<DawgEdge> getEdgesInto() {
		return edgesInto;
	}

	public void setEdgesInto(ArrayList<DawgEdge> edgesInto) {
		this.edgesInto = edgesInto;
	}

	public void addEdge(char edgeLetter, DawgNode nextNode) {
		if (!nodesOutOf.contains(nextNode) && !edgeLetters.contains(edgeLetter)) {
			edgesOutOf.add(new DawgEdge(edgeLetter, this, nextNode));
			nodesOutOf.add(nextNode);
			nextNode.edgesInto.add(new DawgEdge(edgeLetter, this, nextNode));
			edgeLetters.add(edgeLetter);
		}

	}

	public void removeEdge(char edgeLetter, DawgNode nextNode) {
		ArrayList<DawgEdge> temp = new ArrayList<DawgEdge>();
		for (DawgEdge e : edgesOutOf) {
			if (e.getEdgeName() == edgeLetter && e.getTo().equals(nextNode)) {
				temp.add(e);
			}
		}
		for (DawgEdge e : temp) {
			edgesOutOf.remove(e);
		}
		nodesOutOf.remove(nextNode);
	}

	public boolean connectsTo(DawgNode n) {
		for (DawgEdge e : edgesOutOf) {
			if (e.getTo().getNodeId() == n.getNodeId()) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(char edgeLetter) {
		for (DawgEdge e : edgesOutOf) {
			if (e.getEdgeName() == edgeLetter) {
				return true;
			}
		}
		return false;
	}

	public DawgEdge getEdge(char edgeLetter) {
		for (DawgEdge e : edgesOutOf) {
			if (e.getEdgeName() == edgeLetter) {
				return e;
			}
		}
		return null;
	}

	public DawgNode getNextNodeFromEdge(char edgeLetter) {
		for (DawgEdge e : edgesOutOf) {
			if (e.getEdgeName() == edgeLetter) {
				return e.getTo();
			}
		}
		return null;
	}

	public DawgNode getPrevNodeFromEdge(char edgeLetter) {
		for (DawgEdge e : edgesInto) {
			if (e.getEdgeName() == edgeLetter) {
				return e.getFrom();
			}
		}
		return null;
	}

	public boolean isLeaf() {
		if (edgesOutOf.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public ArrayList<DawgEdge> getEdges() {
		return edgesOutOf;
	}

	public void setEdges(ArrayList<DawgEdge> edges) {
		this.edgesOutOf = edges;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	public void printEdgesInto() {
		String str = "Edges Into:";
		for (DawgEdge e : edgesInto) {
			str += e.toString() + "\t";
		}
		StdOut.println(str);
	}

	public void printEdgesOutOf() {
		String str = "Edges Out Of: ";
		for (DawgEdge e : edgesOutOf) {
			str += e.toString() + "\t";
		}
		StdOut.println(str);
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
