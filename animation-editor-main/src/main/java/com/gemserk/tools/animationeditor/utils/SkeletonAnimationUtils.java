package com.gemserk.tools.animationeditor.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.gemserk.tools.animationeditor.converters.JointConverter;
import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.Skeleton;
import com.gemserk.tools.animationeditor.core.SkeletonAnimationKeyFrame;

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
			if (joint == null)
				continue;
			jointConverter.copyToObject(joint, jointKeyFrame);
		}
	}

}
