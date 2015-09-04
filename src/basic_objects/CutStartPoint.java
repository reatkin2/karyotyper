package basic_objects;

public class CutStartPoint {
	private Vertex startPoint;
	private Vertex intersection;
	public CutStartPoint(Vertex start,Vertex intersect){
		this.startPoint=start;
		this.intersection=intersect;
		
	}
	public Vertex getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Vertex startPoint) {
		this.startPoint = startPoint;
	}
	public Vertex getIntersection() {
		return intersection;
	}
	public void setIntersection(Vertex intersection) {
		this.intersection = intersection;
	}

}
