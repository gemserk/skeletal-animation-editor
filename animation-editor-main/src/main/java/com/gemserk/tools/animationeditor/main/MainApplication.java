package com.gemserk.tools.animationeditor.main;

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

public class MainApplication {

	protected static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

	// Ideas to detect the file is opened, but didnt work well.

	// public static boolean isFileOpened(File file) {
	// FileChannel channel = getFileChannel(file);
	//
	// FileLock lock = null;
	//
	// try {
	// lock = performChannelLock(channel);
	// } catch (OverlappingFileLockException e) {
	// return true;
	// } finally {
	// try {
	// channel.close();
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// releaseLock(lock);
	// }
	//
	// return false;
	// }
	//
	// private static FileChannel getFileChannel(File file) {
	// FileChannel channel;
	//
	// try {
	// channel = new RandomAccessFile(file, "rw").getChannel();
	// } catch (FileNotFoundException e) {
	// throw new RuntimeException(e);
	// }
	//
	// return channel;
	// }
	//
	// private static FileLock performChannelLock(FileChannel channel) {
	// try {
	// return channel.lock();
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// }
	//
	// private static void releaseLock(FileLock lock) {
	// if (lock == null)
	// return;
	// try {
	// lock.release();
	// } catch (ClosedChannelException e) {
	//
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// }

	static interface Task {

		boolean isDone();

		void update(float delta);

	}

	static abstract class TaskImpl implements Task {

		boolean done = false;

		@Override
		public boolean isDone() {
			return done;
		}

	}

	static class TaskDelayedImpl implements Task {

		Task task;
		float delay;

		public TaskDelayedImpl(Task task, float delay) {
			this.task = task;
			this.delay = delay;
		}

		@Override
		public boolean isDone() {
			return task.isDone();
		}

		@Override
		public void update(float delta) {
			if (delay < 0)
				// should call with delta - delay?
				task.update(delta);
			else
				delay -= delta;
		}

	}

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
					done = true;
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

			// filesMonitor.monitor(islandDataSource, resourceManager.get("IslandTexture"));

			filesMonitor.register(new FileMonitor(new FileInformationImpl(new File(islandDataSource.getUri())), new FileStatusChangedHandler() {
				@Override
				public void onFileModified(final File file) {

					reloadTask = new TaskDelayedImpl(new TaskImpl() {
						@Override
						public void update(float delta) {
							done = true;
							try {
								logger.debug("trying to reload resource");
								resourceManager.get("IslandTexture").unload();
//								islandSpriteResource.reload();
							} catch (GdxRuntimeException e) {
								logger.error("failed to reload resource, cant reload resource yet", e);
								reloadTask= new TaskDelayedImpl(this, 0.5f);
							}
						}
					}, 0.5f);

				}
			}));
			
			resourceStatusMonitor = new ResourceStatusMonitor(resourceManager.get("IslandTexture"));
			
			// filesMonitor.register(new FileMonitor(new FileInformationImpl(new File(islandDataSource.getUri())), new FileHandler() {
			// @Override
			// public void onFileModified(File file) {
			// islandSpriteResource.reload();
			// }
			// }));

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
