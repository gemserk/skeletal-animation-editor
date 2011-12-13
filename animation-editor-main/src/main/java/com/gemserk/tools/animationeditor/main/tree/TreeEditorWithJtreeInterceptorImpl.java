package com.gemserk.tools.animationeditor.main.tree;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Node;
import com.gemserk.tools.animationeditor.core.tree.TreeEditor;
import com.gemserk.tools.animationeditor.main.list.AnimationKeyFrameListModel;

/**
 * Updates the TreeModel based on changes made over the Nodes of the current skeleton.
 */
public class TreeEditorWithJtreeInterceptorImpl implements TreeEditor {

	class UpdateEditorTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			Object treeNode = e.getPath().getLastPathComponent();
			if (treeNode instanceof TreeNodeEditorImpl) {
				Node editorNode = ((TreeNodeEditorImpl) treeNode).getNode();
				treeEditor.select(editorNode);
			}
		}
	}

	JTree tree;
	DefaultTreeModel model;

	Map<String, TreeNodeEditorImpl> treeNodes = new HashMap<String, TreeNodeEditorImpl>();
	TreeEditor treeEditor;
	JList keyFramesList;

	public TreeEditorWithJtreeInterceptorImpl(TreeEditor treeEditor, JTree tree, JList keyFramesList) {
		this.treeEditor = treeEditor;
		this.tree = tree;
		this.keyFramesList = keyFramesList;
		this.model = (DefaultTreeModel) tree.getModel();
		tree.addTreeSelectionListener(new UpdateEditorTreeSelectionListener());
	}

	private void createTreeNodeForChild(Node node, DefaultMutableTreeNode parentTreeNode) {
		TreeNodeEditorImpl childNode = new TreeNodeEditorImpl(node);
		for (int i = 0; i < node.getChildren().size(); i++) {
			Node child = node.getChildren().get(i);
			createTreeNodeForChild(child, childNode);
		}
		parentTreeNode.add(childNode);
		treeNodes.put(node.getId(), childNode);
	}

	@Override
	public void select(Node node) {
		treeEditor.select(node);

		TreeNodeEditorImpl treeNode = treeNodes.get(node.getId());
		// model.nodeChanged(treeNodeEditorImpl);
		if (treeNode == null) {
			Node parent = node.getParent();
			treeNode = treeNodes.get(parent.getId());
			// return;
		}
		focusOnTreeNode(treeNode);
	}

	@Override
	public void remove(Node node) {
		if (node == null)
			throw new IllegalArgumentException("Cant remove a null node");

		treeEditor.remove(node);

		Node parent = node.getParent();
		TreeNodeEditorImpl parentTreeNode = treeNodes.get(parent.getId());
		if (parentTreeNode == null)
			throw new IllegalArgumentException("Node should be on the JTree to call remove");
		TreeNodeEditorImpl treeNode = treeNodes.get(node.getId());
		if (parentTreeNode.isNodeChild(treeNode))
			parentTreeNode.remove(treeNode);

		model.reload();
	}

	private void focusOnTreeNode(TreeNodeEditorImpl parentTreeNode) {
		TreePath path = new TreePath(parentTreeNode.getPath());
		tree.setSelectionPath(path);
		tree.scrollPathToVisible(path);
	}

	@Override
	public void add(Node node) {
		treeEditor.add(node);

		Node parent = node.getParent();
		TreeNodeEditorImpl parentTreeNode = treeNodes.get(parent.getId());
		if (parentTreeNode == null) {
			DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) model.getRoot();
			if (treeRoot == null)
				throw new IllegalStateException("Expected to have a root DefaultMutableTreeNode in the TreeModel");
			createTreeNodeForChild(node, treeRoot);
			model.reload();
			return;
		}
		createTreeNodeForChild(node, parentTreeNode);
		model.reload();
		// focusOnTreeNode(parentTreeNode);
	}

	@Override
	public Node getNearestNode(float x, float y) {
		return treeEditor.getNearestNode(x, y);
	}

	@Override
	public Node getRoot() {
		return treeEditor.getRoot();
	}

	@Override
	public boolean isSelectedNode(Node node) {
		return treeEditor.isSelectedNode(node);
	}

	public void moveSelected(float dx, float dy) {
		treeEditor.moveSelected(dx, dy);
	}

	public void rotateSelected(float angle) {
		treeEditor.rotateSelected(angle);
	}

	@Override
	public Node getSelectedNode() {
		return treeEditor.getSelectedNode();
	}

	@Override
	public AnimationKeyFrame addKeyFrame() {
		AnimationKeyFrame newKeyFrame = treeEditor.addKeyFrame();

		AnimationKeyFrameListModel listModel = (AnimationKeyFrameListModel) keyFramesList.getModel();
		listModel.values.add(newKeyFrame);

		keyFramesList.setModel(new AnimationKeyFrameListModel(listModel.values));

		return newKeyFrame;
	}

}