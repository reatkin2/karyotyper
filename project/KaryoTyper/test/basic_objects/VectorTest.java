/**
 * 
 */
package basic_objects;

import java.awt.Point;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		vector = new Vector(1, 0);
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
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
		vector.rotateVector(Math.PI / 4);
		double testX = Math.sqrt(2) / 2;
		double testY = Math.sqrt(2) / 2;

		int roundedTestX = (int) Math.round(testX * 100000);
		int roundedTestY = (int) Math.round(testY * 100000);
		int roundedActualX = (int) Math.round(vector.x * 100000);
		int roundedActualY = (int) Math.round(vector.y * 100000);

		assertEquals(roundedTestX, roundedActualX);
		assertEquals(roundedTestY, roundedActualY);
	}

	/**
	 * Test method for {@link basic_objects.Vector#rotateVector(basic_objects.Vector, double)}.
	 */
	public void testRotateVectorVectorDouble() {
		Vector v1 = Vector.rotateVector(vector, Math.PI / 4);
		long actualX = Math.round(v1.x * 100000);
		long actualY = Math.round(v1.y * 100000);
		long testX = Math.round(Math.sqrt(2) / 2 * 100000);
		long testY = Math.round(Math.sqrt(2) / 2 * 100000);

		assertEquals(testX, actualX);
		assertEquals(testY, actualY);
	}

	/**
	 * Test method for {@link basic_objects.Vector#normalize(basic_objects.Vector)}.
	 */
	public void testNormalize() {
		Vector testVector = new Vector(3, 3);
		testVector = Vector.normalize(testVector);
		double newX = Math.sqrt(2) / 2;
		double newY = Math.sqrt(2) / 2;

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

	/**
	 * Test method for {@link basic_objects.Vector#getVectorBetweenTwoPoints(Point, Point)}
	 */
	public void testMagnitude() {
		Vector v1 = new Vector(1,0);
		assertEquals(1.0, Vector.magnitude(v1));
	}
	
	/**
	 * Test method for {@link basic_objects.Vector#dotProduct(Vector, Vector)
	 */
	public void testDotProduct() {
		Vector v1 = new Vector(1,2);
		Vector v2 = new Vector(3,2);
		double testProduct = 7;
		double actualProduct = Vector.dotProduct(v1, v2);
		assertEquals(testProduct, actualProduct);
	}

	/**
	 * Test method for
	 * {@link basic_objects.Vector#getVectorBetweenTwoPoints(java.awt.Point, java.awt.Point)}.
	 */
	public void testGetVectorBetweenTwoPoints() {
		Point p1 = new Point(1, 2);
		Point p2 = new Point(3, 4);

		Vector testVector = new Vector(p2.x - p1.x, p2.y - p1.y);
		Vector actualVector = Vector.getVectorBetweenTwoPoints(p1, p2);
		assertEquals(testVector, actualVector);
	}

	/**
	 * Test method for {@link basic_objects.Vector#getAngle(Vector, Vector)}.
	 */
	public void testGetAngle() {
		Vector v1 = new Vector(2, 1);
		double testAngle = Math.PI / 4;
		Vector v2 = Vector.rotateVector(v1, testAngle);

		double actualAngle = Vector.getAngle(v1, v2);

		int roundedTestAngle = (int) Math.round(testAngle * 100000);
		int roundedActualAngle = (int) Math.round(actualAngle * 100000);

		assertEquals(roundedTestAngle, roundedActualAngle);

		testAngle = 5 * Math.PI / 4;
		Vector v3 = Vector.rotateVector(v1, testAngle);
		actualAngle = Vector.getAngle(v1, v3);
		assertEquals(testAngle, actualAngle);

	}

}
