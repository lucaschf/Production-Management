package tsi.too.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
	private long number;
	private LocalDateTime placementDate;
	private ArrayList<OrderItem> items = new ArrayList<>();	
	
	public Order(long number, LocalDateTime placementDate, List<OrderItem> items) {
		super();
		this.number = number;
		this.placementDate = placementDate;
		this.items.addAll(items);
	}

	public double getTotalValue() {
		return items.stream().mapToDouble(item -> item.getPriceForQuantity()).sum();
	}
	
	public long getNumber() {
		return number;
	}
	
	public LocalDateTime getPlacementDate() {
		return placementDate;
	}

	@Override
	public String toString() {
		return String.format("Order {number= %d, placementDate= %s, items= %s}", number, placementDate, items);
	}
}
