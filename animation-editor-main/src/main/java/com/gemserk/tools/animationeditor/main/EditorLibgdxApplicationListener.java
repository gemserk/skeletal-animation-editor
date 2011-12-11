package com.gemserk.tools.animationeditor.main;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.componentsengine.input.InputDevicesMonitorImpl;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.gemserk.tools.animationeditor.core.NodeImpl;
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
		public static final String DeleteNodeButton = "deleteNodeButton";
		public static final String RotateButton = "rotateButton";

	}

	interface EditorState {

		void update();

	}

	class NormalEditorState implements EditorState {

		public void update() {

			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();

			position.set(nearNode.getX(), nearNode.getY());

			if (inputMonitor.getButton(Actions.RightMouseButton).isReleased()) {
				selectedNode = nearNode;
			}

			if (inputMonitor.getButton(Actions.DeleteNodeButton).isReleased()) {
				if (selectedNode != root) {
					Node parent = selectedNode.getParent();
					parent.getChildren().remove(selectedNode);
					nodes.remove(selectedNode);
					selectedNode = parent;
				}
			}
			
			if (inputMonitor.getButton(Actions.RotateButton).isPressed()) {
				currentState = new RotatingNodeState();
			}

			if (inputMonitor.getButton(Actions.LeftMouseButton).isPressed()) {
				if (position.dst(x, y) < selectDistance) {
					selectedNode = nearNode;
					currentState = new DraggingNodeState();
					return;
				}
			}

			if (inputMonitor.getButton(Actions.LeftMouseButton).isReleased()) {
				Node newNode = new NodeImpl();
				newNode.setParent(selectedNode);
				newNode.setPosition(x, y);
				selectedNode = newNode;
				nodes.add(newNode);
			}

		}

	}

	class DraggingNodeState implements EditorState {

		@Override
		public void update() {

			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();

			if (inputMonitor.getButton(Actions.LeftMouseButton).isHolded()) {
				selectedNode.setPosition(x, y);
			}

			if (inputMonitor.getButton(Actions.LeftMouseButton).isReleased()) {
				currentState = new NormalEditorState();
			}

		}

	}
	
	class RotatingNodeState implements EditorState {
		
		private int currentY;
		float rotationSpeed = 1f;

		public RotatingNodeState() {
			currentY = Gdx.graphics.getHeight() - Gdx.input.getY();
		}

		@Override
		public void update() {

			int y = Gdx.graphics.getHeight() - Gdx.input.getY();

			if (inputMonitor.getButton(Actions.RotateButton).isReleased()) {
				currentState = new NormalEditorState();
				return;
			}
			
			float currentAngle = selectedNode.getAngle();
			float rotation = (float) (currentY - y) * rotationSpeed;
			selectedNode.setAngle(currentAngle + rotation);
			
			currentY = y;
		}

	}

	EditorState currentState;

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);

		root = new NodeImpl();
		root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);

		selectedNode = root;

		currentState = new NormalEditorState();

		nodes = new ArrayList<Node>();
		nodes.add(root);

		spriteBatch = new SpriteBatch();

		inputMonitor = new InputDevicesMonitorImpl<String>();

		new LibgdxInputMappingBuilder<String>(inputMonitor, Gdx.input) {
			{
				monitorMouseLeftButton(Actions.LeftMouseButton);
				monitorMouseRightButton(Actions.RightMouseButton);
				monitorKeys(Actions.DeleteNodeButton, Keys.DEL, Keys.BACKSPACE);
				
				monitorKey(Actions.RotateButton, Keys.CONTROL_LEFT);
			}
		};

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
		inputMonitor.update();

		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();

		nearNode = updateNearNode(x, y);

		currentState.update();

	}

	private void realRender() {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.end();

		renderNodeTree(root);
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