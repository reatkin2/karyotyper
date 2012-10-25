package basic_objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class RadialVectors {

	private ArrayList<Vector> vectorList;
	private Point centerPoint;

	public RadialVectors(Point centerPoint) {
		this.centerPoint = centerPoint;
		vectorList = new ArrayList<Vector>();
		
	}

	public void generateVectors(int numVectors, double distance) {
		if (numVectors < 0) {
			System.out.println("Number of vectors must be positive.");
			System.exit(1);
		}

		final double CIRC = 2 * Math.PI;

		double theta = CIRC / numVectors;

		double aggregateAngle = 0;

		Vector firstVector = new Vector(0, distance);
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
		return new Point((int) centerPoint.x, (int) centerPoint.y);
	}

}
