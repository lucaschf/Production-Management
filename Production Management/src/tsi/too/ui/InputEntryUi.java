package tsi.too.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
	private JFormattedTextField ftfPrice;
	private JComboBox<Input> cbInput;
	private JSpinner spQuantity;

	private final Component parentComponent;

	private InputController inputController;
	private InputEntryController inputEntryController;
	private JFormattedTextField ftfDate;

	/**
	 * @wbp.parser.constructor
	 */
	public InputEntryUi(Component parentComponent) {
		this(parentComponent, null);
	}

	public InputEntryUi(Component parentComponent, Input input) {
		this.parentComponent = parentComponent;

		initController();

		initComponent();
		setupWindow();

		resetForm();

		cbInput.setSelectedItem(input);
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
		setTitle(Constants.INPUT_ENTRIES);
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initComponent() {
		JPanel panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, Constants.ENTRY_DATA, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		BottomActionPanel bottomActionPanel = new BottomActionPanel(Constants.CANCEL, this::onCancel, Constants.ADD,
				this::onOk);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
						.addComponent(bottomActionPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(bottomActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		JLabel lblQuantity = new JLabel(String.format("%s:", Constants.QUANTITY));
		lblQuantity.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblInput = new JLabel(String.format("%s:", Constants.INPUT));
		lblInput.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblPrice = new JLabel(String.format("%s:", Constants.UNITARY_PRICE));
		lblPrice.setHorizontalAlignment(SwingConstants.TRAILING);

		try {
			cbInput = new JComboBox<>();
			cbInput.setModel(new DefaultComboBoxModel<>(inputController.fetchInputsAsVector()));
			cbInput.setRenderer(new InputComboboxRenderer());
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_FETCH_DATA);
		}
		lblInput.setLabelFor(cbInput);

		ftfPrice = new JFormattedTextField(UiUtils.createCurrencyFormatterFactory(0.0, Double.MAX_VALUE));
		lblPrice.setLabelFor(ftfPrice);

		SpinnerNumberModel model = new SpinnerNumberModel(0.0, 0.0, null, 0.5);
		spQuantity = new JSpinner(model);

		JLabel lblDate = new JLabel(String.format("%s:", Constants.DATE));

		ftfDate = new JFormattedTextField(UiUtils.createBrazilianDateMaskFormatter());
		lblDate.setLabelFor(ftfDate);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblInput)
						.addComponent(lblQuantity)
						.addComponent(lblPrice)
						.addComponent(lblDate))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(cbInput, Alignment.TRAILING, 0, 312, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(ftfDate, Alignment.LEADING)
							.addComponent(spQuantity, Alignment.LEADING)
							.addComponent(ftfPrice, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblInput))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(spQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblQuantity))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(ftfPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPrice))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(ftfDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDate))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
	}

	private void onOk(ActionEvent e) {

		Input input = (Input) cbInput.getSelectedItem();
		if (input == null) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.SELECT_THE_INPUT);
			return;
		}

		var quantity = (Double) spQuantity.getValue();
		if (quantity == 0.0) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.INFORM_THE_QUANTITY);
			return;
		}

		LocalDate date;
		try {
			date = LocalDate.parse(ftfDate.getText(), DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN));
		} catch (DateTimeParseException ex) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.INVALID_DATE);
			return;
		}

		if (date.isAfter(LocalDate.now())) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.INVALID_DATE);
			return;
		}

		double price;
		try {
			price = StringExt.toDouble(ftfPrice.getText());
		} catch (NumberFormatException ex) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.INFORM_THE_UNITARY_PRICE);
			return;
		}

		try {
			InputEntry iStock = new InputEntry(input.getId(), quantity, price, date);

			inputEntryController.insert(iStock);
			MessageDialog.showInformationDialog(this, getTitle(), Constants.RECORD_SUCCESSFULLY_INSERTED);
			resetForm();
		} catch (IOException | CloneNotSupportedException ex) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_INSERT_RECORD);
		}
	}

	private void resetForm() {
		try {
			cbInput.setSelectedIndex(0);
		} catch (Exception ignored) {
		}
		
		ftfPrice.setText("0.00");
		spQuantity.setValue(0.0);
		ftfDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern(Patterns.BRAZILIAN_DATE_PATTERN)));
	}

	private void onCancel(ActionEvent e) {
		dispose();
	}

	private static class InputComboboxRenderer extends BasicComboBoxRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value != null)
				setText(((Input) value).getName());

			return this;
		}
	}
}