
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*; 
 
public class RunTargetGetter extends JFrame{
	/**
	 * 
	 */
	private static boolean closing;//=false;
	private static final long serialVersionUID = 1L;
	public int imgCounter;
	public int targetsFound;
	public static JLabel currentStatus;

	public RunTargetGetter(String string) {
		super(string);
		RunTargetGetter.closing=false;
		imgCounter=0;
		targetsFound=0;

	}

	//Create and set up the window.
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length<1){
			System.out.println("add the commandline arugments <imagesPath>");
			System.out.println("Ex.  java -jar *.jar /home/arc/images/");
		}
		else{
			System.out.println(args[0]);

			//int imgCounter=0;
			 RunTargetGetter frame = new RunTargetGetter("Target Getter GUI"); 
			 frame.setLayout(new FlowLayout());
			 JPanel upper=new JPanel();
			 JPanel lower=new JPanel();
			 frame.setLocation(200,200);
			 Dimension minSize = new Dimension(400,200);
			 frame.setMinimumSize(minSize);
			 JLabel imgCount=new JLabel("Currently No Images In Directory");
			 RunTargetGetter.currentStatus=new JLabel("Waiting for images");
			 RunTargetGetter.currentStatus.setForeground(Color.RED);
			 frame.add(upper);
			 frame.add(lower);
			 upper.add(RunTargetGetter.currentStatus);
			 lower.add(imgCount);
			 frame.setVisible(true);
			 //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			 
			//create custom close operation
			frame.addWindowListener(new WindowAdapter()
			{
			      public void windowClosing(WindowEvent e)
			      {
			          exitProcedure();
			      }
	
			});
			// TODO Auto-generated method stub
			TargetGetter test2=new TargetGetter(args[0]);
			String filename;

			while(!RunTargetGetter.closing){
				//System.out.println(args[i]+"---------nextFilestarts Here---------------");

					filename=test2.getNewFiles(args[0]);
					if(filename!=null){
						if(!RunTargetGetter.currentStatus.getText().contains("Finishing")){
							RunTargetGetter.currentStatus.setText("Finding Chromosomes in slide image: "+filename);
						}
						frame.targetsFound+=test2.findBackground(filename);
						frame.targetsFound=test2.findChromosomes(filename, frame.targetsFound);
						test2.printChromosomes();

						imgCount.setText(frame.targetsFound+" Chromosomes found in "+(++frame.imgCounter)+" slides read so far.");
						if(!RunTargetGetter.currentStatus.getText().contains("Finishing")){
							RunTargetGetter.currentStatus.setText("Waiting for images");
						}
						else{
							RunTargetGetter.currentStatus.setText("Finished looking at img"+filename+" shutting down.");
						}
					}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
			
			test2.exit();
			frame.dispose();
		}
	}

	protected static void exitProcedure() {
		RunTargetGetter.closing=true;
		RunTargetGetter.currentStatus.setText("Finishing current image search and shutting down.");
	}

}
