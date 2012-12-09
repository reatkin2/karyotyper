package symmetry;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import basic_objects.Vertex;

import testing.TestShape;

import medial_axis.MedialAxisGraph;

import chromosome.ChromosomeCluster;
import chromosome.ChromosomeList;

public class SymmetryTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SymmetryTests test = new SymmetryTests();
		test.tangentTest();

	}

	/**
	 * Tests the tangent line calculator.
	 */
	public void tangentTest() {
		String imagePath = "C:\\Users\\Robert\\Desktop\\SchoolWork\\CSC492\\repo"
				+ "\\project\\KaryoTyper\\testImages\\testImage1.png";
		int tanLineLengths = 10;

		ChromosomeCluster cluster = TestShape.getCluster(imagePath);
		cluster.createSkeleton();
		cluster.createMedialAxisGraph(TestShape.getGeneticSlideImage());
		LinkedList<Point> axisPoints = cluster.getMedialAxisGraph().getMedialAxisFromGraph();

		// Debug code
		// getter.writeTargetImage(shape, shape.getSkeltonPoints(),
		// Color.GREEN);
		// System.out.println("Reached here");
		// End Debug

		MedialAxisGraph axisGraph = cluster.getMedialAxisGraph();

		// Debug code
		// getter.writeTargetImage(shape, axisGraph.trimMedialAxis(3,
		// axisGraph.getMedialAxis(), shape), Color.green);
		// System.out.println("Reached point 2");
		// End debug

		try {
			axisGraph.generateTangents(5, 7);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		LinkedList<Point> tanLines = new LinkedList<Point>();
		LinkedList<Vertex> vertices = axisGraph.getAxisGraph();

		int counter = 0;
		while (vertices.peek() != null) {
			Vertex v = vertices.poll();
			if (counter % 10 == 0) {
				try {
					tanLines.addAll(v.tangentLine(tanLineLengths));
				} catch (Exception e) {
					System.out.println("Should not have reached here.");
					System.out.println(e);
					e.printStackTrace();
				}
			}
			counter++;
		}

		LinkedList<ChromosomeCluster> clusterList = new LinkedList<ChromosomeCluster>();
		ChromosomeList chromoList = new ChromosomeList(clusterList,
				TestShape.getGeneticSlideImage());

		// Debug code
		for (Point p : tanLines) {
			System.out.println("(" + p.x + ", " + p.y + ")");
		}
		// End debug
		String writePath = "//shapeData//tangentLines//";
		chromoList.writeTargetImage(writePath, cluster, tanLines, Color.GREEN);
	}
}
