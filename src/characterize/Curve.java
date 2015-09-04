package characterize;

import java.awt.geom.Point2D;

public abstract class Curve {
	protected Interval interval;
	
	public Interval getInterval() {
		return this.interval.copy();
	}

	public void setInterval(Interval domain) {
		this.interval = domain.copy();
	}
	
	public abstract Point2D value(double t) throws Exception;
	public abstract Curve copy();
}
