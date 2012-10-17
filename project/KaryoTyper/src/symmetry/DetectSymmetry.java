package symmetry;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import testing.TestShape;

import basic_objects.Vertex;

import chromosome.ChromosomeCluster;
import chromosome.ChromosomeList;
import chromosome.GeneticSlideImage;

import medial_axis.MedialAxisGraph;

public class DetectSymmetry {

//	/**
//	 * Detects if the chromosome is symmetric about the medial axis, given that the
//	 * medial axis has been defined.  The method checks for symmetry of the
//	 * chromosome.  Pixel colors to one side of the medial axis are compared to corresponding
//	 * pixels on the opposite side of the medial axis.  If the percent of pixel colors that 
//	 * match exceed the specified threshold, then image of the chromosome is said to be
//	 * symmetric and no overlap is present.  If the threshold is not exceeded then the image
//	 * is not symmetric and there is an overlap.
//	 * 
//	 * @param img
//	 * @param axis
//	 * @return
//	 */
//	public static boolean detectColorSymmetry(Image img, Axis axis, int widthLimit, double threshold) {
//		int[][] leftMatrix = new int[axis.getLength()][widthLimit / 2 + 1];
//		int[][] rightMatrix = new int[axis.getLength()][widthLimit / 2 + 1];
//		int rowCursor = 0;
//		int numChecks = 0;
//		int numMatches = 0;
//
//		Collection<Point> points = axis.getPoints();
//
//		for (Point p : points) {
//			/*
//			 * To get an approximation to the tangent line at point p the secant line passing through
//			 * p and one of the next closest points, q.
//			 */
//			Point q = axis.getPrevious(p);
//			if (q == null) {
//				q = axis.getNext(p);
//			}
////			try {
////				q = axis.getPrevious(p);
////			} catch (Exception e) {
////				q = axis.getNext(p);
////			}
//			int denominator = p.y - q.y;
//			boolean noSlope;
//			int orthoSlope = 0;
//			if (denominator == 0) {
//				noSlope = true;
//			} else {
//				noSlope = false;
//				orthoSlope = (q.x - p.x) / denominator;
//			}
//			for (int i = 0; i < widthLimit / 2 + 1; i++) {
//				if (noSlope) {
//					leftMatrix[rowCursor][i] = img.getColorAt(p.x, p.y - i);
//					rightMatrix[rowCursor][i] = img.getColorAt(p.x, p.y + i);
//				} else {
//					leftMatrix[rowCursor][i] = img.getColorAt(p.x - i,
//							orthoSlope * (-1 * i) + p.y);
//					rightMatrix[rowCursor][i] = img.getColorAt(p.x + i,
//							orthoSlope * i + p.y);
//				}
//				numChecks++;
//				if (leftMatrix[rowCursor][i] == rightMatrix[rowCursor][i]) {
//					numMatches++;
//				}
//			}
//			rowCursor++;
//		}
//		
//		double matchRating = (double)numMatches / (double)numChecks;
//		
//		//Testing code
//		System.out.println(leftMatrix.length + " " + leftMatrix[0].length);
//		System.out.println("Left Matrix:");
//		for (int i = 0; i < leftMatrix.length; i++) {
//			for (int j = 0; j < leftMatrix[0].length; j++) {
//				System.out.print(leftMatrix[i][j]);
//			}
//			System.out.println();
//		}
//		System.out.println("Right Matrix");
//		for (int i = 0; i < rightMatrix.length; i++) {
//			for (int j = 0; j < rightMatrix[0].length; j++) {
//				System.out.print(rightMatrix[i][j]);
//			}
//			System.out.println();
//		}
//		
//		System.out.println("Percent match: " + matchRating);
//		System.out.println("Threshold: " + threshold);
//		//End Testing code
//		
//		if (matchRating >= threshold) {
//			return true;
//		}
//		return false;
//	}
	
//	public static boolean detectWidthSymmetry(Image image, Axis axis, double threshold) {
//		Collection<Point> axisPoints = axis.getPoints();
//		double[] leftWidth = new double[axis.getLength()];
//		double[] rightWidth = new double[axis.getLength()];
//		//For testing
//		ArrayList<Point> leftEdge = new ArrayList<Point>(axis.getLength());
//		ArrayList<Point> rightEdge = new ArrayList<Point>(axis.getLength());
//		
//		//End testing
//		int arrayInc = 0;
//		
//		for (Point p : axisPoints) {
//			/*
//			 * To get an approximation to the tangent line at point p the secant line passing through
//			 * p and one of the next closest points, q.
//			 */
//			Point q = axis.getNearbyPoint(p);
////			Point q = axis.getPrevious(p);
////			if (q == null) {
////				q = axis.getNext(p);
////			}
////			try {
////				q = axis.getPrevious(p);
////			} catch (ArrayIndexOutOfBoundsException e) {
////				q = axis.getNext(p);
////			}
//			int denominator = p.y - q.y;
//			boolean noSlope;
//			int orthoSlope = 0;
//			if (denominator == 0) {
//				noSlope = true;
//			} else {
//				noSlope = false;
//				orthoSlope = (q.x - p.x) / denominator;
//			}
//			
//			//Check points along the orthogonal line until a point is reached that is clearly a part
//			//of the background.
//			int incrementer = 0;
////			Point edge = null;
//			int xCoord = 0;
//			int yCoord = 0;
//			int color = 0;
//			do {
//				incrementer++;
//				xCoord = p.x - incrementer;
//				yCoord = p.y - orthoSlope * incrementer;
//				try {
//					color = image.getColorAt(xCoord, yCoord);
//				} catch (ArrayIndexOutOfBoundsException e) {
//					break;
//				}
//			} while(color != 0); // When color == 0 then the background has been reached 
//								 // and the previous point is the edge
////			xCoord = xCoord + incrementer;
////			yCoord = yCoord + orthoSlope * incrementer;
//			//Testing
//			leftEdge.add(new Point(xCoord, yCoord));
//			//End testing
////			edge = new Point(xCoord, yCoord);
//			leftWidth[arrayInc] = Point.distance(p.x, p.y, xCoord, yCoord);
//			
//			//Check points along the orthogonal line until a point is reached that is clearly a part
//			//of the background.
//			incrementer = 0;
////			Point edge = null;
//			xCoord = 0;
//			yCoord = 0;
//			color = 0;
//			do {
//				incrementer++;
//				xCoord = p.x + incrementer;
//				yCoord = p.y + orthoSlope * incrementer;
//				try {
//					color = image.getColorAt(xCoord, yCoord);
//				} catch (ArrayIndexOutOfBoundsException e) {
//					break;
//				}
//			} while(color != 0); // When color == 0 then the background has been reached 
//								 // and the previous point is the edge
////			xCoord = xCoord - incrementer;
////			yCoord = yCoord - orthoSlope * incrementer;
//			//Testing
//			rightEdge.add(new Point(xCoord, yCoord));
//			//End testing
////			edge = new Point(xCoord, yCoord);
//			rightWidth[arrayInc] = Point.distance(p.x, p.y, xCoord, yCoord);
//		}
//		
//		double[] diffArray = new double[axisPoints.size()];
//		double notMatchSum = 0;
//		double leftSum = 0;
//		double rightSum = 0;
//		for (int i = 0; i < axisPoints.size(); i++) {
//			diffArray[i] = leftWidth[i] - rightWidth[i];
//			notMatchSum += diffArray[i];
//			leftSum += leftWidth[i];
//			rightSum += rightWidth[i];
//		}
//		double matchLeftRating = (leftSum - notMatchSum)/leftSum;
//		double matchRightRating = (rightSum - notMatchSum)/rightSum;
//		//Testing code
//		System.out.println("Left edge points:");
//		for (int i = 0; i < axisPoints.size(); i++) {
//			System.out.println("(" + leftEdge.get(i).x + ", " + leftEdge.get(i).y + ")");
//		}
//		
//		System.out.println("Right edge points:");
//		for (int i = 0; i < axisPoints.size(); i++) {
//			System.out.println("(" + rightEdge.get(i).x + ", " + rightEdge.get(i).y + ")");
//		}
//		
//		System.out.println("L  R  D");
//		for (int i = 0; i < axisPoints.size(); i++) {
//			System.out.println(leftWidth[i] + "  " + rightWidth[i] + "  " + diffArray[i]);
//		}
//		System.out.println("Percent match left: " + matchLeftRating);
//		System.out.println("Percent match right: " + matchRightRating);
//		//End testing code
//		return matchLeftRating > threshold && matchRightRating > threshold;
//		
//		//TODO:  Testing is confusing, maybe use debugger.
//	}
	
