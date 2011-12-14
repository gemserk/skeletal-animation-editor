package com.gemserk.tools.animationeditor.main.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.gemserk.tools.animationeditor.core.Animation;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.tree.AnimationEditor;
import com.gemserk.tools.animationeditor.core.tree.AnimationEditorImpl;
import com.gemserk.tools.animationeditor.core.tree.SkeletonEditor;
import com.gemserk.tools.animationeditor.main.list.AnimationKeyFrameListModel;

/**
 * Updates the TreeModel based on changes made over the Nodes of the current skeleton.
 */
public class EditorInterceptorImpl implements SkeletonEditor, AnimationEditor {

	class UpdateCurrentAnimationFromJList implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			AnimationKeyFrameListModel model = (AnimationKeyFrameListModel) keyFramesList.getModel();
			if (keyFramesList.getSelectedIndex() == -1)
				return;
			AnimationKeyFrame keyFrame = model.values.get(keyFramesList.getSelectedIndex());
			animationEditor.selectKeyFrame(keyFrame);
		}
	}

	class UpdateEditorTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			Object treeNode = e.getPath().getLastPathComponent();
			if (treeNode instanceof TreeNodeForJointImpl) {
				Joint editorNode = ((TreeNodeForJointImpl) treeNode).getNode();
				skeletonEditor.select(editorNode);
			}
		}
	}

	JTree tree;
	DefaultTreeModel model;

	Map<String, TreeNodeForJointImpl> treeNodes = new HashMap<String, TreeNodeForJointImpl>();
	
	SkeletonEditor skeletonEditor;
	AnimationEditor animationEditor;
	
	JList keyFramesList;

	public EditorInterceptorImpl(SkeletonEditor skeletonEditor, JTree tree, JList keyFramesList) {
		this.skeletonEditor = skeletonEditor;
		this.tree = tree;
		this.keyFramesList = keyFramesList;
		this.model = (DefaultTreeModel) tree.getModel();
		tree.addTreeSelectionListener(new UpdateEditorTreeSelectionListener());
		keyFramesList.addListSelectionListener(new UpdateCurrentAnimationFromJList());
		
		animationEditor = new AnimationEditorImpl(this);
	}

	private void createTreeNodeForChild(Joint joint, DefaultMutableTreeNode parentTreeNode) {
		TreeNodeForJointImpl childNode = new TreeNodeForJointImpl(joint);
		for (int i = 0; i < joint.getChildren().size(); i++) {
			Joint child = joint.getChildren().get(i);
			createTreeNodeForChild(child, childNode);
		}
		parentTreeNode.add(childNode);
		treeNodes.put(joint.getId(), childNode);
	}

	@Override
	public void select(Joint joint) {
		skeletonEditor.select(joint);

		TreeNodeForJointImpl treeNode = treeNodes.get(joint.getId());
		// model.nodeChanged(treeNodeEditorImpl);
		if (treeNode == null) {
			Joint parent = joint.getParent();
			treeNode = treeNodes.get(parent.getId());
			// return;
		}
		focusOnTreeNode(treeNode);
	}

	@Override
	public void remove(Joint joint) {
		if (joint == null)
			throw new IllegalArgumentException("Cant remove a null node");

		skeletonEditor.remove(joint);

		Joint parent = joint.getParent();
		TreeNodeForJointImpl parentTreeNode = treeNodes.get(parent.getId());
		if (parentTreeNode == null)
			throw new IllegalArgumentException("Node should be on the JTree to call remove");
		TreeNodeForJointImpl treeNode = treeNodes.get(joint.getId());
		if (parentTreeNode.isNodeChild(treeNode))
			parentTreeNode.remove(treeNode);

		model.reload();
	}

	private void focusOnTreeNode(TreeNodeForJointImpl parentTreeNode) {
		TreePath path = new TreePath(parentTreeNode.getPath());
		tree.setSelectionPath(path);
		tree.scrollPathToVisible(path);
	}

	@Override
	public void add(Joint joint) {
		skeletonEditor.add(joint);

		Joint parent = joint.getParent();
		TreeNodeForJointImpl parentTreeNode = treeNodes.get(parent.getId());
		if (parentTreeNode == null) {
			DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) model.getRoot();
			if (treeRoot == null)
				throw new IllegalStateException("Expected to have a root DefaultMutableTreeNode in the TreeModel");
			createTreeNodeForChild(joint, treeRoot);
			model.reload();
			return;
		}
		createTreeNodeForChild(joint, parentTreeNode);
		model.reload();
		// focusOnTreeNode(parentTreeNode);
	}

	@Override
	public Joint getNearestNode(float x, float y) {
		return skeletonEditor.getNearestNode(x, y);
	}

	@Override
	public Joint getRoot() {
		return skeletonEditor.getRoot();
	}

	@Override
	public boolean isSelectedNode(Joint joint) {
		return skeletonEditor.isSelectedNode(joint);
	}

	public void moveSelected(float dx, float dy) {
		skeletonEditor.moveSelected(dx, dy);
	}

	public void rotateSelected(float angle) {
		skeletonEditor.rotateSelected(angle);
	}

	@Override
	public Joint getSelectedNode() {
		return skeletonEditor.getSelectedNode();
	}

	@Override
	public AnimationKeyFrame addKeyFrame() {
		AnimationKeyFrame newKeyFrame = animationEditor.addKeyFrame();
		ArrayList<AnimationKeyFrame> keyFrames = getCurrentAnimation().getKeyFrames();
		keyFramesList.setModel(new AnimationKeyFrameListModel(keyFrames));
		keyFramesList.setSelectedIndex(keyFrames.indexOf(newKeyFrame));
		return newKeyFrame;
	}

	@Override
	public void selectKeyFrame(AnimationKeyFrame keyFrame) {
		animationEditor.selectKeyFrame(keyFrame);
		keyFramesList.setSelectedValue(keyFrame, true);
	}

	@Override
	public void removeKeyFrame() {
		animationEditor.removeKeyFrame();
		Animation currentAnimation = animationEditor.getCurrentAnimation();
		keyFramesList.setModel(new AnimationKeyFrameListModel(currentAnimation.getKeyFrames()));
		keyFramesList.setSelectedIndex(0);
	}

	@Override
	public Animation getCurrentAnimation() {
		return animationEditor.getCurrentAnimation();
	}

//	@Override
//	public void setRoot(Joint root) {
//		DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) model.getRoot();
//		treeRoot.removeAllChildren();
//		createTreeNodeForChild(root, treeRoot);
//		model.reload();
//		skeletonEditor.setRoot(root);
//	}

	public boolean isPlayingAnimation() {
		return animationEditor.isPlayingAnimation();
	}

	public void playAnimation() {
		animationEditor.playAnimation();
	}

	public void stopAnimation() {
		animationEditor.stopAnimation();
	}

}