package com.davicro.lookselector.main;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.davicro.lookselector.LookAndFeelSelectorWindow;

public class ThenRunMain {

	public static void main(String[] args) {
		LookAndFeelSelectorWindow lookSelectorWindow = new LookAndFeelSelectorWindow();
		lookSelectorWindow.showThenRun(() -> {
			JFrame frame = new JFrame();
			frame.setSize(500,500);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
		
	}

}
