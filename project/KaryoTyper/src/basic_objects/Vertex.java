package basic_objects;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public class Vertex {
	private LinkedList<Vertex> children;
	private Point myVertex;
	private boolean wasIntersection;
	private int distanceFromEdge;
	private double tangentSlope;
	private boolean noTanSlope;
	private double tangentYIntercept;
	private double orthogonalSlope;
	private boolean noOrthoSlope;
	private double orthogonalYIntercept;
	/**
	 * Used when walking the axis graph so that back edges are avoided. Must be reset to false
	 * before walking again.
	 */
	private boolean hasBeenChecked;
	private boolean tangentIsCalculated;

	public Vertex(Point vertex, int distance) {
		distanceFromEdge = distance;
		myVertex = new Point(vertex);
		children = new LinkedList<Vertex>();
		wasIntersection = false;
		hasBeenChecked = false;
		tangentIsCalculated = false;
	}

	public void addChild(Vertex nextChild) {
		if (!children.contains(nextChild)
				&& !myVertex.equals(nextChild.myVertex)) {
			children.add(nextChild);
		}
	}

	public boolean isChild(Vertex checkPoint) {
		if (children.contains(checkPoint)) {
			return true;
		} else {
			return false;
		}
	}

	public int getDistanceFromEdge() {
		return distanceFromEdge;
	}

	public boolean isIntersection() {
		if (children.size() > 2) {
			wasIntersection = true;
			return true;
		}
		return false;
	}

	public boolean wasIntersection() {
		return this.wasIntersection;
	}

	public boolean checkAdjacent(Vertex checkPoint) {
		// TODO(Andrew): change this for efficiency do this in shape without
		// calculating distance
		if (isChild(checkPoint)) {
			return true;
		} else if (!checkPoint.myVertex.equals(myVertex)
				&& checkPoint.myVertex.distance(myVertex) < 1.5) {
			return true;
		}
		return false;
	}

	public Point getPoint() {
		return myVertex;
	}

	public int numberOfChildren() {
		return this.children.size();
	}

	public LinkedList<Vertex> getChildren() {
		return children;
	}

	/**
	 * Calculates an approximation of the tangent line at this vertex by finding the secant line
	 * that passes through this vertex and some nearby vertex. The nearby vertex is between
	 * distances specified by lowerLimitPrecision and upperLimitPrecision.
	 * 
	 * @param lowerLimitPrecision
	 *            The least acceptable distance for a nearby point.
	 * @param upperLimitPrecision
	 *            The greatest acceptable distance for a nearby point.
	 * @return A double array containing the tangent slope and tangent y-intercept.
	 * @throws DistanceException
	 */
	public double[] calculateTangentLine(double lowerLimitPrecision,
			double upperLimitPrecision) throws Exception {
		Vertex q = getNearbyVertex(lowerLimitPrecision, upperLimitPrecision);

		try {
			tangentSlope = (myVertex.y - q.getPoint().y)
					/ (myVertex.x - q.getPoint().x);
			noTanSlope = false;
		} catch (ArithmeticException e) {
			tangentSlope = Integer.MAX_VALUE;
			noTanSlope = true;
		}

		if (noTanSlope) {
			tangentYIntercept = myVertex.x;
		} else {
			tangentYIntercept = myVertex.y - tangentSlope * myVertex.x;
		}

		tangentIsCalculated = true;

		return new double[] { tangentSlope, tangentYIntercept };
	}

	/**
	 * Calculates an approximation of the orthogonal line at this vertex based on the tangent line.
	 * 
	 * @param lowerLimitPrecision
	 *            The least acceptable distance for a nearby point.
	 * @param upperLimitPrecision
	 *            The greatest acceptable distance for a nearby point.
	 * @return A double array containing the orthogonal slope and orthogonal y-intercept.
	 * @throws DistanceException
	 */
	public double[] calculateOrthogonalLine(double lowerLimitPrecision,
			double upperLimitPrecision) throws Exception {
		if (!tangentIsCalculated) {
			calculateTangentLine(lowerLimitPrecision, upperLimitPrecision);
		}

		try {
			orthogonalSlope = -1 / tangentSlope;
			noOrthoSlope = false;
		} catch (ArithmeticException e) {
			orthogonalSlope = Integer.MAX_VALUE;
			noOrthoSlope = true;
		}

		if (noTanSlope) {
			orthogonalSlope = 0;
		}

		if (noOrthoSlope) {
			orthogonalYIntercept = myVertex.x;
		} else {
			orthogonalYIntercept = myVertex.y - orthogonalSlope * myVertex.x;
		}

		return new double[] { orthogonalSlope, orthogonalYIntercept };
	}

	/**
	 * Finds a nearby vertex by performing breadth first search on the children until vertex is
	 * encountered that is greater than lowerLimitPrecision distance away and less than
	 * upperLimitPrecision away.
	 * 
	 * @param lowerLimitPrecision
	 * @param upperLimitPrecision
	 *            Currently not used, may be able to remove.
	 * @return
	 * @throws DistanceException
	 */
	private Vertex getNearbyVertex(double lowerLimitPrecision,
			double upperLimitPrecision) throws Exception {
		Queue<Vertex> fringe = new LinkedList<Vertex>();
		fringe.addAll(children);
		hasBeenChecked = true;
		Vertex v = null;
		do {
			v = fringe.poll();
			if (v == null) {
				throw new Exception("Lower limit is too high or vertex ("
						+ myVertex.x + ", " + myVertex.y + ") has no children.");
			}
			v.setHasBeenChecked(true);
			for (Vertex w : v.getChildren()) {
				if (!w.hasBeenChecked()) {
					fringe.add(w);
				}
			}
		} while (Point.distance(myVertex.x, myVertex.y, v.getPoint().x,
				v.getPoint().y) < lowerLimitPrecision);
		return v;
	}

	public boolean hasBeenChecked() {
		return hasBeenChecked;
	}

	public void setHasBeenChecked(boolean hasBeenChecked) {
		this.hasBeenChecked = hasBeenChecked;
	}

	/**
	 * Calculates and returns a LinkedList of points on the tangent line close to the vertex.
	 * 
	 * @param length
	 *            Number of points to calculate
	 * @return A LinkedList of points on the tangent line close to the vertex.
	 * @throws Exception
	 *             Thrown if the tangent line has not yet been calculated.
	 */
	public LinkedList<Point> tangentLine(int length) throws Exception {
		if (!tangentIsCalculated) {
			throw new Exception("The tangent line must be calculated first.");
		}
		LinkedList<Point> line = new LinkedList<Point>();

		for (int i = 0; i < length; i++) {
			int x = (int) Math.pow(-1, i + 1);
			if (i % 2 == 0) {
				x = x * (i / 2);
			} else {
				x = x * ((i + 1) / 2);
			}
			x = x + myVertex.x;

			int y = (int) (x * tangentSlope + tangentYIntercept);

			Point p = new Point(x, y);
			line.add(p);
		}

		return line;
	}

	/**
	 * Calculates and returns a LinkedList of points on the orthogonal line close to the vertex.
	 * 
	 * @param length
	 *            Number of points to calculate
	 * @return A LinkedList of points on the orthogonal line close to the vertex.
	 * @throws Exception
	 *             Thrown if the orthogonal line has not yet been calculated.
	 */
	public LinkedList<Point> orthogonalLine(int length) throws Exception {
		if (!tangentIsCalculated) {
			throw new Exception("The orthogonal line must be calculated first.");
		}
		LinkedList<Point> line = new LinkedList<Point>();

		for (int i = 0; i < length; i++) {
			int x = (int) Math.pow(-1, i + 1);
			if (i % 2 == 0) {
				x = x * (i / 2);
			} else {
				x = x * ((i + 1) / 2);
			}
			x = x + myVertex.x;

			int y = (int) (x * orthogonalSlope + orthogonalYIntercept);

			Point p = new Point(x, y);
			line.add(p);
		}

		return line;
	}
}
