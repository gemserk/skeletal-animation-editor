package com.gemserk.tools.animationeditor.main;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.componentsengine.input.InputDevicesMonitorImpl;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.gemserk.tools.animationeditor.core.Bone;
import com.gemserk.tools.animationeditor.core.Node;

public class EditorLibgdxApplicationListener extends Game {

	SpriteBatch spriteBatch;

	Node root;
	Node selectedNode;
	Node nearNode;

	ArrayList<Node> nodes;

	float nodeSize = 2f;
	float backgroundNodeSize = 4f;
	
	float selectDistance = 4f;

	Vector2 position = new Vector2();

	private InputDevicesMonitorImpl<String> inputMonitor;

	private static class Colors {

		public static final Color lineColor = new Color(1f, 1f, 1f, 1f);
		public static final Color nodeColor = new Color(1f, 1f, 1f, 1f);
		public static final Color selectedNodeColor = new Color(0f, 0f, 1f, 1f);
		public static final Color nearNodeColor = new Color(1f, 0f, 0f, 1f);

	}

	private static class Actions {

		public static final String LeftMouseButton = "leftMouseButton";
		public static final String RightMouseButton = "rightMouseButton";

	}

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);

		root = new Bone();
		root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);

		selectedNode = root;

		nodes = new ArrayList<Node>();
		nodes.add(root);

		spriteBatch = new SpriteBatch();

		inputMonitor = new InputDevicesMonitorImpl<String>();

		new LibgdxInputMappingBuilder<String>(inputMonitor, Gdx.input) {
			{
				monitorMouseLeftButton(Actions.LeftMouseButton);
				monitorMouseRightButton(Actions.RightMouseButton);
			}
		};

	}

	@Override
	public void render() {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		inputMonitor.update();

		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();

		if (inputMonitor.getButton(Actions.RightMouseButton).isReleased()) {
			selectedNode = nearNode;
		}
		
		if (inputMonitor.getButton(Actions.LeftMouseButton).isReleased()) {

			position.set(nearNode.getX(), nearNode.getY());

			if (position.dst(x, y) < selectDistance) {
				selectedNode = nearNode;
			} else {
				Node newNode = new Bone();
				newNode.setParent(selectedNode);
				newNode.setPosition(x, y);
				selectedNode = newNode;
				nodes.add(newNode);
			}
		}

		nearNode = updateNearNode(x, y);

		spriteBatch.begin();
		spriteBatch.end();

		renderNodeTree(root);
		// renderNodeOnly(nearNode);
		// renderNodeOnly(selectedNode);
	}

	private Node updateNearNode(int x, int y) {

		Node nearNode = root;

		position.set(nearNode.getX(), nearNode.getY());

		float minDistance = position.dst(x, y);

		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			position.set(node.getX(), node.getY());

			if (position.dst(x, y) < minDistance) {
				minDistance = position.dst(x, y);
				nearNode = node;
			}
		}

		return nearNode;
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
		if (node == nearNode) {
			ImmediateModeRendererUtils.fillRectangle(node.getX() - backgroundNodeSize, //
					node.getY() - backgroundNodeSize, //
					node.getX() + backgroundNodeSize, //
					node.getY() + backgroundNodeSize, //
					Colors.nearNodeColor);
		}

		if (node == selectedNode) {
			float backgroundNodeSize = 4f;
			ImmediateModeRendererUtils.fillRectangle(node.getX() - backgroundNodeSize, //
					node.getY() - backgroundNodeSize, //
					node.getX() + backgroundNodeSize, //
					node.getY() + backgroundNodeSize, //
					Colors.selectedNodeColor);
		}

		ImmediateModeRendererUtils.fillRectangle(node.getX() - nodeSize, node.getY() - nodeSize, node.getX() + nodeSize, node.getY() + nodeSize, //
				Colors.nodeColor);
	}

	private Color getColor(Node node) {
		if (node == selectedNode)
			return Colors.selectedNodeColor;
		// if (node == nearNode)
		// return Colors.nearNodeColor;
		return Colors.nodeColor;
	}

}