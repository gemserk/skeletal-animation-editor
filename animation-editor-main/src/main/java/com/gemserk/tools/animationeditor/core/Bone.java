package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Bone implements Node {

	/**
	 * Local position to parent or absolute if no parent.
	 */
	Vector2 position = new Vector2();
	Vector2 parentPosition = new Vector2();
	
	float angle = 0f;

	Node parent = new NodeRootImpl();
	ArrayList<Node> children = new ArrayList<Node>();

	@Override
	public float getX() {
		return getPosition().x;
	}

	@Override
	public float getY() {
		return getPosition().y;
	}

	private Vector2 getPosition() {
		parentPosition.set(position.x, position.y);
		parentPosition.rotate(parent.getAngle());
		parentPosition.set(parentPosition.x + parent.getX(), parentPosition.y + parent.getY());
		return parentPosition;
	}

	@Override
	public void setPosition(float x, float y) {
		position.set(x - parent.getX(), y - parent.getY());
	}
	
	
	@Override
	public float getAngle() {
		return angle + parent.getAngle();
	}

	@Override
	public void setAngle(float angle) {
		this.angle = angle - parent.getAngle();
	}

	@Override
	public void setParent(Node parent) {
		// should convert coordinates to this other parent?
		this.parent.getChildren().remove(this);
		this.parent = parent;
		this.parent.getChildren().add(this);
	}

	@Override
	public Node getParent() {
		return parent;
	}

	@Override
	public ArrayList<Node> getChildren() {
		return children;
	}

}
