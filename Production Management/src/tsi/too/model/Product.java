package tsi.too.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Product implements Cloneable {
	public static final int MAX_NAME_LENGTH = 50;

	private long id;
	private String name;
	private MeasureUnity measureUnity;
	private final double size;
	private double percentageProfitMargin;
	private double manufacturingCost;

	private final ArrayList<Input> inputs = new ArrayList<>();

	public Product(long id, String name, MeasureUnity measureUnity, double percentageProfitMargin, double size, double manufacturingCost) {
		super();
		this.id = id;
		this.name = name;
		this.measureUnity = measureUnity;
		this.percentageProfitMargin = percentageProfitMargin;
		this.size = size;
		this.manufacturingCost = manufacturingCost;
	}
	
	public Product(long id, String name, MeasureUnity measureUnity, double percentageProfitMargin, double size) {
		super();
		this.id = id;
		this.name = name;
		this.measureUnity = measureUnity;
		this.percentageProfitMargin = percentageProfitMargin;
		this.size = size;
		this.manufacturingCost = 0;
	}
	
	public Product(String name, MeasureUnity measureUnity, double percentageProfitMargin, double size, double manufacturingCost) {
		super();
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
		this.manufacturingCost = 0;
	}

	public void addProductionInput(Collection<Input> inputs) throws IllegalArgumentException, CloneNotSupportedException {
		if (inputs == null)
			throw new IllegalArgumentException();

		for (Input input : inputs){
			this.inputs.add(input.clone());
			manufacturingCost += input.getPriceForQuantity(); 
		}
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
		if(manufacturingCost == 0)
			manufacturingCost = inputs.stream().mapToDouble(Input::getPriceForQuantity).sum();
		
		return manufacturingCost;
	}

	public double getSaleValue() {
		var manufacturingCost = getManufacturingCost();
		var profit = (percentageProfitMargin * manufacturingCost) / 100;
	
		return manufacturingCost + profit;
	}
	
	public double getSize() {
		return size;
	}

	public void setManufacturingCost(double manufacturingCost) {
		this.manufacturingCost = manufacturingCost;
	}
	
	/**
	 * Creates a copy of this {@link Product} with the new {@code id}.
	 *
	 * @param id the target id
	 * @return a copy with the new id
	 * @throws CloneNotSupportedException  if cloning is not supported.
	 */
	public Product withId(long id) throws CloneNotSupportedException {
		var p = clone();
		p.id = id;

		return p;
	}

	/**
	 * Creates a copy of this {@link Product} with the new {@code inputs}.
	 *
	 * @param inputs the target id,
	 * @return a copy with the new inputs.
	 * @throws CloneNotSupportedException  if cloning is not supported.
	 */
	public Product with(Collection<Input> inputs) throws CloneNotSupportedException {
		var p = clone();
		p.inputs.clear();
		p.setManufacturingCost(0);
		p.addProductionInput(inputs);
		
		return p;
	}

	@Override
	public Product clone() throws CloneNotSupportedException {
		return (Product) super.clone();
	}

	@Override
	public String toString() {
		return String.format("Product {id= %d, name= %s, measureUnity= %s, size= %f, percentageProfitMargin= %f, inputs= %s}",
				id, name, measureUnity, size, percentageProfitMargin, inputs);
	}
}
