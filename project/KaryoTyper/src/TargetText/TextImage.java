package TargetText;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import Color.PixelColor;
import Target.TargetImage;
import Target.TargetShape;
import basicObjects.AroundPixel;

//import Shape.Shape;

public class TextImage {
	private int rotationCount;
	private AroundPixel aroundPixel;
	private TargetImage img;
	private TargetText myText;
	
	public TextImage(TargetImage image,int rotationCount){
		myText = new TargetText();
		aroundPixel = new AroundPixel();
		img=image;
		this.rotationCount=rotationCount;
		
	}
	
	public TargetText getText(){
		return myText;
	}
    public void writeTextImage(TargetShape tempShape){
		try {
		    // retrieve image
			TargetShape tempTrimmed=new TargetShape(tempShape);
			//tempTrimmed.trimShape((int)Math.round(.4*img.getPixelsPerFoot()*Math.sqrt(Math.pow(tempTrimmed.getSize().x,2) + Math.pow(tempTrimmed.getSize().y,2) )));
			tempTrimmed.trimShape((int)Math.round(.4*img.getPixelsPerFoot() ));
			short targetText[][]=this.pullTextOutOfTarget(new Rectangle(tempTrimmed.getScreenCordinate().x,tempTrimmed.getScreenCordinate().y,tempTrimmed.getSize().x,tempTrimmed.getSize().y),tempTrimmed);
			BufferedImage tempImg=getTextImage(targetText);
			//tempImg=this.makeRotatedImage(tempImg);
			//BufferedImage tempImg2=new BufferedImage(
			//BufferedImage tempImg=img.getSubImage(tempShape);
			File outputfile = new File("shapeData/textData/"+tempShape.getTitle().substring(tempShape.getTitle().indexOf("ima"),tempShape.getTitle().indexOf(".jpg"))+"_"+(tempShape.getTargetNimageID())+".png");//,tempShape.getTitle().indexOf(".jpg"))+"_"+(inImageTargetCount)+".png"
			ImageIO.write(tempImg, "png", outputfile);

    		Process ocrProcess=Runtime.getRuntime().exec("python ./ocr.py "+rotationCount+" "+outputfile.getCanonicalPath(), null);
    	    BufferedReader procOutStream = new BufferedReader
    	        (new InputStreamReader(ocrProcess.getInputStream()));
			String ocrOutPut =procOutStream.readLine();
			System.out.println(ocrOutPut);
    	    String shapeTextData[]=ocrOutPut.split(":");
    	    String shapeChar="";
    	    String textConfidence="";
    	    String textAngle="";
    	    if(shapeTextData.length==3){
    	    	shapeChar=shapeTextData[0];
    	    	textConfidence=shapeTextData[1];
    	    	textAngle=shapeTextData[2];
    	    }
    	    if(shapeChar.length()==1){
    	    	myText.setAngle(Integer.parseInt(textAngle));
    	    	myText.setTextConfidence(Integer.parseInt(textConfidence));
    	    	myText.setOrientation(img.getHeading(),Integer.parseInt(textAngle),rotationCount);
    	    	myText.setTargetChar(shapeChar);   	    	
    	    	
    	    }
    	    if(ocrOutPut.equals("1")){
    	    	System.out.println("Couldn't open text image file:"+outputfile.getName()+" with ocr.py");
    	    }
    	        
		} catch (IOException e) {
		    System.out.println(e);
		}
    }
    private BufferedImage tryTextTarget(TargetShape testShape){
		BufferedImage tempImg=new BufferedImage(testShape.getSize().x,testShape.getSize().y,BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<testShape.getSize().x;i++){
			for(int j=0;j<testShape.getSize().y;j++){
				
				if(testShape.getValue(i,j))
					tempImg.setRGB(i,j, (Color.BLACK).getRGB());
				else{
					tempImg.setRGB(i,j, (Color.WHITE).getRGB());
				}
			}

		}
		return tempImg;
    }
    public void tryWriteTargetTextImage(TargetShape tempShape){
		try {
		    // retrieve image
			BufferedImage tempImg=this.tryTextTarget(tempShape);
			//tempImg=this.makeRotatedImage(tempImg);
			//BufferedImage tempImg2=new BufferedImage(
			//BufferedImage tempImg=img.getSubImage(tempShape);
			File outputfile = new File("shapeData/tryTargetText/"+tempShape.getTitle().substring(tempShape.getTitle().indexOf("ima"),tempShape.getTitle().indexOf(".jpg"))+"_"+(tempShape.getTargetNimageID())+".png");//,tempShape.getTitle().indexOf(".jpg"))+"_"+(inImageTargetCount)+".png"
			ImageIO.write(tempImg, "png", outputfile);

    		Process ocrProcess=Runtime.getRuntime().exec("python ./ocr.py "+rotationCount+" "+outputfile.getCanonicalPath(), null);
    	    BufferedReader procOutStream = new BufferedReader
    	        (new InputStreamReader(ocrProcess.getInputStream()));
			String ocrOutPut =procOutStream.readLine();
			System.out.println(ocrOutPut);
    	    String shapeTextData[]=ocrOutPut.split(":");
    	    String shapeChar="";
    	    String textConfidence="";
    	    String textAngle="";
    	    if(shapeTextData.length==3){
    	    	shapeChar=shapeTextData[0];
    	    	textConfidence=shapeTextData[1];
    	    	textAngle=shapeTextData[2];
    	    }
    	    if(shapeChar.length()==1){
    	    	myText.setAngle(Integer.parseInt(textAngle));
    	    	myText.setTextConfidence(Integer.parseInt(textConfidence));
    	    	myText.setOrientation(img.getHeading(),Integer.parseInt(textAngle),rotationCount);
    	    	myText.setTargetChar(shapeChar);   	    	
    	    	
    	    }
    	    if(ocrOutPut.equals("1")){
    	    	System.out.println("Couldn't open text image file:"+outputfile.getName()+" with ocr.py");
    	    }
    	        
		} catch (IOException e) {
		    System.out.println(e);
		}
    }

