package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.gemserk.animation4j.timeline.KeyFrame;
import com.gemserk.animation4j.timeline.Timeline;
import com.gemserk.animation4j.timeline.TimelineValue;
import com.gemserk.animation4j.timeline.TimelineValueMutableObjectImpl;
import com.gemserk.tools.animationeditor.converters.JointConverter;

public class JointUtils {

	/**
	 * Returns an array list with the specified Joint and its children.
	 * 
	 * @param joint
	 *            The Joint from where to start the collection.
	 */
	public static ArrayList<Joint> toArrayList(Joint joint) {
		ArrayList<Joint> joints = new ArrayList<Joint>();
		addTreeToList(joints, joint);
		return joints;
	}

	public static void addTreeToList(ArrayList<Joint> joints, Joint joint) {
		joints.add(joint);
		for (int i = 0; i < joint.getChildren().size(); i++) {
			addTreeToList(joints, joint.getChildren().get(i));
		}
	}

	public static Skeleton cloneSkeleton(Skeleton skeleton) {
		return new Skeleton(cloneTree(skeleton.getRoot()));
	}

	public static Joint cloneTree(Joint joint) {
		JointImpl clonedNode = new JointImpl(joint);

		ArrayList<Joint> children = joint.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Joint child = children.get(i);
			Joint clonedChild = cloneTree(child);
			clonedChild.setParent(clonedNode);
		}

		return clonedNode;
	}

	public static Timeline getTimeline(Joint root, ArrayList<SkeletonAnimationKeyFrame> frames) {
		ArrayList<Joint> joints = JointUtils.toArrayList(root);

		ArrayList<TimelineValue> timelineValues = new ArrayList<TimelineValue>();

		for (int j = 0; j < joints.size(); j++) {
			Joint joint = joints.get(j);

			TimelineValue timelineValue = new TimelineValueMutableObjectImpl<Joint>(joint, JointConverter.instance);
			
			boolean emptyTimelineValue = true;

			for (int i = 0; i < frames.size(); i++) {
				SkeletonAnimationKeyFrame skeletonAnimationKeyFrame = frames.get(i);
				if (!skeletonAnimationKeyFrame.containsKeyFrameForJoint(joint.getId()))
					continue;
				float[] keyFrame = skeletonAnimationKeyFrame.getJointKeyFrame(joint.getId());
				timelineValue.addKeyFrame(new KeyFrame(skeletonAnimationKeyFrame.getTime(), keyFrame));
				emptyTimelineValue = false;
			}

			if (!emptyTimelineValue)
				timelineValues.add(timelineValue);

		}

		return new Timeline(timelineValues);
	}

}
