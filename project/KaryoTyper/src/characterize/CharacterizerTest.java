package characterize;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import junit.framework.TestCase;
import color.PixelColor;

public class CharacterizerTest extends TestCase {
	private final double fpThreshold = 0.0001;

	public void testPolygonalArea() {
		ArrayList<Point2D> rightTriangle = new ArrayList<Point2D>(Arrays.asList(new Point(0, 0),
				new Point(0, 2), new Point(2, 0)));
		double rightTriangleArea = Characterizer.polygonalArea(rightTriangle);
		double rightTriangleExpected = 2.0;

		assertTrue(String.format("Expected value of (%s), but instead received (%s)",
				rightTriangleExpected, rightTriangleArea),
				rightTriangleArea < rightTriangleExpected + fpThreshold
						&& rightTriangleArea > rightTriangleExpected - fpThreshold);

		ArrayList<Point2D> square = new ArrayList<Point2D>(Arrays.asList(new Point(0, 0),
				new Point(0, 2), new Point(2, 2), new Point(2, 0)));
		double squareArea = Characterizer.polygonalArea(square);
		double squareExpected = 4.0;

		assertTrue(String.format("Expected value of (%s), but instead received (%s)",
				squareExpected, squareArea), squareArea < squareExpected + fpThreshold
				&& squareArea > squareExpected - fpThreshold);

		ArrayList<Point2D> fractionalSquare = new ArrayList<Point2D>(Arrays.asList(
				new Point2D.Double(0, 0), new Point2D.Double(0, 0.5), new Point2D.Double(0.5, 0.5),
				new Point2D.Double(0.5, 0)));
		double fractionalSquareArea = Characterizer.polygonalArea(fractionalSquare);
		double fractionalSquareExpected = 0.25;

		assertTrue(String.format("Expected value of (%s), but instead received (%s)",
				squareExpected, fractionalSquareArea),
				fractionalSquareArea < fractionalSquareExpected + fpThreshold
						&& fractionalSquareArea > fractionalSquareExpected - fpThreshold);

		ArrayList<Point2D> diamond = new ArrayList<Point2D>(Arrays.asList(new Point(0, 2),
				new Point(2, 4), new Point(4, 2), new Point(2, 0)));
		double diamondArea = Characterizer.polygonalArea(diamond);
		double diamondExpected = 8.0;

		assertTrue(String.format("Expected value of (%s), but instead received (%s)",
				diamondExpected, diamondArea), diamondArea < diamondExpected + fpThreshold
				&& diamondArea > diamondExpected - fpThreshold);

		ArrayList<Point2D> trapezoid = new ArrayList<Point2D>(Arrays.asList(new Point(0, 0),
				new Point(1, 2), new Point(2, 2), new Point(3, 0)));
		double trapezoidArea = Characterizer.polygonalArea(trapezoid);
		double trapezoidExpected = 4;

		assertTrue(String.format("Expected value of (%s), but instead received (%s)",
				trapezoidExpected, trapezoidArea), trapezoidArea < trapezoidExpected + fpThreshold
				&& trapezoidArea > trapezoidExpected - fpThreshold);

		ArrayList<Point2D> pentagon = new ArrayList<Point2D>(Arrays.asList(new Point(0, 1),
				new Point(2, 2), new Point(4, 1), new Point(3, 0), new Point(1, 0)));
		double pentagonArea = Characterizer.polygonalArea(pentagon);
		double pentagonExpected = 5;

		assertTrue(String.format("Expected value of (%s), but instead received (%s)",
				pentagonExpected, pentagonArea), pentagonArea < pentagonExpected + fpThreshold
				&& pentagonArea > pentagonExpected - fpThreshold);
	}

