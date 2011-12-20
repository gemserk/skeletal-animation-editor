package com.gemserk.tools.animationeditor.core.tree;

import com.gemserk.tools.animationeditor.core.SkeletonAnimation;
import com.gemserk.tools.animationeditor.core.SkeletonAnimationKeyFrame;

public interface AnimationEditor {
	
	SkeletonAnimation getCurrentAnimation();
	
	SkeletonAnimationKeyFrame addKeyFrame();
	
	void selectKeyFrame(SkeletonAnimationKeyFrame keyFrame);
	
	/**
	 * Updates the current key frame with the current skeleton values.
	 */
	void updateKeyFrame();
	
	void removeKeyFrame();
	
	boolean isPlayingAnimation();
	
	void playAnimation();
	
	void stopAnimation();
	
}
