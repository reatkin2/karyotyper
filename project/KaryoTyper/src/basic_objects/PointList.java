package basic_objects;

import java.awt.Point;
import java.util.LinkedList;

public class PointList {
	private LinkedList<Point> pointList;
	private int distanceFromEdge;
	private Point axisPoint;

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

}
