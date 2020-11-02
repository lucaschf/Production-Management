package tsi.too.model;

public class ProductInputRelation {
	private long productId;
	private double amountProduced;
	private long inputId;
	private double inputQuantity;

	public ProductInputRelation(long productId, double amountProduced, long inputId, double inputQuantity) {
		super();
		this.productId = productId;
		this.amountProduced = amountProduced;
		this.inputId = inputId;
		this.inputQuantity = inputQuantity;
	}

	public double getAmountProduced() {
		return amountProduced;
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

	public double getInputNeededForProductQuantity(double quantity) {
		return quantity / (amountProduced / inputQuantity);
	}
	
	@Override
	public String toString() {
		return String.format("ProductInputRelation {productId= %d, amountProduced= %f, inputId= %d, inputQuantity= %f}",
				productId, amountProduced, inputId, inputQuantity);
	}
}