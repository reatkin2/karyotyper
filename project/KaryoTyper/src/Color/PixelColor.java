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
    public static int getRGBTotal(Color temp){
    	return (temp.getRed()+temp.getGreen()+temp.getBlue());
    }
    public static boolean isTargeTColorX(Color original,Color newPixel){
    	int allowDiff=13;
    	int redDiff=original.getRed()-newPixel.getRed();
    	int greenDiff=original.getGreen()-newPixel.getGreen();
    	int blueDiff=original.getBlue()-newPixel.getBlue();
		if((Math.abs(redDiff)<allowDiff
				&&Math.abs(greenDiff)<allowDiff
				&&(Math.abs(blueDiff))<allowDiff)){
			return true;
    	}
    	return false;
    }

    public static boolean isTextColor(Color original,Color newPixel){
    	int luminance=(newPixel.getRed()*76) + (newPixel.getGreen()*150) + (newPixel.getBlue()*29);
    	int redDiff=original.getRed()-newPixel.getRed();
    	int greenDiff=original.getGreen()-newPixel.getGreen();
    	int blueDiff=original.getBlue()-newPixel.getBlue();
    	int combinedDiff=Math.abs(redDiff)+Math.abs(greenDiff)+Math.abs(blueDiff);
    	if((combinedDiff<50&&luminance<40000)||(combinedDiff<30)){//combd1 170  combd2 40
			return true;
    	}
    	return false;
    }

    // was isTargeTColor2(Color original,Color newPixel)
    public static boolean isBackGroundColor(Color newPixel,int threshold){
		double tempGreyPixel=(.299*newPixel.getRed())+(.587*newPixel.getGreen())+(.114*newPixel.getBlue());
		if(tempGreyPixel>threshold){
			return true;
		}

    	return false;
    }
    public static boolean isTargeTColorDepreciated(Color original,Color newPixel){
    	int allowDiff=20;
    	int redDiff=original.getRed()-newPixel.getRed();
    	int greenDiff=original.getGreen()-newPixel.getGreen();
    	int blueDiff=original.getBlue()-newPixel.getBlue();
		if((Math.abs(redDiff)>allowDiff&&Math.abs(greenDiff)>allowDiff)
				||(Math.abs(blueDiff)>allowDiff&&Math.abs(greenDiff)>allowDiff)
				||(Math.abs(redDiff)>allowDiff&&Math.abs(blueDiff)>allowDiff)){
			return true;

    	}
    	return false;
    }

}
