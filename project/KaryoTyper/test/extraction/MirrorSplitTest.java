package extraction;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import medial_axis.DistanceMap;
import medial_axis.MedialAxis;
import medial_axis.MedialAxisGraph;

import basic_objects.OrthogonalLine;
import basic_objects.Vector;
import basic_objects.Vertex;

import junit.framework.TestCase;

public class MirrorSplitTest extends TestCase {
	private LinkedList<Point> horizantolLine;
	private LinkedList<Point> straightLine;
	private DistanceMap distanceMap;
	private DistanceMap distanceMapHorz;
	private MedialAxisGraph testGraph;
	private MirrorSplit mirror;
	protected void setUp() throws Exception {
		super.setUp();
		generateVertical();
		generateHorizantal();
		testGraph=new MedialAxisGraph();
		mirror = new MirrorSplit(testGraph);
		
		straightLine = new LinkedList<Point>();
		straightLine.add(new Point(7, 2));
		straightLine.add(new Point(7, 3));
		straightLine.add(new Point(7, 4));
		straightLine.add(new Point(7, 5));
		straightLine.add(new Point(7, 6));
		straightLine.add(new Point(7, 7));
		straightLine.add(new Point(7, 8));
		straightLine.add(new Point(7, 9));
		straightLine.add(new Point(7, 10));
		straightLine.add(new Point(7, 11));

		horizantolLine = new LinkedList<Point>();
		horizantolLine.add(new Point(2, 7));
		horizantolLine.add(new Point(3, 7));
		horizantolLine.add(new Point(4, 7));
		horizantolLine.add(new Point(5, 7));
		horizantolLine.add(new Point(6, 7));
		horizantolLine.add(new Point(7, 7));
		horizantolLine.add(new Point(8, 7));
		horizantolLine.add(new Point(9, 7));
		horizantolLine.add(new Point(10, 7));
		horizantolLine.add(new Point(11, 7));

	}
	public void generateVertical(){
		distanceMap = new DistanceMap(15, 15);
		for (int i = 0; i < distanceMap.getWidth(); i++) {
			for (int j = 0; j < distanceMap.getHeight(); j++) {
				if (i < 4 || i > 10) {
					distanceMap.setDistanceFormEdge(new Point(i, j), 0);
				} else {
					distanceMap.setDistanceFormEdge(new Point(i, j), 5);
				}
			}
		}
	}
	public void generateHorizantal(){
		distanceMapHorz = new DistanceMap(15, 15);
		for (int i = 0; i < distanceMapHorz.getWidth(); i++) {
			for (int j = 0; j < distanceMapHorz.getHeight(); j++) {
				if (j < 4 || j > 10) {
					distanceMapHorz.setDistanceFormEdge(new Point(i, j), 0);
				} else {
					distanceMapHorz.setDistanceFormEdge(new Point(i, j), 5);
				}
			}
		}

	}
	public void testGetShortestDistance() {

		MedialAxis tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMap);
		testGraph.buildGraph(this.straightLine, distanceMap);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMap);
		testGraph.setChromoWidth(8);
		Rectangle bounds = new Rectangle(0, 0, 15, 15);
		OrthogonalLine tempOrtho = new OrthogonalLine();
		try {
			tempOrtho = mirror.getShortestDistance(bounds, new Point(-1, -1), testGraph
					.getAxisGraph().get(3).getPoint(), testGraph.getChromoWidth(), distanceMap);
		} catch (Exception e) {
			System.out.println(e);
		}

		assertEquals(tempOrtho.getlength(), 8.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(3, 5));
		assertEquals(tempOrtho.getLowerPoint(), new Point(11, 5));

		tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.horizantolLine, distanceMapHorz);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph.setChromoWidth(8);
		bounds = new Rectangle(0, 0, 15, 15);
		mirror = new MirrorSplit(testGraph);
		tempOrtho = new OrthogonalLine();
		try {
			tempOrtho = mirror.getShortestDistance(bounds, new Point(-1, -1), testGraph
					.getAxisGraph().get(3).getPoint(), testGraph.getChromoWidth(), distanceMapHorz);
		} catch (Exception e) {
			System.out.println(e);
		}

		assertEquals(tempOrtho.getlength(), 8.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(5, 3));
		assertEquals(tempOrtho.getLowerPoint(), new Point(5, 11));

		tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.horizantolLine, distanceMapHorz);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMapHorz);
		bounds = new Rectangle(0, 0, 15, 15);
		testGraph.setChromoWidth(10);
		mirror = new MirrorSplit(testGraph);
		tempOrtho = new OrthogonalLine();
		assertEquals(distanceMapHorz.getDistanceFromEdge(new Point(5, 3)), 0);
		assertEquals(testGraph.getDistanceMap().getDistanceFromEdge(new Point(5, 3)), 0);
		tempOrtho = mirror.getShortestDistance(bounds, new Point(-1, -1), new Point(5, 5),
				testGraph.getChromoWidth(), distanceMapHorz);
		assertEquals(tempOrtho.getlength(), 8.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(5, 3));
		assertEquals(tempOrtho.getLowerPoint(), new Point(5, 11));

	}
	public void testgetPathOrtho() {

		MedialAxis tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMap);
		testGraph.buildGraph(this.straightLine, distanceMap);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMap);
		testGraph.setChromoWidth(8);
		Rectangle bounds = new Rectangle(0, 0, 15, 15);
		OrthogonalLine tempOrtho = new OrthogonalLine();
		try {
			tempOrtho = mirror.getPathOrtho(bounds, new Point(7,2), testGraph
					.getAxisGraph().get(3).getPoint(), testGraph.getChromoWidth(), distanceMap);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		assertTrue(tempOrtho.isTwoLines());
		assertEquals(tempOrtho.getlength(), 8.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(3, 5));
		assertEquals(tempOrtho.getLowerPoint(), new Point(11, 5));

		tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.horizantolLine, distanceMapHorz);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph.setChromoWidth(8);
		bounds = new Rectangle(0, 0, 15, 15);
		mirror = new MirrorSplit(testGraph);
		tempOrtho = new OrthogonalLine();
		try {
			tempOrtho = mirror.getPathOrtho(bounds, new Point(2, 7), testGraph
					.getAxisGraph().get(3).getPoint(), testGraph.getChromoWidth(), distanceMapHorz);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		assertTrue(tempOrtho.isTwoLines());
		assertEquals(tempOrtho.getlength(), 8.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(5, 11));
		assertEquals(tempOrtho.getLowerPoint(), new Point(5, 3));

		tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.horizantolLine, distanceMapHorz);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMapHorz);
		bounds = new Rectangle(0, 0, 15, 15);
		testGraph.setChromoWidth(10);
		mirror = new MirrorSplit(testGraph);
		tempOrtho = new OrthogonalLine();
		assertEquals(distanceMapHorz.getDistanceFromEdge(new Point(5, 3)), 0);
		assertEquals(testGraph.getDistanceMap().getDistanceFromEdge(new Point(5, 3)), 0);
		tempOrtho = mirror.getPathOrtho(bounds, new Point(2, 5), new Point(5, 5),
				testGraph.getChromoWidth(), distanceMapHorz);

		assertTrue(tempOrtho.isTwoLines());
		assertEquals(tempOrtho.getlength(), 8.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(5, 11));
		assertEquals(tempOrtho.getLowerPoint(), new Point(5, 3));
		
		
		
		
		//corner missing
		
		tempAxis = new MedialAxis();
		DistanceMap cornerMissing=distanceMapHorz;
		for(int i=7;i<15;i++){
			for(int j=3;j<6;j++){
				cornerMissing.setDistanceFormEdge(new Point(i,j), 0);
			}
		}
		tempAxis.setDistanceMap(cornerMissing);
		testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.horizantolLine, cornerMissing);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(cornerMissing);
		bounds = new Rectangle(0, 0, 15, 15);
		testGraph.setChromoWidth(10);
		mirror = new MirrorSplit(testGraph);
		tempOrtho = new OrthogonalLine();
		assertEquals(cornerMissing.getDistanceFromEdge(new Point(5, 3)), 0);
		assertEquals(testGraph.getDistanceMap().getDistanceFromEdge(new Point(5, 3)), 0);
		tempOrtho = mirror.getPathOrtho(bounds, new Point(2, 7), new Point(5, 7),
				testGraph.getChromoWidth(), cornerMissing);

		assertTrue(tempOrtho.isTwoLines());
		assertTrue(tempOrtho.getlength()< 7&&tempOrtho.getlength()>6);
		assertEquals(tempOrtho.getUpperPoint(), new Point(5, 11));
		assertEquals(tempOrtho.getLowerPoint(), new Point(7, 5));

		
		//test only one side touches distance map zero
		generateHorizantal();
		tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.horizantolLine, distanceMapHorz);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMapHorz);
		bounds = new Rectangle(0, 0, 15, 15);
		testGraph.setChromoWidth(10);
		mirror = new MirrorSplit(testGraph);
		tempOrtho = new OrthogonalLine();
		assertEquals(distanceMapHorz.getDistanceFromEdge(new Point(5, 3)), 0);
		assertEquals(testGraph.getDistanceMap().getDistanceFromEdge(new Point(5, 3)), 0);
		tempOrtho = mirror.getPathOrtho(bounds, new Point(2, 5), new Point(5, 5),
				4, distanceMapHorz);
		
		assertFalse(tempOrtho.isTwoLines());
		assertEquals(tempOrtho.getlength(), 4.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(5, 7));
		assertEquals(tempOrtho.getLowerPoint(), new Point(5, 3));

		//test only one side oppisite previos touches distance map zero
		generateHorizantal();
		tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.horizantolLine, distanceMapHorz);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMapHorz);
		bounds = new Rectangle(0, 0, 15, 15);
		testGraph.setChromoWidth(10);
		mirror = new MirrorSplit(testGraph);
		tempOrtho = new OrthogonalLine();
		assertEquals(distanceMapHorz.getDistanceFromEdge(new Point(5, 3)), 0);
		assertEquals(testGraph.getDistanceMap().getDistanceFromEdge(new Point(5, 3)), 0);
		tempOrtho = mirror.getPathOrtho(bounds, new Point(2, 8), new Point(5, 8),
				3, distanceMapHorz);
		
		assertFalse(tempOrtho.isTwoLines());
		assertEquals(tempOrtho.getlength(), 6.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(5, 11));
		assertEquals(tempOrtho.getLowerPoint(), new Point(5, 5));


	}
	public void testGetAllDistances() {
		MedialAxis tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMap);
		testGraph.buildGraph(this.straightLine, distanceMap);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMap);
		testGraph.setChromoWidth(8);
		Rectangle bounds = new Rectangle(0, 0, 15, 15);
		LinkedList<OrthogonalLine> tempOrtho = new LinkedList<OrthogonalLine>();
		Vertex startVertex = testGraph.getAxisGraph().get(0);

		tempOrtho = mirror.getAllDistances(bounds, testGraph.getAxisGraph(), 9, distanceMap);

		for (int i = 0; i < this.straightLine.size(); i++) {
			assertEquals(new Point(3, tempOrtho.get(i).getCenterPoint().y), tempOrtho
					.get(i).getUpperPoint());
			assertEquals(new Point(11, tempOrtho.get(i).getCenterPoint().y), tempOrtho
					.get(i).getLowerPoint());
		}

		tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.horizantolLine, distanceMapHorz);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMapHorz);
		testGraph.setChromoWidth(8);
		bounds = new Rectangle(0, 0, 15, 15);
		mirror = new MirrorSplit(testGraph);
		tempOrtho = new LinkedList<OrthogonalLine>();
		startVertex = testGraph.getAxisGraph().get(0);

		tempOrtho = mirror.getAllDistances(bounds, testGraph.getAxisGraph(), 9, distanceMapHorz);

		for (int i = 0; i < this.horizantolLine.size(); i++) {
			assertEquals(new Point(tempOrtho.get(i).getCenterPoint().x, 3), tempOrtho
					.get(i).getUpperPoint());
			assertEquals(new Point(tempOrtho.get(i).getCenterPoint().x, 11), tempOrtho
					.get(i).getLowerPoint());
		}
		
		
		
		

	}
	public void testIsProjectionEnd(){
		//test only one side oppisite previos touches distance map zero
		generateHorizantal();
		Rectangle bounds = new Rectangle(0, 0, 15, 15);
		assertFalse(mirror.isProjectionEnd(bounds, distanceMapHorz,new Vertex(new Point(5,7),5),new Vector(1,0)));
		assertTrue(mirror.isProjectionEnd(bounds, distanceMapHorz,new Vertex(new Point(5,7),5),new Vector(0,1)));
		assertTrue(mirror.isProjectionEnd(bounds, distanceMapHorz,new Vertex(new Point(3,7),5),new Vector(-1,0)));
		assertFalse(mirror.isProjectionEnd(bounds, distanceMapHorz,new Vertex(new Point(5,9),5),new Vector(0,-1)));
		assertTrue(mirror.isProjectionEnd(bounds, distanceMapHorz,new Vertex(new Point(5,7),5),new Vector(1,1)));
		assertTrue(mirror.isProjectionEnd(bounds, distanceMapHorz,new Vertex(new Point(5,7),5),new Vector(-1,-1)));
		
	}
	public void testGetNextPoint(){
		
		assertEquals(new Point(6,5), (mirror.getNextPoint(new Point(5,5), new Point(5,3), new Point(5,7), new Vector(1,0))).getPoint());
		assertEquals(new Point(7,5), (mirror.getNextPoint(new Point(5,5), new Point(5,3), new Point(5,7), new Vector(1,0))).getChildren().get(0).getPoint());

		assertEquals(new Point(6,6), (mirror.getNextPoint(new Point(5,5), new Point(5,3), new Point(5,7), new Vector(1,1))).getPoint());
		assertEquals(new Point(7,7), (mirror.getNextPoint(new Point(5,5), new Point(5,3), new Point(5,7), new Vector(1,1))).getChildren().get(0).getPoint());
		
		assertEquals(new Point(3,6), (mirror.getNextPoint(new Point(5,5), new Point(5,3), new Point(5,7), new Vector(-2,1))).getPoint());
		assertEquals(new Point(1,7), (mirror.getNextPoint(new Point(5,5), new Point(5,3), new Point(5,7), new Vector(-2,1))).getChildren().get(0).getPoint());

		assertEquals(new Point(6,8), (mirror.getNextPoint(new Point(5,8), new Point(5,4), new Point(5,10), new Vector(1,1))).getPoint());
		assertEquals(new Point(7,9), (mirror.getNextPoint(new Point(5,8), new Point(5,4), new Point(5,10), new Vector(1,1))).getChildren().get(0).getPoint());

	}
	public void testGetNextVector(){
		OrthogonalLine tempOrtho = new OrthogonalLine();
		Vertex currPoint;
		Vector vect;
		
		tempOrtho.setUpperPoint(new Point(3,7));
		tempOrtho.setLowerPoint(new Point(7,3));
		tempOrtho.setCenterPoint(new Point(5,5));
		currPoint=new Vertex(new Point(5,5),0);
		currPoint.addChild(new Vertex(new Point(7,7),0));
		vect=mirror.getNextVector(tempOrtho, currPoint);
		assertTrue(vect.x<2&&vect.x>1);
		assertTrue(vect.y<2&&vect.y>1);
		
		tempOrtho.setUpperPoint(new Point(3,7));
		tempOrtho.setLowerPoint(new Point(7,3));
		tempOrtho.setCenterPoint(new Point(5,5));
		currPoint=new Vertex(new Point(5,5),0);
		currPoint.addChild(new Vertex(new Point(3,3),0));
		vect=mirror.getNextVector(tempOrtho, currPoint);
		assertTrue(vect.x>-2&&vect.x<-1);
		assertTrue(vect.y>-2&&vect.y<-1);

		tempOrtho.setUpperPoint(new Point(5,8));
		tempOrtho.setLowerPoint(new Point(5,2));
		tempOrtho.setCenterPoint(new Point(5,5));
		currPoint=new Vertex(new Point(5,5),0);
		currPoint.addChild(new Vertex(new Point(3,3),0));
		vect=mirror.getNextVector(tempOrtho, currPoint);
		assertEquals(vect.y<.002,vect.y>-0.002);
		assertTrue(vect.x>-2&&vect.x<-1);


	}
	public void testRecenterForUpper(){

		OrthogonalLine tempOrtho = new OrthogonalLine();
		Point newPoint;
		
		tempOrtho.setUpperPoint(new Point(3,7));
		tempOrtho.setLowerPoint(new Point(5,0));
		tempOrtho.setCenterPoint(new Point(5,5));
		
		newPoint=mirror.recenterForUpper(tempOrtho, 8);
		assertEquals(new Point(5,8),newPoint);


	}
	public void testRecenterForLower(){
		OrthogonalLine tempOrtho = new OrthogonalLine();
		Point newPoint;
		
		tempOrtho.setUpperPoint(new Point(3,7));
		tempOrtho.setLowerPoint(new Point(5,0));
		tempOrtho.setCenterPoint(new Point(5,5));
		
		newPoint=mirror.recenterForLower(tempOrtho, 8);
		assertEquals(new Point(9,1),newPoint);

	}
	public void testAngleBetween(){
		OrthogonalLine tempOrtho = new OrthogonalLine();
		
		tempOrtho.setUpperPoint(new Point(9,1));
		tempOrtho.setLowerPoint(new Point(5,0));
		tempOrtho.setCenterPoint(new Point(5,5));
		
		double angle=mirror.angleBetween(tempOrtho);
		assertTrue(angle>.785&&angle<.786);
		
	}

}
