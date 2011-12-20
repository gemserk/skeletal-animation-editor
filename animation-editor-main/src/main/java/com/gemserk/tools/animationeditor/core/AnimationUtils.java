package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public class AnimationUtils {

	public static SkeletonAnimationKeyFrame keyFrame(String name, Skeleton skeleton, float time) {
		return new SkeletonAnimationKeyFrame(name, skeleton, time);
	}

	public static void updateKeyFrame(Skeleton skeleton, SkeletonAnimationKeyFrame keyframe) {
		keyframe.setSkeleton(JointUtils.cloneSkeleton(skeleton));
	}

	/**
	 * Modifies the Skeleton to matches the keyframe.
	 * 
	 * @param skeleton
	 *            The Skeleton to be modified.
	 * @param keyframe
	 *            The keyframe of the skeleton.
	 */
	public static void setKeyframeToSkeleton(Skeleton skeleton, SkeletonAnimationKeyFrame keyframe) {
		ArrayList<Joint> joints = JointUtils.toArrayList(skeleton.getRoot());

		Skeleton keyFrameSkeleton = keyframe.getSkeleton();

		for (int i = 0; i < joints.size(); i++) {
			Joint joint = joints.get(i);
			Joint keyFrameJointValue = keyFrameSkeleton.getRoot().find(joint.getId());
			if (keyFrameJointValue == null)
				continue;
			joint.setLocalPosition(keyFrameJointValue.getLocalX(), keyFrameJointValue.getLocalY());
			joint.setLocalAngle(keyFrameJointValue.getLocalAngle());
		}

	}

}
