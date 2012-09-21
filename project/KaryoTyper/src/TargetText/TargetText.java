package TargetText;


public class TargetText {
	private String targetChar;
	private int textConfidence;
	private int textAngle;
	private double orientation;
	public TargetText(){
    	this.targetChar="";
    	this.textConfidence=-1;
    	this.orientation=-1;
	}
	public void setAngle(int angle){
		this.textAngle=angle;
	}
	public void setTargetChar(String targetchar){
		this.targetChar=targetchar;
	}
	public void setTextConfidence(int textconfidence){
		this.textConfidence=textconfidence;
	}
	public double getOrientation(){
		return this.orientation;
	}
	public int getTextConfidence(){
		return this.textConfidence;
	}
	public String getTargetText(){
		return this.targetChar;
	}
	public String getLetterOrientation(){
		int direction=(int)this.orientation;
		String strDirection="N";

		if(direction>22.5){
			strDirection= "NE";
		}
		if(direction>(45+22.5)){
			strDirection= "E";
		}
		if(direction>(90+22.5)){
			strDirection= "SE";
		}
		if(direction>(135+22.5)){
			strDirection= "S";
		}
		if(direction>(180+22.5)){
			strDirection= "SW";
		}
		if(direction>(225+22.5)){
			strDirection= "W";
		}
		if(direction>(270+22.5)){
			strDirection= "NW";
		}
		if(direction>(315+22.5)){
			strDirection= "N";
		}
		return strDirection;
		
	}

	public void setOrientation(double imgHeading,int angle,int arrayLength){
		this.textAngle=angle;
		this.orientation=imgHeading-180-angle;
		this.orientation %= 360;
        if (this.orientation < 0) {
            this.orientation += 360;
        }
	}

	public int getTextAngle(){
		return this.textAngle;
	}

}
