package chromosome;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

public class GeneticSlideImageTest extends TestCase {
	GeneticSlideImage slide;

	@Override
	protected void setUp() throws Exception {
		String currentPath = (new File(".")).getCanonicalPath();
		String gradientImageFile = currentPath + File.separator + "testData" + File.separator
				+ "GradientSwatch.png";
		slide = new GeneticSlideImage(gradientImageFile);
	}

	public void testGetBackgroundThreshold() {
		// Note that this test checks "proper behavior", however, it is very far from a "real world"
		// test as we shouldn't see gradient images for slides.
		assertEquals(139, slide.getBackgroundThreshold());
	}

	public void testComputeHistogram() throws IOException {
		int[] expected = { 3, 3, 3, 1, 5, 5, 1, 7, 7, 1, 9, 9, 1, 11, 11, 3, 11, 13, 5, 11, 15, 7,
				11, 17, 9, 11, 19, 11, 12, 20, 13, 14, 20, 15, 16, 20, 17, 18, 20, 19, 20, 21, 20,
				22, 23, 20, 24, 25, 20, 27, 26, 20, 29, 28, 20, 31, 30, 22, 31, 32, 24, 31, 34, 26,
				31, 36, 29, 30, 38, 31, 31, 39, 33, 33, 39, 35, 36, 38, 37, 38, 38, 39, 40, 39, 40,
				42, 42, 39, 44, 44, 39, 46, 46, 39, 48, 48, 40, 49, 50, 42, 49, 52, 44, 49, 54, 46,
				49, 56, 48, 49, 58, 50, 51, 59, 51, 53, 59, 53, 55, 59, 55, 57, 59, 58, 58, 60, 59,
				60, 62, 59, 62, 64, 59, 65, 65, 59, 67, 67, 59, 69, 69, 61, 69, 71, 60, 69, 71, 60,
				68, 69, 60, 66, 67, 62, 62, 65, 62, 60, 63, 62, 59, 60, 60, 59, 58, 58, 59, 56, 56,
				59, 55, 53, 59, 53, 51, 58, 52, 49, 56, 52, 48, 53, 52, 46, 51, 52, 44, 49, 52, 42,
				48, 49, 42, 46, 47, 42, 44, 45, 42, 42, 43, 42, 40, 41, 40, 40, 39, 39, 39, 37, 37,
				39, 35, 35, 39, 33, 33, 38, 32, 31, 36, 32, 29, 34, 32, 27, 32, 33, 24, 30, 32, 23,
				28, 30, 23, 26, 28, 24, 23, 26, 24, 21, 24, 24, 19, 22, 22, 19, 20, 20, 19, 18, 18,
				19, 16, 312 };
		int[] histogram = slide.getHistogram();
		for (int i = 0; i < histogram.length; i++) {
			assertEquals(expected[i], histogram[i]);
		}
	}

}
