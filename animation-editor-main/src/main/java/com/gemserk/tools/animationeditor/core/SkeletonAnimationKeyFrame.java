package com.gemserk.tools.animationeditor.core;

import java.util.Map;

/**
 * Holds a specific state of the Skeleton for a given time. Used for the Skeleton Animation.
 */
public class SkeletonAnimationKeyFrame {

	String name;
	Skeleton skeleton;
	float time;
	
	Map<String, float[]> jointKeyFrames; 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	Skeleton getSkeleton() {
		return skeleton;
	}

	public float[] getJointKeyFrame(String id) {
		return jointKeyFrames.get(id);
	}
	
	public boolean containsKeyFrameForJoint(String id) {
		return jointKeyFrames.containsKey(id);
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

	SkeletonAnimationKeyFrame(String name, Skeleton skeleton, float time, Map<String, float[]> jointKeyFrames) {
		this.name = name;
		this.skeleton = skeleton;
		this.time = time;
		this.jointKeyFrames = jointKeyFrames;
	}

}
