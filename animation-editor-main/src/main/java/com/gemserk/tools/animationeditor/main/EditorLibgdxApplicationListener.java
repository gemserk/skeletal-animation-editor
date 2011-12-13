package com.gemserk.tools.animationeditor.main;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.animation4j.timeline.TimelineAnimation;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.componentsengine.input.InputDevicesMonitorImpl;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.gemserk.tools.animationeditor.core.Animation;
import com.gemserk.tools.animationeditor.core.AnimationKeyFrame;
import com.gemserk.tools.animationeditor.core.Node;
import com.gemserk.tools.animationeditor.core.NodeImpl;
import com.gemserk.tools.animationeditor.core.NodeUtils;
import com.gemserk.tools.animationeditor.core.tree.AnimationEditor;
import com.gemserk.tools.animationeditor.core.tree.TreeEditor;

public class EditorLibgdxApplicationListener extends Game {

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

	SpriteBatch spriteBatch;

	Node nearNode;

	float nodeSize = 2f;
	float backgroundNodeSize = 4f;

	float selectDistance = 4f;

	Vector2 position = new Vector2();

	TreeEditor treeEditor;
	AnimationEditor animationEditor;

	private InputDevicesMonitorImpl<String> inputMonitor;

	public void setTreeEditor(TreeEditor treeEditor) {
		this.treeEditor = treeEditor;
	}

	public void setAnimationEditor(AnimationEditor animationEditor) {
		this.animationEditor = animationEditor;
	}

	interface EditorState {

		void update();
		
		void render();

	}

	class PlayingAnimationState implements EditorState {

		private Node root;
		private TimelineAnimation timelineAnimation;

		public PlayingAnimationState() {
			Animation currentAnimation = animationEditor.getCurrentAnimation();
			
			ArrayList<AnimationKeyFrame> keyFrames = currentAnimation.getKeyFrames();
			
			root = NodeUtils.cloneTree(treeEditor.getRoot());
			
			timelineAnimation = new TimelineAnimation(NodeUtils.getTimeline(root, keyFrames), (float)keyFrames.size() - 1);
			timelineAnimation.setSpeed(1f);
			timelineAnimation.setDelay(0f);
			timelineAnimation.start(0);
		}

		@Override
		public void update() {

			timelineAnimation.update(Gdx.graphics.getDeltaTime());
			
			if (!animationEditor.isPlayingAnimation()) {
				currentState = new NormalEditorState();
				return;
			}

		}

		@Override
		public void render() {
			renderNodeTree(root);
		}

	}

	class NormalEditorState implements EditorState {

		public void update() {

			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();

			position.set(nearNode.getX(), nearNode.getY());

			if (animationEditor.isPlayingAnimation()) {
				currentState = new PlayingAnimationState();
				return;
			}

			if (inputMonitor.getButton(Actions.RightMouseButton).isReleased())
				treeEditor.select(nearNode);

			if (inputMonitor.getButton(Actions.DeleteNodeButton).isReleased()) {
				if (!treeEditor.isSelectedNode(treeEditor.getRoot())) {
					if (treeEditor.getSelectedNode() != null)
						treeEditor.remove(treeEditor.getSelectedNode());
				}
			}

			if (inputMonitor.getButton(Actions.RotateButton).isPressed()) {
				currentState = new RotatingNodeState();
			}

			if (inputMonitor.getButton(Actions.LeftMouseButton).isPressed()) {
				if (position.dst(x, y) < selectDistance) {
					treeEditor.select(nearNode);
					currentState = new DraggingNodeState();
					return;
				}
			}

			if (inputMonitor.getButton(Actions.LeftMouseButton).isReleased()) {
				Node newNode = new NodeImpl("node-" + MathUtils.random(111, 999));
				treeEditor.add(newNode);
				newNode.setPosition(x, y);

				treeEditor.select(newNode.getParent());
			}

		}

		@Override
		public void render() {
			renderNodeTree(treeEditor.getRoot());
		}

	}

	class DraggingNodeState implements EditorState {

		private int x0;
		private int y0;

		public DraggingNodeState() {
			x0 = Gdx.input.getX();
			y0 = Gdx.graphics.getHeight() - Gdx.input.getY();
		}

		@Override
		public void update() {
			int x1 = Gdx.input.getX();
			int y1 = Gdx.graphics.getHeight() - Gdx.input.getY();

			if (inputMonitor.getButton(Actions.LeftMouseButton).isReleased()) {
				currentState = new NormalEditorState();
				return;
			}

			treeEditor.moveSelected(x1 - x0, y1 - y0);

			x0 = x1;
			y0 = y1;
		}

		@Override
		public void render() {
			renderNodeTree(treeEditor.getRoot());
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

			float rotation = (float) (currentY - y) * rotationSpeed;
			treeEditor.rotateSelected(rotation);

			// float currentAngle = selectedNode.getAngle();
			// selectedNode.setAngle(currentAngle + rotation);

			currentY = y;
		}

		@Override
		public void render() {
			renderNodeTree(treeEditor.getRoot());
		}

	}

	EditorState currentState;

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);

		Node root = new NodeImpl("root");
		root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);

		// selectedNode = root;

		currentState = new NormalEditorState();

		// nodes = new ArrayList<Node>();
		// nodes.add(root);

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

		// treeObserver.update(root);
		treeEditor.add(root);
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

		nearNode = treeEditor.getNearestNode(x, y);

		currentState.update();
	}

	private void realRender() {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

//		renderNodeTree(treeEditor.getRoot());
		
		currentState.render();
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

		if (treeEditor.isSelectedNode(node)) {
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

	// private Color getColor(Node node) {
	// if (treeEditor.isSelectedNode(node))
	// return Colors.selectedNodeColor;
	// // if (node == nearNode)
	// // return Colors.nearNodeColor;
	// return Colors.nodeColor;
	// }

}