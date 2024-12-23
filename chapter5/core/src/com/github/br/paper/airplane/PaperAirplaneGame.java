package com.github.br.paper.airplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.br.paper.airplane.audio.AudioAssetManager;
import com.github.br.paper.airplane.screen.GameScreens;
import com.github.br.paper.airplane.screen.loading.SimpleProgressBarImpl;
import com.github.br.paper.airplane.screen.loading.LoadingScreen;
import com.ray3k.stripe.FreeTypeSkinLoader;


public class PaperAirplaneGame extends ApplicationAdapter {

    private GameSettings gameSettings;
    private AudioSettings audioSettings;
    private Utils utils;
    private AssetManager assetManager;
    private AudioAssetManager audioAssetManager;
    private GameManager gameManager;

    @Override
    public void create() {
        gameSettings = createGameSettings();
        audioSettings = createAudioSettings();
        utils = new Utils(gameSettings);

        InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
        assetManager = new AssetManager();
        audioAssetManager = new AudioAssetManager(assetManager, audioSettings);
        initLoaders(fileHandleResolver);
        Box2D.init();

        gameManager = new GameManager();
        gameManager.init(
                gameSettings,
                audioSettings,
                utils,
                assetManager,
                audioAssetManager,
                new LoadingScreen(
                        new SimpleProgressBarImpl(
                                gameSettings.getProgressBarWidth(),
                                gameSettings.getProgressBarHeight(),
                                (gameSettings.getVirtualScreenWidth() - gameSettings.getProgressBarWidth()) / 2,
                                (gameSettings.getVirtualScreenHeight() - gameSettings.getProgressBarHeight()) / 2
                        ),
                        gameSettings.getVirtualScreenWidth(),
                        gameSettings.getVirtualScreenHeight()
                ),
                GameScreens.MAIN_MENU
        );
    }

    private void initLoaders(InternalFileHandleResolver fileHandleResolver) {
        // графика
        assetManager.setLoader(Texture.class, new TextureLoader(fileHandleResolver));
        assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(fileHandleResolver));
        assetManager.setLoader(Skin.class, new FreeTypeSkinLoader(fileHandleResolver));

        // эффекты частиц
        assetManager.setLoader(ParticleEffect.class, ".p", new ParticleEffectLoader(fileHandleResolver));

        // звук
        assetManager.setLoader(Sound.class, new SoundLoader(fileHandleResolver));
        assetManager.setLoader(Music.class, new MusicLoader(fileHandleResolver));

        // карты редакторов уровней
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));

        // шрифты
        assetManager.setLoader(BitmapFont.class, new FreetypeFontLoader(fileHandleResolver));
    }

    private GameSettings createGameSettings() {
        return GameSettings.builder()
                .setProgressBarWidth(100)
                .setProgressBarHeight(25)

                .setVirtualScreenWidth(1280)
                .setVirtualScreenHeight(720)

                .setUnitsPerMeter(128)

                .build();
    }

    private AudioSettings createAudioSettings() {
        return new AudioSettings();
    }

    @Override
    public void render() {
        gameManager.screenStateManager.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        gameManager.screenStateManager.resize(width, height);
    }

    @Override
    public void pause() {
        gameManager.screenStateManager.pause();
    }

    @Override
    public void resume() {
        gameManager.screenStateManager.resume();
    }

    @Override
    public void dispose() {
        gameManager.screenStateManager.dispose();
        assetManager.dispose();
    }

}
