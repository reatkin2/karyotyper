/**
 * 
 */
package basic_objects;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * @author Robert
 * 
 */
public class Vector extends Point2D.Double {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Vector(double x, double y) {
		super(x, y);
	}

	/**
	 * Rotates the vector counterclockwise by the angle theta specified in radians.
	 * 
	 * @param theta
	 *            Angle in radians to rotate by.
	 */
	public void rotateVector(double theta) {
		double tempX = x * Math.cos(theta) - y * Math.sin(theta);
		y = x * Math.sin(theta) + y * Math.cos(theta);
		x = tempX;
	}

	/**
	 * Rotates a vector counterclockwise by the angle theta specified in radians.
	 * 
	 * @param vector
	 *            Vector to rotate.
	 * @param theta
	 *            Angle in radians to rotate by.
	 * @return A new vector rotated accordingly.
	 */
	public static Vector rotateVector(Vector vector, double theta) {
		double newX = vector.x * Math.cos(theta) - vector.y * Math.sin(theta);
		double newY = vector.x * Math.sin(theta) + vector.y * Math.cos(theta);

		return new Vector(newX, newY);
	}

	/**
	 * Gets a unit vector in the direction of the specified vector.
	 * 
	 * @param vector
	 *            Vector to normalize.
	 * @return Unit vector in the direction of the specified vector.
	 */
	public static Vector normalize(Vector vector) {
		double norm = Vector.magnitude(vector);

		return new Vector(vector.x / norm, vector.y / norm);
	}

	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector multiply(Vector v, double scalar) {
		Vector prodVector = new Vector(v.x * scalar, v.y * scalar);
		return prodVector;
	}

	public static Vector multiply(double scalar, Vector v) {
		return multiply(v, scalar);
	}
	
	public static double magnitude(Vector vector) {
		return Math.sqrt(vector.x * vector.x + vector.y * vector.y);
	}
	
	public static double dotProduct(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	/**
	 * Gets a vector from the point origin to the point endPoint.
	 * 
	 * @param origin
	 *            Source point of vector
	 * @param endPoint
	 *            End point of vector
	 * @return Vector from origin to endPoint
	 */
	public static Vector getVectorBetweenTwoPoints(Point origin, Point endPoint) {
		double vecX = endPoint.x - origin.x;
		double vecY = endPoint.y - origin.y;

		return new Vector(vecX, vecY);
	}
	
	/**
	 * Gets the counter-clockwise angle (clockwise with respect to image)
	 * from startVector to endVector
	 * @param startVector
	 * @param endVector
	 * @return
	 */
	public static double getAngle(Vector startVector, Vector endVector) {
		double magV1 = Vector.magnitude(startVector);
		double magV2 = Vector.magnitude(endVector);
		double dotProd = Vector.dotProduct(startVector, endVector);
		
		double angle = Math.acos(dotProd/(magV1 * magV2));
		
		if( startVector.x * endVector.y < startVector.y * endVector.x ) {
			   angle = 2 * Math.PI - angle;
		}
		
		return angle;
	}

}
