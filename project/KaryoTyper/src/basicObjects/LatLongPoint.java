package basicObjects;

public class LatLongPoint {
	double longs;
	double lats;
	public LatLongPoint (double newlong,double newlats){
		longs=newlong;
		lats=newlats;
	}
	public double distance(LatLongPoint otherPoint){
		double distance=Math.sqrt(((longs-otherPoint.getLong())*(longs-otherPoint.getLong())
							+(lats-otherPoint.getLat())*(lats-otherPoint.getLat())));
		return distance;
	}
	public double getLong() {
		return longs;
	}
	public void setLong(double longs) {
		this.longs = longs;
	}
	public double getLat() {
		return lats;
	}
	public void setLat(double lats) {
		this.lats = lats;
	}
	public void setLocation(double longsX,double latsX){
		this.longs=longsX;
		this.lats=latsX;
	}


}
