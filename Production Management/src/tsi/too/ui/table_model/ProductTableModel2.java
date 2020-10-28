package tsi.too.ui.table_model;

import java.util.Collection;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import tsi.too.Constants;
import tsi.too.model.MeasureUnity;
import tsi.too.model.Product;

@SuppressWarnings("serial")
public class ProductTableModel2 extends DefaultTableModel {
	public static final String[] COLUMN_NAMES = { Constants.ID, Constants.NAME, Constants.UNIT_OF_MEASUREMENT,
			Constants.PROFIT_MARGIN };

	public ProductTableModel2() {
		super(COLUMN_NAMES, 0);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
			return getValueAt(0, columnIndex).getClass();
		}

		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void addRow(Product item) {
		if (item == null) {
			throw new IllegalArgumentException("item cannot be null");
		}

		Vector<Object> rowVector = new Vector<>();

		rowVector.add(item.getId());
		rowVector.add(item.getName());
		rowVector.add(item.getMeasureUnity());
		rowVector.add(item.getPercentageProfitMargin());

		super.addRow(rowVector);
	}

	public void addRows(Collection<Product> items) {
		items.forEach(i -> addRow(i));
	}

	public void clear() {
		while (getRowCount() > 0)
			removeRow(0);
	}

	public Product getValueAt(int row) {
		try {
			var rowData = getDataVector().elementAt(row);

			var productId = (Long) rowData.get(0);
			var name = (String) rowData.get(1);
			var unity = (MeasureUnity) rowData.get(2);
			var profit = (double) rowData.get(3);

			return new Product(productId, name, unity, profit);
		} catch (Exception e) {
			return null;
		}
	}
}