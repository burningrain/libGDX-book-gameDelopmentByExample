package com.packt.flappeebee.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.packt.flappeebee.FlappeeBeeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("crab");

		//config.width = 1024;
		//config.height = 768;
		//config.fullscreen = true;
		//config.samples = 3;
		//config.setFullscreenMode(Graphics.DisplayMode.);
		new Lwjgl3Application(new FlappeeBeeGame(), config);
	}
}
