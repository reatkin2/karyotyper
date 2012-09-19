package basicObjects;
 /*
 * Shape.java
 *
 * Created on December 14, 2004, 7:09 PM
 */
import java.awt.Point;
import java.util.LinkedList;
import Target.TargetImage;
import Target.TargetShape;

/**
 *
 * @author  Andrew
 */
public class Shape {
	private AroundPixel aroundPixel;
    private boolean[][] shapeX;
    private int [][] distanceFromEdgeMatrix;
    private Point shapeSize;
    private Point screenCordinate;
    private Point firstPixel;
    private String title;
    private Shape next;
    private int pixelCount;
    private int sides;
    private LinkedList<Point> skeleton;
    private int chromosomeWidth[];
    private int biggestIncreaseSkeletonAtWidthCount;
    private int most2LeastRemovedAtWidthCount;
    private boolean removeThisShape;
    //double latLongs[];
    /** Creates a new instance of Shape */
    public Shape(int imageNum) {
    	shapeX=new boolean[50][50];
    	shapeSize=new Point(50,50);
        initShape();

    }
    public Shape() {
    	shapeX=new boolean[50][50];
    	shapeSize=new Point(50,50);
        initShape();
    }

    public Shape(Shape makeNew){
    	//System.out.println("ShapeSize: "+makeNew.shapeSize.x+","+makeNew.shapeSize.y);
    	shapeX=new boolean[makeNew.shapeSize.x][makeNew.shapeSize.y];
        initShape();
        copyShape(makeNew);
    }
    public Shape(Point size){
    	shapeX=new boolean[size.x][size.y];
        initShape();
    }
    public Shape(short[][] map,int xPoint,int yPoint,int shapeColorID){
    	shapeX=new boolean[map.length][map[0].length];
        initShape();
       setShape(map,xPoint,yPoint,shapeColorID); 
    }
    public Shape(int x,int y){
    	shapeX=new boolean[50][50];
    	shapeSize=new Point(50,50);
      initShape();
      screenCordinate=new Point(x,y);
    }
    private void initShape(){
        screenCordinate=new Point(-1,-1);
        firstPixel=new Point(-1,-1);
        title="";
        next=null;
        sides=0;
    	shapeSize=new Point(0,0);
    	pixelCount=0;
    	aroundPixel= new AroundPixel();
    	skeleton=new LinkedList<Point>();
    	distanceFromEdgeMatrix=null;
        biggestIncreaseSkeletonAtWidthCount=-1;
        most2LeastRemovedAtWidthCount=1;
        chromosomeWidth=new int[2];
        chromosomeWidth[0]=-1;
        chromosomeWidth[1]=-1;
        removeThisShape=true;

    	initArray();

    }
    private void initArray(){
        for(int j=0;j<shapeX[0].length;j++)
        	for(int i=0;i<shapeX.length;i++)
        		shapeX[i][j]=false;

    }
    public void setShape(short[][] map,int xPoint,int yPoint,int shapeColorID){
        int firstCol=-1;
        int firstRow=-1;
        int bottomRow=-1;
        int farthestRight=-1;
        initShape();
        int yRefPoint=yPoint;
        int xRefPoint=xPoint;
        if(shapeColorID==0){
        	yRefPoint-=(int)(map[0].length/2);
        	xRefPoint-=(int)(map.length/2);
        }
       for(int i=0;i<map[0].length;i++){            //find the first colom and row that contain anything
            for(int k=0;k<map.length;k++){
                if(map[k][i]==shapeColorID){
                	pixelCount++;
                   if(bottomRow<i){
                       //setScreenCordinate(screenCordinate.x,yRefPoint+i);//put y cordinate at bottom row
                        bottomRow=i;
                    }
                   if(farthestRight<k){
                   	//setScreenCordinate(xRefPoint+k,screenCordinate.y);//put x cordinate farthest rightcolumn
                   	farthestRight=k;
                   }

                    if(firstCol>k){
                        firstCol=k;
                       	setScreenCordinate(xRefPoint+k,screenCordinate.y);//put x cordinate farthest rightcolumn
                    }
                    if(firstRow==-1){
                        firstCol=k;
                        firstPixel.setLocation(k,i);
                        firstRow=i;
                        setScreenCordinate(screenCordinate.x,yRefPoint+i);//put y cordinate at bottom row
                    }
                }

            }
        }
      
        for(int i=firstRow;i<map[0].length;i++){                   //put map into shape
            for(int k=firstCol;k<map.length;k++){
                if(i-firstRow<shapeX.length&&k-firstCol<shapeX[0].length){
                	if(i-firstRow>=0&&k-firstCol>=0){
	                    if(map[k][i]==shapeColorID){
	                    	shapeX[k-firstCol][i-firstRow]=true;
	                    }
                	}
                }
                
            }
        }
        //firstPixel.setLocation(firstPixel.x-firstCol,firstPixel.y-firstRow);
        this.shapeSize=new Point(calcSize());

    }
    public boolean isSame(Shape isShape){
    	for(int j=0;j<shapeX[0].length;j++)
    		for(int i=0;i<shapeX.length;i++)
	          if(shapeX[i][j]!=isShape.getPos(i, j))
	            return false;
      return true;
    }
	public int getPixelCount(){
		return pixelCount;
	}
	public int getWidths(int pos){
		return this.chromosomeWidth[pos];
	}
	public boolean getPos(int x,int y){
        return shapeX[x][y];        
    }
    public boolean getPos(Point posX){
    	return shapeX[posX.x][posX.y];
    }
	public boolean getValue(int x,int y){
        return shapeX[x][y];        
    }
    public boolean getValue(Point posX){
    	return shapeX[posX.x][posX.y];
    }
    public void setPos(int x, int y){
        if(!shapeX[x][y])
        	pixelCount++;
    	shapeX[x][y]=true;
    }
    public void setPixel(Point xy,boolean newValue){
    	if(!newValue&&shapeX[xy.x][xy.y]){
    		this.pixelCount--;
    	}
    	else if(newValue&&!shapeX[xy.x][xy.y]){
    		this.pixelCount++;
    	}
    	shapeX[xy.x][xy.y]=newValue;

    }
    public void setPosNot(int x, int y){
        if(shapeX[x][y])
        	pixelCount--;
    	shapeX[x][y]=false;
    }
    public void setPoint(int x, int y){
        setScreenCordinate(x,y);
    }
    public Point getScreenCordinate(){
        return screenCordinate;
    }
    public Point getSCcenter(){
    	int x=screenCordinate.x+((int)Math.floor(this.shapeSize.x/2));
       	int y=screenCordinate.y+((int)Math.floor(this.shapeSize.y/2));
    	return new Point(x,y);
    }
    public void setScreenCordinate(int x, int y){
    	if(x<0)
    		x=0;
    	if(y<0)
    		y=0;
    	this.screenCordinate.setLocation(x,y);
    }
    public void setScreenCordinate(Point xy){
    	if(xy.x<0)
    		xy.x=0;
    	if(xy.y<0)
    		xy.y=0;
    	this.screenCordinate.setLocation(xy.x,xy.y);
    }
    public void setMedialAxis(LinkedList<Point> tempMedialAxis){
    	this.skeleton=tempMedialAxis;
    }
    public Point getSize(){
		return shapeSize;
	}
    public void setSize(int x, int y){
    	shapeSize.x=x;
    	
    	shapeSize.y=y;
    }
    public Point calcSize(){
    	Point size=new Point(0,0);
      for(int j=0;j<shapeX[0].length;j++){
    	  for(int i=0; i<shapeX.length;i++){
          if(shapeX[i][j]==true){
            if(i>size.x){
              size.x=i;
            }
            if(j>size.y){
              size.y=j;
            } 
          }
        }
      }
      size.y++;
      size.x++;
      return size;
    }
    
