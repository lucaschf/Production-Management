package tsi.too.ui.table_model;

import java.util.Objects;
import java.util.Vector;

import tsi.too.Constants;
import tsi.too.ext.NumberExt;
import tsi.too.ext.StringExt;
import tsi.too.model.Input;

@SuppressWarnings("serial")
public class ProductionInputTableModel extends CustomTableModel<Input> {
	public static final String[] COLUMN_NAMES = {Constants.ID, Constants.NAME, Constants.QUANTITY, Constants.UNITARY_PRICE };

	public ProductionInputTableModel() {
		super(COLUMN_NAMES, 0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void addRow(Input item) {
		super.addRow(createRow(item));
	}
	
	protected Vector<Object> createRow(Input item){
		Objects.requireNonNull(item);

		Vector<Object> rowVector = new Vector<>();

		rowVector.add(item.getId());
		rowVector.add(item.getName());
		rowVector.add(item.getQuantity());
		rowVector.add(NumberExt.toBrazilianCurrency(item.getPrice()));
		
		return rowVector;
	}

	@Override
	public Input getValueAt(int row) {
		try {
			var rowData = getDataVector().elementAt(row);

			var productId = (long) rowData.get(0);
			var name = (String) rowData.get(1);
			var quantity = (int) rowData.get(2);
			var inputsid = StringExt.fromBraziliaCurrencyString(rowData.get(3).toString()).doubleValue();

			return new Input(name, quantity, productId, inputsid);
		} catch (Exception e) {
			return null;
		}
	}	
}