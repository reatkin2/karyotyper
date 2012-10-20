package extraction;

import java.awt.Point;
import java.util.LinkedList;

import medial_axis.SkeletonList;

import basic_objects.AroundPixel;
import basic_objects.PointList;
import chromosome.ChromosomeCluster;

public class ClusterSplitter {

	public static void prepCutLine(PointList cutLine,ChromosomeCluster myCluster) {
		if (cutLine.getCutDir1() % 2 == 1) {
			Point tempPoint=cutLine.getAxisPoint();
			for (int i = 0; i < cutLine.getDir1length(); i++) {
				cutLine.addPoint(AroundPixel.getPoint(
						AroundPixel.handleLoop(cutLine.getCutDir1() - 1), tempPoint), 0);
				tempPoint = AroundPixel.getPoint(cutLine.getCutDir1(), tempPoint);
			}
			Point tempPoint2=AroundPixel.getPoint(AroundPixel.handleLoop(cutLine.getCutDir1() - 1),tempPoint);
			if(tempPoint2.x>=0&&tempPoint2.y>=0&&tempPoint2.x<myCluster.getSize().x&&tempPoint2.y<myCluster.getSize().y){
				cutLine.addPoint(tempPoint2, 0);
			}
			if(tempPoint.x>=0&&tempPoint.y>=0&&tempPoint.x<myCluster.getSize().x&&tempPoint.y<myCluster.getSize().y){
				cutLine.addPoint( tempPoint, 0);			
			}
		}
		if (cutLine.getCutDir2() % 2 == 1) {
			Point tempPoint=cutLine.getAxisPoint();
			for (int i = 0; i < cutLine.getDir2length(); i++) {
				cutLine.addPoint(AroundPixel.getPoint(
						AroundPixel.handleLoop(cutLine.getCutDir2() - 1), tempPoint), 0);
				tempPoint = AroundPixel.getPoint(cutLine.getCutDir2(), tempPoint);
			}
			Point tempPoint2=AroundPixel.getPoint(AroundPixel.handleLoop(cutLine.getCutDir2() - 1),tempPoint);
			if(tempPoint2.x>=0&&tempPoint2.y>=0&&tempPoint2.x<myCluster.getSize().x&&tempPoint2.y<myCluster.getSize().y){
				cutLine.addPoint(tempPoint2, 0);
			}
			if(tempPoint.x>=0&&tempPoint.y>=0&&tempPoint.x<myCluster.getSize().x&&tempPoint.y<myCluster.getSize().y){
				cutLine.addPoint( tempPoint, 0);			
			}
		}
	}

	/**
	 * get all the cutlines in a cluster based off medialaxis points that there distance map values
	 * are below splitMax and the cutline is shortert than splitMax
	 * 
	 * @param myCluster
	 * @param splitMax
	 * @return
	 */
	public static LinkedList<PointList> getSplitPoints(ChromosomeCluster myCluster, int splitMax) {
		LinkedList<PointList> splits = new LinkedList<PointList>();
		SkeletonList skelPoints = myCluster.getMedialAxisGraph().getSkeletonList();
		for (int i = 0; (i < splitMax) && i < (skelPoints.getList().size()); i++) {
			PointList tempList = myCluster.getMedialAxisGraph().getSkeletonList()
					.getListAtDistance(i);
			if (tempList != null) {
				for (int j = 0; j < tempList.getList().size(); j++) {
					PointList tempPoints = checkForSplit(myCluster, tempList.getList().get(j),
							splitMax);
					if (tempPoints != null) {
						ClusterSplitter.keepShortestCut(splits, tempPoints, splitMax);
						// splits.add(tempPoints);
					}
				}
			}
		}
		for(int i=0;i<splits.size();i++){
			ClusterSplitter.prepCutLine(splits.get(i),myCluster);
		}
		return splits;
	}

	/**
	 * keeps only the shortest cut in an area removes all other cuts
	 * 
	 * @param pointList
	 * @param newList
	 * @param splitMax
	 */
	public static void keepShortestCut(LinkedList<PointList> pointList, PointList newList,
			int splitMax) {
		PointList lowest = null;
		for (int i = 0; i < pointList.size(); i++) {
			// pointList.get(i).getAxisPoint().distance(newList.getAxisPoint())<splitMax
			if (isSameCut(pointList.get(i), newList, splitMax)) {
				if (pointList.get(i).getList().size() < newList.getList().size()) {
					if (lowest != null
							&& pointList.get(i).getList().size() < lowest.getList().size()) {
						lowest = pointList.remove(i);
					} else {
						pointList.remove(i);
					}
				} else {
					pointList.remove(i);
				}
			}
		}
		if (lowest == null) {
			pointList.add(newList);
		}
	}

