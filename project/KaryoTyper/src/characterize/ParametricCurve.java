package characterize;

import java.awt.geom.Point2D;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;

public class ParametricCurve extends Curve {
	private DifferentiableUnivariateFunction function;

	public ParametricCurve(Interval domain, DifferentiableUnivariateFunction function) {
		this.setInterval(domain);
		this.function = function;
	}
	
	public ParametricCurve(double start, double end, DifferentiableUnivariateFunction function) {
		this(new Interval(start, end), function);
	}

	@Override
	public Point2D value(double t) {
		if (!(this.interval.getLeft() < t && t < this.interval.getRight())) {
			throw new IllegalArgumentException(
					"Curves may only be evaluated on the interval for which they are defined.");
		}
		return new Point2D.Double(t, this.function.value(t));
	}

	@Override
	public ParametricCurve copy() {
		Interval newDomain = this.interval.copy();
		ParametricCurve newCurve = new ParametricCurve(newDomain, this.function);
		return newCurve;
	}
}