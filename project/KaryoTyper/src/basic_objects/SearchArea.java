package basic_objects;

import java.awt.Point;
import java.util.LinkedList;

import chromosome.ChromosomeCluster;
import chromosome.GeneticSlideImage;

public class SearchArea {

	private boolean[][] pixelChecked;
	/*
	 * pixelFound is a memory versus time in get matching pixel spotNext stores a 2D array of what
	 * pixels should be added to the ones to check next without searching the foundList of next ones
	 * to be checked
	 */
	private boolean[][] pixelFound;
	public SearchArea(GeneticSlideImage img){
		pixelChecked = new boolean[img.getImgWidth()][img.getImgHeight()];
		pixelFound = new boolean[img.getImgWidth()][img.getImgHeight()];
		initPixelsChecked();

	}
	public SearchArea(int width,int height){
		pixelChecked = new boolean[width][height];
		pixelFound = new boolean[width][height];
		initPixelsChecked();	
	}
	
	/**
	 * prepare the search area with already checked all places that
	 * are not a part of the cluster in the cluster square area
	 * @param myCluster
	 * @param cutLines
	 */
	public void prepForCluster(ChromosomeCluster myCluster, LinkedList<PointList> cutLines){
		for(int i=0;i<myCluster.getSize().x;i++){
			for(int j=0;j<myCluster.getSize().y;j++){
				if(!myCluster.getValue(i,j)){
					this.pixelChecked[i][j]=true;
				}
			}
		}
		for(int i=0;i<cutLines.size();i++){
			for(int j=0;j<cutLines.get(i).getList().size();j++){
				this.pixelChecked[cutLines.get(i).getList().get(j).x][cutLines.get(i).getList().get(j).y]=true;
			}
		}
	}
	public boolean isPixelChecked(Point temp) {
		return pixelChecked[temp.x][temp.y];
	}

	public void addPixelFound(Point temp) {
		pixelFound[temp.x][temp.y] = true;
	}

	public boolean isPixelFound(Point temp) {
		return pixelFound[temp.x][temp.y];
	}

	public void initPixelsChecked() {
		for (int i = 0; i < pixelChecked.length; i++) {
			for (int j = 0; j < pixelChecked[0].length; j++) {
				this.pixelChecked[i][j] = false;
				this.pixelFound[i][j] = false;
			}
		}
	}

	public void initNextPixel() {
		for (int i = 0; i < pixelFound.length; i++) {
			for (int j = 0; j < pixelFound[0].length; j++) {
				this.pixelFound[i][j] = false;
			}
		}

	}
	public void markPixelChecked(Point temp) {
		pixelChecked[temp.x][temp.y] = true;
	}
	public int getWidth(){
		return pixelChecked.length;
	}
	public int getHeight(){
		return pixelChecked[0].length;
	}


}
