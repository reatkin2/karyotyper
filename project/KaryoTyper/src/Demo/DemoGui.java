package Demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

import medial_axis.MedialAxisGraph;
import runner.ImageQueue;
import basic_objects.PointList;
import chromosome.ChromosomeList;
import chromosome.GeneticSlideImage;
import extraction.ClusterSplitter;
import extraction.Extractor;
import extraction.MirrorSplit;

public class DemoGui extends JFrame {
	/**
	 * 
	 */
	private static boolean closing;// =false;
	private static final long serialVersionUID = 1L;
	public int imgCounter;
	public int targetsFound;
	public static JLabel currentStatus;
	private long start;
	public JPanel upper; 
	public JPanel lower; 
	public JPanel left;
	public JPanel right;
	public JLabel imgCount;
	public JButton extract;
	public JButton distanceMap;
	public JButton medialAxis;
	public JButton cleanMedialAxis;
	public JButton smallSplit;
	public JButton orthoLines;
	public JButton startPoints;
	public JButton projections;
	public JButton darkBands;
	public JButton linearize;
	public JButton	reset;
	public ImagesPanel imagePane;
	public JScrollPane scroll;


	public DemoGui(String string) {
		super(string);
		DemoGui.closing = false;
		start = System.currentTimeMillis();
		imgCounter = 0;
		targetsFound = 0;
		initGui();
	}
	public void initGui(){
		this.setLayout(new BorderLayout());
		upper = new JPanel();
		lower = new JPanel();
		left = new JPanel();
		right = new JPanel();
		this.setLocation(20, 20);
		Dimension minSize = new Dimension(800, 600);
		Dimension leftMinSize = new Dimension(60, 600);
		//Dimension rightMinSize = new Dimension(740, 600);
		
		left.setMinimumSize(leftMinSize);
		left.setBackground(Color.red);
		right.setBackground(Color.GREEN);
		//right.setMinimumSize(rightMinSize);

		this.setMinimumSize(minSize);
		imgCount = new JLabel("Currently No Images In Directory");
		DemoGui.currentStatus = new JLabel("Chroalan");
		DemoGui.currentStatus.setForeground(Color.RED);
		upper.add(DemoGui.currentStatus);
		lower.add(imgCount);
		this.add(upper,BorderLayout.NORTH);
		this.add(left,BorderLayout.WEST);
		//this.add(right,BorderLayout.CENTER);
		this.setVisible(true);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// create custom close operation
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitProcedure();
			}

		});
		initButtons();
		initImageArea();
	}
	public void initImageArea(){
		imagePane=new ImagesPanel();
		scroll = new JScrollPane(imagePane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(imagePane);
		this.add(scroll,BorderLayout.CENTER);
		
	}
	public void displayImage(BufferedImage tempBuff){
		this.imagePane.writeNewImage(tempBuff);
		this.repaint();

	}
	public void initButtons(){
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		extract=new JButton("extract");
		distanceMap=new JButton("distanceMap");
		medialAxis=new JButton("medialAxis");
		cleanMedialAxis=new JButton("cleanMedialAxis");
		smallSplit=new JButton("smallSplit");
		orthoLines=new JButton("orthoLines");
		startPoints=new JButton("startPoints");
		projections=new JButton("projections");
		darkBands=new JButton("darkBands");
		linearize=new JButton("linearize");
		reset=new JButton("reset");
		buttonPanel.add(extract);
		buttonPanel.add(distanceMap);
		buttonPanel.add(medialAxis);
		buttonPanel.add(cleanMedialAxis);
		buttonPanel.add(smallSplit);
		buttonPanel.add(orthoLines);
		buttonPanel.add(startPoints);
		buttonPanel.add(projections);
		buttonPanel.add(darkBands);
		buttonPanel.add(linearize);
		buttonPanel.add(reset);		
		left.add(buttonPanel);

	}
	// Create and set up the window.
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("add the commandline arugments <imagesPath>");
			System.out.println("Ex.  java -jar *.jar /home/arc/images/");
		} else {
			System.out.println(args[0]);
			DemoGui frame = new DemoGui("chromosome Getter GUI");
			// int imgCounter=0;
			String filename;
			// initialize the que
			ImageQueue images = new ImageQueue();
			// initialize the extractor
			// System.out.println(args[i]+"---------nextFilestarts Here---------------");
			// put images in the que and return next file in the path from string args
			filename = images.getNextFile(args[0]);
			if (filename != null) {
				GeneticSlideImage image = new GeneticSlideImage(filename);
				frame.displayImage(image.getImg());
			}
				
			while (!DemoGui.closing) {
//				// System.out.println(args[i]+"---------nextFilestarts Here---------------");
//				// put images in the que and return next file in the path from string args
//				filename = images.getNextFile(args[0]);
//				if (filename != null) {
//					if (!DemoGui.currentStatus.getText().contains("Finishing")) {
//						DemoGui.currentStatus
//								.setText("Finding Chromosomes in slide image: " + filename);
//					}
//					Extractor extractor = new Extractor();
//					// use the current image in the que and create a slideImage
//					GeneticSlideImage image = new GeneticSlideImage(filename);
//					// extract the background from the image
//					extractor.removeBackground(image);
//					// get clusters from the image and keep a count of how many
//					frame.targetsFound += extractor.findClusters(image);
//					// pass the list of clusters on to slidelist
//					ChromosomeList slideList1 = new ChromosomeList(extractor.getClusterList(), image);
//
//					for(int i=0;i<slideList1.getChromosomeList().size();i++){
//						LinkedList<PointList> cutList=ClusterSplitter.getSplitPoints(slideList1.getChromosomeList().get(i), (int) Math.round(image.getChromoWidth()/3));
//						if(!cutList.isEmpty()){
//							int newChromosomes=extractor.splitClusters(slideList1.getChromosomeList().get(i), cutList, image);
//							if(newChromosomes>1){
//								slideList1.getChromosomeList().remove(i);
//							}
//						}
//					}
//					ChromosomeList splitList=new ChromosomeList(extractor.getSplitList(),image);
//					splitList.calcMedialAxis(image);
//					for(int i=0;i<splitList.getChromosomeList().size();i++){
//						MedialAxisGraph tempGraph=splitList.getChromosomeList().get(i).getMedialAxisGraph();
//						MirrorSplit mirror=new MirrorSplit(tempGraph);
//						splitList.getChromosomeList().get(i).setPaintPoints(mirror.getAllProblemDistances(splitList.getChromosomeList().get(i).getBounds(), 
//								tempGraph.getAxisGraph(), image.getChromoWidth(), tempGraph.getDistanceMap()));
//					}
//					frame.imgCount.setText("Writing Splits" + splitList.size() + " images. ");
//					splitList.printProblemAxisWithMedialAxis(image,false);
//					
//					// test for split lines to shapdata/keep
//					//slideList1.splitNWrite(image);
//					slideList1.calcMedialAxis(image);
//					for(int i=0;i<slideList1.getChromosomeList().size();i++){
//						MedialAxisGraph tempGraph=slideList1.getChromosomeList().get(i).getMedialAxisGraph();
//						MirrorSplit mirror=new MirrorSplit(tempGraph);
//						slideList1.getChromosomeList().get(i).setPaintPoints(mirror.getAllProblemDistances(slideList1.getChromosomeList().get(i).getBounds(), 
//								tempGraph.getAxisGraph(), image.getChromoWidth(), tempGraph.getDistanceMap()));
//					}
//					frame.imgCount.setText("Writing " + slideList1.size() + " images. ");
//					slideList1.printProblemAxisWithMedialAxis(image,false);
//
//					frame.imgCount.setText(frame.targetsFound + " Chromosomes found in "
//							+ (++frame.imgCounter) + " slides read so far.");
//					if (!DemoGui.currentStatus.getText().contains("Finishing")) {
//						DemoGui.currentStatus.setText("Waiting for images");
//					} else {
//						DemoGui.currentStatus.setText("Finished looking at img"
//								+ filename + " shutting down.");
//					}
//				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();

				}
			}

			frame.exit();
			frame.dispose();
		}
	}

	protected static void exitProcedure() {
		DemoGui.closing = true;
		DemoGui.currentStatus
				.setText("Finishing current image search and shutting down.");
	}

	public void exit() {

		// use the current time to mark files as unique file name
		// Date today=new Date();
		System.out.println("Minutes:Secs " + (((System.currentTimeMillis() - start) / 60000) / 60)
				+ ":" + ((System.currentTimeMillis() - start) / 1000));

		try {
			File dir1 = new File(".");
			System.out.println("Working directory is: " + dir1.getAbsolutePath());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}