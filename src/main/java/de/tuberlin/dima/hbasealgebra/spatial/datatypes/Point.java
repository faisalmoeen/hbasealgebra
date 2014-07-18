package de.tuberlin.dima.hbasealgebra.spatial.datatypes;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class Point extends Coordinate{
	
	public Point(double x, double y)
	{
		super(x,y);
	}
	public Point(){
		super(-200,-200);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
}
