package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.Date;

public class Period {
	private Instant start;
	private Instant end;
	
	public Period(Instant start, Instant end){
		setStart(start);
		setEnd(end);
	}
	public Period(Date start,Date end){
		setStart(new Instant(start));
		setEnd(new Instant(end));
	}
	public Instant getStart() {
		return start;
	}
	public void setStart(Instant start) {
		this.start = start;
	}
	public Instant getEnd() {
		return end;
	}
	public void setEnd(Instant end) {
		this.end = end;
	}
	
	
}
