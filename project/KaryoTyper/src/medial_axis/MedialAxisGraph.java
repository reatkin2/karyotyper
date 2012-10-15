package medial_axis;

import java.awt.Point;
import java.util.LinkedList;

import chromosome.ChromosomeCluster;
import chromosome.GeneticSlideImage;

import basic_objects.GraphSegment;
import basic_objects.Vertex;

public class MedialAxisGraph {
	private LinkedList<Vertex> axisGraph = new LinkedList<Vertex>();
	private MedialAxis medialAxis;
	private int segmentCount;
	private double chromoWidth;
	
	public MedialAxisGraph(){
		this.chromoWidth=-1;
		segmentCount=0;
		axisGraph = new LinkedList<Vertex>();
		medialAxis=new MedialAxis();
	}
	public MedialAxisGraph(MedialAxis medialAxisTemp) {
		this.chromoWidth=-1;
		segmentCount=0;
		axisGraph = new LinkedList<Vertex>();
		medialAxis=medialAxisTemp;
		buildGraph(medialAxisTemp.getMedialAxisPoints(),medialAxisTemp.getDistanceMap());
	}

	public MedialAxisGraph(MedialAxis medialAxisTemp,ChromosomeCluster myCluster,GeneticSlideImage img) {
		chromoWidth=img.getChromoWidth();
		segmentCount=0;
		axisGraph = new LinkedList<Vertex>();
		medialAxis=medialAxisTemp;
		buildGraph(medialAxisTemp.getMedialAxisPoints(),medialAxisTemp.getDistanceMap());
		medialAxisTemp.fillInSkeleton(myCluster, this);
		segmentCount=0;
		axisGraph = new LinkedList<Vertex>();
		buildGraph(medialAxisTemp.getMedialAxisPoints(),medialAxisTemp.getDistanceMap());
		trimGraph();
		removeSegments((int)Math.round((img.getChromoWidth()*(2.0/3.0))), -1);
		medialAxisTemp.setMedialAxis(this.axisGraph);


	}

