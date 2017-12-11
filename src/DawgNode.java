import java.util.ArrayList;
import java.util.Arrays;

public class DawgNode {
	public String nodesOutOf;
	public ArrayList<DawgEdge> edgesOutOf;
	public ArrayList<DawgEdge> edgesInto;
	public char[] edgeLetters;
	public int distanceFromRoot;
	
	public char[] getEdgeLetters() {
		return edgeLetters;
	}
	
	
//	public String getSortedEdgeLetters(){
//		//Arrays.sort(edgeLetters);
//		for (int i = 0; i < edgeLetters.length; i++) {
//			StdOut.println(edgeLetters[i]);
//		}
//	}
	
	public int hashCode(){
		String str = "EdgeIn: ";
		for(DawgEdge e: edgesInto){
			str += e.getEdgeName();
		}
		str+=" EdgeOut: ";
		for(DawgEdge e: edgesOutOf){
			str += e.getEdgeName();
		}
		str+=" IsTerminal: ";
		if(isTerminal()){
			str+="Y";
		}
		else{
			str+="N";
		}
		str+=" RootDist: "+distanceFromRoot;
		//StdOut.println(str);
		return str.hashCode();
	}

	public void setEdgeLetters(char edgeLetter) {
		int temp = (int) edgeLetter;
		int temp_integer = 96; // for lower case
		if (temp <= 122 & temp >= 97) {
			this.edgeLetters[temp - temp_integer] = edgeLetter;
		}
	}

	private boolean terminal;
	private int nodeId;

	public DawgNode(int id) {
		edgesOutOf = new ArrayList<DawgEdge>();
		edgesInto = new ArrayList<DawgEdge>();
		nodesOutOf = "";
		edgeLetters = new char[27];
		terminal = false;
		nodeId = id;
		distanceFromRoot = 0;
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
		int temp = (int)edgeLetter;
		int temp_integer = 96; //for lower case
		int index = 0;
		if(temp<=122 & temp>=97){
			index=temp-temp_integer;
		}
		if (!nodesOutOf.contains(String.valueOf(nextNode.nodeId + ", "))
				&& edgeLetters[index] != edgeLetter) {
			edgesOutOf.add(new DawgEdge(edgeLetter, nodeId, nextNode.nodeId));
			nodesOutOf += nextNode.nodeId + ", ";
			nextNode.edgesInto.add(new DawgEdge(edgeLetter, nodeId,
					nextNode.nodeId));
			edgeLetters[index] = edgeLetter;
			nextNode.distanceFromRoot=distanceFromRoot+1;
		}
		

	}

	public void removeEdge(char edgeLetter, DawgNode nextNode) {
		ArrayList<DawgEdge> temp = new ArrayList<DawgEdge>();
		for (DawgEdge e : edgesOutOf) {
			if (e.getEdgeName() == edgeLetter
					&& e.getTo() == nextNode.getNodeId()) {
				temp.add(e);
			}
		}
		for (DawgEdge e : temp) {
			edgesOutOf.remove(e);
		}
		String[] tempSA = nodesOutOf.split(", ");
		nodesOutOf = "";
		for (int i = 0; i < tempSA.length; i++) {
			if (!tempSA[i].equals(nextNode.nodeId + ", ")) {
				nodesOutOf += tempSA[i];
			}
		}
	}

	public boolean connectsTo(DawgNode n) {
		for (DawgEdge e : edgesOutOf) {
			if (e.getTo() == n.getNodeId()) {
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

	public int getNextNodeFromEdge(char edgeLetter) {
		for (DawgEdge e : edgesOutOf) {
			if (e.getEdgeName() == edgeLetter) {
				return e.getTo();
			}
		}
		return 0;
	}

	public int getPrevNodeFromEdge(char edgeLetter) {
		for (DawgEdge e : edgesInto) {
			if (e.getEdgeName() == edgeLetter) {
				return e.getFrom();
			}
		}
		return 0;
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
