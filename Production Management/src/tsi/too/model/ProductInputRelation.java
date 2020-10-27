package tsi.too.model;

public class ProductInputRelation {
	private long productId;
	private long inputId;
	private int quantity;

	public ProductInputRelation(long productId, long inputId, int quantity) {
		super();
		this.productId = productId;
		this.inputId = inputId;
		this.quantity = quantity;
	}

	public long getProductId() {
		return productId;
	}

	public long getInputId() {
		return inputId;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return String.format("ProductInputRelation {productId= %d, inputId= %d, quantity= %d}", productId, inputId,
				quantity);
	}
}