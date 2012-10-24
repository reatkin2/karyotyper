import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.BufferedInputStream;

public class BucketSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner inPut= new Scanner(new BufferedInputStream(System.in)); 
		String buffer=inPut.next();
		
		int inputCount=(int)Integer.parseInt(buffer);
		if(inputCount>0){
			int sortPos[]=new int[inputCount];
			Bucket bucketArray[]=new Bucket[inputCount];
			for(int i=0;i<inputCount;i++){
				bucketArray[i]=new Bucket();
				bucketArray[i].setName(inPut.next());
				bucketArray[i].setBaseWidth((double)Double.parseDouble(inPut.next()));
				bucketArray[i].setTopwidth((double)Double.parseDouble(inPut.next()));
				bucketArray[i].setHeight((double)Double.parseDouble(inPut.next()));
				bucketArray[i].setWaterHeight((double)Double.parseDouble(inPut.next()));
			}
			BucketSort.sortList(bucketArray);
		}
		
	}
	

	public static int[] sortList(Bucket bucketArray[]){
		LinkedList<Integer> arrayPos=new LinkedList<Integer>();
	
		for(int i=0;i<bucketArray.length;i++){
			boolean added=false;
			for(int j=0;j<arrayPos.Size();j++){
				if(bucketArray[i].getVolume()<bucketArray[arrayPos.get(j)].getVolume()){
					arrayPos.add(j,i);
				}
			}
			if(!added){
				
			}
		}
	}
}
