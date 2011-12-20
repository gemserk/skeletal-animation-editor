package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public class SkeletonAnimation {
	
	ArrayList<SkeletonAnimationKeyFrame> keyFrames;
	
	public SkeletonAnimation() {
		keyFrames = new ArrayList<SkeletonAnimationKeyFrame>();
	}
	
	public ArrayList<SkeletonAnimationKeyFrame> getKeyFrames() {
		return keyFrames;
	}

}
