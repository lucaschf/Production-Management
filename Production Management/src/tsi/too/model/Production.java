package tsi.too.model;

public class Production {
	private Product product;
	private int quantity;
	
	public Production(Product p, int quantity) {
		super();
		setProduct(p);
		setQuantity(quantity);
	}
		
	public Product getProduct() {
		return product.clone();
	}
	
	public void setProduct(Product product) {
		this.product = product.clone();
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		if(quantity < 1)
			throw new IllegalArgumentException("Quantity must be greater than zero");
			
		this.quantity = quantity;
	}
	
	public double getManufacturingCost() {
		return product.getManufacturingCost() * quantity;
	}

	@Override
	public String toString() {
		return String.format("Production {p= %s, quantity= %d}", product, quantity);
	}
}
