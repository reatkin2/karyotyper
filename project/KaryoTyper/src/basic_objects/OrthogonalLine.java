package basic_objects;

import java.awt.Point;

public class OrthogonalLine {
	private int upperDistance;
	private int lowerDistance;
	private Point upperPoint;
	private Point lowerPoint;
	private Vertex centerPoint;
	private boolean twoLines;

	public OrthogonalLine(Vertex center,Point upperP, Point lowerP,int upper, int lower){
		centerPoint=center;
		this.upperDistance=upper;
		this.lowerDistance=lower;
		this.upperPoint=upperP;
		this.lowerPoint=lowerP;
		twoLines=false;
	}
	public double getlength(){
		return this.upperPoint.distance(this.lowerPoint);
	}
	public OrthogonalLine(){

		twoLines=true;
	}
	
	public Vertex getCenterPoint() {
		return centerPoint;
	}
	public void setCenterPoint(Vertex centerPoint) {
		this.centerPoint = centerPoint;
	}
	public int getUpperDistance() {
		return upperDistance;
	}

	public void setUpperDistance(int upperDistance) {
		this.upperDistance = upperDistance;
	}

	public int getLowerDistance() {
		return lowerDistance;
	}

	public void setLowerDistance(int lowerDistance) {
		this.lowerDistance = lowerDistance;
	}
	public Point getUpperPoint() {
		return upperPoint;
	}

	public void setUpperPoint(Point upperPoint) {
		this.upperPoint = upperPoint;
	}

	public Point getLowerPoint() {
		return lowerPoint;
	}

	public void setLowerPoint(Point lowerPoint) {
		this.lowerPoint = lowerPoint;
	}
	public boolean isTwoLines() {
		return twoLines;
	}

	public void setTwoLines(boolean twoLines) {
		this.twoLines = twoLines;
	}



}
