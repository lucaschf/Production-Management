package tsi.too.ui;

import static tsi.too.Constants.DO_YOU_WANT_TO_UPDATE_WITH_INFORMED_VALUES;
import static tsi.too.Constants.FAILED_TO_INSERT_RECORD;
import static tsi.too.Constants.FAILED_TO_UPDATE_RECORD;
import static tsi.too.Constants.PRODUCT_ALREADY_REGISTERED;
import static tsi.too.Constants.PRODUCT_REGISTRATION;
import static tsi.too.Constants.RECORD_SUCCESSFULY_INSERTED;
import static tsi.too.Constants.RECORD_SUCCESSFULY_UPDATED;
import static tsi.too.Constants.REGISTER_PRODUCTION_INPUTS;
import static tsi.too.Constants.TO_RECORD;
import static tsi.too.Constants.UPDATE;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

@SuppressWarnings("serial")
public class ProductRegistrationUi extends JDialog {
	private JTextField tfName;
	private JComboBox<MeasureUnity> cbUnity;
	private JPanel fieldsPanel;
	private JLabel lblName;
	private JButton btnRegisterInputs;
	private JSpinner spProfitmargin;
	private BottomActionPanel bottomActionPanel;

	private ProductController productController;

	Component parentComponent;
	Product product;

	/**
	 * @wbp.parser.constructor
	 */
	public ProductRegistrationUi(Component parentComponent) {
		this(parentComponent, null);
	}

	public ProductRegistrationUi(Component parentComponent, Product product) {
		this.product = product == null ? null : product.clone();
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
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(fieldsPanel,
								GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(bottomActionPanel,
								GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(fieldsPanel, GroupLayout.PREFERRED_SIZE, 154, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(bottomActionPanel, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
						.addContainerGap()));

		getContentPane().setLayout(groupLayout);
	}

	private void setupBottomPanel() {
		var positiveText = product == null ? TO_RECORD : UPDATE;

		bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), positiveText, e -> onOk(e));
	}

	private void initFieldsPanel() {
		fieldsPanel = new JPanel();
		fieldsPanel.setBorder(
				new TitledBorder(null, Constants.PRODUCT, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		lblName = new JLabel(String.format("%s:", Constants.NAME));
		lblName.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblUnity = new JLabel(String.format("%s:", Constants.UNITY));
		lblUnity.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblProfitMargin = new JLabel(String.format("%s:", Constants.PROFIT_MARGIN));
		lblProfitMargin.setHorizontalAlignment(SwingConstants.TRAILING);

		setupNameTextField();

		cbUnity = new JComboBox<MeasureUnity>(MeasureUnity.values());
		lblUnity.setLabelFor(cbUnity);

		spProfitmargin = new JSpinner();
		lblProfitMargin.setLabelFor(spProfitmargin);
		spProfitmargin.setModel(new SpinnerNumberModel(0.0, 0.0, 100.0, 1.0));

		initBtnInputRegistration();

		GroupLayout gl_panel = new GroupLayout(fieldsPanel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblUnity, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblProfitMargin, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
								.addComponent(lblName))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(cbUnity, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
								.addGroup(Alignment.TRAILING,
										gl_panel.createSequentialGroup()
												.addComponent(spProfitmargin, GroupLayout.PREFERRED_SIZE, 66,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
												.addComponent(btnRegisterInputs, GroupLayout.PREFERRED_SIZE, 143,
														GroupLayout.PREFERRED_SIZE))
								.addComponent(tfName, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 435,
										Short.MAX_VALUE))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblName).addComponent(tfName,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup()
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblUnity).addComponent(
								cbUnity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblProfitMargin)
								.addComponent(spProfitmargin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnRegisterInputs).addContainerGap()))));

		fieldsPanel.setLayout(gl_panel);
	}

	private void initBtnInputRegistration() {
		btnRegisterInputs = new JButton(REGISTER_PRODUCTION_INPUTS);
		enableInputsButton();
		btnRegisterInputs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (product != null)
					registerInputs(product);
			}
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
		var profitMargin = (double) spProfitmargin.getValue();
		if (!productController.productNameValidator.isValid(name)) {
			MessageDialog.showAlertDialog(this, getTitle(),
					productController.productNameValidator.getErrorMessage(name));
			return;
		}
		
		if(profitMargin == 0) {
			if(!MessageDialog.showConfirmationDialog(this, getTitle(), "Deseja registrar o produto sem margem de lucro?"))
			{
				spProfitmargin.requestFocus();
				return;
			}
		}
		
		try {
			var p = new Product(0, tfName.getText(), (MeasureUnity) cbUnity.getSelectedItem(),
					(Double) spProfitmargin.getValue());

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
		} catch (IOException e) {
			var message = actionEvent.getActionCommand() == UPDATE ? FAILED_TO_UPDATE_RECORD : FAILED_TO_INSERT_RECORD;
			MessageDialog.showAlertDialog(this, getTitle(), message);
		}
	}

	private void addProduct(Product p, Pair<Product, Long> target) {
		try {
			if (target == null) {
				productController.insert(p);

				if (MessageDialog.showConfirmationDialog(this, PRODUCT_REGISTRATION,
						String.format("%s\n%s?", RECORD_SUCCESSFULY_INSERTED, REGISTER_PRODUCTION_INPUTS)))
					registerInputs(p);
				resetForm();
			} else if (MessageDialog.showConfirmationDialog(this, PRODUCT_REGISTRATION,
					String.format("%s\n%s", PRODUCT_ALREADY_REGISTERED, DO_YOU_WANT_TO_UPDATE_WITH_INFORMED_VALUES)))
				update(p.withId(product.getId()), target);
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, UPDATE, FAILED_TO_INSERT_RECORD);
		}
	}

	private void registerInputs(Product p) {
		new ProductionInputsAssociationUi(this, p).setVisible(true);
	}

	private void update(Product p, Pair<Product, Long> target) {
		try {
			if (target == null)
				target = productController.findByName(product.getName());

			if (target.getFirst().getId() != p.getId()) {
				MessageDialog.showAlertDialog(this, UPDATE, PRODUCT_ALREADY_REGISTERED);
				return;
			}

			productController.update(target.getSecond(), p);
			MessageDialog.showInformationDialog(this, PRODUCT_REGISTRATION, RECORD_SUCCESSFULY_UPDATED);
			resetForm();
			
		} catch (IOException | NullPointerException e) {
			MessageDialog.showAlertDialog(this, UPDATE, FAILED_TO_UPDATE_RECORD);
		}
	}

	private void resetForm() {
		tfName.setText("");
		cbUnity.setSelectedIndex(0);
		spProfitmargin.setValue(0.0);
		bottomActionPanel.setPositiveText(TO_RECORD);
		product = null;
	}

	private void fillFields() {
		if (product == null)
			return;

		tfName.setText(product.getName());
		cbUnity.setSelectedItem(product.getMeasureUnity());
		spProfitmargin.setValue(product.getPercentageProfitMargin());
	}
}