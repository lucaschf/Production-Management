package tsi.too.ui;

import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings("serial")
public class BottomActionPanel extends JPanel {
    private final JButton btnPositive;

    public BottomActionPanel(String negativeText, ActionListener negativeAction, String positiveText,
                             ActionListener positiveAction) {
        JButton btnNegative = new JButton(negativeText);
        btnNegative.addActionListener(negativeAction);

        btnPositive = new JButton(positiveText);
        try {
            btnPositive.setMnemonic(positiveText.charAt(0));
        } catch (Exception ignored) {
        }

        btnPositive.addActionListener(positiveAction);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap(256, Short.MAX_VALUE)
        			.addComponent(btnPositive)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btnNegative)
        			.addGap(1))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(1)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnNegative)
        				.addComponent(btnPositive))
        			.addGap(3))
        );
        setLayout(groupLayout);
    }

    public void setPositiveButtonMnemonic(char c) {
        btnPositive.setMnemonic(c);
    }

    public void setPositiveText(String text) {
        btnPositive.setText(text);
        setPositiveButtonMnemonic(text.charAt(0));
    }

	public void setPositiveButtonEnabled(boolean enabled) {
		btnPositive.setEnabled(enabled);
	}
}