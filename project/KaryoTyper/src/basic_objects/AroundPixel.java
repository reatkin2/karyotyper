package basic_objects;

import java.awt.Point;

/**
 * @author andrew
 * 
 */
public class AroundPixel {
	/*
	 * aroundot is an array of 8 points that is a x,y difference from the center
	 * point pixel 107 |-1,-1| 0,-1| 1,-1| 2.6 or 
	 *                 |-1, 0| dot | 1, 0| <--- this is a visual of around dot 345
	 *                 |-1, 1| 0, 1| 1, 1|
	 */

	private Point aroundDot[];

	public AroundPixel() {
		aroundDot = new Point[8];
		aroundDot[0] = new Point(0, -1);// top middle
		aroundDot[1] = new Point(-1, -1);// top left
		aroundDot[2] = new Point(-1, 0);// middle left
		aroundDot[3] = new Point(-1, 1);// bottom left
		aroundDot[4] = new Point(0, 1);// bottom middle
		aroundDot[5] = new Point(1, 1);// bottom right
		aroundDot[6] = new Point(1, 0);// middle right
		aroundDot[7] = new Point(1, -1);// top right

	}

	/**
	 * get a point that values can be added to another point to be the adjacent position from the
	 * point you add the return point too
	 * 
	 * @param pos
	 *            the adjacent position starting with 0 at top center and moving counter clockwise
	 * @return point that values can be added to another point to be the adjacent position from the
	 *         point you add the return point too
	 */
	public Point getPos(int pos) {
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
	public Point getPoint(int pos, Point fromPoint) {
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
	public boolean isAdjacent(int pos1, int pos2) {
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
	public int handleLoop(int pos) {
		if (pos <0) {
			pos+=8;
			return pos;
		}
		if (pos >7) {
			pos-=8;
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
	public boolean isOppisiteCorner(int pos1, int pos2) {
		if (pos1 % 2 == 0 || pos2 % 2 == 0) {
			return false;
		}
		if (Math.abs(pos1 - pos2) == 4) {
			return true;
		}
		return false;
	}
	public Point getOppisiteCorner(int pos,Point xy){
		int oppisiteCorner=pos-4;
		oppisiteCorner=handleLoop(oppisiteCorner);
		return this.getPoint(oppisiteCorner, xy);
	}
}
