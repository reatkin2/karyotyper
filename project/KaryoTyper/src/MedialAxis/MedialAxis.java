package MedialAxis;

import java.awt.Point;
import java.util.LinkedList;

import chromosome.ChromosomeCluster;
import chromosome.GeneticSlideImage;

import basicObjects.AroundPixel;
import basicObjects.Cluster;
import basicObjects.Vertex;

public class MedialAxis {
    private LinkedList<Point> skeleton;
    private double objectWidth[];
    private DistanceMap distanceMap;
	private int biggestIncreaseSkeletonAtWidthCount;
    private int most2LeastRemovedAtWidthCount;
    
	public MedialAxis(){
		initMedialAxis();
	}
	public MedialAxis(MedialAxis newAxis){
		initMedialAxis();
		copyMedialAxis(newAxis);
	}
	public MedialAxis(Cluster myCluster,GeneticSlideImage img){
		initMedialAxis();
		createSkeleton(myCluster, img);
	}
	
	private void initMedialAxis(){
		distanceMap=new DistanceMap();
		skeleton=new LinkedList<Point>();
        biggestIncreaseSkeletonAtWidthCount=-1;
        most2LeastRemovedAtWidthCount=1;
        objectWidth=new double[2];
        objectWidth[0]=-1;
        objectWidth[1]=-1;
	}
	
	
	
	public void setMedialAxis(LinkedList<Point> tempMedialAxis){
		this.skeleton=tempMedialAxis;
	}
	
	private void copyMedialAxis(MedialAxis copyMedialAxis){
		this.distanceMap=new DistanceMap(copyMedialAxis.distanceMap);
		this.skeleton=copyMedialAxis.getMedialAxisPoints();
        this.biggestIncreaseSkeletonAtWidthCount=copyMedialAxis.biggestIncreaseSkeletonAtWidthCount;
        this.most2LeastRemovedAtWidthCount=copyMedialAxis.most2LeastRemovedAtWidthCount;        
        objectWidth[0]=copyMedialAxis.objectWidth[0];
        objectWidth[1]=copyMedialAxis.objectWidth[1];

	
	}
	
	/**
	 * this returns an array of two ints that represent the width of 
	 * the chromosome found when doing erosion/distance map by two different methods
	 * @return the width 
	 */
	public double[] getObjectWidth() {
		return objectWidth;
	}


    public LinkedList<Point> getMedialAxisPoints(){
    	return this.skeleton;
    }
    public DistanceMap getDistanceMap() {
		return distanceMap;
	}
    
