package extraction;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import color.PixelColor;

import chromosome.ChromosomeCluster;
import chromosome.GeneticSlideImage;
import basic_objects.AroundPixel;
import basic_objects.PointList;
import basic_objects.SearchArea;

/**
 * @author andrew
 * 
 */
public class Extractor {
	private LinkedList<ChromosomeCluster> clusterList;
	private LinkedList<ChromosomeCluster> splitList;
	/*
	 * aroundot is an array of 8 points that is a x,y difference from the center point pixel 107
	 * |-1,-1| 0,-1| 1,-1| 2.6 or |-1, 0| dot | 1, 0| <--- this is a visual of around dot 345 |-1,
	 * 1| 0, 1| 1, 1|
	 */
	private int currPixelCount;
	private int pixelSpace;
	private int firstPixelMax;
	private int firstPixelMin;
	private int firstPassCount;

	public Extractor() {
		this.firstPixelMax = 11000;
		this.firstPixelMin = 30;
		this.pixelSpace = 3;
		clusterList = new LinkedList<ChromosomeCluster>();
		splitList = new LinkedList<ChromosomeCluster>();
		currPixelCount = 0;

	}

	/**
	 * this is a getter for the list of clusters extracted
	 * 
	 * @return list of clusters extracted from image
	 */
	public LinkedList<ChromosomeCluster> getClusterList() {
		return clusterList;
	}
	/**
	 * this is a getter for the list of clusters split out
	 * 
	 * @return list of clusters extracted from image
	 */
	public LinkedList<ChromosomeCluster> getSplitList() {
		return splitList;
	}
	public Rectangle getBounds(GeneticSlideImage img,Rectangle canvasBounds,SearchArea searchArea,Point imgPoint,Point searchPoint,Point canvasPoint){
		Rectangle combinedBounds=new Rectangle();
		Point[] max=new Point[3];
		Point[] min=new Point[3];
		max[0]=new Point(canvasBounds.width-canvasPoint.x+imgPoint.x,canvasBounds.height-canvasPoint.y+imgPoint.y);
		min[0]=new Point(canvasBounds.x-canvasPoint.x+imgPoint.x,canvasBounds.y-canvasPoint.y+imgPoint.y);
		max[1]=new Point(img.getImgWidth(),img.getImgHeight());
		min[1]=new Point(0,0);
		max[2]=new Point(searchArea.getWidth()-searchPoint.x+imgPoint.x,searchArea.getHeight()-searchPoint.y+imgPoint.y);
		min[2]=new Point(0-searchPoint.x+imgPoint.x,0-searchPoint.y+imgPoint.y);
		combinedBounds=new Rectangle(min[0].x,min[0].y,max[0].x,max[0].y);
		for(int i=1;i<3;i++){
			if(min[i].x>combinedBounds.x){
				combinedBounds.setLocation(min[i].x, combinedBounds.y);
			}
			if(min[i].y>combinedBounds.y){
				combinedBounds.setLocation(combinedBounds.x,min[i].y);
			}
			if(max[i].x<combinedBounds.width){
				combinedBounds.setSize(max[i].x, combinedBounds.height);
			}
			if(max[i].y<combinedBounds.height){
				combinedBounds.setSize( combinedBounds.width,max[i].y);
			}

		}
		
		
		return new Rectangle(combinedBounds.x,combinedBounds.y,
				combinedBounds.width-combinedBounds.x,combinedBounds.height-combinedBounds.y);
	}
	/**
	 * gets all connected matching colored pixels that are the background color and returns cluster
	 * on the 2d integer array -canvas- painted by the number -clusterID-
	 * 
	 * @param bounds
	 *            is a rectangle that only pixels inside of will be checked
	 * @param currentCoord
	 *            is the starting pixel
	 * @param canvasOrigin
	 *            is the starting position on the 2d integer canvas
	 * @param canvas
	 *            is the 2d integer canvas that stores the cluster(IMPORTANT: expected to have all
	 *            positions not checked initialized to the value -5)
	 * @param clusterID
	 *            is the number entered on canvas that represents matching connected pixels
	 * @return the 2d integer canvas that represents the cluster
	 */
	public short[][] getMatchingPixel(SearchArea searchArea,GeneticSlideImage img, Rectangle bounds,
										Point currentCoord,Point searchAreaStart,Point canvasOrigin,
										short[][] canvas, short clusterID,boolean aboveThreshold,int threshold) {
		LinkedList<Point> foundList = new LinkedList<Point>();
		Point canvasOffset = new Point(canvasOrigin.x - currentCoord.x, canvasOrigin.y
				- currentCoord.y);
		Point searchOffset=new Point(searchAreaStart.x - currentCoord.x, searchAreaStart.y
				- currentCoord.y);
		searchArea.initNextPixel();
		foundList.add(currentCoord);
		while (!foundList.isEmpty()) {
			currentCoord = foundList.pop();
			// loop 8 times once for every position around the center pixel
			for (int i = 0; i < 8; i++) {
				Point imgPoint=new Point(currentCoord.x + AroundPixel.getPos(i).x,currentCoord.y + AroundPixel.getPos(i).y);
				Point searchPoint=new Point(currentCoord.x + searchOffset.x+ AroundPixel.getPos(i).x,
						currentCoord.y +searchOffset.y + AroundPixel.getPos(i).y);
				Point canvasPoint=new Point(currentCoord.x + canvasOffset.x+ AroundPixel.getPos(i).x,currentCoord.y
						+ canvasOffset.y + AroundPixel.getPos(i).y);
				/*
				 * if the spot to be checked is inside of the bounds and if the pixel is inside the
				 * visible resolution of the screen
				 */
				if (bounds.contains(imgPoint)) {
					if (!searchArea.isPixelChecked(searchPoint)) {
						// if the spot to be checked value is -5 meaning it
						// hasn't been checked yet
						if (canvas[canvasPoint.x][canvasPoint.y] == -5) {
							// if the pixel at the position aroundDot matches
							// the color
							Color temp = img.getColorAt(imgPoint.x,imgPoint.y);
							// was isTargeTColor2(this.imgAvgColor,temp)
							if (!aboveThreshold&&PixelColor.isBelowThreshold(temp, threshold)) {
								searchArea.markPixelChecked(searchPoint);
								this.currPixelCount++;
								// paint the canvas at the respectful position
								// on 2d canvas to the clusterID number
								//TODO(aamcknig):this fails to be true when using small bounds and clusters for dark bands 
								canvas[canvasPoint.x][canvasPoint.y] = clusterID;
								if (!searchArea.isPixelFound(searchPoint)) {
									foundList.add(imgPoint);
									searchArea.addPixelFound(searchPoint);
								}
							}
							if (aboveThreshold&&PixelColor.isAboveThreshold(temp, threshold)) {
								searchArea.markPixelChecked(searchPoint);
								this.currPixelCount++;
								// paint the canvas at the respectful position
								// on 2d canvas to the clusterID number
								canvas[currentCoord.x + canvasOffset.x
										+ AroundPixel.getPos(i).x][currentCoord.y
										+ canvasOffset.y + AroundPixel.getPos(i).y] = clusterID;
								if (!searchArea.isPixelFound(searchPoint)) {
									foundList.add(imgPoint);
									searchArea.addPixelFound(searchPoint);
								}
							}
						}
					}
				}
			}
		}
		return canvas;
	}

