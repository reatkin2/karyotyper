package MedialAxis;

import java.awt.Point;
import java.util.LinkedList;

public class DistanceMap {
    private int [][] distanceFromEdgeMatrix;

    public DistanceMap(){
    	this.distanceFromEdgeMatrix=null;
    }
    public DistanceMap(DistanceMap newMap){
    	this.copyDistanceMap(newMap);
    }
    public DistanceMap(int x,int y){
    	distanceFromEdgeMatrix=new int[x][y];
    	initDistanceMap();
    }
    
    private void initDistanceMap(){
    	for(int i=0;i<this.distanceFromEdgeMatrix.length;i++){
    		for(int j=0;j<this.distanceFromEdgeMatrix[0].length;j++){
    			this.distanceFromEdgeMatrix[i][j]=-5;
    		}
    	}

	}
	public Point getSizeOfDistanceMap(){
		return new Point(this.distanceFromEdgeMatrix.length,this.distanceFromEdgeMatrix[0].length);
	}
	public int getDistanceFromEdge(Point tempPoint){
		return this.distanceFromEdgeMatrix[tempPoint.x][tempPoint.y];
	}
	public void setDistanceFormEdge(Point tempPoint, int distance){
		this.distanceFromEdgeMatrix[tempPoint.x][tempPoint.y]=distance;

	}
	public LinkedList<Point> getTheEdge(){
		LinkedList<Point> edge=new LinkedList<Point>();
		for(int i=0;i<this.distanceFromEdgeMatrix[0].length;i++){
    		for(int j=0;j<this.distanceFromEdgeMatrix.length;j++){
    			if(this.distanceFromEdgeMatrix[j][i]==0){
    				edge.add(new Point(j,i));
    			}
    		}
    	}
		return edge;

	}
	public int getWidth(){
		return this.distanceFromEdgeMatrix.length;
	}
	public int getHeight(){
		return this.distanceFromEdgeMatrix[0].length;
	}
	private void copyDistanceMap(DistanceMap copyMap){
		if(copyMap.distanceFromEdgeMatrix!=null){
			this.distanceFromEdgeMatrix=new int[copyMap.distanceFromEdgeMatrix.length][copyMap.distanceFromEdgeMatrix[0].length];
	        for(int j=0;j<this.distanceFromEdgeMatrix.length;j++){
	        	for(int i=0; i<this.distanceFromEdgeMatrix[0].length;i++){
	        		this.distanceFromEdgeMatrix[j][i]=copyMap.distanceFromEdgeMatrix[j][i];
	        	}
	        }

		}
	}
    public void mapOut(){
    	for(int i=0;i<distanceFromEdgeMatrix[0].length;i++){
    		for(int j=0;j<distanceFromEdgeMatrix.length;j++){
    			if(distanceFromEdgeMatrix[j][i]==-5){
    				System.out.print('_');
    			}
    			else{
    				System.out.print(distanceFromEdgeMatrix[j][i]);
    			}
    		}
    		System.out.println("");
    	}
    }


}
