package com.lofisoftware.vigilauntie.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lofisoftware.vigilauntie.VigilAuntie;

import static com.lofisoftware.vigilauntie.Constants.*;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SCREEN_WIDTH_CELLS*CELL_WIDTH*4;
		config.height = SCREEN_HEIGHT_CELLS*CELL_HEIGHT*4;
		config.useGL30 = false;
		config.addIcon("Tentacle-16.png", Files.FileType.Classpath);
		config.addIcon("Tentacle-32.png", Files.FileType.Classpath);
		config.addIcon("Tentacle-128.png", Files.FileType.Classpath);

		Application app = new LwjglApplication(new VigilAuntie(), config);
		Gdx.app.getGraphics().setTitle("RPG");
		Gdx.app = app;
		//Gdx.app.setLogLevel(Application.LOG_INFO);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Gdx.app.setLogLevel(Application.LOG_ERROR);
		//Gdx.app.setLogLevel(Application.LOG_NONE);
    }
}
