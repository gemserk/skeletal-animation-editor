package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;

import com.gemserk.animation4j.converters.TypeConverter;

public class SkeletonConverter implements TypeConverter<Skeleton> {

	public static SkeletonConverter instance = new SkeletonConverter();

	@Override
	public int variables() {
		throw new UnsupportedOperationException("cant return the number of variables of a variable tree");
	}

	public Skeleton copyToObject(Skeleton skeleton, float[] x) {
		ArrayList<Joint> joints = JointUtils.getArrayList(skeleton.getRoot());
		int nodeIndex = 0;
		for (int i = 0; i < x.length; i += 3) {
			Joint n = joints.get(nodeIndex);
			float localX = x[i + 0];
			float localY = x[i + 1];
			float localAngle = x[i + 2];
			n.setLocalPosition(localX, localY);
			n.setLocalAngle(localAngle);
			nodeIndex++;
		}
		return skeleton;
	}

	@Override
	public float[] copyFromObject(Skeleton skeleton, float[] x) {
		ArrayList<Joint> joints = JointUtils.getArrayList(skeleton.getRoot());
		if (x == null)
			x = new float[joints.size() * 3];
		int j = 0;
		for (int i = 0; i < joints.size(); i++) {
			Joint n = joints.get(i);

			x[j] = n.getLocalX();
			x[j + 1] = n.getLocalY();
			x[j + 2] = n.getLocalAngle();

			j += 3;
		}
		return x;
	}
}