package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.gemserk.animation4j.timeline.KeyFrame;
import com.gemserk.animation4j.timeline.Timeline;
import com.gemserk.animation4j.timeline.TimelineValue;
import com.gemserk.animation4j.timeline.TimelineValueMutableObjectImpl;

public class JointUtils {

	public static ArrayList<Joint> getArrayList(Joint joint) {
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

	public static Timeline getTimeline(Joint root, ArrayList<AnimationKeyFrame> frames) {
		ArrayList<Joint> joints = JointUtils.getArrayList(root);

		ArrayList<TimelineValue> timelineValues = new ArrayList<TimelineValue>();

		for (int j = 0; j < joints.size(); j++) {
			Joint joint = joints.get(j);

			TimelineValue timelineValue = new TimelineValueMutableObjectImpl<Joint>(joint, JointConverter.instance);

			for (int i = 0; i < frames.size(); i++) {
				AnimationKeyFrame animationKeyFrame = frames.get(i);
				Joint treeState = animationKeyFrame.getSkeleton().getRoot();
				Joint nodeState = treeState.find(joint.getId());
				
				if (nodeState == null)
					continue;

				timelineValue.addKeyFrame(new KeyFrame(animationKeyFrame.getTime(), JointConverter.instance.copyFromObject(nodeState, null)));
			}
			
			timelineValues.add(timelineValue);

		}

		return new Timeline(timelineValues);
	}

}
