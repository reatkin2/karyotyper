/**
 * 
 */
package basic_objects;

import java.io.File;

import characterization.ChromosomeBand;
import idiogram.IdiogramMap;
import junit.framework.TestCase;

/**
 * @author Robert
 *
 */
public class IdiogramMapTest extends TestCase {

	private IdiogramMap map;
	
	/**
	 * @param name
	 */
	public IdiogramMapTest(String name) {
		super(name);
		map = new IdiogramMap(new File(".\\ChromosomeIdiogramSheet.csv"));
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Test method for {@link idiogram.IdiogramMap#get(Idiogram)}.
	 */
	public void testGet() {
//		Chromosome chromosome = new Chromosome(10);
//		chromosome.add(new ChromosomeBand(ChromosomeBand.Type.WHITE, 21));
//		chromosome.add(new ChromosomeBand(ChromosomeBand.Type.BLACK, 10));
//		
//		int number = map.get(chromosome);
//		
//		assertEquals(number, 11);
		fail("Waiting for decision on implementation.");
	}

}
