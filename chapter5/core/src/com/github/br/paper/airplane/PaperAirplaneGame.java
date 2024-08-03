package com.github.br.paper.airplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.github.br.paper.airplane.level.GameLevels;
import com.github.br.paper.airplane.screen.loading.SimpleProgressBarImpl;
import com.github.br.paper.airplane.screen.loading.LoadingScreen;


public class PaperAirplaneGame extends ApplicationAdapter {

    private GameSettings gameSettings;
    private Utils utils;
    private AssetManager assetManager;
    private GameManager gameManager;

    @Override
    public void create() {
        gameSettings = createGameSettings();
        utils = new Utils(gameSettings);

        InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));
        assetManager.setLoader(ParticleEffect.class, ".p", new ParticleEffectLoader(fileHandleResolver));

        Box2D.init();

        gameManager = new GameManager();
        gameManager.init(
                gameSettings,
                utils,
                assetManager,
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
                GameLevels.LEVEL_0
        );
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

    @Override
    public void render() {
        gameManager.screenStateManager.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        gameManager.screenStateManager.dispose();
        assetManager.dispose();
    }

}
