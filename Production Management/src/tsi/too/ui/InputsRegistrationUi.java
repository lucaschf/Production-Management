package tsi.too.ui;

import static tsi.too.Constants.AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED;
import static tsi.too.Constants.FAILED_TO_INSERT_RECORD;
import static tsi.too.Constants.FAILED_TO_UPDATE_RECORD;
import static tsi.too.Constants.RECORD_SUCCESSFULY_UPDATED;
import static tsi.too.Constants.TO_RECORD;
import static tsi.too.Constants.UPDATE;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;
import tsi.too.controller.InputController;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;
import tsi.too.util.Pair;

@SuppressWarnings("serial")
public class InputsRegistrationUi extends JDialog {

	Component parentComponent;
	private InputController inputController;
	private JTextField tfName;
	private BottomActionPanel bottomActionPanel;

	private Input input;

	public InputsRegistrationUi(Component parentComponent) {
		this(parentComponent, null);
	}

	public InputsRegistrationUi(Component parentComponent, Input input) {
		this.input = input == null ? null : input.clone();
		this.parentComponent = parentComponent;

		initComponent();

		setupWindow();
		initController();
		fillFields();
	}

	private void initComponent() {
		JPanel inputsPanel = new JPanel();
		inputsPanel.setBorder(
				new TitledBorder(null, Constants.INPUT_DATA, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblName = new JLabel(String.format("%s:", Constants.NAME));
		lblName.setHorizontalAlignment(SwingConstants.TRAILING);

		tfName = new JTextField();
		tfName.setColumns(10);

		GroupLayout gl_inputsPanel = new GroupLayout(inputsPanel);
		gl_inputsPanel.setHorizontalGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_inputsPanel.createSequentialGroup().addContainerGap().addComponent(lblName)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tfName, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE).addContainerGap()));
		gl_inputsPanel.setVerticalGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_inputsPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_inputsPanel.createParallelGroup(Alignment.BASELINE).addComponent(tfName,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblName))
						.addGap(42)));
		inputsPanel.setLayout(gl_inputsPanel);

		setupBottomPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(inputsPanel, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
								.addComponent(bottomActionPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(inputsPanel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(bottomActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		getContentPane().setLayout(groupLayout);
	}

	private void setupBottomPanel() {
		var positiveText = input == null ? TO_RECORD : UPDATE;

		bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), positiveText, e -> onOk(e));
	}

	private void onOk(ActionEvent actionEvent) {
		var name = tfName.getText();
		if (!inputController.nameValidator.isValid(name)) {
			MessageDialog.showAlertDialog(this, getTitle(), inputController.nameValidator.getErrorMessage(name));
			return;
		}

		try {
			var item = new Input(name).withId(input.getId());
			var target = inputController.findByName(name);

			switch (actionEvent.getActionCommand()) {
				case UPDATE:
					update(item, target);
					break;
				case TO_RECORD:
					addProduct(item, target);
					break;
				default:
					break;
			}

		} catch (IOException e) {
			var message = actionEvent.getActionCommand() == UPDATE ? FAILED_TO_UPDATE_RECORD : FAILED_TO_INSERT_RECORD;
			MessageDialog.showAlertDialog(this, getTitle(), message);
		}
	}

	private void addProduct(Input p, Pair<Input, Long> target) {
		try {
			if (target != null) {
				MessageDialog.showAlertDialog(this, getTitle(), AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED);
				return;
			}

			inputController.insert(p);
			MessageDialog.showInformationDialog(this, getTitle(), Constants.RECORD_SUCCESSFULY_INSERTED);
			resetForm();
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), FAILED_TO_INSERT_RECORD);
		}
	}

	private void update(Input newData, Pair<Input, Long> target) {
		try {
			if (target != null && target.getFirst().getId() != newData.getId()) {
				MessageDialog.showAlertDialog(this, getTitle(), AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED);
				return;
			}

			if (target == null)
				target = inputController.findByName(input.getName());

			inputController.update(target.getSecond(), newData);
			MessageDialog.showInformationDialog(this, getTitle(), RECORD_SUCCESSFULY_UPDATED);
			resetForm();

		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			MessageDialog.showAlertDialog(this, getTitle(), FAILED_TO_UPDATE_RECORD);
		}
	}

	private void onCancel() {
		dispose();
	}

	private void resetForm() {
		tfName.setText("");
		bottomActionPanel.setPositiveText(Constants.TO_RECORD);
		tfName.requestFocus();
		input = null;
	}

	private void setupWindow() {
		setTitle(Constants.REGISTER_PRODUCTION_INPUTS);
		setResizable(false);
		setModal(true);
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initController() {
		try {
			inputController = InputController.getInstance();
		} catch (FileNotFoundException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.UNABLE_TO_OPEN_FILE);
			dispose();
		}
	}

	private void fillFields() {
		if (input == null)
			return;

		tfName.setText(input.getName());
	}
}