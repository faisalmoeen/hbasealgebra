package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.ArrayList;
import java.util.List;

import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Curve;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Point;

public class mPointCurve {
	private List<iPoint> listiPoints;
	
	public mPointCurve(List<iPoint> listiPoints){
		this.listiPoints = listiPoints;
	}
	
	public Curve getTrajectory(){
		List<Point> listPoints = new ArrayList<Point>();
		for(iPoint ipoint:listiPoints){
			listPoints.add(ipoint.getPoint());
		}
		return new Curve(listPoints);
	}
}
