package tsi.too.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import tsi.too.Constants;
import tsi.too.Patterns;
import tsi.too.controller.InputController;
import tsi.too.controller.InputEntryController;
import tsi.too.ext.StringExt;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;
import tsi.too.model.InputEntry;
import tsi.too.util.UiUtils;

@SuppressWarnings("serial")
public class InputEntryUi extends JDialog {
	private JFormattedTextField ftfprice;
	private JComboBox<Input> cbInput;
	private JSpinner spQuantity;

	private Component parentComponent;

	private InputController inputController;
	private InputEntryController inputEntryController;
	private JFormattedTextField ftfDate;

	public InputEntryUi(Component parentComponent) {
		this.parentComponent = parentComponent;

		initController();

		initComponent();
		setupWindow();

		resetForm();
	}

	private void initController() {
		try {
			inputController = InputController.getInstance();
			inputEntryController = InputEntryController.getInstance();
		} catch (FileNotFoundException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.UNABLE_TO_OPEN_FILE);
			dispose();
		}
	}

	private void setupWindow() {
		setTitle(Constants.INPUT_ENTRY);
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initComponent() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Dados da entrada", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		BottomActionPanel bottomActionPanel = new BottomActionPanel(Constants.CANCEL, this::onCancel, Constants.ADD,
				this::onOk);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(bottomActionPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		groupLayout
				.setVerticalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
								groupLayout.createSequentialGroup().addContainerGap()
										.addComponent(panel, GroupLayout.PREFERRED_SIZE, 154, Short.MAX_VALUE)
										.addGap(12)
										.addComponent(bottomActionPanel, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		JLabel lblQuantity = new JLabel(String.format("%s:", Constants.QUANTITY));
		lblQuantity.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblInput = new JLabel(String.format("%s:", Constants.PRODUCTION_INPUT));
		lblInput.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblPrice = new JLabel("pre\u00E7o unit\u00E1rio:");
		lblPrice.setHorizontalAlignment(SwingConstants.TRAILING);

		try {
			cbInput = new JComboBox<Input>();
			cbInput.setModel(new DefaultComboBoxModel<>(inputController.fetchInputsAsVector()));
			cbInput.setRenderer(new InputComboboxRenderer());
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_FETCH_DATA);
		}
		lblInput.setLabelFor(cbInput);

		ftfprice = new JFormattedTextField(UiUtils.createCurrencyFormatterFactory(0.0, Double.MAX_VALUE));
		lblPrice.setLabelFor(ftfprice);

		SpinnerNumberModel model = new SpinnerNumberModel(0.0, 0.0, null, 0.5);
		spQuantity = new JSpinner(model);

		JLabel lblDate = new JLabel("Data:");

		ftfDate = new JFormattedTextField(UiUtils.createBrazilianDateMaskFormatter());
		lblDate.setLabelFor(ftfDate);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addComponent(lblDate)
								.addComponent(lblInput).addComponent(lblPrice).addComponent(lblQuantity))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(cbInput, Alignment.TRAILING, 0, 447, Short.MAX_VALUE)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(ftfDate, Alignment.LEADING)
										.addComponent(ftfprice, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120,
												Short.MAX_VALUE)
										.addComponent(spQuantity, Alignment.LEADING)))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblInput))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblQuantity).addComponent(
						spQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(10)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblPrice).addComponent(ftfprice,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblDate).addComponent(ftfDate,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
	}

	private void onOk(ActionEvent e) {
		Input input;
		var quantity = (Double) spQuantity.getValue();
		double price;
		LocalDate date;

		try {
			input = (Input) cbInput.getSelectedItem();
		} catch (NullPointerException npe) {
			MessageDialog.showAlertDialog(getTitle(), "Selecione o insumo para entrada");
			return;
		}

		if (quantity == 0.0) {
			MessageDialog.showAlertDialog(getTitle(), "Informe a quantidade de entrada");
			return;
		}

		try {
			date = LocalDate.parse(ftfDate.getText(), DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN));
		} catch (Exception ex) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.INVALID_DATE);
			return;
		}

		if (date.isAfter(LocalDate.now())) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.INVALID_DATE);
			return;
		}

		try {
			price = StringExt.toDouble(ftfprice.getText());
			
			if (price  == 0.0) {
				MessageDialog.showAlertDialog(getTitle(), "Informe o valor de entrada");
				return;
			}
			
			InputEntry iStock = new InputEntry(input.getId(), quantity, price, date);

			inputEntryController.insert(iStock);
			MessageDialog.showInformationDialog(this, getTitle(), Constants.RECORD_SUCCESSFULLY_INSERTED);
			resetForm();
		} catch (NumberFormatException ex) {
			MessageDialog.showAlertDialog(this, getTitle(), "Informe um preço válido para o insumo");
		} catch (IOException ex) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_INSERT_RECORD);
		}
	}

	private void resetForm() {
		cbInput.setSelectedIndex(0);
		ftfprice.setText("0.00");
		spQuantity.setValue(0.0);
		ftfDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN)));
	}

	private void onCancel(ActionEvent e) {
		dispose();
	}

	private class InputComboboxRenderer extends BasicComboBoxRenderer {
		@Override
		public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			setText(((Input) value).getName());
			return this;
		}
	}
}
