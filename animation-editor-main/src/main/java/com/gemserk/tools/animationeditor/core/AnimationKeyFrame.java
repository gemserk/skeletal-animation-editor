package com.gemserk.tools.animationeditor.core;

/**
 * Defines a state of the Skeleton.
 * 
 * @author acoppes
 */
public class AnimationKeyFrame {

	String name;
	Skeleton skeleton;

	float time;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Skeleton getSkeleton() {
		return skeleton;
	}
	
	public Joint getJoint(String id) {
		return skeleton.getRoot().find(id);
	}
	
	public void setSkeleton(Skeleton skeleton) {
		this.skeleton = skeleton;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	AnimationKeyFrame(String name, Skeleton skeleton, float time) {
		this.name = name;
		this.skeleton = skeleton;
		this.time = time;
	}

}
