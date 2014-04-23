package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

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

}
