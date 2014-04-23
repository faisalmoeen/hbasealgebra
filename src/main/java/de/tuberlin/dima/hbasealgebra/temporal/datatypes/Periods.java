package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.Date;
import java.util.List;

public class Periods {
	private List<Period> periods;
	
	public Periods(List<Period> periods){
		setPeriods(periods);
	}

	public List<Period> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Period> periods) {
		this.periods = periods;
	}
	
	public void addPeriod(Date start, Date end){
		periods.add(new Period(start,end));
	}
	
}
