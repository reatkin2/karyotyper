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

	// TODO(aamcknig): right a method to project to end point
	// and project both sides of the end point till crosses current
	public LinkedList<Point> markSplit(Rectangle bounds, LinkedList<CutStartPoint> startPoints) {
		double avgWidth = -1;
		int stableCount = 0;
		final double percentChromoWidthToCheckOutTo = 1.07;
		final double minCromoWidthPercent = .6;
		boolean firstMarking = true;
		Vertex nextPoint;
		Vector nextVector = null;
		Vector projectionAvg = null;
		int projectionCount = 0;
		LinkedList<OrthogonalLine> orthoList = new LinkedList<OrthogonalLine>();
		LinkedList<Point> cutList = new LinkedList<Point>();
		//TODO(aamcknig): find out why first two start points are the same
		for (int i = 0; i < startPoints.size(); i++) {
			nextPoint = startPoints.get(i).getStartPoint();
			nextVector = null;
			firstMarking = true;
			OrthogonalLine tempOrtho = null;
			OrthogonalLine lastOrtho = null;
			boolean enteringCross=false;
			boolean exitingCross=false;
			int inCrossCount=0;
			double angle=-1;
			while (firstMarking
					|| !isProjectionEnd(bounds, this.medialAxisGraph.getDistanceMap(), nextPoint,
							nextVector)) {
				firstMarking = false;
				boolean widthReached = false;
				boolean marked = false;
				if(enteringCross){
					if(exitingCross){
						if(inCrossCount==0){
							enteringCross=false;
							exitingCross=false;
						}
						else{
							inCrossCount--;
						}
					}
					else{
						inCrossCount++;
					}
				}
				// Possible options check angle between orthoUpper lower
				// use this to
				if (stableCount < 3) {
					tempOrtho = this.getShortestDistance(bounds, new Point(-1, -1),
							nextPoint.getPoint(), this.medialAxisGraph.getChromoWidth(),
							this.medialAxisGraph.getDistanceMap());
					if(tempOrtho!=null){
						angle = this.angleBetween(tempOrtho);
					}
					if (avgWidth == -1) {
						avgWidth = tempOrtho.getlength();
					} else {
						avgWidth = ((avgWidth * stableCount) + tempOrtho.getlength())
								/ (stableCount + 1);
					}
					stableCount++;
					widthReached = true;
					marked = true;

				} else {
					tempOrtho = this.getPathOrtho(bounds,
							nextPoint.getChildren().get(0).getPoint(), nextPoint.getPoint(),
							this.medialAxisGraph.getChromoWidth(),
							this.medialAxisGraph.getDistanceMap());
					if (tempOrtho!=null&&tempOrtho.isTwoLines()) {
						angle = this.angleBetween(tempOrtho);
						if (angle > 3.082 && angle < 3.2) {
							tempOrtho = this.getShortestDistance(bounds, new Point(-1, -1),
									nextPoint.getPoint(), this.medialAxisGraph.getChromoWidth(),
									this.medialAxisGraph.getDistanceMap());
							if(tempOrtho!=null){
								angle = this.angleBetween(tempOrtho);
							}
							stableCount++;
							widthReached = true;
							marked = true;
						}
						else if(angle>=3.2&&inCrossCount==0){
							enteringCross=true;
							inCrossCount++;
						}
						else if(enteringCross && inCrossCount>2){
							exitingCross=true;
							inCrossCount--;
						}
					}
					else if(tempOrtho!=null&&lastOrtho!=null&& !enteringCross){//if only one side is touching edge
						if(tempOrtho.getLowerInt()!=-1){
							if(lastOrtho.getLowerInt()!=-1){
								if(lastOrtho.getLowerPoint().equals(tempOrtho.getLowerPoint())){
									enteringCross=true;
									inCrossCount++;
								}
							}
						}
						else{
							if(lastOrtho.getUpperInt()!=-1){
								if(lastOrtho.getUpperPoint().equals(tempOrtho.getUpperPoint())){
									enteringCross=true;
									inCrossCount++;
								}
							}

						}
					}
				}
				// }
				if (tempOrtho != null&&(!enteringCross||exitingCross)) {

					// if we got both sides
					if (!widthReached) {
						// if we got both lines stick to the one farthest from the intersection
						// center and double over the far point and draw cutline on intersection
						// side
						double distanceUpperToStart = tempOrtho.getUpperPoint().distance(
								startPoints.get(i).getStartPoint().getPoint());
						double distanceLowerToStart = tempOrtho.getLowerPoint().distance(
								startPoints.get(i).getStartPoint().getPoint());

						if (tempOrtho.isTwoLines() {
							//TODO(aamcknig):fixe the problem with not checking if the length of 
							// a side is longer than the average width, forcing the whole thing to the side
						
							// pick the farthest from start point
							// startPoints.get(i).getStartPoint()
							if (distanceUpperToStart < distanceLowerToStart) {
								tempOrtho.setUpperPoint(this.recenterForUpper(tempOrtho, avgWidth));
								if (bounds.contains(tempOrtho.getUpperPoint())) {
									cutList.add(tempOrtho.getUpperPoint());
								}

							} else {
								tempOrtho.setLowerPoint(this.recenterForLower(tempOrtho, avgWidth));
								if (bounds.contains(tempOrtho.getLowerPoint())) {
									cutList.add(tempOrtho.getLowerPoint());
								}
							}
						} else {

							if (tempOrtho.getLowerInt() != -1) {
								tempOrtho.setUpperPoint(this.recenterForUpper(tempOrtho, avgWidth));
								if (bounds.contains(tempOrtho.getUpperPoint())) {
									cutList.add(tempOrtho.getUpperPoint());
								}

							} else {
								tempOrtho.setLowerPoint(this.recenterForLower(tempOrtho, avgWidth));
								if (bounds.contains(tempOrtho.getLowerPoint())) {
									cutList.add(tempOrtho.getLowerPoint());
								}
							}
						}
						marked = true;
					}
				}
				if (!marked) {
					// mark both sides for cut
					if(!enteringCross&&inCrossCount==0){
						enteringCross=true;
					}
					if(lastOrtho!=null){
						tempOrtho = new OrthogonalLine(nextPoint.getPoint(), new Point(
								(int) Math.round(lastOrtho.getUpperPoint().x + projectionAvg.x),
								(int) Math.round(lastOrtho.getUpperPoint().y + projectionAvg.y)),
								new Point((int) Math.round(lastOrtho.getLowerPoint().x + projectionAvg.x),
										(int) Math.round(lastOrtho.getLowerPoint().y + projectionAvg.y)),
								lastOrtho.getUpperInt(), lastOrtho.getLowerInt(), lastOrtho.getIndex());

						if (bounds.contains(tempOrtho.getLowerPoint())) {
							cutList.add(tempOrtho.getLowerPoint());
						}
						if (bounds.contains(tempOrtho.getUpperPoint())) {
							cutList.add(tempOrtho.getUpperPoint());
						}
						marked = true;
					}
					else if(tempOrtho!=null){
						if (bounds.contains(tempOrtho.getLowerPoint())) {
							cutList.add(tempOrtho.getLowerPoint());
						}
						if (bounds.contains(tempOrtho.getUpperPoint())) {
							cutList.add(tempOrtho.getUpperPoint());
						}
					}
					else{
						try {
							throw new Exception("lastOrtho and tempOrtho both null");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println(e);
						}
					}

				}
				// center and move foward
				nextVector = getNextVector(tempOrtho, nextPoint);
				if (projectionCount == 0) {
					projectionAvg = nextVector;
				} else {
					double xAvg = ((projectionAvg.x * projectionCount) + nextVector.x)
							/ (projectionCount + 1);
					double yAvg = ((projectionAvg.y * projectionCount) + nextVector.y)
							/ (projectionCount + 1);
					projectionAvg.setLocation(xAvg, yAvg);

				}
				projectionCount++;

				// TODO(aamcknig): cant use get next point getting the center
				// between the upper and lower because they are not along the
				// same line and thus are not going to be the center to project
				// from this is probably while it currently gets stuck in a loop
				nextPoint = getNextPoint(nextPoint.getPoint(), tempOrtho.getUpperPoint(),
						tempOrtho.getLowerPoint(), nextVector);
				orthoList.addFirst(tempOrtho);
//				System.out.println("top: " + tempOrtho.getUpperPoint() + " center: "
//						+ tempOrtho.getCenterPoint() + " bottom: " + tempOrtho.getLowerPoint());
//				System.out.println("Current point along Chromosomes: "
//						+ nextPoint.getPoint().toString() + " projectedPoint:  "
//						+ nextPoint.getChildren().get(0).getPoint());
				lastOrtho = tempOrtho;
			}
			LinkedList<Point> tempCut=finishTip(bounds,nextPoint, projectionAvg,
					this.medialAxisGraph.getChromoWidth(),
					this.medialAxisGraph.getDistanceMap());
			cutList.addAll(tempCut);
		}

		return cutList;
	}

	public Point addVector(Point tempPoint, Vector addVector) {
		return new Point((int) Math.round(tempPoint.x + addVector.x), (int) Math.round(tempPoint.y
				+ addVector.y));
	}

	public Vector getVector(Point from, Point toThis) {
		return new Vector(toThis.x - from.x, toThis.y - from.y);
	}

	public LinkedList<Point> finishTip(Rectangle bounds, Vertex currPoint, Vector projection,
			 double checkDistance,DistanceMap distanceMap) {
		LinkedList<Point> cutList = new LinkedList<Point>();
		int vectorCount = 40;
		int leftVector[] = new int[vectorCount];
		int rightVector[] = new int[vectorCount];
		Point leftPoints[] = new Point[vectorCount];
		Point rightPoints[] = new Point[vectorCount];
		Point endPoint = addVector(currPoint.getPoint(), projection);
		int countDown = 40;
		int middle = vectorCount / 2;
		// init array
		for (int i = 0; i < vectorCount; i++) {
			leftVector[i] = -1;
			rightVector[i] = -1;
		}
		RadialVectors vectors=null;
		// move out from axisPoint tell we run off distance map both sides
		for (int i = 1; countDown > -1 && i <= checkDistance; i++) {
			ArrayList<Point> pointList = new ArrayList<Point>();
			vectorCount = 40;
			vectors = new RadialVectors(currPoint.getPoint(), vectorCount, (double) i);
			pointList = vectors.getPointsInRange(endPoint, Math.PI * 1.05, vectorCount);

			for (int j = 1; countDown > -1 && j < middle; j++) {
				if (bounds.contains(pointList.get(middle + j))
						&& distanceMap.getDistanceFromEdge(pointList.get(middle + j)) <= 0) {
					if (leftVector[j] == -1) {
						leftVector[j] = i;
						leftPoints[j] = pointList.get(middle + j);
						countDown--;
					}
				}

				if (bounds.contains(pointList.get(middle - j))
						&& distanceMap.getDistanceFromEdge(pointList.get(middle - j)) <= 0) {
					if (rightVector[j] == -1) {
						rightVector[j] = i;
						rightPoints[j] = pointList.get(middle - j);
						countDown--;
					}
				}

			}
		}
		for (int i = 1; i < middle; i++) {
			if (leftVector[i] == -1) {
				if (rightVector[i] != -1) {
					Vector tempVect = this.getVector(currPoint.getPoint(), rightPoints[i]);
					cutList.add(this.addVector(currPoint.getPoint(),
							Vector.rotateVector(tempVect, vectors.getStepTheta() * i * 2)));
				}
			} else if (rightVector[i] == -1) {
				if (leftVector[i] != -1) {
					Vector tempVect = this.getVector(currPoint.getPoint(), leftPoints[i]);
					cutList.add(this.addVector(currPoint.getPoint(),
							Vector.rotateVector(tempVect, vectors.getStepTheta() * i * -2)));
				}
			}
		}
		return cutList;
	}

	public Point recenterForUpper(OrthogonalLine ortho, double avgWidth) {
		double lowerPercent = ortho.getLowerPoint().distance(ortho.getCenterPoint()) / avgWidth;
		double lowerChangeX = (ortho.getLowerPoint().x - ortho.getCenterPoint().x);
		double lowerChangeY = (ortho.getLowerPoint().y - ortho.getCenterPoint().y);
		double changeX = (lowerChangeX / lowerPercent) - lowerChangeX;
		double changeY = (lowerChangeY / lowerPercent) - lowerChangeY;

		return new Point((int) Math.round(ortho.getCenterPoint().x - changeX),
				(int) Math.round(ortho.getCenterPoint().y - changeY));
	}

	public Point recenterForLower(OrthogonalLine ortho, double avgWidth) {
		double upperPercent = ortho.getUpperPoint().distance(ortho.getCenterPoint()) / avgWidth;
		double upperChangeX = (ortho.getUpperPoint().x - ortho.getCenterPoint().x);
		double upperChangeY = (ortho.getUpperPoint().y - ortho.getCenterPoint().y);
		double changeX = (upperChangeX / upperPercent) - upperChangeX;
		double changeY = (upperChangeY / upperPercent) - upperChangeY;

		return new Point((int) Math.round(ortho.getCenterPoint().x - changeX),
				(int) Math.round(ortho.getCenterPoint().y - changeY));
	}

	public boolean isProjectionEnd(Rectangle bounds, DistanceMap distanceMap, Vertex checkPoint,
			Vector nextVector) {
		int x = (int) Math.round((double) checkPoint.getPoint().x + (6 * nextVector.x));
		int y = (int) Math.round((double) checkPoint.getPoint().y + (6 * nextVector.y));
		Point tempPoint = new Point(x, y);
		if (bounds.contains(tempPoint)) {
			if (distanceMap.getDistanceFromEdge(tempPoint) < 1) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	public double angleBetween(OrthogonalLine ortho) {
		Vector v1 = new Vector(ortho.getUpperPoint().x - ortho.getCenterPoint().x,
				ortho.getUpperPoint().y - ortho.getCenterPoint().y);
		Vector v2 = new Vector(ortho.getLowerPoint().x - ortho.getCenterPoint().x,
				ortho.getLowerPoint().y - ortho.getCenterPoint().y);

		return Vector.angleBetween(v1, v2);
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
		for (int i = 1; !foundShortest && i <= checkDistance; i++) {
			ArrayList<Point> pointList = new ArrayList<Point>();
			vectorCount = 40;
			vectors = new RadialVectors(centerPoint, vectorCount, (double) i);
			pointList = vectors.getPointsInRange(endPoint, Math.PI * 1.05, vectorCount);

			int middle = vectorCount / 2;
			for (int j = 14; j < middle; j++) {
				// check if left side has passed edge of chromosome//upper
				if (bounds.contains(pointList.get(middle + j))
						&& distanceMap.getDistanceFromEdge(pointList.get(middle + j)) <= 0) {
					if (leftVector[j] == -1) {
						leftVector[j] = i;
						leftPoints[j] = pointList.get(middle + j);
						if (shortestSideLeft.x == -1) {
							shortestSideLeft = pointList.get(middle + j);
							if (tempOrthoHalf != null && tempOrthoHalf.getLowerInt() != -1) {
								tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
										tempOrthoHalf.getLowerPoint(), leftVector[j],
										tempOrthoHalf.getLowerInt(), j);
							} else {
								tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
										pointList.get(middle - j), leftVector[j], -1, j);
							}
						} else if (pointList.get(middle + j).distance(centerPoint) < (centerPoint
								.distance(shortestSideLeft))) {
							shortestSideLeft = pointList.get(middle + j);
							if (tempOrthoHalf != null) {
								if (tempOrthoHalf.getLowerInt() != -1) {
									tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
											tempOrthoHalf.getLowerPoint(), leftVector[j],
											tempOrthoHalf.getLowerInt(), j);

								} else {
									tempOrthoHalf = new OrthogonalLine(centerPoint, leftPoints[j],
											pointList.get(middle - j), leftVector[j], -1, j);
								}
							} else {
								tempOrthoHalf = new OrthogonalLine(centerPoint,
										pointList.get(middle + j), pointList.get(middle - j),
										leftVector[j], -1, j);
							}
						}
					}
				}
				// check if right side has passed edge of chromosome//lower
				if (bounds.contains(pointList.get(middle - j))
						&& distanceMap.getDistanceFromEdge(pointList.get(middle - j)) <= 0) {
					if (rightVector[j] == -1) {
						rightVector[j] = i;
						rightPoints[j] = pointList.get(middle - j);
						if (shortestSideRight.x == -1) {
							shortestSideRight = pointList.get(middle - j);
							if (tempOrthoHalf != null && tempOrthoHalf.getUpperInt() != -1) {
								tempOrthoHalf = new OrthogonalLine(centerPoint,
										tempOrthoHalf.getUpperPoint(), rightPoints[j],
										tempOrthoHalf.getLowerInt(), rightVector[j], j);
							} else {
								tempOrthoHalf = new OrthogonalLine(centerPoint,
										pointList.get(middle + j), rightPoints[j], -1,
										rightVector[j], j);
							}
						} else if (pointList.get(middle - j).distance(centerPoint) < (centerPoint
								.distance(shortestSideRight))) {
							shortestSideRight = pointList.get(middle - j);
							if (tempOrthoHalf != null) {
								if (tempOrthoHalf.getUpperInt() != -1) {
									tempOrthoHalf = new OrthogonalLine(centerPoint,
											tempOrthoHalf.getUpperPoint(), rightPoints[j],
											tempOrthoHalf.getUpperInt(), rightVector[j], j);
								} else {
									tempOrthoHalf = new OrthogonalLine(centerPoint,
											pointList.get(middle + j), rightPoints[j], -1,
											rightVector[j], j);
								}
							} else {
								tempOrthoHalf = new OrthogonalLine(centerPoint,
										pointList.get(middle + j), rightPoints[j], -1,
										rightVector[j], j);
							}

						}
					}
				}
			}
			if (shortestSideRight.x != -1 && shortestSideLeft.x != -1) {// if(shortestTill==-1||shortest<shortestTill){
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
				//TODO(aamcknig):find out why first 3 startpoints are the same
		for (int i = 0; i < intersections.size(); i++) {
			for (int j = 0; j < intersections.get(i).getChildren().size(); j++) {
				LinkedList<Vertex> segment = new LinkedList<Vertex>();
				segment.add(intersections.get(i).getChildren().get(j));
				segment = this.getSegmentFromIntersection(segment, 0, 0);
				if (segment != null) {
					Vertex start = new Vertex(segment.get(segment.size() - 3).getPoint(), 0);
					start.addChild(segment.get(segment.size() - 4));
					if(!containsVertex(startPoints,start)){
						startPoints.add(new CutStartPoint(start, intersections.get(i)));
					}
				}

			}
		}
		return startPoints;
	}
	public boolean containsVertex(LinkedList<CutStartPoint> points,Vertex vert){
		for(int i=0;i<points.size();i++){
			if(points.get(i).getStartPoint().equals(vert)){
				return true;
			}
		}
		return false;
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
