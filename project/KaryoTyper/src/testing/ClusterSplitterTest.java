package testing;

import java.awt.Point;
import java.util.LinkedList;

import chromosome.ChromosomeCluster;
import extraction.ClusterSplitter;

import basic_objects.PointList;

import junit.framework.TestCase;

public class ClusterSplitterTest extends TestCase {
	private ChromosomeCluster myCluster;

	protected void setUp() throws Exception {
		super.setUp();
		myCluster = new ChromosomeCluster(new Point(40, 40));
		initCluster();
	}

	public void initCluster() {
		// create vertical chromosome
		for (int i = 8; i < 16; i++) {
			for (int j = 0; j < 28; j++) {
				myCluster.setPixel(new Point(i, j), true);
			}
		}
		for (int j = 30; j < 39; j++) {
			for (int i = 0; i < 40; i++) {
				myCluster.setPixel(new Point(i, j), true);
			}
		}

		for (int i = 19; i < 27; i++) {
			for (int j = 0; j < 28; j++) {
				myCluster.setPixel(new Point(i, j), true);
			}
		}

		myCluster.setPixel(new Point(8, 28), true);
		myCluster.setPixel(new Point(9, 28), true);
		myCluster.setPixel(new Point(8, 29), true);
		myCluster.setPixel(new Point(9, 29), true);
		myCluster.setPixel(new Point(19, 28), true);
		myCluster.setPixel(new Point(18, 29), true);
		myCluster.setSize(40, 40);

	}

	public void testGetCutPoints() {
		int directions[] = { 3, 3, 3, 3, 6, 6, 6, 6 };
		PointList tempList = ClusterSplitter.getCutPoints(new Point(14, 14), 1, 4, directions);
		assertEquals(tempList.getList().size(), 10);
		assertTrue(tempList.getList().contains(new Point(13, 13)));
		assertTrue(tempList.getList().contains(new Point(12, 12)));
		assertTrue(tempList.getList().contains(new Point(11, 11)));
		assertTrue(tempList.getList().contains(new Point(14, 14)));
		assertTrue(tempList.getList().contains(new Point(14, 15)));
		assertTrue(tempList.getList().contains(new Point(14, 16)));
		assertTrue(tempList.getList().contains(new Point(14, 17)));
		assertTrue(tempList.getList().contains(new Point(14, 18)));
		assertTrue(tempList.getList().contains(new Point(14, 19)));
		assertTrue(tempList.getList().contains(new Point(14, 20)));

	}

	public void testGet4Lowest() {
		int directions[] = { 3, 2, 10, 3, 9, 6, 9, 6 };
		LinkedList<Integer> tempList = ClusterSplitter.get4Lowest(directions);
		assertEquals(tempList.size(), 4);
		assertEquals(tempList.get(0).intValue(), 1);
		assertEquals(tempList.get(1).intValue(), 0);
		assertEquals(tempList.get(2).intValue(), 3);
		assertEquals(tempList.get(3).intValue(), 5);

	}

	public void testGetLowestOppisiteSide() {
		int directions[] = { 3, 2, 10, 3, 9, 6, 9, 6 };
		assertEquals(ClusterSplitter.getLowestOppisiteSide(0, directions), 3);
		assertEquals(ClusterSplitter.getLowestOppisiteSide(2, directions), 5);
		assertEquals(ClusterSplitter.getLowestOppisiteSide(4, directions), 1);
		assertEquals(ClusterSplitter.getLowestOppisiteSide(7, directions), 3);

	}

	public void testIsCrossSectionPartOfCluster() {
		int directions[] = { 3, 2, 10, 3, 9, 6, 9, 6 };
		assertTrue(ClusterSplitter.isCrossSectionPartOfCluster(0, 3, directions, 5));
		assertTrue(ClusterSplitter.isCrossSectionPartOfCluster(1, 5, directions, 5));
		// assertFalse(ClusterSplitter.isCrossSectionPartOfCluster(2, 6, directions, 7));
		int directions2[] = { 3, 3, 3, 3, 9, 6, 9, 6 };
		assertFalse(ClusterSplitter.isCrossSectionPartOfCluster(0, 3, directions2, 5));

	}

	public void testGetQueenMoveLength() {
		assertEquals(7, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 27), 6, 12));
		assertEquals(0, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 27), 1, 12));
		assertEquals(0, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 27), 2, 12));
		assertEquals(0, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 27), 3, 12));
		assertEquals(11,
				12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 27), 4, 12));
		assertEquals(1, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 27), 5, 12));
		assertEquals(7, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 27), 7, 12));
		assertEquals(12,
				12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 27), 0, 12));

		assertEquals(1, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 29), 6, 12));
		assertEquals(0, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 29), 1, 12));
		assertEquals(0, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 29), 2, 12));
		assertEquals(8, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 29), 3, 12));
		assertEquals(9, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 29), 4, 12));
		assertEquals(9, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 29), 5, 12));
		assertEquals(7, 12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 29), 7, 12));
		assertEquals(12,
				12 - ClusterSplitter.getQueenMoveLength(myCluster, new Point(8, 29), 0, 12));

	}

	public void testCheckForSplit() {
		PointList tempList = ClusterSplitter.checkForSplit(myCluster, new Point(8, 29), 7);
		assertEquals(tempList.getList().size(), 2);
		assertTrue(tempList.getList().contains(new Point(8, 29)));
		assertTrue(tempList.getList().contains(new Point(9, 29)));
	}

}
