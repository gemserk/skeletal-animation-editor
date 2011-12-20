package com.gemserk.tools.animationeditor.main;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.gemserk.animation4j.timeline.KeyFrame;
import com.gemserk.animation4j.timeline.Timeline;
import com.gemserk.animation4j.transitions.TimeTransition;
import com.gemserk.animation4j.transitions.TransitionFloatArrayImpl;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.tools.animationeditor.core.SkeletonAnimation;
import com.gemserk.tools.animationeditor.core.AnimationUtils;
import com.gemserk.tools.animationeditor.core.Joint;
import com.gemserk.tools.animationeditor.core.JointImpl;
import com.gemserk.tools.animationeditor.core.JointUtils;
import com.gemserk.tools.animationeditor.core.Skeleton;

public class AnimatedTreeExampleApplicationListener extends Game {

	private Joint root;

	float nodeSize = 2f;

	private ArrayList<Joint> joints;

	private TransitionFloatArrayImpl transition;

	private KeyFrame keyFrame0;
	private KeyFrame keyFrame1;

	private Timeline timeline;

	private TimeTransition timeTransition;

	private static class Colors {

		public static final Color lineColor = new Color(1f, 1f, 1f, 1f);
		public static final Color nodeColor = new Color(1f, 1f, 1f, 1f);
		public static final Color selectedNodeColor = new Color(0f, 0f, 1f, 1f);
		public static final Color nearNodeColor = new Color(1f, 0f, 0f, 1f);

	}

	ArrayList<Joint> getArrayList(Joint joint) {
		ArrayList<Joint> joints = new ArrayList<Joint>();
		add(joints, joint);
		return joints;
	}

	void add(ArrayList<Joint> joints, Joint joint) {
		joints.add(joint);
		for (int i = 0; i < joint.getChildren().size(); i++) {
			add(joints, joint.getChildren().get(i));
		}
	}

//	Timeline getTimeline(Node root, Node... states) {
//		ArrayList<Node> nodes = NodeUtils.getArrayList(root);
//
//		ArrayList<TimelineValue> timelineValues = new ArrayList<TimelineValue>();
//
//		for (int j = 0; j < nodes.size(); j++) {
//			Node node = nodes.get(j);
//
//			TimelineValue timelineValue = new TimelineValueMutableObjectImpl<Node>(node, NodeConverter.instance);
//
//			float time = 0f;
//			
//			for (int i = 0; i < states.length; i++) {
//				Node treeState = states[i];
//				Node nodeState = treeState.getChild(node.getId());
//
//				timelineValue.addKeyFrame(new KeyFrame(time, NodeConverter.instance.copyFromObject(nodeState, null)));
//				time += 1f;
//				
//				timelineValues.add(timelineValue);
//			}
//
//		}
//
//		return new Timeline(timelineValues);
//	}

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);

		root = new JointImpl("root");
		root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);

		Joint node1 = new JointImpl("nodo1", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.75f, 0f);
		Joint node2 = new JointImpl("nodo2", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.65f, 0f);

		node1.setParent(root);
		node2.setParent(node1);

		Joint state0 = JointUtils.cloneTree(root);
		Joint state1 = JointUtils.cloneTree(root);

		state1.setAngle(45f);
		state1.getChildren().get(0).setAngle(180f);

		// keyFrame0 = new KeyFrame(0f, TreeConverter.instance.copyFromObject(root, null));
		//
		// root.setAngle(180f);
		// node1.setAngle(90f);
		//
		// keyFrame1 = new KeyFrame(1f, TreeConverter.instance.copyFromObject(root, null));
		//
		// nodes = getArrayList(root);
		//
		// transition = new TransitionFloatArrayImpl(keyFrame0.getValue());
		// transition.set(keyFrame1.getValue(), keyFrame1.getTime() - keyFrame0.getTime());
		
		SkeletonAnimation skeletonAnimation = new SkeletonAnimation();
		
		skeletonAnimation.getKeyFrames().add(AnimationUtils.keyFrame("keyframe0", new Skeleton(state0), 0f));
		skeletonAnimation.getKeyFrames().add(AnimationUtils.keyFrame("keyframe1", new Skeleton(state1), 1f));

		timeline = JointUtils.getTimeline(root, skeletonAnimation.getKeyFrames());

		// TimelineValueMutableObjectImpl<Node> timelineValue1 = new TimelineValueMutableObjectImpl<Node>(root, NodeConverter.instance);
		// TimelineValueMutableObjectImpl<Node> timelineValue2 = new TimelineValueMutableObjectImpl<Node>(node1, NodeConverter.instance);
		// TimelineValueMutableObjectImpl<Node> timelineValue3 = new TimelineValueMutableObjectImpl<Node>(node2, NodeConverter.instance);
		//
		// timelineValue1.addKeyFrame(new KeyFrame(0f, new float[] { 400f, 300f, 0f }));
		// timelineValue1.addKeyFrame(new KeyFrame(1f, new float[] { 400f, 300f, 360f }));
		//
		// timelineValue2.addKeyFrame(new KeyFrame(0f, new float[] { 20f, 20f, 0f }));
		// timelineValue3.addKeyFrame(new KeyFrame(0f, new float[] { 20f, 0f, 0f }));
		//
		// ArrayList<TimelineValue> values = new ArrayList<TimelineValue>();
		//
		// values.add(timelineValue1);
		// values.add(timelineValue2);
		// values.add(timelineValue3);
		//
		// timeline = new Timeline(values);
		
		timeTransition = new TimeTransition();
		timeTransition.start(1f);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		System.out.println("game.resize " + width + "x" + height);
	}

	@Override
	public void render() {
		realUpdate();
		realRender();
	}

	private void realUpdate() {
		
		timeTransition.update(Gdx.graphics.getDeltaTime());
		timeline.move(timeTransition.get());
		
		if (Gdx.input.justTouched()) {
			timeTransition.start(1f);
		}

		// transition.update(Gdx.graphics.getDeltaTime());
		//
		// if (transition.isFinished()) {
		// if (Gdx.input.justTouched()) {
		// transition.set(keyFrame0.getValue());
		// transition.set(keyFrame1.getValue(), keyFrame1.getTime() - keyFrame0.getTime());
		// }
		// return;
		// }
		//
		// float[] x = transition.get();

		// TreeConverter.instance.copyToObject(root, x);
	}

	private void realRender() {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		renderNodeTree(root);
	}

	private void renderNodeTree(Joint joint) {
		renderNodeOnly(joint);
		ArrayList<Joint> children = joint.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Joint child = children.get(i);
			ImmediateModeRendererUtils.drawLine(joint.getX(), joint.getY(), child.getX(), child.getY(), Colors.lineColor);
			renderNodeTree(child);
		}
	}

	private void renderNodeOnly(Joint joint) {
		ImmediateModeRendererUtils.fillRectangle(joint.getX() - nodeSize, joint.getY() - nodeSize, joint.getX() + nodeSize, joint.getY() + nodeSize, //
				Colors.nodeColor);
	}

}