/**
 * 
 */
package blackbox;

import chromosome.ChromosomeList;
import chromosome.GeneticSlideImage;
import extraction.Extractor;

/**
 * @author Robert
 *
 */
public class RunClusterExtractor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java RunClusterExtractor <filePath>");
			System.exit(1);
		}
		
		String filename = args[0];

		Extractor extractor = new Extractor();
		// use the current image in the queue and create a slideImage
		GeneticSlideImage image = new GeneticSlideImage(filename);
		// extract the background from the image
		extractor.removeBackground(image);
		// get clusters from the image and keep a count of how many
		int numClustersFound = extractor.findClusters(image);
		// pass the list of clusters on to slidelist
		ChromosomeList slideList = new ChromosomeList(extractor.getClusterList(), image);
		slideList.printChromosomes(image);
	}

}
