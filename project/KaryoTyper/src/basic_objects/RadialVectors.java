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
	public ArrayList<Point> getVectorsAsPointsOnImage() {
		ArrayList<Point> points = new ArrayList<Point>(vectorList.size());

		for (Vector v : vectorList) {
			points.add(getVectorAsPointOnImage(v));
		}

		return points;
	}

	/**
	 * @return the centerPoint
	 */
	public Point getCenterPoint() {
		return centerPoint;
	}

	public ArrayList<Point> getRange(Point endPoint, double angle, int numPoints) {
		ArrayList<Point> pointsInRange = new ArrayList<Point>(numPoints);
		int middle = numPoints / 2;
		pointsInRange.add(middle, endPoint);

		double localTheta = angle / numPoints;

		Vector vector = new Vector(endPoint.x, endPoint.y);

		for (int i = 1; i < middle; i++) {
			Vector vectorP = Vector.rotateVector(vector, localTheta * i);
			pointsInRange.add(middle + i, getVectorAsPointOnImage(vectorP));

			Vector vectorN = Vector.rotateVector(vector, localTheta * i * -1);
			pointsInRange.add(middle - i, getVectorAsPointOnImage(vectorN));
		}

		return pointsInRange;
	}

	private Point getVectorAsPointOnImage(Vector vector) {
		int xOnImage = (int) Math.round(vector.x + centerPoint.x);
		int yOnImage = (int) Math.round(vector.y + centerPoint.y);
		return new Point(xOnImage, yOnImage);
	}

	public double getTheta() {
		return theta;
	}

	public Point getOpposite(Point point) {
		Point oppositePoint = new Point(0, 0);
		// TODO: Finish method
		return oppositePoint;
	}

	// TODO: Need to get multiples of unit vectors within range.
	// TODO: Index based on angle multiple. Make method to get Vector based on angle multiple.
}
