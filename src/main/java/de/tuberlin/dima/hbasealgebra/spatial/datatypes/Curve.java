package de.tuberlin.dima.hbasealgebra.spatial.datatypes;

import java.util.List;

public class Curve {
	private List<Point> listPoints;
	
	public Curve(List<Point> listPoints){
		this.listPoints=listPoints;
	}
	
	public Curve(){super();}
	
	public int size(){
		return this.listPoints.size();
	}
	
	public List<Point> getPoints(){
		return this.listPoints;
	}
}
