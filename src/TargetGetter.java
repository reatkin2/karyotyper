
import Color.ColorBuckets;
import Color.PixelColor;
import Target.TargetArray;
import Target.TargetImage;
import Target.TargetShape;
import TargetText.TextImage;
import basicObjects.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.imageio.ImageIO;


public class TargetGetter {
    public TargetArray shapeList;
    public TargetArray partShapeList;
    private FlightPath flightPath;
    /*
     * aroundot is an array of 8 points that is a x,y difference from the center point pixel
     * 107       |-1,-1| 0,-1| 1,-1|
     * 2.6   or  |-1, 0| dot | 1, 0|     <--- this is a visual of around dot
     * 345	     |-1, 1| 0, 1| 1, 1|
     */
    private AroundPixel aroundDot;
    private int targetImgBorderSize;
    private boolean[][] screenChecked;
    private boolean[][] spotNext;
    private int currPixelCount;
    private String imageFolderPath;
    private LinkedList<String> imageQue;
    private	TargetImage img;
    private int distanceToIncludeTargets;
//    private int maxPixelCount;
//    private int minPixelCount;
    private double maxLength;
//    private double minLength;
    private double minWidth;
    private double minHeight;
    private int allowedColorDiff;
    private int pixelSpace;
    private Color colorAverage;
    private int colorCount;
    private int minColorNShape;
    private int maxColorNShape;
    private int targetExcludeDistance;
    private ColorBuckets myBuckets;
    private int firstPixelMax;
    private int firstPixelMin;
    private double firstLengthMax;
    private double firstLengthMin;
    private int firstColorCountMax;
    private int firstColorCountMin;
    private int textImageRotationCount;
    //private Color imgAvgColor;
    private int firstPassCount;
    private int removedCount;
    private long start;
    private static final double feetPerDegreeLatLong=364169.55420532933;//at lat 38.14;//at lat 38.14
   // private int awayFromEdge;
    private boolean onImgEdge;
	public TargetGetter(String jpegPath){
		//super();
		flightPath=new FlightPath();
		aroundDot=new AroundPixel();
		this.targetImgBorderSize=35;//target image border in pixels
		this.distanceToIncludeTargets=120;//70feet
		textImageRotationCount=20;
		this.imageFolderPath=jpegPath;
		this.imageQue=new LinkedList<String>();
		start=System.currentTimeMillis();
		this.onImgEdge=false;
		//this.awayFromEdge=2;//10 in feet
		this.targetExcludeDistance=80;//80 in feet
		this.firstPixelMax=5000;//4000
		this.firstPixelMin=30;//270
//	    this.maxPixelCount=1800;//1800
//	    this.minPixelCount=500;//500
	    //this.maxLength=11;//11 foot
	    this.maxLength=13;//11 foot
	    //this.minLength=3;//3foot
	    //this.minWidth=4.3;
	    this.minWidth=2.5;
	    //this.minHeight=2.8;
	    this.minHeight=2.5;
	    //this.firstLengthMax=15;//20feet
	    this.firstLengthMax=20;//20feet
	    //this.firstLengthMin=2.8;//31 foot
	    this.firstLengthMin=2.5;//31 foot
	    //this.allowedColorDiff=70;//70
	    this.allowedColorDiff=70;//70
	    this.pixelSpace=3;//7
	    this.maxColorNShape=190;//60
	    this.minColorNShape=15;//17
	    this.firstColorCountMax=200;//100
	    this.firstColorCountMin=11;//13
	    colorAverage=new Color(0,0,0);
	    this.colorCount=0;
        //screenChecked=new boolean[super.resolution.x][super.resolution.y];
		loadFiles();
		currPixelCount=0;
		img = null;
		//loadNewImage(filename);
        //initScreenChecked();

	}
	public void loadNewImage(String filename){
			img = new TargetImage(filename,TargetGetter.feetPerDegreeLatLong);
	        screenChecked=new boolean[img.getImgWidth()][img.getImgHeight()];
	        this.spotNext=new boolean[img.getImgWidth()][img.getImgHeight()];
	        flightPath.addPoint(img);
	        flightPath.writePath("flightPath");
        	//this.pixelCountAGL=(int)Math.round(((this.aboveGroundLevel*-18.14736)+3918.95752));
//        	this.maxPixelCount=img.getPixelCountAGL()+1200;
//        	this.minPixelCount=img.getPixelCountAGL()-1300;
	}
	public void initScreenChecked(){
//		Color temp;
//		int red=0;
//		int green=0;
//		int blue=0;
//		int count=0;
		for(int i=0;i<screenChecked.length;i++){
			for(int j=0;j<screenChecked[0].length;j++){
				this.screenChecked[i][j]=false;
//				if(i%5==0&&j%5==0){
//	        		temp=this.convertPixel(img.getRGB(i,j));
//	        		red+=temp.getRed();
//	        		green+=temp.getGreen();
//	        		blue+=temp.getBlue();	
//	        		count++;
//				}
			}
		}
//		this.imgAvgColor=new Color((red/count) , (green/count) , (blue/count));
//		System.out.println("avgColor= "+this.imgAvgColor.getRed()+","+this.imgAvgColor.getGreen()+","+this.imgAvgColor.getBlue());
	}