	/**
	 * The Medial Axis Graph must be built before passing in, and tangents/orthogonals
	 * be calculated.
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
		
		HashMap<Vertex, LinkedList<Point>> vertexToOrthoMap = new HashMap<Vertex, LinkedList<Point>>(50);
		
		for (Vertex v : axisGraph.getAxisGraph()) {
			try {
				LinkedList<Point> orthoPoints = v.orthogonalLine(v.getDistanceFromEdge() * 2);
				vertexToOrthoMap.put(v, orthoPoints);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		Vertex vertex = axisGraph.getAxisGraph().getFirst();
//		
//		int sectionWidth = vertex.getDistanceFromEdge();
//		LinkedList<Point> orthoPoints = null;
//		try {
//			orthoPoints = vertex.orthogonalLine(sectionWidth);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
		
		ArrayList<Color> colorAtPoints = new ArrayList<Color>();
		
		for (Point p : vertexToOrthoMap.get(vertex)) {
			colorAtPoints.add(image.getColorAt(p.x, p.y));
		}
		
		//Debug code
		System.out.println("(R, G, B)");
		Color vertColor = image.getColorAt(vertex.getPoint().x, vertex.getPoint().y);
		System.out.println("Vertex color: (" + vertColor.getRed() + ", " + vertColor.getGreen() + ", " + vertColor.getBlue() + "); ");
		for (Color color : colorAtPoints) {
			System.out.print("(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + "); ");
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
		chromoList.writeTargetImage(".\\shapeData\\testing\\", cluster, vertexToOrthoMap.get(vertex), Color.orange);
		//End debug
		
		return false;
		
	}
}
