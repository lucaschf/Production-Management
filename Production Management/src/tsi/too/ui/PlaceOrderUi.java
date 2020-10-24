package tsi.too.ui;

import java.awt.Component;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;
import tsi.too.model.Product;

@SuppressWarnings("serial")
public class PlaceOrderUi extends JDialog {
	private JTextField tfOrderNumber;
	private JTextField ftfTotalOrderValue;
	private JTable table;
	private JFormattedTextField ftfPlacementDate;

	public PlaceOrderUi(Component parentComponent) {
		setModal(true);
		setTitle(Constants.SELLING_PRODUCTS);

		JPanel OrderPane = new JPanel();
		OrderPane.setBorder(new TitledBorder(null, Constants.SALE, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel orderItemsPane = new JPanel();
		orderItemsPane.setBorder(
				new TitledBorder(null, Constants.ORDER_ITEMS, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		BottomActionPanel bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(),
				Constants.TO_RECORD, e -> onOk());

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(orderItemsPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 566,
								Short.MAX_VALUE)
						.addComponent(OrderPane, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE).addComponent(
								bottomActionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(OrderPane, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(orderItemsPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(bottomActionPanel, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
						.addContainerGap()));

		JLabel lblTotalOrderValue = new JLabel(String.format("%s:", Constants.TOTAL_ORDER_VALUE));

		ftfTotalOrderValue = new JTextField();
		lblTotalOrderValue.setLabelFor(ftfTotalOrderValue);
		ftfTotalOrderValue.setEditable(false);
		ftfTotalOrderValue.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_orderItemsPane = new GroupLayout(orderItemsPane);
		gl_orderItemsPane.setHorizontalGroup(gl_orderItemsPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_orderItemsPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_orderItemsPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
								.addGroup(gl_orderItemsPane.createSequentialGroup().addComponent(lblTotalOrderValue)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(ftfTotalOrderValue,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		gl_orderItemsPane.setVerticalGroup(gl_orderItemsPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_orderItemsPane.createSequentialGroup().addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_orderItemsPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(ftfTotalOrderValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTotalOrderValue))
						.addContainerGap()));

		table = new JTable();
		scrollPane.setColumnHeaderView(table);
		orderItemsPane.setLayout(gl_orderItemsPane);

		JLabel lblOrderNumber = new JLabel(String.format("%s:", Constants.CODE));

		tfOrderNumber = new JTextField();
		lblOrderNumber.setLabelFor(tfOrderNumber);
		tfOrderNumber.setEditable(false);
		tfOrderNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		tfOrderNumber.setColumns(10);

		JLabel lblPlacementDate = new JLabel(String.format("%s:", Constants.DATE));
		lblPlacementDate.setHorizontalAlignment(SwingConstants.TRAILING);

		ftfPlacementDate = new JFormattedTextField();
		lblPlacementDate.setLabelFor(ftfPlacementDate);
		ftfPlacementDate.setEditable(false);

		JLabel lblPlacementHour = new JLabel("Hora:");
		lblPlacementHour.setHorizontalAlignment(SwingConstants.TRAILING);

		JFormattedTextField ftfPlacementHour = new JFormattedTextField();
		lblPlacementHour.setLabelFor(ftfPlacementHour);
		ftfPlacementHour.setEditable(false);

		JPanel productChooserPanel = new JPanel();
		productChooserPanel.setBorder(
				new TitledBorder(null, Constants.PRODUCT, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_OrderPane = new GroupLayout(OrderPane);
		gl_OrderPane.setHorizontalGroup(gl_OrderPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_OrderPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_OrderPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(productChooserPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 526,
										Short.MAX_VALUE)
								.addGroup(gl_OrderPane.createSequentialGroup()
										.addComponent(lblOrderNumber, GroupLayout.PREFERRED_SIZE, 44,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfOrderNumber, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblPlacementDate, GroupLayout.PREFERRED_SIZE, 36,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(ftfPlacementDate, GroupLayout.PREFERRED_SIZE, 111,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblPlacementHour, GroupLayout.PREFERRED_SIZE, 36,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(ftfPlacementHour, 114, 114, 114)))
						.addContainerGap()));
		gl_OrderPane
				.setVerticalGroup(gl_OrderPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_OrderPane.createSequentialGroup().addContainerGap()
								.addGroup(gl_OrderPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(ftfPlacementHour, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblPlacementHour)
										.addComponent(ftfPlacementDate, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblPlacementDate)
										.addComponent(tfOrderNumber, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblOrderNumber))
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(productChooserPanel,
										GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(24, Short.MAX_VALUE)));

		JLabel lblNewLabel_3 = new JLabel(String.format("%s:", Constants.PRODUCT));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.TRAILING);

		JComboBox<Product> cbProduct = new JComboBox<Product>();

		JLabel lblNewLabel_4 = new JLabel(String.format("%s:", Constants.QUANTITY));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.TRAILING);

		JSpinner spQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

		JButton btnAddItem = new JButton(Constants.ADD_TO_ORDER_ITEMS);
		GroupLayout gl_productChooserPanel = new GroupLayout(productChooserPanel);
		gl_productChooserPanel
				.setHorizontalGroup(gl_productChooserPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_productChooserPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_productChooserPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(Alignment.TRAILING, gl_productChooserPanel.createSequentialGroup()
												.addGroup(gl_productChooserPanel.createParallelGroup(Alignment.TRAILING)
														.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 50,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(lblNewLabel_4))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(gl_productChooserPanel.createParallelGroup(Alignment.LEADING)
														.addComponent(cbProduct, 0, 417, Short.MAX_VALUE)
														.addComponent(spQuantity, GroupLayout.PREFERRED_SIZE, 80,
																GroupLayout.PREFERRED_SIZE)))
										.addComponent(btnAddItem, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 198,
												GroupLayout.PREFERRED_SIZE))
								.addContainerGap()));
		gl_productChooserPanel.setVerticalGroup(gl_productChooserPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_productChooserPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_productChooserPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbProduct, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_3))
						.addGroup(gl_productChooserPanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_productChooserPanel.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_productChooserPanel.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblNewLabel_4).addComponent(spQuantity,
														GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
										.addContainerGap(44, Short.MAX_VALUE))
								.addGroup(gl_productChooserPanel.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnAddItem)
										.addContainerGap()))));
		productChooserPanel.setLayout(gl_productChooserPanel);
		OrderPane.setLayout(gl_OrderPane);
		getContentPane().setLayout(groupLayout);
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void onOk() {
	}

	private void onCancel() {
		dispose();
	}
}
