package com.davicro.gui;

import javax.swing.UIManager;

public class LookAndFeelUtils {
	public static void setLookAndFeel(String name) {
		try {
	        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	            if (name.equals(info.getName())) {
	                UIManager.setLookAndFeel(info.getClassName());
	                break;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
