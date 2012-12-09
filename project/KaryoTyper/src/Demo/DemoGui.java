package Demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	public static JLabel currentStatusLabel;
	private long start;
	public JPanel upperPanel;
	public JPanel lowerPanel;
	public JPanel leftPanel;
	public JPanel rightPanel;
	public JLabel imgCountLable;
	public JButton extractButton;
	public JButton distanceMapButton;
	public JButton medialAxisButton;
	public JButton cleanMedialAxisButton;
	public JButton smallSplitButton;
	public JButton orthoLinesButton;
	public JButton startPointsButton;
	public JButton projectionsButton;
	public JButton darkBandsButton;
	public JButton linearizeButton;
	public JButton resetButton;
	public ImagesPanel imagePanel;
	public JScrollPane scrollPane;

	public DemoGui(String string) {
		super(string);
		DemoGui.closing = false;
		start = System.currentTimeMillis();
		imgCounter = 0;
		targetsFound = 0;
		initGui();
	}

	public void initGui() {
		this.setLayout(new BorderLayout());
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		this.setLocation(20, 20);
		Dimension minSize = new Dimension(800, 600);
		Dimension leftMinSize = new Dimension(60, 600);
		// Dimension rightMinSize = new Dimension(740, 600);

		leftPanel.setMinimumSize(leftMinSize);
		leftPanel.setBackground(Color.red);
		rightPanel.setBackground(Color.GREEN);
		// right.setMinimumSize(rightMinSize);

		this.setMinimumSize(minSize);
		imgCountLable = new JLabel("Currently No Images In Directory");
		DemoGui.currentStatusLabel = new JLabel("Chroalan");
		DemoGui.currentStatusLabel.setForeground(Color.RED);
		upperPanel.add(DemoGui.currentStatusLabel);
		lowerPanel.add(imgCountLable);
		this.add(upperPanel, BorderLayout.NORTH);
		this.add(leftPanel, BorderLayout.WEST);
		// this.add(right,BorderLayout.CENTER);
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

	public void initImageArea() {
		imagePanel = new ImagesPanel();
		scrollPane = new JScrollPane(imagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(imagePanel);
		this.add(scrollPane, BorderLayout.CENTER);

	}

	public void displayImage(BufferedImage tempBuff) {
		this.imagePanel.writeNewImage(tempBuff);
	}

	public void initButtons() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		extractButton = new JButton("extract");
		distanceMapButton = new JButton("distanceMap");
		medialAxisButton = new JButton("medialAxis");
		cleanMedialAxisButton = new JButton("cleanMedialAxis");
		smallSplitButton = new JButton("smallSplit");
		orthoLinesButton = new JButton("orthoLines");
		startPointsButton = new JButton("startPoints");
		projectionsButton = new JButton("projections");
		darkBandsButton = new JButton("darkBands");
		linearizeButton = new JButton("linearize");
		resetButton = new JButton("reset");
		buttonPanel.add(extractButton);
		buttonPanel.add(distanceMapButton);
		buttonPanel.add(medialAxisButton);
		buttonPanel.add(cleanMedialAxisButton);
		buttonPanel.add(smallSplitButton);
		buttonPanel.add(orthoLinesButton);
		buttonPanel.add(startPointsButton);
		buttonPanel.add(projectionsButton);
		buttonPanel.add(darkBandsButton);
		buttonPanel.add(linearizeButton);
		buttonPanel.add(resetButton);
		leftPanel.add(buttonPanel);
		initButtonActions();
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
				// // System.out.println(args[i]+"---------nextFilestarts Here---------------");
				// // put images in the que and return next file in the path from string args
				// filename = images.getNextFile(args[0]);
				// if (filename != null) {
				// if (!DemoGui.currentStatus.getText().contains("Finishing")) {
				// DemoGui.currentStatus
				// .setText("Finding Chromosomes in slide image: " + filename);
				// }
				// Extractor extractor = new Extractor();
				// // use the current image in the que and create a slideImage
				// GeneticSlideImage image = new GeneticSlideImage(filename);
				// // extract the background from the image
				// extractor.removeBackground(image);
				// // get clusters from the image and keep a count of how many
				// frame.targetsFound += extractor.findClusters(image);
				// // pass the list of clusters on to slidelist
				// ChromosomeList slideList1 = new ChromosomeList(extractor.getClusterList(),
				// image);
				//
				// for(int i=0;i<slideList1.getChromosomeList().size();i++){
				// LinkedList<PointList>
				// cutList=ClusterSplitter.getSplitPoints(slideList1.getChromosomeList().get(i),
				// (int) Math.round(image.getChromoWidth()/3));
				// if(!cutList.isEmpty()){
				// int newChromosomes=extractor.splitClusters(slideList1.getChromosomeList().get(i),
				// cutList, image);
				// if(newChromosomes>1){
				// slideList1.getChromosomeList().remove(i);
				// }
				// }
				// }
				// ChromosomeList splitList=new ChromosomeList(extractor.getSplitList(),image);
				// splitList.calcMedialAxis(image);
				// for(int i=0;i<splitList.getChromosomeList().size();i++){
				// MedialAxisGraph
				// tempGraph=splitList.getChromosomeList().get(i).getMedialAxisGraph();
				// MirrorSplit mirror=new MirrorSplit(tempGraph);
				// splitList.getChromosomeList().get(i).setPaintPoints(mirror.getAllProblemDistances(splitList.getChromosomeList().get(i).getBounds(),
				// tempGraph.getAxisGraph(), image.getChromoWidth(), tempGraph.getDistanceMap()));
				// }
				// frame.imgCount.setText("Writing Splits" + splitList.size() + " images. ");
				// splitList.printProblemAxisWithMedialAxis(image,false);
				//
				// // test for split lines to shapdata/keep
				// //slideList1.splitNWrite(image);
				// slideList1.calcMedialAxis(image);
				// for(int i=0;i<slideList1.getChromosomeList().size();i++){
				// MedialAxisGraph
				// tempGraph=slideList1.getChromosomeList().get(i).getMedialAxisGraph();
				// MirrorSplit mirror=new MirrorSplit(tempGraph);
				// slideList1.getChromosomeList().get(i).setPaintPoints(mirror.getAllProblemDistances(slideList1.getChromosomeList().get(i).getBounds(),
				// tempGraph.getAxisGraph(), image.getChromoWidth(), tempGraph.getDistanceMap()));
				// }
				// frame.imgCount.setText("Writing " + slideList1.size() + " images. ");
				// slideList1.printProblemAxisWithMedialAxis(image,false);
				//
				// frame.imgCount.setText(frame.targetsFound + " Chromosomes found in "
				// + (++frame.imgCounter) + " slides read so far.");
				// if (!DemoGui.currentStatus.getText().contains("Finishing")) {
				// DemoGui.currentStatus.setText("Waiting for images");
				// } else {
				// DemoGui.currentStatus.setText("Finished looking at img"
				// + filename + " shutting down.");
				// }
				// }
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
		DemoGui.currentStatusLabel.setText("Finishing current image search and shutting down.");
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

	public void initButtonActions() {
		extractButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				extractButtonActionPerformed(evt);

			}

		});

		distanceMapButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				distanceMapButtonActionPerformed(evt);

			}

		});

		medialAxisButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				medialAxisButtonActionPerformed(evt);

			}

		});

		cleanMedialAxisButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				cleanMedialAxisButtonActionPerformed(evt);

			}

		});

		smallSplitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				smallSplitButtonActionPerformed(evt);

			}

		});

		orthoLinesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				orthoLinesButtonActionPerformed(evt);

			}

		});

		startPointsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				startPointsButtonActionPerformed(evt);

			}

		});

		projectionsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				projectionsButtonActionPerformed(evt);

			}

		});

		darkBandsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				darkBandsButtonActionPerformed(evt);

			}

		});

		linearizeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				linearizeButtonActionPerformed(evt);

			}

		});

		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				resetButtonActionPerformed(evt);

			}

		});
	}

	protected void resetButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void linearizeButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void darkBandsButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void projectionsButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void startPointsButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void orthoLinesButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void smallSplitButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void cleanMedialAxisButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void medialAxisButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void distanceMapButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	protected void extractButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}
}