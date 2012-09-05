package Target;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import TargetText.TargetText;
import basicObjects.LatLongPoint;
import basicObjects.Shape;


import Color.ColorBuckets;

public class TargetShape extends Shape {
	private TargetText myText;
	private TargetText tryText;
	private double imgHeading;
    private int targetNimageID;
    private ColorBuckets myBuckets;
    private String metaData;
    private Color colorOShape;
    private int colorCount;
    private double aboveGroundLevel;
    private LatLongPoint latLongs;
    private LatLongPoint imglatLongs;
    private TargetShape next;
    private double sizex;
    private double sizey;
    
    public TargetShape(){
    	super();
    	initTarget();
    }
	public TargetShape(int targetNum){
		super();
		initTarget();
		this.targetNimageID=targetNum;
	}
	public TargetShape(Point size){
		super(size);
		initTarget();
	}
    public TargetShape(short[][] map,int xPoint,int yPoint,int shapeColorID){
    	super(map,xPoint,yPoint,shapeColorID);
        initTarget();
        
    }
    public TargetShape(TargetShape makeNew){
    	super((Shape)makeNew);
        initTarget();
        copyTarget(makeNew);
    }


    private void initTarget(){
    	this.metaData="";
    	this.imglatLongs=new LatLongPoint(0,0);
    	this.latLongs=new LatLongPoint(0,0);
    	colorOShape=new Color(0,0,0);
    	this.colorCount=0;
    	this.aboveGroundLevel=0;
    	myBuckets= new ColorBuckets();
    	myText=new TargetText();
    	tryText=new TargetText();
    	next=null;
    }
    public void setImgHeading(double newHeading){
    	this.imgHeading=newHeading;
    }
    public double getImgHeading(){
    	return this.imgHeading;
    }
    public int getTargetNimageID(){
    	return this.targetNimageID;
    }
    public void setTargetNiamgeID(int ID){
    	this.targetNimageID=ID;
    }
	public String getColorName(){
		int closeTo=0;
		double lowestDistance=500;
		Color list[]=new Color[11];
		list[0]=new Color(Color.red.getRed(),Color.red.getGreen(),Color.red.getBlue());
		list[1]=new Color(Color.green.getRed(),Color.green.getGreen(),Color.green.getBlue());
		list[2]=new Color(Color.blue.getRed(),Color.blue.getGreen(),Color.blue.getBlue());
		list[3]=new Color(Color.orange.getRed(),Color.orange.getGreen(),Color.orange.getBlue());
		list[4]=new Color(Color.yellow.getRed(),Color.yellow.getGreen(),Color.yellow.getBlue());
		list[5]=new Color(128,0,128);//purple
		list[6]=new Color(255,255,255);//white
		list[7]=new Color(0,0,0);//black
		list[8]=new Color(250,250,210);//yellow
		list[9]=new Color(255,165,0);//orange 
		
		for(int i=0;i<10;i++){
			double temp=distanceTo(this.colorOShape.getBlue(),
							this.colorOShape.getGreen(),
								this.colorOShape.getRed(),
								list[i].getRed(),
								list[i].getGreen(),
								list[i].getBlue());
			if(temp<lowestDistance){
				lowestDistance=temp;
				closeTo=i;
			}
			
		}
		return this.colorNames(closeTo);
		
	}
	private String colorNames(int color){
		switch(color){
		case 0:
			return "Red";
		case 1:
			return "Green";
		case 2:
			return "Blue";
		case 3:
			return "Orange";
		case 4:
			return "Yellow";
		case 5:
			return "Purple";
		case 6:
			return "White";
		case 7:
			return "Black";
		case 8:
			return "Yellow";
		case 9:
			return "Orange";
			
		}
		return "";
	}
	private double distanceTo(int x, int y,int z,int x2,int y2,int z2){
		return Math.sqrt((2*(x-x2)*(x-x2)) + (4*(y-y2)*(y-y2)) + (3*(z-z2)*(z-z2)));
	}
    public String getMetaData(){
    	return this.metaData;
    }
    public void setMetaData(String data){
    	this.metaData=data;
    }
    public void setBuckets(ColorBuckets newBuckets){
    	myBuckets=newBuckets;
    }
    public int getColorCount(){
    	return this.colorCount;
    }
    public void setColorCount(int count){
    	this.colorCount=count;
    }
    public void setColor(Color colorX){
    	this.colorOShape=colorX;
    }
    public Color getColor(){
    	return this.colorOShape;
    }
    public double getAGL(){
    	return this.aboveGroundLevel;
    }
    public void setAGL(double agl){
    	this.aboveGroundLevel=agl;
    }
    public double getLat(){
    	return this.latLongs.getLat();
    }
    public double getLong(){
    	return this.latLongs.getLong();
    }
    public void setTargetLatLong(double Lat,double Long){
    	this.latLongs.setLat(Lat);
    	this.latLongs.setLong(Long);
    }
    public double getImgLat(){
    	return this.imglatLongs.getLat();
    }
    public double getImgLong(){
    	return this.imglatLongs.getLong();
    }
    public LatLongPoint getTargetLatLong(){
    	return this.latLongs;
    }
    public void setImgLatLong(double Lat,double Long){
    	this.imglatLongs.setLat(Lat);
    	this.imglatLongs.setLong(Long);
    }
    public ColorBuckets getBuckets(){
    	return myBuckets;
    }
    public TargetShape getNext(){
        return next;
      }
      public void setNext(TargetShape shapeN){
        next=shapeN;
      }
    public double getSizex(){
        return this.sizex;
    }
    public double getSizey(){
        return this.sizey;
    }
    public void setSizex(double size){
        this.sizex = size;
    }
    public void setSizey(double size){
        this.sizey = size;
    }
    public TargetText getText(){
    	return this.myText;
    }
    public void setText(TargetText newText){
    	this.myText=newText;
    }
    public TargetText tryText(){
    	return this.tryText;
    }
    public void trySetText(TargetText newText){
    	this.tryText=newText;
    }

    public void copyTarget(TargetShape copyTarget){
    	this.metaData=copyTarget.getMetaData();
    	this.imglatLongs.setLocation(copyTarget.getImgLong(),copyTarget.getImgLat());
    	this.latLongs.setLocation(copyTarget.getLong(),copyTarget.getLat());
    	this.aboveGroundLevel=copyTarget.getAGL();
    	this.colorCount=copyTarget.getColorCount();
    	this.targetNimageID=copyTarget.getTargetNimageID();
    	colorOShape=copyTarget.getColor();
    	this.myBuckets=copyTarget.getBuckets();
    	this.imgHeading=copyTarget.imgHeading;
    	this.myText=copyTarget.myText;
    	//this.myBuckets.copyBuckets(copyShape.myBuckets.getBucketArray());
    }
}
