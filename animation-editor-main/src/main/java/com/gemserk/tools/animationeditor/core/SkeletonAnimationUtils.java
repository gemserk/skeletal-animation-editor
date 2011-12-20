package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SkeletonAnimationUtils {

	static JointConverter jointConverter = JointConverter.instance;

	public static SkeletonAnimationKeyFrame keyFrame(String name, Skeleton skeleton, float time) {
		HashMap<String, float[]> jointKeyFrames = new HashMap<String, float[]>();

		ArrayList<Joint> jointList = JointUtils.toArrayList(skeleton.getRoot());

		for (Joint joint : jointList) {
			jointKeyFrames.put(joint.getId(), jointConverter.copyFromObject(joint, null));
		}

		return new SkeletonAnimationKeyFrame(name, time, jointKeyFrames);
	}

	public static void updateKeyFrame(Skeleton skeleton, SkeletonAnimationKeyFrame keyframe) {
		HashMap<String, float[]> jointKeyFrames = new HashMap<String, float[]>();

		ArrayList<Joint> jointList = JointUtils.toArrayList(skeleton.getRoot());

		for (Joint joint : jointList) {
			jointKeyFrames.put(joint.getId(), jointConverter.copyFromObject(joint, null));
		}

		keyframe.jointKeyFrames.clear();
		keyframe.jointKeyFrames.putAll(jointKeyFrames);

		// keyframe.setSkeleton(JointUtils.cloneSkeleton(skeleton));
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
		Set<String> keySet = keyframe.jointKeyFrames.keySet();

		for (String jointId : keySet) {
			float[] jointKeyFrame = keyframe.getJointKeyFrame(jointId);
			Joint joint = skeleton.getRoot().find(jointId);
			jointConverter.copyToObject(joint, jointKeyFrame);
		}
	}

}