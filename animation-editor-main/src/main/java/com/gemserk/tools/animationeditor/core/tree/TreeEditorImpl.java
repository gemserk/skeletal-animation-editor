package com.gemserk.tools.animationeditor.core.tree;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.gemserk.tools.animationeditor.core.Animation;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Node;

public class TreeEditorImpl implements TreeEditor {

	private final Vector2 position = new Vector2();

	Node root;
	Node selectedNode;

	ArrayList<Node> nodes = new ArrayList<Node>();

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void select(Node node) {
		selectedNode = node;
	}

	@Override
	public void remove(Node node) {
		Node parent = node.getParent();
		parent.getChildren().remove(node);
		nodes.remove(node);
		if (isSelectedNode(node))
			selectedNode = parent;
	}

	@Override
	public void add(Node node) {
		if (root == null) {
			root = node;
			select(root);
			return;
		}
		node.setParent(selectedNode);
		selectedNode = node;
		nodes.add(node);
	}

	@Override
	public Node getNearestNode(float x, float y) {
		Node nearNode = root;

		position.set(nearNode.getX(), nearNode.getY());

		float minDistance = position.dst(x, y);

		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			position.set(node.getX(), node.getY());

			if (position.dst(x, y) < minDistance) {
				minDistance = position.dst(x, y);
				nearNode = node;
			}
		}

		return nearNode;
	}

	@Override
	public Node getSelectedNode() {
		return selectedNode;
	}

	@Override
	public boolean isSelectedNode(Node node) {
		return selectedNode == node;
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
	
	/// KeyFrames
	
	int index = 0;
	
	Animation currentAnimation = new Animation();
	
	AnimationKeyFrame selectedKeyFrame;

	@Override
	public AnimationKeyFrame addKeyFrame() {
		AnimationKeyFrame keyFrame = new AnimationKeyFrame("keyFrame" + index++);
		currentAnimation.getKeyFrames().add(keyFrame);
		return keyFrame;
	}

	@Override
	public void selectKeyFrame(AnimationKeyFrame keyFrame) {
		selectedKeyFrame = keyFrame;
	}

	@Override
	public void removeKeyFrame() {
		if (selectedKeyFrame != null)
			currentAnimation.getKeyFrames().remove(selectedKeyFrame);
	}

	@Override
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
}
