package tsi.too.ui.table_model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import tsi.too.Constants;
import tsi.too.model.ProductionInput;

@SuppressWarnings("serial")
public class ProductionInputsTableModel extends AbstractTableModel {

	private final List<ProductionInput> items;
	private final String[] columns;

	public ProductionInputsTableModel(List<ProductionInput> items) {
		super();
		this.items = items;
		this.columns = new String[] { Constants.NAME, Constants.QUANTITY, Constants.UNITARY_PRICE };
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return items.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		var item = items.get(row);
		
		switch (getColumnName(column)) {
			case Constants.NAME:
				return item.getName();
			case Constants.QUANTITY:
				return item.getQuantity();
			case Constants.UNITARY_PRICE:
				return item.getPrice();
			default:
				return null;
		}
	}
}