	public void initSpotNext(){
		for(int i=0;i<spotNext.length;i++){
			for(int j=0;j<spotNext[0].length;j++){
				this.spotNext[i][j]=false;
			}
		}
	}
	public void loadFiles(){
		shapeList=new TargetArray(TargetGetter.feetPerDegreeLatLong,this.distanceToIncludeTargets);//load all identified shapes
		//shapeList.loadShapeArray("shapeData/woodShapeList.shp");//load newly identified shapes
		this.partShapeList=new TargetArray(TargetGetter.feetPerDegreeLatLong,this.distanceToIncludeTargets);
	}	
    /**
     *gets all connected matching colored pixels and returns shape
     * on the 2d integer array -canvas- painted by the number -shapeID-
     *@param bounds is a rectangle that only pixels inside of will be checked
     *@param colorOItem is the color to match
     *@param xyCor is the starting pixel
     *@param xyCanvas is the starting position on the 2d integer canvas
     *@param canvas is the 2d integer canvas that stores the shape(IMPORTANT:
     *				expected to have all positions not checked initialized to the value -5)
     *@param shapeID is the number entered on canvas that represents matching connected pixels
     *@return the 2d integer canvas that represents the shape
     */
	private short[][] getMatchingPixel(Rectangle bounds,Color colorOItem,Point xyCor,Point xyCanvas,short[][] canvas,short shapeID,Color colorLeft){
		LinkedList<PixelPoint> foundList=new LinkedList<PixelPoint>();
		colorLeft=new Color(0,0,0);
		Point canvasDiff=new Point(xyCanvas.x-xyCor.x,xyCanvas.y-xyCor.y);
		this.initSpotNext();
		//Color orignalColor=new Color(colorOItem.getRed(),colorOItem.getGreen(),colorOItem.getBlue());
			foundList.add(new PixelPoint(xyCor,colorOItem,colorLeft));
			while(!foundList.isEmpty()){
				PixelPoint curr=foundList.pop();
				colorOItem=new Color(curr.getPrevColor().getRed(),curr.getPrevColor().getGreen(),curr.getPrevColor().getBlue());
				xyCor=new Point(curr.getImgPoint());
				colorLeft=new Color(curr.getColorLeft().getRed(),curr.getColorLeft().getGreen(),curr.getColorLeft().getBlue());
				
			//if((colorLeft.getRed()+colorLeft.getGreen()+colorLeft.getBlue())>(600-this.allowedColorDiff)){
		    	for(int i=0; i<8;i++){//loop 8 times once for every position around the center pixel
		    		/*
		    		 *if the spot to be checked is inside of the bounds 
		    		 *and if the pixel is inside the visible resolution of the screen
		    		 */
		    			if(xyCor.x+aroundDot.getPos(i).x<img.getImgWidth()&&xyCor.y+aroundDot.getPos(i).y<img.getImgHeight()
			            		&&xyCor.x>0&&xyCor.y>0){
		    	    		if(!screenChecked[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]){
			        		//if the spot to be checked value is -5 meaning it hasnt been checked yet
				        	if(!bounds.contains(new Point(xyCor.x+canvasDiff.x+aroundDot.getPos(i).x,xyCor.y+canvasDiff.y+aroundDot.getPos(i).y))
				        			||canvas[xyCor.x+canvasDiff.x+aroundDot.getPos(i).x][xyCor.y+canvasDiff.y+aroundDot.getPos(i).y]==-5){
				        		//System.out.println("Checking color at pixel:"+(xyCor.x+aroundDot.getPos(i).x)+","+(xyCor.y+aroundDot.getPos(i).y));
				        		//if the pixel at the position aroundDot matches the color
				        		Color temp=img.getColorAt(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y);
				        		if(PixelColor.isTargeTColor2(temp)){//was isTargeTColor2(this.imgAvgColor,temp)
					        		this.colorAverage=averageColor(temp);
					    			screenChecked[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]=true;
					        		this.currPixelCount++;
				        			//paint the canvas at the respectful position on 2d canvas to the shapeID number
					        		if(bounds.contains(new Point(xyCor.x+canvasDiff.x+aroundDot.getPos(i).x,xyCor.y+canvasDiff.y+aroundDot.getPos(i).y))){
					        				canvas[xyCor.x+canvasDiff.x+aroundDot.getPos(i).x][xyCor.y+canvasDiff.y+aroundDot.getPos(i).y]=shapeID;
					        		}
				        			//recursively call this method with the new position on screen and on 2d canvas to be checked around
				     		        //getMatchingPixel(bounds,temp,new Point(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y),new Point(xyCanvas.x+aroundDot.getPos(i).x,xyCanvas.y+aroundDot.getPos(i).y),canvas,shapeID,colorLeft);
									if(!this.spotNext[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]){
										foundList.add(new PixelPoint(new Point(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y),temp,colorLeft));
										this.spotNext[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]=true;
									}
				        		}
				        		
				        	}
				        }
			    	}
		    		else{
		    			this.onImgEdge=true;
		    		}
		    	}
			}
			return canvas;
	    }
    /**
     *gets all connected matching colored pixels and returns shape
     * on the 2d integer array -canvas- painted by the number -shapeID-
     *@param bounds is a rectangle that only pixels inside of will be checked
     *@param colorOItem is the color to match
     *@param xyCor is the starting pixel
     *@param xyCanvas is the starting position on the 2d integer canvas
     *@param canvas is the 2d integer canvas that stores the shape(IMPORTANT:
     *				expected to have all positions not checked initialized to the value -5)
     *@param shapeID is the number entered on canvas that represents matching connected pixels
     *@return the 2d integer canvas that represents the shape
     */
	private short[][] getMatchingPixelLeft(Rectangle bounds,Color colorOItem,Point xyCor,Point xyCanvas,short[][] canvas,short shapeID,Color colorLeft){
		LinkedList<PixelPoint> foundList=new LinkedList<PixelPoint>();
		colorLeft=new Color(0,0,0);
		Point canvasDiff=new Point(xyCanvas.x-xyCor.x,xyCanvas.y-xyCor.y);
		this.initSpotNext();
		//Color orignalColor=new Color(colorOItem.getRed(),colorOItem.getGreen(),colorOItem.getBlue());
			foundList.add(new PixelPoint(xyCor,colorOItem,colorLeft));
			while(!foundList.isEmpty()){
				PixelPoint curr=foundList.pop();
				colorOItem=new Color(curr.getPrevColor().getRed(),curr.getPrevColor().getGreen(),curr.getPrevColor().getBlue());
				xyCor=new Point(curr.getImgPoint());
				colorLeft=new Color(curr.getColorLeft().getRed(),curr.getColorLeft().getGreen(),curr.getColorLeft().getBlue());
				
			//if((colorLeft.getRed()+colorLeft.getGreen()+colorLeft.getBlue())>(600-this.allowedColorDiff)){
			if(colorLeft.getRed()<2){
		    	for(int i=0; i<8;i++){//loop 8 times once for every position around the center pixel
		    		/*
		    		 *if the spot to be checked is inside of the bounds 
		    		 *and if the pixel is inside the visible resolution of the screen
		    		 */
		    			if(xyCor.x+aroundDot.getPos(i).x<img.getImgWidth()&&xyCor.y+aroundDot.getPos(i).y<img.getImgHeight()
			            		&&xyCor.x>0&&xyCor.y>0){
		    	    		if(!screenChecked[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]){
			        		//if the spot to be checked value is -5 meaning it hasnt been checked yet
				        	if(!bounds.contains(new Point(xyCor.x+canvasDiff.x+aroundDot.getPos(i).x,xyCor.y+canvasDiff.y+aroundDot.getPos(i).y))
				        			||canvas[xyCor.x+canvasDiff.x+aroundDot.getPos(i).x][xyCor.y+canvasDiff.y+aroundDot.getPos(i).y]==-5){
				        		//System.out.println("Checking color at pixel:"+(xyCor.x+aroundDot.getPos(i).x)+","+(xyCor.y+aroundDot.getPos(i).y));
				        		//if the pixel at the position aroundDot matches the color
				        		Color temp=img.getColorAt(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y);
					        		this.colorAverage=averageColor(temp);
					    			screenChecked[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]=true;
					        		this.currPixelCount++;
				        			//paint the canvas at the respectful position on 2d canvas to the shapeID number
					        		if(bounds.contains(new Point(xyCor.x+canvasDiff.x+aroundDot.getPos(i).x,xyCor.y+canvasDiff.y+aroundDot.getPos(i).y))){
					        				canvas[xyCor.x+canvasDiff.x+aroundDot.getPos(i).x][xyCor.y+canvasDiff.y+aroundDot.getPos(i).y]=shapeID;
					        		}
				        			//recursively call this method with the new position on screen and on 2d canvas to be checked around
				     		        //getMatchingPixel(bounds,temp,new Point(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y),new Point(xyCanvas.x+aroundDot.getPos(i).x,xyCanvas.y+aroundDot.getPos(i).y),canvas,shapeID,colorLeft);
									if(!this.spotNext[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]){
										foundList.add(new PixelPoint(new Point(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y),temp,colorLeft));
										this.spotNext[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]=true;
									}				        		
				        	}
				        }
			    	}
		    		else{
		    			this.onImgEdge=true;
		    		}
		    	}
			}
			}
			return canvas;
	    }
//    private int[][] getMatchingPixel(Rectangle bounds,Color colorOItem,Point xyCor,Point xyCanvas,int[][] canvas,int shapeID,Color colorLeft){
//		if((colorLeft.getRed()+colorLeft.getGreen()+colorLeft.getBlue())>(600-this.allowedColorDiff)){
//
//	    	for(int i=0; i<8;i++){//loop 8 times once for every position around the center pixel
//	    		/*
//	    		 *if the spot to be checked is inside of the bounds 
//	    		 *and if the pixel is inside the visible resolution of the screen
//	    		 */
//	    			if(this.currPixelCount<this.maxPixelCount+20&&bounds.contains(new Point(xyCanvas.x+aroundDot.getPos(i).x,xyCanvas.y+aroundDot.getPos(i).y))
//		            		&&xyCor.x+aroundDot.getPos(i).x<img.getWidth()&&xyCor.y+aroundDot.getPos(i).y<img.getHeight()
//		            		&&xyCor.x>0&&xyCor.y>0){
//	    	    		if(!screenChecked[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]){
//		        		//if the spot to be checked value is -5 meaning it hasnt been checked yet
//			        	if(canvas[xyCanvas.x+aroundDot.getPos(i).x][xyCanvas.y+aroundDot.getPos(i).y]==-5){
//			        		//System.out.println("Checking color at pixel:"+(xyCor.x+aroundDot.getPos(i).x)+","+(xyCor.y+aroundDot.getPos(i).y));
//			        		//if the pixel at the position aroundDot matches the color
//			        		Color temp=this.convertPixel(img.getRGB(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y));
//			        		if(IsColor.isTargeTColor2(colorOItem,temp)){
//				        		this.colorAverage=averageColor(temp);
//				    			screenChecked[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]=true;
//				        		this.currPixelCount++;
//			        			//paint the canvas at the respectful position on 2d canvas to the shapeID number
//			        			canvas[xyCanvas.x+aroundDot.getPos(i).x][xyCanvas.y+aroundDot.getPos(i).y]=shapeID;
//			        			//recursively call this method with the new position on screen and on 2d canvas to be checked around
//			     		        getMatchingPixel(bounds,temp,new Point(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y),new Point(xyCanvas.x+aroundDot.getPos(i).x,xyCanvas.y+aroundDot.getPos(i).y),canvas,shapeID,colorLeft);
//			        		}
//			        		else{
//				        		int red=colorLeft.getRed()-Math.abs(colorOItem.getRed()-temp.getRed());
//				        		int green=colorLeft.getGreen()-Math.abs(colorOItem.getGreen()-temp.getGreen());
//				        		int blue=colorLeft.getBlue()-Math.abs(colorOItem.getBlue()-temp.getBlue());
//				        		if(red<=0||green<=0||blue<=0){
//				        			red=0;
//				        			green=0;
//				        			blue=0;
//				        		}
//				        		colorLeft=new Color(red,green,blue);
//				        		this.currPixelCount++;
//			        			//paint the canvas at the respectful position on 2d canvas to the shapeID number
//			        			canvas[xyCanvas.x+aroundDot.getPos(i).x][xyCanvas.y+aroundDot.getPos(i).y]=shapeID;
//			        			//recursively call this method with the new position on screen and on 2d canvas to be checked around
//			      		        getMatchingPixel(bounds,colorOItem,new Point(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y),new Point(xyCanvas.x+aroundDot.getPos(i).x,xyCanvas.y+aroundDot.getPos(i).y),canvas,shapeID,colorLeft);
//			        		}
//			        		
//			        	}
//			        }
//		    	}
//	    	}
//		}
//	    return canvas;//return the 2d integer canvas
//
//    }
    private short[][] getMatchingPixelExtra(Rectangle bounds,Color colorOItem,Point xyCor,Point xyCanvas,short[][] canvas,short shapeID,Color colorLeft){
//    	if(count>7){
//    		if(match/count<0.70){
//    			return canvas;
//    		}
//    	}
		if((colorLeft.getRed()+colorLeft.getGreen()+colorLeft.getBlue())>(600-this.allowedColorDiff)){

	    	for(int i=0; i<8;i++){//loop 8 times once for every position around the center pixel
	    		/*
	    		 *if the spot to be checked is inside of the bounds 
	    		 *and if the pixel is inside the visible resolution of the screen
	    		 */
	    			if(this.currPixelCount<this.firstPixelMax&&bounds.contains(new Point((xyCanvas.x+aroundDot.getPos(i).x-1),(xyCanvas.y+aroundDot.getPos(i).y-1)))
		            		&&xyCor.x+aroundDot.getPos(i).x<img.getImgWidth()&&xyCor.y+aroundDot.getPos(i).y<img.getImgHeight()
		            		&&xyCor.x>0&&xyCor.y>0&&canvas.length<xyCanvas.x+aroundDot.getPos(i).x&&canvas[0].length<xyCanvas.y+aroundDot.getPos(i).y){
	    	    		//if(!screenChecked[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]){
		        		//if the spot to be checked value is -5 meaning it hasnt been checked yet
			        	if(canvas[xyCanvas.x+aroundDot.getPos(i).x][xyCanvas.y+aroundDot.getPos(i).y]==-5){
			        		//System.out.println("Checking color at pixel:"+(xyCor.x+aroundDot.getPos(i).x)+","+(xyCor.y+aroundDot.getPos(i).y));
			        		//if the pixel at the position aroundDot matches the color
			        		Color temp=img.getColorAt(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y);
			        		myBuckets.dropNBucket(temp);
			        		if(PixelColor.isTargeTColorX(colorOItem,temp)){
				    			//screenChecked[xyCor.x+aroundDot.getPos(i).x][xyCor.y+aroundDot.getPos(i).y]=true;
				        		this.currPixelCount++;
			        			//HORSE.mouseMove(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y);
			        			//HORSE.delay(1);
			        			//paint the canvas at the respectful position on 2d canvas to the shapeID number
			        			canvas[xyCanvas.x+aroundDot.getPos(i).x][xyCanvas.y+aroundDot.getPos(i).y]=shapeID;
			        	    	//System.out.println(" check:"+xyCanvas.x+","+xyCanvas.y+"@"+xyCor.x+","+xyCor.y);
			        			//recursively call this method with the new position on screen and on 2d canvas to be checked around
			     		        getMatchingPixelExtra(bounds,temp,new Point(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y),new Point(xyCanvas.x+aroundDot.getPos(i).x,xyCanvas.y+aroundDot.getPos(i).y),canvas,shapeID,colorLeft);
			        		}
			        		else{
				        		int red=colorLeft.getRed()-Math.abs(colorOItem.getRed()-temp.getRed());
				        		int green=colorLeft.getGreen()-Math.abs(colorOItem.getGreen()-temp.getGreen());
				        		int blue=colorLeft.getBlue()-Math.abs(colorOItem.getBlue()-temp.getBlue());
				        		if(red<=0||green<=0||blue<=0){
				        			red=0;
				        			green=0;
				        			blue=0;
				        		}
				        		colorLeft=new Color(red,green,blue);
				        		this.currPixelCount++;
			        			//HORSE.mouseMove(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y);
			        			//HORSE.delay(1);
			        			//paint the canvas at the respectful position on 2d canvas to the shapeID number
			        			canvas[xyCanvas.x+aroundDot.getPos(i).x][xyCanvas.y+aroundDot.getPos(i).y]=shapeID;
			        	    	//System.out.println(" check:"+xyCanvas.x+","+xyCanvas.y+"@"+xyCor.x+","+xyCor.y);
			        			//recursively call this method with the new position on screen and on 2d canvas to be checked around
			      		        getMatchingPixelExtra(bounds,colorOItem,new Point(xyCor.x+aroundDot.getPos(i).x,xyCor.y+aroundDot.getPos(i).y),new Point(xyCanvas.x+aroundDot.getPos(i).x,xyCanvas.y+aroundDot.getPos(i).y),canvas,shapeID,colorLeft);
			        		}
			        		
			        //	}
			        }
		    	}
	    	}
		}
	    return canvas;//return the 2d integer canvas
    	/*
    	 * changed 4 to -5 and now testing
    	 * this change
    	 */
    }
    public int containsColor(Color temp,LinkedList<Color> list){
    	if (!list.isEmpty()){
    		for(int i=0;i<list.size();i++){
    			if(PixelColor.isTargeTColorX(list.get(i), temp)){
    				return i;
    			}
    		}
    	}
    	return -1;
    }

