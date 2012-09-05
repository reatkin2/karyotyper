package basicObjects;

public class PathPoint {
	private LatLongPoint latLong;
	private double heading;
	private double altitude;
	private int hours;
	private int min;
	private int secs;
	String metaData;
	public PathPoint(LatLongPoint newlatLong,int hrs,int mns,int scs,double alt,double hdng,String metaDataNew){
		latLong=newlatLong;
		hours=hrs;
		secs=scs;
		min=mns;
		altitude=alt;
		heading=hdng;
		metaData=metaDataNew;
	}
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getSecs() {
		return secs;
	}
	public void setSecs(int secs) {
		this.secs = secs;
	}
	public LatLongPoint getLatLong() {
		return latLong;
	}
	public void setLatLong(LatLongPoint latLong) {
		this.latLong = latLong;
	}
}
