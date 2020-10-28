package tsi.too.model;

public class Input implements Cloneable {
	public static final int MAX_NAME_LENGTH = 50;

	private String name;
	private int quantity;
	private long id;
	private double price;

	public Input(String name, int quantity, long code, double price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.id = code;
		this.price = price;
	}

	public Input(String name) {
		this(name,  0, 0);
	}
	
	public Input(String name, int quantity, double price) {
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
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

	@Override
	public String toString() {
		return String.format("ProductionInputs {name= %s, quantity= %d, price= %1.2f}", name, quantity, price);
	}

	@Override
	public Input clone() {
		return new Input(name, quantity, id, price);
	}

	public Input withId(long id) {
		var p = clone();
		p.id = id;

		return p;
	}
}
