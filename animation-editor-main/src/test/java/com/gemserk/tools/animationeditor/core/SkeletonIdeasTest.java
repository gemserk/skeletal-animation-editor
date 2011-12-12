package com.gemserk.tools.animationeditor.core;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;


public class SkeletonIdeasTest {
	
	class SkeletonKeyFrame {
		
		ArrayList<Node> nodes;
		
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
	
	Node tree;
	
	

	@Test
	public void someTest() {
		
		
		
	}

}
