import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import Color.PixelColor;
import Target.TargetImage;
import Target.TargetShape;
import basicObjects.AroundPixel;
import basicObjects.MedialAxisGraph;
import basicObjects.PixelPoint;
import basicObjects.Shape;

import com.drew.imaging.jpeg.JpegProcessingException;

public class TargetGetter {
	public LinkedList<TargetShape> shapeList;
	/*
	 * aroundot is an array of 8 points that is a x,y difference from the center
	 * point pixel 107 |-1,-1| 0,-1| 1,-1| 2.6 or 
	 *                 |-1, 0| dot | 1, 0| <--- this is a visual of around dot 345
	 *                 |-1, 1| 0, 1| 1, 1|
	 */
	private AroundPixel aroundDot;
	private boolean[][] screenChecked;
	private boolean[][] spotNext;
	private int currPixelCount;
	private LinkedList<String> imageQue;
	private TargetImage img;
	private int allowedColorDiff;
	private int pixelSpace;
	private Color colorAverage;
	private int colorCount;
	private int firstPixelMax;
	private int firstPixelMin;
	private int colorThreshold;
	private int firstPassCount;
	private int removedCount;
	private long start;

	public TargetGetter(String jpegPath) {
		aroundDot = new AroundPixel();
		this.colorThreshold = 245;
		this.imageQue = new LinkedList<String>();
		start = System.currentTimeMillis();
		this.firstPixelMax = 11000;
		this.firstPixelMin = 30;
		this.allowedColorDiff = 70;
		this.pixelSpace = 3;
		colorAverage = new Color(0, 0, 0);
		this.colorCount = 0;
		loadFiles();
		currPixelCount = 0;
		img = null;
	}

	public void loadNewImage(String filename) {
		img = new TargetImage(filename);
		screenChecked = new boolean[img.getImgWidth()][img.getImgHeight()];
		this.spotNext = new boolean[img.getImgWidth()][img.getImgHeight()];
		img.computeScale();
		img.graphScale();
	}

	public void initScreenChecked() {
		for (int i = 0; i < screenChecked.length; i++) {
			for (int j = 0; j < screenChecked[0].length; j++) {
				this.screenChecked[i][j] = false;
			}
		}
	}

	public void initSpotNext() {
		for (int i = 0; i < spotNext.length; i++) {
			for (int j = 0; j < spotNext[0].length; j++) {
				this.spotNext[i][j] = false;
			}
		}
	}

	public void loadFiles() {
		shapeList = new LinkedList<TargetShape>();
	}

