package de.tuberlin.dima.hbasealgebra.spatial.datatypes;

import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.GeometryFactory;

import de.tuberlin.dima.hbasealgebra.jts.JtsLineString;
import de.tuberlin.dima.hbasealgebra.jts.JtsMultiLineString;
import de.tuberlin.dima.hbasealgebra.util.Transformer;

public class Line {
	private JtsMultiLineString line;
	
	public Line(List<List<Point>> points)
	{
		JtsLineString[] ls = new JtsLineString[points.size()];
		Iterator<List<Point>> i = points.iterator();
		int index=0;
		while(i.hasNext()){
			ls[index++]=(JtsLineString) new GeometryFactory().createLineString(Transformer.toCoordinate(i.next()));
		}
	}
	
//	public Line(Point[] pointsArray){
//		Coordinate[] coordArray = new Coordinate[pointsArray.length];
//		for(int i=0;i<pointsArray.length;i++){
//			coordArray[i] = new Coordinate(pointsArray[i].getX(), pointsArray[i].getY());
//		}
//		this.line=new GeometryFactory().createLineString(coordArray);
//	}
	
	public JtsMultiLineString getJtsLine(){
		return this.line;
	}
}
