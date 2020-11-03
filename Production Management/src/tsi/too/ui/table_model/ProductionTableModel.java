package tsi.too.ui.table_model;

import java.util.Objects;
import java.util.Vector;

import tsi.too.Constants;
import tsi.too.model.Production;

@SuppressWarnings("serial")
public class ProductionTableModel extends CustomTableModel<Production> {

	public static final String[] COLUMN_NAMES = { Constants.PRODUCT_ID, Constants.PRODUCT_NAME,
			Constants.AMOUNT_PRODUCED, Constants.TOTAL_MANUFACTURING_COST, Constants.TOTAL_SALE_VALUE,
			Constants.UNITARY_MANUFACTURING_COST, Constants.UNITARY_SALE_VALUE, Constants.DATE };

	public ProductionTableModel() {
		super(COLUMN_NAMES, 0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void addRow(Production item) {
		Objects.requireNonNull(item);

		Vector<Object> rowVector = new Vector<>();

		rowVector.add(item.getProductId());
		rowVector.add("TODO");
		rowVector.add(item.getAmountProduced());
		rowVector.add(item.getTotalManufacturingCost());
		rowVector.add(item.getTotalSaleValue());
		rowVector.add(item.getUnitaryManufacturingCost());
		rowVector.add(item.getUnitarySaleValue());
		rowVector.add(item.getDate());

		super.addRow(rowVector);
	}

	@Override
	public Production getValueAt(int row) {
		// TODO Auto-generated method stub
		return null;
	}

}
