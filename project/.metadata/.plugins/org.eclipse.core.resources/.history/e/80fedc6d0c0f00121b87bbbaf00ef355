package chromosome;

import java.awt.Point;
import MedialAxis.MedialAxis;
import basicObjects.Cluster;

public class ChromosomeCluster extends Cluster {

	private double imgHeading;
    private int clusterNimageID;
    private int colorCount;
    private ChromosomeCluster next;
    private MedialAxis medialAxis;
    
	public ChromosomeCluster(int ChromosomeNum){
		super();
		initChromosomeCluster();
		this.clusterNimageID=ChromosomeNum;
	}
	public ChromosomeCluster(Point size){
		super(size);
		initChromosomeCluster();
	}
    public ChromosomeCluster(short[][] map,int xPoint,int yPoint,int shapeColorID){
    	super(map,xPoint,yPoint,shapeColorID);
    	initChromosomeCluster();
        
    }
    public ChromosomeCluster(ChromosomeCluster makeNew){
    	super((Cluster)makeNew);
    	initChromosomeCluster();
        copyChromosome(makeNew);
    }
	public int getWidths(int pos){
		return this.medialAxis.getObjectWidth()[pos];
	}

    private void initChromosomeCluster(){
    	this.colorCount=0;
    	this.medialAxis=new MedialAxis();
    	next=null;
    }


    public MedialAxis getMedialAxis() {
		return medialAxis;
	}
	public void setMedialAxis(MedialAxis medialAxis) {
		this.medialAxis = medialAxis;
	}
    public void setImgHeading(double newHeading){
    	this.imgHeading=newHeading;
    }
    public double getImgHeading(){
    	return this.imgHeading;
    }
    public int getClusterNimageID(){
    	return this.clusterNimageID;
    }
    public void setClusterNimageID(int ID){
    	this.clusterNimageID=ID;
    }

    public int getColorCount(){
    	return this.colorCount;
    }
    public void setColorCount(int count){
    	this.colorCount=count;
    }
    public ChromosomeCluster getNext(){
        return next;
    }
    public void setNext(ChromosomeCluster clusterN){
        next=clusterN;
    }
    public void copyChromosome(ChromosomeCluster copyChromosome){
    	this.colorCount=copyChromosome.getColorCount();
    	this.clusterNimageID=copyChromosome.getClusterNimageID();
    	this.imgHeading=copyChromosome.imgHeading;
        this.medialAxis=new MedialAxis(copyChromosome.medialAxis);

    }
}
