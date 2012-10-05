package testing;
import runner.Extractor;
import runner.ImageQueue;
import chromosome.ChromosomeCluster;
import chromosome.GeneticSlideImage;

public class TestShape {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filename;
		ImageQueue images = new ImageQueue();
		// put new images in the queue and return the next image from the folder args
		filename = images.getNextFile(args[0]);
		// create a slideImage from the filename
		GeneticSlideImage image = new GeneticSlideImage(filename);
		Extractor extractor = new Extractor();
		if (filename != null) {
			// extract the background from the image
			int shapeNum = extractor.findBackground(image);
			// extract the clusters from the image
			shapeNum = extractor.findClusters(image);
			// pass on the list of clusters
			ChromosomeCluster testCluster = extractor.getClusterList().get(0);
			// print out the first cluster in the list
			extractor.getClusterList().get(0).clusterOut();
		}
	}

}
