package basic_objects;

import java.awt.Point;
import java.util.ArrayList;

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

		while (aggregateAngle < CIRC) {
			vectorList.add(Vector.rotateVector(firstVector, aggregateAngle));
			aggregateAngle += theta;
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
	
	private Vector getPointAsVector(Point point) {
		double xComp = point.x - centerPoint.x;
		double yComp = point.y - centerPoint.y;
		return new Vector(xComp, yComp);
	}

	public double getTheta() {
		return theta;
	}

	/**
	 * Returns the point reflected across the center point.
	 * @param point Point on radial edge to reflect.
	 * @return The point reflected across the center point.
	 */
	public Point getOpposite(Point point) {
		Vector vector = getPointAsVector(point);
		
		//Swap components
		vector.x += vector.y;
		vector.y = vector.x - vector.y;
		vector.x -= vector.y;
		
		// Multiply by -1
		vector.x *= -1;
		vector.y *= -1;
		
		//return as Point on image
		return getVectorAsPointOnImage(vector);
	}
	
	public Point getPointAtIndex(int index) {
		Point point = getVectorAsPointOnImage(vectorList.get(index));
		return point;
	}

	// TODO: Need to get multiples of unit vectors within range.
}
