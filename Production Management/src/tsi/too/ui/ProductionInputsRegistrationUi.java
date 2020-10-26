package tsi.too.ui;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;
import tsi.too.model.Product;
import tsi.too.ui.table_model.ProductionInputsTableModel;

@SuppressWarnings("serial")
public class ProductionInputsRegistrationUi extends JDialog {

	private JTextField tfProductionInputsName;
	private JComboBox<Product> cbProductName;
	private JFormattedTextField ftfUnitySize;
	private JSpinner spProductionInputQuantity;
	private JFormattedTextField ftfUnitaryPrice;
	private JButton btnAddInput;
	private JScrollPane scrollPane;
	private JTable table;

	public ProductionInputsRegistrationUi(Component parentComponent, List<Product> products) {
		this(parentComponent);
	}


	/**
	 * @wbp.parser.constructor
	 */
	public ProductionInputsRegistrationUi(Component parentComponent) {
		initComponent();
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initComponent() {
		JPanel productPanel = new JPanel();
		productPanel.setBorder(
				new TitledBorder(null, Constants.PRODUCT, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, Constants.PRODUCTION_INPUTS, TitledBorder.LEADING, TitledBorder.TOP,
				null, null));

		BottomActionPanel bottomActionPanel = new BottomActionPanel(Constants.CANCEL, (ActionListener) null,
				Constants.TO_RECORD, (ActionListener) null);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
						.addComponent(productPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
						.addComponent(bottomActionPanel, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(productPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(bottomActionPanel,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		JLabel lblunitaryPrice = new JLabel(String.format("%s:", Constants.UNITARY_PRICE));
		lblunitaryPrice.setHorizontalAlignment(SwingConstants.TRAILING);

		ftfUnitaryPrice = new JFormattedTextField();
		lblunitaryPrice.setLabelFor(ftfUnitaryPrice);

		JLabel lblProductionInputQuantity = new JLabel(String.format("%s:", Constants.QUANTITY));
		lblProductionInputQuantity.setHorizontalAlignment(SwingConstants.TRAILING);

		spProductionInputQuantity = new JSpinner();
		lblProductionInputQuantity.setLabelFor(spProductionInputQuantity);

		JLabel lblProductionInputName = new JLabel(String.format("%s:", Constants.NAME));
		lblProductionInputName.setHorizontalAlignment(SwingConstants.TRAILING);

		tfProductionInputsName = new JTextField();
		lblProductionInputName.setLabelFor(tfProductionInputsName);
		tfProductionInputsName.setColumns(10);

		btnAddInput = new JButton(Constants.ADD);

		scrollPane = new JScrollPane();

		JLabel lblMessage = new JLabel(Constants.PRODUCTION_INPUTS_SIZE_MESSAGE);
		lblMessage.setIcon(new ImageIcon(ProductionInputsRegistrationUi.class.getResource("/resources/ic_info.png")));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
								.addGroup(gl_panel.createSequentialGroup()
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
												.addComponent(lblProductionInputQuantity).addComponent(lblunitaryPrice)
												.addComponent(lblProductionInputName, GroupLayout.PREFERRED_SIZE, 50,
														GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addComponent(tfProductionInputsName, GroupLayout.DEFAULT_SIZE, 451,
														Short.MAX_VALUE)
												.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
														.addGroup(gl_panel.createSequentialGroup()
																.addComponent(spProductionInputQuantity,
																		GroupLayout.PREFERRED_SIZE, 104,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(lblMessage, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
														.addComponent(ftfUnitaryPrice, Alignment.LEADING,
																GroupLayout.PREFERRED_SIZE, 237,
																GroupLayout.PREFERRED_SIZE))))
								.addComponent(btnAddInput, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 184,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfProductionInputsName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblProductionInputName))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(spProductionInputQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblProductionInputQuantity).addComponent(lblMessage))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(ftfUnitaryPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblunitaryPrice))
				.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnAddInput)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE).addContainerGap()));
		
		table = new JTable();
		table.setModel(new ProductionInputsTableModel(new ArrayList<>()));
		scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);

		JLabel lblUnitySize = new JLabel(String.format("%s:", Constants.UNIT_SIZE));
		lblUnitySize.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblProductName = new JLabel(String.format("%s:", Constants.NAME));
		lblProductName.setHorizontalAlignment(SwingConstants.TRAILING);

		cbProductName = new JComboBox<Product>();
		lblProductName.setLabelFor(cbProductName);

		ftfUnitySize = new JFormattedTextField();
		lblUnitySize.setLabelFor(ftfUnitySize);
		GroupLayout gl_productPanel = new GroupLayout(productPanel);
		gl_productPanel.setHorizontalGroup(gl_productPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_productPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_productPanel.createParallelGroup(Alignment.TRAILING).addComponent(lblUnitySize)
								.addComponent(lblProductName, GroupLayout.PREFERRED_SIZE, 50,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_productPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(cbProductName, 0, 389, Short.MAX_VALUE).addComponent(ftfUnitySize,
										Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		gl_productPanel.setVerticalGroup(gl_productPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_productPanel.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_productPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbProductName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblProductName))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_productPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblUnitySize)
								.addComponent(ftfUnitySize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		productPanel.setLayout(gl_productPanel);
		getContentPane().setLayout(groupLayout);
	}
}
