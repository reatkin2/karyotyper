package Target;
 
import java.awt.Color;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import Color.PixelColor;



public class TargetArray {
  
  private String stylesForGoogle="<StyleMap id=\"msn_red-pushpin\">	<Pair>	<key>normal</key>\r\n"
			+"			<styleUrl>#sn_red-pushpin</styleUrl>\r\n"
			+"		</Pair>\r\n"
			+"		<Pair>\r\n"
			+"			<key>highlight</key>\r\n"
			+"			<styleUrl>#sh_red-pushpin</styleUrl>\r\n"
			+"		</Pair>\r\n"
			+"	</StyleMap>\r\n"
			+"	<Style id=\"sn_red-pushpin\">\r\n"
			+"		<IconStyle>\r\n"
			+"			<scale>1.1</scale>\r\n" 
			+"			<Icon>\r\n"
			+"				<href>http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png</href>\r\n"
			+"			</Icon>\r\n"
			+"			<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n"
			+"		</IconStyle>\r\n"
			+"		<ListStyle>\r\n"
			+"		</ListStyle>\r\n"
			+"	</Style>\r\n"
			+"	<Style id=\"sh_red-pushpin\">\r\n"
			+"		<IconStyle>\r\n"
			+"			<scale>1.3</scale>\r\n"
			+"			<Icon>\r\n"
			+"				<href>http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png</href>\r\n"
			+"			</Icon>\r\n"
			+"			<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n"
			+"		</IconStyle>\r\n"
			+"		<ListStyle>\r\n"
			+"		</ListStyle>\r\n"
			+"	</Style>\r\n"
            +"<StyleMap id=\"msn_ylw-pushpin\">\r\n"
            +"    <Pair>\r\n"
            +"        <key>normal</key>\r\n"
            +"        <styleUrl>#sn_ylw-pushpin</styleUrl>\r\n"
            +"    </Pair>\r\n"
            +"    <Pair>\r\n"
            +"        <key>highlight</key>\r\n"
            +"        <styleUrl>#sh_ylw-pushpin</styleUrl>\r\n"
            +"    </Pair>\r\n"
            +"</StyleMap>\r\n"
            +"<Style id=\"sh_ylw-pushpin\">\r\n"
            +"    <IconStyle>\r\n"
            +"        <scale>1.3</scale>\r\n"
            +"        <Icon>\r\n"
            +"            <href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\r\n"
            +"        </Icon>\r\n"
            +"        <hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n"
            +"    </IconStyle>\r\n"
            +"    <ListStyle>\r\n"
            +"    </ListStyle>\r\n"
            +"</Style>\r\n"
            +"<Style id=\"sn_ylw-pushpin\">\r\n"
            +"    <IconStyle>\r\n"
            +"        <scale>1.1</scale>\r\n"
            +"        <Icon>\r\n"
            +"            <href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\r\n"
            +"        </Icon>\r\n"
            +"        <hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n"
            +"    </IconStyle>\r\n"
            +"    <ListStyle>\r\n"
            +"    </ListStyle>\r\n"
            +"</Style>\r\n";
  private String redPinStyle="<styleUrl>#msn_red-pushpin</styleUrl>";
  private String yellowPinStyle="<styleUrl>#msn_ylw-pushpin</styleUrl>";
  private TargetShape[][] shapeArr; 
  private LinkedList<SameTargetSet> shapeBuckets;
  private TargetShape current;
  private TargetShape prev;
  public LinkedList<TargetShape> unNamed;
  private double feetPerDegreeLatLong;//=364169.55420532933;//at lat 38.14;//at lat 38.14
  private int distanceToIncludeTargets;

  public TargetArray(double feetPerDegreeLatLongX,int distanceIncludeTargets){
	  this.distanceToIncludeTargets=distanceIncludeTargets;
	  shapeBuckets=new LinkedList<SameTargetSet>();
	  this.feetPerDegreeLatLong=feetPerDegreeLatLongX;
	shapeArr=new TargetShape[300][300];
	for(int j=0;j<300;j++){
		for(int i=0;i<300;i++){
			shapeArr[i][j]=null;
		}
	}
    current=null;
    prev=null;
    unNamed=new LinkedList<TargetShape>();
    
  }


