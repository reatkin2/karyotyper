package testing;

import java.awt.Color;

import color.PixelColor;

import junit.framework.TestCase;

public class PixelColorTest extends TestCase {

	public void testIsAboveThreshold() {
		// Test for expected exception
		boolean exceptionFlag = false;
		try {
			exceptionFlag = true;
			PixelColor.isAboveThreshold(Color.BLACK, -1);
			exceptionFlag = false;
		} catch (IllegalArgumentException e) {
		}
		assertTrue(exceptionFlag);
		
		exceptionFlag = false;
		try {
			exceptionFlag = true;
			PixelColor.isAboveThreshold(Color.BLACK, 256);
			exceptionFlag = false;
		} catch (IllegalArgumentException e) {
		}
		assertTrue(exceptionFlag);

		assertTrue(PixelColor.isAboveThreshold(Color.WHITE, 240));
		assertTrue(PixelColor.isAboveThreshold(new Color(241, 241, 241), 240));

		assertFalse(PixelColor.isAboveThreshold(Color.WHITE, 255));
		assertFalse(PixelColor.isAboveThreshold(Color.BLACK, 240));
		assertFalse(PixelColor.isAboveThreshold(new Color(240, 240, 240), 240));
	}

}
