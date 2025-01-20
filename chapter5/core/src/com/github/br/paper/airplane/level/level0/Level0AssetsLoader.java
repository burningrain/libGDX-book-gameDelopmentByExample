package com.github.br.paper.airplane.level.level0;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.br.paper.airplane.gameworld.Res;
import com.github.br.paper.airplane.screen.loading.AssetsLoader;

public class Level0AssetsLoader implements AssetsLoader {

    @Override
    public void loadAssets(AssetManager assetManager) {
        assetManager.load(Res.Skins.SKIN, Skin.class);

        assetManager.load(Res.Pictures.BACKGROUND_PNG, Texture.class);
        assetManager.load(Res.Pictures.AIR_HERO_PNG, Texture.class);
        assetManager.load(Res.HUD.HERO_LIFE_COUNT_PNG, Texture.class);
        assetManager.load(Res.HUD.BULLETS_COUNT_PNG, Texture.class);

        assetManager.load(Res.Particles.PARTICLE_COIN_PNG, Texture.class);
        assetManager.load(Res.Particles.PARTICLE_COIN_P, ParticleEffect.class);
        assetManager.load(Res.Particles.PARTICLE_BULLET_TYPE_P, ParticleEffect.class);

        assetManager.load(Res.Particles.PARTICLE_FIRE_BULLET_PNG, Texture.class);
        assetManager.load(Res.Particles.PARTICLE_FIRE_BULLET_P, ParticleEffect.class);
        assetManager.load(Res.Particles.PARTICLE_ELECTRO_BULLET_PNG, Texture.class);
        assetManager.load(Res.Particles.PARTICLE_ELECTRO_BULLET_P, ParticleEffect.class);
        assetManager.load(Res.Particles.PARTICLE_VENOM_BULLET_PNG, Texture.class);
        assetManager.load(Res.Particles.PARTICLE_VENOM_BULLET_P, ParticleEffect.class);

        assetManager.load(Res.Particles.PARTICLE_SMOKE_PARTICLE_CLOUD_PNG, Texture.class);
        assetManager.load(Res.Particles.PARTICLE_SMOKE_FAST_FADE_OUT_P, ParticleEffect.class);

        assetManager.load(Res.Particles.PARTICLE_FIRE_PNG, Texture.class);
        assetManager.load(Res.Particles.PARTICLE_FIRE_P, ParticleEffect.class);

        assetManager.load(Res.Shaders.SHADER_COSMOS_BACKGROUND, ShaderProgram.class);

        assetManager.load(Res.Music.STONE_INSTRUMENTS, Music.class);
    }

    @Override
    public void unloadAssets(AssetManager assetManager) {
        assetManager.unload(Res.Skins.SKIN);

        assetManager.unload(Res.Pictures.BACKGROUND_PNG);
        assetManager.unload(Res.Pictures.AIR_HERO_PNG);
        assetManager.unload(Res.HUD.HERO_LIFE_COUNT_PNG);
        assetManager.unload(Res.HUD.BULLETS_COUNT_PNG);

        assetManager.unload(Res.Particles.PARTICLE_COIN_PNG);
        assetManager.unload(Res.Particles.PARTICLE_COIN_P);
        assetManager.unload(Res.Particles.PARTICLE_BULLET_TYPE_P);

        assetManager.unload(Res.Particles.PARTICLE_FIRE_BULLET_PNG);
        assetManager.unload(Res.Particles.PARTICLE_FIRE_BULLET_P);
        assetManager.unload(Res.Particles.PARTICLE_ELECTRO_BULLET_PNG);
        assetManager.unload(Res.Particles.PARTICLE_ELECTRO_BULLET_P);
        assetManager.unload(Res.Particles.PARTICLE_VENOM_BULLET_PNG);
        assetManager.unload(Res.Particles.PARTICLE_VENOM_BULLET_P);

        assetManager.unload(Res.Particles.PARTICLE_SMOKE_PARTICLE_CLOUD_PNG);
        assetManager.unload(Res.Particles.PARTICLE_SMOKE_FAST_FADE_OUT_P);

        assetManager.unload(Res.Particles.PARTICLE_FIRE_PNG);
        assetManager.unload(Res.Particles.PARTICLE_FIRE_P);

        assetManager.unload(Res.Shaders.SHADER_COSMOS_BACKGROUND);

        assetManager.unload(Res.Music.STONE_INSTRUMENTS);
    }

}
