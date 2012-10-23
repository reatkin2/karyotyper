
public class Bucket {

	
	private double topwidth;


	private double height;
	private double waterHeight;
	private double baseWidth;
	private double volume;
	private String name;
		public Bucket(double hw,double wt,double ht,double wb){
		height=wt;
		this.waterHeight=hw;
		this.baseWidth=wb;
		this.volume=-1;
	}
	public Bucket(){
		height=-1;
		this.waterHeight=-1;
		this.baseWidth=-1;
		this.volume=-1;
	}
	

	public double getTopwidth() {
		return topwidth;
	}


	public void setTopwidth(double topwidth) {
		this.topwidth = topwidth;
	}


	public double getHeight() {
		return height;
	}


	public void setHeight(double height) {
		this.height = height;
	}


	public double getWaterHeight() {
		return waterHeight;
	}


	public void setWaterHeight(double waterHeight) {
		this.waterHeight = waterHeight;
	}


	public double getBaseWidth() {
		return baseWidth;
	}


	public void setBaseWidth(double baseWidth) {
		this.baseWidth = baseWidth;
	}


	public double getVolume() {
		return volume;
	}


	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double calcVolume(){
		return 0.0;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
