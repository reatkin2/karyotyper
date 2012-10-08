package medial_axis;

import java.awt.Point;
import java.util.LinkedList;

import basic_objects.AroundPixel;
import basic_objects.Cluster;
import basic_objects.ErosionPoint;
import basic_objects.Vertex;
import chromosome.ChromosomeCluster;
import chromosome.GeneticSlideImage;

public class MedialAxis {
	private LinkedList<Point> skeleton;
	private double objectWidth[];
	private DistanceMap distanceMap;
	private int biggestIncreaseSkeletonAtWidthCount;
	private int most2LeastRemovedAtWidthCount;

	public MedialAxis() {
		initMedialAxis();
	}

	public MedialAxis(MedialAxis newAxis) {
		initMedialAxis();
		copyMedialAxis(newAxis);
	}

	public MedialAxis(Cluster myCluster, GeneticSlideImage image) {
		initMedialAxis();
		createSkeleton(myCluster, image);
	}

	private void initMedialAxis() {
		distanceMap = new DistanceMap();
		skeleton = new LinkedList<Point>();
		biggestIncreaseSkeletonAtWidthCount = -1;
		most2LeastRemovedAtWidthCount = 1;
		objectWidth = new double[2];
		objectWidth[0] = -1;
		objectWidth[1] = -1;
	}

	public void setMedialAxis(LinkedList<Point> tempMedialAxis) {
		this.skeleton = tempMedialAxis;
	}

	private void copyMedialAxis(MedialAxis copyMedialAxis) {
		this.distanceMap = new DistanceMap(copyMedialAxis.distanceMap);
		this.skeleton = copyMedialAxis.getMedialAxisPoints();
		this.biggestIncreaseSkeletonAtWidthCount = copyMedialAxis.biggestIncreaseSkeletonAtWidthCount;
		this.most2LeastRemovedAtWidthCount = copyMedialAxis.most2LeastRemovedAtWidthCount;
		objectWidth[0] = copyMedialAxis.objectWidth[0];
		objectWidth[1] = copyMedialAxis.objectWidth[1];

	}

	/**
	 * this returns an array of two integers that represent the width of the chromosome found when
	 * doing erosion/distance map by two different methods
	 * 
	 * @return the width
	 */
	public double[] getObjectWidth() {
		return objectWidth;
	}

	public LinkedList<Point> getMedialAxisPoints() {
		return this.skeleton;
	}

	public DistanceMap getDistanceMap() {
		return distanceMap;
	}

