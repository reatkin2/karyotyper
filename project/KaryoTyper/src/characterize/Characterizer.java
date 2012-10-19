package characterize;

import java.awt.Point;
import java.util.ArrayList;

public class Characterizer {
	// Offsets for getting neighbor pixels in clockwise order starting at 12 o'clock
	public static final int[] xOffset = { 0, 1, 1, 1, 0, -1, -1, -1 };
	public static final int[] yOffset = { 1, 1, 0, -1, -1, -1, 0, 1 };

	private static double[] normalizeVector(double xComp, double yComp) {
		double magnitude = Math.sqrt((xComp * xComp) + (yComp * yComp));
		
		if (magnitude != 0) {
			xComp = xComp / magnitude;
			yComp = yComp / magnitude;
		} else {
			xComp = 0;
			yComp = 0;
		}

		double[] unit = { xComp, yComp };
		return unit;
	}

	private static double[] normalizeVector(double[] vec) {
		return normalizeVector(vec[0], vec[1]);
	}

	private static double[] orthogonalVector(double xComp, double yComp, boolean isRight) {
		/*
		double[] result = new double[2];
		if (isRight) {
			result[0] = ;
			result[1] = ;
		} else {
			result[0] = ;
			result[1] = ;
		}
		return result;
		*/
		double[] result = {xComp, yComp};
		return result;
	}

	private static double[] orthoNormalVector(double xComp, double yComp, boolean isRight) {
		return normalizeVector(orthogonalVector(xComp, yComp, isRight));
	}

	/**
	 * 
	 * 
	 * Note that NaN represents a vertical slope.
	 * 
	 * @param chromBuffer
	 * @param chromMidline
	 * @return
	 */
	public static double[][][] buildSlopeBuffer(GrayBuffer chromBuffer,
			ArrayList<Point> chromMidline) {
		if (chromMidline.size() < 2) {
			throw new IllegalArgumentException(
					"Chromosome cannot be linearized without a valid midline/medial axis.");
		}

		int width = chromBuffer.width;
		int height = chromBuffer.height;

		// Create a buffer of cross-slope information. E.g. The slope orthogonal to the midline of
		// the chromosome.
		ArrayList<Point> currentFrontier = new ArrayList<Point>();
		ArrayList<Point> nextFrontier = new ArrayList<Point>();

		// A unit-vector field representing slopes (useful later, and provides representation for
		// infinite slope). 0-index represents x-offset and 1-index represents y-offset.
		double[][][] slopeField = new double[width][height][2];
		boolean[][] isSet = new boolean[width][height];

		// Pre-set all midline values for slope and initialize frontier
		for (int i = 0; i < chromMidline.size(); i++) {
			Point current = chromMidline.get(i);
			int x = current.x;
			int y = current.y;

			int prevIndex = i - 1;
			if (prevIndex < 0) {
				prevIndex = i;
			}

			Point previous = chromMidline.get(prevIndex);
			double[] prevSlope = normalizeVector((y - previous.y), (x - previous.x));

			int nextIndex = i + 1;
			if (nextIndex > chromMidline.size() - 1) {
				nextIndex = chromMidline.size() - 1;
			}

			Point next = chromMidline.get(nextIndex);
			double[] nextSlope = normalizeVector((next.y - y), (next.x - x));

			isSet[x][y] = true;
			double[] thisSlope = normalizeVector(prevSlope[0] + nextSlope[0] / 2, prevSlope[1]
					+ nextSlope[1] / 2);
			slopeField[x][y] = thisSlope;

			// Initialize the frontier
			// This includes determining on which side a given point is to the "flow" of the median
			for (int j = 0; j < 8; j++) {
				int neighborX = current.x + xOffset[j];
				int neighborY = current.y + yOffset[j];

				if ((neighborX != previous.x || neighborY != previous.y)
						&& (neighborX != next.x || neighborY != next.y)) {
					if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
						if (!isSet[neighborX][neighborY]) {
							currentFrontier.add(new Point(neighborX, neighborY));
						}
					}
				}
			}
		}

		for (Point p : currentFrontier) {
			// Accumulate all the slopes of surrounding cells that were set previously.
			int count = 0;
			double[] slope = { 0, 0 };
			for (int j = 0; j < 8; j++) {
				int neighborX = p.x + xOffset[j];
				int neighborY = p.y + yOffset[j];
				if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
					if (isSet[neighborX][neighborY]) {
						slope[0] += slopeField[neighborX][neighborY][0];
						slope[1] += slopeField[neighborX][neighborY][1];
					}
				}
			}
			
