package tsi.too.ui.table_model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Vector;

import tsi.too.Constants;
import tsi.too.model.PriceEntry;

@SuppressWarnings("serial")
public class PriceEntryModel extends CustomTableModel<PriceEntry> {
	public static final String[] COLUMN_NAMES = { Constants.ID, Constants.PRICE, Constants.DATE };

	public PriceEntryModel() {
		super(COLUMN_NAMES, 0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void addRow(PriceEntry item) {
		Objects.requireNonNull(item);

		Vector<Object> rowVector = new Vector<>();

		rowVector.add(item.getItemId());
		rowVector.add(item.getPrice());
		rowVector.add(item.getDate());

		super.addRow(rowVector);
	}

	@Override
	public PriceEntry getValueAt(int row) {
		try {
			var rowData = getDataVector().elementAt(row);

			var productId = (Long) rowData.get(0);
			var price = (double) rowData.get(1);
			var date = (LocalDateTime) rowData.get(2);

			return new PriceEntry(productId, price, date);
		} catch (Exception e) {
			return null;
		}
	}
}