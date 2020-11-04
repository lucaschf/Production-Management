package tsi.too.model;

import java.util.Objects;

public class Input implements Cloneable {
	public static final int MAX_NAME_LENGTH = 50;

	private String name;
	private double quantity;
	private long id;
	private double price;

	public Input(String name, double quantity, long code, double price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.id = code;
		this.price = price;
	}

	public Input(String name) {
		this(name, 0, 0);
	}

	public Input(String name, double quantity, double price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPriceForQuantity() {
		return price * quantity;
	}

	public long getId() {
		return id;
	}

	/**
	 * Creates a copy with the given id
	 *
	 * @param id the target id
	 * @return a copy with a new id.
	 */
	public Input withId(long id) {
		var p = clone();
		p.id = id;

		return p;
	}

	@Override
	public Input clone() {
		try {
			return (Input) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Something goes wrong");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Input input = (Input) o;
		return Double.compare(input.quantity, quantity) == 0 && id == input.id
				&& Double.compare(input.price, price) == 0 && name.equals(input.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, quantity, id, price);
	}

	@Override
	public String toString() {
		return String.format("Input {name= %s, quantity= %f, id= %d, price= %f}", name, quantity, id, price);
	}
}
