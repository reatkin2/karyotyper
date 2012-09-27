package basicObjects;

import java.awt.Point;
import java.util.LinkedList;

import Target.TargetShape;

public class MedialAxisGraph {
	LinkedList<AxisVertex> axisGraph = new LinkedList<AxisVertex>();

	public MedialAxisGraph(LinkedList<Point> medialAxis) {
		axisGraph = new LinkedList<AxisVertex>();
		buildGraph(medialAxis);
	}

	private void buildGraph(LinkedList<Point> medialAxis) {
		if (medialAxis != null) {
			for (int i = 0; i < medialAxis.size(); i++) {
				AxisVertex tempVertex = new AxisVertex(medialAxis.get(i));
				if (!axisGraph.contains(tempVertex)) {
					axisGraph.add(tempVertex);
					for (int j = 0; j < axisGraph.size(); j++) {
						if (tempVertex.checkAdjacent(axisGraph.get(j))) {
							tempVertex.addChild(axisGraph.get(j));
							axisGraph.get(j).addChild(tempVertex);
						}
					}
				}

			}
		}
	}

	public LinkedList<AxisVertex> getIntersections() {
		LinkedList<AxisVertex> interSections = new LinkedList<AxisVertex>();
		for (int i = 0; i < this.axisGraph.size(); i++) {
			if (axisGraph.get(i).isIntersection()) {
				interSections.add(axisGraph.get(i));
			}
		}
		return interSections;
	}

	public LinkedList<AxisVertex> getSegment(LinkedList<AxisVertex> segment, int pos) {
		// LinkedList<Vertex> segment=new LinkedList<Vertex>();
		int addedCount = 0;
		for (int i = 0; i < segment.get(pos).getChildren().size(); i++) {
			if (!segment.get(pos).getChildren().get(i).isIntersection()
					&& !segment.contains(segment.get(pos).getChildren().get(i))) {
				segment.add(segment.get(pos).getChildren().get(i));
				addedCount++;
				// LinkedList<Vertex> temp=
				getSegment(segment, pos + addedCount);
				// if(temp.size()>0){
				// addedCount+=temp.size();
				// segment.addAll(temp);
				// }
			}
		}
		return segment;
	}

	public void removeSegments(int minLength, int maxLength) {
		LinkedList<AxisVertex> intersections = this.getIntersections();
		for (int i = 0; i < intersections.size(); i++) {
			for (int j = 0; j < intersections.get(i).getChildren().size(); j++) {
				LinkedList<AxisVertex> tempList = new LinkedList<AxisVertex>();
				tempList.add(intersections.get(i).getChildren().get(j));
				LinkedList<AxisVertex> segment = getSegment(tempList, 0);
				if (minLength != -1 && segment.size() < minLength) {
					removeSingleSegment(segment);
				} else if (maxLength != -1 && segment.size() > maxLength) {
					removeSingleSegment(segment);
				}

			}
		}
	}

	private void removeSingleSegment(LinkedList<AxisVertex> removeList) {
		for (int i = 0; i < removeList.size(); i++) {
			this.axisGraph.remove(removeList.get(i));
		}
	}

	public LinkedList<Point> getMedialAxis() {
		LinkedList<Point> medialAxis = new LinkedList<Point>();
		for (int i = 0; i < this.axisGraph.size(); i++) {
			medialAxis.add(this.axisGraph.get(i).getPoint());
		}
		return medialAxis;
	}

	public LinkedList<Point> trimMedialAxis(int minDistance,
			LinkedList<Point> medialAxis, TargetShape shape) {
		LinkedList<Point> trimmedAxis = new LinkedList<Point>();
		if (medialAxis != null) {
			for (int i = 0; i < medialAxis.size(); i++) {
				if (shape.getDistanceFromEdge(medialAxis.get(i)) >= minDistance) {
					trimmedAxis.add(medialAxis.get(i));
				}
			}
		}
		return trimmedAxis;

	}

	public LinkedList<AxisVertex> getAxisGraph() {
		return axisGraph;
	}
}
