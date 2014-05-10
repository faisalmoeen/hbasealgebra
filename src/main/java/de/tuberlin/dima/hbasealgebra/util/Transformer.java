package de.tuberlin.dima.hbasealgebra.util;

import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Curve;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Point;

public class Transformer {
	private static double berlinLeftLong = 13.0882097323;
	private static double berlinRightLong = 13.7606105539;
	private static double berlinBottomLat = 52.3418234221;
	private static double berlinTopLat = 52.6697240587;
	private static double bbikeXOffset = 9267;
	private static double bbikeYOffset = 2598;
	private static double berlinLatWidth = berlinTopLat - berlinBottomLat;
	private static double berlinLongWidth = berlinRightLong - berlinLeftLong;
	private static double bbikeXWidth = 28329+9267;
	private static double bbikeYWidth = 26587+2598;
	private static double xScaleFactor= bbikeXWidth/berlinLongWidth;
	private static double yScaleFactor= bbikeYWidth/berlinLatWidth;
	
	
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
	
	public static Coordinate toCoordinate(String northing, String easting)
	{
		return null;
	}
	
	public static double toLat(double northing){
		return ((northing+bbikeYOffset)/yScaleFactor)+berlinBottomLat;
	}
	
	public static double toLong(double easting){
		return ((easting+bbikeXOffset)/xScaleFactor)+berlinLeftLong;
	}
	
	public static double toEasting(double longitude){
		return (longitude-berlinLeftLong)*xScaleFactor -bbikeXOffset;
	}
	
	public static double toNorthing(double lat){
		return (lat-berlinBottomLat)*yScaleFactor - bbikeYOffset;
	}
}
