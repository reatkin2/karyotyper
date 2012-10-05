package MedialAxis;

import java.awt.Point;
import java.util.LinkedList;

import chromosome.ChromosomeCluster;

import basicObjects.Vertex;


public class MedialAxisGraph {
	LinkedList<Vertex> axisGraph=new LinkedList<Vertex>();
	public MedialAxisGraph(LinkedList<Point> medialAxis){
		axisGraph= new LinkedList<Vertex>(); 
		buildGraph(medialAxis);
	}
	
	/**
	 * this creates the graph from a linkedlist of points
	 * @param medialAxis the linked list of points to graph
	 */
	private void buildGraph(LinkedList<Point> medialAxis){
		if(medialAxis!=null){
			for(int i=0;i<medialAxis.size();i++){
				Vertex tempVertex=new Vertex(medialAxis.get(i));
				if(!axisGraph.contains(tempVertex)){
					axisGraph.add(tempVertex);
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
	
	/**
	 * this goes through checking every vertex of the
	 * graph and adding all connections to the graph as well as
	 * adding this vertex
	 * @param tempVertex the vertex to be added
	 */
	public void addVertex(Vertex tempVertex){
		if(!axisGraph.contains(tempVertex)){
			axisGraph.add(tempVertex);
			for(int j=0;j<axisGraph.size();j++){
				if(tempVertex.checkAdjacent(axisGraph.get(j))){
					tempVertex.addChild(axisGraph.get(j));
					axisGraph.get(j).addChild(tempVertex);
				}
			}
		}

	}
	
	/**
	 * this returns a linkedlist of vertexs that are where
	 * multiple branches of the medial axis come together
	 * @return a linked list of vertexs that are intersections
	 */
	public LinkedList<Vertex> getIntersections(){
		LinkedList<Vertex> interSections=new LinkedList<Vertex>();
		for(int i=0;i<this.axisGraph.size();i++){
			if(axisGraph.get(i).isIntersection()){
				interSections.add(axisGraph.get(i));
			}
		}
		return interSections;
	}
	
	/**
	 * this gets a segment that is seperated by intersections using recursion
	 * starting from the vertex in the list at the positoin pos 
	 * @param segment the segment as it has grown in recursion to this point
	 * @param pos the vertex that we are checking for connections to this segment on
	 * @return a segment that is seperated by 2 intersections or an 1 intersection
	 * and the end of the line
	 */
	public LinkedList<Vertex> getSegment(LinkedList<Vertex> segment,int pos){
		//LinkedList<Vertex> segment=new LinkedList<Vertex>();
		int addedCount=0;
		for(int i=0;i<segment.get(pos).getChildren().size();i++){
			if(!segment.get(pos).getChildren().get(i).isIntersection()
					&&!segment.contains(segment.get(pos).getChildren().get(i))){
				segment.add(segment.get(pos).getChildren().get(i));
				addedCount++;
				//LinkedList<Vertex> temp=
				getSegment(segment,pos+addedCount);
//				if(temp.size()>0){
//					addedCount+=temp.size();
//					segment.addAll(temp);
//				}
			}
		}
		return segment;
	}
	
	/**
	 * remove all segments that are smaller than or larger than minlength
	 * and maxlength
	 * @param minLength  remove all segments smaller or input -1 to not remove
	 * @param maxLength  remove all segments larger or input -1 to not remove
	 */
	//TODO(aamcknig): make this remove only if one end is not an intersection
	public void  removeSegments(int minLength,int maxLength){
		LinkedList<Vertex> intersections=this.getIntersections();
		for(int i=0;i<intersections.size();i++){
			for(int j=0;j<intersections.get(i).getChildren().size();j++){
				LinkedList<Vertex> tempList=new LinkedList<Vertex>();
				tempList.add(intersections.get(i).getChildren().get(j));
				LinkedList<Vertex> segment=getSegment(tempList,0);
				if(minLength!=-1&&segment.size()<minLength){
					removeVertice(segment);
				}
				else if(maxLength!=-1&&segment.size()>maxLength){
					removeVertice(segment);
				}
				
			}
		}
		removeVertice(intersections);
	}
	
	/**
	 * remove all vertexes from the graph that are in the removeList
	 * @param removeList the list of vertexes to be removed
	 */
	private void removeVertice(LinkedList<Vertex> removeList){
		for(int i=0;i<removeList.size();i++){
			for(int j=0;j<removeList.get(i).getChildren().size();j++){
				if(removeList.get(i).getChildren().get(j).getChildren().contains(removeList.get(i))){
					removeList.get(i).getChildren().get(j).getChildren().remove(removeList.get(i));
				}
			}
			this.axisGraph.remove(removeList.get(i));
		}
	}
	private void removeVertex(Vertex removeVertex){
		int positionNGraph=this.indexOfVertexWithPoint(removeVertex.getPoint());
			for(int j=0;j<removeVertex.getChildren().size();j++){
				if(removeVertex.getChildren().get(j).getChildren().contains(removeVertex)){
					removeVertex.getChildren().get(j).getChildren().remove(removeVertex);
				}
			}
			this.axisGraph.remove(positionNGraph);
	}
	
	/**
	 * returns a linked list of points that represent the graph
	 * @return a linked list of points that represent the graph
	 */
	public LinkedList<Point> getMedialAxis(){
		LinkedList<Point> medialAxis=new LinkedList<Point>();
		for(int i=0;i<this.axisGraph.size();i++){
			medialAxis.add(this.axisGraph.get(i).getPoint());
		}
		return medialAxis;
	}
	
	/**
	 * gives you the index of tempPiont in the graph list
	 * or -1 if not in the graph
	 * @param tempPoint the point to find the index of
	 * @return the position in the linkedlist graph of vertex or -1 if not in list
	 */
	private int indexOfVertexWithPoint(Point tempPoint){
		for(int i=0;i<axisGraph.size();i++){
			if(axisGraph.get(i).getPoint().equals(tempPoint)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * checks to see if two points are connected in the graph
	 * @param point1 the first piont see if connects to point2
	 * @param point2 the second piont see if connects to point1
	 * @return true if the pionts are adjacent in the graph
	 */
	public boolean isConnected(Point point1,Point point2){
		LinkedList<Integer> checked=new LinkedList<Integer>();
		int indexNGraph=this.indexOfVertexWithPoint(point1);
		if(indexNGraph>-1){
			return isConnected(point1,point2,indexNGraph,checked);
		}
		return false;
	}
	
	/**
	 * the recursive part of finding if two points are connected called from
	 * isConnected(point,point)
	 * @param point1 the first piont see if connects to point2
	 * @param point2 the second piont see if connects to point1	
	 * @param pos the next position in the graph to check
	 * @param checked list of checked vertexes in the graph
	 * @return true if the two points are connected
	 */
	private boolean isConnected(Point point1,Point point2,int pos,LinkedList<Integer> checked){
		//LinkedList<Vertex> segment=new LinkedList<Vertex>();
		boolean connected=false;
		if(!checked.contains(pos)){
			checked.add(pos);
			for(int i=0;i<this.axisGraph.get(pos).getChildren().size();i++){
				if(this.axisGraph.get(pos).getChildren().get(i).getPoint().equals(point2)){
					return true;
				}
				else if(!checked.contains(this.axisGraph.indexOf(this.axisGraph.get(pos).getChildren().get(i)))){
					connected=isConnected(point1,point2,this.axisGraph.indexOf(axisGraph.get(pos).getChildren().get(i)),checked);
					if(connected){
						return true;
					}
				}
			}
		}
		return connected;
	}
	public void trimGraph(){
		LinkedList<Vertex> intersections=this.getIntersections();
		//foreach intersection
		for(int i=0;i<intersections.size();i++){
			boolean trim=false;
			//for each child of the intersection
			for(int j=0;!trim&&j<intersections.get(i).getChildren().size();j++){
				//for each child of the children of the intersection
				int sameChildrenCount=0;
				for(int k=0;!trim&&k<intersections.get(i).getChildren().size();k++){
					if(intersections.get(i).getChildren().get(j).getChildren().contains(intersections.get(i).getChildren().get(k))){
						sameChildrenCount++;
					}
					else if(intersections.get(i).getChildren().get(j).getPoint().equals(intersections.get(i).getChildren().get(k).getPoint())){
						sameChildrenCount++;
					}

				}
				if(sameChildrenCount==intersections.get(i).getChildren().size()){
					trim=true;
				}
				
			}
			if(trim){
				removeVertex(intersections.get(i));
				intersections.remove(i);
				i--;
			}
		}
	}
}
