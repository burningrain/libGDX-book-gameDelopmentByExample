package com.github.br.libgdx.games.nutty.birds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.github.br.libgdx.games.nutty.birds.screen.LoadingScreen;

public class NuttyBirdsGame extends Game {

	private AssetManager assetManager;

	@Override
	public void create() {
		assetManager = new AssetManager();

		Box2D.init();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		setScreen(new LoadingScreen(this));
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

}
