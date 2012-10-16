package color;

import java.awt.Color;

public class ISOClineColor {
	// 0-20 will be 0-256
	private static double scalingFactor = 12.75;

	/**
	 * this gives a color for each distance in the distance map this returns a scaled value of the
	 * distance map number between 0-20 to a grayscale color between 0-255 for red, green,and blue
	 * values of the color
	 * 
	 * @param color
	 *            the number from the distance map , the distance from the edge
	 * @return the color that will represent this distance from the edge
	 */
	public static Color getColor(int color) {
		int rgb = (int) Math.round(ISOClineColor.scalingFactor * color);
		if (color > 20) {
			return new Color(255, 255, 255);
		}
		return new Color(rgb, rgb, rgb);

	}
}