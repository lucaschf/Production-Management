package tsi.too.ui;

import java.awt.Component;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;
import tsi.too.model.Product;
import tsi.too.util.UiUtils;

@SuppressWarnings("serial")
public class ProductionRegistrationUi extends JDialog {
	private JFormattedTextField ftfTotalSaleValue;
	private JFormattedTextField ftfProductionCost;
	private JTextField tfProductName;
	private JFormattedTextField ftfProductionDate;
	private JFormattedTextField ftfQuantityProduced;
	private JPanel formPanel;

	private Product product;
	private Component parentComponent;
	private BottomActionPanel bottomActionPanel;

	public ProductionRegistrationUi(Component parentComponent) {
		this.parentComponent = parentComponent;
		setupWindow();
	}

	private void setupBottomActionPanel() {
		var positiveText = product == null ? Constants.TO_RECORD : Constants.UPDATE;

		bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), positiveText, e -> onOk());
	}

	private void setupWindow() {
		setTitle(Constants.PRODUCTION_REGISTRATION);
		initComponents();

		setModal(true);

		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initComponents() {
		initFieldsPanel();

		setupBottomActionPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(bottomActionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 623,
										Short.MAX_VALUE)
								.addComponent(formPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(formPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(bottomActionPanel, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
						.addContainerGap()));

		getContentPane().setLayout(groupLayout);
	}

	private void initFieldsPanel() {
		formPanel = new JPanel();

		formPanel.setBorder(
				new TitledBorder(null, Constants.PRODUCTION, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblProductName = new JLabel(String.format("%s:", Constants.PRODUCT_NAME));
		lblProductName.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblProductionDate = new JLabel(String.format("%s:", Constants.PRODUCTION_DATE));
		lblProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblQuantityProduced = new JLabel(String.format("%s:", Constants.QUANTITY_PRODUCED));

		JLabel lblProductionCost = new JLabel(String.format("%s:", Constants.PRODUCTION_COST));
		lblProductionCost.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblTotalSaleValue = new JLabel(String.format("%s:", Constants.TOTAL_SALE_AMOUNT));
		lblTotalSaleValue.setHorizontalAlignment(SwingConstants.TRAILING);

		tfProductName = new JTextField();
		lblProductName.setLabelFor(tfProductName);
		tfProductName.setColumns(10);

		ftfProductionDate = new JFormattedTextField(UiUtils.createBrazilianDateMaskFormatter());
		lblProductionDate.setLabelFor(ftfProductionDate);

		ftfQuantityProduced = new JFormattedTextField();
		lblQuantityProduced.setLabelFor(ftfQuantityProduced);

		ftfProductionCost = new JFormattedTextField();
		lblProductionCost.setLabelFor(ftfProductionCost);
		ftfProductionCost.setFormatterFactory(UiUtils.createCurrencyFormatterFactory(0.0, Double.MAX_VALUE));

		ftfTotalSaleValue = new JFormattedTextField();
		lblTotalSaleValue.setLabelFor(ftfTotalSaleValue);
		ftfTotalSaleValue.setFormatterFactory(UiUtils.createCurrencyFormatterFactory(0.0, Double.MAX_VALUE));

		GroupLayout gl_formPanel = new GroupLayout(formPanel);
		gl_formPanel.setHorizontalGroup(gl_formPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_formPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblTotalSaleValue, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblProductionCost, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblQuantityProduced, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(lblProductionDate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblProductName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_formPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(tfProductName, GroupLayout.PREFERRED_SIZE, 448,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(ftfProductionCost, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
								.addComponent(ftfTotalSaleValue, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 448,
										Short.MAX_VALUE)
								.addGroup(gl_formPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(ftfQuantityProduced, Alignment.LEADING)
										.addComponent(ftfProductionDate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												107, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_formPanel.setVerticalGroup(gl_formPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_formPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblProductName)
								.addComponent(tfProductName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_formPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblProductionDate)
								.addComponent(ftfProductionDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_formPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblQuantityProduced)
								.addComponent(ftfQuantityProduced, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_formPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblProductionCost)
								.addComponent(ftfProductionCost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_formPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblTotalSaleValue)
								.addComponent(ftfTotalSaleValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		formPanel.setLayout(gl_formPanel);
	}

	private void onCancel() {
		dispose();
	}

	private void onOk() {
		
	}
}
