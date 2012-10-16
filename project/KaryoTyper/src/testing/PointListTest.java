package testing;

import java.awt.Point;
import java.util.LinkedList;

import basic_objects.PointList;

import junit.framework.TestCase;

public class PointListTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testSetList(){
		PointList tempList=new PointList(new Point(10,2),2);
		tempList.addPoint(new Point(3,2),2);
		tempList.addPoint(new Point(5,1),2);
		tempList.addPoint(new Point(6,10),5);
		
		PointList pointList=new PointList();
		pointList.setList(tempList);
		
		assertEquals(pointList.getDistanceFromEdge(),2);
		assertEquals(pointList.getList().size(),3);
		assertTrue(pointList.getList().contains(new Point(5,1)));
		assertFalse(pointList.getList().contains(new Point(6,10)));
		
	}
	public void testAddPoint(){
		PointList tempList=new PointList(new Point(10,2),2);
		assertTrue(tempList.addPoint(new Point(3,2),2));
		assertTrue(tempList.addPoint(new Point(5,1),2));
		assertFalse(tempList.addPoint(new Point(6,10),5));
		assertFalse(tempList.addPoint(new Point(6,11),22));

	}
	
}
