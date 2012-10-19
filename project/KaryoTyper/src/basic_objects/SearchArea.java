package basic_objects;

import java.awt.Point;

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
		for (int i = 0; i < pixelChecked.length; i++) {
			for (int j = 0; j < pixelFound[0].length; j++) {
				this.pixelFound[i][j] = false;
			}
		}

	}
	public void markPixelChecked(Point temp) {
		pixelChecked[temp.x][temp.y] = true;
	}


}
