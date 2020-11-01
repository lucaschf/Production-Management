package tsi.too.model;

import java.time.LocalDate;

public class Production {
	private long productId;
	private double quantity;
	private LocalDate date;
	private double totalManufacturingCost;
	private double totalSaleValue;
	
	public Production(long productId, double quantity, LocalDate date, double manufacturingCost,
			double totalSaleValue) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.date = date;
		this.totalManufacturingCost = manufacturingCost;
		this.totalSaleValue = totalSaleValue;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getManufacturingCost() {
		return totalManufacturingCost;
	}

	public void setManufacturingCost(double manufacturingCost) {
		this.totalManufacturingCost = manufacturingCost;
	}

	public double getTotalSaleValue() {
		return totalSaleValue;
	}

	public void setTotalSaleValue(double totalSaleValue) {
		this.totalSaleValue = totalSaleValue;
	}

	@Override
	public String toString() {
		return String.format(
				"Production {productId= %d, quantity= %1.2f, date= %s, manufacturingCost= %f, totalSaleValue= %f}",
				productId, quantity, date, totalManufacturingCost, totalSaleValue);
	}
}
