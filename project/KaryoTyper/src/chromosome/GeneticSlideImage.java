package chromosome;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import characterize.GrayBuffer;

import color.ISOClineColor;

import medial_axis.DistanceMap;
import medial_axis.MedialAxisGraph;
import basic_objects.Cluster;
import basic_objects.SearchArea;
import color.ISOClineColor;
import color.PixelColor;
import color.RainbowColors;

public class GeneticSlideImage {

	private String comments;
	private BufferedImage img;
	private int[] intensityHistogram;

	private int[] edgeHistogram;
	private LinkedList<Double> chromosomeWidth;
	private double chromoWidth;
	private int backgroundThreshold;
	private String imageName;
	private SearchArea searchArea;
/*
 * pixelFound is a memory versus time in get matching pixel spotNext stores a 2D array of what
 * pixels should be added to the ones to check next without searching the foundList of next ones
 * to be checked
 */
private boolean[][] pixelFound;

	public GeneticSlideImage(String filename) {
		intensityHistogram = new int[256];
		edgeHistogram = new int[256];
		img = null;
		imageName = filename;
		for (int i = 0; i < intensityHistogram.length; i++) {
			intensityHistogram[i] = 0;
		}
		try {
			if (filename.contains("imag")) {
				System.out.println();
				System.out.println();
				System.out
						.println("Starting file: " + filename.substring(filename.indexOf("imag")));
			}
			img = ImageIO.read(new File(filename));
			System.out.println("Image Height: " + img.getHeight() + " Width: " + img.getWidth());
		} catch (IOException e) {
			System.out.println(e);
		}
		chromoWidth = -1;
		chromosomeWidth = new LinkedList<Double>();
		searchArea=new SearchArea(this);
		this.computeHistogram();
		this.graphScale();
		// TODO(aamcknig): make this run on linear regressed function and not a static number
		this.backgroundThreshold = this.computeBackgroundThreshold();
	}

	public int getImgHeight() {
		return this.img.getHeight();
	}

	public int getImgWidth() {
		return this.img.getWidth();
	}

	public String getImageName() {
		return imageName;
	}

	public int getBackgroundThreshold() {
		return backgroundThreshold;
	}

	public void setBackgroundThreshold(int backgroundThreshold) {
		this.backgroundThreshold = backgroundThreshold;
	}

	public int[] getHistogram() {
		return intensityHistogram.clone();
	}


public GrayBuffer getSubImage(Cluster chromosomeCluster){
	GrayBuffer tempImg = new GrayBuffer(chromosomeCluster.getSize().x,
			chromosomeCluster.getSize().y);
	for (int i = chromosomeCluster.getImageLocation().x; i < (chromosomeCluster.getImageLocation().x + chromosomeCluster
			.getSize().x); i++) {
		for (int j = chromosomeCluster.getImageLocation().y; j < (chromosomeCluster.getImageLocation().y + chromosomeCluster
				.getSize().y); j++) {
			if (chromosomeCluster.getValue(i - chromosomeCluster.getImageLocation().x, j
					- chromosomeCluster.getImageLocation().y)){
				tempImg.set(i - chromosomeCluster.getImageLocation().x,
						j - chromosomeCluster.getImageLocation().y,
						PixelColor.colorToGrayscale(this.getColorAt(i, j)));
			}
			else {
				tempImg.set(i - chromosomeCluster.getImageLocation().x,
						j - chromosomeCluster.getImageLocation().y, -1);
			}
		}

	}
	return tempImg;
}

