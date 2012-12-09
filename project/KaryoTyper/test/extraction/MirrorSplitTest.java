package extraction;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import medial_axis.DistanceMap;
import medial_axis.MedialAxis;
import medial_axis.MedialAxisGraph;

import basic_objects.OrthogonalLine;
import basic_objects.Vertex;

import junit.framework.TestCase;

public class MirrorSplitTest extends TestCase {
	private LinkedList<Point> horizantolLine;
	private LinkedList<Point> straightLine;
	private DistanceMap distanceMap;
	private DistanceMap distanceMapHorz;

	protected void setUp() throws Exception {
		super.setUp();
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

	public void testGetShortestDistance() {

		MedialAxis tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMap);
		MedialAxisGraph testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.straightLine, distanceMap);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMap);
		testGraph.setChromoWidth(8);
		Rectangle bounds = new Rectangle(0, 0, 15, 15);
		MirrorSplit mirror = new MirrorSplit(testGraph);
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
		// try{
		tempOrtho = mirror.getShortestDistance(bounds, new Point(-1, -1), new Point(5, 5),
				testGraph.getChromoWidth(), distanceMapHorz);
		// }catch(Exception e){
		// System.out.println(e);
		// }
		// TODO(aamcknig): find out why this is returning 5,-1 rather than 5,3
		assertEquals(tempOrtho.getlength(), 8.0);
		assertEquals(tempOrtho.getUpperPoint(), new Point(5, 3));
		assertEquals(tempOrtho.getLowerPoint(), new Point(5, 11));

	}

	public void testGetAllDistances() {
		MedialAxis tempAxis = new MedialAxis();
		tempAxis.setDistanceMap(distanceMap);
		MedialAxisGraph testGraph = new MedialAxisGraph(tempAxis);
		testGraph.buildGraph(this.straightLine, distanceMap);
		testGraph.setMedialAxis(testGraph.getAxisGraph());
		tempAxis.setDistanceMap(distanceMap);
		testGraph.setChromoWidth(8);
		Rectangle bounds = new Rectangle(0, 0, 15, 15);
		MirrorSplit mirror = new MirrorSplit(testGraph);
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

}
