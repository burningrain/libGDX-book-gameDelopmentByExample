package com.github.br.gdx.simple.structure.screen.loading;

import com.badlogic.gdx.assets.AssetManager;

public interface AssetsLoader {

    AssetsLoader EMPTY_ASSET_LOADER = new AssetsLoader() {
        @Override
        public void loadAssets(AssetManager assetManager) {
        }

        @Override
        public void unloadAssets(AssetManager assetManager) {
        }
    };

    void loadAssets(AssetManager assetManager);

    void unloadAssets(AssetManager assetManager);

}
