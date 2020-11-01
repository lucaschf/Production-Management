package tsi.too.model;

import java.time.LocalDateTime;

public class PriceEntry {
	private long itemId;
	private double price;
	private LocalDateTime date;

	public PriceEntry(long itemCode, double price, LocalDateTime date) {
		super();
		this.price = price;
		this.date = date;
		this.itemId = itemCode;
	}
	
	public PriceEntry(long itemCode, double price) {
		super();
		this.price = price;
		this.date = LocalDateTime.now();
		this.itemId = itemCode;
	}

	public double getPrice() {
		return price;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public long getItemId() {
		return itemId;
	}

	@Override
	public String toString() {
		return String.format("PriceEntry {itemId= %d, price= %f, date= %s}", itemId, price, date);
	}
}