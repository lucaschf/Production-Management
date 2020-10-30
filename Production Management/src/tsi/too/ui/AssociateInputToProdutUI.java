package tsi.too.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import tsi.too.Constants;
import tsi.too.controller.InputsByProductController;
import tsi.too.controller.ProductController;
import tsi.too.io.InputDialog;
import tsi.too.io.MessageDialog;
import tsi.too.model.Input;
import tsi.too.model.Product;
import tsi.too.model.ProductInputRelation;
import tsi.too.ui.helper.TableMouseListener;
import tsi.too.ui.table_model.ProductionInputTableModel;

@SuppressWarnings("serial")
public class AssociateInputToProdutUI extends JFrame {
	private static final String VINCULATION_INFO_MESSAGE = "<html>Ao vincular um insumo, insira a quantidade desejada atraves da coluna quantidade na tabela de insumos desvinculados. Insumos cuja quantidade esteja com 0, ser�o adicionados com a quantide 1.<br/> \u00C9 possivel realizar a altera\u00E7\u00E3o da quantidade, atrav\u00E9s do menu de contexto da tabela de insumos vinculados.</html>";
	private JTable tbUnlinked;
	private JTable tbLinked;
	private JButton btnUnlink;
	private JButton btnLink;
	private JButton btnNewInput;
	private JScrollPane scroUnlinked;
	private JScrollPane scrollLinked;

	private Component parentComponent;

	private final LinkingInputTableModel unlinkedTableModel = new LinkingInputTableModel();
	private final ProductionInputTableModel linkedTableModel = new ProductionInputTableModel();

	private ProductController productController;
	private InputsByProductController inputsByProductController;
	private JComboBox<Product> cbProduct;
	private JLabel lblNewLabel;

	/**
	 * @wbp.parser.constructor
	 */
	public AssociateInputToProdutUI(Component parentComponent) {
		this(parentComponent, null);
	}

	public AssociateInputToProdutUI(Component parentComponent, Product product) {
		this.parentComponent = parentComponent;
		initControllers();

		initComponent();
		setupWindow();

		if (product != null)
			cbProduct.setSelectedItem(product);
	}

