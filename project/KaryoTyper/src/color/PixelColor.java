package color;

/*
 * IsColor.java
 *
 * Created on December 13, 2004, 10:33 AM
 */
import java.awt.Color;

/**
 * 
 * @author Andrew
 */
public class PixelColor {
	/**
	 * Intensity values represent portion of each color channel which contributes to grayscale
	 * intensity for the overall color.
	 */
	public final static double RED_INTENSITY = 0.299;
	public final static double GREEN_INTENSITY = 0.587;
	public final static double BLUE_INTENSITY = 0.114;

	/**
	 * this is the method for removing the background color by checking if the current pixel color
	 * is above the grayscale color threshold for this image
	 * 
	 * @param newPixel
	 *            the pixel to be checked if its part of the background
	 * @param threshold
	 *            the cut off threshold for the current image everything above this value is the
	 *            background
	 * @return
	 */
	public static boolean isAboveThreshold(Color newPixel, int threshold) {
		if (threshold > 255 || threshold < 0) {
			throw new IllegalArgumentException(String.format(
					"Threshold provided (%s) was not in the valid range of 0-255", threshold));
		}

		int tempGreyPixel = colorToGrayscale(newPixel);
		if (tempGreyPixel > threshold) {
			return true;
		}

		return false;
	}
	/**
	 * this is the method for removing the background color by checking if the current pixel color
	 * is below the grayscale color threshold for this image
	 * 
	 * @param newPixel
	 *            the pixel to be checked if below threshold
	 * @param threshold
	 *            the cut off threshold for the current image everything below this value is
	 *            dark
	 * @return
	 */
	public static boolean isBelowThreshold(Color newPixel, int threshold) {
		if (threshold > 255 || threshold < 0) {
			throw new IllegalArgumentException(String.format(
					"Threshold provided (%s) was not in the valid range of 0-255", threshold));
		}

		double tempGreyPixel = (.299 * newPixel.getRed()) + (.587 * newPixel.getGreen())
				+ (.114 * newPixel.getBlue());
		if (tempGreyPixel < threshold) {
			return true;
		}

		return false;
	}


	/**
	 * Convert a provided color to grayscale, taking into account visual intensity in each color
	 * channel.
	 * 
	 * @param c
	 *            The color to convert to grayscale
	 * @return Grayscale equivalent of the provided color as an integer.
	 */
	public static int colorToGrayscale(Color c) {
		return (int) Math.round((RED_INTENSITY * c.getRed() + GREEN_INTENSITY * c.getGreen() + BLUE_INTENSITY
				* c.getBlue()));
	}

}