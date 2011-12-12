package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

public class TreeConverter {

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

	public static void copyToObject(Node root, float[] values){ 
		ArrayList<Node> nodes = getArrayList(root);
		int nodeIndex = 0;
		for (int i = 0; i < values.length; i += 3) {
			Node node = nodes.get(nodeIndex);
			float localX = values[i + 0];
			float localY = values[i + 1];
			float localAngle = values[i + 2];
			node.setLocalPosition(localX, localY);
			node.setLocalAngle(localAngle);
			nodeIndex++;
		}
	}
}