package basic_objects;

import java.awt.Point;
import java.util.LinkedList;

public class PointList {
	private LinkedList<Point> pointList;
	private int distanceFromEdge;
	public PointList(){
		pointList=null;
		distanceFromEdge=-1;
	}
	public PointList(Point tempPoint,int distance){
		distanceFromEdge=distance;
		pointList=new LinkedList<Point>();
		pointList.add(tempPoint);
	}
	public void setList(PointList tempList){
		pointList=tempList.getList();
		distanceFromEdge=tempList.getDistanceFromEdge();
	}
	public boolean addPoint(Point tempPoint, int distance){
		if(distance==distanceFromEdge){
			pointList.add(tempPoint);
			return true;
		}
		return false;
	}
	public LinkedList<Point> getList(){
		return pointList;
	}
	public int getDistanceFromEdge() {
		return distanceFromEdge;
	}

}
