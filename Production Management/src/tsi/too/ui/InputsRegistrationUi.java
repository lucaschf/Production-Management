package tsi.too.ui;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;

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
import tsi.too.controller.InputsController;
import tsi.too.ext.StringExt;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;

@SuppressWarnings("serial")
public class InputsRegistrationUi extends JDialog {

	Component parentComponent;
	private InputsController inputsController;
	private JTextField tfInputName;
	private JFormattedTextField ftfUnitaryPrice;

	public InputsRegistrationUi(Component parentComponent) {
		setResizable(false);
		this.parentComponent = parentComponent;

		initComponent();

		setupWindow();
		initController();
	}

	private void initComponent() {
		
		JPanel inputsPanel = new JPanel();
		inputsPanel.setBorder(new TitledBorder(null, Constants.INPUT_DATA, TitledBorder.LEADING, TitledBorder.TOP,								null, null));
		
		JLabel lblProductionInputName = new JLabel("Nome:");
		lblProductionInputName.setHorizontalAlignment(SwingConstants.TRAILING);
		
		tfInputName = new JTextField();
		tfInputName.setColumns(10);
		
		JLabel lblunitaryPrice = new JLabel("Pre\u00E7o Unit\u00E1rio:");
		lblunitaryPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		
		ftfUnitaryPrice = new JFormattedTextField();
		GroupLayout gl_inputsPanel = new GroupLayout(inputsPanel);
		gl_inputsPanel.setHorizontalGroup(
			gl_inputsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_inputsPanel.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_inputsPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblProductionInputName, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblunitaryPrice, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
					.addGap(4)
					.addGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(ftfUnitaryPrice, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfInputName, GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_inputsPanel.setVerticalGroup(
			gl_inputsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_inputsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_inputsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfInputName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblProductionInputName))
					.addGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_inputsPanel.createSequentialGroup()
							.addGap(13)
							.addComponent(lblunitaryPrice))
						.addGroup(gl_inputsPanel.createSequentialGroup()
							.addGap(10)
							.addComponent(ftfUnitaryPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		inputsPanel.setLayout(gl_inputsPanel);
		
		BottomActionPanel bottomActionPanel = new BottomActionPanel("Cancelar", e -> onCancel(), "Gravar", e -> recordInput());
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(bottomActionPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(inputsPanel, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(inputsPanel, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(bottomActionPanel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		getContentPane().setLayout(groupLayout);

	}

	private void onCancel() {
		dispose();
	}

	private void recordInput() {
		try {
			inputsController.insert(
					new Input(tfInputName.getText(), 0, StringExt.toDouble(ftfUnitaryPrice.getText())));
			MessageDialog.showInformationDialog(this, getTitle(), Constants.RECORD_SUCCESSFULY_INSERTED);
			clearFields();
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_INSERT_RECORD);
		}
	}

	private void clearFields() {
		tfInputName.setText("");
		ftfUnitaryPrice.setText("");
	}

	private void setupWindow() {
		setTitle(Constants.REGISTER_PRODUCTION_INPUTS);

		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initController() {
		try {
			inputsController = InputsController.getInstance();
		} catch (FileNotFoundException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.UNABLE_TO_OPEN_FILE);
			dispose();
		}
	}
}
