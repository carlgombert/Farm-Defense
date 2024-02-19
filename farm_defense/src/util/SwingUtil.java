package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.KeyInput;

public class SwingUtil {
	
	public static JPanel createPanel(JPanel panel, LayoutManager layout, int x, int y, int width, int height, Color backgroundColor) {
		panel = new JPanel(layout);
		panel.setBounds(x, y, width, height);
		panel.setBackground(backgroundColor);
		return panel;
	}
	
	public static JLabel createLabel(JLabel label, String text, int x, int y, int width, int height, Color color, Font font) {
		label = new JLabel(text);
		label.setBounds(x, y, width, height);
		label.setForeground(color);
		label.setFont(font);
		return label;
	}
	
	public static JButton createButton(JButton button, int x, int y, int width, int height, String text, boolean clear, Color backgroundColor, Color textColor, Font font) {
		button = new JButton(text);
		button.setFont(font);
		button.addActionListener(new KeyInput());
		button.setBounds(x, y, width, height);
		button.setBackground(backgroundColor);
		button.setForeground(textColor);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setOpaque(clear);
		return button;
	}
}
