package com.gemserk.tools.animationeditor.main;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.gemserk.animation4j.timeline.KeyFrame;
import com.gemserk.animation4j.timeline.Timeline;
import com.gemserk.animation4j.timeline.TimelineValue;
import com.gemserk.animation4j.timeline.TimelineValueMutableObjectImpl;
import com.gemserk.animation4j.transitions.TransitionFloatArrayImpl;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.tools.animationeditor.core.Node;
import com.gemserk.tools.animationeditor.core.NodeConverter;
import com.gemserk.tools.animationeditor.core.NodeImpl;
import com.gemserk.tools.animationeditor.core.TreeConverter;

public class AnimatedTreeExampleApplicationListener extends Game {

	private Node root;

	float nodeSize = 2f;

	private ArrayList<Node> nodes;

	private TransitionFloatArrayImpl transition;

	private KeyFrame keyFrame0;
	private KeyFrame keyFrame1;

	private Timeline timeline;

	private static class Colors {

		public static final Color lineColor = new Color(1f, 1f, 1f, 1f);
		public static final Color nodeColor = new Color(1f, 1f, 1f, 1f);
		public static final Color selectedNodeColor = new Color(0f, 0f, 1f, 1f);
		public static final Color nearNodeColor = new Color(1f, 0f, 0f, 1f);

	}

	ArrayList<Node> getArrayList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		add(nodes, node);
		return nodes;
	}

	void add(ArrayList<Node> nodes, Node node) {
		nodes.add(node);
		for (int i = 0; i < node.getChildren().size(); i++) {
			add(nodes, node.getChildren().get(i));
		}
	}

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);

		root = new NodeImpl("root");
		root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);

		Node node1 = new NodeImpl("nodo1", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.75f, 0f);
		Node node2 = new NodeImpl("nodo2", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.65f, 0f);

		node1.setParent(root);
		node2.setParent(node1);

		keyFrame0 = new KeyFrame(0f, TreeConverter.instance.copyFromObject(root, null));

		root.setAngle(180f);
		node1.setAngle(90f);

		keyFrame1 = new KeyFrame(1f, TreeConverter.instance.copyFromObject(root, null));

		nodes = getArrayList(root);

		transition = new TransitionFloatArrayImpl(keyFrame0.getValue());
		transition.set(keyFrame1.getValue(), keyFrame1.getTime() - keyFrame0.getTime());

		TimelineValueMutableObjectImpl<Node> timelineValue1 = new TimelineValueMutableObjectImpl<Node>(root, NodeConverter.instance);
		TimelineValueMutableObjectImpl<Node> timelineValue2 = new TimelineValueMutableObjectImpl<Node>(node1, NodeConverter.instance);
		TimelineValueMutableObjectImpl<Node> timelineValue3 = new TimelineValueMutableObjectImpl<Node>(node2, NodeConverter.instance);

		timelineValue1.addKeyFrame(new KeyFrame(0f, new float[] { 400f, 300f, 0f }));
		timelineValue1.addKeyFrame(new KeyFrame(1f, new float[] { 400f, 300f, 360f }));

		timelineValue2.addKeyFrame(new KeyFrame(0f, new float[] { 20f, 20f, 0f }));
		timelineValue3.addKeyFrame(new KeyFrame(0f, new float[] { 20f, 0f, 0f }));

		ArrayList<TimelineValue> values = new ArrayList<TimelineValue>();

		values.add(timelineValue1);
		values.add(timelineValue2);
		values.add(timelineValue3);

		timeline = new Timeline(values);
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

		if (Gdx.input.justTouched()) {
			timeline.move(MathUtils.random(0f, 1f));
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

	private void renderNodeTree(Node node) {
		renderNodeOnly(node);
		ArrayList<Node> children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node child = children.get(i);
			ImmediateModeRendererUtils.drawLine(node.getX(), node.getY(), child.getX(), child.getY(), Colors.lineColor);
			renderNodeTree(child);
		}
	}

	private void renderNodeOnly(Node node) {
		ImmediateModeRendererUtils.fillRectangle(node.getX() - nodeSize, node.getY() - nodeSize, node.getX() + nodeSize, node.getY() + nodeSize, //
				Colors.nodeColor);
	}

}