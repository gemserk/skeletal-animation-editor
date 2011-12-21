package com.gemserk.tools.animationeditor.converters;

import com.gemserk.animation4j.converters.TypeConverter;
import com.gemserk.tools.animationeditor.core.Joint;

public class JointConverter implements TypeConverter<Joint> {
	
	public static final JointConverter instance = new JointConverter();

	@Override
	public int variables() {
		return 3;
	}

	@Override
	public float[] copyFromObject(Joint n, float[] x) {
		if (x == null)
			x = new float[variables()];
		x[0] = n.getLocalX();
		x[1] = n.getLocalY();
		x[2] = n.getLocalAngle();
		return x;
	}

	@Override
	public Joint copyToObject(Joint n, float[] x) {
		if (n == null)
			throw new IllegalArgumentException("cant copy to null node");
		n.setLocalPosition(x[0], x[1]);
		n.setLocalAngle(x[2]);
		return n;
	}

}