	/**
	 * gets all connected pixels left after background removed starting at currentpoint and returns
	 * cluster on the 2d integer array -canvas- painted by the number -clusterID-
	 * 
	 * @param bounds
	 *            is a rectangle that only pixels inside of will be checked
	 * @param currentPoint
	 *            is the starting pixel
	 * @param canvasOrigin
	 *            is the starting position on the 2d integer canvas
	 * @param canvas
	 *            is the 2d integer canvas that stores the cluster(IMPORTANT: expected to have all
	 *            positions not checked initialized to the value -5)
	 * @param clusterID
	 *            is the number entered on canvas that represents matching connected pixels
	 * @return the 2d integer canvas that represents the cluster
	 */
	public short[][] getMatchingPixelLeft(SearchArea searchArea, Rectangle bounds,
			Point currentPoint, Point canvasOrigin, short[][] canvas, short clusterID) {
		LinkedList<Point> foundList = new LinkedList<Point>();
		Point canvasOffset = new Point(canvasOrigin.x - currentPoint.x, canvasOrigin.y
				- currentPoint.y);
		searchArea.initNextPixel();
		foundList.add(new Point(currentPoint));
		while (!foundList.isEmpty()) {
			currentPoint = foundList.pop();

			// loop 8 times once for every position around the center pixel
			for (int i = 0; i < 8; i++) {
				/*
				 * if the spot to be checked is inside of the bounds and if the pixel is inside the
				 * visible resolution of the screen
				 */
				if (currentPoint.x + AroundPixel.getPos(i).x < searchArea.getWidth()
						&& currentPoint.y + AroundPixel.getPos(i).y < searchArea.getHeight()
						&& currentPoint.x + AroundPixel.getPos(i).x >= 0
						&& currentPoint.y + AroundPixel.getPos(i).y>= 0) {
					if (!searchArea.isPixelChecked(new Point(currentPoint.x + AroundPixel.getPos(i).x,
							currentPoint.y + AroundPixel.getPos(i).y))) {
						// if the spot to be checked value is -5 meaning it
						// hasn't been checked yet
						if (!bounds.contains(new Point(currentPoint.x + canvasOffset.x
								+ AroundPixel.getPos(i).x, currentPoint.y + canvasOffset.y
								+ AroundPixel.getPos(i).y))
								|| canvas[currentPoint.x + canvasOffset.x + AroundPixel.getPos(i).x][currentPoint.y
										+ canvasOffset.y + AroundPixel.getPos(i).y] == -5) {
							// if the pixel at the position aroundDot
							// matches the color
							searchArea.markPixelChecked(new Point(
									currentPoint.x + AroundPixel.getPos(i).x, currentPoint.y
											+ AroundPixel.getPos(i).y));
							this.currPixelCount++;
							// paint the canvas at the respectful position
							// on 2d canvas to the clusterID number
							if (bounds.contains(new Point(currentPoint.x + canvasOffset.x
									+ AroundPixel.getPos(i).x, currentPoint.y + canvasOffset.y
									+ AroundPixel.getPos(i).y))) {
								canvas[currentPoint.x + canvasOffset.x + AroundPixel.getPos(i).x][currentPoint.y
										+ canvasOffset.y + AroundPixel.getPos(i).y] = clusterID;
							}
							if (!searchArea.isPixelFound(new Point(currentPoint.x
									+ AroundPixel.getPos(i).x, currentPoint.y
									+ AroundPixel.getPos(i).y))) {
								foundList.add(new Point(currentPoint.x + AroundPixel.getPos(i).x,
										currentPoint.y + AroundPixel.getPos(i).y));
								searchArea.addPixelFound(new Point(currentPoint.x
										+ AroundPixel.getPos(i).x, currentPoint.y
										+ AroundPixel.getPos(i).y));
							}
						}
					}
				}
			}
		}
		return canvas;
	}

