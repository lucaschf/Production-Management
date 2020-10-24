package tsi.too.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PriceEntry {
	private double price;
	private LocalDateTime date;

	public PriceEntry(double price, LocalDateTime date) {
		super();
		this.price = price;
		this.date = date;
	}

	public double getPrice() {
		return price;
	}
	
	public LocalDateTime getDate() {
		return date;
	}

	@Override
	public String toString() {
		return String.format("PriceEntry {price= %1.f, date= %s}", price, date);
	}
}