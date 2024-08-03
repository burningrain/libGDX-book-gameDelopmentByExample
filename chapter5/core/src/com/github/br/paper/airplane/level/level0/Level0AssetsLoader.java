package com.github.br.paper.airplane.level.level0;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.github.br.paper.airplane.gameworld.Res;
import com.github.br.paper.airplane.screen.loading.AssetsLoader;

public class Level0AssetsLoader implements AssetsLoader {



    @Override
    public void loadAssets(AssetManager assetManager) {
        assetManager.load(Res.AIR_HERO_PNG, Texture.class);
        assetManager.load(Res.PARTICLE_COIN_PNG, Texture.class);

        assetManager.load(Res.PARTICLE_COIN_COIN_P, ParticleEffect.class);
    }

    @Override
    public void unloadAssets(AssetManager assetManager) {
        assetManager.unload(Res.AIR_HERO_PNG);
        assetManager.unload(Res.PARTICLE_COIN_COIN_P);
    }

}
