package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class NodeImpl implements Node {
	
	String id;

	/**
	 * Local position to parent or absolute if no parent.
	 */
	Vector2 localPosition = new Vector2();
	Vector2 absolutePosition = new Vector2();
	Vector2 tmp = new Vector2(0f, 0f);

	float angle = 0f;

	Node parent = new NodeRootImpl();
	ArrayList<Node> children = new ArrayList<Node>();
	
	public NodeImpl() {
		this("");
	}
	
	public NodeImpl(String id) {
		this(id, 0f, 0f, 0f);
	}

	public NodeImpl(String id, float x, float y, float angle) {
		setId(id);
		setPosition(x, y);
		setAngle(angle);
	}
	
	public NodeImpl(Node node) {
		this.parent = node.getParent();
		setId(node.getId());
		setPosition(node.getX(), node.getY());
		setAngle(node.getAngle());
	}

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public float getX() {
		return getLocalPosition().x;
	}

	@Override
	public float getY() {
		return getLocalPosition().y;
	}

	private Vector2 getLocalPosition() {
		absolutePosition.set(localPosition.x, localPosition.y);
		absolutePosition.rotate(parent.getAngle());
		absolutePosition.set(absolutePosition.x + parent.getX(), absolutePosition.y + parent.getY());
		return absolutePosition;
	}

	@Override
	public void setPosition(float x, float y) {
		float localX = parent.getLocalX(x, y);
		float localY = parent.getLocalY(x, y);

		// System.out.println("local.x = " + localX);
		// System.out.println("local.y = " + localY);
		//
		// System.out.println("parent.x = " + parent.getX());
		// System.out.println("parent.y = " + parent.getY());
		// System.out.println("parent.angle  = " + parent.getAngle());

		localPosition.set(localX, localY);
		// position.set(x - parent.getX(), y - parent.getY());
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
		if (this.parent == parent)
			return;
		
		float x = getX();
		float y = getY();
		float angle = getAngle();
		
		this.parent.getChildren().remove(this);
		this.parent = parent;
		this.parent.getChildren().add(this);
		
		setPosition(x, y);
		setAngle(angle);
	}

	@Override
	public Node getParent() {
		return parent;
	}

	@Override
	public ArrayList<Node> getChildren() {
		return children;
	}

	@Override
	public float getLocalX(float x, float y) {
		tmp.set(x - getX(), y - getY());
		tmp.rotate(-getAngle());
		return tmp.x;
	}

	@Override
	public float getLocalY(float x, float y) {
		tmp.set(x - getX(), y - getY());
		tmp.rotate(-getAngle());
		return tmp.y;
	}

	@Override
	public String toString() {
		return getId();
	}
}
