package com.gemserk.tools.animationeditor.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MainApplication {
	
	private static final class TestApplicationListener extends Game {
		@Override
		public void create() {
			
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
