package com.davicro.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.BorderLayout;
import java.awt.Color;

public class MessageBox extends JPanel {
	public static final String DEFAULT_STYLE = "default";
	
	private static final long serialVersionUID = 1L;
	private JTextPane textArea;
	
	public MessageBox() {
		setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextPane();
		textArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);
		
		addStyle(DEFAULT_STYLE, Color.BLACK);
	}
	
	public void appendMessage(String message) {
		appendMessage(message, DEFAULT_STYLE);
	}
	
	/**
	 * Add a message at the end with the specified style (previously defined with <code>addStyle</code>)
	 * @param message
	 * @param style
	 */
	public void appendMessage(String message, String style) {
		StyledDocument doc = textArea.getStyledDocument();
		
		try {
			doc.insertString(doc.getLength(), message + "\n", textArea.getStyle(style));
			textArea.setCaretPosition(doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Assign an identifier to a color, to be later used in messages
	 * @param name
	 * @param color
	 */
	public void addStyle(String name, Color color) {
		Style style = textArea.addStyle(name, null);
		StyleConstants.setForeground(style, color);
	}
	
	public void clear() {
		textArea.setText("");
	}
}
