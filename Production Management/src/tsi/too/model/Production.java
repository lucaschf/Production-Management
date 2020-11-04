package tsi.too.model;

import java.time.LocalDate;

public class Production {
    private long productId;
    private final double amountProduced;
    private LocalDate date;
    private final double unitaryManufacturingCost;
    private final double unitarySaleValue;
    private final double available;

    public Production(long productId, double amountProduced, LocalDate date, double unitaryManufacturingCost,
                      double unitarySaleValue, double available) {
        super();
        this.productId = productId;
        this.amountProduced = amountProduced;
        this.date = date;
        this.unitaryManufacturingCost = unitaryManufacturingCost;
        this.unitarySaleValue = unitarySaleValue;
        this.available = available;
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

    @Override
    public String toString() {
        return String.format("Production{productId=%d, amountProduced=%s, date=%s, unitaryManufacturingCost=%s, unitarySaleValue=%s, available=%s}",
                productId, amountProduced, date, unitaryManufacturingCost, unitarySaleValue, available);
    }
}