	/**
	 * this creates the graph from a linkedlist of points
	 * 
	 * @param medialAxis
	 *            the linked list of points to graph
	 */
	public void buildGraph(LinkedList<Point> medialAxis,DistanceMap distanceMap) {
		
		int segmentID[]= new int[medialAxis.size()];
		int sgmntCount=0;
		if (medialAxis != null) {
			for (int i = 0; i < medialAxis.size(); i++) {
				if(!axisGraph.contains(medialAxis.get(i))){
					Vertex tempVertex = new Vertex(medialAxis.get(i),distanceMap.getDistanceFromEdge(medialAxis.get(i)));
					LinkedList<Integer> connectedList=new LinkedList<Integer>();
					axisGraph.add(tempVertex);
					boolean adjacent=false;
					for (int j = 0; j < axisGraph.size(); j++) {
						if (tempVertex.checkAdjacent(axisGraph.get(j))) {
							adjacent=true;
							connectedList.add(axisGraph.get(j).getMySegement());
							if(tempVertex.getMySegement()!=-1
									&&tempVertex.getMySegement()>getLowest(segmentID,axisGraph.get(j).getMySegement())){
								connectedList.add(tempVertex.getMySegement());
								tempVertex.setMySegement(getLowest(segmentID,axisGraph.get(j).getMySegement()));
							}
							else if(tempVertex.getMySegement()==-1){
								tempVertex.setMySegement(getLowest(segmentID,axisGraph.get(j).getMySegement()));
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
						setAll2Lowest(segmentID,connectedList.get(k),tempVertex.getMySegement());

					}
				}
			}
			if(!axisGraph.isEmpty()){
				LinkedList<Integer> segNums=new LinkedList<Integer>();
				segNums.add(getLowest(segmentID,axisGraph.get(0).getMySegement()));
				axisGraph.get(0).setMySegement(segNums.indexOf(getLowest(segmentID,axisGraph.get(0).getMySegement())));
				for (int i=1;i<axisGraph.size();i++){
					if(segNums.contains(getLowest(segmentID,axisGraph.get(i).getMySegement()))){
						axisGraph.get(i).setMySegement(segNums.indexOf(getLowest(segmentID,axisGraph.get(i).getMySegement())));
					}
					else{
						segNums.add(getLowest(segmentID,axisGraph.get(i).getMySegement()));
						axisGraph.get(i).setMySegement(segNums.indexOf(getLowest(segmentID,axisGraph.get(i).getMySegement())));
					}
				}
				this.segmentCount=segNums.size();
			}
		}
	}
	private void setAll2Lowest(int list[],int pos,int value){
		if(list[pos]<pos){
			setAll2Lowest(list,list[pos],value);
			list[pos]=value;
		}
		else if(list[pos]==pos&&value<pos){
			list[pos]=value;
		}
	}

	private int getLowest(int list[],int pos){
		if(list[pos]<pos){
			return getLowest(list,list[pos]);
		}
		return list[pos];
	}
	public int getSegmentCount() {
		return segmentCount;
	}

	/**
	 * this goes through checking every vertex of the graph and adding all connections to the graph
	 * as well as adding this vertex
	 * 
	 * @param tempVertex
	 *            the vertex to be added
	 */
	//TODO(aamcknig): TESTTEST
	public void addVertex(Vertex tempVertex) {
		if (!axisGraph.contains(tempVertex)) {
			LinkedList<Integer> connectedSegments=new LinkedList<Integer>();
			int lowestNumConnection=-1;
			axisGraph.add(tempVertex);
			for (int j = 0; j < axisGraph.size(); j++) {
				if (tempVertex.checkAdjacent(axisGraph.get(j))) {
					tempVertex.addChild(axisGraph.get(j));
					axisGraph.get(j).addChild(tempVertex);
					if(!connectedSegments.contains(axisGraph.get(j).getMySegement())){
						connectedSegments.add(this.axisGraph.get(j).getMySegement());
						if(lowestNumConnection!=-1&&lowestNumConnection>this.axisGraph.get(j).getMySegement()){
							lowestNumConnection=this.axisGraph.get(j).getMySegement();
						}
					}
				}
			}
			tempVertex.setMySegement(lowestNumConnection);
			if(connectedSegments.size()>1){
				for(int k=0;k<axisGraph.size();k++){
					if(connectedSegments.contains(this.axisGraph.get(k).getMySegement())){
						this.axisGraph.get(k).setMySegement(lowestNumConnection);
					}
				}
				this.segmentCount=this.segmentCount-connectedSegments.size()+1;
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
	public LinkedList<Vertex> getSegment(LinkedList<Vertex> segment, int pos,Point endPoint) {
		int addedCount = 0;
		for (int i = 0; i < segment.get(pos).getChildren().size(); i++) {
			if (!segment.contains(segment.get(pos).getChildren().get(i))) {
				if(segment.get(pos).getChildren().get(i).getPoint().equals(endPoint)){
					segment.add(segment.get(pos).getChildren().get(i));
					return segment;
				}
				segment.add(segment.get(pos).getChildren().get(i));
				addedCount++;
				getSegment(segment, pos + addedCount);
				if(segment.get(segment.size()-1).getPoint().equals(endPoint)){
					return segment;
				}
			}
		}
		return segment;
	}
	/**
	 * this gets a segment that is using recursion starting from the
	 * vertex in the list at the position pos
	 * 
	 * @param segment
	 *            the segment as it has grown in recursion to this point
	 * @param pos
	 *            the vertex that we are checking for connections to this segment on
	 * @return a segment that is separated by 2 intersections or an 1 intersection and the end of
	 *         the line
	 */
	private LinkedList<Vertex> getNextVertex(LinkedList<Vertex> segment, int pos,int countDown) {
		int addedCount = 0;
		if(countDown>0){
			for (int i = 0; i < segment.get(pos).getChildren().size(); i++) {
				if (!segment.contains(segment.get(pos).getChildren().get(i))) {
					segment.add(segment.get(pos).getChildren().get(i));
					addedCount++;
					getNextVertex(segment, pos + addedCount,--countDown);
				}
			}
		}
		return segment;
	}
//	public getNextVertex(Vertex vertexStart,Vertex vertexOpposite, int count){
//		
//	}
//	public LinkedList<GraphSegment> getSegments() {
//		LinkedList<GraphSegment> graphSegments=new LinkedList<GraphSegment>();
//		LinkedList<Vertex> intersections = this.getIntersections();
//		for (int i = 0; i < intersections.size(); i++) {
//			for (int j = 0; j < intersections.get(i).getChildren().size(); j++) {
//				if(!intersections.get(i).getChildren().get(j).isIntersection()){
//					LinkedList<Vertex> tempList = new LinkedList<Vertex>();
//					tempList.add(intersections.get(i).getChildren().get(j));
//					LinkedList<Vertex> segment = getSegment(tempList, 0);
//					
//				}
//			}
//		}
//		removeVertice(removeThese);
//		removeUnconnectedSegments(4);
//		//removeVertice(intersections);
//	}

	
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
		removeUnconnectedSegments(this.chromoWidth*1.5);
		//removeVertice(intersections);
	}
	/**
	 * this removes segments that have no intersections with a length less
	 * than length
	 * @param length remove segments shorter than or equal to this length
	 */
	//TODO(aamcknig): make this work by when segments are removed the remaining segment id's are changed to be correct
	public void removeUnconnectedSegments(double minLength){
		LinkedList<Vertex> removeThese=new LinkedList<Vertex>();
		LinkedList<GraphSegment> segments=new LinkedList<GraphSegment>();
		if(this.segmentCount>1){
			boolean segHasIntersect[]=new boolean[this.segmentCount];
			for(int k=0;k<this.segmentCount;k++){
				segHasIntersect[k]=false;
				segments.add(new GraphSegment(k));
				
			}
			for(int j=0;j<this.axisGraph.size();j++){
				if(!axisGraph.get(j).isIntersection()&&!segHasIntersect[axisGraph.get(j).getMySegement()]){
					segments.get(axisGraph.get(j).getMySegement()).addVertex(axisGraph.get(j));
				}
				else{
					segHasIntersect[axisGraph.get(j).getMySegement()]=true;
				}
			}
			for(int i=0;i<this.segmentCount;i++){
				if(!segHasIntersect[i]){
					if(segments.get(i).getSegment().size()<=minLength){
						removeThese=combine(removeThese,segments.get(i).getSegment());
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
	
	public void generateTangents(double lowerLimitDistance, double upperLimitDistance) throws Exception {
		resetChecks();
		for (Vertex v : axisGraph) {
			v.calculateTangentLine(lowerLimitDistance, upperLimitDistance);
			resetChecks();
		}
	}
	
	public void generateOrthogonals(double lowerLimitDistance, double upperLimitDistance) throws Exception {
		resetChecks();
		for (Vertex v : axisGraph) {
			v.calculateOrthogonalLine(lowerLimitDistance, upperLimitDistance);
			resetChecks();
		}
	}
	
	/**
	 * Resets hasBeenChecked flag of all vertices to false. Should be used before and after
	 * graph traversals to prevent unintended results.
	 */
	public void resetChecks() {
		for (Vertex v : axisGraph) {
			v.setHasBeenChecked(false);
		}
	}
}