    public LinkedList<Shape> getAllShape(Rectangle bounds,TargetShape inThisShape,boolean print){
        LinkedList<Shape> allShapes=new LinkedList<Shape>();//create a linked list to store shapes in
    	LinkedList<Color> foundColors=new LinkedList<Color>();//list for storeing all found colors
    	short paintColor=0;//number to paint the current color to canvas
        int k=0;//is the count of colors found and the paint number of last found color
    	short[][] canvas = new short[bounds.width][bounds.height];//create a 2d integer array for painting numbers on
        for(int i=0;i<bounds.width;i++)//loop 100 times
            for(int j=0;j<bounds.height;j++)//loop 100 times
                canvas[i][j]=-5;//initialize all values of canvas to -5 meaning not checked yet
    	for(int i=bounds.y;i<=(bounds.y+bounds.height-1)&&i<this.screenChecked[0].length;i++){//loop thru the y bounds
    		for(int j=bounds.x;(j<=bounds.x+bounds.width-1)&&i<this.screenChecked.length;j++){//loop thru the x bounds
    			if(canvas[j-bounds.x][i-bounds.y]==-5&&inThisShape.getValue(j-bounds.x,i-bounds.y)){//if the spot hasn't been checked yet
	    			Color temp=img.getColorAt(j,i);//get the color of the spot
	    			myBuckets.dropNBucket(temp);
	    			if(foundColors.isEmpty()){//if list is empty
	    				foundColors.add(temp);//add first color
	    				paintColor=(short)++k;//increment k to 1
	    			}
	    			else if(containsColor(temp,foundColors)>=0){//if we already have this color
	    				paintColor=(short)(containsColor(temp,foundColors)+1);//set paintColor to color we already have
	    			}
	    			else{//if we got a new color
	    				foundColors.add(temp);//add the color
	    				paintColor=(short)++k;//increment k
	    			}
	    			canvas[j-bounds.x][i-bounds.y]=paintColor;//paint current canvas spot to paintColor our paint number
	    			/*
	    			 * add all connected matching color pixels in bonds to the canvas
	    			 *check the canvas for same color pixel connected to
	    			 *point j,i and matching the color
	    			 *start marking at j,i on the canvas
	    			 */
	    			canvas=this.getMatchingPixelExtra(new Rectangle(bounds.x,bounds.y,bounds.width,bounds.height),temp,
	    					new Point(j,i),new Point(j-bounds.x,i-bounds.y),canvas,paintColor,new Color(200,200,200));	    			
	    		}
    		}
    	}
    	//System.out.println("finished finding colors: "+k);
//error checking
//    	if(k>3&&k<this.maxColorNShape&&print){
//	    	for(int i=0;i<bounds.height;i++){//loop 100 times
//	            for(int j=0;j<bounds.width;j++){//loop 100 times
//	            	if(canvas[j][i]==-5){
//	                    System.out.print("B");//initialize all values of canvas to -5 meaning not checked yet           		
//	            	}
//	            	else{
//	            		System.out.print(canvas[j][i]);//initialize all values of canvas to -5 meaning not checked yet
//	            	}
//	            }
//	            System.out.println();
//	    	}
//    	}
                
                
                //error checking}
    	for(int i=1;i<=k;i++){//loop thru all different colors found
    		allShapes.add(new TargetShape(canvas,bounds.x,bounds.y,i));//add the shape of k color to shape list
    	}
    	
    	return allShapes;//return list of shapes
    	/*
     	 * 
    	 * changed number 4 to -5 currently testing
    	 */
    }


//    public short[][] getInShape(Rectangle bounds,TargetShape inThisShape){
//    	short paintColor=1;//number to paint the current color to canvas
//    	short[][] canvas = new short[inThisShape.getSize().x][inThisShape.getSize().y];//create a 2d integer array for painting numbers on
//        for(int i=0;i<inThisShape.getSize().x;i++)//loop 100 times
//            for(int j=0;j<inThisShape.getSize().y;j++)//loop 100 times
//                canvas[i][j]=-5;//initialize all values of canvas to -5 meaning not checked yet
//    	for(int i=bounds.y;i<=bounds.y+bounds.height&&i<this.screenChecked[0].length;i++){//loop thru the y bounds
//    		for(int j=bounds.x;j<=bounds.x+bounds.width&&i<this.screenChecked.length;j++){//loop thru the x bounds
//    			if(canvas[j-bounds.x][i-bounds.y]==-5&&inThisShape.getValue(j-bounds.x,i-bounds.y)){//if the spot hasn't been checked yet
//	    			Color temp=img.getColorAt(j,i);//get the color of the spot
//	    			canvas[j-bounds.x][i-bounds.y]=paintColor;//paint current canvas spot to paintColor our paint number
//	    			/*
//	    			 * add all connected matching color pixels in bonds to the canvas
//	    			 *check the canvas for same color pixel connected to
//	    			 *point j,i and matching the color
//	    			 *start marking at j,i on the canvas
//	    			 */
//	    			canvas=this.getMatchingPixelExtra(new Rectangle(bounds.x,bounds.y,bounds.width,bounds.height),inThisShape.getColor(),
//	    					new Point(j,i),new Point(j-bounds.x,i-bounds.y),canvas,paintColor,new Color(200,200,200));	    			
//	    		}
//    		}
//    	}
//      
//    	return canvas;//add the shape of k color to shape list
//
//    }    
     /**searches screen spiraling out and returns the point where it
     *finds the item
     *@param bounds is the area to search inside of
     *@param startPnt the point to start searching from
     *@param itemName is the string that appears when the mouse is over the item
     *@param colorOItem the color of item we are looking for
     *@param colorOLetters color of the letters that appear when mouse over item
     *@param radiusInc the number that will be added to the radius as it spirals out(how thurough it searches)
     *@param radiusSensitive is the distance between x or y along the spiral that will be checked(how thurough it searches)   
     *@return the point where item was found or -1,-1 if not found
     * @throws JpegProcessingException 
     */
    public int findBackground(String filename){
    	this.firstPassCount=0;
    	this.removedCount=0;
        LinkedList<TargetShape> tempShapeList=new LinkedList<TargetShape>();
    	int[] removeShapeList=new int[100000];
    	int removeListCount=0;
    	int targetNimgCount=0;
    	int shapeNum=0;
    	this.loadNewImage(filename);
    	this.initScreenChecked();
        Color color1=new Color(0,0,0);//color that will be used to store pixel color to check
    	TargetShape temp=new TargetShape(shapeNum);
    	for(int r=pixelSpace;r<img.getImgWidth()-pixelSpace;r+=pixelSpace){
    		for(int j=pixelSpace;j<img.getImgHeight()-pixelSpace;j+=pixelSpace){
	    		if(!screenChecked[r][j]){
	    			
	    			color1=img.getColorAt(r,j);//get pixel color from point
	        		this.colorAverage=new Color(color1.getRed(),color1.getGreen(),color1.getBlue());
	        		colorCount=1;
	        		if(PixelColor.isTargeTColor2( color1)){//was isTargeTColor2(this.imgAvgColor, color1)
		        		temp=getShape(600,color1,r,j,temp);
		//    			HORSE.mouseMove(r,j);
		//    			HORSE.delay(1);
//		        		if(temp!=null&&temp.getSize().x<(this.firstLengthMax*img.getPixelsPerFoot())
//			    				&&temp.getSize().y<(this.firstLengthMax*img.getPixelsPerFoot())
//			    				&&temp.getSize().x>(this.firstLengthMin*img.getPixelsPerFoot())
//			    				&&temp.getSize().y>(this.firstLengthMin*img.getPixelsPerFoot())
//			    				&&(((double)temp.getPixelCount())/(((double)temp.getSize().x)*((double)temp.getSize().y)))>.33){
		        		if(temp!=null){//newly added for chromosomes
		        			temp=new TargetShape(temp);
		            		temp.setColor(this.colorAverage);
			        		temp.setTargetNiamgeID(shapeNum++);
			        		temp.setTitle(filename);
//			        		temp.setAGL(img.getAboveGroundLevelFeet());
//			        		temp.setMetaData(img.getMetaData());
//		            		TargetShape temp2=new TargetShape(temp);
////		        			TargetShape tempTrimmed=new Shape(tempPop);
////		        			tempTrimmed.trimShape(4);
////		        			short targetText[][]=this.getInsideShape(new Rectangle(tempTrimmed.getScreenCordinate().x,tempTrimmed.getScreenCordinate().y,tempTrimmed.getSize().x,tempTrimmed.getSize().y),tempTrimmed);
////		        			BufferedImage tempImg=getTextImage(targetText);
//
//		        			//shapeList.addShape(this.getInShape(new Rectangle(temp.getScreenCordinate().x,temp.getScreenCordinate().y,temp.getSize().x,temp.getSize().y),temp),true);
//	//        				temp2.trimShape(this.trimAmount);
//		            		myBuckets=new ColorBuckets();
//		            		LinkedList<Shape> thisShapeList=this.getAllShape(new Rectangle(temp2.getScreenCordinate().x,temp2.getScreenCordinate().y,temp2.getSize().x,temp2.getSize().y),temp2,false);
//	//        				TargetShape temp3=new Shape(temp);
//	//        				temp3.trimShape(4);
////		            		LinkedList<Shape> thisShapeList2=this.getAllShape(new Rectangle(temp3.getScreenCordinate().x,temp3.getScreenCordinate().y,temp3.getSize().x,temp3.getSize().y),temp3,true);
//		            		temp.setBuckets(myBuckets);
//		            		temp.setColorCount(thisShapeList.size());
//                            temp.setSizex(temp2.getSize().x/img.getPixelsPerFoot());
//                            temp.setSizey(temp2.getSize().y/img.getPixelsPerFoot());
//		            		System.out.println();
//		            		System.out.println("ColorNShape: "+thisShapeList.size());
//		            		System.out.println(temp2.getSize().x+","+temp2.getSize().y);		            		
//		            		System.out.println("Loc: "+temp2.getScreenCordinate().x+","+temp2.getScreenCordinate().y);
//		            		System.out.println("size: "+(temp2.getSize().x/img.getPixelsPerFoot())+","+(temp2.getSize().y/img.getPixelsPerFoot()));
//		            		System.out.println("pixelC: "+temp2.getPixelCount());
//		            		//this.writeTargetImage(temp2, targetNimgCount++);
//		            		if(thisShapeList.size()<this.firstColorCountMax
//	        				&&thisShapeList.size()>this.firstColorCountMin){
//		        				//TargetShape temp3=new Shape(temp);
//		        				//temp3.trimShape(8);
//			            		//LinkedList<Shape> thisShapeList2=this.getAllShape(new Rectangle(temp3.getScreenCordinate().x,temp3.getScreenCordinate().y,temp3.getSize().x,temp3.getSize().y),temp3,true);
//		        				//temp3.shapeOut();
//		        				if(tempShapeList.isEmpty()){
//		        					tempShapeList.add(temp);
//		        				}
//		        				else{
//		        					for(int i=0;i<tempShapeList.size();i++){
//		        						if(tempShapeList.get(i).getScreenCordinate().distance(temp.getScreenCordinate())<(this.targetExcludeDistance*img.getPixelsPerFoot())){//I made wrong changes here about heading and distance should be right now andrew
//		        							removeShapeList[removeListCount++]=tempShapeList.get(i).getTargetNimageID();
//		        							removeShapeList[removeListCount++]=temp.getTargetNimageID();
//		        						}
//		        						
//		        					}
		        					tempShapeList.add(temp);
//		        				}
//		            		}
//		        			
//		        		}
		        		}
	        		}
	        	}
	    	}
        }
		while(!tempShapeList.isEmpty()){
			TargetShape tempPop=tempShapeList.pop();
			int id=tempPop.getTargetNimageID();
			boolean add = true;
//			for(int i=0;i<removeListCount;i++){
//				if(id==removeShapeList[i]){
//					add=false;
//				}
//			}
//			if(!add){
//				this.removedCount++;
//			}
//    		if(tempPop.getColorCount()>this.minColorNShape
//    				&&tempPop.getColorCount()<this.maxColorNShape){
//
//				if(add//&&tempPop.getPixelCount()<this.maxPixelCount&&tempPop.getPixelCount()>this.minPixelCount
//	    				&&tempPop.getSize().x<(this.maxLength*img.getPixelsPerFoot())
//	    				&&tempPop.getSize().y<(this.maxLength*img.getPixelsPerFoot())
//	    				&&		((tempPop.getSize().y>(this.minWidth*img.getPixelsPerFoot())
//	    									&&(tempPop.getSize().x>(this.minHeight*img.getPixelsPerFoot())))
//	    						||((tempPop.getSize().x>(this.minWidth*img.getPixelsPerFoot())
//	    									&&(tempPop.getSize().y>(this.minHeight*img.getPixelsPerFoot())))))
//	    				){
					
//	    				&&tempPop.getScreenCordinate().x>(this.awayFromEdge*img.getPixelsPerFoot())
//	    				&&tempPop.getScreenCordinate().y>(this.awayFromEdge*img.getPixelsPerFoot())
//	    				&&tempPop.getScreenCordinate().x<(img.getImgWidth()-(this.awayFromEdge*img.getPixelsPerFoot()))
//	    				&&tempPop.getScreenCordinate().y<(img.getImgHeight()-(this.awayFromEdge*img.getPixelsPerFoot()))
					
    				//System.out.print("AGL= "+this.aboveGroundLevel+","+"PixelCount= "+tempPop.getPixelCount());
					//tempPop.shapeOut();
					System.out.println();
            		System.out.println("Loc: "+tempPop.getScreenCordinate().x+","+tempPop.getScreenCordinate().y);
            		System.out.println("size: "+(tempPop.getSize().x/img.getPixelsPerFoot())+","+(tempPop.getSize().y/img.getPixelsPerFoot()));
            		System.out.println("pixelC: "+tempPop.getPixelCount());
					System.out.println(tempPop.getTitle());
    				//System.out.println((tempPop.getPixelCount()-img.getPixelCountAGL())+","+tempPop.getPixelCount()+","+img.getPixelCountAGL()+","+tempPop.getSize().x+","+tempPop.getSize().y);
    				//tempPop.setTargetImg(img);
//    				LatLongPoint tempCor=new LatLongPoint(0,0);
//    				tempCor=img.calcPointLatLongs(tempPop.getSCcenter());
//    				tempPop.setTargetLatLong(tempCor.getLat(),tempCor.getLong());
//    				tempPop.setImgLatLong(img.getImgLat(), img.getImgLong());
//    				TextImage textImg=new TextImage(img,this.textImageRotationCount);
    				//TextImage tryText=new TextImage(img,this.textImageRotationCount);
//    				tempPop.setImgHeading(img.getHeading());
//    				textImg.writeTextImage(tempPop);
    				//tryText.tryWriteTargetTextImage(tempPop);
//    				tempPop.setText(textImg.getText());
    				//tempPop.trySetText(tryText.getText());
    				shapeList.addShape(tempPop);
    				writeTargetImage(tempPop);
    				targetNimgCount++;
//    				this.googleEarthIt();
//    				shapeList.writeTurnInDoc("",this.imageFolderPath);
//    		    	shapeList.writeTurnInDocFalsePos("",this.imageFolderPath);

//				}
//    		}
		}
		System.out.println("FirstPass: "+this.firstPassCount+"   Removed: "+this.removedCount);
		return findChromosomes(filename,shapeNum,targetNimgCount);

    }
    public int findChromosomes(String filename,int shapeNum,int targetNimgCount){
    	this.firstPassCount=0;
    	this.removedCount=0;
        LinkedList<TargetShape> tempShapeList=new LinkedList<TargetShape>();
    	int[] removeShapeList=new int[100000];
    	int removeListCount=0;
    	//int targetNimgCount=0;
    	//int shapeNum=0;
    	//this.loadNewImage(filename);
    	//this.initScreenChecked();
        Color color1=new Color(0,0,0);//color that will be used to store pixel color to check
    	TargetShape temp=new TargetShape(shapeNum);
    	for(int r=pixelSpace+1;r<img.getImgWidth()-pixelSpace;r+=pixelSpace){//made plus one change chromosomes
    		for(int j=pixelSpace;j<img.getImgHeight()-pixelSpace;j+=pixelSpace){
	    		if(!screenChecked[r][j]){
	    			
	    			color1=img.getColorAt(r,j);//get pixel color from point
	        		this.colorAverage=new Color(color1.getRed(),color1.getGreen(),color1.getBlue());
	        		colorCount=1;
		        		temp=getShapeLeft(200,color1,r,j,temp);
		//    			HORSE.mouseMove(r,j);
		//    			HORSE.delay(1);
//		        		if(temp!=null&&temp.getSize().x<(this.firstLengthMax*img.getPixelsPerFoot())
//			    				&&temp.getSize().y<(this.firstLengthMax*img.getPixelsPerFoot())
//			    				&&temp.getSize().x>(this.firstLengthMin*img.getPixelsPerFoot())
//			    				&&temp.getSize().y>(this.firstLengthMin*img.getPixelsPerFoot())
//			    				&&(((double)temp.getPixelCount())/(((double)temp.getSize().x)*((double)temp.getSize().y)))>.33){
		        		if(temp!=null){	
		        			temp=new TargetShape(temp);
		            		temp.setColor(this.colorAverage);
			        		temp.setTargetNiamgeID(shapeNum++);
			        		temp.setTitle(filename);
//			        		temp.setAGL(img.getAboveGroundLevelFeet());
//			        		temp.setMetaData(img.getMetaData());
//		            		TargetShape temp2=new TargetShape(temp);
////		        			TargetShape tempTrimmed=new Shape(tempPop);
////		        			tempTrimmed.trimShape(4);
////		        			short targetText[][]=this.getInsideShape(new Rectangle(tempTrimmed.getScreenCordinate().x,tempTrimmed.getScreenCordinate().y,tempTrimmed.getSize().x,tempTrimmed.getSize().y),tempTrimmed);
////		        			BufferedImage tempImg=getTextImage(targetText);
//
//		        			//shapeList.addShape(this.getInShape(new Rectangle(temp.getScreenCordinate().x,temp.getScreenCordinate().y,temp.getSize().x,temp.getSize().y),temp),true);
//	//        				temp2.trimShape(this.trimAmount);
//		            		myBuckets=new ColorBuckets();
//		            		LinkedList<Shape> thisShapeList=this.getAllShape(new Rectangle(temp2.getScreenCordinate().x,temp2.getScreenCordinate().y,temp2.getSize().x,temp2.getSize().y),temp2,false);
//	//        				TargetShape temp3=new Shape(temp);
//	//        				temp3.trimShape(4);
////		            		LinkedList<Shape> thisShapeList2=this.getAllShape(new Rectangle(temp3.getScreenCordinate().x,temp3.getScreenCordinate().y,temp3.getSize().x,temp3.getSize().y),temp3,true);
//		            		temp.setBuckets(myBuckets);
//		            		temp.setColorCount(thisShapeList.size());
//                            temp.setSizex(temp2.getSize().x/img.getPixelsPerFoot());
//                            temp.setSizey(temp2.getSize().y/img.getPixelsPerFoot());
//		            		System.out.println();
//		            		System.out.println("ColorNShape: "+thisShapeList.size());
//		            		System.out.println(temp2.getSize().x+","+temp2.getSize().y);		            		
//		            		System.out.println("Loc: "+temp2.getScreenCordinate().x+","+temp2.getScreenCordinate().y);
//		            		System.out.println("size: "+(temp2.getSize().x/img.getPixelsPerFoot())+","+(temp2.getSize().y/img.getPixelsPerFoot()));
//		            		System.out.println("pixelC: "+temp2.getPixelCount());
//		            		//this.writeTargetImage(temp2, targetNimgCount++);
//		            		if(thisShapeList.size()<this.firstColorCountMax
//	        				&&thisShapeList.size()>this.firstColorCountMin){
//		        				//TargetShape temp3=new Shape(temp);
//		        				//temp3.trimShape(8);
//			            		//LinkedList<Shape> thisShapeList2=this.getAllShape(new Rectangle(temp3.getScreenCordinate().x,temp3.getScreenCordinate().y,temp3.getSize().x,temp3.getSize().y),temp3,true);
//		        				//temp3.shapeOut();
//		        				if(tempShapeList.isEmpty()){
//		        					tempShapeList.add(temp);
//		        				}
//		        				else{
//		        					for(int i=0;i<tempShapeList.size();i++){
//		        						if(tempShapeList.get(i).getScreenCordinate().distance(temp.getScreenCordinate())<(this.targetExcludeDistance*img.getPixelsPerFoot())){//I made wrong changes here about heading and distance should be right now andrew
//		        							removeShapeList[removeListCount++]=tempShapeList.get(i).getTargetNimageID();
//		        							removeShapeList[removeListCount++]=temp.getTargetNimageID();
//		        						}
//		        						
//		        					}
		        					tempShapeList.add(temp);
//		        				}
//		            		}
//		        			
//		        		}
		        		}
	        	}
	    	}
        }
		while(!tempShapeList.isEmpty()){
			TargetShape tempPop=tempShapeList.pop();
			int id=tempPop.getTargetNimageID();
			boolean add = true;
//			for(int i=0;i<removeListCount;i++){
//				if(id==removeShapeList[i]){
//					add=false;
//				}
//			}
//			if(!add){
//				this.removedCount++;
//			}
//    		if(tempPop.getColorCount()>this.minColorNShape
//    				&&tempPop.getColorCount()<this.maxColorNShape){
//
//				if(add//&&tempPop.getPixelCount()<this.maxPixelCount&&tempPop.getPixelCount()>this.minPixelCount
//	    				&&tempPop.getSize().x<(this.maxLength*img.getPixelsPerFoot())
//	    				&&tempPop.getSize().y<(this.maxLength*img.getPixelsPerFoot())
//	    				&&		((tempPop.getSize().y>(this.minWidth*img.getPixelsPerFoot())
//	    									&&(tempPop.getSize().x>(this.minHeight*img.getPixelsPerFoot())))
//	    						||((tempPop.getSize().x>(this.minWidth*img.getPixelsPerFoot())
//	    									&&(tempPop.getSize().y>(this.minHeight*img.getPixelsPerFoot())))))
//	    				){
					
//	    				&&tempPop.getScreenCordinate().x>(this.awayFromEdge*img.getPixelsPerFoot())
//	    				&&tempPop.getScreenCordinate().y>(this.awayFromEdge*img.getPixelsPerFoot())
//	    				&&tempPop.getScreenCordinate().x<(img.getImgWidth()-(this.awayFromEdge*img.getPixelsPerFoot()))
//	    				&&tempPop.getScreenCordinate().y<(img.getImgHeight()-(this.awayFromEdge*img.getPixelsPerFoot()))
					
    				//System.out.print("AGL= "+this.aboveGroundLevel+","+"PixelCount= "+tempPop.getPixelCount());
					//tempPop.shapeOut();
					System.out.println();
            		System.out.println("Loc: "+tempPop.getScreenCordinate().x+","+tempPop.getScreenCordinate().y);
            		System.out.println("size: "+(tempPop.getSize().x/img.getPixelsPerFoot())+","+(tempPop.getSize().y/img.getPixelsPerFoot()));
            		System.out.println("pixelC: "+tempPop.getPixelCount());
					System.out.println(tempPop.getTitle());
    				//System.out.println((tempPop.getPixelCount()-img.getPixelCountAGL())+","+tempPop.getPixelCount()+","+img.getPixelCountAGL()+","+tempPop.getSize().x+","+tempPop.getSize().y);
    				//tempPop.setTargetImg(img);
//    				LatLongPoint tempCor=new LatLongPoint(0,0);
//    				tempCor=img.calcPointLatLongs(tempPop.getSCcenter());
//    				tempPop.setTargetLatLong(tempCor.getLat(),tempCor.getLong());
//    				tempPop.setImgLatLong(img.getImgLat(), img.getImgLong());
//    				TextImage textImg=new TextImage(img,this.textImageRotationCount);
    				//TextImage tryText=new TextImage(img,this.textImageRotationCount);
//    				tempPop.setImgHeading(img.getHeading());
//    				textImg.writeTextImage(tempPop);
    				//tryText.tryWriteTargetTextImage(tempPop);
//    				tempPop.setText(textImg.getText());
    				//tempPop.trySetText(tryText.getText());
    				shapeList.addShape(tempPop);
     				tempPop.getSkeleton();
    				writeTargetImage(tempPop);
       				targetNimgCount++;
//    				this.googleEarthIt();
//    				shapeList.writeTurnInDoc("",this.imageFolderPath);
//    		    	shapeList.writeTurnInDocFalsePos("",this.imageFolderPath);

//				}
//    		}
		}
		System.out.println("FirstPass: "+this.firstPassCount+"   Removed: "+this.removedCount);
		return targetNimgCount;

    }

