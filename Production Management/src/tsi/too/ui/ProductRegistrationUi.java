package tsi.too.ui;

import java.awt.Component;

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
import tsi.too.model.MeasureUnity;

@SuppressWarnings("serial")
public class ProductRegistrationUi extends JDialog {
	private JTextField tfName;
	private JComboBox<MeasureUnity> cbUnity;
	private JPanel fieldsPanel;
	private JLabel lblName;
	private JButton btnRegisterProductionInputs;

	public ProductRegistrationUi(Component parentComponent) {
		setTitle(Constants.PRODUCT_REGISTRATION);
		initComponents();
		pack();
		setResizable(false);
		setModal(true);
		setLocationRelativeTo(parentComponent);
	}

	private void initContentGroupLayout() {
		BottomActionPanel bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(),
				Constants.TO_RECORD, e -> onOk());
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

	private void onCancel() {
		dispose();
	}

	private void onOk() {
		// TODO Auto-generated method stub
	}

	private void initComponents() {
		initFieldsPanel();
		initContentGroupLayout();
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

		JSpinner spProfitmargin = new JSpinner();
		lblProfitMargin.setLabelFor(spProfitmargin);
		spProfitmargin.setModel(new SpinnerNumberModel(0.0, 0.0, 100.0, 1.0));

		btnRegisterProductionInputs = new JButton(Constants.REGISTER_PRODUCTION_INPUTS);

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
												.addComponent(btnRegisterProductionInputs, GroupLayout.PREFERRED_SIZE,
														143, GroupLayout.PREFERRED_SIZE))
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
								.addComponent(btnRegisterProductionInputs).addContainerGap()))));

		fieldsPanel.setLayout(gl_panel);
	}

	private void setupNameTextField() {
		tfName = new JTextField();
		lblName.setLabelFor(tfName);
		tfName.setColumns(10);
	}
}
