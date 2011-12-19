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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gemserk.commons.files.FileUtils;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.commons.reflection.InjectorImpl;
import com.gemserk.resources.ResourceManager;
import com.gemserk.resources.ResourceManagerImpl;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.JointImpl;
import com.gemserk.tools.animationeditor.core.Skin;
import com.gemserk.tools.animationeditor.core.Skin.SkinPatch;
import com.gemserk.tools.animationeditor.core.tree.SkeletonEditorImpl;
import com.gemserk.tools.animationeditor.json.JointJsonDeserializer;
import com.gemserk.tools.animationeditor.json.JointJsonSerializer;
import com.gemserk.tools.animationeditor.json.SkinJsonSerializer;
import com.gemserk.tools.animationeditor.json.SkinPatchJsonSerializer;
import com.gemserk.tools.animationeditor.main.list.AnimationKeyFrameListModel;
import com.gemserk.tools.animationeditor.main.tree.EditorInterceptorImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditorWindow {

	protected static final Logger logger = LoggerFactory.getLogger(EditorWindow.class);

	private JFrame frmGemserksAnimationEditor;
	private JList keyFramesList;
	private EditorInterceptorImpl editor;

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

		ResourceManager<String> resourceManager = new ResourceManagerImpl<String>();

		Injector injector = new InjectorImpl();

		injector.bind("resourceManager", resourceManager);

		final EditorLibgdxApplicationListener editorApplication = injector.getInstance(EditorLibgdxApplicationListener.class);

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

		JMenuItem mntmImport = new JMenuItem("Import");
		mntmImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images only", "png", "jpg", "gif");

				chooser.setFileFilter(filter);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);

				int returnVal = chooser.showOpenDialog(frmGemserksAnimationEditor);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = chooser.getSelectedFile();

					editorApplication.setCurrentSkin(selectedFile);
					// File[] selectedFiles = chooser.getSelectedFiles();
					// for (int i = 0; i < selectedFiles.length; i++) {
					// System.out.println("file " + i + " : " + selectedFiles[i].getName());
					// }
				}
			}
		});
		mnFile.add(mntmImport);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Project files only", ".aprj");

				chooser.setFileFilter(filter);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(false);

				int returnVal = chooser.showSaveDialog(frmGemserksAnimationEditor);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = chooser.getSelectedFile();
					System.out.println("save project to... " + selectedFile.getName());

					String projectFileNameWithoutExtension = FileUtils.getFileNameWithoutExtension(selectedFile.getAbsolutePath());

					String projectFileName = projectFileNameWithoutExtension + ".aprj";
					String skeletonFileName = projectFileNameWithoutExtension + ".skeleton";
					String skinFileName = projectFileNameWithoutExtension + ".skin";

					try {
						Gson gson = new GsonBuilder() //
								.registerTypeAdapter(JointImpl.class, new JointJsonSerializer()) //
								.registerTypeAdapter(Joint.class, new JointJsonDeserializer()) //
								.setPrettyPrinting() //
								.create();
						FileWriter writer = new FileWriter(new File(skeletonFileName));

						gson.toJson(editor.getSkeleton(), writer);

						writer.flush();
						writer.close();

						logger.info("Skeleton saved to " + skeletonFileName);
					} catch (IOException e1) {
						logger.error("Failed to save skeleton file to " + skeletonFileName, e1);
					}

					try {
						Gson gson = new GsonBuilder() //
								.registerTypeAdapter(SkinPatch.class, new SkinPatchJsonSerializer()) //
								.registerTypeAdapter(Skin.class, new SkinJsonSerializer()) //
								.setPrettyPrinting() //
								.create();
						
						FileWriter writer = new FileWriter(new File(skinFileName));

						gson.toJson(editorApplication.skin, writer);

						writer.flush();
						writer.close();

						logger.info("Skin saved to " + skinFileName);
					} catch (IOException e1) {
						logger.error("Failed to save skin file to " + skeletonFileName, e1);
					}

					// save
				}
			}
		});
		mnFile.add(mntmSave);

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

		JScrollPane scrollPane_2 = new JScrollPane();
		panel_4.add(scrollPane_2, BorderLayout.CENTER);

		JPanel panelImages = new JPanel();
		panelImages.setBackground(Color.LIGHT_GRAY);
		scrollPane_2.setViewportView(panelImages);

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

		final JToggleButton toggleButtonPlay = new JToggleButton("");
		toggleButtonPlay.setSelectedIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonpause.png")));
		toggleButtonPlay.setIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonplay.png")));
		toggleButtonPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (toggleButtonPlay.isSelected())
					editor.playAnimation();
				else
					editor.stopAnimation();
			}
		});
		panelTimeline.add(toggleButtonPlay);

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

		editor = new EditorInterceptorImpl( //
				new SkeletonEditorImpl(), tree, keyFramesList);

		editorApplication.setTreeEditor(editor);
		editorApplication.setAnimationEditor(editor);

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

		JPanel panel_7 = new JPanel();
		panel_3.add(panel_7, BorderLayout.SOUTH);

		JButton buttonAddKeyFrame = new JButton("");
		buttonAddKeyFrame.setIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonadd.png")));
		buttonAddKeyFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AnimationKeyFrame keyFrame = editor.addKeyFrame();
				editor.selectKeyFrame(keyFrame);
			}
		});
		panel_7.add(buttonAddKeyFrame);

		JButton buttonRemoveKeyFrame = new JButton("");
		buttonRemoveKeyFrame.setIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonremove.png")));
		buttonRemoveKeyFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.removeKeyFrame();
			}
		});
		panel_7.add(buttonRemoveKeyFrame);

		JButton buttonStoreKeyFrame = new JButton("");
		buttonStoreKeyFrame.setIcon(new ImageIcon(TestWindow.class.getResource("/data/buttonsave.png")));
		buttonStoreKeyFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.updateKeyFrame();
			}
		});
		panel_7.add(buttonStoreKeyFrame);
	}

}
