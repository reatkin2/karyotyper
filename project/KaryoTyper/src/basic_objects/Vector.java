/**
 * 
 */
package basic_objects;

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
		y = x * Math.cos(theta) + y * Math.sin(theta);
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
		double norm = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
		
		return new Vector(vector.x / norm, vector.y / norm);
	}
	
	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y);
	}

}
