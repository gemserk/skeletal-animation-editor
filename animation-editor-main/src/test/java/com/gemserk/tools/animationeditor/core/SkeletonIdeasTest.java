package com.gemserk.tools.animationeditor.core;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.gemserk.animation4j.transitions.TransitionFloatArrayImpl;

public class SkeletonIdeasTest {

	public static class SkeletonAnimation {

		ArrayList<SkeletonKeyFrame> keyFrames;
		
		public SkeletonAnimation() {
			keyFrames = new ArrayList<SkeletonKeyFrame>();
		}
		
		public void addKeyFrame(SkeletonKeyFrame keyFrame) {
		}

	}

	class SkeletonModel {

		Map<String, SkeletonAnimation> animations;

	}

	public ArrayList<Node> getArrayList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		add(nodes, node);
		return nodes;
	}

	private void add(ArrayList<Node> nodes, Node node) {
		nodes.add(node);
		for (int i = 0; i < node.getChildren().size(); i++) {
			add(nodes, node.getChildren().get(i));
		}
	}

	private Node cloneTree(Node node) {
		NodeImpl clonedNode = new NodeImpl(node);

		ArrayList<Node> children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node child = children.get(i);
			Node clonedChild = cloneTree(child);
			clonedChild.setParent(clonedNode);
		}

		return clonedNode;
	}

	@Test
	public void someTest() {

		Node root = new NodeImpl("AAA", 50f, 100f, 45f);
		Node leaf1 = new NodeImpl("BBB", 25f, 25f, 20f);
		Node leaf2 = new NodeImpl("CCC", -25f, -35f, -40f);

		Node leaf11 = new NodeImpl("BBB.1", 75f, 75f, 70f);

		leaf1.setParent(root);
		leaf2.setParent(root);

		leaf11.setParent(leaf1);

		Node clonedRoot = cloneTree(root);

		assertThat(clonedRoot.getChildren().size(), IsEqual.equalTo(2));

	}

	@Test
	public void anotherTest() {

		Node root = new NodeImpl("AAA", 50f, 100f, 45f);
		Node leaf1 = new NodeImpl("BBB", 25f, 25f, 20f);
		Node leaf2 = new NodeImpl("CCC", -25f, -35f, -40f);

		Node leaf11 = new NodeImpl("BBB.1", 75f, 75f, 70f);

		leaf1.setParent(root);
		leaf2.setParent(root);

		leaf11.setParent(leaf1);

		ArrayList<Node> nodes = getArrayList(root);

		for (int i = 0; i < nodes.size(); i++) {
			System.out.println(nodes.get(i));
		}

	}

	@Test
	public void transitionTest() {

		NodeImpl root = new NodeImpl("AAA", 0f, 0f, 0f);
		NodeImpl leaf1 = new NodeImpl("BBB", 30f, 30f, 30f);
		NodeImpl leaf2 = new NodeImpl("CCC", 60f, 60f, 60f);

		leaf1.setParent(root);
		leaf2.setParent(root);

		SkeletonKeyFrame frame1 = new SkeletonKeyFrame(root);

		root.setPosition(-100f, -100f);
		
		SkeletonKeyFrame frame2 = new SkeletonKeyFrame(root);
		
		TransitionFloatArrayImpl transition = new TransitionFloatArrayImpl(frame1.values);
		transition.set(frame2.values, 5f);

		float[] x = transition.get();
		for (int i = 0; i < x.length; i++) {
			System.out.print("" + x[i] + ",");
		}
		System.out.println();

		transition.update(2.5f);

		x = transition.get();
		for (int i = 0; i < x.length; i++) {
			System.out.print("" + x[i] + ",");
		}
		System.out.println();
		
		transition.update(2.5f);

		x = transition.get();
		for (int i = 0; i < x.length; i++) {
			System.out.print("" + x[i] + ",");
		}
		System.out.println();

	}

}
