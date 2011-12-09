package com.gemserk.tools.resourcemanager.tests;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gemserk.commons.gdx.resources.LibgdxResourceBuilder;
import com.gemserk.commons.tasks.Task;
import com.gemserk.commons.tasks.TaskDelayedImpl;
import com.gemserk.commons.tasks.TaskImpl;
import com.gemserk.componentsengine.input.ButtonMonitor;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.gemserk.resources.Resource;
import com.gemserk.resources.ResourceManager;
import com.gemserk.resources.ResourceManagerImpl;
import com.gemserk.resources.datasources.ClassPathDataSource;
import com.gemserk.resources.monitor.FileInformationImpl;
import com.gemserk.resources.monitor.FileMonitor;
import com.gemserk.resources.monitor.FilesMonitor;
import com.gemserk.resources.monitor.FilesMonitorImpl;
import com.gemserk.resources.monitor.ResourceStatusMonitor;
import com.gemserk.resources.monitor.handlers.FileStatusChangedHandler;

public class ReloadResourcesTestApplication {

	protected static final Logger logger = LoggerFactory.getLogger(ReloadResourcesTestApplication.class);

	private static final class TestApplicationListener extends Game {

		ResourceManager<String> resourceManager;
		ButtonMonitor buttonMonitor;
		SpriteBatch spriteBatch;
		Resource<Sprite> islandSpriteResource;
		FilesMonitor filesMonitor;

		Task reloadTask;
		private ResourceStatusMonitor resourceStatusMonitor;

		@Override
		public void create() {

			reloadTask = new TaskImpl() {
				public void update(float delta) {
					setDone(true);
				};
			};

			resourceManager = new ResourceManagerImpl<String>();

			new LibgdxResourceBuilder(resourceManager) {
				{
					texture("IslandTexture", "data/island01.png");
					sprite("IslandSprite", "IslandTexture");
				}
			};

			buttonMonitor = LibgdxInputMappingBuilder.keyButtonMonitor(Gdx.input, Keys.NUM_1);

			spriteBatch = new SpriteBatch();

			islandSpriteResource = resourceManager.get("IslandSprite");

			filesMonitor = new FilesMonitorImpl();

			ClassPathDataSource islandDataSource = new ClassPathDataSource("data/island01.png");

			filesMonitor.register(new FileMonitor(new FileInformationImpl(new File(islandDataSource.getUri())), new FileStatusChangedHandler() {
				@Override
				public void onFileModified(final File file) {

					reloadTask = new TaskDelayedImpl(new TaskImpl() {
						@Override
						public void update(float delta) {
							setDone(true);
							try {
								logger.debug("trying to reload resource");
								resourceManager.get("IslandTexture").unload();
							} catch (GdxRuntimeException e) {
								logger.error("failed to reload resource, cant reload resource yet", e);
								reloadTask = new TaskDelayedImpl(this, 0.5f);
							}
						}
					}, 0.5f);

				}
			}));

			resourceStatusMonitor = new ResourceStatusMonitor(resourceManager.get("IslandTexture"));

			Gdx.graphics.getGL10().glClearColor(0f, 0f, 0f, 1f);

		}

		@Override
		public void render() {
			Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

			buttonMonitor.update();

			filesMonitor.checkModifiedFiles();

			resourceStatusMonitor.checkChanges();

			if (resourceStatusMonitor.wasUnloaded()) {
				logger.debug("island texture was unloaded, unloading sprite resource");
				islandSpriteResource.unload();
			}

			if (resourceStatusMonitor.wasLoaded()) {
				logger.debug("island texture was loaded, reloading sprite resource");
				islandSpriteResource.reload();
			}

			if (buttonMonitor.isReleased()) {
				resourceManager.get("IslandTexture").reload();
				islandSpriteResource.reload();
			}

			if (!reloadTask.isDone())
				reloadTask.update(Gdx.graphics.getDeltaTime());

			spriteBatch.begin();
			islandSpriteResource.get().draw(spriteBatch);
			spriteBatch.end();

		}

	}

	public static void main(String[] args) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.fullscreen = false;
		config.useGL20 = false;
		config.useCPUSynch = true;
		config.forceExit = true;
		config.vSyncEnabled = true;
		config.width = 800;
		config.height = 600;
		config.title = "Gemserk's Tools - Animation Editor";

		new LwjglApplication(new TestApplicationListener(), config);

	}

}
