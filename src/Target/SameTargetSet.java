package Target;

import java.awt.Color;
import java.util.LinkedList;

import Color.PixelColor;
import basicObjects.LatLongPoint;
import basicObjects.StringBuckets;


public class SameTargetSet {
	private LatLongPoint avgLocation;
	private LinkedList<TargetShape> shapeList;
	private Color shapeColor;
	private Color textColor;
	private StringBuckets targetColorMC;
	private StringBuckets textColorMC;
	private StringBuckets textOrientationMC;
	private StringBuckets textCharMC;
	private int textFoundCount;
	private int distanceToIncludeTargets;
	public SameTargetSet(int distanceIncludeTargets){
		this.distanceToIncludeTargets=distanceIncludeTargets;
		avgLocation=new LatLongPoint(0,0);
		shapeList=new LinkedList<TargetShape>();
		textFoundCount=0;
		targetColorMC= new StringBuckets();
		textColorMC= new StringBuckets();
		textOrientationMC= new StringBuckets();
		textCharMC= new StringBuckets();
	}
	public LatLongPoint getLocation(){
		return this.avgLocation;
	}
//	public LinkedList<TargetShape getList(){
//		return shapeList;
//	}
	public TargetShape get(int index){
		return shapeList.get(index);
	}
	public void add(TargetShape newShape){
		calcNewAvg(new LatLongPoint(newShape.getLong(),newShape.getLat()));
		this.shapeList.add(newShape);
		shapeColor=shapeList.get(0).getColor();
		textColor=shapeList.get(0).getColor();
		recalcAvgLoc();
		if(newShape.getText().getTargetText().length()==1){//check this working major overhall 6/1/2012
			this.textFoundCount++;
			textCharMC.dropNBucket(newShape.getText().getTargetText());
			this.textOrientationMC.dropNBucket(newShape.getText().getLetterOrientation());
		}
		if(!((PixelColor.getColorNString(newShape.getText().getBuckets().getPopulaColor(0,newShape.getBuckets().getPopulaColor(0)))).equals("No Color"))){
			textColorMC.dropNBucket(PixelColor.getColorNString(newShape.getText().getBuckets().getPopulaColor(0,newShape.getBuckets().getPopulaColor(0))));
		}
		if(!((PixelColor.getColorNString(newShape.getBuckets().getPopulaColor(0))).equals("No Color"))){
			targetColorMC.dropNBucket(PixelColor.getColorNString(newShape.getBuckets().getPopulaColor(0)));
		}
		
	}
	public void calcNewAvg(LatLongPoint newLoc){
		if(shapeList.isEmpty()){
			this.avgLocation=newLoc;
		}
		else{
			this.avgLocation.setLat(((this.avgLocation.getLat()*shapeList.size())+newLoc.getLat())/(shapeList.size()+1));
			this.avgLocation.setLong(((this.avgLocation.getLong()*shapeList.size())+newLoc.getLong())/(shapeList.size()+1));	
		}
	}
	public int size(){
		return this.shapeList.size();
	}
	public void recalcAvgLoc(){
		LatLongPoint temp=new LatLongPoint(0,0);
		LinkedList<LatLongPoint> goodPoints=new LinkedList<LatLongPoint>();
		if(this.size()>2){
			for(int i=0;i<this.shapeList.size();i++){
				int goodDistance=0;
				for(int j=0;j<this.shapeList.size();j++){
					if(shapeList.get(i).getTargetLatLong().distance(shapeList.get(j).getTargetLatLong())<30){
						goodDistance++;
					}
				}
				if(goodDistance>=((this.size()-1)/2)){
					goodPoints.add(getLocation());
				}
			}

			for(int i=0;i<goodPoints.size();i++){
				temp.setLat(temp.getLat()+goodPoints.get(i).getLat());
				temp.setLong(temp.getLong()+goodPoints.get(i).getLong());			
			}
			this.avgLocation.setLat(temp.getLat()/goodPoints.size());
			this.avgLocation.setLong(temp.getLong()/goodPoints.size());
		}
	}

	private double distanceTo(int x, int y,int z,int x2,int y2,int z2){
		return Math.sqrt((2*(x-x2)*(x-x2)) + (4*(y-y2)*(y-y2)) + (3*(z-z2)*(z-z2)));
	}
	public Color getShapeRGB(){
		return this.shapeColor;
	}
	public boolean willGoInSet(double feetPerDegreeLatLong,TargetShape newShape){
		if(shapeList.size()>0){
		  if(((shapeList.get(0).getTargetLatLong().distance(newShape.getTargetLatLong()))*feetPerDegreeLatLong)<distanceToIncludeTargets){
					//&&Math.abs(shapeList.get(0).getImgHeading()-newShape.getImgHeading())<30){
			  if(!newShape.getText().getTargetText().equals("")){
			  //if((PixelColor.getColorNString(newShape.getBuckets().getPopulaColor(0))).equals(PixelColor.getColorNString(shapeList.get(0).getBuckets().getPopulaColor(0)))){
				  return true;
			  }
		  }
		}
		return false;//recently changed from true to false I think this is/was a bug when set to true

	}
	public boolean isFalsePositive(){
		if(shapeList.size()>1){
			if((double)this.textFoundCount/(double)shapeList.size()<.5){
				return true;
			}
		}
		else if(this.textFoundCount==0){
			return true;
		}
		return false;
	}
	public String getTargetColor(){
		return this.targetColorMC.getPopularString(0);
	}
	public String getTextColor(){
		return this.textColorMC.getPopularString(0);
	}
	public String getTextChar(){
		int numberEqual=-1;
		int confidence=0;
		String temp=this.textCharMC.getPopularString(0);
		if(temp!=null){
			for(int i=0;i<this.textCharMC.getBucketCount();i++){
				if(this.textCharMC.getPopularStringOccured(0).getCount()==this.textCharMC.getPopularStringOccured(i).getCount()){
					numberEqual++;
				}
			}
			if(numberEqual<=0){
				return temp;
			}
			for(int i=0;i<=numberEqual;i++){
				for(int j=0;j<shapeList.size();j++){
					if(this.textCharMC.getPopularString(i).equals(shapeList.get(j).getText().getTargetText())){
						if(confidence<=shapeList.get(j).getText().getTextConfidence()){
							temp=this.textCharMC.getPopularString(i);
							confidence=shapeList.get(j).getText().getTextConfidence();
						}
					}
				}
			}
		}
		return temp;
	}
	public String getTextOrientation(){
		return this.textOrientationMC.getPopularString(0);
	}
}
