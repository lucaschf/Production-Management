package tsi.too.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;

@SuppressWarnings("serial")
public class SalesReportUi extends JDialog {
	public SalesReportUi(Component parentComponent) {
		JPanel periodPanel = new JPanel();
		periodPanel.setBorder(
				new TitledBorder(null, Constants.SALES_PERIOD, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, Constants.SALES_DATA, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		BottomActionPanel bottomPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), Constants.OK,
				e -> onOk());

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
						.addComponent(periodPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
						.addComponent(bottomPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(periodPanel, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE).addContainerGap()));

		JScrollPane scrollPane = new JScrollPane();

		JLabel lblTotalSalesValue = new JLabel(String.format("%s:", Constants.TOTAL_SALES_VALUE));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, Constants.SALES_CLASSIFICATION, TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setLayout(null);

		JButton btnOrderByDate = new JButton(Constants.BY_DATE);
		btnOrderByDate.setBounds(20, 29, 180, 28);
		panel_1.add(btnOrderByDate);

		JButton btnOrderByHour = new JButton(Constants.BY_HOUR);
		btnOrderByHour.setBounds(212, 29, 180, 28);
		panel_1.add(btnOrderByHour);

		JFormattedTextField formattedTextField_2 = new JFormattedTextField();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup()
												.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 832,
														Short.MAX_VALUE)
												.addContainerGap())
										.addGroup(gl_panel.createSequentialGroup()
												.addComponent(lblTotalSalesValue, GroupLayout.PREFERRED_SIZE, 118,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedTextField_2, GroupLayout.PREFERRED_SIZE, 145,
														GroupLayout.PREFERRED_SIZE)
												.addContainerGap(569, Short.MAX_VALUE))
										.addGroup(gl_panel.createSequentialGroup()
												.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
												.addGap(6)))));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup().addGap(9)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTotalSalesValue, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		panel.setLayout(gl_panel);

		JLabel lblStartDateTime = new JLabel(String.format("%s:", Constants.START_DATE_OR_TIME));

		JFormattedTextField formattedTextField = new JFormattedTextField();

		JLabel lblFinalDateTime = new JLabel(String.format("%s:", Constants.FINAL_DATE_OR_TIME));

		JFormattedTextField formattedTextField_1 = new JFormattedTextField();

		JButton btnNewButton = new JButton(Constants.GET_SALES_DATA);
		GroupLayout gl_periodPanel = new GroupLayout(periodPanel);
		gl_periodPanel.setHorizontalGroup(gl_periodPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_periodPanel.createSequentialGroup().addContainerGap().addComponent(lblStartDateTime)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(formattedTextField, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblFinalDateTime)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(formattedTextField_1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnNewButton)
						.addContainerGap(181, Short.MAX_VALUE)));
		gl_periodPanel
				.setVerticalGroup(gl_periodPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_periodPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_periodPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblStartDateTime)
										.addComponent(formattedTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblFinalDateTime)
										.addComponent(formattedTextField_1, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnNewButton))
								.addContainerGap(78, Short.MAX_VALUE)));
		periodPanel.setLayout(gl_periodPanel);
		setMinimumSize(new Dimension(900, 600));
		setLocationRelativeTo(parentComponent);
		pack();
		getContentPane().setLayout(groupLayout);
	}

	private void onOk() {
		dispose();
	}

	private void onCancel() {
		dispose();
	}
}
