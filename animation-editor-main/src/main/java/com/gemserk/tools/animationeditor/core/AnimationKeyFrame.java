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

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public AnimationKeyFrame(String name, Skeleton skeleton) {
		this.name = name;
		this.skeleton = skeleton;
	}

	public AnimationKeyFrame(String name, Skeleton skeleton, float time) {
		this.name = name;
		this.skeleton = skeleton;
		this.time = time;
	}

}
