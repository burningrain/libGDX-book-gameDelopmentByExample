package com.github.br.paper.airplane.level.level0;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.br.paper.airplane.gameworld.Res;
import com.github.br.paper.airplane.gameworld.ResMusic;
import com.github.br.paper.airplane.screen.loading.AssetsLoader;

public class Level0AssetsLoader implements AssetsLoader {

    @Override
    public void loadAssets(AssetManager assetManager) {
        assetManager.load(Res.SKIN, Skin.class);

        assetManager.load(Res.AIR_HERO_PNG, Texture.class);
        assetManager.load(Res.HERO_LIFE_COUNT_PNG, Texture.class);
        assetManager.load(Res.BULLETS_COUNT_PNG, Texture.class);

        assetManager.load(Res.PARTICLE_COIN_PNG, Texture.class);
        assetManager.load(Res.PARTICLE_COIN_P, ParticleEffect.class);

        assetManager.load(Res.PARTICLE_FIRE_BULLET_PNG, Texture.class);
        assetManager.load(Res.PARTICLE_FIRE_BULLET_P, ParticleEffect.class);

        assetManager.load(Res.PARTICLE_ELECTRO_BULLET_PNG, Texture.class);
        assetManager.load(Res.PARTICLE_ELECTRO_BULLET_P, ParticleEffect.class);

        assetManager.load(ResMusic.STONE_INSTRUMENTS, Music.class);
    }

    @Override
    public void unloadAssets(AssetManager assetManager) {
        assetManager.unload(Res.SKIN);

        assetManager.unload(Res.AIR_HERO_PNG);
        assetManager.unload(Res.HERO_LIFE_COUNT_PNG);
        assetManager.unload(Res.BULLETS_COUNT_PNG);

        assetManager.unload(Res.PARTICLE_COIN_PNG);
        assetManager.unload(Res.PARTICLE_COIN_P);

        assetManager.unload(Res.PARTICLE_FIRE_BULLET_PNG);
        assetManager.unload(Res.PARTICLE_FIRE_BULLET_P);

        assetManager.unload(Res.PARTICLE_ELECTRO_BULLET_PNG);
        assetManager.unload(Res.PARTICLE_ELECTRO_BULLET_P);

        assetManager.unload(ResMusic.STONE_INSTRUMENTS);
    }

}
