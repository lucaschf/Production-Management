package tsi.too.ui.helper;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import tsi.too.model.Product;

@SuppressWarnings("serial")
public class ProductComboboxRenderer extends BasicComboBoxRenderer {
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value != null) {
			Product product = (Product) value;
			setText(String.format("%s - %1.2f %s", product.getName(), product.getSize(),
					product.getMeasureUnity().getInitials()));
		}
		
		return this;
	}
}