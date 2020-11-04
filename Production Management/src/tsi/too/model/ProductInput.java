package tsi.too.model;

public class ProductInput {
	private long productId;
	private long inputId;
	private double inputQuantity;

	public ProductInput(long productId, long inputId, double inputQuantity) {
		super();
		this.productId = productId;
		this.inputId = inputId;
		this.inputQuantity = inputQuantity;
	}

	public long getInputId() {
		return inputId;
	}

	public double getInputQuantity() {
		return inputQuantity;
	}

	public long getProductId() {
		return productId;
	}

	@Override
	public String toString() {
		return "ProductInput {productId= " + productId + ", inputId= " + inputId + ", inputQuantity= " + inputQuantity
				+ "}";
	}
}