package tsi.too.ui;

import java.awt.Component;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import tsi.too.Constants;
import tsi.too.controller.InputController;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;
import tsi.too.ui.helper.TableMouseSelectionListener;
import tsi.too.ui.table_model.ProductionInputTableModel;
import tsi.too.util.UiUtils;

@SuppressWarnings("serial")
public class InputsListUI extends JFrame {
	private JTable tbInputs;
	private JTextField tfName;

	private final TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<>();
	private final ProductionInputTableModel tableModel = new ProductionInputTableModel();

	private final Component parentComponent;
	private InputController controller;

	public InputsListUI(Component parentComponent) {
		this.parentComponent = parentComponent;
		setTitle(Constants.INPUTS_LISTING);

		initComponent();
		setupWindow();

		initController();
		fetchData();
	}

	private void initComponent() {
		JPanel productPanel = new JPanel();
		productPanel.setBorder(new TitledBorder(null, Constants.PRODUCTION_INPUTS, TitledBorder.LEADING,
				TitledBorder.TOP, null, null));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, Constants.FILTERS, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(productPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(productPanel, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE).addContainerGap()));

		JLabel lblProductName = new JLabel(String.format("%s:", Constants.NAME));
		lblProductName.setLabelFor(tfName);
		lblProductName.setHorizontalAlignment(SwingConstants.TRAILING);

		tfName = new JTextField();
		tfName.setColumns(10);

		JButton btnFilter = new JButton(Constants.FETCH);
		btnFilter.addActionListener(e -> filterData());

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING,
								gl_panel.createSequentialGroup().addContainerGap().addComponent(lblProductName)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfName, GroupLayout.PREFERRED_SIZE, 395,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap(403, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup().addContainerGap(747, Short.MAX_VALUE)
								.addComponent(btnFilter, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblProductName).addComponent(
						tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(btnFilter).addContainerGap()));
		panel.setLayout(gl_panel);

		setupTable();
		JScrollPane scroll = new JScrollPane(tbInputs);

		GroupLayout gl_productPanel = new GroupLayout(productPanel);
		gl_productPanel.setHorizontalGroup(gl_productPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_productPanel.createSequentialGroup().addContainerGap()
						.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE).addContainerGap()));
		gl_productPanel.setVerticalGroup(gl_productPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_productPanel.createSequentialGroup().addContainerGap()
						.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE).addContainerGap()));

		productPanel.setLayout(gl_productPanel);
		getContentPane().setLayout(groupLayout);
	}

	private void setupTable() {
		tbInputs = new JTable();
		tbInputs.setModel(tableModel);
		tbInputs.addMouseListener(new TableMouseSelectionListener(tbInputs));
		tbInputs.removeColumn(tbInputs.getColumnModel().getColumn(2));

		UiUtils.setHorizontalAlignment(tbInputs, SwingConstants.LEFT);

		TableColumnModel columnModel = tbInputs.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(5);
		columnModel.getColumn(1).setPreferredWidth(400);
		columnModel.getColumn(2).setPreferredWidth(5);

		setupPopupMenu();
	}

	private void setupPopupMenu() {
		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem editItem = new JMenuItem(Constants.EDIT);
		editItem.addActionListener(e -> edit());
		popupMenu.add(editItem);

		JMenuItem checkInItem = new JMenuItem(Constants.CHECK_IN);
		checkInItem.addActionListener(e -> checkIn());
		popupMenu.add(checkInItem);

		tbInputs.setComponentPopupMenu(popupMenu);
	}

	private void setupWindow() {
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initController() {
		try {
			controller = InputController.getInstance();
		} catch (Exception e) {
			MessageDialog.showAlertDialog(parentComponent, getTitle(), Constants.FAILED_TO_FETCH_DATA);
			dispose();
		}
	}

	private void filterData() {
		String text = tfName.getText().trim();

		try {
			tbInputs.setRowSorter(sorter);
			sorter.setRowFilter(RowFilter.regexFilter(text, 1));
		} catch (PatternSyntaxException pse) {
			pse.printStackTrace();
		}
	}

	private void fetchData() {
		try {
			sorter.setModel(tableModel);
			sorter.setSortsOnUpdates(true);
			tableModel.clear();
			tableModel.addRows(controller.fetchInputs());
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_FETCH_DATA);
		}
	}

	private Input getSelectedItem() {
		return tableModel.getValueAt(tbInputs.convertRowIndexToModel(tbInputs.getSelectedRow()));
	}

	private void edit() {
		new InputsRegistrationUi(this, getSelectedItem()).setVisible(true);
		fetchData();
		filterData();
	}

	private void checkIn() {
		// TODO
	}
}