    public Shape getNext(){
      return next;
    }
    public void setNext(Shape shapeN){
      next=shapeN;
    }
    public String getTitle(){
      return title;
    }
    public void setTitle(String wordTitle){
      title=wordTitle;
    }
    public boolean[][] getShape(){
    	return shapeX;
    }
    public void setSides(int sidesX){
    	sides=sidesX;
    }
    public int getSides(){
    	return sides;
    }
    public void setkeepThisShape(){
    	this.removeThisShape=false;
    }
    public boolean checkKeepThisShape(){
    	return (!this.removeThisShape);
    }
    public void copyShape(Shape copyShape){
    	this.removeThisShape=copyShape.removeThisShape;
    	boolean foundFirstPix=false;
    	this.next=copyShape.next;
    	//this.myBuckets.copyBuckets(copyShape.myBuckets.getBucketArray());
    	setScreenCordinate(copyShape.getScreenCordinate());
        title=copyShape.getTitle();
        this.biggestIncreaseSkeletonAtWidthCount=copyShape.biggestIncreaseSkeletonAtWidthCount;
        this.most2LeastRemovedAtWidthCount=copyShape.most2LeastRemovedAtWidthCount;

    	this.skeleton=copyShape.getSkeltonPoints();
    	if(copyShape.distanceFromEdgeMatrix!=null){
    		this.distanceFromEdgeMatrix=new int[copyShape.distanceFromEdgeMatrix.length][copyShape.distanceFromEdgeMatrix[0].length];
            for(int j=0;j<distanceFromEdgeMatrix.length;j++){
            	for(int i=0; i<distanceFromEdgeMatrix[0].length;i++){
            		this.distanceFromEdgeMatrix[j][i]=copyShape.distanceFromEdgeMatrix[j][i];
            	}
            }

    	}
        for(int j=0;j<shapeX[0].length;j++){
        	for(int i=0; i<shapeX.length;i++){
              if(copyShape.getPos(i,j)){
            	  this.pixelCount++;
            	  if(!foundFirstPix){
            		  firstPixel.setLocation(i,j);
            		  foundFirstPix=true;
            	  }
            	  shapeX[i][j]=true; 
              }
            }
          }
        this.chromosomeWidth[0]=copyShape.chromosomeWidth[0];
        this.chromosomeWidth[1]=copyShape.chromosomeWidth[1];
        
        this.shapeSize=new Point(copyShape.shapeSize);
    }
    public void shapeOut(){
    	System.out.println("ScreenLocation:"+this.screenCordinate.x+","+this.screenCordinate.y);
    	System.out.println(this.getSize().x+","+this.getSize().y+" : "+this.getTitle());
    	System.out.println("PixelCount: "+pixelCount);
    	for(int i=0;i<this.getSize().y;i++){
    		for(int j=0;j<this.getSize().x;j++){
    			if(this.getPos(j, i)){
    				System.out.print('M');
    			}
    			else{
    				System.out.print('_');
    			}
    		}
    		System.out.println("");
    	}
    }
    public void matrixOut(int[][] matrix){
    	System.out.println(this.getSize().x+","+this.getSize().y+" : "+this.getTitle());
    	System.out.println("PixelCount: "+pixelCount);
    	for(int i=0;i<matrix[0].length;i++){
    		for(int j=0;j<matrix.length;j++){
    			if(matrix[j][i]==-5){
    				System.out.print('_');
    			}
    			else{
    				System.out.print(matrix[j][i]);
    			}
    		}
    		System.out.println("");
    	}
    }
    public int calcSides(){
    	int numSides=0;
    	
    	return numSides;
    }
    public Point getFirstPixel(){
    	return firstPixel;
    }

