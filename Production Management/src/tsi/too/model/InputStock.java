package tsi.too.model;

import java.time.LocalDate;

public class InputStock {
	private long inputId;
	private double incoming;
	private double price;
	private double available;
	private LocalDate date;

	public InputStock(long inputId, double incoming, double price, LocalDate date) {
		super();
		this.inputId = inputId;
		this.incoming = incoming;
		this.available = incoming;
		this.price = price;
		this.date = date;
	}
	
	public InputStock(long inputId, double incoming, double price, double available, LocalDate date) {
		super();
		this.inputId = inputId;
		this.incoming = incoming;
		this.price = price;
		this.available = available;
		this.date = date;
	}

	public long getInputId() {
		return inputId;
	}

	public void setInputId(long inputId) {
		this.inputId = inputId;
	}
	
	public double getAvailable() {
		return available;
	}
	
	public double getIncoming() {
		return incoming;
	}

	public void withdraw (double quantity) throws IllegalArgumentException{
		if(available == 0)
			throw new IllegalArgumentException("Nothing available");
		
		if(quantity > available)
			throw new IllegalArgumentException("Quantity must be lesser or equal than available quantity");
		
		available -= quantity;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "InputStock {inputId= " + inputId + ", incoming= " + incoming + ", price= " + price + ", available= "
				+ available + ", date= " + date + "}";
	}
}
