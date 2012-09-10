package Target;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;


import basicObjects.LatLongPoint;
import basicObjects.Shape;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;


public class TargetImage {

    private String comments;
    private	BufferedImage img;
    private double aboveGroundLevel;//meters
	private double aboveGroundLevelFeet;//feet
    private int pixelCountAGL;
    private double pixelsPerFoot;
    private double imgLat;
    private double imgLong;
    private double feetPerDegreeLatLong;//=364169.55420532933;//at lat 38.14;//at lat 38.14
    private double heading;
    private int hours;
    private int mins;
    private int secs;
    private double lensFieldOfView = 70.7508;
    private double []grayScale=new double[256];
    private String filenameX;
	public TargetImage(String filename,double feetPerDegreeLatLongX){
		img = null;
		this.filenameX=filename;
		for(int i=0;i<grayScale.length;i++){
			grayScale[i]=0;
		}
		this.feetPerDegreeLatLong=feetPerDegreeLatLongX;
		try {
			//image metadata reader
//	        File withExif = new File(filename);
//	        try{
//	        	Metadata metadata = JpegMetadataReader.readMetadata(withExif);
	        	//Assert.assertTrue(metadata.containsDirectory(ExifSubIFDDirectory.class));
//	        	Directory directory = metadata.getOrCreateDirectory(ExifSubIFDDirectory.class);
//	        	this.comments=directory.getDescription(ExifSubIFDDirectory.TAG_USER_COMMENT);
	        	//this.imageBrightness=directory.getString(ExifSubIFDDirectory.TAG_BRIGHTNESS_VALUE);
	        	if(filename.contains("imag")){
	        		System.out.println();
	        		System.out.println();
	        		System.out.println("Starting file: "+filename.substring(filename.indexOf("imag")));
	        	}
//	        	String tempSub=this.comments.substring(comments.indexOf("AGL=")+4, comments.indexOf(",R="));
//	        	//String tempSub=this.comments.substring(comments.indexOf("A=")+2, comments.indexOf(",AGL="));
//	        	String tempLat=this.comments.substring(comments.indexOf("N=")+2, comments.indexOf(",W="));
//	        	String tempLong=this.comments.substring(comments.indexOf("W=")+2, comments.indexOf(",GPSALT="));
//	        	String tempHeading=this.comments.substring(comments.indexOf("Y=")+2, comments.indexOf(",GR="));
//	        	String tempTime=this.comments.substring(comments.indexOf("GPSTIME=")+8, comments.indexOf(",PTIME="));
//	        	this.hours=Integer.parseInt(tempTime.substring(0,tempTime.indexOf(".")));
//	        	tempTime=tempTime.substring(tempTime.indexOf(".")+1);
//	        	this.mins=Integer.parseInt(tempTime.substring(0,tempTime.indexOf(".")));
//	        	tempTime=tempTime.substring(tempTime.indexOf(".")+1);
//	        	this.secs=Integer.parseInt(tempTime);
//	        	this.aboveGroundLevel=(Double.parseDouble(tempSub));//in meters
//	        	this.imgLat=(Double.parseDouble(tempLat));
//	        	this.imgLong=(Double.parseDouble(tempLong));
//	        	this.heading=(Double.parseDouble(tempHeading));
//	        	//System.out.println("AGL: "+this.aboveGroundLevel);
//	        	this.pixelCountAGL=(int)Math.round(((this.aboveGroundLevel*-18.14736)+3918.95752));
//	        	System.out.println("PixelAGL: "+this.pixelCountAGL);

//	        	this.aboveGroundLevelFeet=this.aboveGroundLevel*3.2808399;//convert from meters to feet
//	        	//curve fit polynomial equation degree 1
//	        	//this.pixelsPerFoot=(Math.sqrt((3872*3872)+(2592*2592)))/(Math.abs((this.aboveGroundLevelFeet*2)*Math.tan(this.lensFieldOfView/2)));
//	        	System.out.println("XY PPF= "+this.pixelsPerFoot);
//	        	//change this to new way of calculating from camera field of view
//	        	this.pixelsPerFoot=((this.aboveGroundLevelFeet*-1.64666)+1613.33391)/120;//calculate feet per pixel
//	        	//System.out.println("PPF: "+(((this.aboveGroundLevelFeet*-1.64666)+1613.33391)/120));//calculate feet per pixel);



	        		//System.out.println(comments);
//	        }
//	        catch(JpegProcessingException e){
//	        	System.out.println(e);
//	        }
	        
	        //image pixel reader
	        img = ImageIO.read(new File(filename));
	        System.out.println("Image Height: "+img.getHeight()+" Width: "+img.getWidth());
	        //this.computeScale();
	        //this.graphScale();
	        
	        
		} catch (IOException e) {
			System.out.println(e);
		}


	}
//	public getSquareSubImage(Shape targetShape){
//		BufferedImage temp=getSubImage(targetShape);
//		int widthHeight=targetShape.getSize().x;
//		int height=0;
//		boolean widthSide=true;
//		if(targetShape.getSize().y>targetShape.getSize().x){
//			widthHeight=targetShape.getSize().y;
//			width=0;
//			widthSide=false;
//		}
//		BufferedImage tempSquare=new BufferedImage(widthHeight,widthHeight,BufferedImage.TYPE_4BYTE_ABGR);
//		for(int i=(k*temp.getWidth());i<((k*imgBase.getWidth())+imgBase.getWidth());i++){
//			for(int j=0;j<imgBase.getHeight();j++){
//				tempSquare.setRGB(i,j, temp.getRGB(i+width,j+height));
//			}
//
//		}
//
//		
//	}
    public BufferedImage getSubImage(Shape targetShape){
		BufferedImage tempImg=new BufferedImage(targetShape.getSize().x,targetShape.getSize().y,BufferedImage.TYPE_3BYTE_BGR);
		for(int i=targetShape.getScreenCordinate().x;i<(targetShape.getScreenCordinate().x+targetShape.getSize().x);i++){
			for(int j=targetShape.getScreenCordinate().y;j<(targetShape.getScreenCordinate().y+targetShape.getSize().y);j++){
				if(targetShape.getValue(i-targetShape.getScreenCordinate().x, j-targetShape.getScreenCordinate().y))
				tempImg.setRGB(i-targetShape.getScreenCordinate().x, j-targetShape.getScreenCordinate().y, img.getRGB(i,j));
				else{
					tempImg.setRGB(i-targetShape.getScreenCordinate().x, j-targetShape.getScreenCordinate().y, (Color.BLACK).getRGB());
				}
			}

		}
		return tempImg;
    }
    public BufferedImage getSubImage(Shape targetShape,boolean getSkeleton){
		BufferedImage tempImg=new BufferedImage(targetShape.getSize().x,targetShape.getSize().y,BufferedImage.TYPE_3BYTE_BGR);
		for(int i=targetShape.getScreenCordinate().x;i<(targetShape.getScreenCordinate().x+targetShape.getSize().x);i++){
			for(int j=targetShape.getScreenCordinate().y;j<(targetShape.getScreenCordinate().y+targetShape.getSize().y);j++){
				if(targetShape.getValue(i-targetShape.getScreenCordinate().x, j-targetShape.getScreenCordinate().y))
				tempImg.setRGB(i-targetShape.getScreenCordinate().x, j-targetShape.getScreenCordinate().y, img.getRGB(i,j));
				else{
					tempImg.setRGB(i-targetShape.getScreenCordinate().x, j-targetShape.getScreenCordinate().y, (Color.BLACK).getRGB());
				}
			}

		}
		if(getSkeleton){
			for(int i=0;i<targetShape.getSkeltonPoints().size();i++){
				tempImg.setRGB(targetShape.getSkeltonPoints().get(i).x, targetShape.getSkeltonPoints().get(i).y, (new Color(255,0,0,255)).getRGB());
			}
		}
					
		return tempImg;
    }

