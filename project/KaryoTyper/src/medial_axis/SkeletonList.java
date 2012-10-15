package medial_axis;

import java.awt.Point;
import java.util.LinkedList;

import basic_objects.PointList;
import basic_objects.Vertex;

public class SkeletonList {
	private LinkedList<PointList> skeleton;
	private boolean hasEdgeDistance[];
	private int size;
	public SkeletonList(){
		size=0;
		initHED();
		skeleton=new LinkedList<PointList>();
	}
	public SkeletonList(LinkedList<Vertex> vertexList){
		size=0;
		initHED();
		skeleton=new LinkedList<PointList>();
		buildFromVertexList(vertexList);
	}
	private void buildFromVertexList(LinkedList<Vertex> vertexList){
		for(int i=0;i<vertexList.size();i++){
			add(vertexList.get(i).getPoint(),vertexList.get(i).getDistanceFromEdge());
		}
	}
	private void initHED(){
		hasEdgeDistance=new boolean[300];
		for(int i=0;i<300;i++){
			hasEdgeDistance[i]=false;
		}
	}
	public LinkedList<Point> getOneList(){
		LinkedList<Point> tempList=new LinkedList<Point>();
		//start at 2 so as not to give what was previosly being trashed
		//in create medailAxis method in medialAxis class
		for(int i=2;i<hasEdgeDistance.length;i++){
			if(this.hasEdgeDistance[i]){
				for(int j=0;j<skeleton.get(i).getList().size();j++){
					tempList.add(skeleton.get(i).getList().get(j));
				}
			}
		}
		return tempList;
	}
	public void add(Point tempPoint,int distance){
		if(distance>=0){
			if(skeleton.size()<distance){
				for(int i=0;i<distance;i++){
					skeleton.add(new PointList());
				}
			}
			if(!hasEdgeDistance[distance]){
				hasEdgeDistance[distance]=true;
				if(skeleton.size()>distance){
					skeleton.get(distance).setList(new PointList(tempPoint,distance));
				}
				skeleton.add(new PointList(tempPoint,distance));
			}
			else{
				skeleton.get(distance).addPoint(tempPoint, distance);
			}
			size++;
		}
	}
	public PointList getListAtDistance(int distance){
		if(this.hasEdgeDistance[distance]){
			return skeleton.get(distance);
		}
		return null;
	}
	public int size(){
		return this.size;
		
	}
	public boolean contains(Point tempPoint){
		for(int i=0;i<this.hasEdgeDistance.length;i++){
			if(this.hasEdgeDistance[i]){
				if(this.skeleton.get(i).getList().contains(tempPoint)){
					return true;
				}
			}
		}
		return false;
	}
}