    /**
     * this does the erosion and distanceMap creation similtaniously
     * 
     * @param myCluster the cluster to get the medial Axis of
     * @param img the image to the cluster is from
     */
    public void createSkeleton(Cluster myCluster,GeneticSlideImage img){
    	//Rectangle centerImage=new Rectangle((int)Math.round((bounds.getWidth()/2)-(bounds.getWidth()/4)),(int)Math.round((bounds.getHeight()/2)-(bounds.getHeight()/4)),
    	//						(int)Math.round(bounds.getWidth()/2),(int)Math.round(bounds.getHeight()/2));
        //LinkedList<Cluster> allClusters=new LinkedList<Cluster>();//create a linked list to store Clusters in
    	LinkedList<Point> removeEdgePointsHorizantal=new LinkedList<Point>();
    	LinkedList<Point> removeEdgePointsVertical=new LinkedList<Point>();
    	this.skeleton=new LinkedList<Point>();
    	LinkedList<Point> addThisRound=new LinkedList<Point>();
    	Cluster temp=new Cluster(myCluster);
    	this.distanceMap=new DistanceMap(myCluster.getSize().x,myCluster.getSize().y);
    	Point pointRightEdge=new Point(0,0);
    	int objectWdth=0;
    	boolean inObject=false;
    	boolean beforeObjectEdge=true;
    	int lastPixelCount=temp.getPixelCount();
    	int lastSkeletonCount=-1;
    	int distanceFromEdgeCount=0;
    	int removedLastTime=0;
    	int most2LeastRemoved=0;
    	int skeletonSizeLastTime=0;
    	int biggestIncreaseInSkeleton=0;
        while(skeleton.size()>lastSkeletonCount||lastPixelCount!=temp.getPixelCount()){
        	lastSkeletonCount=skeleton.size();
        	lastPixelCount=temp.getPixelCount();
	        for(int i=0;i<temp.getSize().x;i++){//loop width
	            for(int j=0;j<temp.getSize().y;j++){//loop height
	    			if(temp.getPos(i, j)){
	    				if(beforeObjectEdge){
	    					removeEdgePointsHorizantal.push(new Point(i,j));
	    				}
	    				objectWdth++;
    					beforeObjectEdge=false;
						inObject=true;
	    				pointRightEdge=new Point(i,j);
	    			}
	    			else{
	    				if(inObject){
	    					if(objectWdth<=2){
	    						Point tempPoint=removeEdgePointsHorizantal.pop();
	    						if(!skeleton.contains(tempPoint)){
	    							skeleton.add(tempPoint);
	    							addThisRound.add(tempPoint);
	    						}
	    					}
	    					removeEdgePointsHorizantal.push(pointRightEdge);
	    					pointRightEdge=new Point(0,0);
	    				}
    					inObject=false;
    					beforeObjectEdge=true;
    					objectWdth=0;
	    			}
	            }
	            if(inObject){
					if(objectWdth<=2){
						Point tempPoint=removeEdgePointsHorizantal.pop();
						if(!skeleton.contains(tempPoint)){
							skeleton.add(tempPoint);
							addThisRound.add(tempPoint);
						}
					}
					inObject=false;
					beforeObjectEdge=true;
					removeEdgePointsHorizantal.push(pointRightEdge);
					pointRightEdge=new Point(0,0);
				}
	            objectWdth=0;
	        }
	        objectWdth=0;
	        //now go vertical
	        for(int i=0;i<temp.getSize().y;i++){//loop width
	            for(int j=0;j<temp.getSize().x;j++){//loop height
	    			if(temp.getPos(j, i)){
	    				if(beforeObjectEdge){
	    					removeEdgePointsVertical.push(new Point(j,i));
	    				}
    					beforeObjectEdge=false;
						inObject=true;
	    				pointRightEdge=new Point(j,i);
	    				objectWdth++;
	    			}
	    			else{
	    				if(inObject){
	    					if(objectWdth<=2){
	    						Point tempPoint=removeEdgePointsVertical.pop();
	    						if(!skeleton.contains(tempPoint)){
	    							skeleton.add(tempPoint);
	    							addThisRound.add(tempPoint);
	    						}
	    					}
	    					removeEdgePointsVertical.push(pointRightEdge);
	    					pointRightEdge=new Point(0,0);
	    				}
    					inObject=false;
    					beforeObjectEdge=true;
    					objectWdth=0;
	    			}
	            }
	            if(inObject){
					if(objectWdth<=2){
						Point tempPoint=removeEdgePointsVertical.pop();
						if(!skeleton.contains(tempPoint)){
							skeleton.add(tempPoint);
							addThisRound.add(tempPoint);
						}
					}
					inObject=false;
					beforeObjectEdge=true;
					removeEdgePointsVertical.push(pointRightEdge);
					pointRightEdge=new Point(0,0);
				}
	            objectWdth=0;
	        }
	        //remove skeleton points from the list, identified by be duplicate in this list
		    while(!removeEdgePointsVertical.isEmpty()){
	        	Point removePoint=removeEdgePointsVertical.pop();
	        	temp.setPixel(removePoint,false);
	        	if(distanceMap.getDistanceFromEdge(removePoint)==-5){
	        		distanceMap.setDistanceFormEdge(removePoint,distanceFromEdgeCount);
	        	}
	        }
	        while(!removeEdgePointsHorizantal.isEmpty()){
	        	Point removePoint=removeEdgePointsHorizantal.pop();
	        	temp.setPixel(removePoint,false);
	        	if(distanceMap.getDistanceFromEdge(removePoint)==-5){
	        		distanceMap.setDistanceFormEdge(removePoint,distanceFromEdgeCount);
	        	}
	        }
	        if(distanceFromEdgeCount<2){
	        	skeleton=new LinkedList<Point>();
	        }
	        else{
	        	addBackSkeleton(temp);
	        }
	        if(distanceFromEdgeCount>2){//do everytime after first run
	        	int tempRemovedCount=lastPixelCount-temp.getPixelCount();
	        	if(removedLastTime-tempRemovedCount>most2LeastRemoved){
	        		most2LeastRemoved=removedLastTime-tempRemovedCount;
	        		this.most2LeastRemovedAtWidthCount=distanceFromEdgeCount;
	        	}
		        if(biggestIncreaseInSkeleton<skeleton.size()-skeletonSizeLastTime){
		        	biggestIncreaseInSkeleton=skeleton.size()-skeletonSizeLastTime;
		        	this.biggestIncreaseSkeletonAtWidthCount=distanceFromEdgeCount;
		        }
	        }
	        skeletonSizeLastTime=skeleton.size();
	        removedLastTime=lastPixelCount-temp.getPixelCount();
	        distanceFromEdgeCount++;
        }
        addBackSkeleton(temp);
        this.distanceMap.mapOut();
        this.objectWidth[0]=(this.biggestIncreaseSkeletonAtWidthCount+.75)*2;
        this.objectWidth[1]=(this.most2LeastRemovedAtWidthCount+.75)*2;
        this.writeObjectWidths();
        System.out.println("BreakPoint");
        
    }    
    
