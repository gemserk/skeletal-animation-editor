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
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gemserk.tools.animationeditor.core.tree.TreeEditor;
import com.gemserk.tools.animationeditor.core.tree.TreeEditorImpl;
import com.gemserk.tools.animationeditor.main.tree.TreeEditorWithJtreeInterceptorImpl;

public class EditorWindow {

	class KeyFramesListModel extends AbstractListModel {

		private static final long serialVersionUID = 7156920082037501961L;

		public ArrayList<String> values;

		public KeyFramesListModel(String... values) {
			this.values = new ArrayList<String>();
			for (int i = 0; i < values.length; i++)
				this.values.add(values[i]);
		}

		public KeyFramesListModel(ArrayList<String> values) {
			this.values = new ArrayList<String>(values);
		}

		public int getSize() {
			return values.size();
		}

		public Object getElementAt(int index) {
			return values.get(index);
		}

	}

	private JFrame frmGemserksAnimationEditor;
	private JList keyFramesList;

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
					editorApplication.playAnimation();
				else
					editorApplication.pauseAnimation();
			}
		});

		JButton btnNewButton = new JButton("Stop");
		panelTimeline.add(btnNewButton);

		JButton btnAddKeyframe = new JButton("Add keyframe");
		btnAddKeyframe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeyFramesListModel model = (KeyFramesListModel) keyFramesList.getModel();
				model.values.add("keyFrame1");
				keyFramesList.setModel(new KeyFramesListModel(model.values));
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

		final TreeEditor treeEditor = new TreeEditorWithJtreeInterceptorImpl( //
				new TreeEditorImpl(), tree);
		editorApplication.setTreeEditor(treeEditor);

		JPanel panel_3 = new JPanel();
		splitPane_1.setRightComponent(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_2 = new JLabel("Properties");
		panel_3.add(lblNewLabel_2, BorderLayout.NORTH);

		keyFramesList = new JList();
		keyFramesList.setModel(new KeyFramesListModel("keyFrame0"));
		keyFramesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		keyFramesList.setBackground(Color.LIGHT_GRAY);
		panel_3.add(keyFramesList, BorderLayout.CENTER);
	}
}
