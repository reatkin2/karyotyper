package chromosome;

import java.awt.Point;
import java.util.LinkedList;

import medial_axis.MedialAxis;
import medial_axis.MedialAxisGraph;

import basic_objects.Cluster;

public class ChromosomeCluster extends Cluster {

	private double imgHeading;
	private int clusterNimageID;
	private int colorCount;
	private ChromosomeCluster next;
	private MedialAxisGraph medialAxisGraph;
	private LinkedList<ChromosomeCluster> darkBands;


	public ChromosomeCluster(int ChromosomeNum) {
		super();
		initChromosomeCluster();
		this.clusterNimageID = ChromosomeNum;
	}

	public ChromosomeCluster(Point size) {
		super(size);
		initChromosomeCluster();
	}

	public ChromosomeCluster(short[][] map, int xPoint, int yPoint, int shapeColorID,Point canvasStart) {
		super(map, xPoint, yPoint, shapeColorID,canvasStart);
		initChromosomeCluster();

	}

	public ChromosomeCluster(ChromosomeCluster makeNew) {
		super((Cluster) makeNew);
		initChromosomeCluster();
		copyChromosome(makeNew);
	}

	public double getWidths(int pos) {
		return this.medialAxisGraph.getObjectWidth()[pos];
	}

	private void initChromosomeCluster() {
		this.colorCount = 0;
		darkBands=null;
		next = null;
	}

	public void createSkeleton() {
		this.medialAxisGraph = new MedialAxisGraph(this);
		this.medialAxisGraph.createSkeleton(this);
	}

	public void createMedialAxisGraph(GeneticSlideImage image) {
		this.medialAxisGraph.createAxisGraph( image.getChromoWidth());
		// this.medialAxisGraph.buildGraph(this.medialAxisGraph.getMedialAxisPoints(),this.medialAxisGraph.getDistanceMap());
	}

	public MedialAxisGraph getMedialAxisGraph() {
		return medialAxisGraph;
	}

	public void setImgHeading(double newHeading) {
		this.imgHeading = newHeading;
	}

	public double getImgHeading() {
		return this.imgHeading;
	}

	public int getClusterNimageID() {
		return this.clusterNimageID;
	}

	public void setClusterNimageID(int ID) {
		this.clusterNimageID = ID;
	}

	public int getColorCount() {
		return this.colorCount;
	}

	public void setColorCount(int count) {
		this.colorCount = count;
	}

	public ChromosomeCluster getNext() {
		return next;
	}

	public void setNext(ChromosomeCluster clusterN) {
		next = clusterN;
	}
	public LinkedList<ChromosomeCluster> getDarkBands() {
		return darkBands;
	}

	public void setDarkBands(LinkedList<ChromosomeCluster> darkBands) {
		this.darkBands = darkBands;
	}
	public LinkedList<Point> getDarkBandPoints(){
		LinkedList<Point> bandPoints=new LinkedList<Point>();
		if(darkBands!=null&&!darkBands.isEmpty()){
			for(int i=0;i<darkBands.size();i++){
				LinkedList<Point> thisBand=darkBands.get(i).getPointList();
				for(int j=0;j<thisBand.size();j++){
					bandPoints.add(new Point(thisBand.get(j).x-super.getImageLocation().x,
							thisBand.get(j).y-super.getImageLocation().y));
				}
			}
		}
		return bandPoints;
	}

	public void copyChromosome(ChromosomeCluster copyChromosome) {
		this.colorCount = copyChromosome.getColorCount();
		this.clusterNimageID = copyChromosome.getClusterNimageID();
		this.imgHeading = copyChromosome.imgHeading;
		this.medialAxisGraph = copyChromosome.medialAxisGraph;
		this.darkBands=copyChromosome.getDarkBands();

	}
}
