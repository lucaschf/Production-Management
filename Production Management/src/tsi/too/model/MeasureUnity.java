package tsi.too.model;

import tsi.too.Constants;

public enum MeasureUnity {
	GRAM(Constants.GRAM, "g"),
	KILOGRAM(Constants.KILOGRAM, "Kg"),
	LITER(Constants.LITER, "L"),
	MILILITER(Constants.MILILITER, "Ml");
	
	
	private String description;
	private String initials;
	
	private MeasureUnity(String description, String initials) {
		this.description = description;
		this.initials = initials;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getInitials() {
		return initials;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s)", description, initials);
	}
}
