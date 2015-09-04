package characterize;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Interval {
	public static enum Intersection {
		RIGHT, LEFT, SPLIT, CONTAINED, NONE
	};

	private double left;
	private double right;
	
	public Interval(double left, double right) {
		this.left = left;
		this.right = right;
	}
	
	public Interval(Interval template) {
		this(template.getLeft(), template.getRight());
	}
	
	public Interval() {
		this(0, 0);
	}

	public boolean inInterval(double x) {
		if (this.left <= x && x <= this.right) {
			return true;
		} else {
			return false;
		}
	}

	public Interval truncateInterval(Interval intersector) {
		Interval result = new Interval();

		if (this.left < intersector.right && intersector.right < this.right) {
			result.setRight(this.left);
		} else if (this.left < intersector.right && intersector.right < this.right) {
			result.setLeft(this.right);
		}

		return result;
	}

	public double getLeft() {
		return this.left;
	}

	public double getRight() {
		return this.right;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public void setRight(double right) {
		this.right = right;
	}

	public List<Interval> splitInterval(Interval splitter) {
		Interval leftInterval = this.copy();
		Interval rightInterval = this.copy();
		rightInterval.setLeft(splitter.getRight());
		leftInterval.setRight(splitter.getLeft());
		return new LinkedList<Interval>(Arrays.asList(leftInterval, splitter, rightInterval));
	}

	public Interval copy() {
		return new Interval(this);
	}

	public Intersection checkIntersection(Interval intersector) {
		// Check if newInterval contains targetInterval
		if (intersector.left <= this.left && this.right <= intersector.right) {
			return Intersection.CONTAINED;

		} else {
			boolean leftContained = false;
			boolean rightContained = false;

			if (this.left <= intersector.left && intersector.left <= this.right) {
				leftContained = true;
			}

			if (this.left <= intersector.right && intersector.right <= this.right) {
				rightContained = true;
			}

			if (leftContained) {
				if (rightContained) {
					return Intersection.SPLIT;
				} else {
					return Intersection.LEFT;
				}
			} else {
				if (rightContained) {
					return Intersection.LEFT;
				} else {
					return Intersection.NONE;
				}
			}
		}
	}
}
