package com.davicro.gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabelTextField extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JTextField textField;

	public LabelTextField(String labelText, int textFieldWidth) {
		this.setLayout(new GridLayout(1,2));
		
		label = new JLabel(labelText);
		this.add(label);
		
		textField = new JTextField(15);
		this.add(textField);
	}
	
	/**
	 * Get the text field's content
	 * @return
	 */
	public String getText() {
		return textField.getText();
	}
	
	/**
	 * Set the text field's content
	 * @param text
	 */
	public void setText(String text) {
		textField.setText(text);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		textField.setEnabled(enabled);
	}
}