	/**
	 * this does the erosion and distanceMap creation similtaniously
	 * 
	 * @param myCluster
	 *            the cluster to get the medial Axis of
	 * @param img
	 *            the image to the cluster is from
	 */
	public void createSkeleton(Cluster myCluster, GeneticSlideImage image) {
		// create a linked list to store Clusters in
		LinkedList<ErosionPoint> removeEdgePointsVert = new LinkedList<ErosionPoint>();
		LinkedList<ErosionPoint> removeEdgePointsHorz = new LinkedList<ErosionPoint>();
		this.skeleton = new LinkedList<Point>();
//		AroundPixel aroundPixel=new AroundPixel();
		LinkedList<Point> addThisRound = new LinkedList<Point>();
		Cluster temp = new Cluster(myCluster);
		this.distanceMap = new DistanceMap(myCluster.getSize().x, myCluster.getSize().y);
		ErosionPoint pointRightEdge = new ErosionPoint(0, 0,false);
		int objectWidth = 0;
		boolean inObject = false;
		boolean beforeObjectEdge = true;
		int lastPixelCount = temp.getPixelCount();
		int lastSkeletonCount = -1;
		int distanceFromEdgeCount = 0;
		int removedLastTime = 0;
		int most2LeastRemoved = 0;
		int skeletonSizeLastTime = 0;
		int biggestIncreaseInSkeleton = 0;
		while (skeleton.size() > lastSkeletonCount || lastPixelCount != temp.getPixelCount()) {
			lastSkeletonCount = skeleton.size();
			lastPixelCount = temp.getPixelCount();
			// loop width
			for (int i = 0; i < temp.getSize().x; i++) {
				// loop height
				for (int j = 0; j < temp.getSize().y; j++) {
					if (temp.getPos(i, j)) {
						if (beforeObjectEdge) {
							removeEdgePointsVert.push(new ErosionPoint(i, j,true));
						}
						objectWidth++;
						beforeObjectEdge = false;
						inObject = true;
						pointRightEdge = new ErosionPoint(i, j,false);
					} else {
						if (inObject) {
							if (objectWidth <= 2) {
								Point tempPoint = removeEdgePointsVert.pop();
								if (!skeleton.contains(tempPoint)) {
									skeleton.add(tempPoint);
									addThisRound.add(tempPoint);
								}
							}
							removeEdgePointsVert.push(pointRightEdge);
							pointRightEdge = new ErosionPoint(0, 0,false);
						}
						inObject = false;
						beforeObjectEdge = true;
						objectWidth = 0;
					}
				}
				if (inObject) {
					if (objectWidth <= 2) {
						Point tempPoint = removeEdgePointsVert.pop();
						if (!skeleton.contains(tempPoint)) {
							skeleton.add(tempPoint);
							addThisRound.add(tempPoint);
						}
					}
					inObject = false;
					beforeObjectEdge = true;
					removeEdgePointsVert.push(pointRightEdge);
					pointRightEdge = new ErosionPoint(0, 0,false);
				}
				objectWidth = 0;
			}
			objectWidth = 0;
			// now go vertical
			for (int i = 0; i < temp.getSize().y; i++) {// loop width
				for (int j = 0; j < temp.getSize().x; j++) {// loop height
					if (temp.getPos(j, i)) {
						if (beforeObjectEdge) {
							removeEdgePointsHorz.push(new ErosionPoint(j, i,true));
						}
						beforeObjectEdge = false;
						inObject = true;
						pointRightEdge = new ErosionPoint(j, i,false);
						objectWidth++;
					} else {
						if (inObject) {
							if (objectWidth <= 2) {
								Point tempPoint = removeEdgePointsHorz.pop();
								if (!skeleton.contains(tempPoint)) {
									skeleton.add(tempPoint);
									addThisRound.add(tempPoint);
								}
							}
							removeEdgePointsHorz.push(pointRightEdge);
							pointRightEdge = new ErosionPoint(0, 0,false);
						}
						inObject = false;
						beforeObjectEdge = true;
						objectWidth = 0;
					}
				}
				if (inObject) {
					if (objectWidth <= 2) {
						Point tempPoint = removeEdgePointsHorz.pop();
						if (!skeleton.contains(tempPoint)) {
							skeleton.add(tempPoint);
							addThisRound.add(tempPoint);
						}
					}
					inObject = false;
					beforeObjectEdge = true;
					removeEdgePointsHorz.push(pointRightEdge);
					pointRightEdge = new ErosionPoint(0, 0,false);
				}
				objectWidth = 0;
			}
			// remove skeleton points from the list, identified by be duplicate in this list
			while (!removeEdgePointsHorz.isEmpty()) {
				Point removePoint = removeEdgePointsHorz.pop();
				temp.setPixel(removePoint, false);
				if (distanceMap.getDistanceFromEdge(removePoint) == -5) {
					distanceMap.setDistanceFormEdge(removePoint, distanceFromEdgeCount);
				}
			}
			while (!removeEdgePointsVert.isEmpty()) {
				Point removePoint = removeEdgePointsVert.pop();
				temp.setPixel(removePoint, false);
				if (distanceMap.getDistanceFromEdge(removePoint) == -5) {
					distanceMap.setDistanceFormEdge(removePoint, distanceFromEdgeCount);
				}
			}
			if (distanceFromEdgeCount < 2) {
				skeleton = new LinkedList<Point>();
			} else {
				addBackSkeleton(temp);
			}
			if (distanceFromEdgeCount > 2) {// do everytime after first run
				int tempRemovedCount = lastPixelCount - temp.getPixelCount();
				if (removedLastTime - tempRemovedCount > most2LeastRemoved) {
					most2LeastRemoved = removedLastTime - tempRemovedCount;
					this.most2LeastRemovedAtWidthCount = distanceFromEdgeCount;
				}
				if (biggestIncreaseInSkeleton < skeleton.size() - skeletonSizeLastTime) {
					biggestIncreaseInSkeleton = skeleton.size() - skeletonSizeLastTime;
					this.biggestIncreaseSkeletonAtWidthCount = distanceFromEdgeCount;
				}
			}
			skeletonSizeLastTime = skeleton.size();
			removedLastTime = lastPixelCount - temp.getPixelCount();
			distanceFromEdgeCount++;
		}
		addBackSkeleton(temp);
       this.objectWidth[0]=(this.biggestIncreaseSkeletonAtWidthCount+.75)*2;
       this.objectWidth[1]=(this.most2LeastRemovedAtWidthCount+.75)*2;
	}

	/**
	 * this is used to add back the parts of the medial axis that have been removed during erosion
	 * to help with the next step of erosion
	 * 
	 * @param temp
	 *            the cluster to add back the medial axis points too
	 */
	private void addBackSkeleton(Cluster temp) {
		for (int i = 0; i < skeleton.size(); i++) {
			temp.setPixel(skeleton.get(i), true);
		}
	}

