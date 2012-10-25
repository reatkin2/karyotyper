package symmetry;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import testing.TestShape;

import basic_objects.Vertex;

import chromosome.ChromosomeCluster;
import chromosome.ChromosomeList;
import chromosome.GeneticSlideImage;

import medial_axis.MedialAxisGraph;

public class DetectSymmetry {

	/**
	 * The Medial Axis Graph must be built before passing in, and tangents/orthogonals be
	 * calculated.
	 * 
	 * @param axisGraph
	 * @param image
	 * @return
	 */
	public static boolean detectWidthSymmetry(MedialAxisGraph axisGraph, GeneticSlideImage image) {
		try {
			axisGraph.generateOrthogonals(3, 5);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		HashMap<Vertex, LinkedList<Point>> vertexToOrthoMap = new HashMap<Vertex, LinkedList<Point>>(
				50);

		for (Vertex v : axisGraph.getAxisGraph()) {
			try {
				LinkedList<Point> orthoPoints = v.orthogonalLine(v.getDistanceFromEdge() * 2);
				vertexToOrthoMap.put(v, orthoPoints);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		//
		// int sectionWidth = vertex.getDistanceFromEdge();
		// LinkedList<Point> orthoPoints = null;
		// try {
		// orthoPoints = vertex.orthogonalLine(sectionWidth);
		// } catch (Exception e) {
		// System.out.println(e.getMessage());
		// e.printStackTrace();
		// }

		HashMap<Vertex, ArrayList<Color>> vertexToColorsMap = new HashMap<Vertex, ArrayList<Color>>(
				vertexToOrthoMap.size());
		
		for (Vertex v : axisGraph.getAxisGraph()) {
			ArrayList<Color> colorAtPoints = new ArrayList<Color>();

			for (Point p : vertexToOrthoMap.get(v)) {
				colorAtPoints.add(image.getColorAt(p.x, p.y));
			}
			vertexToColorsMap.put(v, colorAtPoints);
		}

//		ArrayList<Color> colorAtPoints = new ArrayList<Color>();
//
//		for (Point p : vertexToOrthoMap.get(vertex)) {
//			colorAtPoints.add(image.getColorAt(p.x, p.y));
//		}

		// Debug code

		Vertex vertex = axisGraph.getAxisGraph().getFirst();
		System.out.println("(R, G, B)");
		Color vertColor = image.getColorAt(vertex.getPoint().x, vertex.getPoint().y);
		System.out.println("Vertex color: (" + vertColor.getRed() + ", " + vertColor.getGreen()
				+ ", " + vertColor.getBlue() + "); ");
		for (Color color : vertexToColorsMap.get(vertex)) {
			System.out.print("(" + color.getRed() + ", " + color.getGreen() + ", "
					+ color.getBlue() + "); ");
		}
		System.out.println();

		String imagePath = ".\\testImages\\testImage.png";
		ChromosomeCluster cluster = TestShape.getCluster(imagePath);

		LinkedList<Point> tanLine = null;
		try {
			tanLine = vertex.tangentLine(vertex.getDistanceFromEdge() * 2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		ChromosomeList chromoList = new ChromosomeList(new LinkedList<ChromosomeCluster>(), image);
		chromoList.writeTargetImage(".\\shapeData\\testing\\", cluster, tanLine, Color.green);
		chromoList.writeTargetImage(".\\shapeData\\testing\\", cluster,
				vertexToOrthoMap.get(vertex), Color.orange);
		// End debug

		return false;

	}
}
