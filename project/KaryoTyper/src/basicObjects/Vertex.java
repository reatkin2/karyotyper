package basicObjects;

import java.awt.Point;
import java.util.LinkedList;



public class Vertex {
	private LinkedList<Point> children;
	private Point myVertex;
	public Vertex(Point vertex){
		myVertex=new Point(vertex);
		children=new LinkedList<Point>();
	}
	public void addChild(Point nextChild){
		if(!children.contains(nextChild)){
			children.add(nextChild);
		}
	}
	public boolean isChild(Point checkPoint){
		if(children.contains(checkPoint)){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean checkAdjacent(Point checkPoint){
		//TODO(Andrew): change this for effiecience do this in shape without calculating distance
		if(isChild(checkPoint)){
			return true;
		}
		else if(checkPoint.distance(myVertex)<1.5){
			addChild(checkPoint);
			return true;
		}
		return false;
	}
	public Point getPoint(){
		return myVertex;
	}
}
