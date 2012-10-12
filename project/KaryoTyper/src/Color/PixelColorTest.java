package color;

import java.awt.Color;

import junit.framework.TestCase;

public class PixelColorTest extends TestCase {

	public void testIsBelowThreshold() {
		// Test for expected exception
		boolean exceptionFlag = false;
		try {
			exceptionFlag = true;
			PixelColor.isBelowThreshold(Color.BLACK, -1);
			exceptionFlag = false;
		} catch (IllegalArgumentException e) {
		}
		assertTrue(exceptionFlag);
		
		exceptionFlag = false;
		try {
			exceptionFlag = true;
			PixelColor.isBelowThreshold(Color.BLACK, 256);
			exceptionFlag = false;
		} catch (IllegalArgumentException e) {
		}
		assertTrue(exceptionFlag);

		assertTrue(PixelColor.isBelowThreshold(Color.WHITE, 240));
		assertTrue(PixelColor.isBelowThreshold(new Color(241, 241, 241), 240));

		assertFalse(PixelColor.isBelowThreshold(Color.WHITE, 255));
		assertFalse(PixelColor.isBelowThreshold(Color.BLACK, 240));
		assertFalse(PixelColor.isBelowThreshold(new Color(240, 240, 240), 240));
	}

}
