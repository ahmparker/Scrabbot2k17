public class DawgEdge {
	private char edgeName;
	private DawgNode from;
	private DawgNode to;

	public DawgEdge(char letter, DawgNode start, DawgNode end) {
		edgeName = letter;
		from = start;
		to = end;
	}

	public char getEdgeName() {
		return edgeName;
	}

	public void setEdgeName(char edgeName) {
		this.edgeName = edgeName;
	}

	public DawgNode getFrom() {
		return from;
	}

	public void setFrom(DawgNode from) {
		this.from = from;
	}

	public DawgNode getTo() {
		return to;
	}

	public void setToId(DawgNode to) {
		if (to != from) {
			this.to = to;
		}
		else{
			StdOut.println(to + "SCREAMING" + from);
		}
	}

	public String toString() {
		return ("Edge " + edgeName + " from " + from.getNodeId() + " to " + to
				.getNodeId());
	}

}
