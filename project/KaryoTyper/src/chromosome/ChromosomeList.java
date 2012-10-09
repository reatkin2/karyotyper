package chromosome;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import medial_axis.MedialAxisGraph;

public class ChromosomeList {
	private LinkedList<ChromosomeCluster> chromosomeList;
	private GeneticSlideImage img;

	public ChromosomeList(LinkedList<ChromosomeCluster> newList, GeneticSlideImage newImg) {
		this.chromosomeList = newList;
		img = newImg;
	}

	public void calcMedialAxis(GeneticSlideImage image){
		for (int i = 0; i < this.chromosomeList.size(); i++) {
			this.chromosomeList.get(i).createMedialAxisGraph(image);
		}
	}
	public int size(){
		return chromosomeList.size();
	}
	/**
	 * goes through the list chromosomelist and calls either the method to write chromosome images
	 * to the keep folder or to the removed folder
	 */
	public void printChromosomes(GeneticSlideImage image) {
		for (int i = 0; i < this.chromosomeList.size(); i++) {
			boolean goodChromosome=false;
			ChromosomeCluster tempCluster = this.chromosomeList.get(i);
//			img.computeEdgeScale(tempCluster.getMedialAxisGraph().getMedialAxis().getDistanceMap().getTheEdge(0));
			if(0==tempCluster.getMedialAxisGraph().getIntersectionCount(tempCluster.getMedialAxisGraph().getAxisGraph())){
				goodChromosome=true;
			}
			if(tempCluster.checkKeepThisCluster()&&goodChromosome){
				writeRainbowImage("/shapeData/Chromosome/",tempCluster, tempCluster.getMedialAxisGraph());
//				writeTargetImage("/shapeData/Chromosome/",tempCluster, tempCluster.getMedialAxisGraph().getMedialAxis().getMedialAxisPoints(),
//						new Color(255, 0, 0));
			}
			else if (tempCluster.checkKeepThisCluster()) {
				writeRainbowImage("/shapeData/Keep/",tempCluster, tempCluster.getMedialAxisGraph());
//				writeTargetImage(tempCluster, tempCluster.getMedialAxisGraph().getMedialAxis().getMedialAxisPoints(),
//						new Color(255, 0, 0),tempCluster.getMedialAxisGraph().getMedialAxis().
//						getPossibleBreaks((int)Math.round((image.getAverage()/2)-4)),new Color(255,0,255));
			} else {
				writeTargetImage("/shapeData/Removed/",tempCluster,null,null);
			}
		}
		this.chromosomeList = new LinkedList<ChromosomeCluster>();
		img.graphEdgeScale();
	}

	/**
	 * Writes chromosome images to the keep folder and if not null writes the linkedlist of points
	 * in color specified
	 * 
	 * @param tempCluster
	 *            the cluster to printOut as the part of the original image that is the cluster
	 * @param colorPoints
	 *            the points to paint over the chromosome the color paintColor
	 * @param paintColor
	 *            the color to paint the Points in colorPoints
	 */
	public void writeTargetImage(String path,ChromosomeCluster tempCluster, LinkedList<Point> colorPoints,
			Color paintColor) {
		try {
			File curDir = new File(".");
			String imageName = new File(tempCluster.getTitle()).getName();
			File outputfile = new File(curDir.getCanonicalPath() + path
					+ imageName.substring(0, imageName.indexOf('.')) + "_"
					+ (tempCluster.getClusterNimageID()) + ".png");
			BufferedImage tempImg = img.getSubImage(tempCluster, colorPoints, paintColor);// ,targetImgBorderSize);//30pixel

			ImageIO.write(tempImg, "png", outputfile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	/**
	 * Writes chromosome images to the keep folder and if not null writes the linkedlist of points
	 * in color specified
	 * 
	 * @param tempCluster
	 *            the cluster to printOut as the part of the original image that is the cluster
	 * @param colorPoints
	 *            the points to paint over the chromosome the color paintColor
	 * @param paintColor
	 *            the color to paint the Points in colorPoints
	 */
	public void writeRainbowImage(String path,ChromosomeCluster tempCluster, MedialAxisGraph graph) {
		try {
			File curDir = new File(".");
			String imageName = new File(tempCluster.getTitle()).getName();
			File outputfile = new File(curDir.getCanonicalPath() + path
					+ imageName.substring(0, imageName.indexOf('.')) + "_"
					+ (tempCluster.getClusterNimageID()) + ".png");
			BufferedImage tempImg = img.getSubImage(tempCluster, graph);// ,targetImgBorderSize);//30pixel

			ImageIO.write(tempImg, "png", outputfile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Writes  images to the keep folder and if not null writes the linkedlist of points
	 * in color specified
	 * 
	 * @param tempCluster
	 *            the cluster to printOut as the part of the original image that is the cluster
	 * @param colorPoints
	 *            the points to paint over the chromosome the color paintColor
	 * @param paintColor
	 *            the color to paint the Points in colorPoints
	 */
	public void writeTargetImage(String path,ChromosomeCluster tempCluster, LinkedList<Point> colorPoints,
			Color paintColor,LinkedList<Point> colorPoints2,Color paintColor2) {
		try {
			File curDir = new File(".");
			String imageName = new File(tempCluster.getTitle()).getName();
			File outputfile = new File(curDir.getCanonicalPath() + path
					+ imageName.substring(0, imageName.indexOf('.')) + "_"
					+ (tempCluster.getClusterNimageID()) + ".png");
			BufferedImage tempImg = img.getSubImage(tempCluster, colorPoints, paintColor,colorPoints2,paintColor2);// ,targetImgBorderSize);//30pixel
																							// border
			ImageIO.write(tempImg, "png", outputfile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * this prints out a image that is the graphical representation of the distance map that is in
	 * tempCluster
	 * 
	 * @param tempCluster
	 *            the cluster that has the distance map to be printed
	 */
	public void writeISOClineImage(ChromosomeCluster tempCluster) {
		try {
			File curDir = new File(".");
			String imageName = new File(tempCluster.getTitle()).getName();
			File outputfile = new File(curDir.getCanonicalPath() + "/shapeData/Keep/"
					+ imageName.substring(0, imageName.indexOf('.')) + "_"
					+ (tempCluster.getClusterNimageID()) + "ISO" + ".png");// ,tempShape.getTitle().indexOf(".jpg"))+"_"+(inImageTargetCount)+".png"
			BufferedImage tempImg = img.getISOcline(tempCluster.getMedialAxisGraph().getMedialAxis().getDistanceMap());// ,targetImgBorderSize);//30pixel
																									// border
			ImageIO.write(tempImg, "png", outputfile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}
