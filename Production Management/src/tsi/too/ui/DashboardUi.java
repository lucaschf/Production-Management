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
		menuBar.add(createEntryMenu());
		menuBar.add(createReportMenu());
		menuBar.add(createSettingsMenu());
	}

	private JMenu createEntryMenu() {
		JMenu mnEntry = new JMenu(Constants.ENTRY);

		JMenuItem itemInputEntry = new JMenuItem(Constants.INPUT_ENTRIES);
		itemInputEntry.addActionListener(e -> new InputEntryUi(this).setVisible(true));
		mnEntry.add(itemInputEntry);

		JMenuItem itemRegisterProduction = new JMenuItem(Constants.PRODUCTION);
		itemRegisterProduction.addActionListener(e -> new ProductionEntryUi(this).setVisible(true));
		mnEntry.add(itemRegisterProduction);

		JMenuItem itemPlaceOrder = new JMenuItem(Constants.ORDER);
		itemPlaceOrder.addActionListener(e -> new PlaceOrderUi(this).setVisible(true));
		mnEntry.add(itemPlaceOrder);

		return mnEntry;
	}

	private JMenu createReportMenu() {
		JMenu mnReport = new JMenu(Constants.REPORTS);

		JMenuItem itemProductionReport = new JMenuItem(Constants.PRODUCTION);
		itemProductionReport.addActionListener(e -> new ProductionReportUi(this).setVisible(true));
		mnReport.add(itemProductionReport);

		JMenuItem itemSalesReport = new JMenuItem(Constants.SALES);
		itemSalesReport.addActionListener(e -> new SalesReportUi(this).setVisible(true));
		mnReport.add(itemSalesReport);

		JMenuItem itemConsultationInputsPrices = new JMenuItem(Constants.INPUT_ENTRIES);
		itemConsultationInputsPrices.addActionListener(e -> new InputsEntryListUi(this).setVisible(true));
		mnReport.add(itemConsultationInputsPrices);

		return mnReport;
	}

	private JMenu createRegistrationMenu() {
		JMenu mnRegister = new JMenu(Constants.REGISTRATION);

		JMenuItem itemRegisterProductionInputs = new JMenuItem(Constants.INPUT);
		itemRegisterProductionInputs.addActionListener(e -> new InputsRegistrationUi(this).setVisible(true));
		mnRegister.add(itemRegisterProductionInputs);

		JMenuItem itemRegisterProduct = new JMenuItem(Constants.PRODUCT);
		itemRegisterProduct.addActionListener(e -> new ProductRegistrationUi(this).setVisible(true));
		mnRegister.add(itemRegisterProduct);

		JMenuItem itemAssociateProductInputs = new JMenuItem(Constants.PRODUCT_INPUT_LINK);
		itemAssociateProductInputs.addActionListener(e -> new ProductInputsUI(this).setVisible(true));
		mnRegister.add(itemAssociateProductInputs);

		return mnRegister;
	}

	private JMenu createSettingsMenu() {
		JMenu mnSettings = new JMenu(Constants.SETTINGS);

		JMenuItem itemAppearance = new JMenuItem(Constants.APPEARANCE);
		itemAppearance.addActionListener(e -> changeLookAndFeel());
		mnSettings.add(itemAppearance);

		return mnSettings;
	}

	private JMenu createConsultationMenu() {
		JMenu mnConsultations = new JMenu(Constants.CONSULTATIONS);

		JMenuItem itemBudget = new JMenuItem(Constants.BUDGET);
		itemBudget.addActionListener(e -> new BudgetUi(this).setVisible(true));
		mnConsultations.add(itemBudget);

		JMenuItem itemProducts = new JMenuItem(Constants.PRODUCT);
		itemProducts.addActionListener(e -> new ProductListUI(this).setVisible(true));
		mnConsultations.add(itemProducts);

		JMenuItem itemConsultationInputs = new JMenuItem(Constants.PRODUCTION_INPUTS);
		itemConsultationInputs.addActionListener(e -> new InputsListUI(this).setVisible(true));
		mnConsultations.add(itemConsultationInputs);

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
