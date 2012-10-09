package medial_axis;

import java.awt.Point;
import java.util.LinkedList;

import chromosome.ChromosomeCluster;
import chromosome.GeneticSlideImage;

import basic_objects.Vertex;

public class MedialAxisGraph {
	private LinkedList<Vertex> axisGraph = new LinkedList<Vertex>();
	private MedialAxis medialAxis;
	private int segmentCount;
	
	public MedialAxisGraph(MedialAxis medialAxisTemp,ChromosomeCluster myCluster,GeneticSlideImage img) {
		segmentCount=0;
		axisGraph = new LinkedList<Vertex>();
		medialAxis=medialAxisTemp;
		buildGraph(medialAxisTemp.getMedialAxisPoints(),medialAxisTemp.getDistanceMap());
		medialAxisTemp.fillInSkeleton(myCluster, this);
		segmentCount=0;
		axisGraph = new LinkedList<Vertex>();
		buildGraph(medialAxisTemp.getMedialAxisPoints(),medialAxisTemp.getDistanceMap());
		trimGraph();
		removeSegments((int)Math.round((img.getAverage()*(2.0/3.0))), -1);
		medialAxisTemp.setMedialAxis(getMedialAxisFromGraph());
		buildGraph(medialAxisTemp.getMedialAxisPoints(),medialAxisTemp.getDistanceMap());


	}

	/**
	 * this creates the graph from a linkedlist of points
	 * 
	 * @param medialAxis
	 *            the linked list of points to graph
	 */
	private void buildGraph(LinkedList<Point> medialAxis,DistanceMap distanceMap) {
		int segmentID[]= new int[medialAxis.size()];
		int sgmntCount=0;
		if (medialAxis != null) {
			for (int i = 0; i < medialAxis.size(); i++) {
				Vertex tempVertex = new Vertex(medialAxis.get(i),distanceMap.getDistanceFromEdge(medialAxis.get(i)));
				LinkedList<Integer> connectedList=new LinkedList<Integer>();
				axisGraph.add(tempVertex);
				boolean adjacent=false;
				for (int j = 0; j < axisGraph.size(); j++) {
					if (tempVertex.checkAdjacent(axisGraph.get(j))) {
						adjacent=true;
						connectedList.add(axisGraph.get(j).getMySegement());
						if(tempVertex.getMySegement()!=-1
								&&tempVertex.getMySegement()>axisGraph.get(j).getMySegement()){
							tempVertex.setMySegement(axisGraph.get(j).getMySegement());
						}
						else if(tempVertex.getMySegement()==-1){
							tempVertex.setMySegement(axisGraph.get(j).getMySegement());
						}
						tempVertex.addChild(axisGraph.get(j));
						axisGraph.get(j).addChild(tempVertex);
					}
				}
				if(!adjacent){
					tempVertex.setMySegement(sgmntCount);
					segmentID[sgmntCount]=sgmntCount++;
				}
				for(int k=0;k<connectedList.size();k++){
					segmentID[connectedList.get(k)]=tempVertex.getMySegement();
				}
				for(int l=0;l<40&&l<medialAxis.size();l++){
					System.out.print(":"+(segmentID[l]));
				}
				System.out.println();
			}
			if(!axisGraph.isEmpty()){
				LinkedList<Integer> segNums=new LinkedList<Integer>();
				segNums.add(segmentID[axisGraph.get(0).getMySegement()]);
				axisGraph.get(0).setMySegement(segNums.indexOf(segmentID[axisGraph.get(0).getMySegement()]));
				for (int i=1;i<axisGraph.size();i++){
					if(segNums.contains(segmentID[axisGraph.get(i).getMySegement()])){
						axisGraph.get(i).setMySegement(segNums.indexOf(segmentID[axisGraph.get(i).getMySegement()]));
					}
					else{
						segNums.add(segmentID[axisGraph.get(i).getMySegement()]);
						axisGraph.get(i).setMySegement(segNums.indexOf(segmentID[axisGraph.get(i).getMySegement()]));
					}
				}
				this.segmentCount=segNums.size();
			}
		}
	}
	/**
	 * this goes through checking every vertex of the graph and adding all connections to the graph
	 * as well as adding this vertex
	 * 
	 * @param tempVertex
	 *            the vertex to be added
	 */
	public void addVertex(Vertex tempVertex) {
		if (!axisGraph.contains(tempVertex)) {
			axisGraph.add(tempVertex);
			for (int j = 0; j < axisGraph.size(); j++) {
				if (tempVertex.checkAdjacent(axisGraph.get(j))) {
					tempVertex.addChild(axisGraph.get(j));
					axisGraph.get(j).addChild(tempVertex);
				}
			}
		}

	}

