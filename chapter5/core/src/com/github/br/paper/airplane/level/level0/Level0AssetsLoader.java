package com.github.br.paper.airplane.level.level0;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.github.br.paper.airplane.screen.loading.AssetsLoader;

public class Level0AssetsLoader implements AssetsLoader {

    @Override
    public void loadAssets(AssetManager assetManager) {
        assetManager.load("badlogic.jpg", Texture.class);
    }

    @Override
    public void unloadAssets(AssetManager assetManager) {
        assetManager.unload("badlogic.jpg");
    }

}
