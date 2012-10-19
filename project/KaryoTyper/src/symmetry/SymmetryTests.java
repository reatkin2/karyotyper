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
		// test.rectangleColorTest();
		// test.crestColorTest();
		// test.realColorTest();
		test.tangentTest();

	}

	public void init() {

	}

	public void rectangleColorTest() {
		short[][] imgValues = new short[10][10];
		ArrayList<Point> axisPoints = new ArrayList<Point>();

		for (int i = 0; i < imgValues.length; i++) {
			for (int j = 0; j < imgValues[0].length; j++) {
				imgValues[i][j] = 0;
			}
		}

		for (int i = 2; i < 8; i++) {
			// imgValues[i][4] = 1;
			imgValues[i][5] = 1;
			imgValues[i][6] = 1;
			imgValues[i][7] = 1;
			// imgValues[i][8] = 1;
			axisPoints.add(new Point(i, 6));
		}

		// imgValues[2][5] = 0;

		Image img = new Image(imgValues);
		Axis axis = new Axis(axisPoints, img, 3, 5);

		System.out.println(img.toString());
		// boolean isSymmetric = DetectSymmetry.detectColorSymmetry(img, axis,
		// 3, .99);
		// System.out.println("Is the shape symmetric? " + isSymmetric);
		//
		// DetectSymmetry.detectWidthSymmetry(img, axis, .99);
	}

	public void crestColorTest() {
		short[][] imgValues = new short[10][10];
		ArrayList<Point> axisPoints = new ArrayList<Point>();

		for (int i = 0; i < imgValues.length; i++) {
			for (int j = 0; j < imgValues[0].length; j++) {
				imgValues[i][j] = 0;
			}
		}

		// Create axis points
		imgValues[7][0] = 1;
		imgValues[7][1] = 1;
		axisPoints.add(new Point(7, 1));
		imgValues[7][2] = 1;
		imgValues[6][0] = 1;
		imgValues[6][1] = 1;
		axisPoints.add(new Point(6, 1));
		imgValues[6][2] = 1;
		imgValues[5][0] = 1;
		imgValues[5][1] = 1;
		axisPoints.add(new Point(5, 1));
		imgValues[5][2] = 1;
		imgValues[4][1] = 1;
		imgValues[3][2] = 1;
		imgValues[4][2] = 1;
		axisPoints.add(new Point(4, 2));
		imgValues[3][3] = 1;
		imgValues[4][3] = 1;
		axisPoints.add(new Point(4, 3));
		imgValues[5][3] = 1;
		imgValues[3][4] = 1;
		imgValues[4][4] = 1;
		axisPoints.add(new Point(4, 4));
		imgValues[5][4] = 1;
		imgValues[4][5] = 1;
		imgValues[5][5] = 1;
		axisPoints.add(new Point(5, 5));
		imgValues[5][6] = 1;
		imgValues[6][4] = 1;
		imgValues[6][5] = 1;
		axisPoints.add(new Point(6, 5));
		imgValues[6][6] = 1;
		imgValues[7][4] = 1;
		imgValues[7][5] = 1;
		axisPoints.add(new Point(7, 5));
		imgValues[7][6] = 1;

		Image img = new Image(imgValues);
		Axis axis = new Axis(axisPoints, img, 3, 5);

		System.out.println(img.toString());
		// boolean isSymmetric = DetectSymmetry.detectColorSymmetry(img, axis,
		// 3, .99);
		// System.out.println("Is the shape symmetric? " + isSymmetric);
		//
		// DetectSymmetry.detectWidthSymmetry(img, axis, .99);
	}

	public void realColorTest() {
		init();

		// shape.getSkeleton(getter.getImg());
		//
		// getter.writeTargetImage(shape, shape.getSkeltonPoints(),
		// Color.GREEN);

		// boolean[][] img = image.getShape();
		// Image imageObject = new Image(img);
		// MedialAxisGraph axis = new MedialAxisGraph(image.getSkeltonPoints());
		// for (Vertex v : axis.getAxisGraph()) {
		// try {
		// v.calculateOrthogonalLine(5, 7);
		// } catch (DistanceException e) {
		// System.out.println(e.getMessage());
		// e.printStackTrace();
		// }
		// }
		// DetectSymmetry.detectColorSymmetry(imageObject, axis, 20, 0.99);
	}

	/**
	 * Tests the tangent line calculator.
	 */
	public void tangentTest() {
		String imagePath = "C:\\Users\\Robert\\Desktop\\SchoolWork\\CSC492\\repo"
				+ "\\project\\KaryoTyper\\testImages\\testImage.png";
		int tanLineLengths = 10;

		ChromosomeCluster cluster = TestShape.getCluster(imagePath);
		cluster.createSkeleton(TestShape.getGeneticSlideImage());
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
		// TODO: Causes IndexOutofBounds
	}
}
