import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DawgTest {
	Dawg d = new Dawg("dict.txt", 8);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testContainsWordString() {
		//d.printDawg();
		for (String word : d.getDictionary()) {
			StdOut.println(d.getDictionary().indexOf(word)/d.getDictionary().size() + " percent done");
			assertTrue(d.containsWord(word));
		}
	}

}
