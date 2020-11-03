package tsi.too.ui.table_model;

import java.util.Collection;
import java.util.Objects;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Convenience class for customs table model based on {@link DefaultTableModel}.
 * 
 * 
 * @author Lucas Cristovam
 *
 * @param <E> the type of item for this {@link DefaultTableModel}.
 * 
 * @version 0.1
 */
@SuppressWarnings("serial")
public abstract class CustomTableModel<E> extends DefaultTableModel {
	public CustomTableModel(String[] columnNames, int i) {
		super(columnNames, i);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
			return getValueAt(0, columnIndex).getClass();
		}

		return super.getColumnClass(columnIndex);
	}

	/**
	 * Adds a row to the end of the model. A notification of the row being added
	 * needs to be generated.
	 * 
	 * @param item data of the row being added.
	 * 
	 * @since 0.1
	 */
	public abstract void addRow(E item);

	/**
	 * Retrieves an item for the given row.
	 * 
	 * @param row the target row.
	 * @return the item displayed in this row;
	 * 
	 * @since 0.1
	 */
	public abstract E getValueAt(int row);

	/**
	 * Adds one collection of rows to the end of the model.
	 * 
	 * @param items data of the rows being added.
	 * 
	 * @since 0.1
	 */
	public void addRows(Collection<E> items) {
		Objects.requireNonNull(items);

		items.forEach(this::addRow);
	}

	/**
	 * /** Removes all rows of this {@link TableModel}
	 * 
	 * @since 0.1
	 */
	public void clear() {
		while (getRowCount() > 0)
			removeRow(0);
	}
}