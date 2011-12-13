package com.gemserk.tools.animationeditor.core;

public class AnimationKeyFrame {
	
	String name;
	Node root;
	
	float time;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public float getTime() {
		return time;
	}
	public void setTime(float time) {
		this.time = time;
	}

	public AnimationKeyFrame(String name, Node root) {
		this.name = name;
		this.root = root;
	}
	
	public AnimationKeyFrame(String name, Node root, float time) {
		this.name = name;
		this.root = root;
		this.time = time;
	}
	
}
