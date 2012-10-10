package basic_objects;

import java.util.LinkedList;

public class GraphSegment {
	private LinkedList<Vertex> segment;
	private LinkedList<Vertex> intersections;
	public GraphSegment(LinkedList<Vertex> tempSegment){
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
}