	/**
	 * this returns a linkedlist of vertices that are where multiple branches of the medial axis
	 * come together
	 * 
	 * @return a linked list of vertices that are intersections
	 */
	public LinkedList<Vertex> getIntersections() {
		LinkedList<Vertex> interSections = new LinkedList<Vertex>();
		for (int i = 0; i < this.axisGraph.size(); i++) {
			if (axisGraph.get(i).isIntersection()) {
				interSections.add(axisGraph.get(i));
			}
		}
		return interSections;
	}
	public MedialAxis getMedialAxis() {
		return medialAxis;
	}

	/**
	 * this gets a segment that is separated by intersections using recursion starting from the
	 * vertex in the list at the position pos
	 * 
	 * @param segment
	 *            the segment as it has grown in recursion to this point
	 * @param pos
	 *            the vertex that we are checking for connections to this segment on
	 * @return a segment that is separated by 2 intersections or an 1 intersection and the end of
	 *         the line
	 */
	public LinkedList<Vertex> getSegment(LinkedList<Vertex> segment, int pos) {
		int addedCount = 0;
		for (int i = 0; i < segment.get(pos).getChildren().size(); i++) {
			if (!segment.get(pos).getChildren().get(i).isIntersection()
					&& !segment.contains(segment.get(pos).getChildren().get(i))) {
				segment.add(segment.get(pos).getChildren().get(i));
				addedCount++;
				getSegment(segment, pos + addedCount);
			}
		}
		return segment;
	}

	public LinkedList<Vertex> getAxisGraph() {
		return axisGraph;
	}

	/**
	 * remove all segments that are smaller than or larger than minlength and maxlength
	 * 
	 * @param minLength
	 *            remove all segments smaller or input -1 to not remove
	 * @param maxLength
	 *            remove all segments larger or input -1 to not remove
	 */
	// TODO(aamcknig): make this remove only if one end is not an intersection
	public void removeSegments(int minLength, int maxLength) {
		LinkedList<Vertex> intersections = this.getIntersections();
		LinkedList<Vertex> removeThese=new LinkedList<Vertex>();
		for (int i = 0; i < intersections.size(); i++) {
			for (int j = 0; j < intersections.get(i).getChildren().size(); j++) {
				if(!intersections.get(i).getChildren().get(j).isIntersection()){
					LinkedList<Vertex> tempList = new LinkedList<Vertex>();
					tempList.add(intersections.get(i).getChildren().get(j));
					LinkedList<Vertex> segment = getSegment(tempList, 0);
					if(distanceToEdge(segment)<4){//this.getIntersectionCount(segment)<2
						if (minLength != -1 && segment.size() < minLength) {
							removeThese=combine(removeThese,segment);
						} else if (maxLength != -1 && segment.size() > maxLength) {
							removeThese=combine(removeThese,segment);
						}
					}
				}
			}
		}
		removeVertice(removeThese);
		removeUnconnectedSegments(4);
		//removeVertice(intersections);
	}
	/**
	 * this removes segments that have no intersections with a length less
	 * than length
	 * @param length remove segments shorter than or equal to this length
	 */
	private void removeUnconnectedSegments(int length){
		LinkedList<Vertex> removeThese=new LinkedList<Vertex>();
		for(int i=0;i<this.axisGraph.size();i++){
			boolean hasIntersection=false;
			if(!axisGraph.get(i).isIntersection()){
				LinkedList<Vertex> segment=new LinkedList<Vertex>();
				segment.add(axisGraph.get(i));
				segment=this.getSegment(segment, 0);
				if(segment.size()<=length){
					for(int j=0;!hasIntersection&&j<segment.size();j++){
						if(this.getIntersectionCount(segment.get(j).getChildren())!=0){
							hasIntersection=true;
						}
					}
					if(!hasIntersection){
						removeThese=combine(removeThese,segment);
					}
				}
			}
		}
		this.removeVertice(removeThese);
	}
	private LinkedList<Vertex> combine(LinkedList<Vertex> list1,LinkedList<Vertex> list2){
		for(int i=0;i<list2.size();i++){
			if(!list1.contains(list2.get(i))){
				list1.add(list2.get(i));
			}
		}
		return list1;
	}
	/**
	 * returns the value of the lowest distance to the edge
	 * using the distanceMap value of each vertex in checklist
	 * @param checkList the list of vertice to check for edge closeness
	 * @return the value of the distance closest to the edge using distanceMap value
	 */
	private int distanceToEdge(LinkedList<Vertex> checkList){
		int distance=1000;
		for(int i=0;i<checkList.size();i++){
			if(checkList.get(i).getDistanceFromEdge()<distance){
				distance=checkList.get(i).getDistanceFromEdge();
			}
		}
		return distance;
	}
	/**
	 * returns the number of intersections in a list of verteci
	 * @param vertexList the list to count intersections of
	 * @return the number of intersections found in the list
	 */
	public int getIntersectionCount(LinkedList<Vertex> vertexList){
		int count=0;
		for (int i=0;i<vertexList.size();i++){
			if(vertexList.get(i).isIntersection()){
				count++;
			}
		}
		return count;
	}
	/**
	 * remove all vertexes from the graph that are in the removeList
	 * 
	 * @param removeList
	 *            the list of vertexes to be removed
	 */
	private void removeVertice(LinkedList<Vertex> removeList) {
		for (int i = 0; i < removeList.size(); i++) {
			for (int j = 0; j < removeList.get(i).getChildren().size(); j++) {
				if (removeList.get(i).getChildren().get(j).getChildren()
						.contains(removeList.get(i))) {
					removeList.get(i).getChildren().get(j).getChildren().remove(removeList.get(i));
				}
			}
			this.axisGraph.remove(removeList.get(i));
		}
	}

