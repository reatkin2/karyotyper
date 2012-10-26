package extraction;

import java.awt.Point;
import java.util.LinkedList;

import basic_objects.OrthogonalLine;
import basic_objects.Vertex;

import medial_axis.MedialAxisGraph;

public class MirrorSplit {
	private MedialAxisGraph medialAxisGraph;

	public MirrorSplit(MedialAxisGraph graph) {
		this.medialAxisGraph = graph;
	}

	public void markSplit(Vertex startVertex) {
		double upperDistanceAvg=-1;
		double lowerDistanceAvg=-1;
		int stableCount=0;
		final int stableValue=5;
		int lastStable=0;
		int chromoWidthOffset=2;
		LinkedList<OrthogonalLine> orthoList=new LinkedList<OrthogonalLine>();
		try{
			for (int i = 0; i < this.medialAxisGraph.getAxisGraph().size(); i++) {
				OrthogonalLine tempOrtho;
				//use small angle rather than 360
				if(lastStable-1==i){
					tempOrtho=getShortestDistance(this.medialAxisGraph.
							getAxisGraph().get(i),this.medialAxisGraph.getChromoWidth());

				}
				//use 360 angle
				else{
					tempOrtho=getShortestDistance(this.medialAxisGraph.
						getAxisGraph().get(i),this.medialAxisGraph.getChromoWidth());
				}
				if(tempOrtho!=null){
					if(stableCount>stableValue&&
						(tempOrtho.isTwoLines()
							||tempOrtho.getlength()>this.medialAxisGraph.
								getChromoWidth()+chromoWidthOffset)){
						orthoList.addFirst(tempOrtho);
						//draw a cutline here
						if(tempOrtho.getUpperDistance()!=-1&&tempOrtho.getLowerDistance()!=-1){
							//see if upper or lower are closer to correct width
						}
					}
					else{
						//everything is normal here with chromosome
						//walk along medial axis
						orthoList.addFirst(tempOrtho);
						stableCount++;
						if(upperDistanceAvg==-1){
							upperDistanceAvg=tempOrtho.getUpperDistance();
						}
						else{
							upperDistanceAvg=((upperDistanceAvg*stableCount)+tempOrtho.getUpperDistance())/(stableCount+1);
						}
						if(upperDistanceAvg==-1){
							upperDistanceAvg=tempOrtho.getUpperDistance();
						}
						else{
							upperDistanceAvg=((upperDistanceAvg*stableCount)+tempOrtho.getUpperDistance())/(stableCount+1);
						}
						lastStable=i;

					}
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}


	}

	public OrthogonalLine getShortestDistance(Vertex axisPoint,double checkDistance) throws Exception{
		boolean foundShortest=true;
		OrthogonalLine tempOrthoLine=null;
		int vectorCount=40;
		int shortest=-1;
		int shortestTill=-1;
		int leftVector[]=new int[vectorCount];
		int rightVector[]=new int[vectorCount];
		//init array
		for(int i=0;i<40;i++){
			leftVector[i]=-1;
			rightVector[i]=-1;			
		}
		//move out from axisPoint tell we run off distance map both sides
		for(int i=0;!foundShortest&&i<checkDistance;i++){
			LinkedList<Point> pointList=getPointsAt(i);
			if(pointList.size()!=vectorCount){
				throw new Exception("array dosn't match number of points");
			}
			//go thru points in pointlist at distance i
			for(int j=0;j<vectorCount;j++ ){
				//check if left side has passed edge of chromosome
				if(this.medialAxisGraph.getDistanceMap().getDistanceFromEdge(pointList.get(j))<=0){
					if(leftVector[j]!=-1){
						leftVector[j]=i;
						if(rightVector[j]!=-1){
							foundShortest=true;
							if(shortest==-1||rightVector[j]+leftVector[j]<shortest){
								shortest=rightVector[j]+leftVector[j];
								tempOrthoLine=new OrthogonalLine(axisPoint, pointList.
										get(j),getOppisite(pointList.get(j)),leftVector[j],rightVector[j]);
							}
						}
					}
				}
				//check if right side has passed edge of chromosome
				if(this.medialAxisGraph.getDistanceMap().getDistanceFromEdge(getOppisite(pointList.get(j)))<=0){
					if(rightVector[j]!=-1){
						rightVector[j]=i;
						if(leftVector[j]!=-1){
							foundShortest=true;
							if(shortest==-1||rightVector[j]+leftVector[j]<shortest){
								shortest=rightVector[j]+leftVector[j];
								tempOrthoLine=new OrthogonalLine(axisPoint, pointList.
										get(j),getOppisite(pointList.get(j)),leftVector[j],rightVector[j]);

							}
						}
					}
				}
			}
			//if we found a short one check to see
			//if any others can be shorter if we continue
			//looping and moving out
			if(shortest!=-1&&(shortestTill==-1||shortestTill>shortest)){
				shortestTill=-1;
				for(int k=0;k<vectorCount;k++){
					//if the left side has met the edge
					if(leftVector[k]!=-1&&rightVector[k]==-1){
						if((leftVector[k]+i+1)<shortest){
							foundShortest=false;
							if(shortestTill==-1||leftVector[k]+i+1<shortestTill){
								shortestTill=leftVector[k]+i+1;
							}
						}
					}
					//if the right side has met the edge
					else if(rightVector[k]!=-1&&leftVector[k]==-1){
						if((rightVector[k]+i+1)<shortest){
							foundShortest=false;
							if(shortestTill==-1||rightVector[k]+i+1<shortestTill){
								shortestTill=rightVector[k]+i+1;
							}
						}
					}

				}
				if(shortestTill==-1||shortest<shortestTill){
					foundShortest=true;
					return tempOrthoLine;
				}
			}
		}
		int shortestLeft=-1;
		int shortestRight=-1;
		int leftPos=-1;
		int rightPos=-1;
		for(int i=0;i<vectorCount;i++){
			if(leftVector[i]!=-1){
				if(shortestLeft==-1||leftVector[i]<shortestLeft){
					shortestLeft=leftVector[i];
					leftPos=i;
				}
			}
			if(rightVector[i]!=-1){
				if(shortestRight==-1||rightVector[i]<shortestRight){
					shortestRight=rightVector[i];
					rightPos=i;
				}
			}
		}
		if(rightPos!=-1&&leftPos!=-1){
			tempOrthoLine=new OrthogonalLine(axisPoint,getPointAt(leftPos),
					getPointAt(rightPos),shortestLeft,shortestRight);
			tempOrthoLine.setTwoLines(true);
		}
		else if(rightPos!=-1){
			tempOrthoLine=new OrthogonalLine();
			tempOrthoLine.setTwoLines(true);
			tempOrthoLine.setLowerPoint(getPointAt(rightPos));
			tempOrthoLine.setLowerDistance(shortestRight);

		}
		else if(leftPos!=-1){
			tempOrthoLine=new OrthogonalLine();
			tempOrthoLine.setTwoLines(true);
			tempOrthoLine.setUpperPoint(getPointAt(leftPos));
			tempOrthoLine.setUpperDistance(shortestLeft);
		}

		return null;
	}

	public LinkedList<Point> getPointsAt(int unitsAway) {
		return new LinkedList<Point>();
	}

	public Point getOppisite(Point temp) {
		return new Point();
	}
	public Point getPointAt(int pos){
		return new Point();
	}
}
