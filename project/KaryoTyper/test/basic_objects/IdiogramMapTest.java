/**
 * 
 */
package basic_objects;

import junit.framework.TestCase;

/**
 * @author Robert
 *
 */
public class IdiogramMapTest extends TestCase {

	/**
	 * @param name
	 */
	public IdiogramMapTest(String name) {
		super(name);
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
	 * Test method for {@link basic_objects.IdiogramMap#IdiogramMap()}.
	 */
	public void testIdiogramMap() {
		IdiogramMap map = new IdiogramMap();
		
		System.out.println(map.toString());
		
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link basic_objects.IdiogramMap#get(Idiogram)}.
	 */
	public void testGet() {
		IdiogramMap map = new IdiogramMap();
		
		Chromosome chromosome = new Chromosome(10);
		chromosome.add(new ChromosomeBand(ChromosomeBand.Type.WHITE, 21));
		chromosome.add(new ChromosomeBand(ChromosomeBand.Type.BLACK, 10));
		
		int number = map.get(chromosome);
		
		assertEquals(number, 11);
	}

}
