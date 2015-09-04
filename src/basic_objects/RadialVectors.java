package basic_objects;

import java.awt.Point;
import java.util.ArrayList;

public class RadialVectors {

	private final double CIRC = 2 * Math.PI;

	private ArrayList<Vector> vectorList;
	private Point centerPoint;
	private double theta;
	private double distance;
	private double stepTheta;

	public RadialVectors(Point centerPoint, int numVectors, double distance) {
		this.centerPoint = centerPoint;
		this.distance = distance;
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
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
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

	/**
	 * Gets the specified number of points on the image around the center point within a specified
	 * angle.
	 * 
	 * If the specified number of vectors is even, then the number of vectors returned will be 1
	 * plus the specified number.
	 * 
	 * @param endPoint
	 *            Point to search about.
	 * @param angle
	 *            Angle in radians to search in.
	 * @param numPoints
	 *            Number of points to return within the angle.
	 * @return ArrayList of the points in the angle.
	 */
	public ArrayList<Point> getPointsInRange(Point endPoint, double angle, int numPoints) {
		if (numPoints % 2 == 0) {
			numPoints++;
		}
		ArrayList<Point> pointsInRange = new ArrayList<Point>(numPoints);
		for (int i = 0; i < numPoints; i++) {
			pointsInRange.add(null);
		}
		int middle = numPoints / 2;
		pointsInRange.set(middle, endPoint);
		if (numPoints == 1) {
			return pointsInRange;
		}

		double localTheta = angle / (numPoints - 1);
		this.stepTheta=localTheta;
		//TODO(reatkin2): I changed this to be at the right distance each time aamcknig 
		Vector vector = getPointAsVectorAtDistance(endPoint);
		//TODO(reatkin2): I changed this to be at the right distance each time aamcknig 
		
		for (int i = 1; i <= middle; i++) {
			Vector vectorP = Vector.rotateVector(vector, localTheta * i);
			pointsInRange.set(middle + i, getVectorAsPointOnImage(vectorP));

			Vector vectorN = Vector.rotateVector(vector, localTheta * i * -1);
			pointsInRange.set(middle - i, getVectorAsPointOnImage(vectorN));
		}

		return pointsInRange;
	}

	/**
	 * Gets the specified number of vectors within a specified angle about a specified middle
	 * vector.
	 * 
	 * @param middleVector
	 *            The middle vector.
	 * @param angle
	 *            Angle in radians to search.
	 * @param numVectors
	 *            Number of vectors to return.
	 * @return ArrayList of vectors about middle vector.
	 */
	public ArrayList<Vector> getVectorsInRange(Vector middleVector, double angle, int numVectors) {
		ArrayList<Vector> vectorsInRange = new ArrayList<Vector>(numVectors);

		return vectorsInRange;
	}

	private Point getVectorAsPointOnImage(Vector vector) {
		int xOnImage = (int) Math.round(vector.x + centerPoint.x);
		int yOnImage = (int) Math.round(vector.y + centerPoint.y);
		return new Point(xOnImage, yOnImage);
	}
	private Vector getPointAsVectorAtDistance(Point point){
		Vector tempVector=getPointAsVector(point);
		tempVector=Vector.multiply(Vector.normalize(tempVector),this.distance);
		return tempVector;
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
	 * 
	 * @param point
	 *            Point on radial edge to reflect.
	 * @return The point reflected across the center point.
	 */
	public Point getOpposite(Point point) {
		Vector vector = getPointAsVector(point);

		// Multiply by -1
		vector.x *= -1;
		vector.y *= -1;

		// return as Point on image
		return getVectorAsPointOnImage(vector);
	}

	public Point getPointAtIndex(int index) {
		Point point = getVectorAsPointOnImage(vectorList.get(index));
		return point;
	}

	public Point getPointAtIndexAndDistance(int index, double distance) {
		Vector vector = vectorList.get(index);
		double multiplier = distance / this.distance;
		vector.x *= multiplier;
		vector.y *= multiplier;

		return getVectorAsPointOnImage(vector);
	}
	
	/**
	 * Based on a specified point in the list, returns the rotated point at the
	 * specified distance.
	 * 
	 * @param index
	 * @param angle
	 * @param distance
	 * @return
	 */
	public Point getRotatedPointAtDistance(int index, double angle, double distance) {
		Vector vector = vectorList.get(index);
		vector = Vector.rotateVector(vector, angle);
		double multiplier = distance/ this.distance;
		vector.x *= multiplier;
		vector.y *= multiplier;
		
		return getVectorAsPointOnImage(vector);
	}

	public void multiplyRadius(double multiple) {
		distance *= multiple;
		for (Vector v : vectorList) {
			v.x *= multiple;
			v.y *= multiple;
		}
	}

	public void normalize() {
		multiplyRadius(1.0 / distance);
	}

	public String toString() {
		String radialVectorStr = "Center point: (" + centerPoint.x + ", " + centerPoint.y + ")\n";
		radialVectorStr += "Theta: " + theta + "\n";
		radialVectorStr += "Distance: " + distance + "\n";
		
		for (Vector v : vectorList) {
			radialVectorStr += "(" + v.x + ", " + v.y + ")\n";
		}
		
		return radialVectorStr;
	}
	
	public String pointsToString() {
		ArrayList<Point> pointList = getVectorsAsPointsOnImage();
		String pointsStr = "Center point: (" + centerPoint.x + ", " + centerPoint.y + ")\n";
		pointsStr += "Theta: " + theta + "\n";
		pointsStr += "Distance: " + distance + "\n";
		
		for (Point p : pointList) {
			pointsStr += "(" + p.x + ", " + p.y + ")\n";
		}
		
		return pointsStr;
	}
	
	public double getStepTheta() {
		return stepTheta;
	}
}
