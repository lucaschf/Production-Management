package tsi.too.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Product {
	public static final int MAX_NAME_LENGTH = 50;

	private long id;
	private String name;
	private MeasureUnity measureUnity;
	private double percentageProfitMargin;

	private ArrayList<ProductionInput> productionInputs = new ArrayList<>();

	public Product(long id, String name, MeasureUnity measureUnity, double percentageProfitMargin) {
		super();
		this.id = id;
		this.name = name;
		this.measureUnity = measureUnity;
		this.percentageProfitMargin = percentageProfitMargin;
	}

	public boolean addProductionInput(ProductionInput productionInput) throws IllegalArgumentException {
		if (productionInput == null)
			throw new IllegalArgumentException();

		return productionInputs.add(productionInput);
	}

	public List<ProductionInput> getProductionInputs() {
		return productionInputs.stream().map(pi -> pi.clone()).collect(Collectors.toList());
	}

	public long getCode() {
		return id;
	}

	public void setCode(long code) {
		this.id = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && name.length() > MAX_NAME_LENGTH)
			this.name = name.substring(0, MAX_NAME_LENGTH);
		else
			this.name = name;
	}

	public MeasureUnity getMeasureUnity() {
		return measureUnity;
	}

	public void setMeasureUnity(MeasureUnity measureUnity) {
		this.measureUnity = measureUnity;
	}

	public double getPercentageProfitMargin() {
		return percentageProfitMargin;
	}

	public void setPercentageProfitMargin(double percentageProfitMargin) {
		this.percentageProfitMargin = percentageProfitMargin;
	}

	public double getPrice() {
		return productionInputs.stream().mapToDouble(pi -> pi.getPriceForQuantity()).sum();
	}

	@Override
	public String toString() {
		return String.format(
				"Product {code= %d, name= %s, measureUnity= %s, percentageProfitMargin= %1.2f, productionInputs= %s}",
				id, name, measureUnity, percentageProfitMargin, productionInputs);
	}
}