	private void initComponent() {
		JPanel productPanel = new JPanel();
		productPanel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				Constants.PRODUCT_DATA, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		JPanel inputsPanel = new JPanel();
		inputsPanel.setBorder(new TitledBorder(null, Constants.PRODUCTION_INPUTS, TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(inputsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(productPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(productPanel, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(inputsPanel, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
					.addContainerGap())
		);

		JPanel unlinkedPanel = new JPanel();
		unlinkedPanel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				Constants.UNLINKED, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		btnLink = new JButton(Constants.LINK);
		btnLink.addActionListener(e -> link());

		btnNewInput = new JButton(Constants.NEW);
		btnNewInput.addActionListener(e -> newInput());

		JPanel linkedPanel = new JPanel();
		linkedPanel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				Constants.LINKED, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		btnUnlink = new JButton(Constants.UNLINK);
		btnUnlink.addActionListener(e -> unlink());

		lblNewLabel = new JLabel(
				VINCULATION_INFO_MESSAGE);
		lblNewLabel.setAutoscrolls(true);
		lblNewLabel.setIcon(new ImageIcon(AssociateInputToProdutUI.class.getResource("/resources/ic_info.png")));
		lblNewLabel.setIconTextGap(10);

		GroupLayout gl_inputsPanel = new GroupLayout(inputsPanel);
		gl_inputsPanel.setHorizontalGroup(
			gl_inputsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_inputsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 834, Short.MAX_VALUE)
						.addGroup(gl_inputsPanel.createSequentialGroup()
							.addComponent(unlinkedPanel, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnNewInput, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
								.addComponent(btnLink, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
								.addComponent(btnUnlink, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(linkedPanel, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_inputsPanel.setVerticalGroup(
			gl_inputsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_inputsPanel.createSequentialGroup()
					.addGroup(gl_inputsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(linkedPanel, 0, 0, Short.MAX_VALUE)
						.addGroup(gl_inputsPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnLink)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewInput)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnUnlink))
						.addComponent(unlinkedPanel, GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		scrollLinked = new JScrollPane();
		GroupLayout gl_linkedPanel = new GroupLayout(linkedPanel);
		gl_linkedPanel.setHorizontalGroup(gl_linkedPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_linkedPanel.createSequentialGroup().addContainerGap()
						.addComponent(scrollLinked, GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE).addContainerGap()));
		gl_linkedPanel.setVerticalGroup(gl_linkedPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_linkedPanel.createSequentialGroup().addContainerGap()
						.addComponent(scrollLinked, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addContainerGap()));

		initLinkedTable();

		linkedPanel.setLayout(gl_linkedPanel);
		linkedPanel.setLayout(gl_linkedPanel);

		scroUnlinked = new JScrollPane();
		GroupLayout gl_unlinkedPanel = new GroupLayout(unlinkedPanel);
		gl_unlinkedPanel.setHorizontalGroup(gl_unlinkedPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_unlinkedPanel.createSequentialGroup().addContainerGap()
						.addComponent(scroUnlinked, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE).addContainerGap()));
		gl_unlinkedPanel.setVerticalGroup(gl_unlinkedPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_unlinkedPanel.createSequentialGroup().addContainerGap()
						.addComponent(scroUnlinked, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE).addContainerGap()));

		initUnlinkedTable();
		unlinkedPanel.setLayout(gl_unlinkedPanel);
		inputsPanel.setLayout(gl_inputsPanel);

		JLabel lblProduct = new JLabel(String.format("%s:", Constants.PRODUCT));
		lblProduct.setHorizontalAlignment(SwingConstants.TRAILING);

		initProductsCombobox();

		GroupLayout gl_productPanel = new GroupLayout(productPanel);
		gl_productPanel.setHorizontalGroup(gl_productPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_productPanel.createSequentialGroup().addContainerGap().addComponent(lblProduct)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(cbProduct, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(413, Short.MAX_VALUE)));
		gl_productPanel.setVerticalGroup(gl_productPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_productPanel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_productPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblProduct).addComponent(
						cbProduct, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(46, Short.MAX_VALUE)));
		productPanel.setLayout(gl_productPanel);
		getContentPane().setLayout(groupLayout);
	}

	private void initProductsCombobox() {
		try {
			cbProduct = new JComboBox<>(productController.fetchProductsAsVector());
			cbProduct.addItemListener(e -> {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					loadInputs(((Product) e.getItem()).getId());
				}
			});

			loadInputs(getProductSelected().getId());
		} catch (IOException e) {
			MessageDialog.showErrorDialog(this, getTitle(), Constants.FAILED_TO_FETCH_DATA);
			// TODO lock form
		}
	}

	/**
	 * Performs a loop in the selected inputs unlinking from the current selected
	 * product.
	 */
	private void unlink() {
		stopEditing(tbLinked);

		var product = getProductSelected();

		if (product == null)
			return;

		var selected = tbLinked.getSelectedRows();

		if (selected.length == 0)
			return;

		try {
			for (int i : selected) {
				inputsByProductController.unLink(product.getId(), linkedTableModel.getValueAt(i).getId());
			}

			MessageDialog.showInformationDialog(this, getTitle(), Constants.UNLINKING_SUCCESS);
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.UNLINKING_FAILURE);
		} finally {
			loadInputs(product.getId());
		}
	}

	private void newInput() {
		new InputsRegistrationUi(this).setVisible(true);
		loadInputs(getProductSelected().getId());
	}

	/**
	 * Performs a loop in the selected inputs unlinking from the current selected
	 * product.
	 */
	private void link() {
		stopEditing(tbUnlinked);

		var product = getProductSelected();

		if (product == null)
			return;

		var selected = tbUnlinked.getSelectedRows();

		if (selected.length == 0)
			return;

		try {
			for (int i : selected) {
				var target = unlinkedTableModel.getValueAt(i);
				inputsByProductController.link(new ProductInputRelation(product.getId(), target.getId(),
						target.getQuantity() > 0 ? target.getQuantity() : 1));
			}

			MessageDialog.showInformationDialog(this, getTitle(), Constants.LINKING_SUCCESS);
		} catch (IOException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.LINKING_FAILURE);
		} finally {
			loadInputs(product.getId());
		}
	}

	private void stopEditing(JTable table) {
		TableCellEditor editor = table.getCellEditor();

		if (editor != null) {
			editor.stopCellEditing();
		}
	}

	private Product getProductSelected() {
		return (Product) cbProduct.getSelectedItem();
	}

	private void initLinkedTable() {
		tbLinked = new JTable();
		tbLinked.setModel(linkedTableModel);
		tbLinked.getColumnModel().getColumn(0).setPreferredWidth(200);
		tbLinked.removeColumn(tbLinked.getColumnModel().getColumn(3));
		tbLinked.removeColumn(tbLinked.getColumnModel().getColumn(0));
		tbLinked.setColumnSelectionAllowed(true);
		tbLinked.setFillsViewportHeight(true);
		tbLinked.addMouseListener(new TableMouseListener(tbLinked));
		scrollLinked.setViewportView(tbLinked);

		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tbLinked.getTableHeader().getDefaultRenderer();
		renderer.setHorizontalAlignment(SwingConstants.LEFT);

		setupLinkedPopupMenu();
	}

	private void setupLinkedPopupMenu() {
		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem editItemQuantity = new JMenuItem(Constants.EDIT_QUANTITY);
		editItemQuantity
				.addActionListener(e -> editItemQuantity(linkedTableModel.getValueAt(tbLinked.getSelectedRow())));
		popupMenu.add(editItemQuantity);

		JMenuItem removeItem = new JMenuItem(Constants.UNLINK);
		removeItem.addActionListener(e -> unlink());
		popupMenu.add(removeItem);

		tbLinked.setComponentPopupMenu(popupMenu);
	}

	private void editItemQuantity(Input target) {
		var quantity = InputDialog.showIntegerInputDialog(target.getName(), Constants.QUANTITY,
				input -> input <= 0 ? Constants.QUANTITY_MUST_BE_GREATER_THAN_ZERO : null);
		var product = getProductSelected();

		if (quantity != null && quantity != target.getQuantity()) {
			target.setQuantity(quantity);

			try {
				var pos = inputsByProductController.fetchByPordutAndInput(product.getId(), target.getId());

				inputsByProductController.update(pos.getSecond(),
						new ProductInputRelation(product.getId(), target.getId(), target.getQuantity()));

				MessageDialog.showInformationDialog(this, getTitle(), Constants.QUANTITY_SUCCESSFUL_CHANGED);
			} catch (IOException e) {
				MessageDialog.showInformationDialog(this, getTitle(), Constants.QUANTITY_CHANGE_FAILURE);
			} finally {
				loadInputs(product.getId());
			}
		}

	}

	private void initUnlinkedTable() {
		tbUnlinked = new JTable();
		tbUnlinked.setColumnSelectionAllowed(true);
		tbUnlinked.setModel(unlinkedTableModel);
		tbUnlinked.removeColumn(tbUnlinked.getColumnModel().getColumn(3));
		tbUnlinked.removeColumn(tbUnlinked.getColumnModel().getColumn(0));

		tbUnlinked.setFillsViewportHeight(true);
		scroUnlinked.setViewportView(tbUnlinked);

		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tbUnlinked.getTableHeader().getDefaultRenderer();
		renderer.setHorizontalAlignment(SwingConstants.LEFT);
	}

	private void setupWindow() {
		setTitle(Constants.PRODUCTION_INPUTS);
		pack();
		setLocationRelativeTo(parentComponent);
	}

	private void initControllers() {
		try {
			productController = ProductController.getInstance();
			inputsByProductController = InputsByProductController.getInstance();
		} catch (FileNotFoundException e) {
			MessageDialog.showAlertDialog(this, getTitle(), Constants.UNABLE_TO_OPEN_FILE);
		}
	}

	/**
	 * Loads booth linked and unlinked inputs for a give product into their
	 * respective tables.
	 * 
	 * @param productId the target product id.
	 */
	private void loadInputs(long productId) {
		loadLinkedInputs(productId);
		loadUnlinkedInputs(productId);
	}

	/**
	 * Fetch all linked Inputs for a given product and stores it into
	 * {@code #linkedTableModel}.
	 * 
	 * @param productId the target product id.
	 */
	private void loadLinkedInputs(long productId) {
		try {
			linkedTableModel.clear();
			var s = inputsByProductController.fetchLinkedInputs(productId);
			linkedTableModel.addRows(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fetch all unlinked Inputs for a given product and stores it into
	 * {@code #unlinkedTableModel}.
	 * 
	 * @param productId the target product id.
	 */
	private void loadUnlinkedInputs(long productId) {
		try {
			unlinkedTableModel.clear();
			var s = inputsByProductController.fetchUnlinkedInputs(productId);
			unlinkedTableModel.addRows(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class LinkingInputTableModel extends ProductionInputTableModel {
		@Override
		public boolean isCellEditable(int row, int column) {
			return getRowCount() > 0 && getColumnName(column).equals(Constants.QUANTITY);
		}
	}
}
