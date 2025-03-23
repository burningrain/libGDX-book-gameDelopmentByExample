package com.packt.flappeebee.screen.level.level1;

import com.badlogic.gdx.assets.AssetManager;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.structure.screen.loading.AssetsLoader;
import com.packt.flappeebee.Resources;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;

public class Level1AssetLoader implements AssetsLoader {

    @Override
    public void loadAssets(AssetManager assetManager) {
        assetManager.load(Resources.HyperLap2D.PROJECT, AsyncResourceManager.class);

        assetManager.load(Resources.Animations.CRAB_ANIM_FSM, SimpleAnimation.class);
    }

    @Override
    public void unloadAssets(AssetManager assetManager) {
        assetManager.unload(Resources.HyperLap2D.PROJECT);

        assetManager.unload(Resources.Animations.CRAB_ANIM_FSM);
    }

}
