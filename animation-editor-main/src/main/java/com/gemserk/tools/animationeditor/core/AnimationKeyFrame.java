package com.gemserk.tools.animationeditor.core;

public class AnimationKeyFrame {
	
	String name;
	Node root;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Node getRoot() {
		return root;
	}

	public AnimationKeyFrame(String name, Node root) {
		this.name = name;
		this.root = root;
	}
	
}
