package tsi.too.model;

public class OrderItem {
	private Product product;
	private int quantity;
	
	public OrderItem(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}

	public OrderItem(Product product) {
		super();
		this.product = product;
		this.quantity = 1;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getPriceForQuantity() {
		return quantity * product.getPrice();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		OrderItem other = (OrderItem) obj;
		
		if (product == null && other.product!= null)
				return false;
		
		return product.equals(other.product);
	}

	@Override
	public String toString() {
		return String.format("OrderItem {product= %s, quantity= %d}", product, quantity);
	}
}