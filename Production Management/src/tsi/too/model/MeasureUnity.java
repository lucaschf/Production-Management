package tsi.too.model;

import tsi.too.Constants;

/**
 * Represents all acceptable units of measurement
 * 
 * 
 * @author Lucas Cristovam
 * @version 0.1
 */
public enum MeasureUnity {
	GRAM(0 ,Constants.GRAM, "g"),
	KILOGRAM(1, Constants.KILOGRAM, "Kg"),
	LITER(2, Constants.LITER, "L"),
	MILLILITER(3, Constants.MILLILITER, "Ml");
	
	private final String description;
	private final String initials;
	private final int code;
	
	MeasureUnity(int code, String description, String initials) {
		this.code = code;
		this.description = description;
		this.initials = initials;
	}
	
	public int getCode() {
		return code;
	}

	public String getInitials() {
		return initials;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s)", description, initials);
	}

	public static MeasureUnity from(int code) {
		for (MeasureUnity mu : values())
			if(mu.getCode() == code)
				return mu;
		
		throw new IllegalArgumentException("No such unit of measurement");
	}
}