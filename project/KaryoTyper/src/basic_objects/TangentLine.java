package basic_objects;

import java.awt.Point;
import java.awt.geom.Line2D;

public class TangentLine extends Line2D.Float{
	private double previosAngle;
	private double followingAngle;
	public TangentLine(Point point1,Point point2,double prevAngle){
		super(point1, point2);
		previosAngle=prevAngle;
		followingAngle=-444;
	}
	public double getAngleBetween(Line2D.Float lineX){
        double slope1 = getY1() - getY2() / getX1() - getX2();
        double slope2 = lineX.getY1() - lineX.getY2() / lineX.getX1() - lineX.getX2();
        double angle = Math.atan((slope1 - slope2) / (1 - (slope1 * slope2)));
        return angle;
	}
	
}
