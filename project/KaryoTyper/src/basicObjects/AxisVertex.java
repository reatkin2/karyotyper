package basicObjects;

import java.awt.Point;
import java.util.LinkedList;

public class AxisVertex {
	private LinkedList<AxisVertex> children;
	private Point myVertex;
	private double tangentSlope;
	private boolean noTanSlope;
	private double tangentYIntercept;
	private double orthogonalSlope;
	private boolean noOrthoSlope;
	private double orthogonalYIntercept;

	public AxisVertex(Point vertex) {
		myVertex = new Point(vertex);
		children = new LinkedList<AxisVertex>();
	}

	public void addChild(AxisVertex nextChild) {
		if (!children.contains(nextChild)
				&& !myVertex.equals(nextChild.myVertex)) {
			children.add(nextChild);
		}
	}

	public boolean isChild(AxisVertex checkPoint) {
		if (children.contains(checkPoint)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isIntersection() {
		if (children.size() > 2) {
			return true;
		}
		return false;
	}

	public boolean checkAdjacent(AxisVertex checkPoint) {
		// TODO(Andrew): change this for effiecience do this in shape without
		// calculating distance
		if (isChild(checkPoint)) {
			return true;
		} else if (!checkPoint.myVertex.equals(myVertex)
				&& checkPoint.myVertex.distance(myVertex) < 1.5) {
			return true;
		}
		return false;
	}

	public Point getPoint() {
		return myVertex;
	}

	public int numberOfChildren() {
		return this.children.size();
	}

	public LinkedList<AxisVertex> getChildren() {
		return children;
	}
}
