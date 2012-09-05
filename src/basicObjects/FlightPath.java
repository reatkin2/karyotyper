package basicObjects;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import Target.TargetImage;

public class FlightPath {
	private LinkedList<PathPoint> flightPath;
	public FlightPath(){
		flightPath= new LinkedList<PathPoint>();
	}
	public void addPoint(TargetImage img){
		boolean added=false;
		PathPoint newPoint=new PathPoint(new LatLongPoint(img.getImgLong(),img.getImgLat()),img.getHours(),img.getMins(),img.getSecs(),img.getAboveGroundLevelFeet(),img.getHeading(),img.getMetaData());
		if(flightPath.size()>0){
			for(int i=0;i<flightPath.size()&&added;i++){
				if(flightPath.get(i).getHours()<=newPoint.getHours()){
					if(flightPath.get(i).getMin()<=newPoint.getMin()){
						if(flightPath.get(i).getSecs()<=newPoint.getSecs()){
							flightPath.add(i,newPoint);
							added=true;
						}						
					}
				}
			}
		}
		if(added==false){
			flightPath.addLast(newPoint);
		}
	}
	public int getSize(){
		return flightPath.size();
	}
	public String getKml(){
		String buffer="";
		return buffer;
	}
	public String getKmlForPoint(int listPos){
		String buffer="";
		buffer+="<Placemark>";
				//+"<name>Pos-"+listPos+"</name>";
		buffer+=this.getIconStyle(listPos);
		buffer+="<description>Flight Pos: "+listPos+"<br /><br />"
				+flightPath.get(listPos).metaData+" <br /></description>";
		buffer+=this.getKmlCor(listPos);
		buffer+="</Placemark>";
		return buffer;
	}
	public void writePath(String filename){
		  File currdir = new File(".");
		  try{
			  FileWriter out=new FileWriter("shapeData/"+filename+".kml",false);
			  out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
					  +"<kml xmlns=\"http://www.opengis.net/kml/2.2\">\r\n"
					  +"<Document>\r\n"
					  +"<name>FlightPath</name>\r\n");
			 out.write("<description>FlightPath</description>\r\n");
			 for(int i=0;i<this.flightPath.size();i++){
				 out.write(getKmlForPoint(i));
			 }
			 out.write(this.getKmlLines());
			 out.write("</Document>\r\n"
					 	+"</kml>\r\n");
			 
			 
			 out.close();
		  }
		  catch(Exception e){
			  System.out.println(e);
		  }
		  
	}
	public String getIconStyle(int listPos){
		  File currdir = new File(".");
		return "<Style>\r\n"
					+"<IconStyle>\r\n"
						+"<scale>.5</scale>\r\n"
						+"<Icon>\r\n"
							+"<href>file:///"+currdir.getAbsolutePath()+"/airports.png</href>\r\n"
						+"</Icon>\r\n"
						+"<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n"
						+"<heading>"+((int)Math.round(flightPath.get(listPos).getHeading()))+"</heading>\r\n"
					+"</IconStyle>\r\n"
					+"<ListStyle>\r\n"
					+"</ListStyle>\r\n"
				+"</Style>\r\n";
	}
	public String getKmlCor(int listPos){
		return "<Point>\r\n"
				+"<coordinates>"+flightPath.get(listPos).getLatLong().getLong()
				+","+flightPath.get(listPos).getLatLong().getLat()
				+"</coordinates>\r\n"
				+"</Point>\r\n";
	}
	public String getKmlLines(){
		String buffer="<Placemark>\r\n" 
							+"<LineString>\r\n"
								+"<coordinates>\r\n";
		
		for(int i=0;i<flightPath.size();i++){
			buffer+=flightPath.get(i).getLatLong().getLong()+","
					+flightPath.get(i).getLatLong().getLat()+",0.\r\n";		
		}
		buffer+=				"</coordinates>\r\n"
		   					+"</LineString>\r\n"
		   				+"<Style> \r\n"
		   					+"<LineStyle> \r\n" 
		   						+"<color>#ff0000ff</color>\r\n"
		   					+"</LineStyle> \r\n"
		   				+"</Style>\r\n"
		   			+"</Placemark>\r\n";
		return buffer;
	}
	
}
