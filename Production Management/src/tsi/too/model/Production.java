package tsi.too.model;

import java.time.LocalDate;

public class Production implements Cloneable {
	private long productId;
	private long id;
	private final double amountProduced;
	private LocalDate date;
	private final double unitaryManufacturingCost;
	private final double unitarySaleValue;
	private final double available;

	public Production(long id, long productId, double amountProduced, LocalDate date, double unitaryManufacturingCost,
			double unitarySaleValue, double available) {
		super();
		this.productId = productId;
		this.amountProduced = amountProduced;
		this.date = date;
		this.unitaryManufacturingCost = unitaryManufacturingCost;
		this.unitarySaleValue = unitarySaleValue;
		this.available = available;
		this.id = id;
	}

	public Production(long productId, double amountProduced, LocalDate date, double unitaryManufacturingCost,
			double unitarySaleValue) {
		super();
		this.productId = productId;
		this.amountProduced = amountProduced;
		this.date = date;
		this.unitaryManufacturingCost = unitaryManufacturingCost;
		this.unitarySaleValue = unitarySaleValue;
		this.available = amountProduced;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public double getAmountProduced() {
		return amountProduced;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getUnitaryManufacturingCost() {
		return unitaryManufacturingCost;
	}

	public double getTotalManufacturingCost() {
		return unitaryManufacturingCost * amountProduced;
	}

	public double getTotalSaleValue() {
		return unitarySaleValue * amountProduced;
	}

	public double getUnitarySaleValue() {
		return unitarySaleValue;
	}

	public double getAvailable() {
		return available;
	}

	public long getId() {
		return id;
	}
	
	/**
	 * Creates a copy of this {@link Production} with the new {@code inputs}.
	 *
	 * @param inputs the target id,
	 * @return a copy with the new inputs.
	 * @throws CloneNotSupportedException if cloning is not supported.
	 */
	public Production withId(long id) throws CloneNotSupportedException {
		var p = clone();
		p.id = id;
		return p;
	}

	@Override
	protected Production clone() throws CloneNotSupportedException {
		return (Production) super.clone();
	}

	@Override
	public String toString() {
		return String.format(
				"Production [productId=%d, id=%d, amountProduced=%f, date=%s, unitaryManufacturingCost=%f, unitarySaleValue=%f, available=%f]",
				productId, id, amountProduced, date, unitaryManufacturingCost, unitarySaleValue, available);
	}

	
}