    private int getClockWiseOutline(int direction,Point currPnt,int count){
		if(count++>7){
			return -1;
		}
    	if(direction<0){
			direction=7;
		}
		Point tempPnt=new Point(currPnt.x+aroundPixel.getPos(direction).x,currPnt.y+aroundPixel.getPos(direction).y);
		if(shapeX.length>tempPnt.x&&tempPnt.x>=0
				&&shapeX[0].length>tempPnt.y&&tempPnt.y>=0){
			if(this.getPos(tempPnt)){//find the next spot thats part of the shape
				//System.out.println("good pnt@"+(currPnt.x+aroundPixel.getPos(direction).x)+","+(currPnt.y+aroundPixel.getPos(direction).y));//error checking
				return direction;
			}
		}
		//rotate around till next spot found
		return getClockWiseOutline(--direction,currPnt,count);
    		//tempPnt.setLocation(currPnt.x+aroundPixel.getPos(direction).x,currPnt.y+aroundPixel.getPos(direction).y);
    		//System.out.println("try pnt: "+direction+"&Point: "+tempPnt);

    }
	private int tryClockWiseDirection(int direction){
   		switch(direction){
			case 0:
				direction=1;
				break;
			case 1:
			case 2:
				direction=3;
				break;
			case 3:
			case 4:
				direction=5;
				break;
			case 5:
			case 6:
				direction=7;
				break;
			case 7:
				direction=1;
				break;
   		}
   		return direction;
	}
	public int getCWOutline(int direction,Point currPnt){
		return  getClockWiseOutline(tryClockWiseDirection(direction), currPnt,0);
	}

