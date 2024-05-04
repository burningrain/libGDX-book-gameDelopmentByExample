package com.github.br.libgdx.games.nutty.birds;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(960, 544);
		config.setForegroundFPS(60);
		config.setTitle("Nutty Birds");
		new Lwjgl3Application(new NuttyBirdsGame(), config);
	}

}