    public void writeTargetImage(TargetShape tempShape){
		try {
		    // retrieve image
//			TargetShape tempTrimmed=new Shape(tempPop);
//			tempTrimmed.trimShape(4);
//			short targetText[][]=this.getInsideShape(new Rectangle(tempTrimmed.getScreenCordinate().x,tempTrimmed.getScreenCordinate().y,tempTrimmed.getSize().x,tempTrimmed.getSize().y),tempTrimmed);
//			BufferedImage tempImg=getTextImage(targetText);
//			tempImg=this.makeRotatedImage(tempImg);
			//BufferedImage tempImg2=new BufferedImage( 
			BufferedImage tempImg=img.getSubImage(tempShape,true);//,targetImgBorderSize);//30pixel border
			File curDir=new File(".");
			File outputfile = new File(curDir.getCanonicalPath()+"/shapeData/"+tempShape.getTitle().substring(tempShape.getTitle().indexOf("ima"),tempShape.getTitle().indexOf(".jpg"))+"_"+(tempShape.getTargetNimageID())+".jpg");//,tempShape.getTitle().indexOf(".jpg"))+"_"+(inImageTargetCount)+".png"
		    ImageIO.write(tempImg, "jpg", outputfile);
		} catch (IOException e) {
		    System.out.println(e);
		}

    }

