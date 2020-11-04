package tsi.too.ui;

import static tsi.too.Constants.AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED;
import static tsi.too.Constants.DO_YOU_WANT_TO_UPDATE_WITH_INFORMED_VALUES;
import static tsi.too.Constants.FAILED_TO_INSERT_RECORD;
import static tsi.too.Constants.FAILED_TO_UPDATE_RECORD;
import static tsi.too.Constants.PRODUCT_REGISTRATION;
import static tsi.too.Constants.RECORD_SUCCESSFULLY_INSERTED;
import static tsi.too.Constants.RECORD_SUCCESSFULLY_UPDATED;
import static tsi.too.Constants.REGISTER_PRODUCTION_INPUTS;
import static tsi.too.Constants.TO_RECORD;
import static tsi.too.Constants.UPDATE;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tsi.too.Constants;
import tsi.too.controller.ProductController;
import tsi.too.io.MessageDialog;
import tsi.too.model.MeasureUnity;
import tsi.too.model.Product;
import tsi.too.util.Pair;
import tsi.too.utils.Validators;

@SuppressWarnings("serial")
public class ProductRegistrationUi extends JDialog {
	private JPanel fieldsPanel;
	private JLabel lblName;
	private JButton btnRegisterInputs;
	private BottomActionPanel bottomActionPanel;

	private ProductController productController;

	Component parentComponent;
	Product product;
	private JTextField tfName;
	private JSpinner spProfit;
	private JComboBox<MeasureUnity> cbUnit;
	private JSpinner spSize;

	/**
	 * @param parentComponent
	 * @wbp.parser.constructor
	 */
	public ProductRegistrationUi(Component parentComponent) {
		this(parentComponent, null);
	}

	public ProductRegistrationUi(Component parentComponent, Product product) {
		try {
			this.product = product == null ? null : product.clone();
		} catch (CloneNotSupportedException ignored) {}

		this.parentComponent = parentComponent;

		setupWindow();
		fillFields();
	}

	private void setupWindow() {
		initController();

		setTitle(PRODUCT_REGISTRATION);
		initComponents();
		setResizable(false);
		setModal(true);

		pack();

		setLocationRelativeTo(parentComponent);
	}

	private void initController() {
		try {
			productController = ProductController.getInstance();
		} catch (FileNotFoundException e) {
			MessageDialog.showAlertDialog(parentComponent, PRODUCT_REGISTRATION, Constants.UNABLE_TO_OPEN_FILE);
			dispose();
		}
	}

	private void initComponents() {
		initFieldsPanel();
		initContentGroupLayout();
	}