	/**
	 * this attempts to reconnect the pieces of the medialAxis that were disconnected during erosion
	 * 
	 * @param myCluster
	 *            the cluster that we are getting the medial axis of
	 * @param graph
	 *            the graph of the medialAxis
	 */
	public void fillInSkeleton(ChromosomeCluster myCluster, MedialAxisGraph graph) {
		AroundPixel aroundPixel = new AroundPixel();
		//for each pixel in skeleton/medial axis
		for (int i = 0; i < skeleton.size(); i++) {
			int mostCenteredConnection = 0;
			int connections = 0;
			Point tempPoint = skeleton.get(i);
			int addPoint = -1;
			boolean added = false;
			Point mostConnected = new Point(-1, -1);
			Point newConnectionPoint = new Point(-1, -1);
			int mostNewConnections = 0;
			boolean connectionPos[] = { false, false, false, false, false, false, false, false };
			for (int j = 0; j < 8; j++) {
				Point tempAround = aroundPixel.getPoint(j, tempPoint);
				//if there not off the edge of the cluster box
				if (tempAround.x >= 0 && tempAround.x < myCluster.getSize().x && tempAround.y >= 0
						&& tempAround.y < myCluster.getSize().y) {
					if (skeleton.contains(tempAround)) {
						connectionPos[j] = true;
						connections++;
					}
				}
			}
			//for each pixel around the current skeleton pixel tempAround
			for (int j = 0; j < 8; j++) {
				//this pixel is not part of skeleton and the two next to it aren't a part of skeleton
				if (!connectionPos[j] 
					&& !connectionPos[aroundPixel.handleLoop(j + 1)]
					&& !connectionPos[aroundPixel.handleLoop(j - 1)]
					&&(!connectionPos[aroundPixel.handleLoop(j - 2)]
							||!connectionPos[aroundPixel.handleLoop(j + 2)])) {
					Point tempAround = aroundPixel.getPoint(j, tempPoint);
					//if there not off the edge of the cluster box
					if (tempAround.x >= 0 && tempAround.x < myCluster.getSize().x
							&& tempAround.y >= 0 && tempAround.y < myCluster.getSize().y) {
						//if this pixel has a more centered value based on distanceMap
						if (distanceMap.getDistanceFromEdge(tempAround) > mostCenteredConnection) {
							mostCenteredConnection = distanceMap.getDistanceFromEdge(tempAround);
							addPoint = j;
						}
						int currConnections = checkForMostNewConnection(j, tempPoint);
						if (currConnections > mostNewConnections) {
							mostConnected = aroundPixel.getPoint(j, tempPoint);
							mostNewConnections = currConnections;
							newConnectionPoint = this.getBridgePoint(j, tempPoint);
						}
					}
				}
			}
			//if this pixel touches less than 3 pixels in the skeleton and is not near the edge of the chromosome
			//TODO(aamcknig): possible address a highly connected pixel have 5 or 6 connections
			//TODO(aamcknig): currently not addressing connections above 2 connections
			if (connections < 3 && distanceMap.getDistanceFromEdge(tempPoint) > 2) {
				if (connections < 2) {
					//if there is a point that connects or bridges back to another part of the skeleton
					if (mostNewConnections > 0 && mostConnected.x != -1) {
						skeleton.add(mostConnected);
						graph.addVertex(new Vertex(mostConnected,distanceMap.getDistanceFromEdge(mostConnected)));
						added = true;
						//if there is a point to add that is centered in chromosome
					} else if (addPoint >= 0) {
						Point newTempPoint = aroundPixel.getPoint(addPoint, tempPoint);
						skeleton.add(newTempPoint);
						graph.addVertex(new Vertex(newTempPoint,distanceMap.getDistanceFromEdge(newTempPoint)));
						added = true;
					}
				}
				//if you have 2 connections but there is a bridge point 
				if (!added && mostConnected.x != -1) {
					//if that bridge point connects to a seperate part of the medial axis
					if (!graph.isConnected(tempPoint, newConnectionPoint)) {
						skeleton.add(mostConnected);
						graph.addVertex(new Vertex(mostConnected,distanceMap.getDistanceFromEdge(mostConnected)));
					}
				}

			}

		}

	}

