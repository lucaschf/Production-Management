package tsi.too.model;

import java.time.LocalDate;

public class InputEntry implements Cloneable {
	private long id;
	private long inputId;
	private double incoming;
	private double price;
	private double available;
	private LocalDate date;

	public InputEntry(long inputId, double incoming, double price, LocalDate date) {
		super();
		this.inputId = inputId;
		this.incoming = incoming;
		this.available = incoming;
		this.price = price;
		this.date = date;
	}

	public InputEntry(long id, long inputId, double incoming, double price, double available, LocalDate date) {
		super();
		this.inputId = inputId;
		this.incoming = incoming;
		this.price = price;
		this.available = available;
		this.date = date;
		this.id = id;
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

	public void withdraw(double quantity) throws IllegalArgumentException {
		if (available == 0)
			throw new IllegalArgumentException("Nothing available");

		if (quantity > available)
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

	public long getId() {
		return id;
	}

	public InputEntry withId(long id) {
		var i = clone();
		i.id = id;
		return i;
	}
	
	public InputEntry minus(double quantity) {
		available -= quantity;
		return this;
	}
	
	@Override
	protected InputEntry clone() {
		try {
			return (InputEntry) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Failed to clone");
		}
	}

	@Override
	public String toString() {
		return String.format("InputStock {id= %d, inputId= %d, incoming= %f, price= %f, available= %f, date= %s}", id,
				inputId, incoming, price, available, date);
	}
}
