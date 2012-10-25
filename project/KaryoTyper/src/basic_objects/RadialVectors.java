package basic_objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class RadialVectors {

	private final double CIRC = 2 * Math.PI;

	private ArrayList<Vector> vectorList;
	private Point centerPoint;
	private double theta;

	public RadialVectors(Point centerPoint, int numVectors, double distance) {
		this.centerPoint = centerPoint;
		vectorList = new ArrayList<Vector>(numVectors);
		generateVectors(numVectors, distance);
	}

	private void generateVectors(int numVectors, double distance) {
		if (numVectors < 0) {
			System.out.println("Number of vectors must be positive.");
			System.exit(1);
		}

		theta = CIRC / numVectors;

		double aggregateAngle = 0;

		Vector firstVector = new Vector(distance, 0);
		vectorList.add(firstVector);

		while (aggregateAngle < CIRC) {
			aggregateAngle += theta;
			vectorList.add(Vector.rotateVector(firstVector, aggregateAngle));
		}
	}

	/**
	 * @return the vectors
	 */
	public ArrayList<Vector> getVectors() {
		return vectorList;
	}

	/**
	 * Returns the list of vectors as a list of points on the image.
	 * 
	 * @return The list of vectors as a list of points on the image.
	 */
	public LinkedList<Point> getVectorsAsPointsOnImage() {
		LinkedList<Point> points = new LinkedList<Point>();

		for (Vector v : vectorList) {
			int pointOnImageX = (int) (v.x + centerPoint.x);
			int pointOnImageY = (int) (v.y + centerPoint.y);

			points.add(new Point(pointOnImageX, pointOnImageY));
		}

		return points;
	}

	/**
	 * @return the centerPoint
	 */
	public Point getCenterPoint() {
		return centerPoint;
	}
	
	public ArrayList<Point> getRange(Point endPoint, double angle) {
		ArrayList<Point> pointsInRange = new ArrayList<Point>();
		pointsInRange.add(endPoint);
		
		Vector vector = new Vector(endPoint.x, endPoint.y);
		
		//TODO: Need getRange based on angle and endPoint
		
		
		return pointsInRange;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public Point getOpposite(Point point) {
		Point oppositePoint = new Point(0,0);
		//TODO: Finish method
		return oppositePoint;
	}

	
	//TODO: Need to get multiples of unit vectors within range.
}
