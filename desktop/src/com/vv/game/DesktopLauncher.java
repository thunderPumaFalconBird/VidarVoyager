package com.vv.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.vv.game.VidarVoyager;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(VidarVoyager.APP_FPS);
		config.setWindowedMode(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
		config.setTitle("VidarVoyager");
		new Lwjgl3Application(new VidarVoyager(), config);
	}
}
