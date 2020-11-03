package tsi.too.ui;

import java.awt.Component;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import tsi.too.Constants;
import tsi.too.controller.InputEntryController;
import tsi.too.io.MessageDialog;
import tsi.too.ui.helper.TableMouseSelectionListener;
import tsi.too.ui.table_model.InputStockTableModel;
import tsi.too.util.UiUtils;

@SuppressWarnings("serial")
public class InputsPriceChangesListUi extends JFrame {
	private JTable tbEntries;

	private final InputStockTableModel tableModel = new InputStockTableModel();

	private final Component parentComponent;
	private InputEntryController controller;

	public InputsPriceChangesListUi(Component parentComponent) {
		this.parentComponent = parentComponent;
		setTitle(Constants.PRODUCT_LISTING);

		initComponent();
		setupWindow();

		initController();
		fetchData();
	}

	private void initComponent() {
		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(
				new TitledBorder(null, Constants.ENTRY, TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		columnModel.getColumn(1).setPreferredWidth(400);
		columnModel.getColumn(2).setPreferredWidth(5);
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
			tableModel.addRows(controller.fetchAll());
		} catch (IOException e) {
			MessageDialog.showAlertDialog(Constants.PRODUCT, Constants.FAILED_TO_FETCH_DATA);
		}
	}
}