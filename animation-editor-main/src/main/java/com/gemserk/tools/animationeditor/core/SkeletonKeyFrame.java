package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public class SkeletonKeyFrame {

	public float[] values;

	public SkeletonKeyFrame(Node root) {
		update(getArrayList(root));
	}

	private void update(ArrayList<Node> nodes) {
		values = new float[nodes.size() * 3];
		int j = 0;
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			
			values[j] = node.getLocalX();
			values[j + 1] = node.getLocalY();
			values[j + 2] = node.getLocalAngle();
			
			j += 3;
		}
	}
	
	ArrayList<Node> getArrayList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		add(nodes, node);
		return nodes;
	}

	void add(ArrayList<Node> nodes, Node node) {
		nodes.add(node);
		for (int i = 0; i < node.getChildren().size(); i++) {
			add(nodes, node.getChildren().get(i));
		}
	}

}