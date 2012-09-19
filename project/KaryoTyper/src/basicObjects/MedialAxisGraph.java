package basicObjects;

import java.awt.Point;
import java.util.LinkedList;

public class MedialAxisGraph {
	LinkedList<Vertex> axisGraph=new LinkedList<Vertex>();
	public MedialAxisGraph(LinkedList<Point> medialAxis){
		axisGraph= new LinkedList<Vertex>(); 
		buildGraph(medialAxis);
	}
	private void buildGraph(LinkedList<Point> medialAxis){
		if(medialAxis!=null){
			for(int i=0;i<medialAxis.size();i++){
				Vertex tempVertex=new Vertex(medialAxis.get(i));
				if(!axisGraph.contains(tempVertex)){
					for(int j=0;j<axisGraph.size();j++){
						if(tempVertex.checkAdjacent(axisGraph.get(j))){
							tempVertex.addChild(axisGraph.get(j));
							axisGraph.get(j).addChild(tempVertex);
						}
					}
				}

			}
		}
	}
	public LinkedList<Vertex> getIntersections(){
		LinkedList<Vertex> interSections=new LinkedList<Vertex>();
		for(int i=0;i<this.axisGraph.size();i++){
			if(axisGraph.get(i).isIntersection()){
				interSections.add(axisGraph.get(i));
			}
		}
		return interSections;
	}
	public LinkedList<Vertex> getSegment(LinkedList<Vertex> segment,int pos){
		//LinkedList<Vertex> segment=new LinkedList<Vertex>();
		int addedCount=0;
		for(int i=0;i<segment.get(pos).getChildren().size();i++){
			if(!segment.get(pos).getChildren().get(i).isIntersection()
					&&!segment.contains(segment.get(pos).getChildren().get(i))){
				segment.add(segment.get(pos).getChildren().get(i));
				addedCount++;
				LinkedList<Vertex> temp=getSegment(segment,pos+addedCount);
				if(temp.size()>0){
					addedCount+=temp.size();
					segment.addAll(temp);
				}
			}
		}
		return segment;
	}
	public void  removeSegments(int minLength,int maxLength){
		LinkedList<Vertex> intersections=this.getIntersections();
		for(int i=0;i<intersections.size();i++){
			for(int j=0;j<intersections.get(i).getChildren().size();j++){
				LinkedList<Vertex> tempList=new LinkedList<Vertex>();
				tempList.add(intersections.get(i).getChildren().get(j));
				LinkedList<Vertex> segment=getSegment(tempList,0);
				if(minLength!=-1&&segment.size()<minLength){
					removeSingleSegment(segment);
				}
				else if(maxLength!=-1&&segment.size()>maxLength){
					removeSingleSegment(segment);
				}
				
			}
		}
	}
	private void removeSingleSegment(LinkedList<Vertex> removeList){
		for(int i=0;i<removeList.size();i++){
			this.axisGraph.remove(removeList.get(0));
		}
	}
	public LinkedList<Point> getMedialAxis(){
		LinkedList<Point> medialAxis=new LinkedList<Point>();
		for(int i=0;i<this.axisGraph.size();i++){
			medialAxis.add(this.axisGraph.get(i).getPoint());
		}
		return medialAxis;
	}
	
}
