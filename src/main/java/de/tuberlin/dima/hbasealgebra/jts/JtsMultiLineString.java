package de.tuberlin.dima.hbasealgebra.jts;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class JtsMultiLineString extends com.vividsolutions.jts.geom.MultiLineString {

	public JtsMultiLineString(LineString[] lineStrings, GeometryFactory factory) {
		super(lineStrings, factory);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6748008105354065441L;
	
	public LineString[] getLineStrings(){
		return (LineString[]) this.geometries;
	}

}
