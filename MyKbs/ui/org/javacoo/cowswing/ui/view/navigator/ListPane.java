package org.javacoo.cowswing.ui.view.navigator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.javacoo.cowswing.ui.style.GuiUtils;
import org.javacoo.cowswing.ui.widget.LinkLabel;

/**
 * 列表panel
 * 
 * @author DuanYong
 * @since 2012-12-13上午9:38:24
 * @version 1.0
 */
public class ListPane extends JPanel {
	private static final long serialVersionUID = 6807890033041814417L;

	private static final int HORZ_PAD = 0;
	private static final int VERT_PAD = -10;

	/**
	 * Creates new form ListPane
	 */
	public ListPane() {
		initComponents();
		Border b = BorderFactory.createEmptyBorder(VERT_PAD, HORZ_PAD,
				VERT_PAD, HORZ_PAD);
		setBorder(b);
	}

	private void initComponents() {
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 5, 10);
		setLayout(layout);
	}

	public void addItem(String text, ImageIcon icon) {
		addItem(text, icon, null);
	}

	public void addItem(Component component) {
		add(component);
	}

	public void addItem(String text, ImageIcon icon, final Action action) {
		LinkLabel lblItem = new LinkLabel();
		lblItem.setPreferredSize(new Dimension(180,15));
		lblItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (action != null) {
					action.actionPerformed(event);
				} else {
					System.out.println(((LinkLabel) event.getSource())
							.getText());
				}
			}
		});
		if (icon != null) {
			lblItem.setIcon(icon);
			lblItem.setForeground(GuiUtils
					.getLookAndFeelColor("foregroundColor"));
		} else
			lblItem.setForeground(GuiUtils
					.getLookAndFeelColor("foregroundColor"));
		lblItem.setText(text);
		add(lblItem);
	}
	public void addItem(String text, ImageIcon icon, final ActionListener actionListener) {
		LinkLabel lblItem = new LinkLabel();
		lblItem.setPreferredSize(new Dimension(180,15));
		lblItem.addActionListener(actionListener);
		if (icon != null) {
			lblItem.setIcon(icon);
			lblItem.setForeground(GuiUtils
					.getLookAndFeelColor("foregroundColor"));
		} else
			lblItem.setForeground(GuiUtils
					.getLookAndFeelColor("foregroundColor"));
		lblItem.setText(text);
		add(lblItem);
	}

	public void addButtonItem(String text, ImageIcon icon) {
		JButton btnItem = new JButton();
		// btnItem.setBorderPainted(false);
		btnItem.setBorder(BorderFactory.createCompoundBorder());
		btnItem.setBackground(GuiUtils
				.getLookAndFeelColor("backgroundFillColor"));
		if (icon != null) {
			btnItem.setIcon(icon);
		}
		btnItem.setForeground(GuiUtils.getLookAndFeelColor("foregroundColor"));
		btnItem.setText(text);
		add(btnItem);
	}

	public void addListItem() {
		String[] data = { "one", "two", "three", "four" };
		JList lstItem = new JList(data);
		add(lstItem);
	}
}
