package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public class AnimationUtils {

	public static AnimationKeyFrame keyFrame(String name, Skeleton skeleton, float time) {
		return new AnimationKeyFrame(name, skeleton, time);
	}

	/**
	 * Modifies the Skeleton to matches the keyframe.
	 * 
	 * @param skeleton
	 *            The Skeleton to be modified.
	 * @param keyFrame
	 *            The keyframe of the skeleton.
	 */
	public static void setKeyframeToSkeleton(Skeleton skeleton, AnimationKeyFrame keyFrame) {
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

}
