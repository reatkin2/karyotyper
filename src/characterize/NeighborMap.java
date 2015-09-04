package characterize;

import java.awt.Point;

public class NeighborMap {
	public enum Position {
		CENTER(0), NORTH(1), NORTHEAST(2), EAST(3), SOUTHEAST(4), SOUTH(5), SOUTHWEST(6), WEST(7), NORTHWEST(
				8);

		// Offsets for getting neighbor pixels in clockwise order starting at 12 o'clock
		private static final int[] xOffset = { 0, 0, 1, 1, 1, 0, -1, -1, -1 };
		private static final int[] yOffset = { 0, 1, 1, 0, -1, -1, -1, 0, 1 };

		public static final int number = 9;
		public final int index;

		private Position(int index) {
			this.index = index;
		}

		public Point getOffset() {
			return new Point(xOffset[this.index], yOffset[this.index]);
		}
	};

	// Order is as defined by Position enum.
	private double[] contribution;

	public NeighborMap() {
		this.contribution = new double[Position.number];
	}

	public double getContribution(Position pos) {
		return this.contribution[pos.index];
	}

	public void setContribution(Position pos, double percentage) {
		this.contribution[pos.index] = percentage;
	}
}
