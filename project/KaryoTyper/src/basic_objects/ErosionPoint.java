package basic_objects;

import java.awt.Point;

public class ErosionPoint extends Point {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean before;

	public ErosionPoint(int x, int y, boolean beforeCluster) {
		super(x, y);
		before = beforeCluster;
	}

	public boolean isBefore() {
		return before;
	}

	public boolean equals(ErosionPoint comparePoint) {
		return super.equals(comparePoint);
	}

}
