package Demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.LinkedList;

import javax.swing.JPanel;

import chromosome.ChromosomeList;
import chromosome.GeneticSlideImage;

public class ImagesPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<BufferedImage> imageList;
	private LinkedList<Rectangle> imagePosition;
	private LinkedList<Boolean> selectedList;
	public ImagesPanel(){
		super();
		imageList=null;
		imagePosition=new LinkedList<Rectangle>();
		selectedList=new LinkedList<Boolean>();
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				Point panelLocation=new Point(arg0.getLocationOnScreen().x-arg0.getComponent().getLocationOnScreen().x,
						arg0.getLocationOnScreen().y-arg0.getComponent().getLocationOnScreen().y);
				markSelected(panelLocation);
				System.out.println("mouse clicked in image panel "+panelLocation.toString());
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

	}
    @Override public void paintComponent(Graphics g) {
         super.paintComponent(g);    // paints background
     	int width=0;
     	int height=0;
     	int rowHeight=0;
     	if(imageList!=null){
			imagePosition=new LinkedList<Rectangle>();     		
 			for(int i=0;i<imageList.size();i++){
 				BufferedImage tempImage=imageList.get(i);
 				//TODO(aamcknig): what about images larger than 600
 				if(imageList.get(i).getWidth()+width+5>600){
 					if(width==0){
 						width=5;
 						if(this.selectedList.get(i)){
 							g.setColor(Color.GREEN);
 							g.fillRect(width-3, height-3, tempImage.getWidth()+6,tempImage.getHeight()+6);
 						}
 						imagePosition.add(new Rectangle(width,height,tempImage.getWidth(),tempImage.getHeight()));
 						g.drawImage(tempImage,width,height, null);
 						width=0;
 	 					height+=5+tempImage.getHeight();
 					}
 					else{
 						width=5;
 						if(this.selectedList.get(i)){
 							g.setColor(Color.GREEN);
 							g.fillRect(width-3, height+5+rowHeight-3, tempImage.getWidth()+6,tempImage.getHeight()+6);
 						}
 						imagePosition.add(new Rectangle(width,height+5+rowHeight,tempImage.getWidth(),tempImage.getHeight()));
						g.drawImage(tempImage,0,height+5+rowHeight, null);
						width+=5+tempImage.getWidth();
 	 					height+=5+rowHeight;
 	 					rowHeight=tempImage.getHeight();
 					}

 				}
 				else{
					if(this.selectedList.get(i)){
						g.setColor(Color.GREEN);
						g.fillRect(width-3, height-3, tempImage.getWidth()+6,tempImage.getHeight()+6);
					}
					imagePosition.add(new Rectangle(width,height,tempImage.getWidth(),tempImage.getHeight()));
					g.drawImage(tempImage,width,height, null);
 					width+=5+tempImage.getWidth();
 					if(rowHeight<tempImage.getHeight()){
 						rowHeight=tempImage.getHeight();
 					}
 				}
 				
 			}
     	}

    }
    public void writeNewImage(LinkedList<BufferedImage> images){
    	int width=0;
    	int height=0;
    	int rowHeight=0;
    	int maxWidth=0;
    	this.selectedList=new LinkedList<Boolean>();
    	this.imageList=images;
    	if(imageList!=null){
			for(int i=0;i<imageList.size();i++){
				this.selectedList.add(false);
				BufferedImage tempImage=imageList.get(i);
				if(imageList.get(i).getWidth()+width+5>600){
					height+=5+rowHeight;
					rowHeight=tempImage.getHeight();
					width=5+tempImage.getWidth();
				}
				else{
					width+=5+tempImage.getWidth();
					if(rowHeight<tempImage.getHeight()){
						rowHeight=tempImage.getHeight();
					}
				}
				if(maxWidth<imageList.get(i).getWidth()){
					maxWidth=imageList.get(i).getWidth();
				}
				
			}
			height+=rowHeight;
			if(maxWidth>600){
				this.setPreferredSize(new Dimension(maxWidth,height));
			}
			else{
				this.setPreferredSize(new Dimension(600,height));				
			}
        	this.repaint();

    	}
    }
	public void markSelected(Point screenLocation) {
		for(int i=0;i<this.imagePosition.size();i++){
			if(imagePosition.get(i).contains(screenLocation)){
				if(this.selectedList.get(i)){
					this.selectedList.set(i, false);
				}
				else{
					this.selectedList.set(i,true);
				}
				this.repaint();
			}
		}
	}
	public boolean isSelected(int position){
		return selectedList.get(position);
	}


}
