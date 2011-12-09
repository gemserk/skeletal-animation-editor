package com.gemserk.tools.animationeditor.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.tools.animationeditor.core.Bone;
import com.gemserk.tools.animationeditor.core.Node;

public class EditorLibgdxApplicationListener extends Game {

	private SpriteBatch spriteBatch;
	
	private Node root;

	@Override
	public void create() {
		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);

		root = new Bone();
		root.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		
		spriteBatch = new SpriteBatch();
		
	}

	@Override
	public void render() {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		spriteBatch.end();
		
		ImmediateModeRendererUtils.drawSolidCircle(root.getX(), root.getY(), 50f, Color.WHITE);
	}

}