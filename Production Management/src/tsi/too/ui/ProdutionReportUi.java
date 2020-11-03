package tsi.too.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;
import tsi.too.controller.ProductionController;
import tsi.too.io.MessageDialog;
import tsi.too.ui.table_model.ProductionTableModel;

@SuppressWarnings("serial")
public class ProdutionReportUi extends JDialog {
	private ProductionController controller;

	private JTable tbProduction;

	private final Component parentComponent;
	private final ProductionTableModel tableModel = new ProductionTableModel();

	public ProdutionReportUi(Component parentComponent) {
		this.parentComponent = parentComponent;

		initController();

		initComponent();
		setupWindow();
		
		fetchData();
	}

	private void initComponent() {
		JPanel filterPanel = new JPanel();
		filterPanel.setBorder(new TitledBorder(null, Constants.PRODUCTION_PERIOD, TitledBorder.LEADING,
				TitledBorder.TOP, null, null));

		JLabel lblStartDate = new JLabel(String.format("%s:", Constants.START_DATE));

		JFormattedTextField ftfStartDate = new JFormattedTextField();
		lblStartDate.setLabelFor(ftfStartDate);

		JLabel lblEndDate = new JLabel(String.format("%s:", Constants.END_DATE));
		lblEndDate.setHorizontalAlignment(SwingConstants.TRAILING);

		JFormattedTextField ftfEndDate = new JFormattedTextField();
		lblEndDate.setLabelFor(ftfEndDate);

		JButton btnCreateReport = new JButton(Constants.GET_PRODUCTION_DATA);
		GroupLayout gl_filterPanel = new GroupLayout(filterPanel);
		gl_filterPanel.setHorizontalGroup(gl_filterPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterPanel.createSequentialGroup().addContainerGap().addComponent(lblStartDate)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(ftfStartDate, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblEndDate, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(ftfEndDate, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnCreateReport)
						.addContainerGap(569, Short.MAX_VALUE)));
		gl_filterPanel.setVerticalGroup(gl_filterPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_filterPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_filterPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblStartDate)
								.addComponent(ftfStartDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblEndDate).addComponent(ftfEndDate, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnCreateReport))));
		filterPanel.setLayout(gl_filterPanel);

		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(
				new TitledBorder(null, Constants.PRODUCTION_DATA, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_tablePanel = new GroupLayout(tablePanel);
		gl_tablePanel.setHorizontalGroup(gl_tablePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tablePanel.createSequentialGroup().addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 832, Short.MAX_VALUE).addContainerGap()));
		gl_tablePanel.setVerticalGroup(gl_tablePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tablePanel.createSequentialGroup().addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE).addContainerGap()));

		tbProduction = new JTable(tableModel);
		scrollPane.setViewportView(tbProduction);
		tablePanel.setLayout(gl_tablePanel);

		BottomActionPanel bottomPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), Constants.OK,
				e -> onOk());
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
						.addComponent(filterPanel, GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
						.addComponent(bottomPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE).addContainerGap()));
		getContentPane().setLayout(groupLayout);
	}

	private void initController() {
		try {
			controller = ProductionController.getInstance();
		} catch (FileNotFoundException e) {
			MessageDialog.showErrorDialog(this, getTitle(), Constants.UNABLE_TO_OPEN_FILE);
		}
	}

	private void setupWindow() {
		setTitle(Constants.PRODUCTION_REPORT);
		setMinimumSize(new Dimension(900, 600));
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void fetchData() {
		try {
			tableModel.clear();
			tableModel.addRows(controller.fetchAll());
		} catch (IOException e) {
			MessageDialog.showAlertDialog(Constants.PRODUCT, Constants.FAILED_TO_FETCH_DATA);
		}
	}
	
	private void onCancel() {
		dispose();
	}

	private void onOk() {
		dispose();
	}
}