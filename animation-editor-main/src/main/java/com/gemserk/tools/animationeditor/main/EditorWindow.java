package com.gemserk.tools.animationeditor.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class EditorWindow {

	private JFrame frmGemserksAnimationEditor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditorWindow window = new EditorWindow();
					window.frmGemserksAnimationEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EditorWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGemserksAnimationEditor = new JFrame();
		frmGemserksAnimationEditor.setTitle("Gemserk's Animation Editor");
		frmGemserksAnimationEditor.setMinimumSize(new Dimension(800, 600));
		BorderLayout borderLayout = (BorderLayout) frmGemserksAnimationEditor.getContentPane().getLayout();
		borderLayout.setHgap(1);
		frmGemserksAnimationEditor.setBounds(100, 100, 800, 600);
		frmGemserksAnimationEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmGemserksAnimationEditor.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JPanel panelTools = new JPanel();
		panelTools.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frmGemserksAnimationEditor.getContentPane().add(panelTools, BorderLayout.WEST);
		panelTools.setMinimumSize(new Dimension(300, 400));
		panelTools.setPreferredSize(new Dimension(300, 400));
		panelTools.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelTools.add(splitPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setMinimumSize(new Dimension(100, 100));
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Preview");
		panel_1.add(lblNewLabel, BorderLayout.NORTH);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		
		Canvas canvas_2 = new Canvas();
		panel_1.add(canvas_2);
		canvas_2.setBackground(Color.LIGHT_GRAY);
		
		JPanel panel_4 = new JPanel();
		panel_4.setPreferredSize(new Dimension(100, 400));
		panel_4.setMinimumSize(new Dimension(100, 200));
		splitPane.setLeftComponent(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitle = new JLabel("Select Image");
		panel_4.add(lblTitle, BorderLayout.NORTH);
		lblTitle.setVerticalAlignment(SwingConstants.TOP);
		lblTitle.setToolTipText("Select image");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		Canvas canvasImages = new Canvas();
		panel_4.add(canvasImages);
		canvasImages.setBackground(Color.LIGHT_GRAY);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frmGemserksAnimationEditor.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelEditor = new JPanel();
		panel.add(panelEditor, BorderLayout.CENTER);
		panelEditor.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelEditor.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Editor");
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panelEditor.add(lblNewLabel_1, BorderLayout.NORTH);
		
		
		final EditorLibgdxApplicationListener editorApplication = new EditorLibgdxApplicationListener();
		
		Canvas canvasEditor = new Canvas() {
			
			private LwjglApplication lwjglApplication;

			public final void addNotify() {
				super.addNotify();
				lwjglApplication = new LwjglApplication(editorApplication, false, this);
			}
			
			public final void removeNotify() {
				lwjglApplication.stop();
				super.removeNotify();
			}
			
			{
				addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent e) {
						super.componentResized(e);
						System.out.println("resizeD!!");
						
					}
				});
			}
			
		};
		
		canvasEditor.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				System.out.println("resizeD!!");
			}
		});
		
//		Canvas canvasEditor = new Canvas();
		panelEditor.add(canvasEditor, BorderLayout.CENTER);
		canvasEditor.setIgnoreRepaint(true);
		
		JPanel panelTimeline = new JPanel();
		panel.add(panelTimeline, BorderLayout.SOUTH);
		panelTimeline.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Play");
		panelTimeline.add(tglbtnNewToggleButton);
		
		JButton btnNewButton = new JButton("Stop");
		panelTimeline.add(btnNewButton);
	}

}
