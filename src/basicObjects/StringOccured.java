package basicObjects;

public class StringOccured{
	private String myStr;
	private int count;
	public StringOccured(String newStr){
		myStr=newStr;
		count=1;
	}
	public int getCount(){
		return count;
	}
	public void increment(){
		count++;
	}
	public String getStr(){
		return myStr;
	}
}