    /**
     * this is used to add back the parts of the medial axis that have been
     * removed during erosion to help with the next step of erosion
     * @param temp the cluster to add back the medial axis points too
     */
    private void addBackSkeleton(Cluster temp){
        for(int i=0;i<skeleton.size();i++){
        	temp.setPixel(skeleton.get(i), true);
        }
    }
    
    /**
     * this attempts to reconnect the pieces of the medialAxis that were
     * disconnected during erosion
     * @param myCluster the cluster that we are getting the medial axis of
     * @param graph the graph of the medialAxis
     */
    public void fillInSkeleton(ChromosomeCluster myCluster,MedialAxisGraph graph){
    	AroundPixel aroundPixel=new AroundPixel();
        for(int i=0;i<skeleton.size();i++){
        	int mostCenteredConnection=0;
        	int connections=0; 
        	Point tempPoint=skeleton.get(i);
        	int addPoint=-1;
        	boolean added=false;
        	Point mostConnected=new Point(-1,-1);
        	Point newConnectionPoint=new Point(-1,-1);
        	int mostNewConnections=0;
        	boolean connectionPos[]={false,false,false,false,false,false,false,false};
        	for(int j=0;j<8;j++){
    			Point tempAround=aroundPixel.getPoint(j,tempPoint);
        		if(tempAround.x>=0
        				&&tempAround.x<myCluster.getSize().x
        				&&tempAround.y>=0
        				&&tempAround.y<myCluster.getSize().y){
	        		if(skeleton.contains(tempAround)){
	        			connectionPos[j]=true;
	        			connections++;
	        		}
        		}
        	}
        	for(int j=0;j<8;j++){
            	//TODO: andrew fix not picking ideal pixel sept27,2012 because not picking within 2 of tempPoint
        		if(!connectionPos[j]
        				&&!connectionPos[aroundPixel.handleLoop(j+1)]
        				&&!connectionPos[aroundPixel.handleLoop(j-1)]){
        			Point tempAround=aroundPixel.getPoint(j,tempPoint);
	        		if(tempAround.x>=0
	        				&&tempAround.x<myCluster.getSize().x
	        				&&tempAround.y>=0
	        				&&tempAround.y<myCluster.getSize().y){
		        		if(distanceMap.getDistanceFromEdge(tempAround)>mostCenteredConnection){
        					mostCenteredConnection=distanceMap.getDistanceFromEdge(tempAround);
        					addPoint=j; 			
		        		}
	        			int currConnections=check4MostNewConnection(j,tempPoint);
        				if(currConnections>mostNewConnections){
        					mostConnected=aroundPixel.getPoint(j,tempPoint);
        					mostNewConnections=currConnections;
        					newConnectionPoint=this.getBridgePoint(j, tempPoint);
        				}	
	        		}
        		}
        	}
        	if(connections<3&&distanceMap.getDistanceFromEdge(tempPoint)>2){    		
        		if(connections<2){
	        		if(mostNewConnections>0&&mostConnected.x!=-1){
	        			skeleton.add(mostConnected);
	        			graph.addVertex(new Vertex(mostConnected));
	        			added=true;
	        		}
	        		else if(addPoint>=0){
	        			Point newTempPoint=aroundPixel.getPoint(addPoint,tempPoint);
	        			skeleton.add(newTempPoint);
	        			graph.addVertex(new Vertex(newTempPoint));
	        			added=true;
	        		}
        		}
        		if(!added&&mostConnected.x!=-1){
        			if(!graph.isConnected(tempPoint,newConnectionPoint)){
        				skeleton.add(mostConnected);
	        			graph.addVertex(new Vertex(mostConnected));
        			}
        		}
        		
        	}


        }

    }
    
