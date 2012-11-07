/**
 * 
 */
package basic_objects;

import java.awt.Point;
import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @author Robert
 *
 */
public class RadialVectorsTest extends TestCase {
	
	private RadialVectors radialVectors;
	private final Point CENTER_POINT = new Point(2,4);
	private final int NUM_VECTORS = 8;
	private final double DISTANCE = 10;

	/**
	 * @param name
	 */
	public RadialVectorsTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		radialVectors = new RadialVectors(CENTER_POINT, NUM_VECTORS, DISTANCE);
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link basic_objects.RadialVectors#getVectors()}.
	 */
	public void testGetVectors() {
		ArrayList<Vector> vectorList = radialVectors.getVectors();
		
		assertEquals(NUM_VECTORS, vectorList.size());
		//TODO: Test fails
		ArrayList<Vector> expectedList = new ArrayList<Vector>(NUM_VECTORS);
		
		double angleValue = Math.sqrt(2)/2;
		expectedList.add(new Vector(DISTANCE, 0));
		expectedList.add(new Vector(DISTANCE * angleValue, DISTANCE * angleValue));
		expectedList.add(new Vector(0, DISTANCE));
		expectedList.add(new Vector(-1 * DISTANCE * angleValue, DISTANCE * angleValue));
		expectedList.add(new Vector(-1 * DISTANCE, 0));
		expectedList.add(new Vector(-1 * DISTANCE * angleValue, -1 * DISTANCE * angleValue));
		expectedList.add(new Vector(0, -1 * DISTANCE));
		expectedList.add(new Vector(DISTANCE * angleValue, -1 * DISTANCE * angleValue));
		
		for (int i = 0; i < NUM_VECTORS; i++) {
			Vector testVector = expectedList.get(i);
			Vector actualVector = vectorList.get(i);
			
			testVector.x = Math.round(100000 * testVector.x);
			testVector.y = Math.round(100000 * testVector.y);
			
			actualVector.x = Math.round(100000 * actualVector.x);
			actualVector.y = Math.round(100000 * actualVector.y);
			
			assertEquals(testVector, actualVector);
			
		}
	}

	/**
	 * Test method for {@link basic_objects.RadialVectors#getTheta()}.
	 */
	public void testGetTheta() {
		double expectedTheta = 2 * Math.PI / NUM_VECTORS;
		assertEquals(expectedTheta, radialVectors.getTheta());
	}

	/**
	 * Test method for {@link basic_objects.RadialVectors#getCenterPoint()}.
	 */
	public void testGetCenterPoint() {
		assertEquals(CENTER_POINT, radialVectors.getCenterPoint());
	}

	/**
	 * Test method for {@link basic_objects.RadialVectors#getVectorsAsPointsOnImage()}.
	 */
	public void testGetVectorsAsPointsOnImage() {
		ArrayList<Point> pointList = radialVectors.getVectorsAsPointsOnImage();
		
		assertEquals(NUM_VECTORS, pointList.size());
		
		ArrayList<Vector> vectorList = radialVectors.getVectors();
		
		for (int i = 0; i < NUM_VECTORS; i++) {
			int imageX = (int)Math.round(vectorList.get(i).x + CENTER_POINT.x);
			int imageY = (int)Math.round(vectorList.get(i).y + CENTER_POINT.y);
			Point imagePoint = new Point(imageX, imageY);
			assertEquals(imagePoint,pointList.get(i));
		}
	}

	/**
	 * Test method for {@link basic_objects.RadialVectors#getRange(java.awt.Point, double, int)}.
	 */
	public void testGetRange() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link basic_objects.RadialVectors#getOpposite(java.awt.Point)}.
	 */
	public void testGetOpposite() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link basic_objects.RadialVectors#getPointAtIndex(int)}.
	 */
	public void testGetPointAtIndex() {
		fail("Not yet implemented");
	}

}
