package characterize;

import java.awt.Point;
import java.util.List;

public class GrayBuffer {
	private int[][] buffer;
	public final int width;
	public final int height;

	private final int MAX_VALUE = 255;
	private final int MIN_VALUE = -1;

	public GrayBuffer(int width, int height) {
		this.buffer = new int[width][height];
		this.width = width;
		this.height = height;
	}

	public int get(int x, int y) {
		return buffer[x][y];
	}

	public int get(Point p) {
		return this.get(p.x, p.y);
	}

	public int[] getList(List<Point> points) {
		int[] result = new int[points.size()];

		int i = 0;
		for (Point p : points) {
			result[i] = this.get(p);
			i++;
		}

		return result;
	}

	public int[] getList(Point[] points) {
		int[] result = new int[points.length];

		for (int i = 0; i < points.length; i++) {
			result[i] = this.get(points[i]);
		}

		return result;
	}

	public void set(int x, int y, int value) {
		if (value > MAX_VALUE || value < MIN_VALUE) {
			throw new IllegalArgumentException(String.format(
					"Value being set must be a member of [%d, %d].", MIN_VALUE, MAX_VALUE));
		}

		buffer[x][y] = value;
	}

	public void set(Point p, int value) {
		this.set(p.x, p.y, value);
	}
}
