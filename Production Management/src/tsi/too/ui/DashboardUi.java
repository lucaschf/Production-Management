package tsi.too.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import tsi.too.Constants;

@SuppressWarnings("serial")
public class DashboardUi extends JFrame {
	public DashboardUi() {
		setTitle(Constants.APP_NAME);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

		initComponents();
	}

	private void initComponents() {
		initMenuBar();

		setMinimumSize(new Dimension(900, 600));
		getContentPane().setLayout(null);
	}

	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnRegister = new JMenu(Constants.REGISTRATION);
		menuBar.add(mnRegister);

		JMenuItem mntmRegisterProduct = new JMenuItem(Constants.PRODUCT);
		mntmRegisterProduct.addActionListener(e -> new ProductRegistrationUi(this).setVisible(true));
		mnRegister.add(mntmRegisterProduct);

		JMenuItem mntmRegisterProduction = new JMenuItem(Constants.PRODUCTION);
		mntmRegisterProduction.addActionListener(e -> new ProductionRegistrationUi(this).setVisible(true));
		mnRegister.add(mntmRegisterProduction);

		JMenuItem mntmRegisterProductionInputs = new JMenuItem(Constants.PRODUCTION_INPUTS);
		mntmRegisterProductionInputs.addActionListener(e -> new InputsRegistrationUi(this).setVisible(true));
		mnRegister.add(mntmRegisterProductionInputs);

		JMenu mnOrder = new JMenu(Constants.ORDERS);
		menuBar.add(mnOrder);

		JMenuItem mntmPlaceOrder = new JMenuItem(Constants.NEW_ORDER);
		mntmPlaceOrder.addActionListener(e -> new PlaceOrderUi(this).setVisible(true));
		mnOrder.add(mntmPlaceOrder);

		JMenu mnReport = new JMenu(Constants.REPORTS);
		menuBar.add(mnReport);

		JMenuItem mntmProductionReport = new JMenuItem(Constants.PRODUCTION);
		mntmProductionReport.addActionListener(e -> new ProdutionReportUi(this).setVisible(true));
		mnReport.add(mntmProductionReport);

		JMenuItem mntmSalesReport = new JMenuItem(Constants.SALES);
		mntmSalesReport.addActionListener(e -> new SalesReportUi(this).setVisible(true));
		mnReport.add(mntmSalesReport);

		JMenu mnConsultations = new JMenu(Constants.CONSULTATIONS);
		menuBar.add(mnConsultations);

		JMenuItem mntmBudget = new JMenuItem(Constants.BUDGET);
		mntmBudget.addActionListener(e -> new BudgetUi(this).setVisible(true));
		mnConsultations.add(mntmBudget);

		JMenuItem mntmProducts = new JMenuItem(Constants.PRODUCT);
		mntmProducts.addActionListener(e -> new ProductListUI(this).setVisible(true));
		mnConsultations.add(mntmProducts);
		
		JMenuItem mntmConsultationInputs= new JMenuItem(Constants.PRODUCTION_INPUTS);
		mntmConsultationInputs.addActionListener(e -> new InputsListUI(this).setVisible(true));
		mnConsultations.add(mntmConsultationInputs);

		JMenu mnSettings = new JMenu(Constants.SETTINGS);
		menuBar.add(mnSettings);

		JMenuItem mntmAppearance = new JMenuItem(Constants.APPEARANCE);
		mntmAppearance.addActionListener(e -> changeLookAndFeel());
		mnSettings.add(mntmAppearance);
	}

	public void changeLookAndFeel() {
		List<String> lookAndFeelsDisplay = new ArrayList<>();
		List<String> lookAndFeelsRealNames = new ArrayList<>();

		for (LookAndFeelInfo each : UIManager.getInstalledLookAndFeels()) {
			lookAndFeelsDisplay.add(each.getName());
			lookAndFeelsRealNames.add(each.getClassName());
		}

		String changeLook = (String) JOptionPane.showInputDialog(this, Constants.SELECT_APPEARANCE,
				Constants.APPEARANCE, JOptionPane.PLAIN_MESSAGE, null, lookAndFeelsDisplay.toArray(), null);

		if (changeLook != null) {
			for (int i = 0; i < lookAndFeelsDisplay.size(); i++) {
				if (changeLook.equals(lookAndFeelsDisplay.get(i))) {
					try {
						UIManager.setLookAndFeel(lookAndFeelsRealNames.get(i));
						break;
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException ex) {
						ex.printStackTrace(System.err);
					}
				}
			}
		}
	}
}
