package basic_objects;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
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
	 * Used when walking the axis graph so that back edges are avoided. Must be
	 * reset to false before walking again.
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
	 * Calculates an approximation of the tangent line at this vertex by finding
	 * the secant line that passes through this vertex and some nearby vertex.
	 * The nearby vertex is between distances specified by lowerLimitPrecision
	 * and upperLimitPrecision.
	 * 
	 * @param lowerLimitPrecision
	 *            The least acceptable distance for a nearby point.
	 * @param upperLimitPrecision
	 *            The greatest acceptable distance for a nearby point.
	 * @return A double array containing the tangent slope and tangent
	 *         y-intercept.
	 * @throws DistanceException
	 */
	public double[] calculateTangentLine(double lowerLimitPrecision,
			double upperLimitPrecision) throws Exception {
		Vertex q = getNearbyVertex(lowerLimitPrecision, upperLimitPrecision);

		try {
			tangentSlope = (double)(myVertex.y - q.getPoint().y)
					/ (myVertex.x - q.getPoint().x);
			noTanSlope = false;
		} catch (ArithmeticException e) {
			tangentSlope = Integer.MAX_VALUE;
			noTanSlope = true;
		}

		if (noTanSlope) {
			tangentYIntercept = (double)myVertex.x;
		} else {
			tangentYIntercept = (double)myVertex.y - tangentSlope * myVertex.x;
		}

		tangentIsCalculated = true;

		return new double[] { tangentSlope, tangentYIntercept };
	}

	/**
	 * Calculates an approximation of the orthogonal line at this vertex based
	 * on the tangent line.
	 * 
	 * @param lowerLimitPrecision
	 *            The least acceptable distance for a nearby point.
	 * @param upperLimitPrecision
	 *            The greatest acceptable distance for a nearby point.
	 * @return A double array containing the orthogonal slope and orthogonal
	 *         y-intercept.
	 * @throws DistanceException
	 */
	public double[] calculateOrthogonalLine(double lowerLimitPrecision,
			double upperLimitPrecision) throws Exception {
		if (!tangentIsCalculated) {
			calculateTangentLine(lowerLimitPrecision, upperLimitPrecision);
		}

		try {
			orthogonalSlope = -1.0 / tangentSlope;
			noOrthoSlope = false;
		} catch (ArithmeticException e) {
			orthogonalSlope = Integer.MAX_VALUE;
			noOrthoSlope = true;
		}

		if (noTanSlope) {
			orthogonalSlope = 0;
		}

		if (noOrthoSlope) {
			orthogonalYIntercept = (double)myVertex.x;
		} else {
			orthogonalYIntercept = (double)myVertex.y - orthogonalSlope * myVertex.x;
		}

		return new double[] { orthogonalSlope, orthogonalYIntercept };
	}

	/**
	 * Finds a nearby vertex by performing breadth first search on the children
	 * until vertex is encountered that is greater than lowerLimitPrecision
	 * distance away and less than upperLimitPrecision away.
	 * 
	 * @param lowerLimitPrecision
	 *            Minimum distance required to be usable.
	 * @param upperLimitPrecision
	 *            Currently not used, may be able to remove.
	 * @return The nearby vertex
	 * @throws Exception
	 *             Thrown if there is no vertex that satisfies the limit.
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
				throw new Exception("No child of the vertex (" + myVertex.x
						+ ", " + myVertex.y
						+ ") satisfies the requested limit.");
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
	 * Calculates and returns a LinkedList of points on the tangent line close
	 * to the vertex.
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

		// Calculates x and y values around this vertex
		for (int i = 0; i < length; i++) {
			// sign is used to alternate directions in which new points are
			// calculated
			int sign = (int) Math.pow(-1, i + 1);
			int x = sign;
			// For even integers, starting with 0, x values to the left of this
			// vertex are
			// selected. For odd, values to the right are selected.
			if (i % 2 == 0) {
				x = x * (i / 2);
			} else {
				x = x * ((i + 1) / 2);
			}
			x = x + myVertex.x;

			// Calculate y value. If the tangent line has no slope then x is
			// reset to the x
			// value of this vertex and y is set to values above and below this
			// vertex's y value.
			int y = 0;
			if (noTanSlope) {
				x = myVertex.x;
				if (i % 2 == 0) {
					y = myVertex.y + (sign * (i / 2));
				} else {
					y = myVertex.y + (sign * ((i + 1) / 2));
				}
			} else {
				y = (int) (x * tangentSlope + tangentYIntercept);
			}

			Point p = new Point(x, y);
			line.add(p);
		}

		
		return fillLine(line);
	}

	/**
	 * Calculates and returns a LinkedList of points on the orthogonal line
	 * close to the vertex.
	 * 
	 * @param length
	 *            Number of points to calculate
	 * @return A LinkedList of points on the orthogonal line close to the
	 *         vertex.
	 * @throws Exception
	 *             Thrown if the orthogonal line has not yet been calculated.
	 */
	public LinkedList<Point> orthogonalLine(int length) throws Exception {
		if (!tangentIsCalculated) {
			throw new Exception("The orthogonal line must be calculated first.");
		}
		LinkedList<Point> line = new LinkedList<Point>();

		for (int i = 0; i < length; i++) {
			// sign is used to alternate directions in which new points are
			// calculated
			int sign = (int) Math.pow(-1, i + 1);
			int x = sign;
			if (i % 2 == 0) {
				x = x * (i / 2);
			} else {
				x = x * ((i + 1) / 2);
			}
			x = x + myVertex.x;

			int y = 0;
			if (noOrthoSlope) {
				y = myVertex.y;
				if (i % 2 == 0) {
					x = myVertex.x + (sign * (i / 2));
				} else {
					x = myVertex.x + (sign * ((i + 1) / 2));
				}
			} else {
				y = (int) (x * tangentSlope + tangentYIntercept);
			}

			Point p = new Point(x, y);
			line.add(p);
		}

		return fillLine(line);
	}
	
	/**
	 * Fills in points between calculated points to form a whole line.
	 * @param line
	 * @return
	 */
	private LinkedList<Point> fillLine(LinkedList<Point> line) {
		Collections.sort(line, new Comparator<Point>() {

			@Override
			public int compare(Point o1, Point o2) {
				return o1.y - o2.y;
			}});
		
		Collections.sort(line, new Comparator<Point>() {

			@Override
			public int compare(Point o1, Point o2) {
				return o1.x - o2.x;
			}});
		
		for (int i = 0; i < line.size() - 1; i++) {
			//Debugging code
			System.out.println("On index " + i);
			//End debug
			Point nextPoint = getNextPointToFill(line, i);
			if (!line.contains(nextPoint)) {
				line.add(i + 1, nextPoint);
			}
		}
		
		return line;
	}

	private Point getNextPointToFill(LinkedList<Point> line, int index) {
		Point thisPoint = line.get(index);
		Point nextPoint = new Point(thisPoint.x, thisPoint.y - 1);
		double leastDist = nextPoint.distance(line.get(index+1));
		
		Point tempPoint = new Point(thisPoint.x + 1, thisPoint.y - 1);
		double tempDist = tempPoint.distance(line.get(index+1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x + 1, thisPoint.y);
		tempDist = tempPoint.distance(line.get(index+1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x + 1, thisPoint.y + 1);
		tempDist = tempPoint.distance(line.get(index+1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x, thisPoint.y + 1);
		tempDist = tempPoint.distance(line.get(index+1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x - 1, thisPoint.y + 1);
		tempDist = tempPoint.distance(line.get(index+1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x - 1, thisPoint.y);
		tempDist = tempPoint.distance(line.get(index+1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x - 1, thisPoint.y - 1);
		tempDist = tempPoint.distance(line.get(index+1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}
		return nextPoint;
	}
}