	/**
	 * gets all connected matching colored pixels and returns shape on the 2d integer array -canvas-
	 * painted by the number -shapeID-
	 * 
	 * @param bounds
	 *            is a rectangle that only pixels inside of will be checked
	 * @param colorOItem
	 *            is the color to match
	 * @param xyCor
	 *            is the starting pixel
	 * @param xyCanvas
	 *            is the starting position on the 2d integer canvas
	 * @param canvas
	 *            is the 2d integer canvas that stores the shape(IMPORTANT: expected to have all
	 *            positions not checked initialized to the value -5)
	 * @param shapeID
	 *            is the number entered on canvas that represents matching connected pixels
	 * @return the 2d integer canvas that represents the shape
	 */
	private short[][] getMatchingPixel(Rectangle bounds, Color colorOItem, Point xyCor,
			Point xyCanvas, short[][] canvas, short shapeID, Color colorLeft) {
		LinkedList<PixelPoint> foundList = new LinkedList<PixelPoint>();
		colorLeft = new Color(0, 0, 0);
		Point canvasDiff = new Point(xyCanvas.x - xyCor.x, xyCanvas.y - xyCor.y);
		this.initSpotNext();
		foundList.add(new PixelPoint(xyCor, colorOItem, colorLeft));
		while (!foundList.isEmpty()) {
			PixelPoint curr = foundList.pop();
			colorOItem = new Color(curr.getPrevColor().getRed(), curr.getPrevColor().getGreen(),
					curr.getPrevColor().getBlue());
			xyCor = new Point(curr.getImgPoint());
			colorLeft = new Color(curr.getColorLeft().getRed(), curr.getColorLeft().getGreen(),
					curr.getColorLeft().getBlue());

			// loop 8 times once for every position around the center pixel
			for (int i = 0; i < 8; i++) {
				/*
				 * if the spot to be checked is inside of the boundsand if the
				 * pixel is inside the visible resolution of the screen
				 */
				if (xyCor.x + aroundDot.getPos(i).x < img.getImgWidth()
						&& xyCor.y + aroundDot.getPos(i).y < img.getImgHeight() && xyCor.x > 0
						&& xyCor.y > 0) {
					if (!screenChecked[xyCor.x + aroundDot.getPos(i).x][xyCor.y
							+ aroundDot.getPos(i).y]) {
						// if the spot to be checked value is -5 meaning it
						// hasnt been checked yet
						if (!bounds.contains(new Point(xyCor.x + canvasDiff.x
								+ aroundDot.getPos(i).x, xyCor.y + canvasDiff.y
								+ aroundDot.getPos(i).y))
								|| canvas[xyCor.x + canvasDiff.x + aroundDot.getPos(i).x][xyCor.y
										+ canvasDiff.y + aroundDot.getPos(i).y] == -5) {
							// if the pixel at the position aroundDot matches
							// the color
							Color temp = img.getColorAt(xyCor.x + aroundDot.getPos(i).x, xyCor.y
									+ aroundDot.getPos(i).y);
							// was isTargeTColor2(this.imgAvgColor,temp)
							if (PixelColor.isBackGroundColor(temp, this.colorThreshold)) {
								this.colorAverage = averageColor(temp);
								screenChecked[xyCor.x + aroundDot.getPos(i).x][xyCor.y
										+ aroundDot.getPos(i).y] = true;
								this.currPixelCount++;
								// paint the canvas at the respectful position
								// on 2d canvas to the shapeID number
								if (bounds.contains(new Point(xyCor.x + canvasDiff.x
										+ aroundDot.getPos(i).x, xyCor.y + canvasDiff.y
										+ aroundDot.getPos(i).y))) {
									canvas[xyCor.x + canvasDiff.x + aroundDot.getPos(i).x][xyCor.y
											+ canvasDiff.y + aroundDot.getPos(i).y] = shapeID;
								}
								if (!this.spotNext[xyCor.x + aroundDot.getPos(i).x][xyCor.y
										+ aroundDot.getPos(i).y]) {
									foundList.add(new PixelPoint(new Point(xyCor.x
											+ aroundDot.getPos(i).x, xyCor.y
											+ aroundDot.getPos(i).y), temp, colorLeft));
									this.spotNext[xyCor.x + aroundDot.getPos(i).x][xyCor.y
											+ aroundDot.getPos(i).y] = true;
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
	 * gets all connected matching colored pixels and returns shape on the 2d integer array -canvas-
	 * painted by the number -shapeID-
	 * 
	 * @param bounds
	 *            is a rectangle that only pixels inside of will be checked
	 * @param colorOItem
	 *            is the color to match
	 * @param xyCor
	 *            is the starting pixel
	 * @param xyCanvas
	 *            is the starting position on the 2d integer canvas
	 * @param canvas
	 *            is the 2d integer canvas that stores the shape(IMPORTANT: expected to have all
	 *            positions not checked initialized to the value -5)
	 * @param shapeID
	 *            is the number entered on canvas that represents matching connected pixels
	 * @return the 2d integer canvas that represents the shape
	 */
	private short[][] getMatchingPixelLeft(Rectangle bounds, Color colorOItem, Point xyCor,
			Point xyCanvas, short[][] canvas, short shapeID, Color colorLeft) {
		LinkedList<PixelPoint> foundList = new LinkedList<PixelPoint>();
		colorLeft = new Color(0, 0, 0);
		Point canvasDiff = new Point(xyCanvas.x - xyCor.x, xyCanvas.y - xyCor.y);
		this.initSpotNext();
		// Color orignalColor=new
		// Color(colorOItem.getRed(),colorOItem.getGreen(),colorOItem.getBlue());
		foundList.add(new PixelPoint(xyCor, colorOItem, colorLeft));
		while (!foundList.isEmpty()) {
			PixelPoint curr = foundList.pop();
			colorOItem = new Color(curr.getPrevColor().getRed(), curr.getPrevColor().getGreen(),
					curr.getPrevColor().getBlue());
			xyCor = new Point(curr.getImgPoint());
			colorLeft = new Color(curr.getColorLeft().getRed(), curr.getColorLeft().getGreen(),
					curr.getColorLeft().getBlue());

			if (colorLeft.getRed() < 2) {
				// loop 8 times once for every position around the center pixel
				for (int i = 0; i < 8; i++) {
					/*
					 * if the spot to be checked is inside of the boundsand if
					 * the pixel is inside the visible resolution of the screen
					 */
					if (xyCor.x + aroundDot.getPos(i).x < img.getImgWidth()
							&& xyCor.y + aroundDot.getPos(i).y < img.getImgHeight() && xyCor.x > 0
							&& xyCor.y > 0) {
						if (!screenChecked[xyCor.x + aroundDot.getPos(i).x][xyCor.y
								+ aroundDot.getPos(i).y]) {
							// if the spot to be checked value is -5 meaning it
							// hasnt been checked yet
							if (!bounds.contains(new Point(xyCor.x + canvasDiff.x
									+ aroundDot.getPos(i).x, xyCor.y + canvasDiff.y
									+ aroundDot.getPos(i).y))
									|| canvas[xyCor.x + canvasDiff.x + aroundDot.getPos(i).x][xyCor.y
											+ canvasDiff.y + aroundDot.getPos(i).y] == -5) {
								// if the pixel at the position aroundDot
								// matches the color
								Color temp = img.getColorAt(xyCor.x + aroundDot.getPos(i).x,
										xyCor.y + aroundDot.getPos(i).y);
								this.colorAverage = averageColor(temp);
								screenChecked[xyCor.x + aroundDot.getPos(i).x][xyCor.y
										+ aroundDot.getPos(i).y] = true;
								this.currPixelCount++;
								// paint the canvas at the respectful position
								// on 2d canvas to the shapeID number
								if (bounds.contains(new Point(xyCor.x + canvasDiff.x
										+ aroundDot.getPos(i).x, xyCor.y + canvasDiff.y
										+ aroundDot.getPos(i).y))) {
									canvas[xyCor.x + canvasDiff.x + aroundDot.getPos(i).x][xyCor.y
											+ canvasDiff.y + aroundDot.getPos(i).y] = shapeID;
								}
								if (!this.spotNext[xyCor.x + aroundDot.getPos(i).x][xyCor.y
										+ aroundDot.getPos(i).y]) {
									foundList.add(new PixelPoint(new Point(xyCor.x
											+ aroundDot.getPos(i).x, xyCor.y
											+ aroundDot.getPos(i).y), temp, colorLeft));
									this.spotNext[xyCor.x + aroundDot.getPos(i).x][xyCor.y
											+ aroundDot.getPos(i).y] = true;
								}
							}
						}
					}
				}
			}
		}
		return canvas;
	}

	private short[][] getMatchingPixelExtra(Rectangle bounds, Color colorOItem, Point xyCor,
			Point xyCanvas, short[][] canvas, short shapeID, Color colorLeft) {
		if ((colorLeft.getRed() + colorLeft.getGreen() + colorLeft.getBlue()) > (600 - this.allowedColorDiff)) {
			// loop 8 times once for every position around the center pixel
			for (int i = 0; i < 8; i++) {
				/*
				 * if the spot to be checked is inside of the boundsand if the
				 * pixel is inside the visible resolution of the screen
				 */
				if (this.currPixelCount < this.firstPixelMax
						&& bounds.contains(new Point((xyCanvas.x + aroundDot.getPos(i).x - 1),
								(xyCanvas.y + aroundDot.getPos(i).y - 1)))
						&& xyCor.x + aroundDot.getPos(i).x < img.getImgWidth()
						&& xyCor.y + aroundDot.getPos(i).y < img.getImgHeight() && xyCor.x > 0
						&& xyCor.y > 0 && canvas.length < xyCanvas.x + aroundDot.getPos(i).x
						&& canvas[0].length < xyCanvas.y + aroundDot.getPos(i).y) {
					// if the spot to be checked value is -5 meaning it hasnt
					// been checked yet
					if (canvas[xyCanvas.x + aroundDot.getPos(i).x][xyCanvas.y
							+ aroundDot.getPos(i).y] == -5) {
						// if the pixel at the position aroundDot matches the
						// color
						Color temp = img.getColorAt(xyCor.x + aroundDot.getPos(i).x, xyCor.y
								+ aroundDot.getPos(i).y);
						if (PixelColor.isTargeTColorX(colorOItem, temp)) {
							this.currPixelCount++;
							// paint the canvas at the respectful position on 2d
							// canvas to the shapeID number
							canvas[xyCanvas.x + aroundDot.getPos(i).x][xyCanvas.y
									+ aroundDot.getPos(i).y] = shapeID;
							// recursively call this method with the new
							// position on screen and on 2d canvas to be checked
							// around
							getMatchingPixelExtra(
									bounds,
									temp,
									new Point(xyCor.x + aroundDot.getPos(i).x, xyCor.y
											+ aroundDot.getPos(i).y),
									new Point(xyCanvas.x + aroundDot.getPos(i).x, xyCanvas.y
											+ aroundDot.getPos(i).y), canvas, shapeID, colorLeft);
						} else {
							int red = colorLeft.getRed()
									- Math.abs(colorOItem.getRed() - temp.getRed());
							int green = colorLeft.getGreen()
									- Math.abs(colorOItem.getGreen() - temp.getGreen());
							int blue = colorLeft.getBlue()
									- Math.abs(colorOItem.getBlue() - temp.getBlue());
							if (red <= 0 || green <= 0 || blue <= 0) {
								red = 0;
								green = 0;
								blue = 0;
							}
							colorLeft = new Color(red, green, blue);
							this.currPixelCount++;
							// paint the canvas at the respectful position on 2d
							// canvas to the shapeID number
							canvas[xyCanvas.x + aroundDot.getPos(i).x][xyCanvas.y
									+ aroundDot.getPos(i).y] = shapeID;
							// recursively call this method with the new
							// position on screen and on 2d canvas to be checked
							// around
							getMatchingPixelExtra(
									bounds,
									colorOItem,
									new Point(xyCor.x + aroundDot.getPos(i).x, xyCor.y
											+ aroundDot.getPos(i).y),
									new Point(xyCanvas.x + aroundDot.getPos(i).x, xyCanvas.y
											+ aroundDot.getPos(i).y), canvas, shapeID, colorLeft);
						}
					}
				}
			}
		}
		// return the 2d integer canvas
		return canvas;
		/*
		 * changed 4 to -5 and now testing this change
		 */
	}

	public int containsColor(Color temp, LinkedList<Color> list) {
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if (PixelColor.isTargeTColorX(list.get(i), temp)) {
					return i;
				}
			}
		}
		return -1;
	}

	public LinkedList<Shape> getAllShape(Rectangle bounds, TargetShape inThisShape, boolean print) {
		// create a linked list to store shapes in
		LinkedList<Shape> allShapes = new LinkedList<Shape>();
		// list for storeing all found colors
		LinkedList<Color> foundColors = new LinkedList<Color>();
		short paintColor = 0;// number to paint the current color to canvas
		// is the count of colors found and the paint number of last found color
		int k = 0;
		// create a 2d integer array for painting numbers on
		short[][] canvas = new short[bounds.width][bounds.height];
		for (int i = 0; i < bounds.width; i++)
			// loop 100 times
			for (int j = 0; j < bounds.height; j++)
				// loop 100 times
				// initialize all values of canvas to -5 meaning not checked yet
				canvas[i][j] = -5;
		for (int i = bounds.y; i <= (bounds.y + bounds.height - 1)
				&& i < this.screenChecked[0].length; i++) {// loop thru the y
															// bounds
			for (int j = bounds.x; (j <= bounds.x + bounds.width - 1)
			// loop thru the x bounds
					&& i < this.screenChecked.length; j++) {
				// if the spot hasn't been checked yet
				if (canvas[j - bounds.x][i - bounds.y] == -5
						&& inThisShape.getValue(j - bounds.x, i - bounds.y)) {
					Color temp = img.getColorAt(j, i);// get the color of the
														// spot
					if (foundColors.isEmpty()) {// if list is empty
						foundColors.add(temp);// add first color
						paintColor = (short) ++k;// increment k to 1
						// if we already have this color
					} else if (containsColor(temp, foundColors) >= 0) {
						// set paintColor to color we already have
						paintColor = (short) (containsColor(temp, foundColors) + 1);
					} else {// if we got a new color
						foundColors.add(temp);// add the color
						paintColor = (short) ++k;// increment k
					}
					// paint current canvas spot to paintColor our paint number
					canvas[j - bounds.x][i - bounds.y] = paintColor;
					/*
					 * add all connected matching color pixels in bonds to the
					 * canvascheck the canvas for same color pixel connected to
					 * point j,i and matching the colorstart marking at j,i on
					 * the canvas
					 */
					canvas = this
							.getMatchingPixelExtra(new Rectangle(bounds.x, bounds.y, bounds.width,
									bounds.height), temp, new Point(j, i), new Point(j - bounds.x,
									i - bounds.y), canvas, paintColor, new Color(200, 200, 200));
				}
			}
		}

		// loop thru all different colors found
		for (int i = 1; i <= k; i++) {
			// add the shape of k color to shape list
			allShapes.add(new TargetShape(canvas, bounds.x, bounds.y, i));
		}

		return allShapes;// return list of shapes
		/*
		 * 
		 * changed number 4 to -5 currently testing
		 */
	}

	/**
	 * searches screen spiraling out and returns the point where it finds the item
	 * 
	 * @param bounds
	 *            is the area to search inside of
	 * @param startPnt
	 *            the point to start searching from
	 * @param itemName
	 *            is the string that appears when the mouse is over the item
	 * @param colorOItem
	 *            the color of item we are looking for
	 * @param colorOLetters
	 *            color of the letters that appear when mouse over item
	 * @param radiusInc
	 *            the number that will be added to the radius as it spirals out(how thurough it
	 *            searches)
	 * @param radiusSensitive
	 *            is the distance between x or y along the spiral that will be checked(how thurough
	 *            it searches)
	 * @return the point where item was found or -1,-1 if not found
	 * @throws JpegProcessingException
	 */
	public int findBackground(String filename) {
		this.firstPassCount = 0;
		this.removedCount = 0;
		LinkedList<TargetShape> tempShapeList = new LinkedList<TargetShape>();
		int shapeNum = 0;
		this.loadNewImage(filename);
		this.initScreenChecked();
		Color color1 = new Color(0, 0, 0);// color that will be used to store
											// pixel color to check
		TargetShape temp = new TargetShape(shapeNum);
		for (int r = pixelSpace; r < img.getImgWidth() - pixelSpace; r += pixelSpace) {
			for (int j = pixelSpace; j < img.getImgHeight() - pixelSpace; j += pixelSpace) {
				if (!screenChecked[r][j]) {
					color1 = img.getColorAt(r, j);// get pixel color from point
					this.colorAverage = new Color(color1.getRed(), color1.getGreen(),
							color1.getBlue());
					colorCount = 1;
					// was isTargeTColor2(this.imgAvgColor, color1)
					if (PixelColor.isBackGroundColor(color1, this.colorThreshold)) {
						temp = getShape(600, color1, r, j, temp);
						// newly added for chromosomes
						if (temp != null) {
							temp = new TargetShape(temp);
							temp.setColor(this.colorAverage);
							temp.setTargetNimageID(shapeNum++);
							temp.setTitle(filename);
							tempShapeList.add(temp);
						}
					}
				}
			}
		}
		while (!tempShapeList.isEmpty()) {
			TargetShape tempPop = tempShapeList.pop();
			System.out.println();
			System.out.println("Loc: " + tempPop.getScreenCordinate().x + ","
					+ tempPop.getScreenCordinate().y);
			System.out.println("pixelC: " + tempPop.getPixelCount());
			System.out.println(tempPop.getTitle());
		}
		System.out
				.println("FirstPass: " + this.firstPassCount + "   Removed: " + this.removedCount);

		return shapeNum;

	}

	public int findChromosomes(String filename, int shapeNum) {
		this.firstPassCount = 0;
		this.removedCount = 0;
		LinkedList<TargetShape> tempShapeList = new LinkedList<TargetShape>();
		// color that will be used to store pixel color to check
		Color color1 = new Color(0, 0, 0);
		TargetShape temp = new TargetShape(shapeNum);
		// made plus one change chromosomes
		for (int r = pixelSpace + 1; r < img.getImgWidth() - pixelSpace; r += pixelSpace) {
			for (int j = pixelSpace; j < img.getImgHeight() - pixelSpace; j += pixelSpace) {
				if (!screenChecked[r][j]) {

					color1 = img.getColorAt(r, j);// get pixel color from point
					this.colorAverage = new Color(color1.getRed(), color1.getGreen(),
							color1.getBlue());
					colorCount = 1;
					temp = getShapeLeft(200, color1, r, j, temp);

					if (temp != null) {
						temp = new TargetShape(temp);
						temp.setColor(this.colorAverage);
						temp.setTargetNimageID(shapeNum++);
						temp.setTitle(filename);

						tempShapeList.add(temp);
					}
				}
			}
		}
		while (!tempShapeList.isEmpty()) {
			TargetShape tempPop = tempShapeList.pop();

			System.out.println();
			System.out.println("Loc: " + tempPop.getScreenCordinate().x + ","
					+ tempPop.getScreenCordinate().y);
			System.out.println("pixelC: " + tempPop.getPixelCount());
			System.out.println(tempPop.getTitle());
			shapeList.add(tempPop);
			tempPop.getSkeleton(img);
			img.addWidth(tempPop.getWidths(0));
			img.addWidth(tempPop.getWidths(1));
			if (((tempPop.getWidths(0) >= 4 && tempPop.getWidths(0) < 15) || (tempPop.getWidths(1) >= 4 && tempPop
					.getWidths(1) < 15))) {
				if ((tempPop.getPixelCount() < this.firstPixelMax)
						|| (tempPop.getWidths(0) < 10 && tempPop.getWidths(1) < 10)) {
					if (!((tempPop.getWidths(0) > 15 || tempPop.getWidths(1) > 15))) {
						tempPop.setkeepThisShape();
					}
				}
			}
			System.out.println("Image: " + tempPop.getTitle() + " Count: "
					+ tempPop.getTargetNimageID());
			tempPop.writeShapeWidths();
		}
		System.out
				.println("FirstPass: " + this.firstPassCount + "   Removed: " + this.removedCount);
		double avgChromosomewidth = img.calcFinalAverage();

		System.out.println("Width: " + avgChromosomewidth);
		return shapeNum;// targetNimgCount;

	}

	public void printChromosomes() {
		for (int i = 0; i < this.shapeList.size(); i++) {
			TargetShape tempShape = this.shapeList.get(i);
			MedialAxisGraph tempGraph = new MedialAxisGraph(tempShape.getSkeltonPoints());
			tempShape.setMedialAxis(tempGraph.trimMedialAxis(
					(int) Math.round(img.getAverage() * .7), tempShape.getSkeltonPoints(),
					tempShape));
			if (tempShape.checkKeepThisShape()) {
				writeTargetImage(tempShape);
			} else {
				writeRemovedImage(tempShape);
			}
		}
		this.shapeList = new LinkedList<TargetShape>();
	}

	public void writeTargetImage(TargetShape tempShape) {
		try {
			File curDir = new File(".");
			String imageName = new File(tempShape.getTitle()).getName();
			File outputfile = new File(curDir.getCanonicalPath() + "/shapeData/Keep/"
					+ imageName.substring(0, imageName.indexOf('.')) + "_"
					+ (tempShape.getTargetNimageID()) + ".png");
			BufferedImage tempImg = img.getSubImage(tempShape, true);
			ImageIO.write(tempImg, "png", outputfile);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	public void writeRemovedImage(TargetShape tempShape) {
		try {
			File curDir = new File(".");
			String imageName = new File(tempShape.getTitle()).getName();
			File outputfile = new File(curDir.getCanonicalPath() + "/shapeData/Removed/"
					+ imageName.substring(0, imageName.indexOf('.')) + "_"
					+ (tempShape.getTargetNimageID()) + ".png");
			BufferedImage tempImg = img.getSubImage(tempShape, false);
			ImageIO.write(tempImg, "png", outputfile);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	/**
	 * returns a shape of all connected pixels matching in color that fit in double the searchWidth
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
	 *            place found shape is stored
	 * @return returns the shape if found and null if no shape found
	 */
	public TargetShape getShape(int searchWidth, Color colorOItem, int xCor, int yCor,
			TargetShape shpN) {
		// if the point has coordinates less than zero or off screen in neg
		// direction
		if (xCor < 0 || yCor < 0) {
			return null;// return null for no shape found
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
		 * find all connected matching pixels startingat the center of canvas
		 * and the point xCor,yCormarking the matching pixels as the number 1ont
		 * the canvas
		 */
		this.currPixelCount = 0;
		canvas = getMatchingPixel(canvasBounds, colorOItem, new Point(xCor, yCor), canvasStart,
				canvas, (short) 0, new Color(200, 200, 200));
		if (/* !this.onImgEdge&& */this.currPixelCount > this.firstPixelMin) {
			shpN = new TargetShape(new Point(sizeSquared, sizeSquared));
			// store the shape marked by the number 1 in shape
			shpN.setShape(canvas, xCor, yCor, 0);
			this.firstPassCount++;
			// shpN.shapeOut();
			return shpN;
		}
		// return the shape found
		return null;
		/*
		 * the number 4 has been changed -5 currently testing -5 check in
		 * Shape.class if work on this issue
		 */
	}

	/**
	 * returns a shape of all connected pixels matching in color that fit in double the searchWidth
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
	 *            place found shape is stored
	 * @return returns the shape if found and null if no shape found
	 */
	public TargetShape getShapeLeft(int searchWidth, Color colorOItem, int xCor, int yCor,
			TargetShape shpN) {
		if (xCor < 0 || yCor < 0) {// if the point has cordinates less than zero
									// or off screen in neg direction
			return null;// return null for no shape found
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
		 * find all connected matching pixels startingat the center of canvas
		 * and the point xCor,yCormarking the matching pixels as the number 1ont
		 * the canvas
		 */
		this.currPixelCount = 0;
		canvas = getMatchingPixelLeft(canvasBounds, colorOItem, new Point(xCor, yCor), canvasStart,
				canvas, (short) 0, new Color(200, 200, 200));
		if (/* !this.onImgEdge&& */this.currPixelCount > this.firstPixelMin) {
			shpN = new TargetShape(new Point(sizeSquared, sizeSquared));
			// store the shape marked by the number 1 in shape
			shpN.setShape(canvas, xCor, yCor, 0);
			this.firstPassCount++;
			return shpN;
		}
		// return the shape found
		return null;
		/*
		 * the number 4 has been changed -5 currently testing -5 check in
		 * Shape.class if work on this issue
		 */
	}

	public void exit() {

		// use the current time to mark files as unique file name
		// Date today=new Date();
		System.out.println("Minutes:Secs " + (((System.currentTimeMillis() - start) / 60000) / 60)
				+ ":" + ((System.currentTimeMillis() - start) / 1000));

		try {
			File dir1 = new File(".");
			System.out.println("Working directory is: " + dir1.getAbsolutePath());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Color averageColor(Color temp) {
		int red = ((this.colorAverage.getRed() * this.colorCount) + temp.getRed())
				/ (this.colorCount + 1);
		int green = ((this.colorAverage.getGreen() * this.colorCount) + temp.getGreen())
				/ (this.colorCount + 1);
		int blue = ((this.colorAverage.getBlue() * this.colorCount) + temp.getBlue())
				/ (this.colorCount + 1);
		this.colorCount++;
		return new Color(red, green, blue);
	}

	public boolean waitTillFileIsReady(String filename) {
		File fileToCopy = new File(filename);
		boolean fileReady = false;
		int sleepTime = 500; // Sleep 1 second
		while (!fileReady) {
			// Cannot write to file, windows still working on it
			// Sleep(sleepTime);
			try {
				Thread.sleep(sleepTime);
				FileReader test = new FileReader(fileToCopy);
				test.close();
				fileReady = true;
			} catch (Exception e) {
				System.out.println(e);
				waitTillFileIsReady(filename);
			}
		}
		return true;
	}

	public String getNewFiles(String path) {
		boolean foundFile = false;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String file = listOfFiles[i].getName();
				if (file.endsWith(".jpg") || file.endsWith(".JPG")) {
					if (imageQue.size() == 0) {
						imageQue.add(file);
						this.imageQue.add(file);
						return path + "/" + file;
					} else {
						foundFile = false;
						for (int j = 0; j < imageQue.size(); j++) {
							if (imageQue.get(j).equals(file)) {
								foundFile = true;
							}
						}
						if (!foundFile) {
							this.imageQue.add(file);
							this.waitTillFileIsReady(path + "/" + file);
							return path + "/" + file;
						}
					}
				}
			}
		}
		return null;
	}
}
