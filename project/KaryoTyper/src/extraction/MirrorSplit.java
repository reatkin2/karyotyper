package extraction;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import basic_objects.CutStartPoint;
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

	// TODO(aamcknig): make this so the projection sticks to the far side of the intersection

	
	
	
	// TODO(aamcknig): need to have two types of stop points where
	// the chromosome comes to an end or you reach another stable part
	// of the chromsome where the width on both sides of the medial axis
	// are half the width of the chromosome
	public LinkedList<Point> markSplit(Rectangle bounds, LinkedList<CutStartPoint> startPoints) {
		double upperDistanceAvg = -1;
		double lowerDistanceAvg = -1;
		int stableCount = 0;
		final double percentChromoWidthToCheckOutTo = 1.07;
		final double minCromoWidthPercent = .6;
		boolean firstMarking = true;
		Vertex nextPoint;
		Vector nextVector = null;
		Vector projectionAvg=null;
		int projectionCount=0;
		Point currShortPoint = new Point(-1, -1);
		LinkedList<OrthogonalLine> orthoList = new LinkedList<OrthogonalLine>();
		LinkedList<Point> cutList = new LinkedList<Point>();
		for (int i = 0; i < startPoints.size(); i++) {
			nextPoint = startPoints.get(i).getStartPoint();
			nextVector = null;
			firstMarking = true;
			boolean lastStable = false;
			OrthogonalLine tempOrtho = null;
			OrthogonalLine lastOrtho = null;
			while (firstMarking || !isProjectionEnd(bounds, nextPoint, nextVector)) {
				firstMarking = false;
				boolean widthReached = false;
				boolean marked = false;
				// use small angle rather than 360
				// if (lastStable) {
				// tempOrtho = getShortestDistance(bounds, tempOrtho.getUpperPoint(),
				// nextPoint.getPoint(), this.medialAxisGraph.getChromoWidth()
				// * percentChromoWidthToCheckOutTo,
				// this.medialAxisGraph.getDistanceMap());
				// System.out.println("small range index: "+tempOrtho.getIndex());
				// if (tempOrtho.getIndex() > 3 || tempOrtho.getIndex() < 1) {
				// lastStable = false;
				// }
				//
				// }
				// // use 360 angle
				// else {
				if(stableCount<3){
					tempOrtho=this.getShortestDistance(bounds, new Point(-1,-1), nextPoint.getPoint(),
							this.medialAxisGraph.getChromoWidth(),
							this.medialAxisGraph.getDistanceMap());
				}
				else{
					tempOrtho =this.getPathOrtho(bounds, nextPoint.getChildren().get(0).getPoint(), nextPoint.getPoint(),
							this.medialAxisGraph.getChromoWidth(),
							this.medialAxisGraph.getDistanceMap());
				}
				lastStable = true;
				// }
				if (tempOrtho != null) {
					lastOrtho = tempOrtho;
					//if we got both sides
					if (tempOrtho.isTwoLines()) {
						double distance = tempOrtho.getlength();
						//if its the first 3
						//establish an average
						if (stableCount < 3
								&& (distance < (this.medialAxisGraph.getChromoWidth() * percentChromoWidthToCheckOutTo) && distance > this.medialAxisGraph
										.getChromoWidth() * minCromoWidthPercent)) {
							if (upperDistanceAvg == -1) {
								upperDistanceAvg = tempOrtho.getlength();
							} else {
								upperDistanceAvg = ((upperDistanceAvg * stableCount) + tempOrtho
										.getlength()) / (stableCount + 1);
							}
							stableCount++;
							widthReached = true;
							marked = true;
							//if we are inside the average width
						} else if ((distance < (upperDistanceAvg * percentChromoWidthToCheckOutTo) && distance > upperDistanceAvg
								* minCromoWidthPercent)) {
							stableCount++;
							widthReached = true;
							marked = true;
						}
					}
					// if we are stable and dont have both lines inside the width of chromosome
					// use last good
					// draw cutline on one side
					if (!widthReached) {
						//if we got both lines stick to the one farthest from the intersection
						//TODO(aamcknig):debug changes here
						if (tempOrtho.isTwoLines()
								&& (tempOrtho.getUpperPoint().distance(startPoints.get(i).getIntersection().getPoint())
										< tempOrtho.getLowerPoint().distance(startPoints.get(i).getIntersection().getPoint()))) {
							double lowerPercent=tempOrtho.getLowerPoint().distance(tempOrtho.getCenterPoint())/upperDistanceAvg;
							double lowerChangeX =( tempOrtho.getLowerPoint().x
									- tempOrtho.getCenterPoint().x);
							double lowerChangeY =( tempOrtho.getLowerPoint().y
									- tempOrtho.getCenterPoint().y);	
							double changeX=(lowerChangeX/lowerPercent)-lowerChangeX;
							double changeY=(lowerChangeY/lowerPercent)-lowerChangeY;
							tempOrtho.setUpperPoint(new Point((int)Math.round(tempOrtho.getCenterPoint().x
									- changeX), (int)Math.round(tempOrtho.getCenterPoint().y - changeY)));
							if (bounds.contains(tempOrtho.getUpperPoint())) {
								cutList.add(tempOrtho.getUpperPoint());
							}
							
						} else {//if we got one line use the avearge from the edge point
							//to make the center point and double it over to make the cutine
							//TODO(aamcknig):debug changes here
							double upperPercent=tempOrtho.getUpperPoint().distance(tempOrtho.getCenterPoint())/upperDistanceAvg;
							double upperChangeX = (tempOrtho.getUpperPoint().x
									- tempOrtho.getCenterPoint().x)*1.14;
							double upperChangeY = (tempOrtho.getUpperPoint().y
									- tempOrtho.getCenterPoint().y)*1.14;
							double changeX=(upperChangeX/upperPercent)-upperChangeX;
							double changeY=(upperChangeY/upperPercent)-upperChangeY;

							tempOrtho.setLowerPoint(new Point((int)Math.round(tempOrtho.getCenterPoint().x
									- changeX), (int)Math.round(tempOrtho.getCenterPoint().y - changeY)));
							if (bounds.contains(tempOrtho.getLowerPoint())) {
								cutList.add(tempOrtho.getLowerPoint());
							}
						}
						lastStable = false;
						marked = true;
					}
				}
				if (!marked) {
					// mark both sides for cut
					tempOrtho = new OrthogonalLine(nextPoint.getPoint(), new Point(
							(int) Math.round(lastOrtho.getUpperPoint().x + nextVector.x),
							(int) Math.round(lastOrtho.getUpperPoint().y + nextVector.y)),
							new Point((int) Math.round(lastOrtho.getLowerPoint().x + nextVector.x),
									(int) Math.round(lastOrtho.getLowerPoint().y + nextVector.y)),
							lastOrtho.getUpperInt(), lastOrtho.getLowerInt(), lastOrtho.getIndex());
					if (bounds.contains(tempOrtho.getLowerPoint())) {
						cutList.add(tempOrtho.getLowerPoint());
					}
					if (bounds.contains(tempOrtho.getUpperPoint())) {
						cutList.add(tempOrtho.getUpperPoint());
					}
					lastStable = false;
					marked = true;

				}
				// center and move foward
				nextVector = getNextVector(tempOrtho, nextPoint);
				if(projectionCount==0){
					projectionAvg=nextVector;
				}
				else{
					double xAvg = ((projectionAvg.x * projectionCount) + nextVector.x)
							/ (projectionCount + 1);
					double yAvg = ((projectionAvg.y * projectionCount) + nextVector.y)
							/ (projectionCount + 1);
					projectionAvg.setLocation(xAvg, yAvg);

				}
				projectionCount++;
				nextPoint = getNextPoint(nextPoint.getPoint(), tempOrtho.getUpperPoint(),
						tempOrtho.getLowerPoint(), nextVector);
				orthoList.addFirst(tempOrtho);
				System.out.println("top: " + tempOrtho.getUpperPoint() + " center: "
						+ tempOrtho.getCenterPoint() + " bottom: " + tempOrtho.getLowerPoint());
				System.out.println("Current point along Chromosomes: "
						+ nextPoint.getPoint().toString() + " projectedPoint:  "
						+ nextPoint.getChildren().get(0).getPoint());
			}
		}

		return cutList;
	}

	public boolean isProjectionEnd(Rectangle bounds, Vertex checkPoint, Vector nextVector) {
		int x = (int) Math.round((double) checkPoint.getPoint().x + (4.5 * nextVector.x));
		int y = (int) Math.round((double) checkPoint.getPoint().y + (4.5 * nextVector.y));
		Point tempPoint = new Point(x, y);
		if (bounds.contains(tempPoint)) {
			if (this.medialAxisGraph.getDistanceMap().getDistanceFromEdge(tempPoint) < 1) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	public Vertex getNextPoint(Point currPoint, Point top, Point bottom, Vector Next) {
		double centerX = (top.x + bottom.x) / 2;
		double centerY = (top.y + bottom.y) / 2;
		centerX += Next.x;
		centerY += Next.y;
		Point nextPnt = new Point((int) Math.round(centerX), (int) Math.round(centerY));
		Point childPnt = new Point((int) Math.round(centerX + Next.x), (int) Math.round(centerY
				+ Next.y));
		Vertex next = new Vertex(nextPnt, 0);
		next.addChild(new Vertex(childPnt, 0));
		return next;
	}

	public Vector getNextVector(OrthogonalLine tempOrtho, Vertex currPoint) {
		Vector upper = new Vector(tempOrtho.getUpperPoint().x - tempOrtho.getCenterPoint().x,
				tempOrtho.getUpperPoint().y - tempOrtho.getCenterPoint().y);
		Vector ninty = Vector.multiply(Vector.normalize(Vector.rotateVector(upper, 1.57079633)),
				1.7);
		Vector twoSeventy = Vector.multiply(
				Vector.normalize(Vector.rotateVector(upper, 4.71238898)), 1.7);
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
	public OrthogonalLine getPathOrtho(Rectangle bounds, Point endPoint, Point centerPoint,
			double checkDistance, DistanceMap distanceMap) {// throws Exception
		boolean foundShortest = false;
		OrthogonalLine tempOrthoHalf = null;
		OrthogonalLine tempOrthoLine = null;
		int vectorCount = 40;
		double shortest = -1;
		Point shortestSideLeft = new Point(-1, -1);
		Point shortestSideRight = new Point(-1, -1);
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
			vectorCount = 40;
			vectors = new RadialVectors(centerPoint, vectorCount, (double) i);
			pointList = vectors.getPointsInRange(endPoint, 360, vectorCount);

			int middle=vectorCount/2;
			for (int j = 1; j < middle-1; j++) {
				// check if left side has passed edge of chromosome//upper
				if (bounds.contains(pointList.get(j))
						&& distanceMap.getDistanceFromEdge(pointList.get(j)) <= 0) {
					if (leftVector[j] == -1) {
						leftVector[j] = i;
						leftPoints[j] = pointList.get(j);
						if (shortestSideLeft.x == -1) {
							shortestSideLeft = pointList.get(j);
							if(shortestSideRight.x!=-1){
								tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
										shortestSideRight, leftVector[j], -1, j);
							}
							else{
							tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
									pointList.get(j+middle), leftVector[j], -1, j);
							}
						} else if (pointList.get(j).distance(centerPoint) < (centerPoint
								.distance(shortestSideLeft))) {
							shortestSideLeft = pointList.get(j);
							if(tempOrthoHalf!=null){
								if(tempOrthoHalf.getLowerPoint().x!=-1){
									tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
											tempOrthoHalf.getLowerPoint(),leftVector[j],tempOrthoHalf.getLowerInt(),  j);	

								}
								else{
									tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
											pointList.get(j+middle), leftVector[j],-1, j);
								}
							}
							else{
								tempOrthoHalf = new OrthogonalLine(centerPoint, pointList.get(j),
										pointList.get(j+middle), leftVector[j],-1, j);
							}
						}
					}
				}
				// check if right side has passed edge of chromosome//lower
				if (bounds.contains(pointList.get(j+middle))
						&& distanceMap.getDistanceFromEdge(pointList.get(j+middle)) <= 0) {
					if (rightVector[j] == -1) {
						rightVector[j] = i;
						rightPoints[j] = pointList.get(j+middle);
						if (shortestSideRight.x == -1) {
							shortestSideRight = pointList.get(j+middle);
							if(shortestSideLeft.x!=-1){
								tempOrthoHalf = new OrthogonalLine(centerPoint, shortestSideLeft,
										rightPoints[j], -1, rightVector[j], j);
							}
							else{
								tempOrthoHalf = new OrthogonalLine(centerPoint, rightPoints[j],
										pointList.get(j+middle), rightVector[j], -1, j);
							}
						} else if (pointList.get(j+middle).distance(centerPoint) < (centerPoint
								.distance(shortestSideRight))) {
							shortestSideRight = pointList.get(j+middle);
							if(tempOrthoHalf!=null){
								if(tempOrthoHalf.getUpperPoint().x!=-1){
									tempOrthoHalf = new OrthogonalLine(centerPoint, tempOrthoHalf.getUpperPoint(),
											rightPoints[j], tempOrthoHalf.getUpperInt(),rightVector[j],  j);	
								}
								else{
									tempOrthoHalf = new OrthogonalLine(centerPoint, pointList.get(j),
											rightPoints[j], -1, rightVector[j], j);
								}
							}
							else{
								tempOrthoHalf = new OrthogonalLine(centerPoint, pointList.get(j),
										rightPoints[j], -1, rightVector[j], j);
							}

						}
					}
				}
			}
			if (shortestSideRight.x!=-1&&shortestSideLeft.x!=-1) {// if(shortestTill==-1||shortest<shortestTill){
				foundShortest = true;
				tempOrthoHalf.setTwoLines(true);
				return tempOrthoHalf;
			}
		}
		return tempOrthoHalf;
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
				vectorCount = 5;
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

	public LinkedList<CutStartPoint> getStartPoints(LinkedList<Vertex> graph,
			LinkedList<Vertex> intersections) {
		LinkedList<CutStartPoint> startPoints = new LinkedList<CutStartPoint>();
		for (int i = 0; i < intersections.size(); i++) {
			for (int j = 0; j < intersections.get(i).getChildren().size(); j++) {
				LinkedList<Vertex> segment = new LinkedList<Vertex>();
				segment.add(intersections.get(i).getChildren().get(j));
				segment = this.getSegmentFromIntersection(segment, 0, 0);
				if (segment != null) {
					Vertex start = new Vertex(segment.get(segment.size() - 3).getPoint(), 0);
					start.addChild(segment.get(segment.size() - 4));
					startPoints.add(new CutStartPoint(start,intersections.get(i)));
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
