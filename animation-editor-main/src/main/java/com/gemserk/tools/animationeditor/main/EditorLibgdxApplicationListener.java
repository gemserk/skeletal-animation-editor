package com.gemserk.tools.animationeditor.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class EditorLibgdxApplicationListener extends Game {
	
	@Override
	public void create() {
		Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);
	}

	@Override
	public void render() {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

}