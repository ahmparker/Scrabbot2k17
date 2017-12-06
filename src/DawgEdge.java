public class DawgEdge {
	private char edgeName;
	private int from;
	private int to;

	public DawgEdge(char letter, int start, int end) {
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

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setToId(int to) {
		if (to != from) {
			this.to = to;
		}
		else{
			StdOut.println(to + "SCREAMING" + from);
		}
	}

	public String toString() {
		return ("Edge " + edgeName + " from " + from + " to " + to );
	}

}
