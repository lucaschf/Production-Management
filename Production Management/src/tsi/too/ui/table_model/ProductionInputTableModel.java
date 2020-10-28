package tsi.too.ui.table_model;

import java.util.Collection;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import tsi.too.Constants;
import tsi.too.ext.NumberExt;
import tsi.too.ext.StringExt;
import tsi.too.model.Product;
import tsi.too.model.Input;

@SuppressWarnings("serial")
public class ProductionInputTableModel extends DefaultTableModel {
	public static final String[] COLUMN_NAMES = { Constants.NAME, Constants.QUANTITY, Constants.UNITARY_PRICE };

	public ProductionInputTableModel() {
		super(COLUMN_NAMES, 0);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
			return getValueAt(0, columnIndex).getClass();
		}

		return super.getColumnClass(columnIndex);
	}

	public void addRow(Input item) {
		if (item == null) {
			throw new IllegalArgumentException("item cannot be null");
		}

		Vector<Object> rowVector = new Vector<>();

		rowVector.add(item.getName());
		rowVector.add(item.getQuantity());
		rowVector.add(NumberExt.toBrazilianCurrency(item.getPrice()));

		super.addRow(rowVector);
	}

	public Input getValueAt(int row) {
		try {
			var rowData = getDataVector().elementAt(row);

			var productId = rowData.get(0).toString();
			var quantity = StringExt.toInt(rowData.get(1).toString());
			var inputsid = StringExt.fromBraziliaCurrencyString(rowData.get(2).toString()).doubleValue();

			return new Input(productId, quantity, inputsid);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void addRows(Collection<Input> items) {
		items.forEach(i -> addRow(i));
	}
	
	public void clear() {
		while(getRowCount() > 0)
			removeRow(0);
	}
}