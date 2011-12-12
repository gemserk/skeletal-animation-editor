package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public class SkeletonKeyFrame {

	public float[] values;

	public SkeletonKeyFrame(NodeImpl root) {
		update(getArrayList(root));
	}

	private void update(ArrayList<NodeImpl> nodes) {
		values = new float[nodes.size() * 3];
		int j = 0;
		for (int i = 0; i < nodes.size(); i++) {
			NodeImpl node = nodes.get(i);
			
			values[j] = node.localPosition.x;
			values[j + 1] = node.localPosition.y;
			values[j + 2] = node.angle;
			
			j += 3;
		}
	}
	
	ArrayList<NodeImpl> getArrayList(NodeImpl node) {
		ArrayList<NodeImpl> nodes = new ArrayList<NodeImpl>();
		add(nodes, node);
		return nodes;
	}

	void add(ArrayList<NodeImpl> nodes, NodeImpl node) {
		nodes.add(node);
		for (int i = 0; i < node.getChildren().size(); i++) {
			add(nodes, (NodeImpl) node.getChildren().get(i));
		}
	}

}