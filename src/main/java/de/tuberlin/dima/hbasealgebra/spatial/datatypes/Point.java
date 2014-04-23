package de.tuberlin.dima.hbasealgebra.spatial.datatypes;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class Point{
	private com.vividsolutions.jts.geom.Point jtsPoint;
	
	public Point(double x, double y)
	{
		this.jtsPoint=new GeometryFactory().createPoint(new Coordinate(x, y));
	}
	
	public double getX(){
		return jtsPoint.getX();
	}
	
	public double getY(){
		return jtsPoint.getY();
	}
}
