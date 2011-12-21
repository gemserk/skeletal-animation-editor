package com.gemserk.tools.animationeditor.main;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class EditorApplication {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception ex) {

					}
					EditorWindow window = new EditorWindow();
					window.getMainFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
