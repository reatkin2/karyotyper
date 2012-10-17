/**
 * 
 */
package basic_objects;

import java.awt.Point;
import java.util.LinkedList;

import medial_axis.DistanceMap;
import medial_axis.MedialAxisGraph;

import junit.framework.TestCase;

/**
 * @author Robert
 *
 */
public class VertexTest extends TestCase {

	private Vertex vertex;
	/** Points ordered in breadth-first ordering from vertex */
	private LinkedList<Point> pointList;
	
	/**
	 * @param name
	 */
	public VertexTest(String name) {
		super(name);
		
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		buildAxisGraph();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private void buildAxisGraph() {
		buildPointList();
		MedialAxisGraph graph = new MedialAxisGraph();
		graph.buildGraph(pointList,  new DistanceMap(100, 100));
		vertex = graph.getAxisGraph().getFirst();
	}
	
	private void buildPointList() {
		pointList = new LinkedList<Point>();
		pointList.add(new Point(20,20));
		pointList.add(new Point(21,20));
		pointList.add(new Point(22,21));
		pointList.add(new Point(23,22));
		pointList.add(new Point(23,23));
		pointList.add(new Point(23,24));
		pointList.add(new Point(19,19));
		pointList.add(new Point(19,18));
		pointList.add(new Point(19,17));
		pointList.add(new Point(18,16));
		pointList.add(new Point(18,15));
		
	}

	/**
	 * Test method for {@link basic_objects.Vertex#calculateTangentLine(double, double)}.
	 */
	public void testCalculateTangentLine() {
		double[] tangentLine = null;
		try {
			tangentLine = vertex.calculateTangentLine(3, 5);
		} catch (Exception e) {
			fail("Should not reach here.");
		}
		assertEquals(tangentLine.length, 2);
		
		double slope = tangentLine[0];
		double intercept = tangentLine[1];
		
		assertEquals(vertex.getPoint(), new Point(20,20));
		assertTrue(slope == (17.0 - 20.0)/(19.0 - 20.0) || slope == (22.0 - 20.0)/(23.0 - 20.0));
		assertTrue(intercept == -40.0 || intercept == (20.0 - 20.0*slope));
		
	}

	/**
	 * Test method for {@link basic_objects.Vertex#calculateOrthogonalLine(double, double)}.
	 */
	public void testCalculateOrthogonalLine() {
		double[] orthoLine = null;
		try {
			orthoLine = vertex.calculateOrthogonalLine(3, 5);
		} catch (Exception e) {
			fail("Should not reach here.");
		}
		assertEquals(orthoLine.length, 2);
		
		double slope = orthoLine[0];
		double intercept = orthoLine[1];
		
		assertEquals(vertex.getPoint(), new Point(20,20));
		assertTrue(slope == -1 * (19.0 - 20.0)/(17.0 - 20.0) || slope == -1 * (23.0 - 20.0)/(22.0 - 20.0));
		assertEquals(intercept, 20.0 - 20.0*slope);
	}

	/**
	 * Test method for {@link basic_objects.Vertex#hasBeenChecked()}.
	 */
	public void testHasBeenChecked() {
		assertFalse(vertex.hasBeenChecked());
	}

	/**
	 * Test method for {@link basic_objects.Vertex#setHasBeenChecked(boolean)}.
	 */
	public void testSetHasBeenChecked() {
		vertex.setHasBeenChecked(true);
		assertTrue(vertex.hasBeenChecked());
	}

	/**
	 * Test method for {@link basic_objects.Vertex#tangentLine(int)}.
	 */
	public void testTangentLine() {
		double[] tangent = null;
		try {
			tangent = vertex.calculateTangentLine(3, 5);
		} catch (Exception e) {
			fail("Should not reach here.");
		}
		assertEquals(tangent.length, 2);
		
		double slope = tangent[0];
		double intercept = tangent[1];
		
		assertEquals(vertex.getPoint(), new Point(20,20));
		LinkedList<Point> tangentLine = null;
		
		try {
			tangentLine = vertex.tangentLine(5);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not reach here.");
		}
		assertTrue(tangentLine.contains(new Point(20, 20)));
		assertTrue(tangentLine.contains(new Point(21, (int)(slope * 21 + intercept))));
		assertTrue(tangentLine.contains(new Point(19, (int)(slope * 19 + intercept))));
		assertTrue(tangentLine.contains(new Point(22, (int)(slope * 22 + intercept))));
		assertTrue(tangentLine.contains(new Point(18, (int)(slope * 18 + intercept))));
	}

	/**
	 * Test method for {@link basic_objects.Vertex#orthogonalLine(int)}.
	 */
	public void testOrthogonalLine() {
		double[] orthogonal = null;
		try {
			orthogonal = vertex.calculateOrthogonalLine(3, 5);
		} catch (Exception e) {
			fail("Should not reach here.");
		}
		assertEquals(orthogonal.length, 2);
		
		double slope = orthogonal[0];
		double intercept = orthogonal[1];
		
		assertEquals(vertex.getPoint(), new Point(20,20));
		LinkedList<Point> orthogonalLine = null;
		
		try {
			orthogonalLine = vertex.orthogonalLine(5);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not reach here.");
		}
		System.out.println("Orthogonal points:");
		for (Point p : orthogonalLine) {
			System.out.println("(" + p.x + ", " + p.y + ")");
		}
		LinkedList<Point> testPoints = new LinkedList<Point>();
		testPoints.add(new Point(20, 20));
		testPoints.add(new Point(21, (int)(slope * 21 + intercept)));
		testPoints.add(new Point(19, (int)(slope * 19 + intercept)));
		testPoints.add(new Point(22, (int)(slope * 22 + intercept)));
		testPoints.add(new Point(18, (int)(slope * 18 + intercept)));
		
		for (Point p : testPoints) {
			System.out.println("(" + p.x + ", " + p.y + ")");
		}
		
		assertTrue(orthogonalLine.contains(new Point(20, 20)));
		assertTrue(orthogonalLine.contains(new Point(21, (int)(slope * 21 + intercept))));
		assertTrue(orthogonalLine.contains(new Point(19, (int)(slope * 19 + intercept))));
		assertTrue(orthogonalLine.contains(new Point(22, (int)(slope * 22 + intercept))));
		assertTrue(orthogonalLine.contains(new Point(18, (int)(slope * 18 + intercept))));
	}

}
