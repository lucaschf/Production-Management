package tsi.too.model;

import java.time.LocalDateTime;

public class PriceEntry {
	private long itemCode;
	private double price;
	private LocalDateTime date;

	public PriceEntry(long itemCode, double price, LocalDateTime date) {
		super();
		this.price = price;
		this.date = date;
		this.itemCode = itemCode;
	}

	public double getPrice() {
		return price;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public long getItemCode() {
		return itemCode;
	}

	@Override
	public String toString() {
		return String.format("PriceEntry {itemCode= %d, price= %f, date= %s}", itemCode, price, date);
	}
}