	/**
	 * this method checks to see if to lines cut at the same connection between two chromosomes
	 * 
	 * @param list1
	 * @param list2
	 * @param splitMax
	 * @return
	 */
	public static boolean isSameCut(PointList list1, PointList list2, int splitMax) {
		if (list1.getList().size() > 1 && list2.getList().size() > 1) {
			if (list1.getList().getLast().distance(list2.getList().getLast()) < splitMax) {
				if (list1.getList().get(list1.getList().size() - 2)
						.distance(list2.getList().get(list2.getList().size() - 2)) < splitMax) {
					return true;
				}
			} else if (list1.getList().getLast()
					.distance(list2.getList().get(list2.getList().size() - 2)) < splitMax) {
				if (list1.getList().get(list1.getList().size() - 2)
						.distance(list2.getList().getLast()) < splitMax) {
					return true;
				}
			}
		} else if (list1.getList().size() > 1) {
			if (list1.getList().getLast().distance(list2.getAxisPoint()) < splitMax) {
				if (list1.getList().get(list1.getList().size() - 2).distance(list2.getAxisPoint()) < splitMax) {
					return true;
				}
			}
		} else if (list2.getList().size() > 1) {
			if (list1.getAxisPoint().distance(list2.getList().getLast()) < splitMax) {
				if (list1.getAxisPoint().distance(list2.getList().get(list2.getList().size() - 2)) < splitMax) {
					return true;
				}
			}
		} else {
			if (list1.getAxisPoint().distance(list2.getAxisPoint()) < splitMax) {
				return true;
			}
		}
		return false;
	}

	/**
	 * gets a cutline if there is one at the point startPoint
	 * 
	 * @param chessboard
	 * @param startPoint
	 * @param maxCut
	 * @return
	 */
	public static PointList checkForSplit(ChromosomeCluster chessboard, Point startPoint, int maxCut) {
		// int checkDistance=2*maxCut;
		int diagonalCheck = (int) Math.round(maxCut / 1.4142);
		int dir[] = new int[8];
		int dirDistance[] = new int[8];
		for (int i = 0; i < 8; i++) {
			if (i % 2 == 0) {
				dir[i] = (maxCut - getQueenMoveLength(chessboard, startPoint, i, maxCut));
				dirDistance[i] = dir[i];
			} else {
				dir[i] = (diagonalCheck - getQueenMoveLength(chessboard, startPoint, i,
						diagonalCheck));
				dirDistance[i] = (int) Math.round(1.4142 * dir[i]);
			}
		}

		LinkedList<Integer> lowest4 = get4Lowest(dirDistance);

		int shortestCut = dirDistance[ClusterSplitter.getLowestOppisiteSide(lowest4.get(0),
				dirDistance)] + dirDistance[lowest4.get(0)];
		int lowestPair[] = new int[2];
		lowestPair[0] = lowest4.get(0);
		lowestPair[1] = ClusterSplitter.getLowestOppisiteSide(lowest4.get(0), dirDistance);
		for (int i = 1; i < 4; i++) {
			if ((dirDistance[ClusterSplitter.getLowestOppisiteSide(lowest4.get(i), dirDistance)] + dirDistance[i]) < shortestCut) {
				shortestCut = dirDistance[ClusterSplitter.getLowestOppisiteSide(lowest4.get(i),
						dirDistance)] + dirDistance[lowest4.get(i)];
				lowestPair[0] = lowest4.get(i);
				lowestPair[1] = ClusterSplitter.getLowestOppisiteSide(lowest4.get(i), dirDistance);
			}
		}
		if ((dirDistance[lowestPair[0]] + dirDistance[lowestPair[1]] + 1) <= maxCut) {
			if (isCrossSectionPartOfCluster(lowestPair[0], lowestPair[1], dirDistance, maxCut - 1)) {
				return getCutPoints(startPoint, lowestPair[0], lowestPair[1], dir);
			}
		}
		return null;
	}

	/**
	 * find the length moving from point xy in the direction of pos that is the farthest you can go
	 * till you hit the edge of the cluster or the countdown runs out
	 * 
	 * @param chessboard
	 *            a rectangular area that contains a cluster
	 * @param xy
	 *            the point to start from
	 * @param pos
	 *            the direction to go from point xy
	 * @param countDown
	 *            the largest length to get
	 * @return
	 */
	public static int getQueenMoveLength(ChromosomeCluster chessboard, Point xy, int pos,
			int countDown) {

		Point nextPoint = AroundPixel.getPoint(pos, xy);
		if (countDown > 0) {
			if (nextPoint.x < 0 || nextPoint.y < 0 || nextPoint.x > chessboard.getSize().x - 1
					|| nextPoint.y > chessboard.getSize().y - 1) {
				return countDown;
			} else if (chessboard.getValue(nextPoint.x, nextPoint.y)) {
				return getQueenMoveLength(chessboard, nextPoint, pos, --countDown);
			} else {
				return countDown;
			}
		}
		return 0;
	}

