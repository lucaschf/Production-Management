package tsi.too.ui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;
import tsi.too.controller.InputsByProductController;
import tsi.too.controller.ProductController;
import tsi.too.ext.NumberExt;
import tsi.too.io.MessageDialog;
import tsi.too.model.Product;
import tsi.too.model.Production;
import tsi.too.util.UiUtils;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ProductionRegistrationUi extends JDialog {
	private JFormattedTextField ftfTotalSaleValue;
	private JFormattedTextField ftfProductionDate;
	private JPanel formPanel;

	private Product product;
	private Component parentComponent;
	private BottomActionPanel bottomActionPanel;

	private ProductController productController;
	private InputsByProductController inputsByProductController;
	private JComboBox<Product> cbProduct;

	private Production target;
	private JSpinner spQuantity;
	private JTextField ftfProductionCost;

	public ProductionRegistrationUi(Component parentComponent) {
		this.parentComponent = parentComponent;

		initControllers();
		initComponents();

		setupWindow();
	}

	private void initControllers() {
		try {
			productController = ProductController.getInstance();
			inputsByProductController = InputsByProductController.getInstance();
		} catch (FileNotFoundException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.UNABLE_TO_OPEN_FILE);
		}
	}

	private void setupBottomActionPanel() {
		var positiveText = product == null ? Constants.TO_RECORD : Constants.UPDATE;

		bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), positiveText, e -> onOk());
	}

	private void setupWindow() {
		setTitle(Constants.PRODUCTION_REGISTRATION);
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

		JLabel lblProductName = new JLabel("Produto:");
		lblProductName.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblProductionDate = new JLabel(String.format("%s:", Constants.PRODUCTION_DATE));
		lblProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblQuantityProduced = new JLabel(String.format("%s:", Constants.QUANTITY_PRODUCED));

		JLabel lblProductionCost = new JLabel(String.format("%s:", Constants.PRODUCTION_COST));
		lblProductionCost.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblTotalSaleValue = new JLabel(String.format("%s:", Constants.TOTAL_SALE_AMOUNT));
		lblTotalSaleValue.setHorizontalAlignment(SwingConstants.TRAILING);

		ftfProductionDate = new JFormattedTextField(UiUtils.createBrazilianDateMaskFormatter());
		lblProductionDate.setLabelFor(ftfProductionDate);

		ftfTotalSaleValue = new JFormattedTextField();
		lblTotalSaleValue.setLabelFor(ftfTotalSaleValue);
		ftfTotalSaleValue.setFormatterFactory(UiUtils.createCurrencyFormatterFactory(0.0, Double.MAX_VALUE));

		initProductsCombobox();
		spQuantity = new JSpinner();
		spQuantity.setModel(new SpinnerNumberModel(1, 1, null, 1));

		ftfProductionCost = new JTextField();
		ftfProductionCost.setEditable(false);
		ftfProductionCost.setColumns(10);

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
								.addComponent(ftfTotalSaleValue, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 448,
										Short.MAX_VALUE)
								.addComponent(cbProduct, 0, 448, Short.MAX_VALUE)
								.addGroup(gl_formPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(spQuantity, Alignment.LEADING, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(ftfProductionDate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												107, Short.MAX_VALUE))
								.addComponent(ftfProductionCost, GroupLayout.PREFERRED_SIZE, 147,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		gl_formPanel.setVerticalGroup(gl_formPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_formPanel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_formPanel
						.createParallelGroup(Alignment.BASELINE).addComponent(lblProductName).addComponent(cbProduct,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_formPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblProductionDate)
						.addComponent(ftfProductionDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_formPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblQuantityProduced)
						.addComponent(spQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
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

	private void initProductsCombobox() {
		try {
			var products = productController.fetchProductsAsVector();

			products.forEach(target -> {
				try {
					target.addProductionInput(inputsByProductController.fetchLinkedInputs(target.getId()));
				} catch (IllegalArgumentException | IOException e2) {
				}
			});

			cbProduct = new JComboBox<>(products);
			
			cbProduct.addItemListener(e -> {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					var product = (Product) e.getItem();
					target = new Production(product, (int) spQuantity.getValue());
					if (product.getProductionInputs().isEmpty()) {
						if (MessageDialog.showConfirmationDialog(this, getTitle(),
								Constants.THE_SELECTED_PRODUCT_HAS_NO_INPUTS_DO_YOU_WANT_TO_ASSOCIATE))
							new AssociateInputToProdutUI(this, product).setVisible(true);
					}
					displayManufacturingCost();
				}
			});

		} catch (IOException e) {
			MessageDialog.showErrorDialog(this, getTitle(), Constants.FAILED_TO_FETCH_DATA);
		}
	}

	private void displayManufacturingCost() {
		ftfProductionCost.setText(NumberExt.toBrazilianCurrency(target.getManufacturingCost()));
	}

	private void onCancel() {
		dispose();
	}

	private void onOk() {

	}
}
