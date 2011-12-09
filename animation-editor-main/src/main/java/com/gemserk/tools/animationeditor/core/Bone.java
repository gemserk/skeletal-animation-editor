package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Bone implements Node {
	
	/**
	 * Local position to parent or absolute if no parent.
	 */
	Vector2 position = new Vector2();

	Node parent = new NodeNullImpl();
	ArrayList<Bone> children = new ArrayList<Bone>();
	
	@Override
	public float getX() {
		return parent.getX() + position.x;
	}
	
	@Override
	public float getY() {
		return parent.getY() + position.y;		
	}
	@Override
	public float getAngle() {
		return parent.getAngle();
	}

	@Override
	public void setPosition(float x, float y) {
		position.set(x - parent.getX(), y - parent.getY());
	}
	
}
