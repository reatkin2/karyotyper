package basic_objects;

import java.awt.Point;
import java.util.LinkedList;

public class PointList {
	private LinkedList<Point> pointList;
	private int distanceFromEdge;
	private Point axisPoint;
	private int cutDir1;
	private int cutDir2;
	private int dir1length;
	private int dir2length;

	public PointList() {
		axisPoint = new Point(-1, -1);
		pointList = null;
		distanceFromEdge = -1;
	}

	public PointList(Point tempPoint, int distance) {
		distanceFromEdge = distance;
		pointList = new LinkedList<Point>();
		pointList.add(tempPoint);
	}
	public PointList(Point tempPoint, int distance,int dir1,int dir2,int dir1Len,int dir2Len) {
		distanceFromEdge = distance;
		pointList = new LinkedList<Point>();
		pointList.add(tempPoint);
		cutDir1=dir1;
		cutDir2=dir2;
		dir1length=dir1Len;
		dir2length=dir2Len;

	}

	public void setList(PointList tempList) {
		pointList = (LinkedList<Point>) tempList.getList().clone();
		distanceFromEdge = tempList.getDistanceFromEdge();
	}

	public boolean addPoint(Point tempPoint, int distance) {
		if (distance == distanceFromEdge) {
			pointList.add(tempPoint);
			return true;
		}
		return false;
	}

	public LinkedList<Point> getList() {
		return pointList;
	}

	public int getDistanceFromEdge() {
		return distanceFromEdge;
	}

	public Point getAxisPoint() {
		return axisPoint;
	}

	public void setAxisPoint(Point axisPoint) {
		this.axisPoint = axisPoint;
	}
	public int getCutDir1() {
		return cutDir1;
	}

	public void setCutDir1(int cutDir1) {
		this.cutDir1 = cutDir1;
	}

	public int getCutDir2() {
		return cutDir2;
	}

	public void setCutDir2(int cutDir2) {
		this.cutDir1 = cutDir2;
	}
	public int getDir1length() {
		return dir1length;
	}

	public void setDir1length(int dir1length) {
		this.dir1length = dir1length;
	}

	public int getDir2length() {
		return dir2length;
	}

	public void setDir2length(int dir2length) {
		this.dir2length = dir2length;
	}


}
