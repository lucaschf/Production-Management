package tsi.too.ui.table_model;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Vector;

import tsi.too.Constants;
import tsi.too.Patterns;
import tsi.too.ext.NumberExt;
import tsi.too.model.Production;
import tsi.too.util.Pair;

@SuppressWarnings("serial")
public class ProductionTableModel extends CustomTableModel<Pair<String, Production>> {

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
	public void addRow(Pair<String, Production> item) {
		Objects.requireNonNull(item);

		Vector<Object> rowVector = new Vector<>();

		var production = item.getSecond();
		
		rowVector.add(production.getProductId());
		rowVector.add(item.getFirst());
		rowVector.add(production.getAmountProduced());
		rowVector.add(NumberExt.toBrazilianCurrency(production.getTotalManufacturingCost()));
		rowVector.add(NumberExt.toBrazilianCurrency(production.getTotalSaleValue()));
		rowVector.add(NumberExt.toBrazilianCurrency(production.getUnitaryManufacturingCost()));
		rowVector.add(NumberExt.toBrazilianCurrency(production.getUnitarySaleValue()));
		rowVector.add(production.getDate().format(DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN)));

		super.addRow(rowVector);
	}

	@Override
	public Pair<String, Production>  getValueAt(int row) {
		throw new UnsupportedOperationException();
	}

}
