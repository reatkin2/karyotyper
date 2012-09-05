package basicObjects;

import java.awt.Color;
import java.awt.Point;

public class PixelPoint {
	private short prevColor[];
	private short imgPoint[];
	private short colorLeft[];
	public PixelPoint(){
		prevColor=new short[3];
		prevColor[0]=0;
		prevColor[1]=0;
		prevColor[2]=0;
		imgPoint=new short[2];
		imgPoint[0]=-1;
		imgPoint[1]=-1;
				//Point(-1,-1);
		colorLeft=new short[3];
		colorLeft[0]=0;
		colorLeft[1]=0;
		colorLeft[2]=0;
		//Color(0,0,0);
	}
	public PixelPoint(Point pointX,Color prev,Color left){
		prevColor=new short[3];
		imgPoint=new short[2];
		colorLeft=new short[3];

		prevColor[0]=(short)prev.getRed();
		prevColor[1]=(short)prev.getGreen();
		prevColor[2]=(short)prev.getBlue();
		colorLeft[0]=(short)left.getRed();
		colorLeft[1]=(short)left.getGreen();
		colorLeft[2]=(short)left.getBlue();
		imgPoint[0]=(short)pointX.x;
		imgPoint[1]=(short)pointX.y;
		
	}
	public Color getPrevColor(){
		return new Color(prevColor[0],prevColor[1],prevColor[2]);
	}
	public Color getColorLeft(){
		return new Color(colorLeft[0],colorLeft[1],colorLeft[2]);
	}
	public Point getImgPoint(){
		return new Point(imgPoint[0],imgPoint[1]);
	}
	public void setImgPoint(int x,int y){
		imgPoint[0]=(short)x;
		imgPoint[1]=(short)y;
	}
	public void setColorLeft(Color temp){
		colorLeft[0]=(short)temp.getRed();
		colorLeft[1]=(short)temp.getGreen();
		colorLeft[2]=(short)temp.getBlue();
	}
	public void setPrevColor(Color temp){
		prevColor[0]=(short)temp.getRed();
		prevColor[1]=(short)temp.getGreen();
		prevColor[2]=(short)temp.getBlue();

	}

}