	/**
	 * this takes the array and returns the 4 lowest integer values in order from lowest to highest
	 * 
	 * @param direction
	 *            the array of integers to go thru
	 * @return linkedlist of 4 integers that are lowest to highest
	 */
	public static LinkedList<Integer> get4Lowest(int[] direction) {
		LinkedList<Integer> lowest4List = new LinkedList<Integer>();
		lowest4List.addFirst(0);
		for (int i = 1; i < direction.length; i++) {
			boolean added = false;
			for (int j = 0; !added && j < lowest4List.size(); j++) {
				if (direction[i] < direction[lowest4List.get(j)]) {
					lowest4List.add(j, i);
					added = true;
				}
			}
			if (lowest4List.size() < 4 && !added) {
				lowest4List.addLast(i);
			}
			if (lowest4List.size() > 4) {
				lowest4List.removeLast();
			}
		}
		return lowest4List;
	}

	/**
	 * returns the lowest length oppisite side of center pixel
	 * 
	 * @param cornerToCheck
	 *            direction to check for bridge connections based on AroundPixel
	 * @param axisPoint
	 *            the point on the medial axis to bridge from
	 * @return the number of connections the corner2Check has
	 */
	public static int getLowestOppisiteSide(int startSpot, int[] direction) {

		int oppisitCorner = AroundPixel.getOppisitePos(startSpot);
		int lowestConnection = oppisitCorner;
		if (direction[AroundPixel.handleLoop(oppisitCorner - 1)] < direction[lowestConnection]) {
			lowestConnection = AroundPixel.handleLoop(oppisitCorner - 1);
		}
		if (direction[AroundPixel.handleLoop(oppisitCorner + 1)] < direction[lowestConnection]) {
			lowestConnection = AroundPixel.handleLoop(oppisitCorner + 1);
		}
		return lowestConnection;
	}

	/**
	 * this checks to be sure that the cutline has a descent size chromosome or object on both sides
	 * 
	 * @param startSpot
	 *            this with startoppisite make the cut line
	 * @param startOppisite
	 *            this with startSpot make the cutline
	 * @param direction
	 *            the length that you can go out from any position around the pixel
	 * @return true if the lengths that are a crossection to the line made by startSPot and
	 *         startOppisite are longer than minLength
	 */
	public static boolean isCrossSectionPartOfCluster(int startSpot, int startOppisite,
			int[] direction, int minLength) {

		LinkedList<Integer> posBetween = AroundPixel.getPositionsBetweenPlus(startSpot,
				startOppisite);
		LinkedList<Integer> negBetween = AroundPixel.getPositionsBetweenNeg(startSpot,
				startOppisite);
		boolean isPositiveSideLong = false;
		boolean isNegativeSideLong = false;
		for (int i = 0; i < posBetween.size(); i++) {
			if (direction[posBetween.get(i)] > minLength) {
				isPositiveSideLong = true;
			}
		}
		for (int i = 0; i < negBetween.size(); i++) {
			if (direction[negBetween.get(i)] > minLength) {
				isNegativeSideLong = true;
			}
		}
		if (isNegativeSideLong && isPositiveSideLong) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * this gets the points that make the cut line between chromosomes
	 * 
	 * @param cutPoint
	 *            the start point that was used to check for cut
	 * @param startSpot
	 *            the start position around the pixel to go out from
	 * @param startOppisite
	 *            the oppistie side of the pixel from startspot to go out from
	 * @param directionLength
	 *            the array of lengths that you can go out from each position around the pixel
	 * @return a pointlist that is the points of where to cut two chromosomes apart
	 */
	public static PointList getCutPoints(Point cutPoint, int startSpot, int startOppisite,
			int directionLength[]) {
		PointList cutList = new PointList(cutPoint, 0, startSpot, startOppisite,
				directionLength[startSpot], directionLength[startOppisite]);
		cutList.setAxisPoint(cutPoint);
		Point currPoint1 = AroundPixel.getPoint(startSpot, cutPoint);
		for (int i = 0; i < directionLength[startSpot] - 1; i++) {
			cutList.addPoint(currPoint1, 0);
			currPoint1 = AroundPixel.getPoint(startSpot, currPoint1);
		}
		Point currPoint2 = AroundPixel.getPoint(startOppisite, cutPoint);
		for (int i = 0; i < directionLength[startOppisite] - 1; i++) {
			cutList.addPoint(currPoint2, 0);
			currPoint2 = AroundPixel.getPoint(startOppisite, currPoint2);
		}

		// endPoints on the end of list for isSameCut
		if (directionLength[startSpot] > 0) {
			cutList.addPoint(currPoint1, 0);
		}
		if (directionLength[startOppisite] > 0) {
			cutList.addPoint(currPoint2, 0);
		}

		return cutList;
	}

}
