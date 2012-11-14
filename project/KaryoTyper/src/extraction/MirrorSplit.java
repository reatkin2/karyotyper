package extraction;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import basic_objects.OrthogonalLine;
import basic_objects.RadialVectors;
import basic_objects.Vector;
import basic_objects.Vertex;

import medial_axis.DistanceMap;
import medial_axis.MedialAxisGraph;

public class MirrorSplit {
	private MedialAxisGraph medialAxisGraph;

	public MirrorSplit(MedialAxisGraph graph) {
		this.medialAxisGraph = graph;
	}

	// TODO(aamcknig): need to find a start point that is a stable start
	// point where the width on both sides the chromosome are half the width
	// of the chromosome
	// TODO(aamcknig): need to have two types of stop points where
	// the chromosome comes to an end or you reach another stable part
	// of the chromsome where the width on both sides of the medial axis
	// are half the width of the chromosome
	public void markSplit(Rectangle bounds, LinkedList<Vertex> startPoints) {
		double upperDistanceAvg = -1;
		double lowerDistanceAvg = -1;
		int stableCount = 0;
		final int stableValue = 5;
		int lastStable = 0;
		int chromoWidthOffset = 2;
		Vertex nextPoint;
		Vector nextVector = null;
		Point currShortPoint = new Point(-1, -1);
		LinkedList<OrthogonalLine> orthoList = new LinkedList<OrthogonalLine>();
		LinkedList<Point> cutList = new LinkedList<Point>();
		try {
			for (int i = 0; i < startPoints.size(); i++) {
				nextPoint = startPoints.get(i);
				while (!isProjectionEnd(nextPoint, nextVector)) {
					OrthogonalLine tempOrtho;
					// use small angle rather than 360
					if (i > 0 && lastStable == i - 1) {
						tempOrtho = getShortestDistance(bounds, currShortPoint,
								nextPoint.getPoint(), this.medialAxisGraph.getChromoWidth(),
								this.medialAxisGraph.getDistanceMap());
						if (tempOrtho.getIndex() < 5 || tempOrtho.getIndex() > 0) {
							lastStable = i;
						}

					}
					// use 360 angle
					else {
						tempOrtho = getShortestDistance(bounds, new Point(-1, -1),
								nextPoint.getPoint(), this.medialAxisGraph.getChromoWidth(),
								this.medialAxisGraph.getDistanceMap());
					}
					if (tempOrtho != null) {
						// if we are stable and dont have both lines inside the width of chromosome
						// use last good
						if (tempOrtho.isTwoLines()
								|| tempOrtho.getlength() > this.medialAxisGraph.getChromoWidth()
										- chromoWidthOffset) {
							orthoList.addFirst(tempOrtho);
							// draw a cutline here
							// use a array of pointlists for cut lines
							// fill in the line with points between non adjacent points
							// double over the center point
							// and mark cut
							cutList.add(tempOrtho.getLowerPoint());

							stableCount = 0;
						} else {
							nextVector = getNextVector(tempOrtho, nextPoint);
							nextPoint = getNextPoint(tempOrtho.getUpperPoint(),
									tempOrtho.getLowerPoint(), nextVector);
							// center on ortho line and project new point
							// based on perpendicular to ortho line
							// everything is normal here with chromosome
							// walk along medial axis
							// TODO(aamcknig):left off here
							orthoList.addFirst(tempOrtho);
							stableCount++;
							if (upperDistanceAvg == -1) {
								upperDistanceAvg = tempOrtho.getUpperDistance();
							} else {
								upperDistanceAvg = ((upperDistanceAvg * stableCount) + tempOrtho
										.getUpperDistance()) / (stableCount + 1);
							}
							if (lowerDistanceAvg == -1) {
								lowerDistanceAvg = tempOrtho.getLowerDistance();
							} else {
								lowerDistanceAvg = ((lowerDistanceAvg * stableCount) + tempOrtho
										.getLowerDistance()) / (stableCount + 1);
							}
							lastStable = i;

						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public boolean isProjectionEnd(Vertex checkPoint, Vector nextVector) {
		int x = (int) Math.round((double) checkPoint.getPoint().x + (5 * nextVector.x));
		int y = (int) Math.round((double) checkPoint.getPoint().y + (5 * nextVector.y));
		Point tempPoint = new Point(x, y);
		if (this.medialAxisGraph.getDistanceMap().getDistanceFromEdge(tempPoint) < 1) {
			return true;
		}
		return false;
	}

	public Vertex getNextPoint(Point top, Point bottom, Vector Next) {
		double changeX = (top.x - bottom.x) / 2;
		double changeY = (top.y - bottom.y) / 2;
		changeX += Next.x;
		changeY += Next.y;
		Point nextPnt = new Point((int) Math.round((double) bottom.x + changeX),
				(int) Math.round((double) bottom.y + changeY));
		Point childPnt = new Point((int) Math.round((double) bottom.x + changeX + Next.x),
				(int) Math.round((double) bottom.y + changeY + Next.y));
		Vertex next = new Vertex(nextPnt, 0);
		next.addChild(new Vertex(childPnt, 0));
		return next;
	}

	public Vector getNextVector(OrthogonalLine tempOrtho, Vertex currPoint) {
		Vector upper = new Vector(tempOrtho.getUpperPoint().x - tempOrtho.getCenterPoint().x,
				tempOrtho.getUpperPoint().y - tempOrtho.getCenterPoint().y);
		Vector ninty = Vector.normalize(Vector.rotateVector(upper, 90));
		Vector twoSeventy = Vector.normalize(Vector.rotateVector(upper, 90));
		Point nintyPoint = new Point((int) (tempOrtho.getCenterPoint().x + ninty.x),
				(int) (tempOrtho.getCenterPoint().y + ninty.y));
		Point twoSeventyPoint = new Point((int) (tempOrtho.getCenterPoint().x + twoSeventy.x),
				(int) (tempOrtho.getCenterPoint().y + twoSeventy.y));

		if (nintyPoint.distance(currPoint.getChildren().get(0).getPoint()) < twoSeventyPoint
				.distance((Point) currPoint.getChildren().get(0).getPoint())) {
			return ninty;
		} else {
			return twoSeventy;
		}
	}

	public LinkedList<OrthogonalLine> getAllDistances(Rectangle bounds, LinkedList<Vertex> graph,
			double chromoWidth, DistanceMap distanceMap) {
		LinkedList<OrthogonalLine> orthoList = new LinkedList<OrthogonalLine>();
		for (int i = 0; i < graph.size(); i++) {
			OrthogonalLine tempOrtho = getShortestDistance(bounds, new Point(-1, -1), graph.get(i)
					.getPoint(), chromoWidth, distanceMap);
			if (tempOrtho != null && tempOrtho.isTwoLines()) {
				orthoList.add(getShortestDistance(bounds, new Point(-1, -1), graph.get(i)
						.getPoint(), chromoWidth, distanceMap));
			}
		}
		return orthoList;
	}

	public LinkedList<Point> getAllProblemDistances(Rectangle bounds, LinkedList<Vertex> graph,
			double chromoWidth, DistanceMap distanceMap) {
		LinkedList<Point> pointList = new LinkedList<Point>();
		for (int i = 0; i < graph.size(); i++) {
			OrthogonalLine tempOrtho = getShortestDistance(bounds, new Point(-1, -1), graph.get(i)
					.getPoint(), chromoWidth, distanceMap);
			if (tempOrtho != null && tempOrtho.isTwoLines()) {
				double distance = tempOrtho.getlength();
				if (distance > (chromoWidth * 1.2) || distance < chromoWidth * .6) {
					pointList.add(graph.get(i).getPoint());
				} else {
					graph.get(i).setCentered(true);
				}
			} else {
				pointList.add(graph.get(i).getPoint());
			}
		}
		return pointList;
	}

	public OrthogonalLine getShortestDistance(Rectangle bounds, Point endPoint, Point centerPoint,
			double checkDistance, DistanceMap distanceMap) {// throws Exception
		boolean foundShortest = false;
		OrthogonalLine tempOrthoHalf = null;
		OrthogonalLine tempOrthoLine = null;
		int vectorCount = 40;
		double shortest = -1;
		Point shortestSide = new Point(-1, -1);
		int leftVector[] = new int[vectorCount];
		int rightVector[] = new int[vectorCount];
		Point leftPoints[] = new Point[vectorCount];
		Point rightPoints[] = new Point[vectorCount];

		// init array
		for (int i = 0; i < vectorCount; i++) {
			leftVector[i] = -1;
			rightVector[i] = -1;
		}
		RadialVectors vectors;
		// move out from axisPoint tell we run off distance map both sides
		for (int i = 1; !foundShortest && i < checkDistance; i++) {
			ArrayList<Point> pointList = new ArrayList<Point>();
			if (endPoint.x != -1) {
				vectors = new RadialVectors(centerPoint, 40, (double) i);
				pointList = vectors.getPointsInRange(endPoint, 45, 5);
			} else {
				vectors = new RadialVectors(centerPoint, 40, (double) i);
				pointList = vectors.getVectorsAsPointsOnImage();
			}
			// if(pointList.size()!=vectorCount){
			// throw new Exception("array dosn't match number of points");
			// }
			// go thru points in pointlist at distance i
			for (int j = 0; j < vectorCount; j++) {
				// check if left side has passed edge of chromosome
				if (bounds.contains(pointList.get(j))
						&& distanceMap.getDistanceFromEdge(pointList.get(j)) <= 0) {
					if (leftVector[j] == -1) {
						leftVector[j] = i;
						leftPoints[j] = pointList.get(j);
						if (shortestSide.x == -1) {
							shortestSide = pointList.get(j);
							tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
									vectors.getOpposite(pointList.get(j)), leftVector[j], -1, j);
						} else if (pointList.get(j).distance(centerPoint) < (centerPoint
								.distance(shortestSide))) {
							shortestSide = pointList.get(j);
							tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
									vectors.getOpposite(pointList.get(j)), leftVector[j], -1, j);
						}
						if (rightVector[j] != -1) {
							foundShortest = true;
							if (shortest < 0 || rightPoints[j].distance(leftPoints[j]) < shortest) {
								shortest = rightPoints[j].distance(leftPoints[j]);
								tempOrthoLine = new OrthogonalLine(centerPoint, rightPoints[j],
										leftPoints[j], leftVector[j], rightVector[j], j);
								tempOrthoLine.setTwoLines(true);
							}
						}
					}
				}
				// check if right side has passed edge of chromosome
				if (bounds.contains(vectors.getOpposite(pointList.get(j)))
						&& distanceMap.getDistanceFromEdge(vectors.getOpposite(pointList.get(j))) <= 0) {
					if (rightVector[j] == -1) {
						rightVector[j] = i;
						rightPoints[j] = vectors.getOpposite(pointList.get(j));
						if (shortestSide.x == -1) {
							shortestSide = vectors.getOpposite(pointList.get(j));
							tempOrthoHalf = new OrthogonalLine(centerPoint, rightPoints[j],
									pointList.get(j), rightVector[j], -1, j);
						} else if (vectors.getOpposite(pointList.get(j)).distance(centerPoint) < (centerPoint
								.distance(shortestSide))) {
							shortestSide = vectors.getOpposite(pointList.get(j));
							tempOrthoHalf = new OrthogonalLine(centerPoint, rightPoints[j],
									pointList.get(j), rightVector[j], -1, j);
						}
						if (leftVector[j] != -1) {
							foundShortest = true;
							if (shortest < 0 || rightPoints[j].distance(leftPoints[j]) < shortest) {
								shortest = rightPoints[j].distance(leftPoints[j]);
								tempOrthoLine = new OrthogonalLine(centerPoint, rightPoints[j],
										leftPoints[j], leftVector[j], rightVector[j], j);
								tempOrthoLine.setTwoLines(true);

							}
						}
					}
				}
			}
			if (shortest > -1) {// if(shortestTill==-1||shortest<shortestTill){
				foundShortest = true;
				return tempOrthoLine;
			}
		}
		return tempOrthoHalf;
	}

	public LinkedList<Vertex> getStartPoints(LinkedList<Vertex> graph,
			LinkedList<Vertex> intersections) {
		LinkedList<Vertex> startPoints = new LinkedList<Vertex>();
		for (int i = 0; i < intersections.size(); i++) {
			for (int j = 0; j < intersections.get(i).getChildren().size(); j++) {
				LinkedList<Vertex> segment = new LinkedList<Vertex>();
				segment.add(intersections.get(i).getChildren().get(j));
				segment = this.getSegmentFromIntersection(segment, 0, 0);
				if (segment != null) {
					Vertex start = new Vertex(segment.get(segment.size() - 5).getPoint(), 0);
					start.addChild(segment.get(segment.size() - 6));
					startPoints.add(start);
				}

			}
		}
		return startPoints;
	}

	/**
	 * this gets a segment that is separated by intersections using recursion starting from the
	 * vertex in the list at the position pos
	 * 
	 * @param segment
	 *            the segment as it has grown in recursion to this point
	 * @param pos
	 *            the vertex that we are checking for connections to this segment on
	 * @return a segment that is separated by 2 intersections or an 1 intersection and the end of
	 *         the line
	 */
	public LinkedList<Vertex> getSegmentFromIntersection(LinkedList<Vertex> segment, int pos,
			int stableCount) {
		if (stableCount == 10) {
			Vertex tempVert = new Vertex(new Point(-5, -5), 0);
			segment.add(tempVert);
			return segment;
		}
		for (int i = 0; i < segment.get(pos).getChildren().size(); i++) {
			if (!segment.get(pos).getChildren().get(i).isIntersection()
					&& !segment.contains(segment.get(pos).getChildren().get(i))) {
				if (segment.get(pos).getChildren().get(i).isCentered()) {
					stableCount++;
				} else {
					stableCount = 0;
				}
				segment.add(segment.get(pos).getChildren().get(i));
				getSegmentFromIntersection(segment, segment.size() - 1, stableCount);
			}
		}
		if (segment.getLast().getPoint().equals(new Point(-5, -5))) {
			return segment;
		} else {
			return null;
		}
	}

	// public LinkedList<Point> getPointsAt(int unitsAway) {
	// return new LinkedList<Point>();
	// }
	// public LinkedList<Point> getPointsAtSmallAngle(int unitsAway) {
	// return new LinkedList<Point>();
	// }
	//
	// public Point getOppisite(Point temp) {
	// return new Point();
	// }
	// public Point getPointAt(int pos){
	// return new Point();
	// }

	// int shortestLeft=-1;
	// int shortestRight=-1;
	// int leftPos=-1;
	// int rightPos=-1;
	// for(int i=0;i<vectorCount;i++){
	// if(leftVector[i]!=-1){
	// if(shortestLeft==-1||leftVector[i]<shortestLeft){
	// shortestLeft=leftVector[i];
	// leftPos=i;
	// }
	// }
	// if(rightVector[i]!=-1){
	// if(shortestRight==-1||rightVector[i]<shortestRight){
	// shortestRight=rightVector[i];
	// rightPos=i;
	// }
	// }
	// }
	// if(rightPos!=-1&&leftPos!=-1){
	// tempOrthoLine=new OrthogonalLine(axisPoint,vectors.getPointAtIndex(leftPos),
	// vectors.getPointAtIndex(rightPos),shortestLeft,shortestRight);
	// tempOrthoLine.setTwoLines(true);
	// }
	// else if(rightPos!=-1){
	// tempOrthoLine=new OrthogonalLine();
	// tempOrthoLine.setTwoLines(true);
	// tempOrthoLine.setLowerPoint(vectors.getPointAtIndex(rightPos));
	// tempOrthoLine.setLowerDistance(shortestRight);
	//
	// }
	// else if(leftPos!=-1){
	// tempOrthoLine=new OrthogonalLine();
	// tempOrthoLine.setTwoLines(true);
	// tempOrthoLine.setUpperPoint(vectors.getPointAtIndex(leftPos));
	// tempOrthoLine.setUpperDistance(shortestLeft);
	// }
	//
	// return tempOrthoLine;
}
