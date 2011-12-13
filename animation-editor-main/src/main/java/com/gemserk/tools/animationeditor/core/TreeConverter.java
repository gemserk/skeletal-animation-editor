package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.gemserk.animation4j.converters.TypeConverter;

public class TreeConverter implements TypeConverter<Node> {

	public static TreeConverter instance = new TreeConverter();

	@Override
	public int variables() {
		throw new UnsupportedOperationException("cant return the number of variables of a variable tree");
	}

	public Node copyToObject(Node node, float[] x) {
		ArrayList<Node> nodes = NodeUtils.getArrayList(node);
		int nodeIndex = 0;
		for (int i = 0; i < x.length; i += 3) {
			Node n = nodes.get(nodeIndex);
			float localX = x[i + 0];
			float localY = x[i + 1];
			float localAngle = x[i + 2];
			n.setLocalPosition(localX, localY);
			n.setLocalAngle(localAngle);
			nodeIndex++;
		}
		return node;
	}

	@Override
	public float[] copyFromObject(Node node, float[] x) {
		ArrayList<Node> nodes = NodeUtils.getArrayList(node);
		if (x == null)
			x = new float[nodes.size() * 3];
		int j = 0;
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);

			x[j] = n.getLocalX();
			x[j + 1] = n.getLocalY();
			x[j + 2] = n.getLocalAngle();

			j += 3;
		}
		return x;
	}
}