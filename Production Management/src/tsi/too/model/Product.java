package tsi.too.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Product implements Cloneable {
	public static final int MAX_NAME_LENGTH = 50;

	private long id;
	private String name;
	private MeasureUnity measureUnity;
	private double percentageProfitMargin;

	private ArrayList<Input> productionInputs = new ArrayList<>();

	public Product(long id, String name, MeasureUnity measureUnity, double percentageProfitMargin) {
		super();
		this.id = id;
		this.name = name;
		this.measureUnity = measureUnity;
		this.percentageProfitMargin = percentageProfitMargin;
	}

	public boolean addProductionInput(Input productionInput) {
		Objects.requireNonNull(productionInput);
		
		return productionInputs.add(productionInput.clone());
	}
	
	public boolean addProductionInput(Collection<Input> productionInput) throws IllegalArgumentException {
		if (productionInput == null)
			throw new IllegalArgumentException();

		return productionInputs.addAll(productionInput.stream().map( input -> input.clone()).collect(Collectors.toList()));
	}

	public List<Input> getProductionInputs() {
		return productionInputs.stream().map(pi -> pi.clone()).collect(Collectors.toList());
	}

	public long getId() {
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

	public double getManufacturingCost() {
		return productionInputs.stream().mapToDouble(pi -> pi.getPriceForQuantity()).sum();
	}

	public Product withId(long id) {
		var p = clone();
		p.id = id;

		return p;
	}

	@Override
	public Product clone() {
		return new Product(id, name, measureUnity, percentageProfitMargin);
	}

	@Override
	public String toString() {
		return name; // used for combobox
	}

}
