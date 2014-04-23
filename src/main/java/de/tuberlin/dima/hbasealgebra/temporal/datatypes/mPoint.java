package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.List;

import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Line;

public class mPoint {
	private List<mPointSegment> listmPointSegments;
	
	public mPoint(List<mPointSegment> listmPointSegments){
		this.listmPointSegments = listmPointSegments;
	}
	
	public Line getTrajectory(){
		return null;
	}
}
