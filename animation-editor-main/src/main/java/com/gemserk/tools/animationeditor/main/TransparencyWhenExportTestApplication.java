package com.gemserk.tools.animationeditor.main;

import java.io.File;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.gemserk.commons.gdx.ApplicationListenerGameStateBasedImpl;
import com.gemserk.commons.gdx.graphics.Mesh2d;
import com.gemserk.commons.gdx.graphics.Mesh2dBuilder;
import com.gemserk.util.ScreenshotSaver;

public class TransparencyWhenExportTestApplication {

	static class TransparencyWhenExportApplicationListener extends ApplicationListenerGameStateBasedImpl {

		private Mesh2d mesh;
		private Texture texture1;
		private Texture texture2;

		@Override
		public void create() {
			super.create();

			mesh = new Mesh2dBuilder() //
					.color(1f, 1f, 1f, 1f) //
					.texCoord(0f, 0f) //
					.vertex(0f, 0f) //
					.color(1f, 1f, 1f, 1f) //
					.texCoord(0f, 1f) //
					.vertex(0f, 0.5f) //
					.color(1f, 1f, 1f, 1f) //
					.texCoord(1f, 1f) //
					.vertex(0.5f, 0.5f) //

					.color(1f, 1f, 1f, 1f) //
					.texCoord(0f, 0f) //
					.vertex(0f, 0f) //
					.color(1f, 1f, 1f, 1f) //
					.texCoord(1f, 1f) //
					.vertex(0.5f, 0.5f) //
					.color(1f, 1f, 1f, 1f) //
					.texCoord(1f, 0f) //
					.vertex(0.5f, 0f) //

					.build();

			Texture.setEnforcePotImages(false);

			texture1 = new Texture(Gdx.files.internal("alphatest/body-top.png"));
			texture2 = new Texture(Gdx.files.internal("alphatest/head.png"));
		}

		@Override
		public void render() {
			super.render();

			// GL20 gl20 = Gdx.graphics.getGL20();

			// GL11.glClearColor(1f, 1f, 1f, 0f);
			// GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			// gl20.glDisable(GL10.GL_SCISSOR_TEST);
			//
			// gl20.glBlendFuncSeparate(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA, GL10.GL_ZERO, GL10.GL_ONE_MINUS_SRC_ALPHA);
			//
			// gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			// gl20.glClear(GL10.GL_COLOR_BUFFER_BIT);

			// Mesh2dRenderUtils.draw(GL11.GL_TRIANGLES, mesh);

			// GL11.glEnable(GL_BLEND);
			// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			// GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_SRC_ALPHA);

			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			GL11.glEnable(GL11.GL_BLEND);

			GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

			renderMesh(mesh, texture2, -0.3f, 0f);
			renderMesh(mesh, texture1, 0f, 0f);
			renderMesh(mesh, texture2, 0.2f, 0f);

			if (Gdx.input.justTouched()) {

				try {
					ScreenshotSaver.saveScreenshot(new File("/tmp/output.png"), true);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		private void renderMesh(Mesh2d mesh, Texture texture, float x, float y) {
			GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0f);
			renderMesh(mesh, texture);
			GL11.glPopMatrix();
		}

		private void renderMesh(Mesh2d mesh, Texture texture) {
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glVertexPointer(3, 0, mesh.getVertexArray());

			if (mesh.getColorArray() != null) {
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
				GL11.glColorPointer(4, 0, mesh.getColorArray());
			}

			if (mesh.getTexCoordArray() != null) {
				GL11.glEnable(GL11.GL_TEXTURE_2D);

				texture.bind();

				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glTexCoordPointer(2, 0, mesh.getTexCoordArray());
			}

			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.getVertexArray().limit());

			if (mesh.getTexCoordArray() != null) {
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
			}

			if (mesh.getColorArray() != null) {
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			}

			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}

	}

	public static void main(String[] args) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Transparency when export test";
		config.width = 640;
		config.height = 480;
		config.fullscreen = false;
		config.useGL20 = false;
		config.useCPUSynch = true;
		config.forceExit = true;
		config.vSyncEnabled = true;

		TransparencyWhenExportApplicationListener game = new TransparencyWhenExportApplicationListener();

		new LwjglApplication(game, config);
	}

}