    /**
     * this gets a point that is the bridge connection to another part of the
     * medial axis and returns the point(-1,-1) if there wasnt a bridge point
     * @param corner2Check the direction to look for a bridge based of aroundPixel
     * @param axisPoint a point on the medialAxis
     * @return the bridgeing point or (-1,-1)
     */
    private Point getBridgePoint(int corner2Check,Point axisPoint){
    	AroundPixel aroundPixel=new AroundPixel();
    	Point cornerConnection=new Point(-1,-1);
//    	for(int i=0;i<3;i++){
    		Point tempPoint = aroundPixel.getPoint(corner2Check, axisPoint);
//    		if(cornerConnected-1+i==-1){
//        		tempPoint=this.aroundPixel.getPoint(7, axisPoint);
//    		}
//    		else if(cornerConnected-1+i==8){
//        		tempPoint=this.aroundPixel.getPoint(0, axisPoint);
//    		}
//    		else{
//        		tempPoint=this.aroundPixel.getPoint(cornerConnected-1+i, axisPoint);
//    		}
    		if(this.skeleton.contains(tempPoint)){
    			return cornerConnection;
    		}
    		if(this.skeleton.contains(aroundPixel.getPoint(corner2Check,tempPoint))){
    			return aroundPixel.getPoint(corner2Check,tempPoint);
    		}
    		if(corner2Check-1<0){
        		if(this.skeleton.contains(aroundPixel.getPoint(7,tempPoint))){
        			return aroundPixel.getPoint(7,tempPoint);
        		}

    		}
    		else if(this.skeleton.contains(aroundPixel.getPoint(corner2Check-1,tempPoint))){
    			return aroundPixel.getPoint(corner2Check-1,tempPoint);
    		}
    		if(corner2Check+1>7){
        		if(this.skeleton.contains(aroundPixel.getPoint(0,tempPoint))){
        			return aroundPixel.getPoint(0,tempPoint);
        		}
    		}
    		else if(this.skeleton.contains(aroundPixel.getPoint(corner2Check+1,tempPoint))){
    			return aroundPixel.getPoint(corner2Check+1,tempPoint);
    		}

 //   	}
    	return cornerConnection;
    }
    /**
     * returns the number of connections a bridge point will give
     * 
     * @param corner2Check direction to check for bridge connections based on AroundPixel
     * @param axisPoint the point on the medial axis to bridge from
     * @return the number of connections the corner2Check has
     */
    private int check4MostNewConnection(int corner2Check,Point axisPoint){
    	AroundPixel aroundPixel=new AroundPixel();
//    	Point cornerConnection=new Point(-1,-1);
    	int connectionCount=0;
//    	for(int i=0;i<3;i++){
    		Point tempPoint = aroundPixel.getPoint(corner2Check, axisPoint);
//    		if(cornerConnected-1+i==-1){
//        		tempPoint=aroundPixel.getPoint(7, axisPoint);
//    		}
//    		else if(cornerConnected-1+i==8){
//        		tempPoint=aroundPixel.getPoint(0, axisPoint);
//    		}
//    		else{
//        		tempPoint=aroundPixel.getPoint(cornerConnected-1+i, axisPoint);
//    		}
    		if(this.skeleton.contains(tempPoint)){
    			return 0;
    		}
    		if(this.skeleton.contains(aroundPixel.getPoint(corner2Check,tempPoint))){
    			connectionCount++;
    		}
    		if(corner2Check-1<0){
        		if(this.skeleton.contains(aroundPixel.getPoint(7,tempPoint))){
        			connectionCount++;
        		}

    		}
    		else if(this.skeleton.contains(aroundPixel.getPoint(corner2Check-1,tempPoint))){
    			connectionCount++;
    		}
    		if(corner2Check+1>7){
        		if(this.skeleton.contains(aroundPixel.getPoint(0,tempPoint))){
        			connectionCount++;
        		}
    		}
    		else if(this.skeleton.contains(aroundPixel.getPoint(corner2Check+1,tempPoint))){
    			connectionCount++;
    		}

 //   	}
    	return connectionCount;
    }
	/**
	 * returns a list of points of this medial axis trimmed by
	 * removing points from the medial axis based on the distance from the edge
	 * any point that is less than minDistance will be removed
	 * @param minDistance the distance from edge that axis must be or be removed 
	 * @return a linklist of points of the trimmed medial axis
	 */
	public LinkedList<Point> trimMedialAxis(int minDistance){
		LinkedList<Point> trimmedAxis=new LinkedList<Point>();
		if(this.skeleton!=null){
			for(int i=0;i<this.skeleton.size();i++){
				if(distanceMap.getDistanceFromEdge(this.skeleton.get(i))>=minDistance){
					trimmedAxis.add(this.skeleton.get(i));
				}
			}
		}
		return trimmedAxis;

	}

    
	/**
	 * this is for testing it outputs the widths to console
	 * that were found creating the medial axis
	 */
	public void writeObjectWidths() {
		// TODO Auto-generated method stub
		System.out.print("Widths for this image: "+this.objectWidth[0]+","+this.objectWidth[1]);

	}


}

