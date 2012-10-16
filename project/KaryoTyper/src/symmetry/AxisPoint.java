package symmetry;

import java.awt.Point;

public class AxisPoint extends Point {

	private double tangentSlope;
	private boolean noTanSlope;
	private double tangentYIntercept;
	private double orthogonalSlope;
	private boolean noOrthoSlope;
	private double orthogonalYIntercept;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param tangentSlope
	 * @param tangentYIntercept
	 */
	public AxisPoint(int x, int y, double tangentSlope, double tangentYIntercept) {
		super(x, y);
		this.tangentSlope = tangentSlope;
		this.noTanSlope = tangentSlope == Integer.MAX_VALUE ? true : false;
		this.tangentYIntercept = tangentYIntercept;
		setOrthogonalLine();
		
	}
	
	public AxisPoint(int x, int y, boolean noTanSlope) {
		super(x, y);
		this.tangentSlope = Integer.MAX_VALUE;
		this.noTanSlope = noTanSlope;
		this.tangentYIntercept = x;
		setOrthogonalLine();
	}
	
	public AxisPoint (Point point, double tangentSlope, double tangentYIntercept) {
		this(point.x, point.y, tangentSlope, tangentYIntercept);
	}
	
	public AxisPoint (Point point, boolean noTanSlope) {
		this(point.x, point.y, noTanSlope);
	}

	public void setOrthogonalLine() {
		try {
			orthogonalSlope = -1/tangentSlope;
			noOrthoSlope = false;
			if (tangentSlope == Integer.MAX_VALUE) {
				orthogonalSlope = 0;
			}
			orthogonalYIntercept = y - orthogonalSlope * x; 
		} catch (ArithmeticException e) {
			orthogonalSlope = Integer.MAX_VALUE;
			noOrthoSlope = true;
			orthogonalYIntercept = x;
		}
	}
}
