package Target;

import java.awt.Color;
import java.awt.Point;

import TargetText.TargetText;
import basicObjects.Shape;

public class TargetShape extends Shape {
	private TargetText myText;
	private TargetText tryText;
	private double imgHeading;
    private int targetNimageID;
    private String metaData;
    private Color colorOShape;
    private int colorCount;
    private double aboveGroundLevel;
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
    	colorOShape=new Color(0,0,0);
    	this.colorCount=0;
    	this.aboveGroundLevel=0;
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
    public void setTargetNimageID(int ID){
    	this.targetNimageID=ID;
    }
    public String getMetaData(){
    	return this.metaData;
    }
    public void setMetaData(String data){
    	this.metaData=data;
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
    	this.aboveGroundLevel=copyTarget.getAGL();
    	this.colorCount=copyTarget.getColorCount();
    	this.targetNimageID=copyTarget.getTargetNimageID();
    	colorOShape=copyTarget.getColor();
    	this.imgHeading=copyTarget.imgHeading;
    	this.myText=copyTarget.myText;
    	//this.myBuckets.copyBuckets(copyShape.myBuckets.getBucketArray());
    }
}
