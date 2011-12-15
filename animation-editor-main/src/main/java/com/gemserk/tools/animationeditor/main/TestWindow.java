package com.gemserk.tools.animationeditor.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TestWindow {

	private JFrame frmGemserksAnimationEditor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestWindow window = new TestWindow();
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
	public TestWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGemserksAnimationEditor = new JFrame();
		frmGemserksAnimationEditor.setPreferredSize(new Dimension(1024, 768));
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
		panelTools.setPreferredSize(new Dimension(200, 400));
		panelTools.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setPreferredSize(new Dimension(200, 29));
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
		
		Canvas canvasEditor = new Canvas();
		panelEditor.add(canvasEditor, BorderLayout.CENTER);
		canvasEditor.setBackground(Color.BLACK);
		
		JPanel panelTimeline = new JPanel();
		panel.add(panelTimeline, BorderLayout.SOUTH);
		panelTimeline.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("");
		tglbtnNewToggleButton.setSelectedIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonpause.png")));
		tglbtnNewToggleButton.setIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonplay.png")));
		panelTimeline.add(tglbtnNewToggleButton);
		
		JButton btnAddKeyframe = new JButton("Add keyframe");
		btnAddKeyframe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		panelTimeline.add(btnAddKeyframe);
		
		JPanel panelStructure = new JPanel();
		panelStructure.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelStructure.setPreferredSize(new Dimension(200, 10));
		panelStructure.setMinimumSize(new Dimension(150, 10));
		frmGemserksAnimationEditor.getContentPane().add(panelStructure, BorderLayout.EAST);
		panelStructure.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelStructure.add(splitPane_1, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		panel_2.setMinimumSize(new Dimension(10, 300));
		splitPane_1.setLeftComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_5 = new JPanel();
		scrollPane.setViewportView(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JLabel labelPanelStructureTitle = new JLabel("Structure");
		panel_5.add(labelPanelStructureTitle, BorderLayout.NORTH);
		
		JTree tree = new JTree();
		panel_5.add(tree);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println(e.getPath().getLastPathComponent());
			}
		});
		tree.setEditable(true);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("root") {
				{
					DefaultMutableTreeNode node_1;
					DefaultMutableTreeNode node_2;
					DefaultMutableTreeNode node_3;
					DefaultMutableTreeNode node_4;
					node_1 = new DefaultMutableTreeNode("leftArm");
						node_2 = new DefaultMutableTreeNode("hand");
							node_3 = new DefaultMutableTreeNode("hand");
								node_4 = new DefaultMutableTreeNode("hand");
									node_4.add(new DefaultMutableTreeNode("hand\t\t"));
								node_3.add(node_4);
							node_2.add(node_3);
						node_1.add(node_2);
					add(node_1);
					node_1 = new DefaultMutableTreeNode("rightArm");
						node_1.add(new DefaultMutableTreeNode("hand"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("leftLeg");
						node_1.add(new DefaultMutableTreeNode("foot"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("rightLeg");
						node_1.add(new DefaultMutableTreeNode("foot"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("leftArm");
						node_2 = new DefaultMutableTreeNode("hand");
							node_3 = new DefaultMutableTreeNode("hand");
								node_4 = new DefaultMutableTreeNode("hand");
									node_4.add(new DefaultMutableTreeNode("hand\t\t"));
								node_3.add(node_4);
							node_2.add(node_3);
						node_1.add(node_2);
					add(node_1);
					node_1 = new DefaultMutableTreeNode("rightArm");
						node_1.add(new DefaultMutableTreeNode("hand"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("leftLeg");
						node_1.add(new DefaultMutableTreeNode("foot"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("rightLeg");
						node_1.add(new DefaultMutableTreeNode("foot"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("leftArm");
						node_2 = new DefaultMutableTreeNode("hand");
							node_3 = new DefaultMutableTreeNode("hand");
								node_4 = new DefaultMutableTreeNode("hand");
									node_4.add(new DefaultMutableTreeNode("hand\t\t"));
								node_3.add(node_4);
							node_2.add(node_3);
						node_1.add(node_2);
					add(node_1);
					node_1 = new DefaultMutableTreeNode("rightArm");
						node_1.add(new DefaultMutableTreeNode("hand"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("leftLeg");
						node_1.add(new DefaultMutableTreeNode("foot"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("rightLeg");
						node_1.add(new DefaultMutableTreeNode("foot"));
					add(node_1);
				}
			}
		));
		tree.setBackground(Color.LIGHT_GRAY);
		
		JPanel panel_3 = new JPanel();
		splitPane_1.setRightComponent(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_3.add(scrollPane_1, BorderLayout.CENTER);
		
		JPanel panel_6 = new JPanel();
		scrollPane_1.setViewportView(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JList list = new JList();
		panel_6.add(list);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"pepe", "loco", "toto", "toto"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JLabel lblNewLabel_2 = new JLabel("KeyFrames");
		scrollPane_1.setColumnHeaderView(lblNewLabel_2);
		
		JPanel panel_7 = new JPanel();
		panel_3.add(panel_7, BorderLayout.SOUTH);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setFont(new Font("DejaVu Sans", Font.PLAIN, 10));
		btnNewButton_1.setIconTextGap(0);
		btnNewButton_1.setIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonadd.png")));
		panel_7.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonremove.png")));
		panel_7.add(btnNewButton_2);
		
		JButton btnStoreKeyFrame = new JButton("");
		btnStoreKeyFrame.setIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonsave.png")));
		panel_7.add(btnStoreKeyFrame);

	}

}
