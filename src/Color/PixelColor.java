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
//    	int combinedDiff=Math.abs(redDiff)+Math.abs(greenDiff)+Math.abs(blueDiff);
    	//if(combinedDiff<30){
    		if((Math.abs(redDiff)<allowDiff
    				&&Math.abs(greenDiff)<allowDiff
    				&&(Math.abs(blueDiff))<allowDiff)){
    			return true;
//            		if(Math.abs(averageDiff-Math.abs(redDiff))<2&&Math.abs(averageDiff-Math.abs(greenDiff))<2&&Math.abs(averageDiff-Math.abs(blueDiff))<2)
//                	if(Math.abs(redDiff-blueDiff)<20){
//                    	if(Math.abs(redDiff-greenDiff)<20){
//                        	if(Math.abs(greenDiff-blueDiff)<20){
//                        		return true;
//                        		
//                        	}
//                    		
//                    	}
//                		
//                	}
        	}
    	//}
    	return false;
    }
    public static boolean isTextColor(Color original,Color newPixel){
//    	int allowDiff=45;//19380+38250+7395=65025
    	int luminance=(newPixel.getRed()*76) + (newPixel.getGreen()*150) + (newPixel.getBlue()*29);
    	int redDiff=original.getRed()-newPixel.getRed();
    	int greenDiff=original.getGreen()-newPixel.getGreen();
    	int blueDiff=original.getBlue()-newPixel.getBlue();
    	int combinedDiff=Math.abs(redDiff)+Math.abs(greenDiff)+Math.abs(blueDiff);
    	if((combinedDiff<50&&luminance<40000)||(combinedDiff<30)){//combd1 170  combd2 40
//    	if(Math.abs(newPixel.getBlue()-original.getBlue())<20
//    			&&Math.abs(newPixel.getGreen()-original.getGreen())<20
//    			&&Math.abs(newPixel.getRed()-original.getRed())<20){
//    		if((Math.abs(redDiff)<allowDiff
//    				&&Math.abs(greenDiff)<allowDiff
//    				&&(Math.abs(blueDiff))<allowDiff)){
    			return true;
//            		if(Math.abs(averageDiff-Math.abs(redDiff))<2&&Math.abs(averageDiff-Math.abs(greenDiff))<2&&Math.abs(averageDiff-Math.abs(blueDiff))<2)
//                	if(Math.abs(redDiff-blueDiff)<20){
//                    	if(Math.abs(redDiff-greenDiff)<20){
//                        	if(Math.abs(greenDiff-blueDiff)<20){
//                        		return true;
//                        		
//                        	}
//                    		
//                    	}
//                		
//                	}
        	}
    	//}
    	return false;
    }

//    public static boolean isTargeTColor2(Color original,Color newPixel){
//    	int redDiff=original.getRed()-newPixel.getRed();
//    	int greenDiff=original.getGreen()-newPixel.getGreen();
//    	int blueDiff=original.getBlue()-newPixel.getBlue();
//    	int combinedDiff=Math.abs(redDiff)+Math.abs(greenDiff)+Math.abs(blueDiff);
//    	int luminance=(newPixel.getRed()*76) + (newPixel.getGreen()*150) + (newPixel.getBlue()*29);
//    	if(luminance>42000){
//    		if(combinedDiff<150){
//    			return true;
//    		}
//        	if(Math.abs(greenDiff)<3){
//            	if(Math.abs(blueDiff)<3){
//                	if(Math.abs(redDiff-blueDiff)<20){
//                    	if(Math.abs(redDiff-greenDiff)<20){
//                        	if(Math.abs(greenDiff-blueDiff)<20){
//                        		return true;
//                        		
//                        	}
//                    		
//                    	}
//                		
//                	}
//            		
//            	}
        		
//        }
//    	return false;
//    }
// working color for getmatchingpixel
    //targets2 good
//	if((newPixel.getRed()>130&&Math.abs(newPixel.getGreen()-newPixel.getRed())>70)
//			||(newPixel.getGreen()>200&&Math.abs(newPixel.getGreen()-newPixel.getBlue())>100)
//			||(newPixel.getBlue()>100&&((newPixel.getBlue()-newPixel.getGreen()>80)||(newPixel.getBlue()-newPixel.getRed()>80)))
//			||(newPixel.getGreen()>200&&newPixel.getBlue()>200&&newPixel.getRed()>200)){
//		return true;

    public static boolean isTargeTColor2(Color newPixel){// was isTargeTColor2(Color original,Color newPixel)
//    	double red=original.getRed()/115.0;
//    	double green=original.getGreen()/130.0;
//    	double blue=original.getBlue()/80.0;
//    	int redBlueDiff=Math.abs(newPixel.getRed()-newPixel.getBlue());
    	if((newPixel.getTransparency()==0)||(newPixel.getRed()>250&& newPixel.getGreen()>250&& newPixel.getBlue()>250)){
    		return true;
        		
        }
    	return false;
    }
    public static boolean isTargeTColorDepreciated(Color original,Color newPixel){
    	int allowDiff=20;
    	int redDiff=original.getRed()-newPixel.getRed();
    	int greenDiff=original.getGreen()-newPixel.getGreen();
    	int blueDiff=original.getBlue()-newPixel.getBlue();
//    	int combinedDiff=Math.abs(redDiff)+Math.abs(greenDiff)+Math.abs(blueDiff);
    	//if(combinedDiff>40){
    		if((Math.abs(redDiff)>allowDiff&&Math.abs(greenDiff)>allowDiff)
    				||(Math.abs(blueDiff)>allowDiff&&Math.abs(greenDiff)>allowDiff)
    				||(Math.abs(redDiff)>allowDiff&&Math.abs(blueDiff)>allowDiff)){
    			return true;
//            		if(Math.abs(averageDiff-Math.abs(redDiff))<2&&Math.abs(averageDiff-Math.abs(greenDiff))<2&&Math.abs(averageDiff-Math.abs(blueDiff))<2)
//                	if(Math.abs(redDiff-blueDiff)<20){
//                    	if(Math.abs(redDiff-greenDiff)<20){
//                        	if(Math.abs(greenDiff-blueDiff)<20){
//                        		return true;
//                        		
//                        	}
//                    		
//                    	}
//                		
//                	}
        	}
    	//}
    	return false;
    }

}
