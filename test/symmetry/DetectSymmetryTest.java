/**
 * 
 */
package symmetry;

import java.util.LinkedList;

import testing.TestShape;
import medial_axis.MedialAxisGraph;
import chromosome.ChromosomeCluster;
import chromosome.ChromosomeList;
import chromosome.GeneticSlideImage;
import junit.framework.TestCase;

/**
 * @author Robert
 * 
 */
public class DetectSymmetryTest extends TestCase {

	private GeneticSlideImage image;
	private ChromosomeCluster cluster;
	private MedialAxisGraph axisGraph;

	/**
	 * @param name
	 */
	public DetectSymmetryTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		String imagePath = ".\\testImages\\testImage1.png";
		cluster = TestShape.getCluster(imagePath);
		image = TestShape.getGeneticSlideImage();
		cluster.createMedialAxisGraph(image);
		axisGraph = cluster.getMedialAxisGraph();
		axisGraph.generateOrthogonals(3, 5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link symmetry.DetectSymmetryTest#detectWidthSymmetry(medial_axis.MedialAxisGraph, chromosome.GeneticSlideImage)}
	 * .
	 */
	public void testDetectWidthSymmetry() {
		DetectSymmetry.detectWidthSymmetry(axisGraph, image);
		fail("Not yet implemented.  DetectSymmetry may be obsolete (11/11/2012)");
	}

}