	private void initContentGroupLayout() {
		setupBottomPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(fieldsPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 575,
										Short.MAX_VALUE)
								.addComponent(bottomActionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 575,
										Short.MAX_VALUE))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(fieldsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(bottomActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		getContentPane().setLayout(groupLayout);
	}

	private void setupBottomPanel() {
		var positiveText = product == null ? TO_RECORD : UPDATE;

		bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), positiveText, this::onOk);
	}

	private void initFieldsPanel() {
		fieldsPanel = new JPanel();
		fieldsPanel.setBorder(
				new TitledBorder(null, Constants.PRODUCT, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		lblName = new JLabel(String.format("%s:", Constants.NAME));
		lblName.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblUnity = new JLabel("Unidade de medida:");
		lblUnity.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblProfitMargin = new JLabel(String.format("%s:", Constants.PROFIT_MARGIN));
		lblProfitMargin.setHorizontalAlignment(SwingConstants.TRAILING);

		setupNameTextField();

		initBtnInputRegistration();

		JLabel lblNewLabel = new JLabel("Tamanho:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);

		lblName.setLabelFor(tfName);
		tfName.setColumns(10);

		cbUnit = new JComboBox<>();
		lblUnity.setLabelFor(cbUnit);

		spSize = new JSpinner(new SpinnerNumberModel(0.0, 0.0, null, 0.5));
		lblNewLabel.setLabelFor(spSize);

		spProfit = new JSpinner(new SpinnerNumberModel(0.0, 0.0, null, 0.5));
		lblProfitMargin.setLabelFor(spProfit);

		GroupLayout gl_panel = new GroupLayout(fieldsPanel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(20, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnRegisterInputs, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblProfitMargin)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
									.addComponent(lblName)
									.addComponent(lblUnity, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblNewLabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(tfName, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbUnit, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(spProfit, Alignment.LEADING)
									.addComponent(spSize, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)))
							.addContainerGap())))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUnity)
						.addComponent(cbUnit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(spSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProfitMargin)
						.addComponent(spProfit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnRegisterInputs)
					.addContainerGap())
		);

		fieldsPanel.setLayout(gl_panel);
	}

	private void initBtnInputRegistration() {
		btnRegisterInputs = new JButton(REGISTER_PRODUCTION_INPUTS);
		btnRegisterInputs.setVisible(false);
		enableInputsButton();
		btnRegisterInputs.addActionListener(e -> {
			if (product != null)
				registerInputs(product);
		});
	}

	private void setupNameTextField() {
		tfName = new JTextField();
		lblName.setLabelFor(tfName);
		tfName.setColumns(10);

		tfName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				enableInputsButton();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				enableInputsButton();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				enableInputsButton();
			}
		});
	}

	public void enableInputsButton() {
		btnRegisterInputs.setEnabled(!tfName.getText().isBlank());
	}

	private void onCancel() {
		dispose();
	}

	private void onOk(ActionEvent actionEvent) {
		var name = tfName.getText();
		var profitMargin = (double) spProfit.getValue();
		var size = (double) spSize.getValue();
		if (!Validators.nameValidator.isValid(name)) {
			MessageDialog.showAlertDialog(this, getTitle(),
					Validators.nameValidator.getErrorMessage(name));
			return;
		}

		if (size == 0) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.INVALID_SIZE);
			return;
		}

		if (profitMargin == 0) {
			if (!MessageDialog.showConfirmationDialog(this, getTitle(),
					"Deseja registrar o produto sem margem de lucro?")) {
				spProfit.requestFocus();
				return;
			}
		}

		try {
			var p = new Product(name, (MeasureUnity) cbUnit.getSelectedItem(), profitMargin, size);
			var target = productController.findByName(p.getName());

			switch (actionEvent.getActionCommand()) {
				case UPDATE:
					update(p.withId(product.getId()), target);
					break;
				case TO_RECORD:
					addProduct(p, target);
					break;
				default:
					break;
			}
		} catch (IOException | CloneNotSupportedException e) {
			var message = actionEvent.getActionCommand().equals(UPDATE) ? FAILED_TO_UPDATE_RECORD
					: FAILED_TO_INSERT_RECORD;
			MessageDialog.showAlertDialog(this, getTitle(), message);
		}
	}

	private void addProduct(Product p, Pair<Product, Long> target) {
		try {
			if (target == null) {
				productController.insert(p);

				if (MessageDialog.showConfirmationDialog(this, PRODUCT_REGISTRATION,
						String.format("%s\n%s?", RECORD_SUCCESSFULLY_INSERTED, REGISTER_PRODUCTION_INPUTS)))
					registerInputs(p);
				resetForm();
			} else if (MessageDialog.showConfirmationDialog(this, PRODUCT_REGISTRATION, String.format("%s\n%s",
					AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED, DO_YOU_WANT_TO_UPDATE_WITH_INFORMED_VALUES)))
				update(p.withId(target.getFirst().getId()), target);
		} catch (IOException | CloneNotSupportedException e) {
			MessageDialog.showAlertDialog(this, UPDATE, FAILED_TO_INSERT_RECORD);
		}
	}

	private void registerInputs(Product p) {
		new ProductInputsUI(this, p).setVisible(true);
	}

	private void update(Product p, Pair<Product, Long> target) {
		try {
			if (target == null)
				target = productController.findByName(product.getName());

			if (target.getFirst().getId() != p.getId()) {
				MessageDialog.showAlertDialog(this, UPDATE, AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED);
				return;
			}

			productController.update(target.getSecond(), p);
			MessageDialog.showInformationDialog(this, PRODUCT_REGISTRATION, RECORD_SUCCESSFULLY_UPDATED);
			resetForm();

		} catch (IOException  | NullPointerException | CloneNotSupportedException e) {
			MessageDialog.showAlertDialog(this, UPDATE, FAILED_TO_UPDATE_RECORD);
		}
	}

	private void resetForm() {
		tfName.setText("");
		cbUnit.setSelectedIndex(0);
		spProfit.setValue(0.0);
		spSize.setValue(0.0);
		bottomActionPanel.setPositiveText(TO_RECORD);
		product = null;
	}

	private void fillFields() {
		if (product == null)
			return;
		
		tfName.setText(product.getName());
		cbUnit.setSelectedItem(product.getMeasureUnity());
		spProfit.setValue(product.getPercentageProfitMargin());
		spSize.setValue(product.getSize());
	}
}