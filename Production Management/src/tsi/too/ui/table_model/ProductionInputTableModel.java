package tsi.too.ui.table_model;

import java.util.Objects;
import java.util.Vector;

import tsi.too.Constants;
import tsi.too.ext.NumberExt;
import tsi.too.ext.StringExt;
import tsi.too.model.Input;

@SuppressWarnings("serial")
public class ProductionInputTableModel extends CustomTableModel<Input> {
	public static final String[] COLUMN_NAMES = { Constants.NAME, Constants.QUANTITY, Constants.UNITARY_PRICE };

	public ProductionInputTableModel() {
		super(COLUMN_NAMES, 0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void addRow(Input item) {
		Objects.requireNonNull(item);

		Vector<Object> rowVector = new Vector<>();

		rowVector.add(item.getName());
		rowVector.add(item.getQuantity());
		rowVector.add(NumberExt.toBrazilianCurrency(item.getPrice()));

		super.addRow(rowVector);
	}

	@Override
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
}