	/**
	 * this gets a point that is the bridge connection to another part of the medial axis and
	 * returns the point(-1,-1) if there wasn't a bridge point
	 * 
	 * @param corner2Check
	 *            the direction to look for a bridge based of aroundPixel
	 * @param axisPoint
	 *            a point on the medialAxis
	 * @return the bridging point or (-1,-1)
	 */
	private Point getBridgePoint(int corner2Check, Point axisPoint) {
		AroundPixel aroundPixel = new AroundPixel();
		Point cornerConnection = new Point(-1, -1);
		Point tempPoint = aroundPixel.getPoint(corner2Check, axisPoint);
		if (this.skeleton.contains(tempPoint)) {
			return cornerConnection;
		}
		if (this.skeleton.contains(aroundPixel.getPoint(corner2Check, tempPoint))) {
			return aroundPixel.getPoint(corner2Check, tempPoint);
		}
		if (corner2Check - 1 < 0) {
			if (this.skeleton.contains(aroundPixel.getPoint(7, tempPoint))) {
				return aroundPixel.getPoint(7, tempPoint);
			}

		} else if (this.skeleton.contains(aroundPixel.getPoint(corner2Check - 1, tempPoint))) {
			return aroundPixel.getPoint(corner2Check - 1, tempPoint);
		}
		if (corner2Check + 1 > 7) {
			if (this.skeleton.contains(aroundPixel.getPoint(0, tempPoint))) {
				return aroundPixel.getPoint(0, tempPoint);
			}
		} else if (this.skeleton.contains(aroundPixel.getPoint(corner2Check + 1, tempPoint))) {
			return aroundPixel.getPoint(corner2Check + 1, tempPoint);
		}
		return cornerConnection;
	}
	/**
	 * this gets a point that is the bridge connection to another part of the medial axis and
	 * returns the point(-1,-1) if there wasn't a bridge point
	 * 
	 * @param corner2Check
	 *            the direction to look for a bridge based of aroundPixel
	 * @param axisPoint
	 *            a point on the medialAxis
	 * @return the bridging point or (-1,-1)
	 */
	private boolean isBridgePoint(int corner2Check, Point bridgePoint) {
		AroundPixel aroundPixel = new AroundPixel();
		if (this.skeleton.contains(bridgePoint)) {
			return false;
		}
		if (this.skeleton.contains(aroundPixel.getPoint(corner2Check, bridgePoint))) {
			return true;
		}
		if (corner2Check - 1 < 0) {
			if (this.skeleton.contains(aroundPixel.getPoint(7, bridgePoint))) {
				return true;
			}

		} else if (this.skeleton.contains(aroundPixel.getPoint(corner2Check - 1, bridgePoint))) {
			return true;
		}
		if (corner2Check + 1 > 7) {
			if (this.skeleton.contains(aroundPixel.getPoint(0, bridgePoint))) {
				return true;
			}
		} else if (this.skeleton.contains(aroundPixel.getPoint(corner2Check + 1, bridgePoint))) {
			return true;
		}
		return false;
	}

	/**
	 * returns the number of connections a bridge point will give
	 * 
	 * @param cornerToCheck
	 *            direction to check for bridge connections based on AroundPixel
	 * @param axisPoint
	 *            the point on the medial axis to bridge from
	 * @return the number of connections the corner2Check has
	 */
	private int checkForMostNewConnection(int cornerToCheck, Point axisPoint) {
		AroundPixel aroundPixel = new AroundPixel();
		int connectionCount = 0;
		Point tempPoint = aroundPixel.getPoint(cornerToCheck, axisPoint);
		if (this.skeleton.contains(tempPoint)) {
			return 0;
		}
		if (this.skeleton.contains(aroundPixel.getPoint(cornerToCheck, tempPoint))) {
			connectionCount++;
		}

		if (this.skeleton.contains(aroundPixel.getPoint(aroundPixel.handleLoop(cornerToCheck - 1), tempPoint))) {
			connectionCount++;
		}

		if (this.skeleton.contains(aroundPixel.getPoint(aroundPixel.handleLoop(cornerToCheck + 1), tempPoint))) {
			connectionCount++;
		}
		return connectionCount;
	}

	/**
	 * returns a list of points of this medial axis trimmed by removing points from the medial axis
	 * based on the distance from the edge any point that is less than minDistance will be removed
	 * 
	 * @param minDistance
	 *            the distance from edge that axis must be or be removed
	 * @return a linklist of points of the trimmed medial axis
	 */
	public LinkedList<Point> trimMedialAxis(int minDistance) {
		LinkedList<Point> trimmedAxis = new LinkedList<Point>();
		if (this.skeleton != null) {
			for (int i = 0; i < this.skeleton.size(); i++) {
				if (distanceMap.getDistanceFromEdge(this.skeleton.get(i)) >= minDistance) {
					trimmedAxis.add(this.skeleton.get(i));
				}
			}
		}
		return trimmedAxis;

	}

	/**
	 * this is for testing it outputs the widths to console that were found creating the medial axis
	 */
	public void writeObjectWidths() {
		System.out.print("Widths for this image: " + this.objectWidth[0] + ","
				+ this.objectWidth[1]);
	}
}