package basicObjects;

import java.util.LinkedList;

public class StringBuckets {
	private LinkedList<StringOccured> top10;
	public StringBuckets(){
		top10=new LinkedList<StringOccured>();
	}
	/***
	 * add a color to the buckets
	 * @param nextString
	 */
	public void dropNBucket(String nextString){
		boolean inserted=false;
		StringOccured temp=new StringOccured(nextString);
		for(int i=0;i<10&&i<top10.size();i++){
			if(nextString.equals(top10.get(i).getStr())){
				temp =top10.remove(i);
				temp.increment();
				
			}
		}
		for(int i=0;i<10&&!inserted&&i<top10.size();i++){
			if(i>=0&&top10.get(i).getCount()<temp.getCount()){
				top10.add(i,temp);
				inserted=true;
			}
		}
		if(top10.size()>=10){
			top10.removeLast();
		}
		else if(!inserted){
			top10.addLast(temp);
		}
	} 
	/***
	 * get a color by its position in the top 10
	 * @param popular top 10 from 0 to 9
	 * @return
	 */
	public String getPopularString(int popular){
		if(top10.size()==0){
			return null;
		}
		return top10.get(popular).getStr();
	}
	public StringOccured getPopularStringOccured(int popular){
		if(top10.size()==0){
			return null;
		}
		return top10.get(popular);
	}
	/***
	 * get the most popular color that is not this color
	 * @param popular position to start in the top10 from 0-9
	 * @param notThisString the color you don't want returned
	 * @return
	 */
	public String getPopularString(int popular,String notThisString){
		String temp="";
		if(top10.size()==0){
			return null;
		}
		while((top10.get(popular).getStr()).equals(notThisString)&&popular<top10.size()){
			temp=top10.get(popular).getStr();
			popular++;
		}
		if(popular==top10.size()){
			temp=top10.get(0).getStr();
		}
		return temp;
	}
	/**
	 * gets the number of times the color was found
	 * @param temp the color to get the count of times found for
	 * @return
	 */
	public int getStringCount(String temp){
		int count=0;
		for(int i=0;i<this.top10.size();i++){
			if(temp.equals(top10.get(i).getStr())){
				return top10.get(i).getCount();
			}
		}
		return count;
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
	    	StringOccured temp=this.getPopularStringOccured(l);
    			list+=("("+temp.getStr()+")="+temp.getCount()+", \r\n");
	    }
	    return list;
	}
//		public int[][][] getBucketArray(){
//			return buckets;
//		}
//		public int[][][] copyBuckets(int[][][] temp){
//			for(int i=0;i<temp.length;i++){
//				for(int j=0;j<temp.length;j++){
//					for(int k=0;k<temp.length;k++){
//						this.buckets[i][j][k]=temp[i][j][k];
//					}
//				}
//			}
//			return temp;
//		}
}