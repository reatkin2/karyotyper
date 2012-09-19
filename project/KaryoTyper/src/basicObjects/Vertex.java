package basicObjects;

import java.awt.Point;
import java.util.LinkedList;



public class Vertex {
	private LinkedList<Vertex> children;
	private Point myVertex;
	public Vertex(Point vertex){
		myVertex=new Point(vertex);
		children=new LinkedList<Vertex>();
	}
	public void addChild(Vertex nextChild){
		if(!children.contains(nextChild)&&!nextChild.equals(myVertex)){
			children.add(nextChild);
		}
	}
	public boolean isChild(Vertex checkPoint){
		if(children.contains(checkPoint)){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean isIntersection(){
		if(children.size()>2){
			return true;
		}
		return false;
	}
	public boolean checkAdjacent(Vertex checkPoint){
		//TODO(Andrew): change this for effiecience do this in shape without calculating distance
		if(isChild(checkPoint)){
			return true;
		}
		else if(checkPoint.myVertex.distance(myVertex)<1.5){
			addChild(checkPoint);
			return true;
		}
		return false;
	}
	public Point getPoint(){
		return myVertex;
	}
	public int numberOfChildren(){
		return this.children.size();
	}
	public LinkedList<Vertex> getChildren(){
		return children;
	}
}
