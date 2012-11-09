package extraction;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import chromosome.ChromosomeCluster;
import chromosome.GeneticSlideImage;

import extraction.Extractor;

import basic_objects.SearchArea;
import junit.framework.TestCase;

public class ExtractorTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	public SearchArea cutInHalf(SearchArea searchArea){
		int middle=(int)Math.round(searchArea.getHeight()/2);
		for(int i=0;i<searchArea.getWidth();i++){
			searchArea.markPixelChecked(new Point(i,middle));
		}
		
		return searchArea;
	}

	public void testGetMatchingPixelLeft(){
		SearchArea searchArea=new SearchArea(10, 10);
		searchArea=cutInHalf(searchArea);
		int middle=(int)Math.round(searchArea.getHeight()/2);
		Rectangle bounds=new Rectangle(0,0,20,20);
		short[][] canvas=new short[20][20];
		for(int j=0;j<canvas.length;j++){
			for(int i=0;i<canvas[0].length;i++){
				canvas[j][i]=-5;
			}
		}
		Point currentPoint=new Point(3,3);
		Point canvasOrigin=new Point(10,10);
		Rectangle findArea=new Rectangle(10-3,10-3,10,middle);
		short clusterID=0;
		canvas[10][10]=clusterID;
		Extractor extract=new Extractor();
		canvas=extract.getMatchingPixelLeft(searchArea,bounds,
				currentPoint, canvasOrigin,canvas,clusterID);
		for(int i=0;i<canvas.length;i++){
			for(int j=0;j<canvas[0].length;j++){
				if(findArea.contains(new Point(i,j))){
					assertEquals(canvas[i][j],clusterID);
				}
				else{
					assertEquals(canvas[i][j],-5);
				}
			}
		}
		//try diagnal connection true
		searchArea=new SearchArea(10, 10);
		searchArea.markPixelChecked(new Point(2,3));
		searchArea.markPixelChecked(new Point(3,2));
		searchArea.markPixelChecked(new Point(4,3));
		searchArea.markPixelChecked(new Point(3,4));
		
		bounds=new Rectangle(0,0,20,20);
		canvas=new short[20][20];
		for(int j=0;j<canvas.length;j++){
			for(int i=0;i<canvas[0].length;i++){
				canvas[j][i]=-5;
			}
		}
		currentPoint=new Point(3,3);
		canvasOrigin=new Point(10,10);
		Point canvasOffset=new Point(currentPoint.x-canvasOrigin.x,currentPoint.y-canvasOrigin.y);
		findArea=new Rectangle(10-3,10-3,10,10);
		clusterID=0;
		canvas[10][10]=clusterID;
		extract=new Extractor();
		canvas=extract.getMatchingPixelLeft(searchArea,bounds,
				currentPoint, canvasOrigin,canvas,clusterID);
		for(int i=0;i<canvas.length;i++){
			for(int j=0;j<canvas[0].length;j++){
				if(findArea.contains(new Point(i,j))){
					if((new Point(i+canvasOffset.x,j+canvasOffset.y)).equals(new Point(2,3))
							||(new Point(i+canvasOffset.x,j+canvasOffset.y)).equals(new Point(3,2))
							||(new Point(i+canvasOffset.x,j+canvasOffset.y)).equals(new Point(4,3))
							||(new Point(i+canvasOffset.x,j+canvasOffset.y)).equals(new Point(3,4))){
						assertEquals(canvas[i][j],-5);
					}
					else{
						assertEquals(canvas[i][j],clusterID);
					}
				}
				else{
					assertEquals(canvas[i][j],-5);
				}
			}
		}
		//surrounded dot no growth
		searchArea=new SearchArea(10, 10);
		searchArea.markPixelChecked(new Point(2,3));
		searchArea.markPixelChecked(new Point(3,2));
		searchArea.markPixelChecked(new Point(4,3));
		searchArea.markPixelChecked(new Point(3,4));
		searchArea.markPixelChecked(new Point(4,4));
		searchArea.markPixelChecked(new Point(2,2));
		searchArea.markPixelChecked(new Point(4,2));
		searchArea.markPixelChecked(new Point(2,4));
		
		bounds=new Rectangle(0,0,20,20);
		canvas=new short[20][20];
		for(int j=0;j<canvas.length;j++){
			for(int i=0;i<canvas[0].length;i++){
				canvas[j][i]=-5;
			}
		}
		currentPoint=new Point(3,3);
		canvasOrigin=new Point(10,10);
		findArea=new Rectangle(10-3,10-3,10,10);
		clusterID=0;
		canvas[10][10]=clusterID;
		extract=new Extractor();
		canvas=extract.getMatchingPixelLeft(searchArea,bounds,
				currentPoint, canvasOrigin,canvas,clusterID);
		for(int i=0;i<canvas.length;i++){
			for(int j=0;j<canvas[0].length;j++){
				if((new Point(i,j)).equals(new Point(10,10))){
					assertEquals(canvas[i][j],clusterID);
				}
				else{
					assertEquals(canvas[i][j],-5);
				}
			}
		}

	}
	public void testGetMatchingPixel(){
		BufferedImage tempImg = new BufferedImage(10,10, BufferedImage.TYPE_3BYTE_BGR);
		for(int j=0;j<tempImg.getWidth();j++){
			for(int i=0;i<tempImg.getHeight();i++){
				if(j<(int)Math.round(tempImg.getHeight()/2)){
					tempImg.setRGB(i, j, Color.WHITE.getRGB());
				}
				else{
					tempImg.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}
		GeneticSlideImage img=new GeneticSlideImage(tempImg);
		boolean aboveThreshold =true;
		int threshold=200;
		SearchArea searchArea=new SearchArea(10, 10);
		int middle=(int)Math.round(searchArea.getHeight()/2);
		Rectangle bounds=new Rectangle(0,0,20,20);
		short[][] canvas=new short[20][20];
		for(int j=0;j<canvas.length;j++){
			for(int i=0;i<canvas[0].length;i++){
				canvas[j][i]=-5;
			}
		}
		Point searchAreaPoint=new Point(3,3);
		Point currentPoint=new Point(3,3);
		Point canvasOrigin=new Point(10,10);
		Rectangle findArea=new Rectangle(10-3,10-3,10,middle);
		short clusterID=0;
		canvas[10][10]=clusterID;
		Extractor extract=new Extractor();
		canvas=extract.getMatchingPixel(searchArea,img,bounds,
				currentPoint,searchAreaPoint, canvasOrigin,canvas,clusterID,aboveThreshold,threshold);
		for(int i=0;i<canvas.length;i++){
			for(int j=0;j<canvas[0].length;j++){
				if(findArea.contains(new Point(i,j))){
					assertEquals(canvas[i][j],clusterID);
				}
				else{
					assertEquals(canvas[i][j],-5);
				}
			}
		}
		searchAreaPoint=new Point(8,8);
		currentPoint=new Point(8,8);
		canvasOrigin=new Point(10,10);
		findArea=new Rectangle(10-8,10-8+middle,10,middle);
		searchArea=new SearchArea(10, 10);
		canvas=new short[20][20];
		for(int j=0;j<canvas.length;j++){
			for(int i=0;i<canvas[0].length;i++){
				canvas[j][i]=-5;
			}
		}
		canvas=extract.getMatchingPixel(searchArea,img,bounds,
				currentPoint,searchAreaPoint, canvasOrigin,canvas,clusterID,false,threshold);
		for(int i=0;i<canvas.length;i++){
			for(int j=0;j<canvas[0].length;j++){
				if(findArea.contains(new Point(i,j))){
					assertEquals(canvas[i][j],clusterID);
				}
				else{
					assertEquals(canvas[i][j],-5);
				}
			}
		}
		searchAreaPoint=new Point(2,2);
		currentPoint=new Point(8,8);
		canvasOrigin=new Point(10,10);
		findArea=new Rectangle(8,8,4,4);
		searchArea=new SearchArea(4, 4);
		canvas=new short[20][20];
		for(int j=0;j<canvas.length;j++){
			for(int i=0;i<canvas[0].length;i++){
				canvas[j][i]=-5;
			}
		}
		canvas=extract.getMatchingPixel(searchArea,img,bounds,
				currentPoint,searchAreaPoint, canvasOrigin,canvas,clusterID,false,threshold);
		for(int i=0;i<canvas.length;i++){
			for(int j=0;j<canvas[0].length;j++){
				if(findArea.contains(new Point(i,j))){
					assertEquals(canvas[i][j],clusterID);
				}
				else{
					assertEquals(canvas[i][j],-5);
				}
			}
		}


	}
	public void testRemoveBackground(){
		BufferedImage tempImg = new BufferedImage(10,10, BufferedImage.TYPE_3BYTE_BGR);
		for(int j=0;j<tempImg.getWidth();j++){
			for(int i=0;i<tempImg.getHeight();i++){
				if(j<(int)Math.round(tempImg.getHeight()/2)){
					tempImg.setRGB(i, j, Color.WHITE.getRGB());
				}
				else{
					tempImg.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}
		GeneticSlideImage img=new GeneticSlideImage(tempImg);
		Extractor extract=new Extractor();
		extract.removeBackground(img);
		for(int i=0;i<img.getSearchArea().getWidth();i++){
			for(int j=0;j<img.getSearchArea().getHeight();j++){
				if(j<Math.round(img.getImgHeight()/2)){
					assertTrue(img.getSearchArea().isPixelChecked(new Point(i,j)));
				}
				else{
					assertFalse(img.getSearchArea().isPixelChecked(new Point(i,j)));
				}
			}
		}


	}
	public void testFindClusters(){
		BufferedImage tempImg = new BufferedImage(10,10, BufferedImage.TYPE_3BYTE_BGR);
		for(int j=0;j<tempImg.getWidth();j++){
			for(int i=0;i<tempImg.getHeight();i++){
				if(j<(int)Math.round(tempImg.getHeight()/2)){
					tempImg.setRGB(i, j, Color.WHITE.getRGB());
				}
				else{
					tempImg.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}
		GeneticSlideImage img=new GeneticSlideImage(tempImg);
		Extractor extract=new Extractor();
		extract.removeBackground(img);
		int clusterCount=extract.findClusters(img);
		assertEquals(1,clusterCount);
		for(int i=0;i<img.getSearchArea().getWidth();i++){
			for(int j=0;j<img.getSearchArea().getHeight();j++){
					assertTrue(img.getSearchArea().isPixelChecked(new Point(i,j)));
			}
		}
		LinkedList<ChromosomeCluster> tempList=extract.getClusterList();
		assertEquals(1,tempList.size());
		assertEquals(50,tempList.get(0).getPixelCount());
		assertEquals(new Point(0,5),tempList.get(0).getImageLocation());
		assertEquals(new Point(10,5),tempList.get(0).getSize());
		
		//keeper meets chromosome early checks
		tempImg = new BufferedImage(150,40, BufferedImage.TYPE_3BYTE_BGR);
		for(int j=0;j<tempImg.getWidth();j++){
			for(int i=0;i<tempImg.getHeight();i++){
				if(j>0&&j<tempImg.getWidth()-1&&i>3&&i<(int)Math.round(tempImg.getHeight()/2)){
					tempImg.setRGB(j, i, Color.BLACK.getRGB());
				}
				else{
					tempImg.setRGB(j, i, Color.WHITE.getRGB());
				}
			}
		}

		img=new GeneticSlideImage(tempImg);
		extract=new Extractor();
		extract.removeBackground(img);
		clusterCount=extract.findClusters(img);
		assertEquals(1,clusterCount);
		for(int i=0;i<img.getSearchArea().getWidth();i++){
			for(int j=0;j<img.getSearchArea().getHeight();j++){
					assertTrue(img.getSearchArea().isPixelChecked(new Point(i,j)));
			}
		}
		tempList=extract.getClusterList();
		//TODO(aamcknig): find out why there is an extra part of the image
		assertEquals(1,tempList.size());
		assertEquals(2368,tempList.get(0).getPixelCount());
		assertEquals(new Point(1,4),tempList.get(0).getImageLocation());
		assertEquals(new Point(148,16),tempList.get(0).getSize());
		assertTrue(tempList.get(0).checkKeepThisCluster());
		
		
		tempImg = new BufferedImage(10,10, BufferedImage.TYPE_3BYTE_BGR);
		for(int j=0;j<tempImg.getWidth();j++){
			for(int i=0;i<tempImg.getHeight();i++){
				if(j<(int)Math.round(tempImg.getHeight()/2)){
					tempImg.setRGB(i, j, Color.WHITE.getRGB());
				}
				else{
					tempImg.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}
		img=new GeneticSlideImage(tempImg);
		try{
			File curDir = new File(".");
			File outputfile = new File(curDir.getCanonicalPath() + "/shapeData/"+"test2.png");
			ImageIO.write(tempImg, "png", outputfile);
		}catch(Exception e){
			System.out.println(e);
		}

		extract=new Extractor();
		extract.removeBackground(img);
		clusterCount=extract.findClusters(img);
		tempList=extract.getClusterList();
		assertEquals(new Point(0,5),tempList.get(0).getImageLocation());


	}

		
}