	/**
	 * searches image down each column and marks the background as if it has been removed from the
	 * image returns the number of chunks of the background marked removed
	 * 
	 * @return the number of chunks of the background marked removed
	 */
	public int removeBackground(GeneticSlideImage img) {
		boolean aboveThreshold=true;
		this.firstPassCount = 0;
		LinkedList<ChromosomeCluster> tempClusterList = new LinkedList<ChromosomeCluster>();
		int clusterNum = 0;
		// color that will be used to store pixel color to check
		Color currentPxColor = new Color(0, 0, 0);
		ChromosomeCluster temp = new ChromosomeCluster(clusterNum);
		for (int r = pixelSpace; r < img.getImgWidth() - pixelSpace; r += pixelSpace) {
			for (int j = pixelSpace; j < img.getImgHeight() - pixelSpace; j += pixelSpace) {
				if (!img.getSearchArea().isPixelChecked(new Point(r, j))) {
					// get pixel color from point
					currentPxColor = img.getColorAt(r, j);
					if (PixelColor.isAboveThreshold(currentPxColor, img.getBackgroundThreshold())) {
						img.getSearchArea().markPixelChecked(new Point(r,j));
						temp = getCluster(img.getSearchArea(),img, 600, new Point(r, j),new Point(r, j), temp,aboveThreshold,img.getBackgroundThreshold());
						// newly added for chromosomes
						if (temp != null) {
							//temp = new ChromosomeCluster(temp);
							temp.setClusterNimageID(clusterNum++);
							temp.setTitle(img.getImageName());
							tempClusterList.add(temp);
						}
					}
				}
			}
		}
		while (!tempClusterList.isEmpty()) {
			ChromosomeCluster tempPop = tempClusterList.pop();
//			System.out.println();
//			System.out.println("Loc: " + tempPop.getImageLocation().x + ","
//					+ tempPop.getImageLocation().y);
//			System.out.println("pixelC: " + tempPop.getPixelCount());
//			System.out.println(tempPop.getTitle());
		}
//		System.out.println("FirstPass: " + this.firstPassCount + "   BackGroundChunksRemoved: "
//				+ clusterNum);

		return clusterNum;

	}
	public void outCanvas(SearchArea canvas){
		for (int j = 0; j < canvas.getHeight(); j++) {
			for (int i = 0; i < canvas.getWidth(); i++) {
				if(canvas.isPixelChecked(new Point(i,j))){
					System.out.print('0');
				}
				else{
					System.out.print('_');
				}
			}
			System.out.println();
		}
			

	}

