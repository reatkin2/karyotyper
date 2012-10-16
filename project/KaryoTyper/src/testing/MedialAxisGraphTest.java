package testing;

import java.awt.Point;
import java.lang.reflect.Method;
import java.util.LinkedList;

import chromosome.ChromosomeCluster;

import basic_objects.Vertex;

import medial_axis.DistanceMap;
import medial_axis.MedialAxisGraph;
import junit.framework.TestCase;

public class MedialAxisGraphTest extends TestCase {
	private LinkedList<Point> straightLine;
	private LinkedList<Point> diagonalLine;
	private LinkedList<Point> curvedLine;
	private LinkedList<Point> oneIntersection4branches;
	private LinkedList<Point> threeSegmentGraph;
	private DistanceMap distanceMap;
	
	protected void setUp() throws Exception {
		super.setUp();
		distanceMap=new DistanceMap(15,15);
		for(int i=0;i<distanceMap.getWidth();i++){
			for(int j=0;j<distanceMap.getHeight();j++){
				if(j==distanceMap.getHeight()-1||i==distanceMap.getWidth()-1){
					distanceMap.setDistanceFormEdge(new Point(i,j), 0);
				}
				else{
					distanceMap.setDistanceFormEdge(new Point(i,j), 10);
				}
			}
		}
		straightLine=new LinkedList<Point>();
		straightLine.add(new Point(0,0));		
		straightLine.add(new Point(0,1));
		straightLine.add(new Point(0,2));
		straightLine.add(new Point(0,3));
		straightLine.add(new Point(0,4));
		straightLine.add(new Point(0,5));
		straightLine.add(new Point(0,6));
		straightLine.add(new Point(0,7));
		straightLine.add(new Point(0,8));
		straightLine.add(new Point(0,9));
		straightLine.add(new Point(0,10));
		straightLine.add(new Point(0,11));
		
		curvedLine=new LinkedList<Point>();
		curvedLine.add(new Point(0,0));
		curvedLine.add(new Point(0,1));
		curvedLine.add(new Point(0,2));
		curvedLine.add(new Point(0,3));
		curvedLine.add(new Point(1,3));
		curvedLine.add(new Point(1,4));
		curvedLine.add(new Point(2,4));
		curvedLine.add(new Point(2,5));
		curvedLine.add(new Point(3,5));
		curvedLine.add(new Point(3,6));
		curvedLine.add(new Point(3,7));
		curvedLine.add(new Point(2,8));
		curvedLine.add(new Point(2,9));
		curvedLine.add(new Point(1,9));
		curvedLine.add(new Point(1,10));
		curvedLine.add(new Point(0,10));
		
		diagonalLine=new LinkedList<Point>();
		diagonalLine.add(new Point(0,0));
		diagonalLine.add(new Point(1,1));
		diagonalLine.add(new Point(2,2));
		diagonalLine.add(new Point(3,3));
		diagonalLine.add(new Point(4,4));
		diagonalLine.add(new Point(5,5));
		diagonalLine.add(new Point(6,6));
		diagonalLine.add(new Point(7,7));
		diagonalLine.add(new Point(8,8));
		diagonalLine.add(new Point(9,9));
		diagonalLine.add(new Point(10,10));
		
		oneIntersection4branches=new LinkedList<Point>();
		//intersection
		oneIntersection4branches.add(new Point(10,10));
		//branch1
		oneIntersection4branches.add(new Point(9,9));
		oneIntersection4branches.add(new Point(8,8));
		oneIntersection4branches.add(new Point(7,7));
		oneIntersection4branches.add(new Point(6,6));
		oneIntersection4branches.add(new Point(5,5));
		oneIntersection4branches.add(new Point(4,4));
		oneIntersection4branches.add(new Point(3,3));
		oneIntersection4branches.add(new Point(2,2));
		//branch2
		oneIntersection4branches.add(new Point(9,11));
		oneIntersection4branches.add(new Point(8,11));
		oneIntersection4branches.add(new Point(7,11));
		oneIntersection4branches.add(new Point(6,11));
		oneIntersection4branches.add(new Point(5,11));
		oneIntersection4branches.add(new Point(4,11));
		oneIntersection4branches.add(new Point(3,11));
		//branch3 touches distance map 0
		oneIntersection4branches.add(new Point(11,11));
		oneIntersection4branches.add(new Point(12,12));
		oneIntersection4branches.add(new Point(13,13));
		oneIntersection4branches.add(new Point(14,14));
		//branch4 touches distance map 0
		oneIntersection4branches.add(new Point(11,9));
		oneIntersection4branches.add(new Point(11,8));
		oneIntersection4branches.add(new Point(11,7));
		oneIntersection4branches.add(new Point(11,6));
		oneIntersection4branches.add(new Point(11,5));
		oneIntersection4branches.add(new Point(12,4));
		oneIntersection4branches.add(new Point(13,5));
		oneIntersection4branches.add(new Point(14,6));
		
		
		threeSegmentGraph=new LinkedList<Point>();
		//segment1
		threeSegmentGraph.add(new Point(9,11));
		threeSegmentGraph.add(new Point(8,11));
		threeSegmentGraph.add(new Point(7,11));
		threeSegmentGraph.add(new Point(6,11));
		threeSegmentGraph.add(new Point(5,11));
		threeSegmentGraph.add(new Point(4,11));
		threeSegmentGraph.add(new Point(3,11));
		//segment2 touches distance map 0
		threeSegmentGraph.add(new Point(11,11));
		threeSegmentGraph.add(new Point(12,12));
		threeSegmentGraph.add(new Point(13,13));
		threeSegmentGraph.add(new Point(14,14));
		//segment3 touches distance map 0
		threeSegmentGraph.add(new Point(11,9));
		threeSegmentGraph.add(new Point(11,8));
		threeSegmentGraph.add(new Point(11,7));
		threeSegmentGraph.add(new Point(11,6));


	}
	public void testBuildGraph(){
		
		MedialAxisGraph testGraph=new MedialAxisGraph();
		testGraph.buildGraph(this.oneIntersection4branches, distanceMap);
		assertEquals(testGraph.getAxisGraph().size(),28);
		assertEquals(testGraph.getIntersectionCount(testGraph.getAxisGraph()),1);
		assertEquals(testGraph.getSegmentCount(),1);
		
		testGraph=new MedialAxisGraph();
		testGraph.buildGraph(this.straightLine, distanceMap);
		assertEquals(testGraph.getAxisGraph().size(),12);
		assertEquals(testGraph.getIntersectionCount(testGraph.getAxisGraph()),0);
		assertEquals(testGraph.getSegmentCount(),1);

		testGraph=new MedialAxisGraph();
		testGraph.buildGraph(this.threeSegmentGraph, distanceMap);
		assertEquals(testGraph.getAxisGraph().size(),15);
		assertEquals(testGraph.getIntersectionCount(testGraph.getAxisGraph()),0);
		assertEquals(testGraph.getSegmentCount(),3);
	
	}
	
