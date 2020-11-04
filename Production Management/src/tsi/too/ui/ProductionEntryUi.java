package tsi.too.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;
import tsi.too.Patterns;
import tsi.too.controller.InputEntryController;
import tsi.too.controller.ProductController;
import tsi.too.controller.ProductInputsController;
import tsi.too.controller.ProductionController;
import tsi.too.exception.InsufficientStockException;
import tsi.too.ext.NumberExt;
import tsi.too.io.InputDialog.InputValidator;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;
import tsi.too.model.Product;
import tsi.too.model.Production;
import tsi.too.ui.helper.ProductComboboxRenderer;
import tsi.too.util.Pair;
import tsi.too.util.UiUtils;

@SuppressWarnings("serial")
public class ProductionEntryUi extends JDialog {
	private JFormattedTextField ftfTotalSaleValue;
	private JFormattedTextField ftfProductionDate;
	private JPanel formPanel;

	private Product product;
	private final Component parentComponent;
	private BottomActionPanel bottomActionPanel;

	private ProductController productController;
	private ProductInputsController inputsByProductController;
	private ProductionController productionController;
	private InputEntryController inputEntryController;

	private JComboBox<Product> cbProduct;
	private Production target;
	private JSpinner spQuantity;
	private JTextField ftfProductionCost;
	private JLabel lblProductionDate;
	private List<Pair<Long, Input>> inputsReserved;

	public ProductionEntryUi(Component parentComponent) {
		this.parentComponent = parentComponent;

		initControllers();
		initComponents();

		setupWindow();
	}

