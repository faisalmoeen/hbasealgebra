package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.ArrayList;
import java.util.List;

import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Line;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Curve;

public class mPoint {
	private List<mPointCurve> listmPointSegments;
	
	public mPoint(List<mPointCurve> listmPointSegments){
		this.listmPointSegments = listmPointSegments;
	}
	
	public Line getTrajectory(){
		List<Curve> lls = new ArrayList<Curve>();
		for(mPointCurve mpointsegment: listmPointSegments){
			lls.add(mpointsegment.getTrajectory());
		}
		return new Line(lls);
	}
}
