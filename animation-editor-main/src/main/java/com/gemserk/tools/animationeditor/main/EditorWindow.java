package com.gemserk.tools.animationeditor.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.tree.SkeletonEditorImpl;
import com.gemserk.tools.animationeditor.main.list.AnimationKeyFrameListModel;
import com.gemserk.tools.animationeditor.main.tree.EditorInterceptorImpl;

public class EditorWindow {

	private JFrame frmGemserksAnimationEditor;
	private JList keyFramesList;
	private EditorInterceptorImpl treeEditor;

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

		final EditorLibgdxApplicationListener editorApplication = new EditorLibgdxApplicationListener();

		final Canvas canvasEditor = new Canvas() {

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
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						requestFocus();
					}

					@Override
					public void mouseExited(MouseEvent e) {
						getParent().requestFocus();
					};
				});
			}
		};

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
		panelEditor.setMaximumSize(new Dimension(800, 600));
		panelEditor.setPreferredSize(new Dimension(800, 600));
		panelEditor.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelEditor.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_1 = new JLabel("Editor");
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panelEditor.add(lblNewLabel_1, BorderLayout.NORTH);

		panelEditor.add(canvasEditor, BorderLayout.CENTER);
		canvasEditor.setBackground(Color.BLACK);

		JPanel panelTimeline = new JPanel();
		panel.add(panelTimeline, BorderLayout.SOUTH);
		panelTimeline.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		final JToggleButton tglbtnNewToggleButton = new JToggleButton("Play");
		panelTimeline.add(tglbtnNewToggleButton);

		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tglbtnNewToggleButton.isSelected())
					treeEditor.playAnimation();
				else
					treeEditor.stopAnimation();
			}
		});

		JButton btnAddKeyframe = new JButton("Add");
		btnAddKeyframe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnimationKeyFrame keyFrame = treeEditor.addKeyFrame();
				treeEditor.selectKeyFrame(keyFrame);
			}
		});
		panelTimeline.add(btnAddKeyframe);

		JButton btnRemoveKeyframe = new JButton("Remove");
		btnRemoveKeyframe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treeEditor.removeKeyFrame();
			}
		});
		panelTimeline.add(btnRemoveKeyframe);

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
		panel_2.setMinimumSize(new Dimension(10, 400));
		splitPane_1.setLeftComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel labelPanelStructureTitle = new JLabel("Structure");
		panel_2.add(labelPanelStructureTitle, BorderLayout.NORTH);

		final JTree tree = new JTree();
		tree.setEditable(true);
		tree.setRootVisible(false);
		tree.setExpandsSelectedPaths(true);

		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("root")));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		panel_2.add(tree);
		tree.setBackground(Color.LIGHT_GRAY);

		keyFramesList = new JList();
		keyFramesList.setModel(new AnimationKeyFrameListModel());
		keyFramesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		keyFramesList.setBackground(Color.LIGHT_GRAY);

		treeEditor = new EditorInterceptorImpl( //
				new SkeletonEditorImpl(), tree, keyFramesList);

		editorApplication.setTreeEditor(treeEditor);
		editorApplication.setAnimationEditor(treeEditor);

		JPanel panel_3 = new JPanel();
		splitPane_1.setRightComponent(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_3.add(scrollPane_1, BorderLayout.CENTER);

		JPanel panel_6 = new JPanel();
		scrollPane_1.setViewportView(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));

		panel_6.add(keyFramesList, BorderLayout.CENTER);

		JLabel lblNewLabel_2 = new JLabel("KeyFrames");
		scrollPane_1.setColumnHeaderView(lblNewLabel_2);
	}
}
