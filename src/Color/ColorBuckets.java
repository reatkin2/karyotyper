package Color;

import java.awt.Color;
import java.util.LinkedList;

public class ColorBuckets {
	private int[][][] buckets;
	private LinkedList<Color> top10;
	public ColorBuckets(){
		buckets= new int[10][10][10];
		top10=new LinkedList<Color>();
	}
	/***
	 * add a color to the buckets
	 * @param nextColor
	 */
	public void dropNBucket(Color nextColor){
		int red=nextColor.getRed()/30;
		int green=nextColor.getGreen()/30;
		int blue=nextColor.getBlue()/30;
		buckets[red][green][blue]++;
		bumpColor(new Color(red,green,blue));
	}
	/***
	 * insert the top colors in order by most times found to least times found
	 * @param bumper
	 */
	private void bumpColor(Color bumper){
		boolean inserted=false;
		for(int i=0;i<10&&i<top10.size();i++){
			if(bumper.equals(top10.get(i))){
				top10.remove(i);
			}
		}
		for(int i=0;i<10&&!inserted&&i<top10.size();i++){
			if(i>=0&&buckets[top10.get(i).getRed()][top10.get(i).getGreen()][top10.get(i).getBlue()]<
						buckets[bumper.getRed()][bumper.getGreen()][bumper.getBlue()]){
				top10.add(i,bumper);
				inserted=true;
			}
		}
		if(top10.size()>=10){
			top10.removeLast();
		}
		else if(!inserted){
			top10.addLast(bumper);
		}
	} 
	/***
	 * get a color by its position in the top 10
	 * @param popular top 10 from 0 to 9
	 * @return
	 */
	public Color getPopulaColor(int popular){
		if(top10.size()==0){
			return null;
		}
		Color temp=top10.get(popular);
		return new Color(temp.getRed()*30,temp.getGreen()*30,temp.getBlue()*30);
	}
	/***
	 * get the most popular color that is not this color
	 * @param popular position to start in the top10 from 0-9
	 * @param notThisColor the color you don't want returned
	 * @return
	 */
	public Color getPopulaColor(int popular,Color notThisColor){
		if(top10.size()==0){
			return null;
		}
		Color temp=new Color(top10.get(popular).getRed()*30,top10.get(popular).getGreen()*30,top10.get(popular).getBlue()*30);
		while(PixelColor.getColorNString(temp).equals(PixelColor.getColorNString(notThisColor))&&popular<top10.size()){
			if((PixelColor.getRGBTotal(temp))>(PixelColor.getRGBTotal(notThisColor)*2)){
				return new Color(255,255,255);
			}
			temp=new Color(top10.get(popular).getRed()*30,top10.get(popular).getGreen()*30,top10.get(popular).getBlue()*30);
			popular++;
		}
		if(popular==top10.size()){
			if(PixelColor.getRGBTotal(new Color(top10.get(0).getRed()*30,top10.get(0).getGreen()*30,top10.get(0).getBlue()*30))>PixelColor.getRGBTotal(notThisColor)){
				return new Color(255,255,255);
			}
			temp=new Color(top10.get(0).getRed()*30,top10.get(0).getGreen()*30,top10.get(0).getBlue()*30);
		}
		return temp;
	}
	/**
	 * gets the number of times the color was found
	 * @param temp the color to get the count of times found for
	 * @return
	 */
	public int getColorCount(Color temp){
		return buckets[temp.getRed()/30][temp.getGreen()/30][temp.getBlue()/30];
	}
	/**
	 * get how many colors are in the top10
	 * @return
	 */
	public int getBucketCount(){
		return top10.size();
	}
	/**
	 * Get the entire top ten with there count as a string
	 * @return
	 */
	public String getTop10(){
		String list="";
	    for(int l=0;l<9&&l<this.getBucketCount();l++){
	    	Color temp=this.getPopulaColor(l);
    			list+=("("+(temp.getRed())+","+(temp.getGreen())+","+(temp.getBlue())+")="+this.getColorCount(temp)+"\r\n");
	    }
	    return list;
	}
//	public int[][][] getBucketArray(){
//		return buckets;
//	}
//	public int[][][] copyBuckets(int[][][] temp){
//		for(int i=0;i<temp.length;i++){
//			for(int j=0;j<temp.length;j++){
//				for(int k=0;k<temp.length;k++){
//					this.buckets[i][j][k]=temp[i][j][k];
//				}
//			}
//		}
//		return temp;
//	}
}