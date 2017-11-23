import java.util.ArrayList;


public class DawgNode {
	public ArrayList<DawgEdge> edges;
	private boolean terminal;
	private int nodeId;
	
	public DawgNode(int id){
		edges = new ArrayList<DawgEdge>();
		terminal = false;
		nodeId = id;
	}
	
	public void addEdge(char edgeLetter, DawgNode nextNode){
		edges.add(new DawgEdge(edgeLetter, this , nextNode));
	}
	
	public boolean contains(char edgeLetter){
		for(DawgEdge e: edges){
			if(e.getEdgeName()==edgeLetter){
				return true;
			}
		}
		return false;
	}
	
	public DawgEdge getEdge(char edgeLetter){
		for(DawgEdge e: edges){
			if(e.getEdgeName()==edgeLetter){
				return e;
			}
		}
		return null;
	}
	
	public DawgNode getNextNodeFromEdge(char edgeLetter){
		for(DawgEdge e: edges){
			if(e.getEdgeName()==edgeLetter){
				return e.getTo();
			}
		}
		return null;
	}
	
	public boolean isLeaf(){
		if(edges.isEmpty()){
			return true;
		}
		return false;
	}

	public boolean isTerminal() {
		return terminal;
	}
	public ArrayList<DawgEdge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<DawgEdge> edges) {
		this.edges = edges;
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

}
