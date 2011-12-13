package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public class Animation {
	
	ArrayList<AnimationKeyFrame> keyFrames;
	
	public Animation() {
		keyFrames = new ArrayList<AnimationKeyFrame>();
	}
	
	public ArrayList<AnimationKeyFrame> getKeyFrames() {
		return keyFrames;
	}

}