	/**
	 * finds all the black bands in a cluster
	 * 
	 * @return the number of bands found in cluster
	 */
	public LinkedList<ChromosomeCluster> getBlackBands(GeneticSlideImage img,ChromosomeCluster myCluster) {
		boolean aboveThreshold=false;
		//TODO(aamcknig): make this not a constant value 26 for grayscale
		int threshold=106;
		this.firstPassCount = 0;
		LinkedList<ChromosomeCluster> tempBandList = new LinkedList<ChromosomeCluster>();
		SearchArea clusterArea=new SearchArea(myCluster.getSize().x,myCluster.getSize().y);
		clusterArea.prepForCluster(myCluster, null);
		int clusterNum = 0;
		// color that will be used to store pixel color to check
		Color currentPxColor = new Color(0, 0, 0);
		ChromosomeCluster temp = new ChromosomeCluster(clusterNum);
		for (int r = 0; r < clusterArea.getWidth() ; r += 1) {
			for (int j = 0; j < clusterArea.getHeight() ; j += 1) {
				if (!clusterArea.isPixelChecked(new Point(r, j))) {
					// get pixel color from point
					Point clusterPoint=new Point(r,j);
					Point tryPoint=new Point(r+myCluster.getImageLocation().x, j+myCluster.getImageLocation().y);
					currentPxColor = img.getColorAt(tryPoint.x, tryPoint.y);
					if (PixelColor.isBelowThreshold(currentPxColor, threshold)) {
						clusterArea.markPixelChecked(new Point(r, j));
						//TODO(aamcknig):pass second point for cluster location for search area
						temp = getCluster(clusterArea,img, (int)Math.round(img.getChromoWidth()*2.5),
								tryPoint,  clusterPoint, temp,aboveThreshold,threshold);
						// newly added for chromosomes
						if (temp != null) {
							//temp = new ChromosomeCluster(temp);
							temp.setClusterNimageID(clusterNum++);
							temp.setTitle(myCluster.getTitle());
							tempBandList.add(temp);
						}
					}
				}
			}
		}

//		System.out.println("FirstPass: " + this.firstPassCount + "   BackGroundChunksRemoved: "
//				+ clusterNum);

		return tempBandList;

	}

	/**
	 * searches image down each column and adds all found clusters to clusterList returns the number
	 * of clusters found in the image img
	 * 
	 * @return the number clusters found in the image
	 */

	public int findClusters(GeneticSlideImage img) {
		int clusterNum = 0;
		this.firstPassCount = 0;
		this.clusterList = new LinkedList<ChromosomeCluster>();
		Color color1 = new Color(0, 0, 0);// color that will be used to store pixel color to check
		ChromosomeCluster temp = new ChromosomeCluster(clusterNum);
		// made plus one change chromosomes
		for (int r = 2 + 1; r < img.getImgWidth() ; r += 2) {
			for (int j = 2; j < img.getImgHeight() ; j += 2) {
				if (!img.getSearchArea().isPixelChecked(new Point(r, j))) {
					img.getSearchArea().markPixelChecked(new Point(r,j));
					color1 = img.getColorAt(r, j);// get pixel color from point
					temp = getClusterLeft(img.getSearchArea(), 500, color1, r, j, temp);
					if (temp != null) {
						//temp = new ChromosomeCluster(temp);
						temp.setClusterNimageID(clusterNum++);
						temp.setTitle(img.getImageName());
						clusterList.add(temp);
						temp.createSkeleton();
						img.addWidth(temp.getWidths(0));
						img.addWidth(temp.getWidths(1));
						if (((temp.getWidths(0) >= 8 && temp.getWidths(0) < 30) || (temp
								.getWidths(1) >= 8 && temp.getWidths(1) < 30))) {
							if ((temp.getPixelCount() < this.firstPixelMax)
									|| (temp.getWidths(0) < 20 && temp.getWidths(1) < 20)) {
								if (!((temp.getWidths(0) > 30 || temp.getWidths(1) > 30))) {
									temp.setkeepThisCluster();
								}
							}
						}
//						System.out.println("Image: " + temp.getTitle() + " Count: "
//								+ temp.getClusterNimageID());
					}
				}
			}
		}
//		System.out.println("FirstPass: " + this.firstPassCount);
		double avgChromosomewidth = img.calcFinalAverage();

//		System.out.println("Width: " + avgChromosomewidth);
		return clusterNum;// targetNimgCount;
	}
	/**
	 * searches image down each column and adds all found clusters to clusterList returns the number
	 * of clusters found in the image img
	 * 
	 * @return the number clusters found in the image
	 */