	public Point projectToCenter(Point start,int xdir,int ydir){
//		Point end=new Point((int)(Math.round(this.shapeSize.x/2.0)),(int)Math.round(this.shapeSize.y/2.0));
//		double slope=getSlope(start,end);
//		if(slope==0){
//			slope=.0001;
//		}
//		double intercept=(double)start.y-(slope*(double)start.x);
		int partX=start.x;
		int partY=start.y;
		while(partX<this.shapeSize.x&&partY<this.shapeSize.y&&partX>=0&&partY>=0){
			//if(slope>1){
				//partX=(int)Math.round(((double)partY-intercept)/slope);
				if(partX>=0&&partX<this.shapeSize.x&&partY>=0&&partY<this.shapeSize.y){
					if(shapeX[partX][partY]){
						return new Point(partX,partY);
					}
					partY+=ydir;
					partX+=xdir;
				}
			//}
//			else{
//				partY=(int)Math.round(((double)partX*slope)+intercept);
//				if(partX>=0&&partX<this.shapeSize.x&&partY>=0&&partY<this.shapeSize.y){
//					if(shapeX[partX][partY]){
//						return new Point(partX,partY);
//					}
//				}
//				partX+=xdir;
//			}
		}
		return new Point(-1,-1);
	}
	public double getSlope(Point start, Point end){
		if(end.x-start.x==0){
			return 1000.01;
		}
		return (((double)end.y-(double)start.y)/((double)end.x-(double)start.x));
	}
	public void trimShape(int depth){
		LinkedList<Point> pointList=new LinkedList<Point>();
//		int xdir=1;
//		int ydir=1;
		for(int k=0;k<depth;k++){
			for(int i=0;i<this.shapeSize.x;i++){
//				if(i>=this.shapeSize.x/2.0){
//					xdir=-1;
//				}
//				else{
//					xdir=1;
//				}
				pointList.push(projectToCenter(new Point(i,0),0,1));
				pointList.push(projectToCenter(new Point(i,this.shapeSize.y-1),0,-1));
			}
			for(int j=0;j<this.shapeSize.y;j++){
//				if(j>=this.shapeSize.y/2.0){
//					ydir=-1;
//				}
//				else{
//					ydir=1;
//				}
				pointList.push(projectToCenter(new Point(0,j),1,0));
				pointList.push(projectToCenter(new Point(this.shapeSize.x-1,j),-1,0));	
			}
			while(!pointList.isEmpty()){
				Point temp=pointList.pop();
				if(temp.x!=-1&&temp.y!=-1){
					if(shapeX[temp.x][temp.y]){
						this.pixelCount--;
					}
					this.shapeX[temp.x][temp.y]=false;
				}
			}
		}
	}
    public LinkedList<Point> getSkeltonPoints(){
    	return this.skeleton;
    }
    public LinkedList<Point> getSkeleton(TargetImage img){
    	//Rectangle centerImage=new Rectangle((int)Math.round((bounds.getWidth()/2)-(bounds.getWidth()/4)),(int)Math.round((bounds.getHeight()/2)-(bounds.getHeight()/4)),
    	//						(int)Math.round(bounds.getWidth()/2),(int)Math.round(bounds.getHeight()/2));
        //LinkedList<Shape> allShapes=new LinkedList<Shape>();//create a linked list to store shapes in
    	LinkedList<Point> removeEdgePointsHorizantal=new LinkedList<Point>();
    	LinkedList<Point> removeEdgePointsVertical=new LinkedList<Point>();
    	this.skeleton=new LinkedList<Point>();
    	Shape temp=new Shape(this);
    	this.distanceFromEdgeMatrix=new int[temp.getSize().x][temp.getSize().y];
    	for(int i=0;i<distanceFromEdgeMatrix.length;i++){
    		for(int j=0;j<distanceFromEdgeMatrix[0].length;j++){
    			distanceFromEdgeMatrix[i][j]=-5;
    		}
    	}
    	Point pointRightEdge=new Point(0,0);
    	int objectWidth=0;
    	boolean inObject=false;
    	boolean beforeObjectEdge=true;
    	int lastPixelCount=temp.getPixelCount();
    	int lastSkeletonCount=-1;
    	int distanceFromEdgeCount=0;
    	int removedLastTime=0;
    	int most2LeastRemoved=0;
    	int skeletonSizeLastTime=0;
    	int biggestIncreaseInSkeleton=0;
        while(skeleton.size()>lastSkeletonCount||lastPixelCount!=temp.pixelCount){
        	lastSkeletonCount=skeleton.size();
        	lastPixelCount=temp.getPixelCount();
	        for(int i=0;i<temp.getSize().x;i++){//loop width
	            for(int j=0;j<temp.getSize().y;j++){//loop height
	    			if(temp.getPos(i, j)){
	    				if(beforeObjectEdge){
	    					removeEdgePointsHorizantal.push(new Point(i,j));
	    				}
	    				objectWidth++;
    					beforeObjectEdge=false;
						inObject=true;
	    				pointRightEdge=new Point(i,j);
	    			}
	    			else{
	    				if(inObject){
	    					if(objectWidth<=2){
	    						Point tempPoint=removeEdgePointsHorizantal.pop();
	    						if(!skeleton.contains(tempPoint)){
	    							skeleton.add(tempPoint);
	    						}
	    					}
	    					removeEdgePointsHorizantal.push(pointRightEdge);
	    					pointRightEdge=new Point(0,0);
	    				}
    					inObject=false;
    					beforeObjectEdge=true;
    					objectWidth=0;
	    			}
	            }
	            if(inObject){
					if(objectWidth<=2){
						Point tempPoint=removeEdgePointsHorizantal.pop();
						if(!skeleton.contains(tempPoint)){
							skeleton.add(tempPoint);
						}
					}
					inObject=false;
					beforeObjectEdge=true;
					removeEdgePointsHorizantal.push(pointRightEdge);
					pointRightEdge=new Point(0,0);
				}
	            objectWidth=0;
	        }
	        objectWidth=0;
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
	    				objectWidth++;
	    			}
	    			else{
	    				if(inObject){
	    					if(objectWidth<=2){
	    						Point tempPoint=removeEdgePointsVertical.pop();
	    						if(!skeleton.contains(tempPoint)){
	    							skeleton.add(tempPoint);
	    						}
	    					}
	    					removeEdgePointsVertical.push(pointRightEdge);
	    					pointRightEdge=new Point(0,0);
	    				}
    					inObject=false;
    					beforeObjectEdge=true;
    					objectWidth=0;
	    			}
	            }
	            if(inObject){
					if(objectWidth<=2){
						Point tempPoint=removeEdgePointsVertical.pop();
						if(!skeleton.contains(tempPoint)){
							skeleton.add(tempPoint);
						}
					}
					inObject=false;
					beforeObjectEdge=true;
					removeEdgePointsVertical.push(pointRightEdge);
					pointRightEdge=new Point(0,0);
				}
	            objectWidth=0;
	        }
	        //remove skeleton points from the list, identified by be duplicate in this list

	        while(!removeEdgePointsVertical.isEmpty()){
	        	Point removePoint=removeEdgePointsVertical.pop();
	        	temp.setPixel(removePoint,false);
	        	if(distanceFromEdgeMatrix[removePoint.x][removePoint.y]==-5){
	        		distanceFromEdgeMatrix[removePoint.x][removePoint.y]=distanceFromEdgeCount;
	        	}
	        }
	        while(!removeEdgePointsHorizantal.isEmpty()){
	        	Point removePoint=removeEdgePointsHorizantal.pop();
	        	temp.setPixel(removePoint,false);
	        	if(distanceFromEdgeMatrix[removePoint.x][removePoint.y]==-5){
	        		distanceFromEdgeMatrix[removePoint.x][removePoint.y]=distanceFromEdgeCount;
	        	}
	        }
	        for(int i=0;i<skeleton.size();i++){
	        	temp.setPixel(skeleton.get(i), true);
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
//	        temp.shapeOut();
//	        temp.matrixOut(this.distanceFromEdgeMatrix);
        }
        this.chromosomeWidth[0]=this.biggestIncreaseSkeletonAtWidthCount;
        this.chromosomeWidth[1]=this.most2LeastRemovedAtWidthCount;
        return skeleton;
    }    
	public void writeShapeWidths() {
		// TODO Auto-generated method stub
		System.out.print("Widths for this image: "+this.chromosomeWidth[0]+","+this.chromosomeWidth[0]);

	}


}
