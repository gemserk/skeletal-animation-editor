package com.gemserk.tools.animationeditor.core;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.gemserk.animation4j.transitions.TransitionFloatArrayImpl;

public class SkeletonIdeasTest {

	class SkeletonKeyFrame {

		ArrayList<Node> nodes;
		float[] values;

		public SkeletonKeyFrame(ArrayList<Node> nodes) {
			this.nodes = nodes;
			values = new float[nodes.size() * 3];
			int j = 0;
			for (int i = 0; i < nodes.size(); i++) {
				Node node = nodes.get(i);
				values[j] = node.getX();
				values[j + 1] = node.getY();
				values[j + 2] = node.getAngle();
				j += 3;
			}
		}
		
	}

	class SkeletonAnimation {

		ArrayList<SkeletonKeyFrame> keyFrames;

	}

	class SkeletonModel {

		Map<String, SkeletonAnimation> animations;

	}

	private ArrayList<Node> getArrayList(Node node) {
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

		Node root = new NodeImpl("AAA", 50f, 100f, 45f);
		Node leaf1 = new NodeImpl("BBB", 25f, 25f, 20f);
		Node leaf2 = new NodeImpl("CCC", -25f, -35f, -40f);

		Node leaf11 = new NodeImpl("BBB.1", 75f, 75f, 70f);

		leaf1.setParent(root);
		leaf2.setParent(root);

		leaf11.setParent(leaf1);
		
		Node clonedRoot = cloneTree(root);

		ArrayList<Node> nodes = getArrayList(root);
		ArrayList<Node> nodes2 = getArrayList(clonedRoot);
		
		nodes2.get(2).setPosition(700f, 600f);
		
		SkeletonKeyFrame frame1 = new SkeletonKeyFrame(nodes);
		SkeletonKeyFrame frame2 = new SkeletonKeyFrame(nodes2);

		TransitionFloatArrayImpl transition = new TransitionFloatArrayImpl(nodes.size() * 3);
		transition.set(frame1.values);
		
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

	}

}
