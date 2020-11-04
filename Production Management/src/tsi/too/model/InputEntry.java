package tsi.too.model;

import tsi.too.exception.InsufficientStockException;

import java.time.LocalDate;

public class InputEntry implements Cloneable {
	private long id;
	private long inputId;
	private final double incoming;
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

	/**
	 * Creates a copy of this {@link InputEntry} with the new {@code id}.
	 *
	 * @param id the target id
	 * @return a copy with the new id
	 * @throws CloneNotSupportedException  if cloning is not supported.
	 */
	public InputEntry withId(long id) throws CloneNotSupportedException {
		var i = clone();
		i.id = id;
		return i;
	}

	/**
	 * decreases the available quantity in {@code quantity}.
	 *
	 * @param quantity the quantity to be decreased.
	 * @return this with the available quantity decreased.
	 *
	 * @throws InsufficientStockException if {@code available} is equal {@code 0} or is lesser than {@code quantity}.
	 */
	public InputEntry minus(double quantity) throws InsufficientStockException {
		if (available == 0)
			throw new InsufficientStockException("Nothing available");

		if (quantity > available)
			throw new InsufficientStockException("Quantity must be lesser or equal than available quantity");

		available -= quantity;

		return this;
	}
	
	@Override
	protected InputEntry clone() throws CloneNotSupportedException {
		return (InputEntry) super.clone();
	}

	@Override
	public String toString() {
		return String.format("InputStock {id= %d, inputId= %d, incoming= %f, price= %f, available= %f, date= %s}", id,
				inputId, incoming, price, available, date);
	}
}