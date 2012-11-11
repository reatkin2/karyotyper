/**
 * 
 */
package basic_objects;

import junit.framework.TestCase;

/**
 * @author Robert
 *
 */
public class VectorTest extends TestCase {
	
	private Vector vector;

	/**
	 * @param name
	 */
	public VectorTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		vector = new Vector(1, 0);
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link basic_objects.Vector#Vector(double, double)}.
	 */
	public void testVector() {
		double xComp = vector.x;
		double yComp = vector.y;
		
		assertEquals(1.0, xComp);
		assertEquals(0.0, yComp);
	}

	/**
	 * Test method for {@link basic_objects.Vector#rotateVector(double)}.
	 */
	public void testRotateVectorDouble() {
		vector.rotateVector(Math.PI/4);
		double testX = Math.sqrt(2)/2;
		double testY = Math.sqrt(2)/2;
		
		assertEquals(testX, vector.x);
		assertEquals(testY, vector.y);
	}

	/**
	 * Test method for {@link basic_objects.Vector#rotateVector(basic_objects.Vector, double)}.
	 */
	public void testRotateVectorVectorDouble() {
		Vector v1 = Vector.rotateVector(vector, Math.PI/4);
		long actualX = Math.round(v1.x * 100000);
		long actualY = Math.round(v1.y * 100000);
		long testX = Math.round(Math.sqrt(2)/2 * 100000);
		long testY = Math.round(Math.sqrt(2)/2 * 100000);
		
		
		
		assertEquals(testX, actualX);
		assertEquals(testY, actualY);
	}

	/**
	 * Test method for {@link basic_objects.Vector#normalize(basic_objects.Vector)}.
	 */
	public void testNormalize() {
		Vector testVector = new Vector(3,3);
		testVector = Vector.normalize(testVector);
		double newX = Math.sqrt(2)/2;
		double newY = Math.sqrt(2)/2;
		
		assertEquals(testVector.x, newX);
		assertEquals(testVector.y, newY);
	}

	/**
	 * Test method for {@link basic_objects.Vector#add(basic_objects.Vector, basic_objects.Vector)}.
	 */
	public void testAdd() {
		Vector v1 = new Vector(2, 5);
		Vector v2 = new Vector(-4, 9);
		v1 = Vector.add(v1, v2);
		double newX = 2 - 4;
		double newY = 5 + 9;
		
		assertEquals(v1.x, newX);
		assertEquals(v1.y, newY);
	}

}
