package com.packt.flappeebee.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.packt.flappeebee.FlappeeBeeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "crab";

		config.width = 1024;
		config.height = 768;
		//config.fullscreen = true;
		config.samples = 3;
		new LwjglApplication(new FlappeeBeeGame(), config);
	}
}
