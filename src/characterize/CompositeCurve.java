package characterize;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CompositeCurve extends Curve {
	private Interval interval;
	private List<Curve> subCurves;

	public CompositeCurve(Interval domain, LinkedList<Curve> subCurves) {
		this.setInterval(domain);
		this.subCurves = new LinkedList<Curve>();
		
		for (Curve sub : subCurves) {
			this.subCurves.add(sub);
		}
	}
	
	public CompositeCurve(Interval domain) {
		this(domain, new LinkedList<Curve>());
	}

	public CompositeCurve(double start, double end) {
		this(new Interval(start, end));
	}

	public CompositeCurve() {
		this(0, 0);
	}

	public CompositeCurve copy() {
		CompositeCurve newCurve = new CompositeCurve(this.interval);
		List<Curve> newSubCurves = new LinkedList<Curve>();
		for (Curve c : this.subCurves) {
			newSubCurves.add(c.copy());
		}

		newCurve.subCurves = newSubCurves;
		return newCurve;
	}

	public void addSubCurve(CompositeCurve newCurve) throws Exception {
		Interval newInterval = newCurve.getInterval();

		// TODO(ahkeslin): Do this intersection search more efficiently....
		switch (this.interval.checkIntersection(newInterval)) {
		case LEFT:
			for(Curve sub : this.subCurves) {
				if(sub.getInterval().inInterval(newInterval.getLeft())) {
					sub.setInterval(sub.getInterval().truncateInterval(newCurve.getInterval()));
					this.subCurves.add(0, newCurve);
					break;
				} else {
					this.subCurves.remove(sub);
				}
			}
			break;
		case RIGHT:
			// Figure out which subCurves this overlaps
			break;
		case SPLIT:
			// Figure out which subCurves this overlaps
			break;
		case CONTAINED:
			this.subCurves = new LinkedList<Curve>(Arrays.asList(newCurve));
			this.setInterval(newInterval);
			break;
		default:
			throw new Exception("New sub-curve does not overlap existing interval.");
		}
	}

	public Point2D value(double t) throws Exception {
		if (this.interval.inInterval(t)) {
			for (Curve sub : this.subCurves) {
				if (this.interval.inInterval(t)) {
					return sub.value(t);
				}
			}
		} else {
			throw new IllegalArgumentException(
					"Value must be within the interval defining this curve.");
		}

		throw new Exception(
				"Undefined state reached: Value was in CompositeCurve interval, but not in a subcurve's interval.");
	}
}