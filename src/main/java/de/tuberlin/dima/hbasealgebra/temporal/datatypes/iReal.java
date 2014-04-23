package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

public class iReal {
	private double real;
	private Instant instant;
	
	public iReal(double real, Instant instant){
		setReal(real);
		setInstant(instant);
	}

	public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public Instant getInstant() {
		return instant;
	}

	public void setInstant(Instant instant) {
		this.instant = instant;
	}
	
	
}
