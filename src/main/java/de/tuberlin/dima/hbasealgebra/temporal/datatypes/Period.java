package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.Date;

public class Period {
	private Date start;
	private Date end;
	
	public Period(long start, long end){
		setStart(start);
		setEnd(end);
	}
	public Period(Date start,Date end){
		setStart(start);
		setEnd(end);
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
	public void setStart(long start) {
		this.start = new Date(start);
	}
	
	public void setEnd(long end) {
		this.end = new Date(end);
	}
	
	public double getStartD(){
		return (double)this.getStart().getTime();
	}
	
	public double getEndD(){
		return (double)this.getEnd().getTime();
	}
}
