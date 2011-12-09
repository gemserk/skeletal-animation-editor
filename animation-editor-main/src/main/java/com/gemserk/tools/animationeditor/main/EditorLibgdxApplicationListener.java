package com.gemserk.tools.animationeditor.main;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.componentsengine.input.InputDevicesMonitorImpl;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.gemserk.tools.animationeditor.core.Bone;
import com.gemserk.tools.animationeditor.core.Node;

public class EditorLibgdxApplicationListener extends Game {

	private SpriteBatch spriteBatch;

	private Node root;
	private Node selectedNode;
	
	float nodeSize = 3f;

	private InputDevicesMonitorImpl<String> inputMonitor;
	
	private static class Colors {
		
		public static final Color lineColor = new Color(1f, 1f, 1f, 1f);
		public static final Color nodeColor = new Color(1f, 1f, 1f, 1f);
		public static final Color selectedNodeColor = new Color(0f, 0f, 1f, 1f);
		
	}
	
	private static class Actions { 
		
		public static final String LeftMouseButton = "leftMouseButton";
		
	}

	@Override
	public void create() {
		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);

		root = new Bone();
		root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		
		selectedNode = root;

		spriteBatch = new SpriteBatch();
		
		inputMonitor = new InputDevicesMonitorImpl<String>();
		
		new LibgdxInputMappingBuilder<String>(inputMonitor, Gdx.input) {{
			monitorMouseLeftButton(Actions.LeftMouseButton);
		}};
		
	}

	@Override
	public void render() {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		inputMonitor.update();
		
		if (inputMonitor.getButton(Actions.LeftMouseButton).isReleased()) {
			Bone bone = new Bone();
			bone.setParent(selectedNode);
			bone.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			selectedNode = bone;
		}

		spriteBatch.begin();
		spriteBatch.end();

		renderNodeTree(root);
		renderNodeOnly(selectedNode);
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
				node == selectedNode ? Colors.selectedNodeColor : Colors.nodeColor);
	}

}