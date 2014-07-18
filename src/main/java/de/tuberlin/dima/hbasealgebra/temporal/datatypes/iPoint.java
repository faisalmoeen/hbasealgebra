package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import de.tuberlin.dima.hbasealgebra.datatypes.DataTypes;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Point;

public class iPoint {
	
	private Point point;
	private Instant instant;
	
	public iPoint(Point point, Instant instant){
		setPoint(point);
		setInstant(instant);
	}
	
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public Instant getInstant() {
		return instant;
	}
	public void setInstant(Instant instant) {
		this.instant = instant;
	}
	
	public static double[] val(double[] darray){
		double[] point = Arrays.copyOfRange(darray, 0, DataTypes.IPOINTMETA.TIME_INDEX);
		point[0]=DataTypes.IPOINT;
		return point;
	}

}