    /**
     * returns a shape of all connected pixels matching in color that
     * fit in double the searchWidth
     * @param searchWidth half of the width of the squared search area
     * @param colorOItem the color to match
     * @param xCor the x cordinate of the starting pixel
     * @param yCor the y cordinate of the starting pixel
     * @param shpN place found shape is stored
     * @return returns the shape if found and null if no shape found
     */
    public TargetShape getShape(int searchWidth,Color colorOItem,int xCor,int yCor,TargetShape shpN){
    	if(xCor<0||yCor<0){//if the point has cordinates less than zero or off screen in neg direction
        	return null;//return null for no shape found
        }
        int sizeSquared=2*searchWidth;//double width for search area
        short[][] canvas = new short[sizeSquared][sizeSquared];//create a 2d integer canvas to mark matching connected pixels on
        Rectangle canvasBounds=new Rectangle(0,0,sizeSquared,sizeSquared);
        for(int i=0;i<sizeSquared;i++)//loop y area
            for(int j=0;j<sizeSquared;j++)//loop x area
                canvas[i][j]=-5;//initialize all spots on the 2d canvas to not checked
        Point canvasStart=new Point((int)(sizeSquared/2.0),(int)(sizeSquared/2.0));
        canvas[canvasStart.x][canvasStart.y]=0;//mark the center of the canvas as a found pixel
        /*
         *find all connected matching pixels starting
         *at the center of canvas and the point xCor,yCor
         *marking the matching pixels as the number 1
         *ont the canvas
       	 */
        this.currPixelCount=0;
        this.onImgEdge=false;
        canvas=getMatchingPixel(canvasBounds,colorOItem,new Point(xCor,yCor),canvasStart,canvas,(short)0,new Color(200,200,200));
        if(/*!this.onImgEdge&&*/this.currPixelCount>this.firstPixelMin){//this.currPixelCount>this.firstPixelMin&&this.currPixelCount<this.firstPixelMax
        	shpN=new TargetShape(new Point(sizeSquared,sizeSquared));
        	shpN.setShape(canvas, xCor, yCor,0);//store the shape marked by the number 1 in shape
        	this.firstPassCount++;
        	//shpN.shapeOut();
        	return shpN;
        }
       return null;//return the shape found
       /*
        * the number 4 has been changed -5 
        * currently testing -5
        * check in Shape.class if work on this issue
        */
    }
    /**
     * returns a shape of all connected pixels matching in color that
     * fit in double the searchWidth
     * @param searchWidth half of the width of the squared search area
     * @param colorOItem the color to match
     * @param xCor the x cordinate of the starting pixel
     * @param yCor the y cordinate of the starting pixel
     * @param shpN place found shape is stored
     * @return returns the shape if found and null if no shape found
     */
    public TargetShape getShapeLeft(int searchWidth,Color colorOItem,int xCor,int yCor,TargetShape shpN){
    	if(xCor<0||yCor<0){//if the point has cordinates less than zero or off screen in neg direction
        	return null;//return null for no shape found
        }
        int sizeSquared=2*searchWidth;//double width for search area
        short[][] canvas = new short[sizeSquared][sizeSquared];//create a 2d integer canvas to mark matching connected pixels on
        Rectangle canvasBounds=new Rectangle(0,0,sizeSquared,sizeSquared);
        for(int i=0;i<sizeSquared;i++)//loop y area
            for(int j=0;j<sizeSquared;j++)//loop x area
                canvas[i][j]=-5;//initialize all spots on the 2d canvas to not checked
        Point canvasStart=new Point((int)(sizeSquared/2.0),(int)(sizeSquared/2.0));
        canvas[canvasStart.x][canvasStart.y]=0;//mark the center of the canvas as a found pixel
        /*
         *find all connected matching pixels starting
         *at the center of canvas and the point xCor,yCor
         *marking the matching pixels as the number 1
         *ont the canvas
       	 */
        this.currPixelCount=0;
        this.onImgEdge=false;
        canvas=getMatchingPixelLeft(canvasBounds,colorOItem,new Point(xCor,yCor),canvasStart,canvas,(short)0,new Color(200,200,200));
        if(/*!this.onImgEdge&&*/this.currPixelCount>this.firstPixelMin){//this.currPixelCount>this.firstPixelMin&&this.currPixelCount<this.firstPixelMax
        	shpN=new TargetShape(new Point(sizeSquared,sizeSquared));
        	shpN.setShape(canvas, xCor, yCor,0);//store the shape marked by the number 1 in shape
        	this.firstPassCount++;
        	return shpN;
        }
       return null;//return the shape found
       /*
        * the number 4 has been changed -5 
        * currently testing -5
        * check in Shape.class if work on this issue
        */
    }

