package characterize;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

public class CharacterizerTest extends TestCase {
	private final double fpThreshold = 0.0001;

	public void testBuildSlopeBuffer() {

		/* First: Test a basic linear buffer ************************************************ */
		int[] linearEnds = { -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		int[] linearMiddle = { -1, 0, 0, 1, 1, 1, 1, 0, -1 };
		int[][] alreadyLinear = new int[linearEnds.length][20];
		for (int row = 0; row < alreadyLinear.length; row++) {
			if (row == 0 || row == alreadyLinear.length) {
				alreadyLinear[row] = linearEnds;
			} else {
				alreadyLinear[row] = linearMiddle;
			}
		}

		GrayBuffer simpleLinear = new GrayBuffer(alreadyLinear[0].length, alreadyLinear.length);
		for (int y = 0; y < alreadyLinear.length; y++) {
			for (int x = 0; x < alreadyLinear[0].length; x++) {
				simpleLinear.set(x, y, alreadyLinear[y][x]);
			}
		}

		// Generate midline
		Point[] alreadyLinearMidline = new Point[alreadyLinear.length - 2];
		for (int y = 1; y < alreadyLinear.length - 1; y++) {
			alreadyLinearMidline[y - 1] = new Point(4, y);
		}

		ArrayList<Point> simpleLinearMidline = new ArrayList<Point>(
				Arrays.asList(alreadyLinearMidline));
		double[][][] slopeBuffer = Characterizer.buildSlopeBuffer(simpleLinear, simpleLinearMidline);

		double[] expected = {0, -1};
		for (int y = 0; y < simpleLinear.height; y++) {
			for (int x = 0; x < simpleLinear.width; x++) {
				// All slopes should be effectively zero as midline is vertical
				double[] slope = slopeBuffer[x][y];
				assertTrue(String.format(
						"Expected value of (%s) for x at index (%s, %s), but instead received (%s)",
						expected[0], x, y, slope[0]), slope[0] < expected[0] + fpThreshold
						&& slope[0] > expected[0] - fpThreshold);
				assertTrue(String.format(
						"Expected value of (%s) for y at index (%s, %s), but instead received (%s)",
						expected[1], x, y, slope[1]), slope[1] < expected[1] + fpThreshold
						&& slope[1] > expected[1] - fpThreshold);
			}
		}
	}
}
