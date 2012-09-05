package basicObjects;

import java.awt.Point;

public class AroundPixel {
	private Point aroundDot[];
	public AroundPixel(){
		aroundDot=new Point[8];
        aroundDot[0]=new Point(0,-1);//top middle
        aroundDot[1]=new Point(-1,-1);//top left
        aroundDot[2]=new Point(-1,0);//middle left
        aroundDot[3]=new Point(-1,1);//bottom left
        aroundDot[4]=new Point(0,1);//bottom middle
        aroundDot[5]=new Point(1,1);//bottom right
        aroundDot[6]=new Point(1,0);//middle right
        aroundDot[7]=new Point(1,-1);//top right

	}
	public Point getPos(int pos){
		return aroundDot[pos];
	}
}
