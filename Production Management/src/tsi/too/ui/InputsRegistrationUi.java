package tsi.too.ui;

import tsi.too.Constants;
import tsi.too.controller.InputController;
import tsi.too.controller.InputPriceEntryController;
import tsi.too.ext.StringExt;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;
import tsi.too.model.PriceEntry;
import tsi.too.util.Pair;
import tsi.too.util.UiUtils;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import static tsi.too.Constants.*;

@SuppressWarnings("serial")
public class InputsRegistrationUi extends JDialog {

    private JTextField tfName;
    private BottomActionPanel bottomActionPanel;
    private JFormattedTextField ftfPrice;

    private Input input;

    private final Component parentComponent;
    private InputController inputController;
    private InputPriceEntryController priceEntryController;

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

        JLabel lblPrice = new JLabel(String.format("%s:", Constants.PRICE));

        initPriceTextField();
        lblPrice.setLabelFor(ftfPrice);

        GroupLayout gl_inputsPanel = new GroupLayout(inputsPanel);
        gl_inputsPanel.setHorizontalGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_inputsPanel.createSequentialGroup().addContainerGap()
                        .addGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_inputsPanel.createSequentialGroup().addComponent(lblName)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(tfName, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
                                .addGroup(gl_inputsPanel.createSequentialGroup().addComponent(lblPrice)
                                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(ftfPrice,
                                                GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap()));
        gl_inputsPanel.setVerticalGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_inputsPanel
                .createSequentialGroup().addContainerGap()
                .addGroup(gl_inputsPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblName))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_inputsPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblPrice).addComponent(
                        ftfPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(20)));
        inputsPanel.setLayout(gl_inputsPanel);

        setupBottomPanel();
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                        .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                .addComponent(inputsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 562,
                                        Short.MAX_VALUE)
                                .addComponent(bottomActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                        .addComponent(inputsPanel, GroupLayout.PREFERRED_SIZE, 90, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(bottomActionPanel,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()));

        getContentPane().setLayout(groupLayout);
    }

    private void initPriceTextField() {
        ftfPrice = new JFormattedTextField();
        ftfPrice.setFormatterFactory(UiUtils.createCurrencyFormatterFactory(0.0, Double.MAX_VALUE));
    }

    private void setupBottomPanel() {
        var positiveText = input == null ? TO_RECORD : UPDATE;

        bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), positiveText, this::onOk);
    }

    private void onOk(ActionEvent actionEvent) {
        var name = tfName.getText();
        var price = StringExt.toDouble(ftfPrice.getText());

        var item = new Input(name, price);

        var validationMessage = getValidationMessage(item);
        if (!StringExt.isNullOrBlank(validationMessage)) {
            MessageDialog.showAlertDialog(this, getTitle(), validationMessage);
            return;
        }

        try {
            var target = inputController.findByName(name);
            Input out;
            boolean shouldSavePriceEntry;

            if (UPDATE.equals(actionEvent.getActionCommand())) {
                out = update(item.withId(input.getId()), target);
                shouldSavePriceEntry = item.getPrice() != input.getPrice();
            } else {
                out = addProduct(item, target);
                shouldSavePriceEntry = true;
            }

            resetForm();

            if (shouldSavePriceEntry) {
                try {
                    priceEntryController.insert(new PriceEntry(Objects.requireNonNull(out).getId(), out.getPrice()));
                } catch (IOException e) {
                    MessageDialog.showAlertDialog(this, getTitle(), Constants.FAILED_TO_LOG_PRICE_ENTRY);
                }
            }
        } catch (IOException e) {
            var message = actionEvent.getActionCommand().equals(UPDATE) ? FAILED_TO_UPDATE_RECORD : FAILED_TO_INSERT_RECORD;
            MessageDialog.showAlertDialog(this, getTitle(), message);
        }
    }

    private String getValidationMessage(Input item) {
        if (!inputController.nameValidator.isValid(item.getName())) {
            return inputController.nameValidator.getErrorMessage(item.getName());
        }

        if (!inputController.priceValidator.isValid(item.getPrice())) {
            return inputController.priceValidator.getErrorMessage(item.getPrice());
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
        ftfPrice.setText("0.00");
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
            priceEntryController = InputPriceEntryController.getInstance();
        } catch (FileNotFoundException e) {
            MessageDialog.showAlertDialog(this, getTitle(), Constants.UNABLE_TO_OPEN_FILE);
            dispose();
        }
    }

    private void fillFields() {
        if (input == null)
            return;

        ftfPrice.setText(String.valueOf(input.getPrice()));
        tfName.setText(input.getName());
    }
}