package com.gemserk.tools.animationeditor.core;

import java.util.Map;

/**
 * Holds a specific state of the Skeleton for a given time. Used for the Skeleton Animation.
 */
public class SkeletonAnimationKeyFrame {

	String name;
	float time;

	public Map<String, float[]> jointKeyFrames;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float[] getJointKeyFrame(String id) {
		return jointKeyFrames.get(id);
	}

	public boolean containsKeyFrameForJoint(String id) {
		return jointKeyFrames.containsKey(id);
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public SkeletonAnimationKeyFrame(String name, float time, Map<String, float[]> jointKeyFrames) {
		this.name = name;
		this.time = time;
		this.jointKeyFrames = jointKeyFrames;
	}

}