	/**
	 * This returns a bufferedImage of the square cluster area with the original image of the
	 * cluster inside the square and all points in the square area that are not part of the cluster
	 * are painted white and all points in pointList are painted the color drawColor if not null
	 * 
	 * @param targetCluster
	 *            the cluster to create a buffered image of
	 * @param pointList
	 *            the LinkedList of points to paint over the image if not null
	 * @param drawColor
	 *            the color to paint the points in point list if not null
	 * @return a buffered image of the cluster area with points painted over
	 */
	public BufferedImage getSubImage(Cluster targetCluster, LinkedList<Point> pointList,
			Color drawColor) {
		BufferedImage tempImg = new BufferedImage(targetCluster.getSize().x,
				targetCluster.getSize().y, BufferedImage.TYPE_3BYTE_BGR);
		for (int i = targetCluster.getImageLocation().x; i < (targetCluster.getImageLocation().x + targetCluster
				.getSize().x); i++) {
			for (int j = targetCluster.getImageLocation().y; j < (targetCluster.getImageLocation().y + targetCluster
					.getSize().y); j++) {
				if (targetCluster.getValue(i - targetCluster.getImageLocation().x, j
						- targetCluster.getImageLocation().y))
					tempImg.setRGB(i - targetCluster.getImageLocation().x,
							j - targetCluster.getImageLocation().y, img.getRGB(i, j));
				else {
					tempImg.setRGB(i - targetCluster.getImageLocation().x,
							j - targetCluster.getImageLocation().y,
							((Color.WHITE).getRGB() & Color.TRANSLUCENT));
				}
			}

		}

		if (pointList != null && !pointList.isEmpty()) {
			for (int i = 0; i < pointList.size(); i++) {
				if(pointList.get(i).x>=0&&pointList.get(i).y>=0
						&&pointList.get(i).x<targetCluster.getSize().x
						&&pointList.get(i).y<targetCluster.getSize().y){
					tempImg.setRGB(pointList.get(i).x, pointList.get(i).y, (drawColor).getRGB());
				}
			}
		}

		return tempImg;
	}

	/**
	 * this returns a bufferedImage of the square cluster area with the original image of the
	 * cluster inside the square and all points in the square area that are not part of the cluster
	 * are painted white and all points in pointList are painted the color drawColor if not null
	 * 
	 * @param targetCluster
	 *            the cluster to create a buffered image of
	 * @param pointList
	 *            the pointlist of points to paint over the image if not null
	 * @param drawColor
	 *            the color to paint the points in point list if not null
	 * @return a buffered image of the cluster area with points painted over
	 */
	public BufferedImage getSubImage(Cluster targetCluster, MedialAxisGraph graphList) {
		BufferedImage tempImg = new BufferedImage(targetCluster.getSize().x,
				targetCluster.getSize().y, BufferedImage.TYPE_3BYTE_BGR);
		for (int i = targetCluster.getImageLocation().x; i < (targetCluster.getImageLocation().x + targetCluster
				.getSize().x); i++) {
			for (int j = targetCluster.getImageLocation().y; j < (targetCluster.getImageLocation().y + targetCluster
					.getSize().y); j++) {
				if (targetCluster.getValue(i - targetCluster.getImageLocation().x, j
						- targetCluster.getImageLocation().y))
					tempImg.setRGB(i - targetCluster.getImageLocation().x,
							j - targetCluster.getImageLocation().y, img.getRGB(i, j));
				else {
					tempImg.setRGB(i - targetCluster.getImageLocation().x,
							j - targetCluster.getImageLocation().y, ((Color.WHITE).getRGB() & Color.TRANSLUCENT));
				}
			}

		}

		if (graphList != null && !graphList.getAxisGraph().isEmpty()) {
			for (int i = 0; i < graphList.getAxisGraph().size(); i++) {
				tempImg.setRGB(graphList.getAxisGraph().get(i).getPoint().x, graphList
						.getAxisGraph().get(i).getPoint().y,
						RainbowColors.tasteRainbow(graphList.getAxisGraph().get(i).getMySegement())
								.getRGB());
			}
		}

		return tempImg;
	}

