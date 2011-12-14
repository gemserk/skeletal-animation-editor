package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

class JointNullImpl implements Joint {
	
	final String id = "";

	ArrayList<Joint> children = new ArrayList<Joint>();
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {

	}

	@Override
	public float getX() {
		return 0f;
	}

	@Override
	public float getY() {
		return 0f;
	}

	@Override
	public float getAngle() {
		return 0f;
	}

	@Override
	public void setPosition(float x, float y) {
		
	}

	@Override
	public void setLocalPosition(float x, float y) {
		
	}

	@Override
	public void setParent(Joint joint) {

	}

	@Override
	public Joint getParent() {
		return null;
	}

	@Override
	public ArrayList<Joint> getChildren() {
		return children;
	}

	@Override
	public void setAngle(float angle) {
		
	}

	@Override
	public float projectX(float x, float y) {
		return x;
	}

	@Override
	public float projectY(float x, float y) {
		return y;
	}

	@Override
	public float getLocalAngle() {
		return 0;
	}

	@Override
	public void setLocalAngle(float angle) {
		
	}

	@Override
	public float getLocalX() {
		return 0;
	}

	@Override
	public float getLocalY() {
		return 0;
	}

	@Override
	public Joint find(String id) {
		return null;
	}

}
