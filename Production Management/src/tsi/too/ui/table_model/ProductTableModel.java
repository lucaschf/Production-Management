package tsi.too.ui.table_model;

import java.util.Objects;
import java.util.Vector;

import tsi.too.Constants;
import tsi.too.model.MeasureUnity;
import tsi.too.model.Product;

@SuppressWarnings("serial")
public class ProductTableModel extends CustomTableModel<Product>{
	public static final String[] COLUMN_NAMES = { Constants.ID, Constants.NAME, Constants.UNIT_OF_MEASUREMENT,
			Constants.PROFIT_MARGIN };

	public ProductTableModel() {
		super(COLUMN_NAMES, 0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void addRow(Product item) {
		Objects.requireNonNull(item);

		Vector<Object> rowVector = new Vector<>();

		rowVector.add(item.getId());
		rowVector.add(item.getName());
		rowVector.add(item.getMeasureUnity());
		rowVector.add(item.getPercentageProfitMargin());

		super.addRow(rowVector);
	}
	
	@Override
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