	/**
	 * this returns a bufferedImage of the square cluster area with the original image of the
	 * cluster inside the square and all points in the square area that are not part of the cluster
	 * are painted white and all points in pointList are painted the color drawColor if not null
	 * 
	 * @param targetCluster
	 *            the cluster to create a buffered image of
	 * @param pointList
	 *            the pointlist of points to paint over the image if not null
	 * @param drawColor
	 *            the color to paint the points in point list if not null
	 * @return a buffered image of the cluster area with points painted over
	 */
	public BufferedImage getSubImage(Cluster targetCluster, LinkedList<Point> pointList,
			Color drawColor, LinkedList<Point> pointList2, Color drawColor2) {
		BufferedImage tempImg = new BufferedImage(targetCluster.getSize().x,
				targetCluster.getSize().y, BufferedImage.TYPE_3BYTE_BGR);
		for (int i = targetCluster.getImageLocation().x; i < (targetCluster.getImageLocation().x + targetCluster
				.getSize().x); i++) {
			for (int j = targetCluster.getImageLocation().y; j < (targetCluster.getImageLocation().y + targetCluster
					.getSize().y); j++) {
				if (targetCluster.getValue(i - targetCluster.getImageLocation().x, j
						- targetCluster.getImageLocation().y))
					tempImg.setRGB(i - targetCluster.getImageLocation().x,
							j - targetCluster.getImageLocation().y, img.getRGB(i, j));
				else {
					tempImg.setRGB(i - targetCluster.getImageLocation().x,
							j - targetCluster.getImageLocation().y, ((Color.WHITE).getRGB() & Color.TRANSLUCENT));
				}
			}

		}

		if (pointList != null && !pointList.isEmpty()) {
			for (int i = 0; i < pointList.size(); i++) {
				tempImg.setRGB(pointList.get(i).x, pointList.get(i).y, (drawColor).getRGB());
			}
		}
		if (pointList2 != null && !pointList2.isEmpty()) {
			for (int i = 0; i < pointList2.size(); i++) {
				tempImg.setRGB(pointList2.get(i).x, pointList2.get(i).y, (drawColor2).getRGB());
			}
		}

		return tempImg;
	}

	/**
	 * This returns a graphical representation of a distance map as a BufferedImage
	 * 
	 * @param distanceMap
	 *            the distance map to be represented
	 * @return a BufferedImage representing the distance map
	 */
	public BufferedImage getISOcline(DistanceMap distanceMap) {
		BufferedImage tempImg = new BufferedImage(distanceMap.getWidth(), distanceMap.getHeight(),
				BufferedImage.TYPE_3BYTE_BGR);
		for (int i = 0; i < distanceMap.getHeight(); i++) {
			for (int j = 0; j < distanceMap.getWidth(); j++) {
				if (distanceMap.getDistanceFromEdge(new Point(j, i)) < 0) {
					tempImg.setRGB(j, i, Color.BLUE.getRGB());
				} else {
					Color temp = ISOClineColor.getColor(distanceMap.getDistanceFromEdge(new Point(
							j, i)));
					tempImg.setRGB(j, i, temp.getRGB());
				}
			}

		}

		return tempImg;
	}

	public String getMetaData() {
		return this.comments;
	}

	public Color getColorAt(int x, int y) {
		return this.convertPixel(img.getRGB(x, y));
	}

	public void addWidth(double width) {
		this.calcNewAvg(width);
		this.chromosomeWidth.add(width);
	}

	public double calcFinalAverage() {
		this.recalcAvgWidth();
		return this.chromoWidth;
	}

	public double getChromoWidth() {
		return this.chromoWidth;
	}

	/**
	 * Required to convert the number representing a color in a JPEG image to a number that can be
	 * stored as java Color object
	 * 
	 * @param pixel
	 *            the number of representing the RGB value of a pixel in a JPEG image
	 * @return a java Color object that represents the color of the jpeg's pixel
	 */
	public Color convertPixel(int pixel) {
		int red = (pixel & 0x00ff0000) >> 16;
		int green = (pixel & 0x0000ff00) >> 8;
		int blue = pixel & 0x000000ff;
		return new Color(red, green, blue);
	}

	/**
	 * This creates a histogram of the grayscale of colors in the image called in the constructor
	 */
	private void computeHistogram() {
		// TODO(ahkeslin): Pull out magic numbers for grayscale conversion into something more
		// understandable.
		Color tempColor = new Color(0, 0, 0);
		for (int i = 0; i < this.getImgWidth(); i++) {
			for (int j = 0; j < this.getImgHeight(); j++) {
				tempColor = this.getColorAt(i, j);
				double tempGreyPixel = (.299 * tempColor.getRed()) + (.587 * tempColor.getGreen())
						+ (.114 * tempColor.getBlue());
				this.intensityHistogram[(int) Math.round(tempGreyPixel)]++;
			}
		}
	}

