package basic_objects;

import java.awt.Point;
import java.util.LinkedList;

/**
 * @author andrew
 * 
 */
public class AroundPixel {
	/*
	 * aroundot is an array of 8 points that is a x,y difference from the center point pixel 107
	 * |-1,-1| 0,-1| 1,-1| 2.6 or |-1, 0| dot | 1, 0| <--- this is a visual of around dot 345 |-1,
	 * 1| 0, 1| 1, 1|
	 */

	private final static Point[] aroundDot = { new Point(0, -1), new Point(-1, -1),
			new Point(-1, 0), new Point(-1, 1), new Point(0, 1), new Point(1, 1), new Point(1, 0),
			new Point(1, -1) };

	/**
	 * get a point that values can be added to another point to be the adjacent position from the
	 * point you add the return point too
	 * 
	 * @param pos
	 *            the adjacent position starting with 0 at top center and moving counter clockwise
	 * @return point that values can be added to another point to be the adjacent position from the
	 *         point you add the return point too
	 */
	public static Point getPos(int pos) {
		return aroundDot[pos];
	}

	/**
	 * returns the adjacent point to fromPoint in the position around fromPoint pos
	 * 
	 * @param pos
	 *            the adjacent position starting with 0 at top center and moving counter clockwise
	 * @param fromPoint
	 *            the point that your trying to find an adjacent point of
	 * @return the adjacent point
	 */
	public static Point getPoint(int pos, Point fromPoint) {
		return new Point(fromPoint.x + aroundDot[pos].x, fromPoint.y + aroundDot[pos].y);
	}

	/**
	 * returns true if the positions are adjacent based on 0 at the top and incrementing by 1 and
	 * rotating counter clockwise 0-7
	 * 
	 * @param pos1
	 *            a position around a point
	 * @param pos2
	 *            a position around a point
	 * @return true if the positions are adjacent
	 */
	public static boolean isAdjacent(int pos1, int pos2) {
		if ((pos1 == 0 && pos2 == 7) || pos1 == 7 && pos2 == 0) {
			return true;
		} else {
			int distance = Math.abs(pos1 - pos2);
			if (distance <= 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns numbers that are from 0-7 if -1 returns 7 if 8 returns 0
	 * 
	 * @param pos
	 *            the number to be checked and returned
	 * @return a number between 0-7 7 if pos is -1 and 0 if pos is 8
	 */
	public static int handleLoop(int pos) {
		if (pos < 0) {
			pos += 8;
			return pos;
		}
		if (pos > 7) {
			pos -= 8;
			return pos;
		}
		return pos;
	}

	/**
	 * if the positions are opposite corners it returns true
	 * 
	 * @param pos1
	 *            position to be checked
	 * @param pos2
	 *            position to be checked
	 * @return true if the positions are opposite corners
	 */
	public static boolean isOppisiteCorner(int pos1, int pos2) {
		if (pos1 % 2 == 0 || pos2 % 2 == 0) {
			return false;
		}
		if (Math.abs(pos1 - pos2) == 4) {
			return true;
		}
		return false;
	}

	/**
	 * returns the oppisite corner of the position specified
	 * 
	 * @param pos
	 *            the pos to get the oppisite corner of
	 * @param xy
	 *            the center point the oppisite will be across
	 * @return the Point that is the oppisite corner
	 */
	public static Point getOppisiteCorner(int pos, Point xy) {
		int oppisiteCorner = pos - 4;
		oppisiteCorner = handleLoop(oppisiteCorner);
		return AroundPixel.getPoint(oppisiteCorner, xy);
	}

	/**
	 * returns the oppisite corner position of the position specified
	 * 
	 * @param pos
	 *            the pos to get the oppisite corner of
	 * @return the oppisite position of pos
	 */
	public static int getOppisitePos(int pos) {
		int oppisiteCorner = pos - 4;
		return handleLoop(oppisiteCorner);
	}

	/**
	 * returns a list of integers that represent positions aroundPixel that are between startPos and
	 * otherPos by adding +1 to startPos till it equals otherPos
	 * 
	 * @param startPos
	 *            the position to start from and move toward otherPos
	 * @param otherPos
	 *            the position to stop when all positions have been gooten
	 * @return list of integers between startPos and otherPos from start in plus direction
	 */
	public static LinkedList<Integer> getPositionsBetweenPlus(int startPos, int otherPos) {
		LinkedList<Integer> betweenList = new LinkedList<Integer>();
		int betweenCurr = startPos;
		do {
			if (AroundPixel.handleLoop(betweenCurr + 1) != otherPos) {
				betweenList.add(AroundPixel.handleLoop(betweenCurr + 1));
			}
			betweenCurr++;
		} while (AroundPixel.handleLoop(betweenCurr + 1) != otherPos);
		return betweenList;
	}

	/**
	 * returns a list of integers that represent positions aroundPixel that are between startPos and
	 * otherPos by subtracting 1 from startPos till it equals otherPos
	 * 
	 * @param startPos
	 *            the position to start from and move toward otherPos
	 * @param otherPos
	 *            the position to stop when all positions have been gooten
	 * @return list of integers between startPos and otherPos from start in negative direction
	 */
	public static LinkedList<Integer> getPositionsBetweenNeg(int startPos, int otherPos) {
		LinkedList<Integer> betweenList = new LinkedList<Integer>();
		int betweenCurr = startPos;
		do {
			if (AroundPixel.handleLoop(betweenCurr - 1) != otherPos) {
				betweenList.add(AroundPixel.handleLoop(betweenCurr - 1));
			}
			betweenCurr--;
		} while (AroundPixel.handleLoop(betweenCurr - 1) != otherPos);
		return betweenList;
	}

}
