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
	private double size;
	private double percentageProfitMargin;

	private final ArrayList<Input> inputs = new ArrayList<>();

	public Product(long id, String name, MeasureUnity measureUnity, double percentageProfitMargin, double size) {
		super();
		this.id = id;
		this.name = name;
		this.measureUnity = measureUnity;
		this.percentageProfitMargin = percentageProfitMargin;
		this.size = size;
	}
	
	public Product(String name, MeasureUnity measureUnity, double percentageProfitMargin, double size) {
		super();
		this.name = name;
		this.measureUnity = measureUnity;
		this.percentageProfitMargin = percentageProfitMargin;
		this.size = size;
	}

	public boolean addProductionInput(Input input) {
		Objects.requireNonNull(input);

		return inputs.add(input.clone());
	}

	public boolean addProductionInput(Collection<Input> inputs) throws IllegalArgumentException {
		if (inputs == null)
			throw new IllegalArgumentException();

		return this.inputs.addAll(inputs.stream().map(input -> input.clone()).collect(Collectors.toList()));
	}

	public List<Input> getProductionInputs() {
		return inputs.stream().map(pi -> pi.clone()).collect(Collectors.toList());
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
		Objects.requireNonNull(name);
		
		if (name.length() > MAX_NAME_LENGTH)
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
		return inputs.stream().mapToDouble(pi -> pi.getPriceForQuantity()).sum();
	}

	public double getSaleValue() {
		var manufacturingCost = getManufacturingCost();
		var profit = (percentageProfitMargin * manufacturingCost) / 100;
	
		return manufacturingCost + profit;
	}
	
	public double getSize() {
		return size;
	}

	/**
	 * Creates a copy with the given id
	 * 
	 * @param id
	 * @return a copy with a new id.
	 */
	public Product withId(long id) {
		var p = clone();
		p.id = id;

		return p;
	}
	
	/**
	 * Creates a copy with the given inputs
	 * 
	 * @param inputs
	 * @return a copy with new inputs.
	 */
	public Product with(Collection<Input> inputs) {
		var p = clone();
		p.inputs.clear();
		p.addProductionInput(inputs);
		
		return p;
	}

	@Override
	public Product clone() {
		try {
			return (Product) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Something goes wrong");
		}
	}

	@Override
	public String toString() {
		return String.format("Product {id= %d, name= %s, measureUnity= %s, size= %f, percentageProfitMargin= %f, inputs= %s}",
				id, name, measureUnity, size, percentageProfitMargin, inputs);
	}
}
