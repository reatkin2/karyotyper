package characterize;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

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

	public void testLinearizeChromosome() {

	}
}
