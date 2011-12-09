package com.gemserk.tools.animationeditor.main;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

public class EditorApplication {
	
	static class EditorLibgdxApplicationListener extends Game {

		@Override
		public void create() {
			
		}
		
	}

	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();

		mainFrame.setSize(800, 600);
		mainFrame.setResizable(true);
		// mainFrame.setLayout(new GridLayout(2, 1));

		mainFrame.setJMenuBar(new JMenuBar() {
			{
				add(new JMenu("File") {
					{
						add(new JMenuItem("Exit") {
							{
								
							}
						});
					}
				});
			}
		});
		
		LwjglCanvas lwjglCanvas = new LwjglCanvas(new EditorLibgdxApplicationListener(), false);
		mainFrame.add(lwjglCanvas.getCanvas());

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}

		mainFrame.setVisible(true);
		mainFrame.validate();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
