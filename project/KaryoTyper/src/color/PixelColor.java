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

		double tempGreyPixel = (.299 * newPixel.getRed()) + (.587 * newPixel.getGreen())
				+ (.114 * newPixel.getBlue());
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


}