    private BufferedImage getTextImage(short[][] targetText){
		BufferedImage tempImg=new BufferedImage(targetText.length,targetText[0].length,BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<targetText.length;i++){
			for(int j=0;j<targetText[0].length;j++){
				
				if(targetText[i][j]>-1)
					tempImg.setRGB(i,j, (Color.BLACK).getRGB());
				else{
					tempImg.setRGB(i,j, (Color.WHITE).getRGB());
				}
			}

		}
		return tempImg;
    }
    
    /***
     * this method trys to eliminate all the colors that appear on the edge of the target and
     * keep all the colors that appear in the center of the target
     * @param bounds the bounds of the trimmed target image
     * @param inThisShape the trimmed target shape
     * @return  a 2d array of integers that represent a color map of the text colors found and all the removed pixels set to -8
     */
    private short[][] pullTextOutOfTarget(Rectangle bounds,TargetShape inThisShape){
    	//Rectangle centerImage=new Rectangle((int)Math.round((bounds.getWidth()/2)-(bounds.getWidth()/4)),(int)Math.round((bounds.getHeight()/2)-(bounds.getHeight()/4)),
    	//						(int)Math.round(bounds.getWidth()/2),(int)Math.round(bounds.getHeight()/2));
        //LinkedList<Shape> allShapes=new LinkedList<Shape>();//create a linked list to store shapes in
    	LinkedList<Color> foundColors=new LinkedList<Color>();//list for storeing all found colors
    	LinkedList<Short> removeEdgeColors=new LinkedList<Short>();
    	LinkedList<Short> keepCenterColors=new LinkedList<Short>();    	
    	short lastPaintThisRow=-5;
    	boolean beforeFirstEdge=true;
    	short paintColor=0;//number to paint the current color to canvas
        int k=0;//is the count of colors found and the paint number of last found color
    	short[][] canvas = new short[bounds.width][bounds.height];//create a 2d integer array for painting numbers on
        for(int i=0;i<bounds.width;i++)//loop width
            for(int j=0;j<bounds.height;j++)//loop height
                canvas[i][j]=-5;//initialize all values of canvas to -5 meaning not checked yet
    	for(int i=bounds.y;i<=(bounds.y+bounds.height-1)&&i<img.getImgHeight();i++){//loop thru the y bounds//this.screenChecked[0].length
    		for(int j=bounds.x;(j<=bounds.x+bounds.width-1)&&i<img.getImgWidth();j++){//loop thru the x bounds//this.screenChecked.length
    			if(canvas[j-bounds.x][i-bounds.y]==-5){//if the spot hasn't been checked yet
    				//if this is part of the target
    				if(inThisShape.getValue(j-bounds.x,i-bounds.y)){
	    				Color temp=img.getColorAt(j,i);//get the color of the spot
		    			if(foundColors.isEmpty()){//if list is empty
		    				foundColors.add(temp);//add first color
		    				paintColor=(short)k;//increment k to 1
		    			}
		    			else if(containsTextColor(temp,foundColors)>=0){//if we already have this color
		    				paintColor=(short)(containsTextColor(temp,foundColors)+1);//set paintColor to color we already have
		    			}
		    			else{//if we got a new color
		    				foundColors.add(temp);//add the color
		    				paintColor=(short)++k;//increment k
		    			}
		    			if(beforeFirstEdge){
		    				removeEdgeColors.add(paintColor);
		    				beforeFirstEdge=false;
		    			}
		    			canvas[j-bounds.x][i-bounds.y]=paintColor;//paint current canvas spot to paintColor our paint number
		    			lastPaintThisRow=paintColor;
		    			/*
		    			 * add all connected matching color pixels in bonds to the canvas
		    			 *check the canvas for same color pixel connected to
		    			 *point j,i and matching the color
		    			 *start marking at j,i on the canvas
		    			 */
		    			canvas=this.getMatchingPixelText(new Rectangle(bounds.x,bounds.y,bounds.width,bounds.height),temp,
		    					new Point(j,i),new Point(j-bounds.x,i-bounds.y),canvas,paintColor,new Color(0,200,200));
    				}
    				//if the current pixel is not part of the target then check if its the text in the middle of the target
    				else if(this.checkForSpotInText(inThisShape, j-bounds.x,i-bounds.y)){
	    				Color temp=img.getColorAt(j,i);//get the color of the spot
		    			if(foundColors.isEmpty()){//if list is empty
		    				foundColors.add(temp);//add first color
		    				paintColor=(short)k;//increment k to 1
		    			}
		    			else if(containsTextColor(temp,foundColors)>=0){//if we already have this color
		    				paintColor=(short)(containsTextColor(temp,foundColors)+1);//set paintColor to color we already have
		    			}
		    			else{//if we got a new color
		    				foundColors.add(temp);//add the color
		    				paintColor=(short)++k;//increment k
		    			}
		    			canvas[j-bounds.x][i-bounds.y]=paintColor;//paint current canvas spot to paintColor our paint number
		    			keepCenterColors.add(paintColor);
		    			//lastPaintThisRow=paintColor;
		    			/*
		    			 * add all connected matching color pixels in bonds to the canvas
		    			 *check the canvas for same color pixel connected to
		    			 *point j,i and matching the color
		    			 *start marking at j,i on the canvas
		    			 */
		    			canvas=this.getMatchingPixelText(new Rectangle(bounds.x,bounds.y,bounds.width,bounds.height),temp,
		    					new Point(j,i),new Point(j-bounds.x,i-bounds.y),canvas,paintColor,new Color(0,200,200));
    				}
	    		}
    		}
    		beforeFirstEdge=true;
    		removeEdgeColors.add(lastPaintThisRow);
    	}
    	//add loop of color removalls
    	for(int i=0;i<keepCenterColors.size();i++){
    		while(removeEdgeColors.removeFirstOccurrence(keepCenterColors.get(i)));
    	}
        for(int i=0;i<bounds.width;i++){//loop width
            for(int j=0;j<bounds.height;j++){//loop height
            	for(int l=0;l<removeEdgeColors.size();l++){
            		if(canvas[i][j]==removeEdgeColors.get(l)){
            			canvas[i][j]=-8;
            		}
            	}
            }
        }
    	
    	return canvas;//return list of shapes
    	/*
     	 * 
    	 * changed number 4 to -5 currently testing
    	 */
    }    
    private int containsTextColor(Color temp,LinkedList<Color> list){
    	if (!list.isEmpty()){
    		for(int i=0;i<list.size();i++){
    			if(PixelColor.isTextColor(list.get(i), temp)){
    				return i;
    			}
    		}
    	}
    	return -1;
    }
    private short[][] getMatchingPixelText(Rectangle bounds,Color colorOItem,Point xyCor,Point xyCanvas,short[][] canvas,short shapeID,Color colorLeft){
//    	if(count>7){
//    		if(match/count<0.70){
//    			return canvas;
//    		}
//    	}
    		if(colorLeft.getRed()<2){
	    	for(int i=0; i<8;i++){//loop 8 times once for every position around the center pixel
	    		/*
	    		 *if the spot to be checked is inside of the bounds 
	    		 *and if the pixel is inside the visible resolution of the screen
	    		 */
	    			if(bounds.contains(new Point((xyCanvas.x+this.aroundPixel.getPos(i).x-1),(xyCanvas.y+this.aroundPixel.getPos(i).y-1)))
		            		&&xyCor.x+this.aroundPixel.getPos(i).x<img.getImgWidth()&&xyCor.y+this.aroundPixel.getPos(i).y<img.getImgHeight()
		            		&&xyCor.x>0&&xyCor.y>0&&canvas.length<xyCanvas.x+this.aroundPixel.getPos(i).x&&canvas[0].length<xyCanvas.y+this.aroundPixel.getPos(i).y){
	    	    		//if(!screenChecked[xyCor.x+this.aroundPixel.getPos(i).x][xyCor.y+this.aroundPixel.getPos(i).y]){
		        		//if the spot to be checked value is -5 meaning it hasnt been checked yet
			        	if(canvas[xyCanvas.x+this.aroundPixel.getPos(i).x][xyCanvas.y+this.aroundPixel.getPos(i).y]==-5){
			        		//System.out.println("Checking color at pixel:"+(xyCor.x+this.aroundPixel.getPos(i).x)+","+(xyCor.y+this.aroundPixel.getPos(i).y));
			        		//if the pixel at the position aroundDot matches the color
			        		Color temp=img.getColorAt(xyCor.x+this.aroundPixel.getPos(i).x,xyCor.y+this.aroundPixel.getPos(i).y);
			        		if(PixelColor.isTextColor(colorOItem,temp)){
				    			//screenChecked[xyCor.x+this.aroundPixel.getPos(i).x][xyCor.y+this.aroundPixel.getPos(i).y]=true;
				        		//this.currPixelCount++;
			        			//HORSE.mouseMove(xyCor.x+this.aroundPixel.getPos(i).x,xyCor.y+this.aroundPixel.getPos(i).y);
			        			//HORSE.delay(1);
			        			//paint the canvas at the respectful position on 2d canvas to the shapeID number
			        			canvas[xyCanvas.x+this.aroundPixel.getPos(i).x][xyCanvas.y+this.aroundPixel.getPos(i).y]=shapeID;
			        	    	//System.out.println(" check:"+xyCanvas.x+","+xyCanvas.y+"@"+xyCor.x+","+xyCor.y);
			        			//recursively call this method with the new position on screen and on 2d canvas to be checked around
			     		        getMatchingPixelText(bounds,temp,new Point(xyCor.x+this.aroundPixel.getPos(i).x,xyCor.y+this.aroundPixel.getPos(i).y),new Point(xyCanvas.x+this.aroundPixel.getPos(i).x,xyCanvas.y+this.aroundPixel.getPos(i).y),canvas,shapeID,colorLeft);
			        		}
			        		else{
				        		colorLeft=new Color(colorLeft.getRed()+1,colorLeft.getGreen(),colorLeft.getBlue());
				        		//this.currPixelCount++;
			        			//HORSE.mouseMove(xyCor.x+this.aroundPixel.getPos(i).x,xyCor.y+this.aroundPixel.getPos(i).y);
			        			//HORSE.delay(1);
			        			//paint the canvas at the respectful position on 2d canvas to the shapeID number
			        			canvas[xyCanvas.x+this.aroundPixel.getPos(i).x][xyCanvas.y+this.aroundPixel.getPos(i).y]=shapeID;
			        	    	//System.out.println(" check:"+xyCanvas.x+","+xyCanvas.y+"@"+xyCor.x+","+xyCor.y);
			        			//recursively call this method with the new position on screen and on 2d canvas to be checked around
			      		        getMatchingPixelText(bounds,colorOItem,new Point(xyCor.x+this.aroundPixel.getPos(i).x,xyCor.y+this.aroundPixel.getPos(i).y),new Point(xyCanvas.x+this.aroundPixel.getPos(i).x,xyCanvas.y+this.aroundPixel.getPos(i).y),canvas,shapeID,colorLeft);
			        		}

			        		
			        	}
			        }
		    	}
	    	}
	    return canvas;//return the 2d integer canvas
    	/*
    	 * changed 4 to -5 and now testing
    	 * this change
    	 */
    }
    private boolean checkForSpotInText(TargetShape chessboard,int x, int y){
		if(isQueenPosGood(chessboard,x+1,y,1,0)
				&&isQueenPosGood(chessboard,x,y+1,0,1)
				&&isQueenPosGood(chessboard,x,y-1,0,-1)
				&&isQueenPosGood(chessboard,x-1,y,-1,0)
				&&isQueenPosGood(chessboard,x+1,y+1,1,1)
				&&isQueenPosGood(chessboard,x-1,y-1,-1,-1)
				&&isQueenPosGood(chessboard,x-1,y+1,-1,1)
				&&isQueenPosGood(chessboard,x+1,y-1,1,-1)){
			return true;
		}
		return false;
    }
    private boolean isQueenPosGood(TargetShape chessboard,int x,int y,int dirX,int dirY){
    	if(x<0||y<0||x>chessboard.getSize().x-1||y>chessboard.getSize().y-1){
    		return false;
    	}
    	else if(!chessboard.getValue(x,y)){
    		return isQueenPosGood(chessboard,x+dirX,y+dirY,dirX,dirY);
    	}
    	else{
    		return true;
    	}
    }

//  private int testTextImage(short[][] targetText){
//		//BufferedImage tempImg=new BufferedImage(targetText.length,targetText[0].length,BufferedImage.TYPE_USHORT_GRAY);
//		int textPixelCount=0;
//  	for(int i=0;i<targetText.length;i++){
//			for(int j=0;j<targetText[0].length;j++){
//				if(targetText[i][j]>1)
//					textPixelCount++;
//				else{
//					//tempImg.setRGB(i,j, (Color.BLACK).getRGB());
//				}
//			}
//
//		}
//		System.out.println("pixelsNText: "+textPixelCount);
//		return textPixelCount;
//  }


}
