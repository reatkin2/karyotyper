package basic_objects;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class TangentLine extends Line2D.Float {
	private Vertex vertex;
	private double slope;
	private boolean noSlope;
	private double yIntercept;
	private boolean lineIsCalculated;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double angle2Xaxis;

	public TangentLine(Point point1, Point point2) {
		super(point1, point2);
		this.angle2Xaxis = getAngle2Xaxis();
		vertex = null;
		lineIsCalculated = false;
	}
	
	public TangentLine(Vertex vertex, double lowerLimitPrecision, double upperLimitPrecision) throws Exception {
		this(vertex.getPoint(), vertex.getNearbyVertex(lowerLimitPrecision, upperLimitPrecision).getPoint());
		this.vertex = vertex;
		calculateTangentLine(lowerLimitPrecision, upperLimitPrecision);
	}

	private double getAngle2Xaxis() {
		return Math.atan(slope);
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
	 */
	public double[] calculateTangentLine(double lowerLimitPrecision, double upperLimitPrecision) {

		try {
			slope = (getY1() - getY2()) / (getX1() - getX2());
			noSlope = false;
		} catch (ArithmeticException e) {
			slope = Integer.MAX_VALUE;
			noSlope = true;
		}

		if (noSlope) {
			yIntercept = getX1();
		} else {
			yIntercept = getY1() - slope * getX1();
		}

		lineIsCalculated = true;
		
		return new double[] { slope, yIntercept };
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
		if (!lineIsCalculated) {
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
			x = x + vertex.getPoint().x;

			// Calculate y value. If the tangent line has no slope then x is
			// reset to the x
			// value of this vertex and y is set to values above and below this
			// vertex's y value.
			int y = 0;
			if (noSlope) {
				x = vertex.getPoint().x;
				if (i % 2 == 0) {
					y = vertex.getPoint().y + (sign * (i / 2));
				} else {
					y = vertex.getPoint().y + (sign * ((i + 1) / 2));
				}
			} else {
				y = (int) (x * slope + yIntercept);
			}

			Point p = new Point(x, y);
			line.add(p);
		}

		return fillLine(line);
	}
	
	/**
	 * Fills in points between calculated points to form a whole line.
	 * 
	 * @param line
	 * @return
	 */
	private LinkedList<Point> fillLine(LinkedList<Point> line) {
		Collections.sort(line, new Comparator<Point>() {

			@Override
			public int compare(Point o1, Point o2) {
				return o1.y - o2.y;
			}
		});

		Collections.sort(line, new Comparator<Point>() {

			@Override
			public int compare(Point o1, Point o2) {
				return o1.x - o2.x;
			}
		});

		for (int i = 0; i < line.size() - 1; i++) {
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
		double leastDist = nextPoint.distance(line.get(index + 1));

		Point tempPoint = new Point(thisPoint.x + 1, thisPoint.y - 1);
		double tempDist = tempPoint.distance(line.get(index + 1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x + 1, thisPoint.y);
		tempDist = tempPoint.distance(line.get(index + 1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x + 1, thisPoint.y + 1);
		tempDist = tempPoint.distance(line.get(index + 1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x, thisPoint.y + 1);
		tempDist = tempPoint.distance(line.get(index + 1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x - 1, thisPoint.y + 1);
		tempDist = tempPoint.distance(line.get(index + 1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x - 1, thisPoint.y);
		tempDist = tempPoint.distance(line.get(index + 1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}

		tempPoint = new Point(thisPoint.x - 1, thisPoint.y - 1);
		tempDist = tempPoint.distance(line.get(index + 1));
		if (tempDist < leastDist) {
			leastDist = tempDist;
			nextPoint = tempPoint;
		}
		return nextPoint;
	}

	/**
	 * @return the vertex
	 */
	public Vertex getVertex() {
		return vertex;
	}

	/**
	 * @return the slope
	 */
	public double getSlope() {
		return slope;
	}

	/**
	 * @return the noSlope
	 */
	public boolean isNoSlope() {
		return noSlope;
	}

	/**
	 * @return the yIntercept
	 */
	public double getyIntercept() {
		return yIntercept;
	}
	
	

}