  public void addShape(TargetShape newShape){
	  boolean addedShape=false;
	  if(shapeBuckets.isEmpty()){
		  SameTargetSet temp=new SameTargetSet(this.distanceToIncludeTargets);
		  temp.add(newShape);
		  shapeBuckets.add(temp);
	  }
	  else{
		  for(int i=0;i<shapeBuckets.size()&&!addedShape;i++){
			  //LatLongPoint temp=new LatLongPoint (shapeBuckets.get(i).getLocation().getLong(),shapeBuckets.get(i).getLocation().getLat());
			  if(shapeBuckets.get(i).willGoInSet(this.feetPerDegreeLatLong,newShape)){
				  shapeBuckets.get(i).add(newShape);
				  addedShape=true;
			  }
		  }
		  if(!addedShape){
			  SameTargetSet temp=new SameTargetSet(this.distanceToIncludeTargets);
			  temp.add(newShape);
			  shapeBuckets.add(temp);
		  }
	  }
  }
  public void addShape(TargetShape newShape,boolean first){
    if(first){
    	if(newShape.getTitle().equals("")){
    		unNamed.add(new TargetShape(newShape));
    	}
    	current=shapeArr[newShape.getSize().x][newShape.getSize().y];
    	prev=current;
    }
	if(current==null){
		if(prev==null){
			shapeArr[newShape.getSize().x][newShape.getSize().y]=new TargetShape(newShape);
		}
		else{
    		prev.setNext(new TargetShape(newShape));
		}
	}
    else if(!current.isSame(newShape)){
      prev=current;
      current=current.getNext();
      addShape(newShape,false);
    }
  }
  public TargetShape getShape(String shapeName){  
	  TargetShape cur;
      for(int j=0;j<300;j++){
    	  	for(int i=0;i<300;i++){
	        	cur=this.shapeArr[i][j];
	        	while(cur!=null){
	        		//System.out.println(cur.getTitle());
	        		if(cur.getTitle().equals(shapeName)){
	        			return cur;
	        		}
	        		cur=cur.getNext();
	        	}
	        }
	  }
	  return null;
  }

