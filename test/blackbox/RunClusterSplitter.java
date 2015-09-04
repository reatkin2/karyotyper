package blackbox;

import java.util.LinkedList;

import basic_objects.PointList;
import chromosome.ChromosomeList;
import chromosome.GeneticSlideImage;
import extraction.ClusterSplitter;
import extraction.Extractor;

public class RunClusterSplitter {

	public static void main(String [] args) {
		if (args.length < 1) {
			System.out.println("Usage: java RunClusterSplitter <filePath>");
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
		
		for(int i = 0; i < slideList.getChromosomeList().size(); i++){
			LinkedList<PointList> cutList=ClusterSplitter.getSplitPoints(slideList.getChromosomeList().get(i), (int) Math.round(image.getChromoWidth()/3));
			if(!cutList.isEmpty()){
				extractor.splitClusters(slideList.getChromosomeList().get(i), cutList, image);
			}
		}
		ChromosomeList splitList=new ChromosomeList(extractor.getSplitList(),image);
		splitList.calcMedialAxis(image);
		splitList.printSplits(image);
		slideList.calcMedialAxis(image);

		slideList.printChromosomes(image);
	}
	
}
