package com.gemserk.tools.animationeditor.core;

/**
 * Holds a specific state of the Skeleton for a given time. Used for the Skeleton Animation.
 */
public class SkeletonAnimationKeyFrame {

	String name;
	Skeleton skeleton;

	float time;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	Skeleton getSkeleton() {
		return skeleton;
	}

	public Joint getJoint(String id) {
		return skeleton.getRoot().find(id);
	}

	void setSkeleton(Skeleton skeleton) {
		this.skeleton = skeleton;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	SkeletonAnimationKeyFrame(String name, Skeleton skeleton, float time) {
		this.name = name;
		this.skeleton = skeleton;
		this.time = time;
	}

}