	public int splitClusters(ChromosomeCluster myCluster,LinkedList<PointList> cutLines,GeneticSlideImage image) {
		this.firstPassCount = 0;
		int splitCount=0;
		LinkedList<ChromosomeCluster> tempClusters=new LinkedList<ChromosomeCluster>();
		SearchArea clusterArea=new SearchArea(myCluster.getSize().x,myCluster.getSize().y);
		clusterArea.prepForCluster(myCluster, cutLines);
		ChromosomeCluster temp = new ChromosomeCluster(splitCount);
		// made plus one change chromosomes
		for (int r = 0; r < myCluster.getSize().x ; r += 2) {
			for (int j = 0; j < myCluster.getSize().y ; j += 2) {
				if (!clusterArea.isPixelChecked(new Point(r, j))) {
					//color1 = img.getColorAt(r, j);// get pixel color from point
					Color tempColor=new Color(0,0,0);
					clusterArea.markPixelChecked(new Point(r,j));
					temp = getClusterLeft(clusterArea, 500, tempColor, r, j, temp);
					if (temp != null) {
						Point imageLocation=new Point(temp.getImageLocation().x+myCluster.getImageLocation().x,temp.getImageLocation().y+myCluster.getImageLocation().y);
						temp.setImageLocation(imageLocation);
						//TODO(aamcknig):figure out what is broke with output images
						//temp = new ChromosomeCluster(temp);
						temp.setClusterNimageID(splitCount);
						temp.setTitle(myCluster.getTitle().substring(0,myCluster.getTitle().indexOf('.'))+"_"+myCluster.getClusterNimageID()+"_.");
						splitCount++;
						tempClusters.add(temp);
						temp.createSkeleton();
						if (((temp.getWidths(0) >= 8 && temp.getWidths(0) < 30) || (temp
								.getWidths(1) >= 8 && temp.getWidths(1) < 30))) {
							if ((temp.getPixelCount() < this.firstPixelMax)
									|| (temp.getWidths(0) < 20 && temp.getWidths(1) < 20)) {
								if (!((temp.getWidths(0) > 30 || temp.getWidths(1) > 30))) {
									temp.setkeepThisCluster();
								}
							}
						}
//						System.out.println("Image: " + temp.getTitle() + " Count: "
//								+ temp.getClusterNimageID());
					}
				}
			}
		}
		if(tempClusters.size()>1){
			for(int i=0;i<tempClusters.size();i++){
				this.splitList.add(tempClusters.get(i));
			}
		}
		return tempClusters.size();// targetNimgCount;
	}