			// Set up initial frontier slope vectors so we have a clean flow pattern
			// orthogonal to and point away from the midline
			// TODO(ahkeslin): actually make this point in the correct direction.
			slopeField[p.x][p.y] = orthoNormalVector(slope[0], slope[1], true);
		}
		
		// Mark all cells as having their slopes set
		for (Point p : currentFrontier) {
			isSet[p.x][p.y] = true;
		}
		
		// Build up next frontier
		for (Point p : currentFrontier) {
			for (int j = 0; j < 8; j++) {
				int neighborX = p.x + xOffset[j];
				int neighborY = p.y + yOffset[j];
				if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
					if (!isSet[neighborX][neighborY]) {
						nextFrontier.add(new Point(neighborX, neighborY));
					}
				}
			}
		}
		
		// Flip frontier buffers
		ArrayList<Point> tempNext = new ArrayList<Point>(nextFrontier.size());
		currentFrontier = nextFrontier;
		nextFrontier = tempNext;

		while (!currentFrontier.isEmpty()) {
			// Set slopes for the current frontier.
			for (Point p : currentFrontier) {
				int x = p.x;
				int y = p.y;

				// If this happens to be set (which should only happen for the initial frontier
				// values)
				if (isSet[x][y]) {
					continue;
				}

				// Accumulate all the slopes of surrounding cells that were set previously.
				int count = 0;
				double[] slope = { 0, 0 };
				for (int j = 0; j < 8; j++) {
					int neighborX = x + xOffset[j];
					int neighborY = y + yOffset[j];
					if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
						if (isSet[neighborX][neighborY]) {
							slope[0] += slopeField[neighborX][neighborY][0];
							slope[1] += slopeField[neighborX][neighborY][1];
						}
					}
				}

				// Be sure to average out all contributing vector components
				slope[0] /= count;
				slope[1] /= count;
				slopeField[x][y] = normalizeVector(slope);
			}

			// Though it means that we have to walk the frontier three times, we must do so to
			// assure slopes are only informed by cells closer to the midline and that we only add
			// new cells to the frontier.
			
			// Mark all cells as having their slopes set
			for (Point p : currentFrontier) {
				isSet[p.x][p.y] = true;
			}
			
			// Build up next frontier
			for (Point p : currentFrontier) {
				for (int j = 0; j < 8; j++) {
					int neighborX = p.x + xOffset[j];
					int neighborY = p.y + yOffset[j];
					if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
						if (!isSet[neighborX][neighborY]) {
							nextFrontier.add(new Point(neighborX, neighborY));
						}
					}
				}
			}

			// Flip frontier buffers
			tempNext = new ArrayList<Point>(nextFrontier.size());
			currentFrontier = nextFrontier;
			nextFrontier = tempNext;
		}

		return slopeField;
	}

	public static GrayBuffer linearizeChromosome(GrayBuffer chromBuffer,
			ArrayList<Point> chromMidline) {

		/*
		 * GRAND IDEA
		 * 
		 * Copy over medial axis first and then copy "adjacent" pixels into the spots adjacent to
		 * those already copied. In this way we do not need to compute cross-sections at varying
		 * slopes, but instead may interpolate pixels that claim to occupy the same spot.
		 * 
		 * Take as example two pixels at top-right to lower-left diagonals, call the lower L and
		 * higher H and consider the copy operation to occur from right diagonal to left diagonal.
		 * In this fashion H will be on top of L in the linearized image and H(x-1), H(y+1), and
		 * H(x-1, y+1) all occur to the left of H in the linearized image with L(x-1), L(y+1), and
		 * L(x-1, y+1) being to the immediate left of L in the linearized image. If we purely
		 * average all the pixels we are adding entropy to the system... which in the case of
		 * low-resolution imagery is unacceptable, thus we must find a solution which does this as
		 * little as possible.
		 */

		if (chromMidline.size() < 2) {
			throw new IllegalArgumentException(
					"Chromosome cannot be linearized without a valid midline/medial axis.");
		}

		int width = chromBuffer.width;
		int height = chromBuffer.height;

		GrayBuffer linearized = new GrayBuffer(width, height);

		return linearized;
	}
}
