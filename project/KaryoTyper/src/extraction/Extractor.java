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

/**
 * @author andrew
 * 
 */
public class Extractor {
	private LinkedList<ChromosomeCluster> clusterList;
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
	private short[][] getMatchingPixel(GeneticSlideImage img, Rectangle bounds, Point currentCoord,
			Point canvasOrigin, short[][] canvas, short clusterID) {
		LinkedList<Point> foundList = new LinkedList<Point>();
		Point canvasOffset = new Point(canvasOrigin.x - currentCoord.x, canvasOrigin.y
				- currentCoord.y);
		img.getSearchArea().initNextPixel();
		foundList.add(currentCoord);
		while (!foundList.isEmpty()) {
			currentCoord = foundList.pop();

			// loop 8 times once for every position around the center pixel
			for (int i = 0; i < 8; i++) {
				/*
				 * if the spot to be checked is inside of the bounds and if the pixel is inside the
				 * visible resolution of the screen
				 */
				if (currentCoord.x + AroundPixel.getPos(i).x < img.getImgWidth()
						&& currentCoord.y + AroundPixel.getPos(i).y < img.getImgHeight()
						&& currentCoord.x > 0 && currentCoord.y > 0) {
					if (!img.getSearchArea().isPixelChecked(new Point(currentCoord.x + AroundPixel.getPos(i).x,
							currentCoord.y + AroundPixel.getPos(i).y))) {
						// if the spot to be checked value is -5 meaning it
						// hasn't been checked yet
						if (!bounds.contains(new Point(currentCoord.x + canvasOffset.x
								+ AroundPixel.getPos(i).x, currentCoord.y + canvasOffset.y
								+ AroundPixel.getPos(i).y))
								|| canvas[currentCoord.x + canvasOffset.x + AroundPixel.getPos(i).x][currentCoord.y
										+ canvasOffset.y + AroundPixel.getPos(i).y] == -5) {
							// if the pixel at the position aroundDot matches
							// the color
							Color temp = img.getColorAt(currentCoord.x + AroundPixel.getPos(i).x,
									currentCoord.y + AroundPixel.getPos(i).y);
							// was isTargeTColor2(this.imgAvgColor,temp)
							if (PixelColor.isBackGroundColor(temp, img.getColorThreshold())) {
								img.getSearchArea().markPixelChecked(new Point(currentCoord.x
										+ AroundPixel.getPos(i).x, currentCoord.y
										+ AroundPixel.getPos(i).y));
								this.currPixelCount++;
								// paint the canvas at the respectful position
								// on 2d canvas to the clusterID number
								if (bounds.contains(new Point(currentCoord.x + canvasOffset.x
										+ AroundPixel.getPos(i).x, currentCoord.y + canvasOffset.y
										+ AroundPixel.getPos(i).y))) {
									canvas[currentCoord.x + canvasOffset.x
											+ AroundPixel.getPos(i).x][currentCoord.y
											+ canvasOffset.y + AroundPixel.getPos(i).y] = clusterID;
								}
								if (!img.getSearchArea().isPixelFound(new Point(currentCoord.x
										+ AroundPixel.getPos(i).x, currentCoord.y
										+ AroundPixel.getPos(i).y))) {
									foundList.add(new Point(currentCoord.x
											+ AroundPixel.getPos(i).x, currentCoord.y
											+ AroundPixel.getPos(i).y));
									img.getSearchArea().addPixelFound(new Point(currentCoord.x
											+ AroundPixel.getPos(i).x, currentCoord.y
											+ AroundPixel.getPos(i).y));
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
	private short[][] getMatchingPixelLeft(GeneticSlideImage img, Rectangle bounds,
			Point currentPoint, Point canvasOrigin, short[][] canvas, short clusterID) {
		LinkedList<Point> foundList = new LinkedList<Point>();
		Point canvasOffset = new Point(canvasOrigin.x - currentPoint.x, canvasOrigin.y
				- currentPoint.y);
		img.getSearchArea().initNextPixel();
		foundList.add(new Point(currentPoint));
		while (!foundList.isEmpty()) {
			currentPoint = foundList.pop();

			// loop 8 times once for every position around the center pixel
			for (int i = 0; i < 8; i++) {
				/*
				 * if the spot to be checked is inside of the bounds and if the pixel is inside the
				 * visible resolution of the screen
				 */
				if (currentPoint.x + AroundPixel.getPos(i).x < img.getImgWidth()
						&& currentPoint.y + AroundPixel.getPos(i).y < img.getImgHeight()
						&& currentPoint.x > 0 && currentPoint.y > 0) {
					if (!img.getSearchArea().isPixelChecked(new Point(currentPoint.x + AroundPixel.getPos(i).x,
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
							img.getSearchArea().markPixelChecked(new Point(
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
							if (!img.getSearchArea().isPixelFound(new Point(currentPoint.x
									+ AroundPixel.getPos(i).x, currentPoint.y
									+ AroundPixel.getPos(i).y))) {
								foundList.add(new Point(currentPoint.x + AroundPixel.getPos(i).x,
										currentPoint.y + AroundPixel.getPos(i).y));
								img.getSearchArea().addPixelFound(new Point(currentPoint.x
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
		this.firstPassCount = 0;
		LinkedList<ChromosomeCluster> tempClusterList = new LinkedList<ChromosomeCluster>();
		int clusterNum = 0;
		Color color1 = new Color(0, 0, 0);// color that will be used to store
											// pixel color to check
		ChromosomeCluster temp = new ChromosomeCluster(clusterNum);
		for (int r = pixelSpace; r < img.getImgWidth() - pixelSpace; r += pixelSpace) {
			for (int j = pixelSpace; j < img.getImgHeight() - pixelSpace; j += pixelSpace) {
				if (!img.getSearchArea().isPixelChecked(new Point(r, j))) {
					color1 = img.getColorAt(r, j);// get pixel color from point
					// was isTargeTColor2(this.imgAvgColor, color1)
					if (PixelColor.isBackGroundColor(color1, img.getColorThreshold())) {
						temp = getCluster(img, 600, r, j, temp);
						// newly added for chromosomes
						if (temp != null) {
							temp = new ChromosomeCluster(temp);
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
			System.out.println();
			System.out.println("Loc: " + tempPop.getImageLocation().x + ","
					+ tempPop.getImageLocation().y);
			System.out.println("pixelC: " + tempPop.getPixelCount());
			System.out.println(tempPop.getTitle());
		}
		System.out.println("FirstPass: " + this.firstPassCount + "   BackGroundChunksRemoved: "
				+ clusterNum);

		return clusterNum;

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
		for (int r = pixelSpace + 1; r < img.getImgWidth() - pixelSpace; r += pixelSpace) {
			for (int j = pixelSpace; j < img.getImgHeight() - pixelSpace; j += pixelSpace) {
				if (!img.getSearchArea().isPixelChecked(new Point(r, j))) {
					color1 = img.getColorAt(r, j);// get pixel color from point
					temp = getClusterLeft(img, 500, color1, r, j, temp);
					if (temp != null) {
						temp = new ChromosomeCluster(temp);
						temp.setClusterNimageID(clusterNum++);
						temp.setTitle(img.getImageName());
						clusterList.add(temp);
						temp.createSkeleton(img);
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
						System.out.println("Image: " + temp.getTitle() + " Count: "
								+ temp.getClusterNimageID());
						clusterList.add(temp);
					}
				}
			}
		}
		System.out.println("FirstPass: " + this.firstPassCount);
		double avgChromosomewidth = img.calcFinalAverage();

		System.out.println("Width: " + avgChromosomewidth);
		return clusterNum;// targetNimgCount;
	}
	/**
	 * searches image down each column and adds all found clusters to clusterList returns the number
	 * of clusters found in the image img
	 * 
	 * @return the number clusters found in the image
	 */

	public int splitClusters(ChromosomeCluster myCluster,LinkedList<PointList> cutLines) {
		int clusterNum = 0;
		this.firstPassCount = 0;
		this.clusterList = new LinkedList<ChromosomeCluster>();
		Color color1 = new Color(0, 0, 0);// color that will be used to store pixel color to check
		ChromosomeCluster temp = new ChromosomeCluster(clusterNum);
		// made plus one change chromosomes
		for (int r = pixelSpace + 1; r < img.getImgWidth() - pixelSpace; r += pixelSpace) {
			for (int j = pixelSpace; j < img.getImgHeight() - pixelSpace; j += pixelSpace) {
				if (!img.getSearchArea().isPixelChecked(new Point(r, j))) {
					color1 = img.getColorAt(r, j);// get pixel color from point
					temp = getClusterLeft(img, 500, color1, r, j, temp);
					if (temp != null) {
						temp = new ChromosomeCluster(temp);
						temp.setClusterNimageID(clusterNum++);
						temp.setTitle(img.getImageName());
						clusterList.add(temp);
						temp.createSkeleton(img);
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
						System.out.println("Image: " + temp.getTitle() + " Count: "
								+ temp.getClusterNimageID());
						clusterList.add(temp);
					}
				}
			}
		}
		System.out.println("FirstPass: " + this.firstPassCount);
		double avgChromosomewidth = img.calcFinalAverage();

		System.out.println("Width: " + avgChromosomewidth);
		return clusterNum;// targetNimgCount;
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
	public ChromosomeCluster getCluster(GeneticSlideImage img, int searchWidth, int xCor, int yCor,
			ChromosomeCluster shpN) {
		// if the point has coordinates less than zero or off screen in neg
		// direction
		if (xCor < 0 || yCor < 0) {
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
		Point canvasStart = new Point((int) (sizeSquared / 2.0), (int) (sizeSquared / 2.0));
		canvas[canvasStart.x][canvasStart.y] = 0;// mark the center of the
													// canvas as a found pixel
		/*
		 * find all connected matching pixels startingat the center of canvas and the point
		 * xCor,yCormarking the matching pixels as the number 1ont the canvas
		 */
		this.currPixelCount = 0;
		canvas = getMatchingPixel(img, canvasBounds, new Point(xCor, yCor), canvasStart, canvas,
				(short) 0);
		if (/* !this.onImgEdge&& */this.currPixelCount > this.firstPixelMin) {
			shpN = new ChromosomeCluster(new Point(sizeSquared, sizeSquared));
			// store the cluster marked by the number 1 in cluster
			shpN.setCluster(canvas, xCor, yCor, 0);
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
	 *            the x cordinate of the starting pixel
	 * @param yCor
	 *            the y cordinate of the starting pixel
	 * @param shpN
	 *            place found cluster is stored
	 * @return returns the cluster if found and null if no cluster found
	 */
	public ChromosomeCluster getClusterLeft(GeneticSlideImage img, int searchWidth,
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
		Point canvasStart = new Point((int) (sizeSquared / 2.0), (int) (sizeSquared / 2.0));
		// mark the center of the canvas as a found pixel
		canvas[canvasStart.x][canvasStart.y] = 0;
		/*
		 * find all connected matching pixels startingat the center of canvas and the point
		 * xCor,yCormarking the matching pixels as the number 1ont the canvas
		 */
		this.currPixelCount = 0;
		canvas = getMatchingPixelLeft(img, canvasBounds, new Point(xCor, yCor), canvasStart,
				canvas, (short) 0);
		if (/* !this.onImgEdge&& */this.currPixelCount > this.firstPixelMin) {
			shpN = new ChromosomeCluster(new Point(sizeSquared, sizeSquared));
			// store the cluster marked by the number 1 in cluster
			shpN.setCluster(canvas, xCor, yCor, 0);
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
