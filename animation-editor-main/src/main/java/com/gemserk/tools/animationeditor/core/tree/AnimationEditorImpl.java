package com.gemserk.tools.animationeditor.core.tree;

import com.gemserk.tools.animationeditor.core.Skeleton;
import com.gemserk.tools.animationeditor.core.SkeletonAnimation;
import com.gemserk.tools.animationeditor.core.SkeletonAnimationKeyFrame;
import com.gemserk.tools.animationeditor.utils.JointUtils;
import com.gemserk.tools.animationeditor.utils.SkeletonAnimationUtils;

public class AnimationEditorImpl implements AnimationEditor {

	protected SkeletonEditor skeletonEditor;

	SkeletonAnimation currentAnimation = new SkeletonAnimation();
	SkeletonAnimationKeyFrame selectedKeyFrame;

	float timeInTimeline = 1f;

	public AnimationEditorImpl(SkeletonEditor skeletonEditor) {
		this.skeletonEditor = skeletonEditor;
	}

	@Override
	public SkeletonAnimationKeyFrame addKeyFrame() {
		int index = currentAnimation.getKeyFrames().size();
		
		timeInTimeline = index;

		String name = "keyFrame" + index++;
		
		SkeletonAnimationKeyFrame keyFrame = SkeletonAnimationUtils.keyFrame(name, JointUtils.cloneSkeleton(skeletonEditor.getSkeleton()), timeInTimeline);
		currentAnimation.getKeyFrames().add(keyFrame);

		// copy current skeleton values to keyframe.

		// skeletonEditor.setCurrentKeyFrame(keyFrame);

		return keyFrame;
	}

	@Override
	public void updateKeyFrame() {
		if (selectedKeyFrame == null)
			return;
		// throw new IllegalStateException("cant store current skeleton to no keyframe");
		// then we should disable the button when you can't save a keyframe!!
		SkeletonAnimationUtils.updateKeyFrame(skeletonEditor.getSkeleton(), selectedKeyFrame);
	}

	@Override
	public void selectKeyFrame(SkeletonAnimationKeyFrame keyFrame) {
		selectedKeyFrame = keyFrame;
		Skeleton skeleton = skeletonEditor.getSkeleton();
		SkeletonAnimationUtils.setKeyframeToSkeleton(skeleton, keyFrame);
	}

	@Override
	public void removeKeyFrame() {
		if (selectedKeyFrame != null)
			currentAnimation.getKeyFrames().remove(selectedKeyFrame);
	}

	@Override
	public SkeletonAnimation getCurrentAnimation() {
		return currentAnimation;
	}

	boolean playing = false;

	@Override
	public boolean isPlayingAnimation() {
		return playing;
	}

	@Override
	public void playAnimation() {
		playing = true;
	}

	@Override
	public void stopAnimation() {
		playing = false;
	}

	@Override
	public void setCurrentAnimation(SkeletonAnimation animation) {
		this.currentAnimation = animation;
		this.selectedKeyFrame = null;
	}

}
