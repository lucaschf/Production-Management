package tsi.too.ui.helper;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

/**
 * Convenience class for selecting a single row from a table when the user
 * clicks on it.
 * 
 * @author Lucas Cristovam
 *
 */
public class TableMouseSelectionListener extends MouseAdapter {

	private final JTable table;

	public TableMouseSelectionListener(JTable table) {
		this.table = table;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		try {
			Point point = event.getPoint();
			int currentRow = table.rowAtPoint(point);
			table.setRowSelectionInterval(currentRow, currentRow);
		} catch (Exception e) {
		}
	}
}