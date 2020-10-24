package tsi.too.ui;

import java.awt.Component;
import java.time.Month;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tsi.too.Constants;

@SuppressWarnings("serial")
public class BudgetUi extends JDialog {
	public BudgetUi(Component parentComponent) {
		setModal(true);
		JPanel filterPanel = new JPanel();
		filterPanel.setBorder(
				new TitledBorder(null, Constants.PERIOD, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(
				new TitledBorder(null, Constants.BUDGET, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		BottomActionPanel bottomActionPanel = new BottomActionPanel(Constants.CANCEL, e -> onCancel(), Constants.OK,
				e -> onOk());
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(bottomActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(filterPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
						.addComponent(dataPanel, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(14)
						.addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(dataPanel, GroupLayout.PREFERRED_SIZE, 290, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(bottomActionPanel,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(9)));

		JPanel expensesPanel = new JPanel();
		expensesPanel.setBorder(
				new TitledBorder(null, Constants.EXPENSES, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel recipesPanel = new JPanel();
		recipesPanel.setBorder(
				new TitledBorder(null, Constants.RECIPE, TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel balancePanel = new JPanel();
		balancePanel.setBorder(
				new TitledBorder(null, Constants.BALANCE, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_dataPanel = new GroupLayout(dataPanel);
		gl_dataPanel.setHorizontalGroup(gl_dataPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_dataPanel
				.createSequentialGroup()
				.addGroup(gl_dataPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_dataPanel.createSequentialGroup().addGap(10)
								.addGroup(gl_dataPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(recipesPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 542,
												Short.MAX_VALUE)
										.addComponent(balancePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 542,
												Short.MAX_VALUE)))
						.addGroup(gl_dataPanel.createSequentialGroup().addContainerGap().addComponent(expensesPanel,
								GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)))
				.addContainerGap()));
		gl_dataPanel.setVerticalGroup(gl_dataPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dataPanel.createSequentialGroup().addContainerGap()
						.addComponent(expensesPanel, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(recipesPanel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(balancePanel, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(13, Short.MAX_VALUE)));

		JLabel lblBalance = new JLabel(String.format("%s:", Constants.BALANCE));

		JFormattedTextField ftfBalance = new JFormattedTextField();
		lblBalance.setLabelFor(ftfBalance);
		GroupLayout gl_balancePanel = new GroupLayout(balancePanel);
		gl_balancePanel.setHorizontalGroup(gl_balancePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_balancePanel.createSequentialGroup().addContainerGap()
						.addComponent(lblBalance, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(ftfBalance, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(293, Short.MAX_VALUE)));
		gl_balancePanel
				.setVerticalGroup(gl_balancePanel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
						gl_balancePanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_balancePanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(ftfBalance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblBalance))
								.addContainerGap(19, Short.MAX_VALUE)));
		balancePanel.setLayout(gl_balancePanel);

		JLabel lblTotalSalesNumber = new JLabel(String.format("%s:", Constants.TOTAL_NUMBER_OF_SALES));

		JFormattedTextField ftfTotalSalesNumber = new JFormattedTextField();
		lblTotalSalesNumber.setLabelFor(ftfTotalSalesNumber);

		JLabel lblTotalSalesValue = new JLabel(String.format("%s:", Constants.TOTAL_SALES_VALUE));
		lblTotalSalesValue.setHorizontalAlignment(SwingConstants.TRAILING);

		JFormattedTextField ftfTotalSalesValue = new JFormattedTextField();
		lblTotalSalesValue.setLabelFor(ftfTotalSalesValue);
		GroupLayout gl_recipesPanel = new GroupLayout(recipesPanel);
		gl_recipesPanel.setHorizontalGroup(gl_recipesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_recipesPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_recipesPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblTotalSalesValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(lblTotalSalesNumber))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_recipesPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(ftfTotalSalesValue).addComponent(ftfTotalSalesNumber,
										GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(222, Short.MAX_VALUE)));
		gl_recipesPanel.setVerticalGroup(gl_recipesPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_recipesPanel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_recipesPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblTotalSalesNumber)
						.addComponent(ftfTotalSalesNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_recipesPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblTotalSalesValue)
						.addComponent(ftfTotalSalesValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap(19, Short.MAX_VALUE)));
		recipesPanel.setLayout(gl_recipesPanel);

		JLabel lblTotalInputs = new JLabel(String.format("%s:", Constants.TOTAL_VALUE_OF_INPUTS));

		JFormattedTextField ftfTotalInputs = new JFormattedTextField();
		lblTotalInputs.setLabelFor(ftfTotalInputs);
		GroupLayout gl_expensesPanel = new GroupLayout(expensesPanel);
		gl_expensesPanel
				.setHorizontalGroup(gl_expensesPanel.createParallelGroup(Alignment.LEADING).addGroup(
						gl_expensesPanel.createSequentialGroup().addContainerGap().addComponent(lblTotalInputs)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(ftfTotalInputs,
										GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(221, Short.MAX_VALUE)));
		gl_expensesPanel.setVerticalGroup(gl_expensesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_expensesPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_expensesPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblTotalInputs)
								.addComponent(ftfTotalInputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap(21, Short.MAX_VALUE)));
		expensesPanel.setLayout(gl_expensesPanel);
		dataPanel.setLayout(gl_dataPanel);

		JLabel lblMonth = new JLabel(String.format("%s:", Constants.MONTH));

		JComboBox<Month> cbMonth = new JComboBox<Month>(Month.values());
		lblMonth.setLabelFor(cbMonth);
		GroupLayout gl_filterPanel = new GroupLayout(filterPanel);
		gl_filterPanel.setHorizontalGroup(gl_filterPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterPanel.createSequentialGroup().addContainerGap()
						.addComponent(lblMonth, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(cbMonth, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(362, Short.MAX_VALUE)));
		gl_filterPanel.setVerticalGroup(gl_filterPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_filterPanel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_filterPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblMonth).addComponent(
						cbMonth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(15, Short.MAX_VALUE)));

		filterPanel.setLayout(gl_filterPanel);
		getContentPane().setLayout(groupLayout);

		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void onCancel() {
		dispose();
	}

	private void onOk() {
	}
}
