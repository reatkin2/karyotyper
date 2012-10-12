package basic_objects;

import java.util.LinkedList;

public class GraphSegment {
	private LinkedList<Vertex> segment;
	private LinkedList<Vertex> intersections;
	private int segID;
	public GraphSegment(int seg){
		segID=seg;
		segment=new LinkedList<Vertex>();
		intersections=new LinkedList<Vertex>();

	}
	public GraphSegment(LinkedList<Vertex> tempSegment){
		segID=-1;
		segment=tempSegment;
		intersections=new LinkedList<Vertex>();

	}
	public void addIntersection(Vertex tempIntersection){
		intersections.add(tempIntersection);
	}
	public void addVertex(Vertex tempVertex){
		segment.add(tempVertex);
	}
	public LinkedList<Vertex> getSegment(){
		return segment;
	}
	public int getIntersectionCount(){
		return intersections.size();
	}
	public int getSegmentLength(){
		return segment.size();
	}
	public int getSegID() {
		return segID;
	}
	public void setSegID(int segID) {
		this.segID = segID;
	}

}
