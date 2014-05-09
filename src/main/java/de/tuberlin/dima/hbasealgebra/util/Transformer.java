package de.tuberlin.dima.hbasealgebra.util;

import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Curve;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Point;

public class Transformer {
	
	public static Coordinate[] toCoordinate(List<Point> pointList){
		Coordinate[] coords = new Coordinate[pointList.size()];
		Iterator<Point> i = pointList.iterator();
		int index=0;
		while(i.hasNext()){
			Point p=i.next();
			coords[index++]=new Coordinate(p.getX(), p.getY());
		}
		return coords;
	}
	
	public static Coordinate[] toCoordinate(Curve lineSegment){
		Coordinate[] coords = new Coordinate[lineSegment.size()];
		Iterator<Point> i = lineSegment.getPoints().iterator();
		int index=0;
		while(i.hasNext()){
			Point p=i.next();
			coords[index++]=new Coordinate(p.getX(), p.getY());
		}
		return coords;
	}
}
