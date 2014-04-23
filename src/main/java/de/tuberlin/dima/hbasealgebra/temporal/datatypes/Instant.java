package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Instant {
	private Date instant;
	
	public Instant(Date instant)
	{
		this.instant=instant;
	}
	
	public Instant(String date, String pattern){
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		try {
			instant = f.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public Date getInstant()
	{
		return instant;
	}
	

}
