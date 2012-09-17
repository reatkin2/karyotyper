package basicObjects;

import java.awt.Point;
import java.util.LinkedList;

public class MedialAxisGraph {
	LinkedList<Vertex> axisGraph=new LinkedList<Vertex>();
	public MedialAxisGraph(LinkedList<Point> medialAxis){
		axisGraph= new LinkedList<Vertex>(); 
		buildGraph(medialAxis);
	}
	private void buildGraph(LinkedList<Point> medialAxis){
		if(medialAxis!=null){
			for(int i=0;i<medialAxis.size();i++){
				Vertex tempVertex=new Vertex(medialAxis.get(i));
				if(!axisGraph.contains(tempVertex)){
					for(int j=0;j<axisGraph.size();j++){
						if(tempVertex.checkAdjacent(axisGraph.get(j).getPoint())){
							tempVertex.addChild(axisGraph.get(j).getPoint());
							axisGraph.get(j).addChild(tempVertex.getPoint());
						}
					}
				}

			}
		}
	}
	
}