    public BufferedImage getSubImage(Shape targetShape,int borderPixels){
		BufferedImage tempImg=new BufferedImage(targetShape.getSize().x+(2*borderPixels),targetShape.getSize().y+(2*borderPixels),BufferedImage.TYPE_3BYTE_BGR);
		for(int i=targetShape.getScreenCordinate().x-borderPixels;i<(targetShape.getScreenCordinate().x+targetShape.getSize().x+borderPixels);i++){
			for(int j=targetShape.getScreenCordinate().y-borderPixels;j<(targetShape.getScreenCordinate().y+targetShape.getSize().y+borderPixels);j++){
				if(i>=0&&j>=0&&i<img.getWidth()&&j<img.getHeight()){
					tempImg.setRGB(i-(targetShape.getScreenCordinate().x-borderPixels), j-(targetShape.getScreenCordinate().y-borderPixels), img.getRGB(i,j));
				}
				else{
					tempImg.setRGB(i-(targetShape.getScreenCordinate().x-borderPixels), j-(targetShape.getScreenCordinate().y-borderPixels),(new Color(0,0,0,0).getRGB()));
				}
			}

		}
		return tempImg;
    }
    public String getMetaData(){
    	return this.comments;
    }

	public Color getColorAt(int x,int y){
		return this.convertPixel(img.getRGB(x,y));
		//return new Color(img.getRGB(x, y));//changed for chromosomes
	}
	public double getAboveGroundLevelFeet() {
		return aboveGroundLevelFeet;
	}
	public int getPixelCountAGL() {
		return pixelCountAGL;
	}
	public double getPixelsPerFoot() {
		return pixelsPerFoot;
	}
	public double getImgLat() {
		return imgLat;
	}
	public double getImgLong() {
		return imgLong;
	}
	public double getHeading() {
		return heading;
	}
    public LatLongPoint calcPointLatLongs(Point shapeCordinates){
    	Point centerPoint = new Point ( img.getWidth()/2, img.getHeight()/2 );
    	LatLongPoint result = new LatLongPoint(0,0);
    	double degrees;
    	double distance12=shapeCordinates.distance(centerPoint);
    	double distance13=centerPoint.distance(new Point(centerPoint.x,img.getHeight()));
    	double distance23=shapeCordinates.distance(new Point(centerPoint.x,img.getHeight()));
    	double angle = Math.acos(((distance12*distance12)+(distance13*distance13)-(distance23*distance23))/(2*distance12*distance13));//cos-1((P12^2 + P13^2 - P23^2)/(2 * P12 * P13)) //between 0 and 2 * PI, angle is in radians//1degree=0.0174532925radians
    	if(shapeCordinates.x>centerPoint.x){
    		degrees=360-(angle/0.0174532925);
    	}
    	else{
    		degrees=(angle/0.0174532925);
    	}
    	degrees=this.heading+degrees;
    	if(degrees>360){
    		degrees-=360;
    	}
    	double distance      = (shapeCordinates.distance(centerPoint)/this.pixelsPerFoot)/this.feetPerDegreeLatLong;//distance in lat longs
    	result.setLong(this.imgLong +( distance * Math.sin( (degrees*0.0174532925) ) ));
    	result.setLat(this.imgLat + ( distance * Math.cos( (degrees*0.0174532925) ) ));
    	return result;
    }
    public Color convertPixel(int pixel){
	      int red = (pixel & 0x00ff0000) >> 16;
	      int green = (pixel & 0x0000ff00) >> 8;
	      int blue = pixel & 0x000000ff;
	      return new Color(red,green,blue);

  }
    public int getImgHeight(){
    	return img.getHeight();
    }
    public int getImgWidth(){
    	return img.getWidth();
    }
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getMins() {
		return mins;
	}
	public void setMins(int mins) {
		this.mins = mins;
	}
	public int getSecs() {
		return secs;
	}
	public void setSecs(int secs) {
		this.secs = secs;
	}
	public void computeScale(){
		Color tempColor=new Color(0,0,0);
		for(int i=100;i<this.getImgWidth();i++){
			for(int j=100;j<this.getImgHeight();j++){
				tempColor=this.getColorAt(i,j);
				double tempGreyPixel=(.299*tempColor.getRed())+(.587*tempColor.getGreen())+(.114*tempColor.getBlue());
				this.grayScale[(int)Math.round(tempGreyPixel)]++;
				
			}
		}
	}
	public void graphScale(){
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter("GrayScale.txt",true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  String buffer="";
			  for(int i=0;i<this.grayScale.length;i++){//out.write("Hello Java");
				  buffer+=""+this.grayScale[i]+",";
			  }
			  out.write(buffer);
//			  for(int j=255;j>0;j--){
//				  buffer="";
//				  for(int i=0;i<this.grayScale.length;i++){//out.write("Hello Java");
//					  if((this.grayScale[i]/740000)*255>j){
//						  buffer+='M';
//					  }
//					  else{
//						  buffer+='_';
//					  }
//				  }
//				  out.write(buffer);
//			  }
			  out.write("\n");
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
			  
			
	}


}
