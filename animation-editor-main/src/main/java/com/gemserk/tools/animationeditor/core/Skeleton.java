package com.gemserk.tools.animationeditor.core;


public class Skeleton {

	Joint root;

	public Joint getRoot() {
		return root;
	}
	
	public void setRoot(Joint root) {
		this.root = root;
	}
	
	public Skeleton() {
		this(new JointImpl());
	}

	public Skeleton(Joint root) {
		this.root = root;
	}
	
	public void remove(Joint joint) {
		
	}
	
	public Joint find(String id) {
		return root.find(id);
	}
	
}