	public void testRemoveSegments(){
		
		LinkedList<Point> pointList;
		MedialAxisGraph testGraph=new MedialAxisGraph();
		testGraph.buildGraph(this.oneIntersection4branches, distanceMap);
		testGraph.removeSegments(5, -1);
		pointList=testGraph.getMedialAxisFromGraph();
		assertTrue(pointList.contains(new Point(5,11)));
		assertTrue(pointList.contains(new Point(5,5)));
		assertTrue(pointList.contains(new Point(11,6)));
		assertFalse(pointList.contains(new Point(12,12)));
		assertEquals(this.oneIntersection4branches.size()-4,pointList.size());
		

	}
	public void testRemoveUnconnectedSegments(){
		
		LinkedList<Point> pointList;
		MedialAxisGraph testGraph=new MedialAxisGraph();
		testGraph.buildGraph(this.threeSegmentGraph, distanceMap);
		testGraph.removeUnconnectedSegments(4);
		pointList=testGraph.getMedialAxisFromGraph();
		assertFalse(pointList.contains(new Point(11,11)));
		assertFalse(pointList.contains(new Point(11,7)));
		assertTrue(pointList.contains(new Point(8,11)));
		assertEquals(this.threeSegmentGraph.size()-pointList.size(),8);

	}
	public void testAddVertex(){
		LinkedList<Point> pointList;
		MedialAxisGraph testGraph=new MedialAxisGraph();
		testGraph.buildGraph(this.threeSegmentGraph, distanceMap);
		assertEquals(testGraph.getSegmentCount(),3);
		assertEquals(testGraph.getIntersectionCount(testGraph.getAxisGraph()),0);
		testGraph.addVertex(new Vertex(new Point(10,10),10));
		pointList=testGraph.getMedialAxisFromGraph();
		assertTrue(pointList.contains(new Point(10,10)));
		assertEquals(testGraph.getSegmentCount(),1);
		assertEquals(testGraph.getIntersectionCount(testGraph.getAxisGraph()),1);
		

	}

	public void testGetSegment(){
	
		LinkedList<Point> pointList;
	
		MedialAxisGraph testGraph=new MedialAxisGraph();
		LinkedList<Vertex> testSegment=new LinkedList<Vertex>();
		testGraph.buildGraph(straightLine, distanceMap);
		testSegment.add(testGraph.getAxisGraph().get(0));
		testGraph.getSegment(testSegment, 0);
		pointList=testGraph.getMedialAxisFromGraph();
		assertTrue(pointList.contains(new Point(0,11)));
		assertEquals(pointList.size(),12);
		
		
		testGraph=new MedialAxisGraph();
		testSegment=new LinkedList<Vertex>();
		testGraph.buildGraph(diagonalLine, distanceMap);
		testSegment.add(testGraph.getAxisGraph().get(0));
		testGraph.getSegment(testSegment, 0);
		pointList=testGraph.getMedialAxisFromGraph();
		assertTrue(pointList.contains(new Point(10,10)));
		assertEquals(pointList.size(),11);
	
		testGraph=new MedialAxisGraph();
		testSegment=new LinkedList<Vertex>();
		testGraph.buildGraph(curvedLine, distanceMap);
		testSegment.add(testGraph.getAxisGraph().get(0));
		testGraph.getSegment(testSegment, 0);
		pointList=testGraph.getMedialAxisFromGraph();
		assertTrue(pointList.contains(new Point(0,10)));
		assertEquals(pointList.size(),16);
	
		
	}
	public void testFillInSkeleton(){
		//TODO(aamcknig): add some tests
	}
}
