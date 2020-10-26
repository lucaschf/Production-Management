package tsi.too.model;

public class ProductionInput implements Cloneable{
	private String name;
	private int quantity;
	private long code;
	private double price;
	
	public ProductionInput(String name, int quantity, long code, double price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.code = code;
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
	
	public long getCode() {
		return code;
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
		return new ProductionInput(name, quantity, code, price);
	}
}
