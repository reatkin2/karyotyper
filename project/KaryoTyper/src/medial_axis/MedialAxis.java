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
	protected SkeletonList skeleton;
	private double objectWidth[];
	protected DistanceMap distanceMap;
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
		skeleton = new SkeletonList();
		biggestIncreaseSkeletonAtWidthCount = -1;
		most2LeastRemovedAtWidthCount = 1;
		objectWidth = new double[2];
		objectWidth[0] = -1;
		objectWidth[1] = -1;
	}

	public void setMedialAxis(LinkedList<Vertex> tempMedialAxis) {
		this.skeleton = new SkeletonList(tempMedialAxis);
	}

	private void copyMedialAxis(MedialAxis copyMedialAxis) {
		this.distanceMap = new DistanceMap(copyMedialAxis.distanceMap);
		this.skeleton = copyMedialAxis.skeleton;
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
		return this.skeleton.getOneList();
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
		this.skeleton = new SkeletonList();
		// AroundPixel aroundPixel=new AroundPixel();
		LinkedList<Point> addThisRound = new LinkedList<Point>();
		Cluster temp = new Cluster(myCluster);
		this.distanceMap = new DistanceMap(myCluster.getSize().x, myCluster.getSize().y);
		ErosionPoint pointRightEdge = new ErosionPoint(0, 0, false);
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
							removeEdgePointsVert.push(new ErosionPoint(i, j, true));
						}
						objectWidth++;
						beforeObjectEdge = false;
						inObject = true;
						pointRightEdge = new ErosionPoint(i, j, false);
					} else {
						if (inObject) {
							if (objectWidth <= 2) {
								Point tempPoint = removeEdgePointsVert.pop();
								if (!skeleton.contains(tempPoint)) {
									skeleton.add(tempPoint, distanceFromEdgeCount);
									addThisRound.add(tempPoint);
								}
							}
							removeEdgePointsVert.push(pointRightEdge);
							pointRightEdge = new ErosionPoint(0, 0, false);
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
							skeleton.add(tempPoint, distanceFromEdgeCount);
							addThisRound.add(tempPoint);
						}
					}
					inObject = false;
					beforeObjectEdge = true;
					removeEdgePointsVert.push(pointRightEdge);
					pointRightEdge = new ErosionPoint(0, 0, false);
				}
				objectWidth = 0;
			}
			objectWidth = 0;
			// now go vertical
			for (int i = 0; i < temp.getSize().y; i++) {// loop width
				for (int j = 0; j < temp.getSize().x; j++) {// loop height
					if (temp.getPos(j, i)) {
						if (beforeObjectEdge) {
							removeEdgePointsHorz.push(new ErosionPoint(j, i, true));
						}
						beforeObjectEdge = false;
						inObject = true;
						pointRightEdge = new ErosionPoint(j, i, false);
						objectWidth++;
					} else {
						if (inObject) {
							if (objectWidth <= 2) {
								Point tempPoint = removeEdgePointsHorz.pop();
								if (!skeleton.contains(tempPoint)) {
									skeleton.add(tempPoint, distanceFromEdgeCount);
									addThisRound.add(tempPoint);
								}
							}
							removeEdgePointsHorz.push(pointRightEdge);
							pointRightEdge = new ErosionPoint(0, 0, false);
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
							skeleton.add(tempPoint, distanceFromEdgeCount);
							addThisRound.add(tempPoint);
						}
					}
					inObject = false;
					beforeObjectEdge = true;
					removeEdgePointsHorz.push(pointRightEdge);
					pointRightEdge = new ErosionPoint(0, 0, false);
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
			if (distanceFromEdgeCount >= 2) {
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
		this.objectWidth[0] = (this.biggestIncreaseSkeletonAtWidthCount + .75) * 2;
		this.objectWidth[1] = (this.most2LeastRemovedAtWidthCount + .75) * 2;
	}

	/**
	 * this is used to add back the parts of the medial axis that have been removed during erosion
	 * to help with the next step of erosion
	 * 
	 * @param temp
	 *            the cluster to add back the medial axis points too
	 */
	private void addBackSkeleton(Cluster temp) {
		LinkedList<Point> tempList = skeleton.getOneList();
		for (int i = 0; i < tempList.size(); i++) {
			temp.setPixel(tempList.get(i), true);
		}
	}

	public SkeletonList getSkeletonList() {
		return this.skeleton;
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
		if (this.skeleton.contains(bridgePoint)) {
			return false;
		}
		if (this.skeleton.contains(AroundPixel.getPoint(corner2Check, bridgePoint))) {
			return true;
		}
		if (corner2Check - 1 < 0) {
			if (this.skeleton.contains(AroundPixel.getPoint(7, bridgePoint))) {
				return true;
			}

		} else if (this.skeleton.contains(AroundPixel.getPoint(corner2Check - 1, bridgePoint))) {
			return true;
		}
		if (corner2Check + 1 > 7) {
			if (this.skeleton.contains(AroundPixel.getPoint(0, bridgePoint))) {
				return true;
			}
		} else if (this.skeleton.contains(AroundPixel.getPoint(corner2Check + 1, bridgePoint))) {
			return true;
		}
		return false;
	}

	/**
	 * returns a list of points of this medial axis trimmed by removing points from the medial axis
	 * based on the distance from the edge any point that is less than minDistance will be removed
	 * 
	 * @param minDistance
	 *            the distance from edge that axis must be or be removed
	 * @return a linklist of points of the trimmed medial axis
	 */
	public LinkedList<Point> getPossibleBreaks(int minDistance) {
		LinkedList<Point> possibleBreaks = new LinkedList<Point>();
		LinkedList<Point> skelPoints = this.skeleton.getOneList();
		if (skelPoints != null) {
			for (int i = 0; i < skelPoints.size(); i++) {
				if (distanceMap.getDistanceFromEdge(skelPoints.get(i)) <= minDistance) {
					possibleBreaks.add(skelPoints.get(i));
				}
			}
		}
		return possibleBreaks;
	}

	/**
	 * this is for testing it outputs the widths to console that were found creating the medial axis
	 */
	public void writeObjectWidths() {
		System.out.print("Widths for this image: " + this.objectWidth[0] + ","
				+ this.objectWidth[1]);
	}
}