	private void removeVertex(Vertex removeVertex){
		int positionNGraph=this.indexOfVertexWithPoint(removeVertex.getPoint());
		if(positionNGraph!=-1){
			for(int j=0;j<removeVertex.getChildren().size();j++){
				if(removeVertex.getChildren().get(j).getChildren().contains(removeVertex)){
					removeVertex.getChildren().get(j).getChildren().remove(removeVertex);
				}
			}
			this.axisGraph.remove(positionNGraph);
		}
	}
	
	/**
	 * returns a linked list of points that represent the graph
	 * 
	 * @return a linked list of points that represent the graph
	 */
	public LinkedList<Point> getMedialAxisFromGraph() {
		LinkedList<Point> medialAxis = new LinkedList<Point>();
		for (int i = 0; i < this.axisGraph.size(); i++) {
			medialAxis.add(this.axisGraph.get(i).getPoint());
		}
		return medialAxis;
	}
	/**
	 * gives you the index of tempPoint in the graph list or -1 if not in the graph
	 * 
	 * @param tempPoint
	 *            the point to find the index of
	 * @return the position in the linkedlist graph of vertex or -1 if not in list
	 */
	private int indexOfVertexWithPoint(Point tempPoint) {
		for (int i = 0; i < axisGraph.size(); i++) {
			if (axisGraph.get(i).getPoint().equals(tempPoint)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * checks to see if two points are connected in the graph
	 * 
	 * @param point1
	 *            the first piont see if connects to point2
	 * @param point2
	 *            the second piont see if connects to point1
	 * @return true if the pionts are adjacent in the graph
	 */
	public boolean isConnected(Point point1, Point point2) {
		LinkedList<Integer> checked = new LinkedList<Integer>();
		int indexNGraph = this.indexOfVertexWithPoint(point1);
		if (indexNGraph > -1) {
			return isConnected(point1, point2, indexNGraph, checked);
		}
		return false;
	}

	/**
	 * the recursive part of finding if two points are connected called from
	 * isConnected(point,point)
	 * 
	 * @param point1
	 *            the first piont see if connects to point2
	 * @param point2
	 *            the second piont see if connects to point1
	 * @param pos
	 *            the next position in the graph to check
	 * @param checked
	 *            list of checked vertexes in the graph
	 * @return true if the two points are connected
	 */
	private boolean isConnected(Point point1, Point point2, int pos, LinkedList<Integer> checked) {
		// LinkedList<Vertex> segment=new LinkedList<Vertex>();
		boolean connected = false;
		if (!checked.contains(pos)) {
			checked.add(pos);
			for (int i = 0; i < this.axisGraph.get(pos).getChildren().size(); i++) {
				if (this.axisGraph.get(pos).getChildren().get(i).getPoint().equals(point2)) {
					return true;
				} else if (!checked.contains(this.axisGraph.indexOf(this.axisGraph.get(pos)
						.getChildren().get(i)))) {
					connected = isConnected(point1, point2,
							this.axisGraph.indexOf(axisGraph.get(pos).getChildren().get(i)),
							checked);
					if (connected) {
						return true;
					}
				}
			}
		}
		return connected;
	}
	
	/**
	 * removes any point of the graph that is not critical 
	 * to connecting the graph
	 */
	public void trimGraph(){
		LinkedList<Vertex> intersections=this.getIntersections();
		//foreach intersection
		for(int i=0;i<intersections.size();i++){
			int removeVertex=-1;
			//for each child of the intersection
			for(int j=0;removeVertex==-1&&j<intersections.get(i).getChildren().size();j++){
				//for each child of the children of the intersection
				int sameChildrenCount=0;
				for(int k=0;removeVertex==-1&&k<intersections.get(i).getChildren().get(j).getChildren().size();k++){
					if(intersections.get(i).getChildren().contains(intersections.get(i).getChildren().get(j).getChildren().get(k))){
						sameChildrenCount++;
					}
					else if(intersections.get(i).getPoint().equals(intersections.get(i).getChildren().get(j).getChildren().get(k).getPoint())){
						sameChildrenCount++;
					}

				}
				if(sameChildrenCount>1&&sameChildrenCount==intersections.get(i).getChildren().get(j).getChildren().size()){
					removeVertex=j;
				}
				
			}
			if(removeVertex!=-1){
				removeVertex(intersections.get(i).getChildren().get(removeVertex));
			}
		}
	}
}
