package Demo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class ImagesPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage tempBuff;
	
	public ImagesPanel(){
		super();
		tempBuff=new BufferedImage(600,600,BufferedImage.TYPE_3BYTE_BGR);
	}
    @Override public void paintComponent(Graphics g) {
         super.paintComponent(g);    // paints background
 		
		g.drawImage(tempBuff,0,0, null);
    }
    public void writeNewImage(BufferedImage image){
    	tempBuff=image;
    	this.setPreferredSize(new Dimension(tempBuff.getWidth(),tempBuff.getHeight()));
    	this.repaint();
    }

}
