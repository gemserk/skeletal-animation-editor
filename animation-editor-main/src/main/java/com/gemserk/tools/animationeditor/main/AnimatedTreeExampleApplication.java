package com.gemserk.tools.animationeditor.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class AnimatedTreeExampleApplication extends JFrame {

	protected static final Logger logger = LoggerFactory.getLogger(AnimatedTreeExampleApplication.class);

	private static final long serialVersionUID = 1269100988236728215L;

	public AnimatedTreeExampleApplication() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {

		}

		setResizable(true);

		final Canvas canvas = new Canvas() {
			
			private LwjglApplication lwjglApplication;

			public final void addNotify() {
				super.addNotify();
				lwjglApplication = new LwjglApplication(new AnimatedTreeExampleApplicationListener(), false, this);
			}
			
			public final void removeNotify() {
				lwjglApplication.stop();
				super.removeNotify();
			}
			
		};
		
		canvas.setSize(800, 600);
		canvas.setIgnoreRepaint(true);
		
		setLayout(new BorderLayout());
		
		add(canvas, BorderLayout.CENTER);
		
		validate();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Gdx.app.exit();
			}
		});
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnimatedTreeExampleApplication app = new AnimatedTreeExampleApplication();
					app.setBounds(100, 100, 800, 600);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}