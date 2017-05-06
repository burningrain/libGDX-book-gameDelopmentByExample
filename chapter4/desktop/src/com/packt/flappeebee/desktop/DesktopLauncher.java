package com.packt.flappeebee.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.br.ecs.simple.utils.ViewHelper;
import com.packt.flappeebee.FlappeeBeeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		config.height = (int) ViewHelper.WORLD_HEIGHT;
//		config.width = (int) ViewHelper.WORLD_WIDTH;
		new LwjglApplication(new FlappeeBeeGame(), config);
	}
}
