package com.gemserk.tools.animationeditor.core.tree;

import java.util.ArrayList;

import com.gemserk.tools.animationeditor.core.Animation;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.AnimationUtils;
import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.JointUtils;
import com.gemserk.tools.animationeditor.core.Skeleton;

public class AnimationEditorImpl implements AnimationEditor {

	protected SkeletonEditor skeletonEditor;

	int index = 0;
	Animation currentAnimation = new Animation();
	AnimationKeyFrame selectedKeyFrame;

	float duration = 0f;

	public AnimationEditorImpl(SkeletonEditor skeletonEditor) {
		this.skeletonEditor = skeletonEditor;
	}

	@Override
	public AnimationKeyFrame addKeyFrame() {
		String name = "keyFrame" + index++;
		AnimationKeyFrame keyFrame = AnimationUtils.keyFrame(name, JointUtils.cloneSkeleton(skeletonEditor.getSkeleton()), duration);
		currentAnimation.getKeyFrames().add(keyFrame);
		duration += 1f;

		// copy current skeleton values to keyframe.

		// skeletonEditor.setCurrentKeyFrame(keyFrame);

		return keyFrame;
	}

	@Override
	public void updateKeyFrame() {
		if (selectedKeyFrame == null)
			return;
		// throw new IllegalStateException("cant store current skeleton to no keyframe");

		selectedKeyFrame.setSkeleton(JointUtils.cloneSkeleton(skeletonEditor.getSkeleton()));
	}

	@Override
	public void selectKeyFrame(AnimationKeyFrame keyFrame) {
		selectedKeyFrame = keyFrame;

		// copy current keyframe values to skeleton.

		Skeleton skeleton = skeletonEditor.getSkeleton();
		ArrayList<Joint> joints = JointUtils.toArrayList(skeleton.getRoot());

		Skeleton keyFrameSkeleton = keyFrame.getSkeleton();

		for (int i = 0; i < joints.size(); i++) {
			Joint joint = joints.get(i);
			Joint keyFrameJointValue = keyFrameSkeleton.getRoot().find(joint.getId());
			if (keyFrameJointValue == null)
				continue;
			joint.setLocalPosition(keyFrameJointValue.getLocalX(), keyFrameJointValue.getLocalY());
			joint.setLocalAngle(keyFrameJointValue.getLocalAngle());
		}

	}

	@Override
	public void removeKeyFrame() {
		if (selectedKeyFrame != null)
			currentAnimation.getKeyFrames().remove(selectedKeyFrame);
	}

	@Override
	public Animation getCurrentAnimation() {
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

}
