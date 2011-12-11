package com.gemserk.tools.animationeditor.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gemserk.tools.animationeditor.core.Node;

public class EditorWindow {

	/**
	 * Updates the TreeModel based on changes made over the Nodes of the current skeleton.
	 */
	static class UpdateTreeModelWithNodeChanges implements TreeObserver {

		JTree tree;
		DefaultTreeModel model;

		Map<String, TreeNodeEditorImpl> treeNodes = new HashMap<String, TreeNodeEditorImpl>();

		UpdateTreeModelWithNodeChanges(JTree tree) {
			this.tree = tree;
			this.model = (DefaultTreeModel) tree.getModel();
		}

		@Override
		public void update(Node root) {
			DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) model.getRoot();
			if (treeRoot == null)
				throw new IllegalStateException("Expected to have a root DefaultMutableTreeNode in the TreeModel");
			treeRoot.removeAllChildren();
			treeNodes.clear();
			update(root, treeRoot);
			model.reload();
		}

		private void update(Node node, DefaultMutableTreeNode parentTreeNode) {
			TreeNodeEditorImpl childNode = new TreeNodeEditorImpl(node);
			for (int i = 0; i < node.getChildren().size(); i++) {
				Node child = node.getChildren().get(i);
				update(child, childNode);
			}
			parentTreeNode.add(childNode);
			treeNodes.put(node.getId(), childNode);
		}

		@Override
		public void nodeSelected(Node node) {
			TreeNodeEditorImpl treeNodeEditorImpl = treeNodes.get(node.getId());
			// model.nodeChanged(treeNodeEditorImpl);
			if (treeNodeEditorImpl == null) {
				Node parent = node.getParent();
				treeNodeEditorImpl = treeNodes.get(parent.getId());
				// return;
			}
			TreePath path = new TreePath(treeNodeEditorImpl.getPath());
			tree.setSelectionPath(path);
			tree.scrollPathToVisible(path);
		}
	}

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

		JToggleButton tglbtnNewToggleButton = new JToggleButton("Play");
		panelTimeline.add(tglbtnNewToggleButton);

		JButton btnNewButton = new JButton("Stop");
		panelTimeline.add(btnNewButton);

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

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				Object treeNode = e.getPath().getLastPathComponent();
				if (treeNode instanceof TreeNodeEditorImpl) {
					Node editorNode = ((TreeNodeEditorImpl) treeNode).getNode();
					editorApplication.selectedNode = editorNode;
				}
			}
		});

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		panel_2.add(tree);
		tree.setBackground(Color.LIGHT_GRAY);

		editorApplication.setTreeObserver(new UpdateTreeModelWithNodeChanges(tree));

		JPanel panel_3 = new JPanel();
		splitPane_1.setRightComponent(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_2 = new JLabel("Properties");
		panel_3.add(lblNewLabel_2, BorderLayout.NORTH);
	}

}
