package characterize;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Characterizer {
	private enum Direction {
		LEFT, RIGHT, INLINE
	};

	public static double FLOATING_POINT_THRESHOLD = 0.0001;

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

	/**
	 * Given a 2D vector, provide an orthogonal vector in the desired direction (or the original
	 * vector in the case of INLINE as direction).
	 * 
	 * @param xComp
	 *            The x component of the origin vector.
	 * @param yComp
	 *            The y component of the origin vector.
	 * @param dir
	 *            Which direction to point the orthogonal vector (LEFT or RIGHT). This has a special
	 *            caveat of INLINE for the case where an "orthogonal" vector is being generated for
	 *            a cell which is inline with the medial axis.
	 * @return The vector "orthogonal" in the desired direction.
	 */
	private static double[] orthogonalVector(double xComp, double yComp, Direction dir) {
		// Effectively computes the cross-product of the vector with an appropriate vector for
		// generating right or left orthogonal
		// B: [Right: <0, 0, 1>, Left: <0, 0, -1>]
		double[] result = new double[2];
		switch (dir) {
		case RIGHT:
			// (a_2*b_3 - a_3*b_2)*i
			result[0] = yComp;
			// (a_3*b_1 - a_1*b_3)*j
			result[1] = -xComp;
			break;
		case LEFT:
			// (a_2*b_3 - a_3*b_2)*i
			result[0] = -yComp;
			// (a_3*b_1 - a_1*b_3)*j
			result[1] = xComp;
			break;
		case INLINE:
			// This is inline with the vector... so don't orthogonalize it
			result[0] = xComp;
			result[1] = yComp;
			break;
		}
		return result;
	}

	/**
	 * Figure out if the comparing vector is to the left, right, or inline with the midline vector.
	 * 
	 * @param midlineSlope
	 *            A 2D vector representing the slope of the midline at this point.
	 * @param compareSlope
	 *            A 2D vector representing the slope of a vector running between the midline point
	 *            and the an adjacent point.
	 * @return The direction which this point is relative to the midline.
	 */
	private static Direction flowDirection(double[] midlineSlope, double[] compareSlope) {
		// (a_1*b_2 - a_2*b_1)*k
		double zComp = midlineSlope[0] * compareSlope[1] - midlineSlope[1] * compareSlope[0];
		if (zComp > 0 + FLOATING_POINT_THRESHOLD) {
			return Direction.LEFT;
		} else if (zComp < 0 - FLOATING_POINT_THRESHOLD) {
			return Direction.RIGHT;
		} else {
			return Direction.INLINE;
		}
	}

	private static double[] orthoNormalVector(double xComp, double yComp, Direction dir) {
		return normalizeVector(orthogonalVector(xComp, yComp, dir));
	}

	/**
	 * Constructs a two dimensional buffer of 2D unit vectors representing slopes orthogonal to the
	 * medial axis unless it is inline with the medial axis.
	 * 
	 * @param chromBuffer
	 *            Two dimensional buffer of pixel values for the specific chromosome.
	 * @param chromMidline
	 *            Points representing the medial axis as a single in-order list.
	 * @return 2D buffer of 2D Points (thus the 3D double array) with each cell representing a
	 *         unit-vector of the direction of the field at that point.
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
			double[] prevSlope = normalizeVector((x - previous.x), (y - previous.y));

			int nextIndex = i + 1;
			if (nextIndex > chromMidline.size() - 1) {
				nextIndex = chromMidline.size() - 1;
			}

			Point next = chromMidline.get(nextIndex);
			double[] nextSlope = normalizeVector((next.x - x), (next.y - y));

			isSet[x][y] = true;
			double[] thisSlope = normalizeVector((prevSlope[0] + nextSlope[0]) / 2,
					(prevSlope[1] + nextSlope[1]) / 2);
			slopeField[x][y] = thisSlope;

			// Initialize the frontier
			// This includes determining on which side a given point is to the "flow" of the median
			for (int j = 0; j < 8; j++) {
				int neighborX = current.x + xOffset[j];
				int neighborY = current.y + yOffset[j];

				// TODO(ahkeslin): Refactor this so it doesn't add the same point more than once
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
			double[] accSlope = { 0, 0 };
			// This is to handle figuring where we are in relation to the midline
			double[] adjacencySlope = { 0, 0 };
			for (int j = 0; j < 8; j++) {
				int neighborX = p.x + xOffset[j];
				int neighborY = p.y + yOffset[j];
				if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
					if (isSet[neighborX][neighborY]) {
						accSlope[0] += slopeField[neighborX][neighborY][0];
						accSlope[1] += slopeField[neighborX][neighborY][1];
						adjacencySlope[0] = (p.x - neighborX);
						adjacencySlope[1] = (p.y - neighborY);
						count++;
					}
				}
			}

			// Set up initial frontier slope vectors so we have a clean flow pattern
			// orthogonal to and point away from the midline
			// TODO(ahkeslin): actually make this point in the correct direction.
			accSlope[0] /= count;
			accSlope[1] /= count;
			slopeField[p.x][p.y] = orthoNormalVector(accSlope[0], accSlope[1],
					flowDirection(accSlope, adjacencySlope));
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
							count++;
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
						Point newPt = new Point(neighborX, neighborY);
						// TODO(ahkeslin): Create a more efficient lookup structure to prevent
						// linear search from ".contains"
						if (!isSet[neighborX][neighborY] && !nextFrontier.contains(newPt)) {
							nextFrontier.add(newPt);
						}
					}
				}
			}

			// Flip frontier buffers
			tempNext = new ArrayList<Point>();
			currentFrontier = nextFrontier;
			nextFrontier = tempNext;
		}

		return slopeField;
	}

	/**
	 * Print a slope buffer as (x.x, y.y) points.
	 * 
	 * @param buffer
	 *            Slope buffer as defined by buildSlopeBuffer.
	 */
	public static void printSlopeBuffer(double[][][] buffer) {
		for (int y = buffer[0].length - 1; y >= 0; y--) {
			System.out.print('\n');
			for (int x = 0; x < buffer.length; x++) {
				double[] slopeVector = buffer[x][y];
				System.out
						.print(String.format("(%(,.2f, %(,.2f) ", slopeVector[0], slopeVector[1]));
			}
		}
	}

	/**
	 * Given a list of in-order points defining a non-crossing polygon, calculate the area.
	 * 
	 * @param perimeter
	 *            A list of in-order points defining a non-crossing polygon.
	 * @return The area of the polygon.
	 */
	public static double polygonalArea(ArrayList<Point2D> perimeter) {
		if (perimeter.size() < 3) {
			throw new IllegalArgumentException("Perimeter must contain at least 3 points.");
		}

		// Application of Green's thm. Integral(perimeter) = Integral(Area) = 2*Area
		// Integral(perimeter) = 0.5 * sum(x_i*y_{i+1} - x_{i+1}*y_i) with 0=length wrapping
		Point2D prev = perimeter.get(perimeter.size() - 1);
		Point2D next = perimeter.get(0);
		double acc = prev.getX() * next.getY() - prev.getY() * next.getX();
		for (int i = 0; i < perimeter.size() - 1; i++) {
			prev = perimeter.get(i);
			next = perimeter.get(i + 1);
			acc += prev.getX() * next.getY() - prev.getY() * next.getX();
		}
		return 0.5 * Math.abs(acc);
	}

	/**
	 * Given a linearized buffer, sum each row.
	 * 
	 * @param linearizedBuffer
	 *            A pixel buffer of a chromosome linearized along the y-axis
	 * @return An array with each row-sum as a separate point.
	 */
	public static double[] calculateBandFunction(GrayBuffer linearizedBuffer) {
		double[] result = new double[linearizedBuffer.height];
		final int WHITE = 255;
		double max_value = 0;
		double min_value = Integer.MAX_VALUE;

		// Record values
		for (int y = 0; y < linearizedBuffer.height; y++) {
			int acc = 0;
			for (int x = 0; x < linearizedBuffer.width; x++) {
				// Darkness = WHITE - color (since color is 0 for black and 255 for white)
				int value = linearizedBuffer.get(x, y);
				if (value != -1) {
					acc += WHITE - linearizedBuffer.get(x, y);
				}
			}
			result[y] = acc;

			if (acc > max_value) {
				max_value = acc;
			}

			if (acc < min_value) {
				min_value = acc;
			}
		}

		// Normalize values
		max_value = max_value - min_value;
		for (int i = 0; i < result.length; i++) {
			result[i] = (result[i] - min_value) / max_value;
		}

		return result;
	}
	
	public static ArrayList<ArrayList<Point2D>> segmentPath(ArrayList<Point2D> path) {
		ArrayList<ArrayList<Point2D>> result = new ArrayList<ArrayList<Point2D>>();
		return result;
	}

	/**
	 * Take a curved chromosome and linearize it, keeping all chromosome features clear and
	 * equivalent to pre-linearization.
	 * 
	 * @param chromBuffer A pixel-buffer representing 
	 * @param chromMidline
	 * @return
	 */
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

		GrayBuffer linearized = new GrayBuffer((2 * (chromBuffer.width / 2) + 1),
				chromMidline.size());
		// Initialize linearized to unset
		for (int x = 0; x < linearized.width; x++) {
			for (int y = 0; y < linearized.height; y++) {
				linearized.set(x, y, -1);
			}
		}
		double[][][] slopeBuffer = buildSlopeBuffer(chromBuffer, chromMidline);

		for (int row = 0; row < chromMidline.size(); row++) {
			Point medialPoint = chromMidline.get(row);
			Point center = new Point(linearized.width / 2, row);
			linearized.set(center, chromBuffer.get(medialPoint));
			double[] centerSlope = slopeBuffer[medialPoint.x][medialPoint.y];

			Point chromCurrent = new Point(medialPoint);
			Point linearCurrent = new Point(center);
			double[] rightOffset = orthoNormalVector(centerSlope[0], centerSlope[1],
					Direction.RIGHT);
			for (int j = 0; j < linearized.width / 2; j++) {
				int newX = (int) Math.round(chromCurrent.x + rightOffset[0]);
				int newY = (int) Math.round(chromCurrent.y + rightOffset[1]);
				// If we're out of bounds, skip this loop
				if (newX < 0 || newX >= chromBuffer.width || newY < 0 || newY >= chromBuffer.height) {
					continue;
				} else {
					chromCurrent.setLocation(newX, newY);
					linearCurrent.setLocation(linearCurrent.x + 1, row);
					int value = chromBuffer.get(chromCurrent);
					if (value != -1) {
						linearized.set(linearCurrent, value);
						rightOffset = slopeBuffer[chromCurrent.x][chromCurrent.y];
					} else {
						break;
					}
				}
			}

			chromCurrent = new Point(medialPoint);
			linearCurrent = new Point(center);
			double[] leftOffset = orthoNormalVector(centerSlope[0], centerSlope[1], Direction.LEFT);
			for (int j = 0; j < linearized.width / 2; j++) {
				int newX = (int) Math.round(chromCurrent.x + leftOffset[0]);
				int newY = (int) Math.round(chromCurrent.y + leftOffset[1]);
				// If we're out of bounds, skip this loop
				if (newX < 0 || newX >= chromBuffer.width || newY < 0 || newY >= chromBuffer.height) {
					linearized.set(linearCurrent, -1);
					break;
				} else {
					chromCurrent.setLocation(newX, newY);
					linearCurrent.setLocation(linearCurrent.x - 1, row);
					int value = chromBuffer.get(chromCurrent);
					if (value != -1) {
						linearized.set(linearCurrent, value);
						leftOffset = slopeBuffer[chromCurrent.x][chromCurrent.y];
					} else {
						break;
					}
				}
			}
		}

		return linearized;
	}
}