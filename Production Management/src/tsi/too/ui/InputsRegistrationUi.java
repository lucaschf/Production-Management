package tsi.too.ui;

import static tsi.too.Constants.AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED;
import static tsi.too.Constants.FAILED_TO_INSERT_RECORD;
import static tsi.too.Constants.FAILED_TO_UPDATE_RECORD;
import static tsi.too.Constants.RECORD_SUCCESSFULLY_UPDATED;
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
import tsi.too.controller.InputPriceEntryController;
import tsi.too.ext.StringExt;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;
import tsi.too.util.Pair;

@SuppressWarnings("serial")
public class InputsRegistrationUi extends JDialog {

    private JTextField tfName;
    private BottomActionPanel bottomActionPanel;

    private Input input;

    private final Component parentComponent;
    private InputController inputController;

    /**
     * @param parentComponent
     * @wbp.parser.constructor
     */
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

        initPriceTextField();

        GroupLayout gl_inputsPanel = new GroupLayout(inputsPanel);
        gl_inputsPanel.setHorizontalGroup(
        	gl_inputsPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_inputsPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblName)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(tfName, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        			.addContainerGap())
        );
        gl_inputsPanel.setVerticalGroup(
        	gl_inputsPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_inputsPanel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_inputsPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblName))
        			.addGap(35))
        );
        inputsPanel.setLayout(gl_inputsPanel);

        setupBottomPanel();
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(inputsPanel, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
        				.addComponent(bottomActionPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap())
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(inputsPanel, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(bottomActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );

        getContentPane().setLayout(groupLayout);
    }

    private void initPriceTextField() {
    }

    private void setupBottomPanel() {
        var positiveText = input == null ? TO_RECORD : UPDATE;

        bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), positiveText, this::onOk);
    }

    private void onOk(ActionEvent actionEvent) {
        var name = tfName.getText();

        var item = new Input(name);

        var validationMessage = getValidationMessage(item);
        if (!StringExt.isNullOrBlank(validationMessage)) {
            MessageDialog.showAlertDialog(this, getTitle(), validationMessage);
            return;
        }

        try {
            var target = inputController.findByName(name);

            if (UPDATE.equals(actionEvent.getActionCommand())) {
                update(item.withId(input.getId()), target);
            } else {
                addProduct(item, target);
            }

            resetForm();

        } catch (IOException e) {
            var message = actionEvent.getActionCommand().equals(UPDATE) ? FAILED_TO_UPDATE_RECORD : FAILED_TO_INSERT_RECORD;
            MessageDialog.showAlertDialog(this, getTitle(), message);
        }
    }

    private String getValidationMessage(Input item) {
        if (!inputController.nameValidator.isValid(item.getName())) {
            return inputController.nameValidator.getErrorMessage(item.getName());
        }

        return null;
    }

    private Input addProduct(Input p, Pair<Input, Long> target) {
        try {
            if (target != null) {
                MessageDialog.showAlertDialog(this, getTitle(), AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED);
                return null;
            }

            var out = inputController.insert(p);
            MessageDialog.showInformationDialog(this, getTitle(), Constants.RECORD_SUCCESSFULLY_INSERTED);
            return out;
        } catch (IOException e) {
            MessageDialog.showAlertDialog(this, getTitle(), FAILED_TO_INSERT_RECORD);
            return null;
        }
    }

    private Input update(Input newData, Pair<Input, Long> target) {
        try {
            if (target != null && target.getFirst().getId() != newData.getId()) {
                MessageDialog.showAlertDialog(this, getTitle(), AN_ITEM_WITH_THIS_NAME_ALREADY_REGISTERED);
                return null;
            }

            if (target == null)
                target = inputController.findByName(input.getName());

            inputController.update(target.getSecond(), newData);
            MessageDialog.showInformationDialog(this, getTitle(), RECORD_SUCCESSFULLY_UPDATED);
            return newData;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            MessageDialog.showAlertDialog(this, getTitle(), FAILED_TO_UPDATE_RECORD);
            return null;
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