	/**
	 * This uses the image's histogram to determine up to what threshold to extend the background
	 * color.
	 * 
	 * @return The threshold value at which to separate background from foreground.
	 */
	private int computeBackgroundThreshold() {
		// For a good frame of reference... a threshold 245 was determined by manual analysis of an
		// image histogram

		// Sliding integral approach:
		// The following code sums a sliding interval and keeps track of the largest left index
		int maxSum = 0;
		int sumIndex = 0;
		int intervalSize = 10;

		for (int x = this.intensityHistogram.length - 1; x >= 0; x--) {
			int sum = 0;
			for (int dx = 0; dx < intervalSize && dx + x < this.intensityHistogram.length; dx++) {
				sum += this.intensityHistogram[x + dx];
			}
			if (sum >= maxSum) {
				maxSum = sum;
				sumIndex = x;
			}
		}

		return sumIndex;
	}

	/**
	 * This appends to a comma separated file of values of the grayscale found in each image, called
	 * in constructor
	 */
	private void graphScale() {
		try {
			// Create file
			FileWriter fstream = new FileWriter("GrayScale.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			String buffer = "";
			for (int i = 0; i < this.intensityHistogram.length; i++) {// out.write("Hello Java");
				buffer += "" + this.intensityHistogram[i] + ",";
			}
			out.write(buffer);
			out.write("\n");

			// Close the output stream
			out.close();
			// Catch exception if any
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	/**
	 * This is for testing, outputs the average width of chromosomes in the current image
	 */
	public void writeChromosomesWidth() {
		System.out.print("Widths for this image: ");
		for (int i = 0; i < this.chromosomeWidth.size(); i++) {
			System.out.print(this.chromosomeWidth.get(i) + ",");
		}
	}

	/**
	 * Averages in a new value to get the average width of chromosomes in this image
	 * 
	 * @param newWidth
	 *            the new width to average in
	 */
	public void calcNewAvg(double newWidth) {
		if (this.chromosomeWidth.isEmpty()) {
			this.chromoWidth = newWidth;
		} else {
			this.chromoWidth = (((this.chromoWidth * this.chromosomeWidth.size()) + newWidth) / (this.chromosomeWidth
					.size() + 1));
		}
	}

	/**
	 * This removes outliers and recalculates the average width of chromosomes in this image
	 */
	public void recalcAvgWidth() {
		double temp = -1;
		LinkedList<Double> goodWidths = new LinkedList<Double>();
		if (this.chromosomeWidth.size() > 4) {
			for (int i = 0; i < this.chromosomeWidth.size(); i++) {
				if (Math.abs(this.chromoWidth - ((double) this.chromosomeWidth.get(i))) < 3) {
					goodWidths.add(this.chromosomeWidth.get(i));
				}
			}

			for (int i = 0; i < goodWidths.size(); i++) {
				temp = temp + goodWidths.get(i);
			}
			this.chromoWidth = temp / goodWidths.size();
		}
		System.out.println("AverageWidth: " + this.chromoWidth);
	}

	/**
	 * This creates a histogram of the grayscale of colors from around the edge of chromosomes
	 */
	public void computeEdgeHistogram(LinkedList<Point> edgePoints) {
		Color tempColor = new Color(0, 0, 0);
		for (int i = 100; i < edgePoints.size(); i++) {
			tempColor = this.getColorAt(edgePoints.get(i).x, edgePoints.get(i).y);
			double tempGreyPixel = (.299 * tempColor.getRed()) + (.587 * tempColor.getGreen())
					+ (.114 * tempColor.getBlue());
			this.edgeHistogram[(int) Math.round(tempGreyPixel)]++;
		}
	}

	/**
	 * This appends to a comma separated file of values of the grayscale found in each image, called
	 * in constructor
	 */
	public void graphEdgeHistogram() {
		try {
			FileWriter fstream = new FileWriter("EdgeScale.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			String buffer = "";
			for (int i = 0; i < this.edgeHistogram.length; i++) {
				buffer += "" + this.edgeHistogram[i] + ",";
			}
			out.write(buffer);
			out.write("\n");

			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	public SearchArea getSearchArea() {
		return searchArea;
	}

	public void setSearchArea(SearchArea searchArea) {
		this.searchArea = searchArea;
	}


}
