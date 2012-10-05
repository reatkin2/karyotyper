package chromosome;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import MedialAxis.MedialAxisGraph;

public class ChromosomeList {
	private LinkedList<ChromosomeCluster> chromosomeList;
	private GeneticSlideImage img;
	
	public ChromosomeList(LinkedList<ChromosomeCluster> newList,GeneticSlideImage newImg){
		this.chromosomeList=newList;
		img=newImg;
	}

    /**
     * goes through the list chromosomelist and calls either
     * the method to write chromosome images to the keep folder or
     * to the removed folder
     */
    public void printChromosomes(){
		for(int i=0;i<this.chromosomeList.size();i++){
			ChromosomeCluster tempCluster=this.chromosomeList.get(i);
			MedialAxisGraph tempGraph2=new MedialAxisGraph(tempCluster.getMedialAxis().getMedialAxisPoints());
			tempCluster.getMedialAxis().fillInSkeleton(tempCluster,tempGraph2);
//			tempGraph2.removeSegments((int)Math.round((img.getAverage()/2)), -1);
//			tempShape.setMedialAxis(tempGraph2.getMedialAxis());
//			tempShape.fillInSkeleton(tempGraph2);
			//TODO(aamcknig):remove segments only if they don't have interesctions at both ends
				if(tempCluster.checkKeepThisCluster()){
					writeTargetImage(tempCluster,tempCluster.getMedialAxis().getMedialAxisPoints(),new Color(255,0,0));
				}
				else{
					writeRemovedImage(tempCluster);
				}
		}
		this.chromosomeList=new LinkedList<ChromosomeCluster>();
}
    
/**
 * Writes chromosome images to the keep folder and if not null writes the linkedlist
 * of points in color specified
 * @param tempCluster the cluster to printOut as the part of the original image that is the cluster
 * @param colorPoints the points to paint over the chromosome the color paintColor
 * @param paintColor the color to paint the Points in colorPoints
 */
public void writeTargetImage(ChromosomeCluster tempCluster,LinkedList<Point> colorPoints,Color paintColor){
	try {
		File curDir=new File(".");
		String imageName=new File(tempCluster.getTitle()).getName();
		File outputfile = new File(curDir.getCanonicalPath()+"/shapeData/Keep/"+imageName.substring(0,imageName.indexOf('.'))+"_"+(tempCluster.getClusterNimageID())+".png");
		BufferedImage tempImg=img.getSubImage(tempCluster,colorPoints,paintColor);//,targetImgBorderSize);//30pixel border
	    ImageIO.write(tempImg, "png", outputfile);
	} catch (IOException e) {
	    System.out.println(e);
	}
}

/**
 * this prints out a image that is the graphical representation of the distance map
 *  that is in tempCluster
 * @param tempCluster the cluster that has the distancemap to be printed
 */
public void writeISOClineImage(ChromosomeCluster tempCluster){
	try {
		File curDir=new File(".");
		String imageName=new File(tempCluster.getTitle()).getName();
		File outputfile = new File(curDir.getCanonicalPath()+"/shapeData/Keep/"+imageName.substring(0,imageName.indexOf('.'))+"_"+(tempCluster.getClusterNimageID())+"ISO"+".png");//,tempShape.getTitle().indexOf(".jpg"))+"_"+(inImageTargetCount)+".png"
		BufferedImage tempImg=img.getISOcline(tempCluster.getMedialAxis().getDistanceMap());//,targetImgBorderSize);//30pixel border
	    ImageIO.write(tempImg, "png", outputfile);
	} catch (IOException e) {
	    System.out.println(e);
	}

}

/**
 * this rights the part of the image that is the cluster tempCluster to the 
 * removed folder
 * @param tempCluster the cluster to write to the removed folder
 */
public void writeRemovedImage(ChromosomeCluster tempCluster){
	try {
		File curDir=new File(".");
		String imageName=new File(tempCluster.getTitle()).getName();
		File outputfile = new File(curDir.getCanonicalPath()+"/shapeData/Removed/"+imageName.substring(0,imageName.indexOf('.'))+"_"+(tempCluster.getClusterNimageID())+".png");//,tempShape.getTitle().indexOf(".jpg"))+"_"+(inImageTargetCount)+".png"
		BufferedImage tempImg=img.getSubImage(tempCluster,null,null);//,targetImgBorderSize);//30pixel border
	    ImageIO.write(tempImg, "png", outputfile);
	} catch (IOException e) {
		System.out.println(e);
	}

}
}
