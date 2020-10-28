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
import javax.swing.table.TableRowSorter;

import tsi.too.Constants;
import tsi.too.controller.InputsController;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;
import tsi.too.ui.helper.TableMouseListener;
import tsi.too.ui.table_model.ProductionInputTableModel;

@SuppressWarnings("serial")
public class InputsListUI extends JFrame {
	private JTable tbinputs;
	private JTextField tfName;
	private JButton btnFilter;

	private final TableRowSorter<AbstractTableModel> sorter = new TableRowSorter<AbstractTableModel>();
	private final ProductionInputTableModel tableModel = new ProductionInputTableModel();
	
	private Component parentComponent;
	private InputsController controler;
	
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
		productPanel.setBorder(
				new TitledBorder(null, Constants.PRODUCT, TitledBorder.LEADING, TitledBorder.TOP, null, null));

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

		btnFilter = new JButton(Constants.FETCH);
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
		JScrollPane scroll = new JScrollPane(tbinputs);

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
		tbinputs = new JTable();
		tbinputs.setModel(tableModel);
		tbinputs.addMouseListener(new TableMouseListener(tbinputs));
		tbinputs.removeColumn(tbinputs.getColumnModel().getColumn(1));
		
		setupPopupMenu();
	}
	
	private void setupPopupMenu() {
		final JPopupMenu popupMenu = new JPopupMenu();
		
		JMenuItem editItem = new JMenuItem(Constants.EDIT);
		editItem.addActionListener(e -> edit());
		popupMenu.add(editItem);

		tbinputs.setComponentPopupMenu(popupMenu);
	}

	private void setupWindow() {
		pack();
		setLocationRelativeTo(parentComponent);
	}
	
	private void initController() {
		try {
			controler = InputsController.getInstance();
		}catch (Exception e) {
			MessageDialog.showAlertDialog(parentComponent, getTitle(), Constants.FAILED_TO_FETCH_DATA);
			dispose();
		}
	}

	private void filterData() {
		String text = tfName.getText().trim();
		try {
			tbinputs.setRowSorter(sorter);
			sorter.setRowFilter(RowFilter.regexFilter(text, 0));
		} catch (PatternSyntaxException pse) {
			pse.printStackTrace();
		}
	}

	private void fetchData() {
		try {
			sorter.setModel(tableModel);
			sorter.setSortsOnUpdates(true);
			tableModel.clear();
			tableModel.addRows(controler.fetchInputs());
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_FETCH_DATA);
		}
	}

	private Input getSelectedItem() {
		return tableModel.getValueAt(tbinputs.convertRowIndexToModel(tbinputs.getSelectedRow()));
	}

	private void edit() {
		
	}
}