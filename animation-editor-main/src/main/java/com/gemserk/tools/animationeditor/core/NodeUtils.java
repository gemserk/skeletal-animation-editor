package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.gemserk.animation4j.timeline.KeyFrame;
import com.gemserk.animation4j.timeline.Timeline;
import com.gemserk.animation4j.timeline.TimelineValue;
import com.gemserk.animation4j.timeline.TimelineValueMutableObjectImpl;

public class NodeUtils {

	public static ArrayList<Node> getArrayList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		addTreeToList(nodes, node);
		return nodes;
	}

	public static void addTreeToList(ArrayList<Node> nodes, Node node) {
		nodes.add(node);
		for (int i = 0; i < node.getChildren().size(); i++) {
			addTreeToList(nodes, node.getChildren().get(i));
		}
	}
	
	public static Node cloneTree(Node node) {
		NodeImpl clonedNode = new NodeImpl(node);

		ArrayList<Node> children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node child = children.get(i);
			Node clonedChild = cloneTree(child);
			clonedChild.setParent(clonedNode);
		}

		return clonedNode;
	}
	
	public static Timeline getTimeline(Node root, ArrayList<AnimationKeyFrame> frames) {
		ArrayList<Node> nodes = NodeUtils.getArrayList(root);

		ArrayList<TimelineValue> timelineValues = new ArrayList<TimelineValue>();

		for (int j = 0; j < nodes.size(); j++) {
			Node node = nodes.get(j);

			TimelineValue timelineValue = new TimelineValueMutableObjectImpl<Node>(node, NodeConverter.instance);

			float time = 0f;
			
			for (int i = 0; i < frames.size(); i++) {
				AnimationKeyFrame animationKeyFrame = frames.get(i);
				Node treeState = animationKeyFrame.getRoot();
				Node nodeState = treeState.getChild(node.getId());

				timelineValue.addKeyFrame(new KeyFrame(animationKeyFrame.getTime(), NodeConverter.instance.copyFromObject(nodeState, null)));
				time += 1f;
				
				timelineValues.add(timelineValue);
			}

		}

		return new Timeline(timelineValues);
	}
	
}
