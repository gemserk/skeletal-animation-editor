package com.gemserk.tools.animationeditor.core;

public class AnimationUtils {
	
	public static AnimationKeyFrame keyFrame(String name, Skeleton skeleton, float time) {
		return new AnimationKeyFrame(name, skeleton, time);
	}

}
