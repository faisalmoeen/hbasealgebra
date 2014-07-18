package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Periods {
	private List<Period> periods;
	
	public Periods(List<Period> periods){
		setPeriods(periods);
	}
	
	public Periods(double[] darray, int index, int numPeriods){
		for(int i=0;i<numPeriods;i++){
			this.periods.add(new Period((long)darray[index++], (long)darray[index++]));
		}
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
	
	public void addPeriod(long start, long end){
		periods.add(new Period(new Date(start),new Date(end)));
	}
	
	public List<Double> toDoubleArrayList(){
		Iterator<Period> i = periods.iterator();
		List<Double> doubleList = new ArrayList<Double>();
		Period p=null;
		while(i.hasNext()){
			p=i.next();
			doubleList.add(p.getStartD());
			doubleList.add(p.getEndD());
		}
		return doubleList;
	}
	
}