    public void googleEarthIt(){
    	
    	//use the current time to mark files as unique file name
    	//Date today=new Date();
    	System.out.println("Minutes:Secs "+(((System.currentTimeMillis()-start)/60000)/60)+":"+((System.currentTimeMillis()-start)/1000));
    	//save identified shapes to rockShapeList
    	//shapeList.writeArrayRock("shapeData/rockShapeList.shp");
        //save new found unidentified shapes in woodShapeList
    	//shapeList.writeShapesByLoc("shapeData/woodShapeList"+(today.getTime()%1000));
    	shapeList.writeArrayAGLDataByLoc("shapeData/targetData",this.imageFolderPath);
    	shapeList.writeNonTargetArrayAGLDataByLoc("shapeData/targetFalsePos",this.imageFolderPath);
        //save new found unidentified site shapes to woodSiteList
    	//img.flush();
    	try{
    		//File dir1 = new File(".");
    		//Runtime.getRuntime().exec(this.googleEarthPath+" "+dir1.getCanonicalPath()+"/shapeData/targetData.kml", null);

    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
    }

    public void exit(){
    	
    	//use the current time to mark files as unique file name
    	//Date today=new Date();
    	System.out.println("Minutes:Secs "+(((System.currentTimeMillis()-start)/60000)/60)+":"+((System.currentTimeMillis()-start)/1000));
    	//save identified shapes to rockShapeList
    	//shapeList.writeArrayRock("shapeData/rockShapeList.shp");
        //save new found unidentified shapes in woodShapeList
    	//shapeList.writeShapesByLoc("shapeData/woodShapeList"+(today.getTime()%1000));
    	shapeList.writeArrayAGLDataByLoc("shapeData/targetData",this.imageFolderPath);
    	shapeList.writeNonTargetArrayAGLDataByLoc("shapeData/targetFalsePos",this.imageFolderPath);
    	shapeList.writeTurnInDoc("",this.imageFolderPath);
    	shapeList.writeTurnInDocFalsePos("",this.imageFolderPath);
        //save new found unidentified site shapes to woodSiteList
    	//img.flush();
    	
    	try{
        	File dir1 = new File(".");
        	System.out.println("Working directory is: "+dir1.getAbsolutePath());
       		//File dir1 = new File(".");
    		//Runtime.getRuntime().exec(this.googleEarthPath+" "+dir1.getCanonicalPath()+"\\shapeData\\targetData.kml", null);
    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
    }
    public Color averageColor(Color temp){
    	int red=((this.colorAverage.getRed()*this.colorCount)+temp.getRed())/(this.colorCount+1);
    	int green=((this.colorAverage.getGreen()*this.colorCount)+temp.getGreen())/(this.colorCount+1);
    	int blue=((this.colorAverage.getBlue()*this.colorCount)+temp.getBlue())/(this.colorCount+1);
    	this.colorCount++;
    	return new Color(red,green,blue);
    }

    public boolean waitTillFileIsReady(String filename){
	    File fileToCopy = new File(filename);
	    boolean fileReady=false;
	    int sleepTime = 500; // Sleep 1 second
	    while(!fileReady){
	        // Cannot write to file, windows still working on it
	        //Sleep(sleepTime);
	    	try{
	    		
	    		Thread.sleep(sleepTime);
	    		FileReader test = new FileReader(fileToCopy);
	    		test.close();
	    		fileReady=true;
	    	}
	    	catch(Exception e){
	    		System.out.println(e);
	    		waitTillFileIsReady(filename);
	    	}
//	        sleepTime *= 2; // Multiply sleep time by 2 (not really exponential but will do the trick)
//	        if(sleepTime > 30000){ 
//	            // Set a maximum sleep time to ensure we are not sleeping forever :)
//	            sleepTime = 30000;
//	        }
	    }
	    return true;
    }
    public String getNewFiles(String path){
    	// Directory path here
    	  //LinkedList<String> imageFiles=new LinkedList<String>();
    	  //String path = "."; 
    	boolean foundFile=false;
    	  File folder = new File(path);
    	  //System.out.println(path);
    	  File[] listOfFiles = folder.listFiles(); 
    	 
    	  for (int i = 0; i < listOfFiles.length; i++) 
    	  {
    	 
    	   if (listOfFiles[i].isFile()) 
    	   {
    		   String file = listOfFiles[i].getName();
    	       if (file.endsWith(".jpg") || file.endsWith(".JPG"))
    	       {
    	    	   if(imageQue.size()==0){
   	    			   imageQue.add(file);
	    			   this.imageQue.add(file);
	    			   return path+"/"+file;
    	    	   }
    	    	   else{
    	    		   foundFile=false;
	    	    	   for(int j=0;j<imageQue.size();j++){
	    	    		   if(imageQue.get(j).equals(file)){
	    	    			   //imageQue.add(file);
	    	    			   foundFile=true;
	    	    		   }
	    	    	   }
	    	    	   if(!foundFile){
		    			   this.imageQue.add(file);
		    			   this.waitTillFileIsReady(path+"/"+file);
		    			   return path+"/"+file;
	    	    	   }
    	    	   }
    	        }
    	     }
    	  }
    	    return null;
    	}    

    public String getNewFilesFromLog(String path) {
        String log = path+"/imagelog.txt";
        LinkedList<String> imageList = new LinkedList<String>();
        try {
        FileReader fileReader = new FileReader(log);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            imageList.add(line);
        }
        bufferedReader.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }

        ListIterator imageIter = imageList.listIterator();

        String next;
        while (imageIter.hasNext()) {
            next = (String) imageIter.next();
            if (imageQue.contains(next)) {
                continue;
            }
            else {
                imageQue.add(next);
                return next;
            }
        }
        return null;
    }


}