	private void initControllers() {
		try {
			productController = ProductController.getInstance();
			inputsByProductController = ProductInputsController.getInstance();
			productionController = ProductionController.getInstance();
			inputEntryController = InputEntryController.getInstance();
		} catch (FileNotFoundException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.UNABLE_TO_OPEN_FILE);
		}
	}

	private void setupWindow() {
		setTitle(Constants.PRODUCTION_REGISTRATION);
		setModal(true);
		pack();
		setMinimumSize(new Dimension(620, getHeight()));
		setLocationRelativeTo(parentComponent);
	}

	private void initComponents() {
		bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), Constants.TO_RECORD, e -> onOk());
		initFieldsPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(formPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(bottomActionPanel, GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(formPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(bottomActionPanel,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		getContentPane().setLayout(groupLayout);
	}

	private void initFieldsPanel() {
		formPanel = new JPanel();

		formPanel.setBorder(
				new TitledBorder(null, Constants.PRODUCTION, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblProductName = new JLabel(String.format("%s:", Constants.PRODUCT));
		lblProductName.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblQuantityProduced = new JLabel(String.format("%s:", Constants.QUANTITY_PRODUCED));

		JLabel lblProductionCost = new JLabel(String.format("%s:", Constants.PRODUCTION_COST));
		lblProductionCost.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblTotalSaleValue = new JLabel(String.format("%s:", Constants.TOTAL_SALE_AMOUNT));
		lblTotalSaleValue.setHorizontalAlignment(SwingConstants.TRAILING);

		ftfTotalSaleValue = new JFormattedTextField();
		ftfTotalSaleValue.setEditable(false);
		lblTotalSaleValue.setLabelFor(ftfTotalSaleValue);

		ftfProductionCost = new JTextField();
		ftfProductionCost.setEditable(false);
		ftfProductionCost.setColumns(10);

		initQuantitySpinner();
		initProductsCombobox();
		initDateTextField();

		cbProduct.setSelectedIndex(-1);

		GroupLayout gl_formPanel = new GroupLayout(formPanel);
		gl_formPanel
				.setHorizontalGroup(gl_formPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_formPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_formPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblTotalSaleValue, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblProductionCost, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblQuantityProduced, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblProductionDate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblProductName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_formPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_formPanel
										.createParallelGroup(Alignment.LEADING, false).addComponent(ftfTotalSaleValue)
										.addComponent(ftfProductionCost, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
										.addComponent(ftfProductionDate, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
										.addComponent(spQuantity)).addComponent(cbProduct, 0, 498, Short.MAX_VALUE))
								.addContainerGap()));
		gl_formPanel.setVerticalGroup(gl_formPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_formPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_formPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblProductName)
								.addComponent(cbProduct, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
						.addContainerGap()));
		formPanel.setLayout(gl_formPanel);
	}

	private void initDateTextField() {
		lblProductionDate = new JLabel(String.format("%s:", Constants.PRODUCTION_DATE));
		lblProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);

		ftfProductionDate = new JFormattedTextField(UiUtils.createBrazilianDateMaskFormatter());
		lblProductionDate.setLabelFor(ftfProductionDate);
		ftfProductionDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN)));

		ftfProductionDate.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				retrieveInputsAndCalculateValues(getDate());
			}
		});
	}

	private void initProductsCombobox() {
		try {
			var products = productController.fetchProductsAsVector();

			cbProduct = new JComboBox<>(products);
			cbProduct.setRenderer(new ProductComboboxRenderer());

			bottomActionPanel.setPositiveButtonEnabled(!products.isEmpty());

			cbProduct.addItemListener(e -> {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					this.product = (Product) e.getItem();
					if (product != null)
						retrieveInputsAndCalculateValues(getDate());
				}
			});
		} catch (IOException e) {
			bottomActionPanel.setPositiveButtonEnabled(false);
			MessageDialog.showErrorDialog(this, getTitle(), Constants.FAILED_TO_FETCH_DATA);
		}
	}

	private void initQuantitySpinner() {
		spQuantity = new JSpinner();
		spQuantity.setModel(new SpinnerNumberModel(1.0, 1.0, null, 1.0));

		spQuantity.addChangeListener(e -> retrieveInputsAndCalculateValues(getDate()));
	}

	private void retrieveInputsAndCalculateValues(LocalDate date) {
		ftfProductionCost.setText("");
		ftfTotalSaleValue.setText("");

		if (date == null || product == null) {
			target = null;
			return;
		}

		setInProgress(true);

		try {
			var amountBeingproduced = (Double) spQuantity.getValue();
			var inputsForUnitaryProduction = inputsByProductController.fetchAll(product.getId());

			inputsReserved = inputEntryController.getInputsNeeded(inputsForUnitaryProduction, amountBeingproduced,
					date);

			List<Input> needed = inputsReserved.stream().map(m -> m.getSecond()).collect(Collectors.toList());

			product = product.with(needed);

			target = new Production(product.getId(), amountBeingproduced, date, product.getManufacturingCost(),
					product.getSaleValue());

			ftfProductionCost.setText(NumberExt.toBrazilianCurrency(target.getTotalManufacturingCost()));
			ftfTotalSaleValue.setText(NumberExt.toBrazilianCurrency(target.getTotalSaleValue()));
		} catch (CloneNotSupportedException | IOException ex) {
			target = null;
			bottomActionPanel.setPositiveButtonEnabled(false);
		} catch (InsufficientStockException e) {
			target = null;
			bottomActionPanel.setPositiveButtonEnabled(false);
			MessageDialog.showAlertDialog(this, getTitle(), Constants.THERE_IS_NO_ENOUGH_INPUTS_FOR_THIS_PRODUCTION);
		} finally {
			setInProgress(false);
		}
	}

	private void onOk() {
		var date = getDate();

		var dateValidator = (InputValidator<LocalDate>) input -> {
			if (input == null)
				return Constants.INVALID_PRODUCTION_DATE;
			if (input.isAfter(LocalDate.now())) {
				return Constants.DATE_CANNOT_BE_A_FUTURE_DATE;
			}
			return null;
		};

		if (!dateValidator.isValid(date)) {
			MessageDialog.showAlertDialog(this, getTitle(), dateValidator.getErrorMessage(date));
			return;
		}

		if (target == null) { // there is no product selected
			MessageDialog.showAlertDialog(getTitle(), Constants.INVALID_PRODUCTION_DATA);
			return;
		}

		if (target.getUnitaryManufacturingCost() == 0
				&& !MessageDialog.showConfirmationDialog(this, getTitle(), Constants.PRODUCTION_WITHOUT_INPUTS_MESSAGE))
			return;

		try {
			productionController.withdraw(inputsReserved);
			productionController.insert(target);
			resetForm();
			MessageDialog.showInformationDialog(this, getTitle(), Constants.RECORD_SUCCESSFULLY_INSERTED);
		} catch (IllegalArgumentException | InsufficientStockException | IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_INSERT_RECORD);
		}
	}

	private void setInProgress(boolean inProgress) {
		ftfProductionDate.setEnabled(!inProgress);
		spQuantity.setEnabled(!inProgress);
	}

	private void onCancel() {
		dispose();
	}

	private LocalDate getDate() {
		try {
			return LocalDate.parse(ftfProductionDate.getText(),
					DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN));
		} catch (Exception e) {
			return null;
		}
	}

	private void resetForm() {
		cbProduct.setSelectedIndex(-1);
		ftfProductionCost.setText("");
		ftfTotalSaleValue.setText("");
		target = null;
	}
}
