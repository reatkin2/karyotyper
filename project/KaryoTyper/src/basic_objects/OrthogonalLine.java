package basic_objects;

import java.awt.Point;

public class OrthogonalLine {
	private int upperDistance;
	private int lowerDistance;
	private Point upperPoint;
	private Point lowerPoint;
	private Point startPoint;
	private boolean twoLines;
	private int index;

	public OrthogonalLine(Point start,Point upperP, Point lowerP,int upper, int lower,int indexFoundAt){
		startPoint=start;
		this.upperDistance=upper;
		this.lowerDistance=lower;
		this.upperPoint=upperP;
		this.lowerPoint=lowerP;
		twoLines=false;
		index=indexFoundAt;
	}
	public OrthogonalLine(Point start,Point upperP,int upper,int indexFoundAt){
		startPoint=start;
		this.upperDistance=upper;
		this.lowerDistance=-1;
		this.upperPoint=upperP;
		this.lowerPoint=null;
		twoLines=false;
		index=indexFoundAt;
	}

	public double getlength(){
		return this.upperPoint.distance(this.lowerPoint);
	}
	public OrthogonalLine(){

		twoLines=true;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public Point getCenterPoint() {
		return startPoint;
	}
	public void setCenterPoint(Point centerPoint) {
		this.startPoint = centerPoint;
	}
	public double getUpperDistance() {
		return this.upperPoint.distance(this.startPoint);
	}

	public void setUpperDistance(int upperDistance) {
		this.upperDistance = upperDistance;
	}

	public double getLowerDistance() {
		return this.lowerPoint.distance(this.startPoint);
	}
	public int getLowerInt() {
		return this.lowerDistance;
	}
	public int getUpperInt() {
		return this.upperDistance;
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
