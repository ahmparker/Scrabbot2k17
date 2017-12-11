public class TrieEdge {
	private char edgeName;
	private int from;
	private int to;

	/**Initializes the TrieEdge*/
	public TrieEdge(char letter, int start, int end) {
		edgeName = letter;
		from = start;
		to = end;
	}

	public char getEdgeName() {
		return edgeName;
	}

	public int getTo() {
		return to;
	}

	public String toString() {
		return ("Edge " + edgeName + " from " + from + " to " + to );
	}

}