  public void writeArrayAGLData(String filename){
	  try{
	    FileWriter out=new FileWriter(filename+".csv",true);
	    for(int j=0;j<300;j++){
	    	for(int i=0;i<300;i++){
	        if(shapeArr[i][j]!=null){
	        	current=shapeArr[i][j];
	          while(current!=null){
//					out.write(current.getAGL()+","+current.getSize().x+","+current.getSize().y+"\r\n");
	        	  out.write(current.getLat()+","+current.getLong()+"\r\n");
	        	  //					out.write(current.getPixelCount()+","+current.getAGL()+","+current.getSize().x+","+current.getSize().y+"\r\n");
					if(current.getNext()==null){
						current=null;
					}
					else{
						current=current.getNext();
					}
	          }
	        }
	      }
	    }
	    out.close();
	  }
	  catch(Exception E){
		  System.out.print(E);
	  }
  }
  private String printRestOfTargetSet(SameTargetSet thisTarget,String imageFolder) throws IOException{
	  File currdir = new File(".");
	  String buffer="";
	    for(int k=0;k<thisTarget.size();k++){
	    	buffer+="Target number: <FONT COLOR=\"FF0000\">"+k+"</FONT> of this set from the image: <FONT COLOR=\"FF0000\">\r\n";
			buffer+=(thisTarget.get(k).getTitle().substring(thisTarget.get(k).getTitle().indexOf("imag"),thisTarget.get(k).getTitle().indexOf(".jpg"))+"</FONT><br />"+"\r\n");
    		buffer+=("<img height=\"100\" width=\"200\" ");
    		buffer+=(" src=\""+thisTarget.get(k).getTitle().substring(thisTarget.get(k).getTitle().indexOf("ima"),thisTarget.get(k).getTitle().indexOf(".jpg"))+"_"+thisTarget.get(k).getTargetNimageID()+".jpg"+"\"></img><br />"+"\r\n");
	    	buffer+=("<a href=\"file://"+imageFolder+"/"+thisTarget.get(k).getTitle().substring(thisTarget.get(k).getTitle().indexOf("imag"),thisTarget.get(k).getTitle().indexOf(".jpg"))+".jpg\">View the Image this Target was found in</a><br />"+"\r\n");
    		buffer+=("Longitude this Target: " +thisTarget.get(k).getLong()+" Latitude this Target: "+thisTarget.get(k).getLat()+"<br />\r\n");
//    	    buffer+=("Target color is: " +thisTarget.get(k).getColorName()+" RGB("+thisTarget.get(k).getColor().getRed()
//					+","+thisTarget.get(k).getColor().getGreen()
//					+","+thisTarget.get(k).getColor().getBlue()+")<br />\r\n");

    	    buffer+=("Target has the Letter:<FONT COLOR=\"FF0000\">"+thisTarget.get(k).getText().getTargetText()
    	    				+"</FONT> with a Confidence of:<FONT COLOR=\"FF0000\"> "+thisTarget.get(k).getText().getTextConfidence()
    	    				+"</FONT> oriented :<FONT COLOR=\"FF0000\"> "+thisTarget.get(k).getText().getOrientation()+","+thisTarget.get(k).getText().getLetterOrientation()
    	    				+"</FONT> Rotation angle: <FONT COLOR=\"FF0000\">"+(thisTarget.get(k).getText().getTextAngle())+"&deg;</FONT> <br />\r\n");
    	    buffer+=("Target try straight Letter:<FONT COLOR=\"FF0000\">"+thisTarget.get(k).tryText().getTargetText()
    				+"</FONT> with a Confidence of:<FONT COLOR=\"FF0000\"> "+thisTarget.get(k).tryText().getTextConfidence()
    				+"</FONT> oriented :<FONT COLOR=\"FF0000\"> "+thisTarget.get(k).tryText().getOrientation()+","+thisTarget.get(k).tryText().getLetterOrientation()
    				+"</FONT> Rotation angle: <FONT COLOR=\"FF0000\">"+(thisTarget.get(k).tryText().getTextAngle())+"&deg;</FONT> <br />\r\n");

	    	buffer+=("<a href=\"file://"+currdir.getCanonicalPath()+"/shapeData/textData/"+thisTarget.get(k).getTitle().substring(thisTarget.get(k).getTitle().indexOf("imag"),thisTarget.get(k).getTitle().indexOf(".jpg"))+"_"+thisTarget.get(k).getTargetNimageID()+".png\">View this Targets Text Image</a><br />"+"\r\n");
            buffer+="Size: <FONT COLOR=\"FF0000\">"+thisTarget.get(k).getSizex()+","+thisTarget.get(k).getSizey()+"</FONT><br />";
    	    buffer+="Target Color is: <FONT COLOR=\"FF0000\">"+PixelColor.getColorNString(thisTarget.get(k).getBuckets().getPopulaColor(0))+"</FONT><br />\r\n";
	    	buffer+=("Top 10 Colors in target: <FONT COLOR=\"FF0000\">\r\n");
    	    buffer+=(thisTarget.get(k).getBuckets().getTop10()+"</FONT><br />");
    	    buffer+="Text Color is: <FONT COLOR=\"FF0000\">"+PixelColor.getColorNString(thisTarget.get(k).getText().getBuckets().getPopulaColor(0,thisTarget.get(k).getBuckets().getPopulaColor(0)))+"</FONT><br />\r\n";
    	    buffer+=("Top 10 Colors in text: <FONT COLOR=\"FF0000\">\r\n");
    	    buffer+=(thisTarget.get(k).getText().getBuckets().getTop10()+"</FONT><br />");
	    	//System.out.println("<br />link test: "+("<a href=\"file://"+imageFolder+"/"+thisTarget.get(k).getTitle().substring(thisTarget.get(k).getTitle().indexOf("imag"),thisTarget.get(k).getTitle().indexOf(".jpg"))+".jpg\">View this Image</a><br />"+"\r\n"));
			buffer+=("MetaData: <FONT COLOR=\"FF0000\">"+thisTarget.get(k).getMetaData()+"</FONT><br /><br />"+"\r\n");
		}
	    return buffer;

  }
  public void writeArrayAGLDataByLoc(String filename,String imageFolder){
	  try{
		  File currdir = new File(".");
		  FileWriter out=new FileWriter(filename+".kml",false);
	       out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\r\n");
	       out.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">"+"\r\n");
	       out.write("<Document>"+"\r\n");
	       //out.write("<Folder>"+"\r\n");
	       out.write("<name>Targets</name>"+"\r\n");
	       out.write(this.stylesForGoogle);
	       out.write("<description>"+filename+"</description>"+"\r\n");
	       for(int j=0;j<this.shapeBuckets.size();j++){
	    		SameTargetSet current=shapeBuckets.get(j);
	    		if(!current.isFalsePositive()){
		    		out.write("<Placemark>"+"\r\n");
		    		out.write("<name>TAP-"+current.size()+"</name>"+"\r\n");
	                out.write(this.yellowPinStyle);
	
		    		out.write("<description>Target found in image: <br />"+"\r\n");
		 	       	out.write("<![CDATA[\r\n");
		    	    if(current.get(0).getTitle().contains("image")){
	//	    	    	out.write("TargetSet color is: " +current.getColorName()+" RGB("+current.getShapeRGB().getRed()
	//	    	    								+","+current.getShapeRGB().getGreen()
	//	    	    								+","+current.getShapeRGB().getBlue()+")<br />\r\n");
		    	    	String tempPictureLink=(current.get(0).getTitle().substring(current.get(0).getTitle().indexOf("ima"),current.get(0).getTitle().indexOf(".jpg"))+".jpg");
		    	    	//tempPictureLink.replace("shapeData/", "");
		    	    	//System.out.println("link test: "+("<a href=\"file://"+imageFolder+"/"+tempPictureLink+"\">View this Image</a><br />"+"\r\n"));
		    	    	out.write("<a href=\"file://"+imageFolder+"/"+tempPictureLink+"\">View the image this target was found in</a><br />"+"\r\n");
		    	    	out.write("<a href=\"file://"+imageFolder+"/"+tempPictureLink+"\">");
		    	    	out.write("<img height=\"100\" width=\"200\" ");
		    	    	out.write(" src=\""+current.get(0).getTitle().substring(current.get(0).getTitle().indexOf("ima"),current.get(0).getTitle().indexOf(".jpg"))+"_"+current.get(0).getTargetNimageID()+".jpg"+"\"></img><br />"+"\r\n");
		    	    	out.write("</a><br />"+"\r\n");
		    	    	out.write("This target found at Longitude: <FONT COLOR=\"FF0000\">" +current.getLocation().getLong()+"</FONT> Latitude: <FONT COLOR=\"FF0000\">"+current.getLocation().getLat()+"</FONT><br />\r\n");
		    	    	out.write("The top of the Target is Pointed towards: <FONT COLOR=\"FF0000\">"+current.getTextOrientation()+"</FONT><br />\r\n");
		    	    	out.write("Target Color is: <FONT COLOR=\"FF0000\">"+current.getTargetColor()+"</FONT><br />\r\n");
		    	    	out.write("Target shape is: <add Shape here> <br />\r\n");
		    	    	out.write("The text found on target is: <FONT COLOR=\"FF0000\">"+current.getTextChar()+"</FONT><br />\r\n");
		        	    out.write("Text Color is: <FONT COLOR=\"FF0000\">"+current.getTextColor()+"</FONT><br />\r\n");	    	    	
		    	    	out.write("<br />\r\n<br />\r\n<br />\r\n<br />\r\n");
		    	    }
		    	    out.write(this.printRestOfTargetSet(current, imageFolder));
		    	    out.write("]]>");
		    		out.write("</description>\r\n");
		    	    out.write("<Point>"+"\r\n");
		    	    out.write("<coordinates>"+current.getLocation().getLong()+","+current.getLocation().getLat()+",0</coordinates>"+"\r\n");
		    	    out.write("</Point>"+"\r\n");
		    	    out.write("</Placemark>"+"\r\n");
	//					out.write(current.getAGL()+","+current.getSize().x+","+current.getSize().y+"\r\n");
		        	  //out.write(current.getLat()+","+current.getLong()+"\r\n");
		        	  //					out.write(current.getPixelCount()+","+current.getAGL()+","+current.getSize().x+","+current.getSize().y+"\r\n");
	    		}

	    }

	    //out.write("</Folder>"+"\r\n");
	    out.write("</Document>"+"\r\n");
	    out.write("</kml>"+"\r\n");
	    out.close();
	  }
	  catch(Exception E){
		  System.out.print(E);
	  }
  }
  public void writeNonTargetArrayAGLDataByLoc(String filename,String imageFolder){
	  try{
		  File currdir = new File(".");
		  FileWriter out=new FileWriter(filename+".kml",false);
	       out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\r\n");
	       out.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">"+"\r\n");
	       out.write("<Document>"+"\r\n");
	       //out.write("<Folder>"+"\r\n");
	       out.write("<name>False Targets</name>"+"\r\n");
	       out.write(this.stylesForGoogle);
	       out.write("<description>"+filename+"</description>"+"\r\n");
	       for(int j=0;j<this.shapeBuckets.size();j++){
	    		SameTargetSet current=shapeBuckets.get(j);
	    		if(current.isFalsePositive()){
		    		out.write("<Placemark>"+"\r\n");
		    		out.write("<name>TAP-"+current.size()+"</name>"+"\r\n");
	                out.write(this.redPinStyle);
	
		    		out.write("<description>Target found in image: <br />"+"\r\n");
		 	       	out.write("<![CDATA[\r\n");
		    	    if(current.get(0).getTitle().contains("image")){
	//	    	    	out.write("TargetSet color is: " +current.getColorName()+" RGB("+current.getShapeRGB().getRed()
	//	    	    								+","+current.getShapeRGB().getGreen()
	//	    	    								+","+current.getShapeRGB().getBlue()+")<br />\r\n");
		    	    	String tempPictureLink=(current.get(0).getTitle().substring(current.get(0).getTitle().indexOf("ima"),current.get(0).getTitle().indexOf(".jpg"))+".jpg");
		    	    	//tempPictureLink.replace("shapeData/", "");
		    	    	//System.out.println("link test: "+("<a href=\"file://"+imageFolder+"/"+tempPictureLink+"\">View this Image</a><br />"+"\r\n"));
		    	    	out.write("<a href=\"file://"+imageFolder+"/"+tempPictureLink+"\">View the image this target was found in</a><br />"+"\r\n");
		    	    	out.write("<a href=\"file://"+imageFolder+"/"+tempPictureLink+"\">");
		    	    	out.write("<img height=\"100\" width=\"200\" ");
		    	    	out.write(" src=\""+current.get(0).getTitle().substring(current.get(0).getTitle().indexOf("ima"),current.get(0).getTitle().indexOf(".jpg"))+"_"+current.get(0).getTargetNimageID()+".jpg"+"\"></img><br />"+"\r\n");
		    	    	out.write("</a><br />"+"\r\n");
		    	    	out.write("This target found at Longitude: <FONT COLOR=\"FF0000\">" +current.getLocation().getLong()+"</FONT> Latitude: <FONT COLOR=\"FF0000\">"+current.getLocation().getLat()+"</FONT><br />\r\n");
		    	    	out.write("The top of the Target is Pointed towards: <FONT COLOR=\"FF0000\">"+current.getTextOrientation()+"</FONT><br />\r\n");
		    	    	out.write("Target Color is: <FONT COLOR=\"FF0000\">"+current.getTargetColor()+"</FONT><br />\r\n");
		    	    	out.write("Target shape is: <add Shape here> <br />\r\n");
		    	    	out.write("The text found on target is: <FONT COLOR=\"FF0000\">"+current.getTextChar()+"</FONT><br />\r\n");
		        	    out.write("Text Color is: <FONT COLOR=\"FF0000\">"+current.getTextColor()+"</FONT><br />\r\n");	    	    	
		    	    	out.write("<br />\r\n<br />\r\n<br />\r\n<br />\r\n");
		    	    }
		    	    out.write(this.printRestOfTargetSet(current, imageFolder));
		    	    out.write("]]>");
		    		out.write("</description>\r\n");
		    	    out.write("<Point>"+"\r\n");
		    	    out.write("<coordinates>"+current.getLocation().getLong()+","+current.getLocation().getLat()+",0</coordinates>"+"\r\n");
		    	    out.write("</Point>"+"\r\n");
		    	    out.write("</Placemark>"+"\r\n");
	//					out.write(current.getAGL()+","+current.getSize().x+","+current.getSize().y+"\r\n");
		        	  //out.write(current.getLat()+","+current.getLong()+"\r\n");
		        	  //					out.write(current.getPixelCount()+","+current.getAGL()+","+current.getSize().x+","+current.getSize().y+"\r\n");
	    		}

	    }

	    //out.write("</Folder>"+"\r\n");
	    out.write("</Document>"+"\r\n");
	    out.write("</kml>"+"\r\n");
	    out.close();
	  }
	  catch(Exception E){
		  System.out.print(E);
	  }
  }
  public void writeTurnInDoc(String filename,String imageFolder){
	  String addZero="0";
	  try{
	    FileWriter out=new FileWriter("ncsuARC.txt",false);
	     out.write("#\tGPS_N\tGPS_W\tOrient\tShape\tColor\tLetter\tLtrClr\tFilename\r\n");
	    for(int j=0;j<this.shapeBuckets.size();j++){
    		SameTargetSet current=shapeBuckets.get(j);
    		if(!current.isFalsePositive()){
		    	if((j+1)>10){
		    		addZero="";
		    	}
		    	out.write(addZero+(j+1)
		    					+"\tN"+current.getLocation().getLat()
		    					+"\tW"+current.getLocation().getLong()+"\t"+current.getTextOrientation()+"\t\t"
		    					+current.getTargetColor()
		    					+"\t"+current.getTextChar()+"\t"+current.getTextColor()
		    					+"\t"
		    					+imageFolder+"/"+(current.get(0).getTitle().substring(current.get(0).getTitle().indexOf("ima"),current.get(0).getTitle().indexOf(".jpg"))+".jpg")+"\r\n");
	
		    }
	    }

	    out.close();
	  }
	  catch(Exception E){
		  System.out.print(E);
	  }
  }
  public void writeTurnInDocFalsePos(String filename,String imageFolder){
	  String addZero="0";
	  try{
	    FileWriter out=new FileWriter("ncsuARCFalsePos.txt",false);
	     out.write("#\tGPS_N\tGPS_W\tOrient\tShape\tColor\tLetter\tLtrClr\tFilename\r\n");
	    for(int j=0;j<this.shapeBuckets.size();j++){
    		SameTargetSet current=shapeBuckets.get(j);

    		if(current.isFalsePositive()){
		    	if((j+1)>10){
		    		addZero="";
		    	}
		    	out.write(addZero+(j+1)
		    					+"\tN"+current.getLocation().getLat()
		    					+"\tW"+current.getLocation().getLong()+"\t"+current.getTextOrientation()+"\t\t"
		    					+current.getTargetColor()
		    					+"\t"+current.getTextChar()+"\t"+current.getTextColor()
		    					+"\t"
		    					+imageFolder+"/"+(current.get(0).getTitle().substring(current.get(0).getTitle().indexOf("ima"),current.get(0).getTitle().indexOf(".jpg"))+".jpg")+"\r\n");
    		}
	    }

	    out.close();
	  }
	  catch(Exception E){
		  System.out.print(E);
	  }
  }
public TargetShape getShape(int pos){
	return this.unNamed.get(pos);
}
 
  public LinkedList<TargetShape> getListShapes(){
	  LinkedList<TargetShape> temp=new LinkedList<TargetShape>();
	    for(int j=0;j<300;j++){
	    	for(int i=0;i<300;i++){
	        if(shapeArr[i][j]!=null){
	        	current=shapeArr[i][j];
	          while(current!=null){
					current.shapeOut();
					temp.add(new TargetShape(current));
					if(current.getNext()==null){
						current=null;
					}
					else{
						current=current.getNext();
					}
	          }
	        }
	      }
	    }
	    return temp;
  }




  
}
