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

		menuBar.add(createRegistrationMenu());
		menuBar.add(createConsultationMenu());
		menuBar.add(createEntryMeny());
		menuBar.add(createReportMenu());
		menuBar.add(createSettingsMenu());
	}

	private JMenu createEntryMeny() {
		JMenu mnEntry = new JMenu(Constants.ENTRY);

		JMenuItem mntmInputEntry = new JMenuItem(Constants.INPUT_ENTRIES);
		mntmInputEntry.addActionListener(e -> new InputEntryUi(this).setVisible(true));
		mnEntry.add(mntmInputEntry);

		JMenuItem mntmPlaceOrder = new JMenuItem(Constants.ORDER);
		mntmPlaceOrder.addActionListener(e -> new PlaceOrderUi(this).setVisible(true));
		mnEntry.add(mntmPlaceOrder);

		return mnEntry;
	}

	private JMenu createReportMenu() {
		JMenu mnReport = new JMenu(Constants.REPORTS);

		JMenuItem mntmProductionReport = new JMenuItem(Constants.PRODUCTION);
		mntmProductionReport.addActionListener(e -> new ProdutionReportUi(this).setVisible(true));
		mnReport.add(mntmProductionReport);

		JMenuItem mntmSalesReport = new JMenuItem(Constants.SALES);
		mntmSalesReport.addActionListener(e -> new SalesReportUi(this).setVisible(true));
		mnReport.add(mntmSalesReport);

		JMenuItem mntmConsultationInputsPrices = new JMenuItem(Constants.INPUT_ENTRIES);
		mntmConsultationInputsPrices.addActionListener(e -> new InputsEntryListUi(this).setVisible(true));
		mnReport.add(mntmConsultationInputsPrices);

		return mnReport;
	}

	private JMenu createRegistrationMenu() {
		JMenu mnRegister = new JMenu(Constants.REGISTRATION);

		JMenuItem mntmRegisterProductionInputs = new JMenuItem(Constants.INPUT);
		mntmRegisterProductionInputs.addActionListener(e -> new InputsRegistrationUi(this).setVisible(true));
		mnRegister.add(mntmRegisterProductionInputs);
		
		JMenuItem mntmRegisterProduct = new JMenuItem(Constants.PRODUCT);
		mntmRegisterProduct.addActionListener(e -> new ProductRegistrationUi(this).setVisible(true));
		mnRegister.add(mntmRegisterProduct);

		JMenuItem mntmRegisterProduction = new JMenuItem(Constants.PRODUCTION);
		mntmRegisterProduction.addActionListener(e -> new ProductionEntryUi(this).setVisible(true));
		mnRegister.add(mntmRegisterProduction);

		JMenuItem mntmAssociateProductInputs = new JMenuItem(Constants.PRODUCT_INPUT_LINK);
		mntmAssociateProductInputs.addActionListener(e -> new AssociateInputToProductUI(this).setVisible(true));
		mnRegister.add(mntmAssociateProductInputs);

		return mnRegister;
	}

	private JMenu createSettingsMenu() {
		JMenu mnSettings = new JMenu(Constants.SETTINGS);

		JMenuItem mntmAppearance = new JMenuItem(Constants.APPEARANCE);
		mntmAppearance.addActionListener(e -> changeLookAndFeel());
		mnSettings.add(mntmAppearance);

		return mnSettings;
	}

	private JMenu createConsultationMenu() {
		JMenu mnConsultations = new JMenu(Constants.CONSULTATIONS);

		JMenuItem mntmBudget = new JMenuItem(Constants.BUDGET);
		mntmBudget.addActionListener(e -> new BudgetUi(this).setVisible(true));
		mnConsultations.add(mntmBudget);

		JMenuItem mntmProducts = new JMenuItem(Constants.PRODUCT);
		mntmProducts.addActionListener(e -> new ProductListUI(this).setVisible(true));
		mnConsultations.add(mntmProducts);

		JMenuItem mntmConsultationInputs = new JMenuItem(Constants.PRODUCTION_INPUTS);
		mntmConsultationInputs.addActionListener(e -> new InputsListUI(this).setVisible(true));
		mnConsultations.add(mntmConsultationInputs);

		return mnConsultations;
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
						repaint();
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
