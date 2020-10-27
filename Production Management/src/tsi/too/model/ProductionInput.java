package tsi.too.model;

public class ProductionInput implements Cloneable{
	public static final int MAX_NAME_LENGTH = 50;
	
	private String name;
	private int quantity;
	private long id;
	private double price;
	
	public ProductionInput(String name, int quantity, long code, double price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.id = code;
		this.price = price;
	}
	
	public ProductionInput(String name, int quantity, double price) {
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
		return String.format(
				"ProductionInputs {name= %s, quantity= %d, price= %1.2f}", 
				name, 
				quantity, 
				price
		);
	}
	
	@Override
	protected ProductionInput clone() {
		return new ProductionInput(name, quantity, id, price);
	}
}
