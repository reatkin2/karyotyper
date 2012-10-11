package basic_objects;

import java.awt.Point;
import java.awt.geom.Line2D;

public class TangentLine extends Line2D.Float{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double angle2Xaxis;
	public TangentLine(Point point1,Point point2){
		super(point1, point2);
		this.angle2Xaxis=getAngle2Xaxis();
	}
	private double getAngle2Xaxis(){
        double slope1 = getY1() - getY2() / getX1() - getX2();
        return Math.atan(slope1) ;
	}
	
}
