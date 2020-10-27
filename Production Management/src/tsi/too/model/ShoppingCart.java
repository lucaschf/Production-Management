package tsi.too.model;

import java.util.HashSet;
import java.util.Set;

public class ShoppingCart {
	private Set<OrderItem> items = new HashSet<>();
	
	public boolean add(OrderItem item) {
		return items.add(item);
	}
	
	public Double getTotal() {
		return items.stream().mapToDouble(item -> item.getPriceForQuantity()).sum();
	}
	
	public boolean remove(OrderItem item) {
		return items.remove(item);
	}

	@Override
	public String toString() {
		return String.format("ShoppingCart {items= %s}", items);
	}
}