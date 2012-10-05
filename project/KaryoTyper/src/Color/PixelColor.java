package Color;
/*
 * IsColor.java
 *
 * Created on December 13, 2004, 10:33 AM
 */
import java.awt.Color;
/**
 *
 * @author  Andrew
 */
public class PixelColor {

   
    /**
     * this is the method for removing the background color by checking if
     * the current pixel color is above the grayscale color threshold for this image
     * @param newPixel the pixel to be checked if its part of the background
     * @param threshold the cut off threshold for the current image everything above
     * 	this value is the background
     * @return
     */
    public static boolean isBackGroundColor(Color newPixel,int threshold){
		double tempGreyPixel=(.299*newPixel.getRed())+(.587*newPixel.getGreen())+(.114*newPixel.getBlue());
		if(tempGreyPixel>threshold){
			return true;
		}

    	return false;
    }

}
