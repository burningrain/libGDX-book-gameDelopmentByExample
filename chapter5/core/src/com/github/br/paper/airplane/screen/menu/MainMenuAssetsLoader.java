package com.github.br.paper.airplane.screen.menu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.br.paper.airplane.gameworld.Res;
import com.github.br.paper.airplane.gameworld.ResMusic;
import com.github.br.paper.airplane.screen.loading.AssetsLoader;

public class MainMenuAssetsLoader implements AssetsLoader {

    @Override
    public void loadAssets(AssetManager assetManager) {
        assetManager.load(Res.SKIN, Skin.class);
        assetManager.load(ResMusic.STONE_14, Music.class);
    }

    @Override
    public void unloadAssets(AssetManager assetManager) {
        assetManager.unload(Res.SKIN);
        assetManager.unload(ResMusic.STONE_14);
    }

}