	public void testBuildSlopeBufferVertical() {

		/******** Test a basic linear buffer vertically ************************************ */
		int[] ends = { -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		int[] middle = { -1, 0, 1, 1, 1, 1, 1, 0, -1 };
		int[][] chromosomeArray = new int[20][ends.length];
		for (int row = 0; row < chromosomeArray.length; row++) {
			if (row == 0 || row == chromosomeArray.length) {
				chromosomeArray[row] = ends;
			} else {
				chromosomeArray[row] = middle;
			}
		}

		GrayBuffer chromosomeBuffer = new GrayBuffer(chromosomeArray[0].length,
				chromosomeArray.length);
		for (int y = 0; y < chromosomeArray.length; y++) {
			for (int x = 0; x < chromosomeArray[0].length; x++) {
				chromosomeBuffer.set(x, y, chromosomeArray[y][x]);
			}
		}

		// Generate midline
		Point[] midlineArray = new Point[chromosomeArray.length - 2];
		for (int y = 1; y < chromosomeArray.length - 1; y++) {
			midlineArray[y - 1] = new Point(4, y);
		}

		ArrayList<Point> midlineBuffer = new ArrayList<Point>(Arrays.asList(midlineArray));
		double[][][] slopeBuffer = Characterizer.buildSlopeBuffer(chromosomeBuffer, midlineBuffer);

		// Characterizer.printSlopeBuffer(slopeBuffer);

		// As we are checking vertically, expected values are indexed in terms of x... scanning left
		// to right
		double[] expectedX = { -1, -1, -1, -1, 0, 1, 1, 1, 1 };
		double[] expectedY = { 0, 0, 0, 0, 1, 0, 0, 0, 0 };
		for (int y = 0; y < chromosomeBuffer.height; y++) {
			for (int x = 0; x < chromosomeBuffer.width; x++) {

				// Slope vector x comparison
				assertTrue(
						String.format(
								"Expected value of (%s) for x at index (%s, %s), but instead received (%s)",
								expectedX[x], x, y, slopeBuffer[x][y][0]),
						slopeBuffer[x][y][0] < expectedX[x] + fpThreshold
								&& slopeBuffer[x][y][0] > expectedX[x] - fpThreshold);
				// Slope vector y comparison
				assertTrue(
						String.format(
								"Expected value of (%s) for y at index (%s, %s), but instead received (%s)",
								expectedX[x], x, y, slopeBuffer[x][y][1]),
						slopeBuffer[x][y][1] < expectedY[x] + fpThreshold
								&& slopeBuffer[x][y][1] > expectedY[x] - fpThreshold);
			}
		}
	}

	public void testBuildSlopeBufferHorizontal() {

		/******** Test a basic linear buffer horizontally *********************************** */
		int[] ends = { -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		int[] middle = { -1, 0, 1, 1, 1, 1, 1, 0, -1 };
		int[][] chromosomeArray = new int[20][ends.length];
		for (int row = 0; row < chromosomeArray.length; row++) {
			if (row == 0 || row == chromosomeArray.length) {
				chromosomeArray[row] = ends;
			} else {
				chromosomeArray[row] = middle;
			}
		}

		// Flip coordinates as chromosomeArray is the transpose of what we want
		// Note that the chromosomeArray coords are already flipped for setting simplicity
		GrayBuffer chromosomeBuffer = new GrayBuffer(chromosomeArray.length,
				chromosomeArray[0].length);
		for (int x = 0; x < chromosomeArray.length; x++) {
			for (int y = 0; y < chromosomeArray[0].length; y++) {
				chromosomeBuffer.set(x, y, chromosomeArray[x][y]);
			}
		}

		// Generate midline
		Point[] midlineArray = new Point[chromosomeArray.length - 2];
		for (int x = 1; x < chromosomeArray.length - 1; x++) {
			midlineArray[x - 1] = new Point(x, 4);
		}

		ArrayList<Point> midlineBuffer = new ArrayList<Point>(Arrays.asList(midlineArray));
		double[][][] slopeBuffer = Characterizer.buildSlopeBuffer(chromosomeBuffer, midlineBuffer);

		// Characterizer.printSlopeBuffer(slopeBuffer);

		// As we are checking horizontally, expected values are indexed in terms of y... scanning
		// bottom to top
		double[] expectedX = { 0, 0, 0, 0, 1, 0, 0, 0, 0 };
		double[] expectedY = { -1, -1, -1, -1, 0, 1, 1, 1, 1 };
		for (int y = 0; y < chromosomeBuffer.height; y++) {
			for (int x = 0; x < chromosomeBuffer.width; x++) {

				// Slope vector x comparison
				assertTrue(
						String.format(
								"Expected value of (%s) for x at index (%s, %s), but instead received (%s)",
								expectedX[y], x, y, slopeBuffer[x][y][0]),
						slopeBuffer[x][y][0] < expectedX[y] + fpThreshold
								&& slopeBuffer[x][y][0] > expectedX[y] - fpThreshold);
				// Slope vector y comparison
				assertTrue(
						String.format(
								"Expected value of (%s) for y at index (%s, %s), but instead received (%s)",
								expectedY[y], x, y, slopeBuffer[x][y][1]),
						slopeBuffer[x][y][1] < expectedY[y] + fpThreshold
								&& slopeBuffer[x][y][1] > expectedY[y] - fpThreshold);
			}
		}
	}

	public void testLinearizeChromosome() throws IOException {
		String currentPath = (new File(".")).getCanonicalPath();
		String chromFile = currentPath + File.separator + "testData" + File.separator
				+ "ExampleChromosome.png";
		BufferedImage chromImage = ImageIO.read(new File(chromFile));

		GrayBuffer chromBuffer = new GrayBuffer(chromImage.getWidth(), chromImage.getHeight());
		for (int x = 0; x < chromImage.getWidth(); x++) {
			for (int y = 0; y < chromImage.getHeight(); y++) {
				// BufferedImage's origin is top-left and we want GrayBuffer to have bottom-left so
				// the internal byte-array mirrors the image for debug purposes.
				Color pxColor = new Color(chromImage.getRGB(x, y));
				chromBuffer.set(x, y, PixelColor.colorToGrayscale(pxColor));
			}
		}

		ArrayList<Point> medialAxis = new ArrayList<Point>(Arrays.asList(new Point(9, 5), new Point(
				8, 6), new Point(7, 5), new Point(6, 6), new Point(6, 7), new Point(6, 8),
				new Point(6, 9), new Point(7, 10), new Point(8, 11), new Point(8, 12), new Point(9,
						13), new Point(9, 14), new Point(9, 15), new Point(10, 16), new Point(10,
						17), new Point(10, 18), new Point(10, 19), new Point(10, 20), new Point(11,
						21), new Point(11, 22), new Point(11, 23), new Point(11, 24), new Point(12,
						25), new Point(12, 26), new Point(12, 27), new Point(12, 28), new Point(12,
						29), new Point(12, 30), new Point(13, 31), new Point(13, 32), new Point(14,
						33), new Point(13, 34), new Point(13, 35), new Point(13, 36), new Point(13,
						37), new Point(14, 38), new Point(14, 39), new Point(15, 40), new Point(15,
						41), new Point(15, 42), new Point(15, 43), new Point(15, 44), new Point(15,
						45), new Point(15, 46), new Point(16, 47), new Point(16, 48), new Point(17,
						49), new Point(18, 50), new Point(19, 51), new Point(19, 52), new Point(20,
						53), new Point(20, 54), new Point(20, 55), new Point(20, 56), new Point(20,
						57), new Point(21, 58), new Point(21, 59), new Point(21, 60), new Point(22,
						61), new Point(23, 62), new Point(23, 63), new Point(23, 64), new Point(24,
						65), new Point(24, 66), new Point(24, 67), new Point(24, 68), new Point(25,
						69), new Point(26, 70), new Point(26, 71), new Point(26, 72), new Point(26,
						73), new Point(27, 74), new Point(28, 75), new Point(27, 76), new Point(27,
						77), new Point(28, 78), new Point(29, 79), new Point(30, 80), new Point(31,
						81), new Point(31, 82), new Point(30, 83), new Point(31, 84), new Point(32,
						85), new Point(33, 86), new Point(33, 87), new Point(33, 88), new Point(34,
						89), new Point(35, 90), new Point(36, 91), new Point(36, 92), new Point(37,
						93), new Point(37, 94), new Point(38, 95), new Point(38, 96), new Point(39,
						97), new Point(40, 98), new Point(40, 99), new Point(41, 100), new Point(
						42, 101), new Point(42, 102), new Point(42, 103), new Point(43, 104),
				new Point(44, 105), new Point(45, 106), new Point(46, 107), new Point(46, 108),
				new Point(46, 109), new Point(47, 110), new Point(48, 111), new Point(48, 112),
				new Point(49, 113), new Point(49, 114), new Point(50, 115), new Point(50, 116)));

		String expectedLinearFile = currentPath + File.separator + "testData" + File.separator
				+ "ExampleLinearChromosome.png";
		BufferedImage expectedImage = ImageIO.read(new File(expectedLinearFile));
		int width = expectedImage.getWidth();
		int height = expectedImage.getHeight();
		
		GrayBuffer expectedBuffer = new GrayBuffer(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// BufferedImage's origin is top-left and we want GrayBuffer to have bottom-left so
				// the internal byte-array mirrors the image for debug purposes.
				Color pxColor = new Color(expectedImage.getRGB(x, y));
				expectedBuffer.set(x, y, PixelColor.colorToGrayscale(pxColor));
			}
		}

		GrayBuffer resultBuffer = Characterizer.linearizeChromosome(chromBuffer, medialAxis);

		assertEquals(expectedBuffer.width, resultBuffer.width);
		assertEquals(expectedBuffer.height, resultBuffer.height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int expected = expectedBuffer.get(x, y);
				int actual = resultBuffer.get(x, y);
				if (!(expected == 255 && actual == -1)) {
					assertEquals(String.format("For index (%s, %s)", x, y), expected, actual,
							1);
				}
			}
		}
	}

