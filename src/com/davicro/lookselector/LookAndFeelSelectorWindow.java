package com.davicro.lookselector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class LookAndFeelSelectorWindow extends JDialog implements ActionListener, WindowListener{
		
	private JLabel label;
	
	private JPanel centerPanel;
	private JComboBox<ComboBoxEntry<String>> comboBox;
	
	private JPanel buttonsPanel;
	private JButton okButton;
	
	private static final String ACTION_OK = "action_close";
	private static final String ACTION_ITEM_SELECT = "action_item_select";
	
	private Runnable runAfterClose;
	private boolean proceed = false;
	
	public LookAndFeelSelectorWindow() {
		this(null);
	}
	
	public LookAndFeelSelectorWindow(Frame owner) {
		super(owner, "Select look n' feel", true);
		
		setSize(300, 150);
		setLocationRelativeTo(owner);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		addWindowListener(this);
		
		createInterface();
		updateComboBoxOptions();
	}
	
	/**
	 * Create and add every component of the interface
	 */
	private void createInterface() {
		//this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setLayout(new BorderLayout());
		
		//Label
//		label = new JLabel("Select Look n' Feel");
//		label.setHorizontalAlignment(JLabel.CENTER);
//		label.setAlignmentX(CENTER_ALIGNMENT);
//		add(label, BorderLayout.NORTH);
		//Center
		centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		add(centerPanel, BorderLayout.CENTER);
		
		//centerPanel.add(Box.createVerticalGlue());
		
		//Combo box
		comboBox = new JComboBox<>();
		comboBox.setMaximumSize(new Dimension(500, 25));
		comboBox.setAlignmentX(CENTER_ALIGNMENT);
		comboBox.setAlignmentY(0.5f);
		comboBox.setActionCommand(ACTION_ITEM_SELECT);
		comboBox.addActionListener(this);
		centerPanel.add(comboBox);
		
		//centerPanel.add(Box.createVerticalGlue());
		
		//Buttons
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		add(buttonsPanel, BorderLayout.SOUTH);
		
		//OK Button
		okButton = new JButton("OK");
		okButton.setActionCommand(ACTION_OK);
		okButton.addActionListener(this);
		buttonsPanel.add(okButton, BorderLayout.SOUTH);
	}
	
	/**
	 * Updates the combo box options to display all installed look and feels
	 */
	private void updateComboBoxOptions() {
		comboBox.removeAllItems();
		
		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo info : infos) {
			comboBox.addItem(new ComboBoxEntry<String>(info.getName(), info.getClassName()));
		}
	}

	private void setLookAndFeel(String className) {
		try {
			UIManager.setLookAndFeel(className);
			SwingUtilities.updateComponentTreeUI(this);
			if(this.getParent() != null) SwingUtilities.updateComponentTreeUI(this.getParent());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case ACTION_OK:
			proceed = true;
			this.dispose();
			break;
		case ACTION_ITEM_SELECT:
			JComboBox<ComboBoxEntry<String>> comboBox = (JComboBox<ComboBoxEntry<String>>)e.getSource();
			ComboBoxEntry<String> selected = comboBox.getItemAt(comboBox.getSelectedIndex());
			setLookAndFeel(selected.getObj());
			break;
		default:
			System.out.println("No action defined for " + e.getActionCommand());
			break;
		}
	}
	
	public void showThenRun(Runnable runnable) {
		this.runAfterClose = runnable;
		setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		if(proceed && runAfterClose != null) runAfterClose.run();
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
