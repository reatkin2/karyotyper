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
    public static String getColorNString(Color color4Str){
    	if(color4Str==null){
    		return "No Color";
    	}
    	int red=color4Str.getRed();
    	int blue=color4Str.getBlue();
    	int green=color4Str.getGreen();
    	if(Math.abs(red-blue)<=40&&Math.abs(red-green)<=40&&Math.abs(green-blue)<=40){
    		if((red+green+blue)>550){
    			return "White";
    		}
    	}
    	if(green>blue&&green>red){
			if(Math.abs(green-red)<40){
				return "Yellow";
			}
			else if(Math.abs(green-red)<70&&red>blue&&red>100){
				return "Yellow";
			}
			return "Green";
    	}
    	if(red>blue&&red>green){
    		if((Math.abs(green-red)<60)){
    			if(Math.abs(red-green)<20){
    				return "Yellow";	
    			}
    			else{
    				return "Orange";
    			}
    		}
    		else if(Math.abs(red-blue)<70){
    			return "Purple";
    		}
    		else{
    			return "Red";
    		}
    	}
    	if(blue>red&&blue>green){
    		return "Blue";
    	}
    	if(Math.abs(red-blue)<=30&&Math.abs(red-green)<=30&&Math.abs(green-blue)<=30){
    		if(red<140&&green<140&&blue<140){
    			return "Black";
    		}
    	}

    	if(red==green&&green>blue){
    		return "Yellow";
    	}
    	if(red==blue){
    		return "Purple";
    	}
    	if(green==blue){
    		return "Green";
    	}
    	if(Math.abs(red-blue)<70){
    		return "Purple";
    	}
    	return "Blue";

    }
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