	public void testCalculateBandFunction() throws IOException {
		String currentPath = (new File(".")).getCanonicalPath();
		String linearChromFile = currentPath + File.separator + "testData" + File.separator
				+ "ExampleLinearChromosome.png";
		BufferedImage img = ImageIO.read(new File(linearChromFile));
		int width = img.getWidth();
		int height = img.getHeight();

		double[] referenceFunc = { 0.13886792452830188, 0.2558490566037736, 0.3418867924528302,
				0.44528301886792454, 0.5335849056603773, 0.5456603773584906, 0.11018867924528301,
				0.26867924528301884, 0.31471698113207547, 0.41509433962264153, 0.4362264150943396,
				0.39245283018867927, 0.1328301886792453, 0.13358490566037737, 0.08452830188679246,
				0.06943396226415094, 0.09660377358490566, 0.3584905660377358, 0.4362264150943396,
				0.29660377358490564, 0.6860377358490566, 0.7584905660377359, 0.7713207547169811,
				0.6739622641509434, 0.5569811320754717, 0.4362264150943396, 0.022641509433962263,
				0.03471698113207547, 0.18641509433962264, 0.2528301886792453, 0.3652830188679245,
				0.38264150943396225, 0.6150943396226415, 0.8128301886792453, 0.6566037735849056,
				0.5177358490566037, 0.15622641509433963, 0.21358490566037736, 0.43018867924528303,
				0.9154716981132075, 1.0, 0.8618867924528302, 0.43849056603773584,
				0.24075471698113207, 0.20528301886792452, 0.17433962264150943, 0.13660377358490566,
				0.09207547169811321, 0.20830188679245282, 0.2830188679245283, 0.2988679245283019,
				0.33132075471698114, 0.37660377358490565, 0.4075471698113208, 0.3879245283018868,
				0.12452830188679245, 0.19169811320754718, 0.259622641509434, 0.30566037735849055,
				0.3305660377358491, 0.28452830188679246, 0.18716981132075472, 0.1230188679245283,
				0.13132075471698113, 0.17962264150943397, 0.18716981132075472, 0.07924528301886792,
				0.07169811320754717, 0.2732075471698113, 0.2030188679245283, 0.09886792452830188,
				0.08226415094339623, 0.16150943396226414, 0.3886792452830189, 0.5962264150943396,
				0.729811320754717, 0.6430188679245283, 0.40226415094339624, 0.19018867924528302,
				0.12830188679245283, 0.19547169811320755, 0.289811320754717, 0.32075471698113206,
				0.43924528301886795, 0.3350943396226415, 0.5449056603773584, 0.7660377358490567,
				0.8067924528301886, 0.6935849056603773, 0.4377358490566038, 0.24528301886792453,
				0.14264150943396225, 0.20452830188679244, 0.3411320754716981, 0.5064150943396226,
				0.6271698113207547, 0.6732075471698113, 0.6264150943396226, 0.43018867924528303,
				0.21056603773584906, 0.09811320754716982, 0.08226415094339623, 0.16754716981132076,
				0.3479245283018868, 0.4935849056603774, 0.5652830188679245, 0.5962264150943396,
				0.5735849056603773, 0.5071698113207547, 0.5358490566037736, 0.0709433962264151,
				0.0030188679245283017, 0.5207547169811321, 0.0 };

		GrayBuffer pixelBuffer = new GrayBuffer(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// BufferedImage's origin is top-left and we want GrayBuffer to have bottom-left so
				// the internal byte-array mirrors the image for debug purposes.
				Color pxColor = new Color(img.getRGB(x, height - (y + 1)));
				pixelBuffer.set(x, y, PixelColor.colorToGrayscale(pxColor));
			}
		}

		double[] bandFunc = Characterizer.calculateBandFunction(pixelBuffer);
		assertEquals(referenceFunc.length, bandFunc.length);
		for (int i = 0; i < referenceFunc.length; i++) {
			assertEquals(String.format("For index %s", i), referenceFunc[i], bandFunc[i],
					fpThreshold);
		}
	}
}
