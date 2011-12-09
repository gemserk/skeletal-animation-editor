package com.gemserk.tools.animationeditor.main;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.badlogic.gdx.graphics.GL10;

public class EditorApplication {
	
	protected static final Logger logger = LoggerFactory.getLogger(EditorApplication.class);
	
	public static class Messages {
		
		public static final String DialogExitMessage = "dialogs.exit.message";
		public static final String DialogExitTitle = "dialogs.exit.title";
		
	}

	static class EditorLibgdxApplicationListener extends Game {

		@Override
		public void create() {
			Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);
		}

		@Override
		public void render() {
			Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		}

	}

	static class EditorMainFrame extends JFrame {

		private EditorLibgdxApplicationListener editorApplication;
		
		private ResourceBundle messages;
		
		public EditorMainFrame() {
			
			messages = ResourceBundle.getBundle("messages");
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex) {

			}

			setResizable(true);

			editorApplication = new EditorLibgdxApplicationListener();

			JMenuBar menuBar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");

			JMenuItem exitMenuItem = new JMenuItem("Exit");
			
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					int showConfirmDialog = JOptionPane.showConfirmDialog(EditorMainFrame.this, // 
							messages.getString(Messages.DialogExitMessage), //
							messages.getString(Messages.DialogExitTitle), // 
							JOptionPane.YES_NO_OPTION);
					
					if (showConfirmDialog == JOptionPane.NO_OPTION)
						return;
					
					logger.info("Exit action, closing application");
					
					Gdx.app.exit();					
				}
			});

			fileMenu.add(exitMenuItem);

			menuBar.add(fileMenu);
			
			setJMenuBar(menuBar);

			LwjglCanvas lwjglCanvas = new LwjglCanvas(editorApplication, false);
			
			Canvas canvas = lwjglCanvas.getCanvas();
			
			add(canvas);

			setVisible(true);
			validate();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					Gdx.app.exit();
				}
			});
		}

	}

	public static void main(String[] args) {
		JFrame mainFrame = new EditorMainFrame();
		mainFrame.setSize(800, 600);
	}

}
