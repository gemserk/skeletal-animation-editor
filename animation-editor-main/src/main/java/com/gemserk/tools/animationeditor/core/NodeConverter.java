package com.gemserk.tools.animationeditor.core;

import com.gemserk.animation4j.converters.TypeConverter;

public class NodeConverter implements TypeConverter<Node> {
	
	public static final NodeConverter instance = new NodeConverter();

	@Override
	public int variables() {
		return 3;
	}

	@Override
	public float[] copyFromObject(Node n, float[] x) {
		if (x == null)
			x = new float[variables()];
		x[0] = n.getLocalX();
		x[1] = n.getLocalY();
		x[2] = n.getLocalAngle();
		return x;
	}

	@Override
	public Node copyToObject(Node n, float[] x) {
		if (n == null)
			throw new IllegalArgumentException("cant copy to null node");
		n.setLocalPosition(x[0], x[1]);
		n.setLocalAngle(x[2]);
		return n;
	}

}
