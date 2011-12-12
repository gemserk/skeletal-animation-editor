package com.gemserk.tools.animationeditor.main.tree;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.gemserk.tools.animationeditor.core.Node;
import com.gemserk.tools.animationeditor.core.tree.TreeObserver;
import com.gemserk.tools.animationeditor.main.TreeNodeEditorImpl;

/**
 * Updates the TreeModel based on changes made over the Nodes of the current skeleton.
 */
public class TreeObserverForJtree implements TreeObserver {

	JTree tree;
	DefaultTreeModel model;

	Map<String, TreeNodeEditorImpl> treeNodes = new HashMap<String, TreeNodeEditorImpl>();

	public TreeObserverForJtree(JTree tree) {
		this.tree = tree;
		this.model = (DefaultTreeModel) tree.getModel();
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
		Node parent = node.getParent();
		TreeNodeEditorImpl parentTreeNode = treeNodes.get(parent.getId());
		if (parentTreeNode == null)
			throw new IllegalArgumentException("Node should be on the JTree to call remove");
		parentTreeNode.removeAllChildren();
		model.reload(parentTreeNode);
	}

	private void focusOnTreeNode(TreeNodeEditorImpl parentTreeNode) {
		TreePath path = new TreePath(parentTreeNode.getPath());
		tree.setSelectionPath(path);
		tree.scrollPathToVisible(path);
	}

	@Override
	public void add(Node node) {
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
		focusOnTreeNode(parentTreeNode);
	}
}