	/**
	 * returns a cluster of all connected pixels matching in color that fit in double the
	 * searchWidth
	 * 
	 * @param searchWidth
	 *            half of the width of the squared search area
	 * @param colorOItem
	 *            the color to match
	 * @param xCor
	 *            the x coordinate of the starting pixel
	 * @param yCor
	 *            the y coordinate of the starting pixel
	 * @param shpN
	 *            place found cluster is stored
	 * @return returns the cluster if found and null if no cluster found
	 */
	public ChromosomeCluster getCluster(SearchArea searchArea,GeneticSlideImage img,
			int searchWidth, Point imgCor,Point searchAreaPoint,
			ChromosomeCluster shpN,boolean aboveThreshold,int threshold) {
		// if the point has coordinates less than zero or off screen in neg
		// direction
		if (imgCor.x < 0 || imgCor.y < 0) {
			return null;// return null for no cluster found
		}
		int sizeSquared = 2 * searchWidth;// double width for search area
		// create a 2d integer canvas to mark matching connected pixels on
		short[][] canvas = new short[sizeSquared][sizeSquared];
		Rectangle canvasBounds = new Rectangle(0, 0, sizeSquared, sizeSquared);
		for (int y = 0; y < sizeSquared; y++) {
			for (int x = 0; x < sizeSquared; x++) {
				// initialize all spots on the 2d canvas to not checked
				canvas[y][x] = -5;
			}
		}
		Point canvasStart = new Point(searchWidth, searchWidth);
		canvas[canvasStart.x][canvasStart.y] = 0;// mark the center of the
													// canvas as a found pixel
		/*
		 * find all connected matching pixels startingat the center of canvas and the point
		 * xCor,yCormarking the matching pixels as the number 1ont the canvas
		 */
		this.currPixelCount = 0;
		Rectangle allBounds=this.getBounds(img, canvasBounds, searchArea, imgCor, searchAreaPoint, canvasStart);
		canvas = getMatchingPixel(searchArea,img, allBounds, imgCor,searchAreaPoint, canvasStart, canvas,
				(short) 0,aboveThreshold,threshold);
		if (/* !this.onImgEdge&& */this.currPixelCount > this.firstPixelMin||!aboveThreshold) {
			shpN = new ChromosomeCluster(new Point(sizeSquared, sizeSquared));
			// store the cluster marked by the number 1 in cluster
			shpN.setCluster(canvas, imgCor, 0,canvasStart);
			this.firstPassCount++;
			// shpN.clusterOut();
			return shpN;
		}
		// return the cluster found
		return null;
		/*
		 * the number 4 has been changed -5 currently testing -5 check in Cluster.class if work on
		 * this issue
		 */
	}

	/**
	 * returns a cluster of all connected pixels left after background remved that fit in double the
	 * searchWidth
	 * 
	 * @param searchWidth
	 *            half of the width of the squared search area
	 * @param colorOItem
	 *            the color to match
	 * @param xCor
	 *            the x coordinate of the starting pixel
	 * @param yCor
	 *            the y coordinate of the starting pixel
	 * @param shpN
	 *            place found cluster is stored
	 * @return returns the cluster if found and null if no cluster found
	 */
	public ChromosomeCluster getClusterLeft(SearchArea searchArea, int searchWidth,
			Color colorOItem, int xCor, int yCor, ChromosomeCluster shpN) {
		if (xCor < 0 || yCor < 0) {// if the point has cordinates less than zero
									// or off screen in neg direction
			return null;// return null for no cluster found
		}
		int sizeSquared = 2 * searchWidth;// double width for search area
		// create a 2d integer canvas to mark matching connected pixels on
		short[][] canvas = new short[sizeSquared][sizeSquared];
		Rectangle canvasBounds = new Rectangle(0, 0, sizeSquared, sizeSquared);
		// initialize all spots on the 2d canvas to not checked
		for (int i = 0; i < sizeSquared; i++)
			// loop y area
			for (int j = 0; j < sizeSquared; j++)
				// loop x area
				canvas[i][j] = -5;
		Point canvasStart = new Point(searchWidth, searchWidth);
		// mark the center of the canvas as a found pixel
		canvas[canvasStart.x][canvasStart.y] = 0;
		/*
<<<<<<< HEAD:project/KaryoTyper/src/extraction/Extractor.java
		 * find all connected matching pixels startingat the center of canvas and the point
		 * xCor,yCormarking the matching pixels as the number 1ont the canvas
=======
		 * find all connected matching pixels starting at the center of canvas
		 * and the point xCor,yCormarking the matching pixels as the number 1ont
		 * the canvas
>>>>>>> origin/ahkeslin:project/KaryoTyper/src/runner/Extractor.java
		 */
		this.currPixelCount = 0;
		canvas = getMatchingPixelLeft(searchArea, canvasBounds, new Point(xCor, yCor), canvasStart,
				canvas, (short) 0);
		if (/* !this.onImgEdge&& */this.currPixelCount > this.firstPixelMin) {
			shpN = new ChromosomeCluster(new Point(sizeSquared, sizeSquared));
			// store the cluster marked by the number 1 in cluster
			shpN.setCluster(canvas,  new Point(xCor, yCor), 0,canvasStart);
			this.firstPassCount++;
			return shpN;
		}
		// return the cluster found
		return null;
		/*
		 * the number 4 has been changed -5 currently testing -5 check in Cluster.class if work on
		 * this issue
		 */
	}

}
