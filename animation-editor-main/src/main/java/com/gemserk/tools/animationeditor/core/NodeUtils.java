package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

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
	
}
