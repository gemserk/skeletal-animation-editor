package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public class SkeletonKeyFrame {

	private static ArrayList<Node> getArrayList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		add(nodes, node);
		return nodes;
	}

	private static void add(ArrayList<Node> nodes, Node node) {
		nodes.add(node);
		for (int i = 0; i < node.getChildren().size(); i++) {
			add(nodes, node.getChildren().get(i));
		}
	}

	public static float[] convert(Node root) {
		ArrayList<Node> nodes = getArrayList(root);

		float[] values = new float[nodes.size() * 3];
		int j = 0;
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);

			values[j] = node.getLocalX();
			values[j + 1] = node.getLocalY();
			values[j + 2] = node.getLocalAngle();

			j += 3;
		}

		return values;
	}

}