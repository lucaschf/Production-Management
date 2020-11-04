package tsi.too.ui;

import java.awt.Component;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import tsi.too.Constants;
import tsi.too.Patterns;
import tsi.too.controller.InputEntryController;
import tsi.too.ext.NumberExt;
import tsi.too.ext.StringExt;
import tsi.too.io.MessageDialog;
import tsi.too.model.InputEntry;
import tsi.too.ui.helper.TableMouseSelectionListener;
import tsi.too.ui.table_model.CustomTableModel;
import tsi.too.util.Pair;
import tsi.too.util.UiUtils;

@SuppressWarnings("serial")
public class InputsEntryListUi extends JFrame {
	private JTable tbEntries;

	private final InputStockTableModel tableModel = new InputStockTableModel();

	private final Component parentComponent;
	private InputEntryController controller;

	public InputsEntryListUi(Component parentComponent) {
		this.parentComponent = parentComponent;
		setTitle(Constants.INPUT_ENTRIES);

		initComponent();
		setupWindow();

		initController();
		fetchData();
	}

	private void initComponent() {
		JPanel dataPanel = new JPanel();

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(dataPanel, GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(dataPanel, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE).addContainerGap()));

		setupTable();
		JScrollPane scroll = new JScrollPane(tbEntries);

		GroupLayout gl_dataPanel = new GroupLayout(dataPanel);
		gl_dataPanel.setHorizontalGroup(gl_dataPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dataPanel.createSequentialGroup().addContainerGap()
						.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE).addContainerGap()));
		gl_dataPanel.setVerticalGroup(gl_dataPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_dataPanel.createSequentialGroup().addContainerGap()
						.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE).addContainerGap()));

		dataPanel.setLayout(gl_dataPanel);
		getContentPane().setLayout(groupLayout);
	}

	private void setupTable() {
		tbEntries = new JTable();
		tbEntries.setModel(tableModel);
		tbEntries.addMouseListener(new TableMouseSelectionListener(tbEntries));
		UiUtils.setHorizontalAlignment(tbEntries, SwingConstants.LEFT);

		TableColumnModel columnModel = tbEntries.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(5);
		columnModel.getColumn(1).setPreferredWidth(5);
		columnModel.getColumn(2).setPreferredWidth(400);

		tbEntries.removeColumn(columnModel.getColumn(1));
		tbEntries.removeColumn(columnModel.getColumn(0));
	}

	private void setupWindow() {
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initController() {
		try {
			controller = InputEntryController.getInstance();
		} catch (Exception e) {
			MessageDialog.showAlertDialog(parentComponent, Constants.PRODUCT_LISTING, Constants.FAILED_TO_FETCH_DATA);
			dispose();
		}
	}

	private void fetchData() {
		try {
			tableModel.clear();
			tableModel.addRows(controller.fetchPaired());
		} catch (IOException e) {
			MessageDialog.showAlertDialog(Constants.PRODUCT, Constants.FAILED_TO_FETCH_DATA);
		}
	}

	private static class InputStockTableModel extends CustomTableModel<Pair<String, InputEntry>> {

		public static final String[] COLUMN_NAMES = { Constants.ID, Constants.INPUT_ID, Constants.NAME, Constants.PRICE,
				Constants.INCOME, Constants.IN_STOCK, Constants.DATE };

		public InputStockTableModel() {
			super(COLUMN_NAMES, 0);
		}

		@Override
		public void addRow(Pair<String, InputEntry> item) {
			Objects.requireNonNull(item);

			Vector<Object> rowVector = new Vector<>();

			var entry = item.getSecond();

			rowVector.add(entry.getId());
			rowVector.add(entry.getInputId());
			rowVector.add(item.getFirst());// name

			rowVector.add(NumberExt.toBrazilianCurrency(entry.getPrice()));
			rowVector.add(entry.getIncoming());
			rowVector.add(entry.getAvailable());
			rowVector.add(entry.getDate().format(DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN)));

			super.addRow(rowVector);
		}

		@Override
		public Pair<String, InputEntry> getValueAt(int row) {
			try {
				var rowData = getDataVector().elementAt(row);

				var id = (Long) rowData.get(0);
				var inputId = (Long) rowData.get(1);
				var name = (String) rowData.get(2);
				var price = StringExt.fromBraziliaCurrencyString(rowData.get(3).toString()).doubleValue();
				var incoming = (double) rowData.get(4);
				var available = (double) rowData.get(5);

				var date = (LocalDate.parse(rowData.get(6).toString(),
						DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN)));

				return new Pair<String, InputEntry>(name,
						new InputEntry(id, inputId, incoming, price, available, date));
			} catch (Exception e) {
				return null;
			}
		}
	}
}