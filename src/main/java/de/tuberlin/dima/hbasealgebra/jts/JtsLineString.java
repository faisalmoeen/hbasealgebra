package de.tuberlin.dima.hbasealgebra.jts;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;

public class JtsLineString extends LineString {

	public JtsLineString(Coordinate[] points, PrecisionModel precisionModel,
			int SRID) {
		super(points, precisionModel, SRID);
	}

	
	public JtsLineString(CoordinateSequence points, GeometryFactory factory) {
		super(points, factory);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1693021474628877330L;

}
