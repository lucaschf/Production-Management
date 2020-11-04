package tsi.too.ui.table_model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Vector;

import tsi.too.Constants;
import tsi.too.model.InputEntry;

@SuppressWarnings("serial")
public class InputStockTableModel extends CustomTableModel<InputEntry> {

	public static final String[] COLUMN_NAMES = { Constants.ID, Constants.INPUT_ID, Constants.NAME, Constants.PRICE,
			Constants.INCOME, Constants.IN_STOCK, Constants.DATE };

	public InputStockTableModel() {
		super(COLUMN_NAMES, 0);
	}

	@Override
	public void addRow(InputEntry item) {
		Objects.requireNonNull(item);

		Vector<Object> rowVector = new Vector<>();

		rowVector.add(item.getId());
		rowVector.add(item.getInputId());
		rowVector.add("");// name

		rowVector.add(item.getPrice());
		rowVector.add(item.getIncoming());
		rowVector.add(item.getAvailable());
		rowVector.add(item.getDate());

		super.addRow(rowVector);

	}

	@Override
	public InputEntry getValueAt(int row) {
		try {
			var rowData = getDataVector().elementAt(row);

			var id = (Long) rowData.get(0);
			var inputId = (Long) rowData.get(1);
			// name
			var price = (double) rowData.get(3);
			var incoming = (double) rowData.get(4);
			var available = (double) rowData.get(5);

			var date = (LocalDate) rowData.get(6);

			return new InputEntry(id, inputId, incoming, price, available, date);
		} catch (Exception e) {
			return null;
		}
	}

}
