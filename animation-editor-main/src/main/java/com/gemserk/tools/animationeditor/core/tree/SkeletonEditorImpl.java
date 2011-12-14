package com.gemserk.tools.animationeditor.core.tree;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.Skeleton;

public class SkeletonEditorImpl implements SkeletonEditor {

	private final Vector2 position = new Vector2();

	AnimationKeyFrame currentKeyFrame;

	Joint selectedNode;

	ArrayList<Joint> joints = new ArrayList<Joint>();

	public SkeletonEditorImpl() {
		currentKeyFrame = new AnimationKeyFrame("none", new Skeleton());
	}

	@Override
	public Joint getRoot() {
		return currentKeyFrame.getSkeleton().getRoot();
	}

	@Override
	public Skeleton getSkeleton() {
		return currentKeyFrame.getSkeleton();
	}

	@Override
	public void select(Joint joint) {
		selectedNode = joint;
	}

	@Override
	public void remove(Joint joint) {
		Joint parent = joint.getParent();
		parent.getChildren().remove(joint);
		joints.remove(joint);
		if (isSelectedNode(joint))
			selectedNode = parent;
	}

	@Override
	public void setCurrentSkeleton(Skeleton skeleton) {
		currentKeyFrame.setSkeleton(skeleton);
		// currentKeyFrame = new AnimationKeyFrame("none", new Skeleton());
	}

	@Override
	public void add(Joint joint) {
		joint.setParent(selectedNode);
		selectedNode = joint;
		joints.add(joint);
	}

	@Override
	public Joint getNearestNode(float x, float y) {
		Joint nearNode = currentKeyFrame.getSkeleton().getRoot();

		position.set(nearNode.getX(), nearNode.getY());

		float minDistance = position.dst(x, y);

		for (int i = 0; i < joints.size(); i++) {
			Joint joint = joints.get(i);
			position.set(joint.getX(), joint.getY());

			if (position.dst(x, y) < minDistance) {
				minDistance = position.dst(x, y);
				nearNode = joint;
			}
		}

		return nearNode;
	}

	@Override
	public Joint getSelectedNode() {
		return selectedNode;
	}

	@Override
	public boolean isSelectedNode(Joint joint) {
		return selectedNode == joint;
	}

	@Override
	public void moveSelected(float dx, float dy) {
		float x = selectedNode.getX();
		float y = selectedNode.getY();
		selectedNode.setPosition(x + dx, y + dy);
	}

	@Override
	public void rotateSelected(float angle) {
		float currentAngle = selectedNode.getAngle();
		selectedNode.setAngle(currentAngle + angle);
	}

	// @Override
	// public void setRoot(Joint root) {
	// this.root = root;
	// selectedNode = root;
	//
	// joints.clear();
	// joints.addAll(JointUtils.getArrayList(root));
	// }

}
