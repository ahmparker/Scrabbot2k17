import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DawgTest {
	Dawg d = new Dawg("testdict.txt", 8);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testContainsWordString() {
		for (String word : d.dictionary) {
			assertTrue(d.containsWord(word));
		}
	}

}
