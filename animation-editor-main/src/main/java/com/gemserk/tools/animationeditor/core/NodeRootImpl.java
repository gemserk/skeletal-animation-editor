package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class NodeRootImpl implements Node {

	Vector2 position = new Vector2(0f, 0f);
	Vector2 tmp = new Vector2(0f, 0f);
	float angle = 0f;

	ArrayList<Node> children = new ArrayList<Node>();

	@Override
	public float getX() {
		return position.x;
	}

	@Override
	public float getY() {
		return position.y;
	}

	@Override
	public float getAngle() {
		return angle;
	}

	@Override
	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	@Override
	public void setParent(Node node) {

	}

	@Override
	public Node getParent() {
		return null;
	}

	@Override
	public ArrayList<Node> getChildren() {
		return children;
	}

	@Override
	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public float getLocalX(float x, float y) {
		tmp.set(x - position.x, y - position.y);
		tmp.rotate(-angle);
		return tmp.x;
	}

	@Override
	public float getLocalY(float x, float y) {
		tmp.set(x - position.x, y - position.y);
		tmp.rotate(-angle);
		return tmp.y;
	}

}
