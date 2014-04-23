package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

public class iBool {
	private boolean bool;
	private Instant instant;
	
	public iBool(boolean bool, Instant instant){
		setBool(bool);
		setInstant(instant);
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public Instant getInstant() {
		return instant;
	}

	public void setInstant(Instant instant) {
		this.instant = instant;
	}
	
	
}
