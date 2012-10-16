package testing;

import java.awt.Point;
import java.util.LinkedList;

import medial_axis.SkeletonList;

import basic_objects.PointList;
import basic_objects.Vertex;

import junit.framework.TestCase;

public class SkeletonListTest extends TestCase {
	private LinkedList<Vertex> vertList;
	protected void setUp() throws Exception {
		super.setUp();
		vertList=new LinkedList<Vertex>();
		vertList.add(new Vertex(new Point(3,1),4));
		vertList.add(new Vertex(new Point(2,1),5));
		vertList.add(new Vertex(new Point(3,4),4));
		vertList.add(new Vertex(new Point(3,6),7));
		vertList.add(new Vertex(new Point(3,9),4));
		vertList.add(new Vertex(new Point(5,1),10));

	}
	public void testBuildFromVertexList(){
		
		SkeletonList skelList= new SkeletonList();
		skelList.buildFromVertexList(vertList);
		
		assertEquals(skelList.getList().size(),11);
		assertEquals(skelList.size(),6);
		
		PointList tempList=skelList.getListAtDistance(4);
		assertTrue(tempList.getList().contains(new Point(3,1)));
		assertEquals(tempList.getList().size(),3);
		
		PointList tempList2=skelList.getListAtDistance(10);
		assertTrue(tempList2.getList().contains(new Point(5,1)));
		assertEquals(tempList2.getList().size(),1);
	}
	public void testAdd(){
		SkeletonList skelList=new SkeletonList();
		skelList.add(new Point(4,4), 6);
		assertEquals(skelList.getList().size(),7);
		assertEquals(skelList.size(),1);
		
		skelList.add(new Point(6,6), 2);
		assertEquals(skelList.getList().size(),7);
		assertEquals(skelList.size(),2);
		
		skelList.add(new Point(7,7), 6);
		assertEquals(skelList.getList().size(),7);
		assertEquals(skelList.size(),3);

		skelList.add(new Point(7,8), 10);
		assertEquals(skelList.getList().size(),11);
		assertEquals(skelList.size(),4);

		skelList.add(new Point(9,9), 6);
		assertEquals(skelList.getList().size(),11);
		assertEquals(skelList.size(),5);
		assertEquals(skelList.getListAtDistance(6).getList().size(),3);

	}
	public void testGetListAtDistance(){
		SkeletonList skelList=new SkeletonList();
		skelList.add(new Point(4,4), 6);
		skelList.add(new Point(6,6), 2);
		skelList.add(new Point(7,7), 6);
		skelList.add(new Point(7,8), 10);
		skelList.add(new Point(9,9), 6);
		
		
		PointList tempList=skelList.getListAtDistance(6);
		assertEquals(tempList.getList().size(),3);
		assertTrue(tempList.getList().contains(new Point(4,4)));
		assertFalse(tempList.getList().contains(new Point(6,6)));
		assertTrue(tempList.getList().contains(new Point(7,7)));		

	}
	public void testGetOneList(){
		SkeletonList skelList=new SkeletonList();
		skelList.add(new Point(4,4), 6);
		skelList.add(new Point(6,6), 2);
		skelList.add(new Point(7,7), 6);
		skelList.add(new Point(7,8), 10);
		skelList.add(new Point(9,9), 6);
		
		
		LinkedList<Point> tempList=skelList.getOneList();
		assertEquals(tempList.size(),5);
		assertTrue(tempList.contains(new Point(4,4)));
		assertTrue(tempList.contains(new Point(6,6)));
		assertTrue(tempList.contains(new Point(7,7)));	
		assertTrue(tempList.contains(new Point(7,8)));	
		assertTrue(tempList.contains(new Point(9,9)));		

	}

}
