package com.gemserk.tools.animationeditor.core;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.gemserk.animation4j.timeline.KeyFrame;

public class SkeletonIdeasTest {

	public static class SkeletonAnimation {

		ArrayList<KeyFrame> keyFrames;
		
		public SkeletonAnimation() {
			keyFrames = new ArrayList<KeyFrame>();
		}
		
		public void addKeyFrame(KeyFrame keyFrame) {
			this.keyFrames.add(keyFrame);
		}

	}

	class SkeletonModel {

		Map<String, SkeletonAnimation> animations;

	}

	public ArrayList<Joint> getArrayList(Joint joint) {
		ArrayList<Joint> joints = new ArrayList<Joint>();
		add(joints, joint);
		return joints;
	}

	private void add(ArrayList<Joint> joints, Joint joint) {
		joints.add(joint);
		for (int i = 0; i < joint.getChildren().size(); i++) {
			add(joints, joint.getChildren().get(i));
		}
	}

	private Joint cloneTree(Joint joint) {
		JointImpl clonedNode = new JointImpl(joint);

		ArrayList<Joint> children = joint.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Joint child = children.get(i);
			Joint clonedChild = cloneTree(child);
			clonedChild.setParent(clonedNode);
		}

		return clonedNode;
	}

	@Test
	public void someTest() {

		Joint root = new JointImpl("AAA", 50f, 100f, 45f);
		Joint leaf1 = new JointImpl("BBB", 25f, 25f, 20f);
		Joint leaf2 = new JointImpl("CCC", -25f, -35f, -40f);

		Joint leaf11 = new JointImpl("BBB.1", 75f, 75f, 70f);

		leaf1.setParent(root);
		leaf2.setParent(root);

		leaf11.setParent(leaf1);

		Joint clonedRoot = cloneTree(root);

		assertThat(clonedRoot.getChildren().size(), IsEqual.equalTo(2));

	}

	@Test
	public void anotherTest() {

		Joint root = new JointImpl("AAA", 50f, 100f, 45f);
		Joint leaf1 = new JointImpl("BBB", 25f, 25f, 20f);
		Joint leaf2 = new JointImpl("CCC", -25f, -35f, -40f);

		Joint leaf11 = new JointImpl("BBB.1", 75f, 75f, 70f);

		leaf1.setParent(root);
		leaf2.setParent(root);

		leaf11.setParent(leaf1);

		ArrayList<Joint> joints = getArrayList(root);

		for (int i = 0; i < joints.size(); i++) {
			System.out.println(joints.get(i));
		}

	}

}
