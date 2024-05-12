package com.github.br.paper.airplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.github.br.paper.airplane.level.GameLevels;
import com.github.br.paper.airplane.screen.loading.SimpleProgressBarImpl;
import com.github.br.paper.airplane.screen.loading.LoadingScreen;


public class PaperAirplaneGame extends ApplicationAdapter {

    private AssetManager assetManager;
    private GameManager gameManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        Box2D.init();

        gameManager = new GameManager();
        gameManager.init(
                assetManager,
                new LoadingScreen(
                        new SimpleProgressBarImpl(
                                GameConstants.PROGRESSBAR_WIDTH,
                                GameConstants.PROGRESSBAR_HEIGHT,
                                (GameConstants.VIRTUAL_SCREEN_WIDTH - GameConstants.PROGRESSBAR_WIDTH) / 2,
                                (GameConstants.VIRTUAL_SCREEN_HEIGHT - GameConstants.PROGRESSBAR_HEIGHT) / 2
                        ),
                        GameConstants.VIRTUAL_SCREEN_WIDTH,
                        GameConstants.VIRTUAL_SCREEN_HEIGHT
                ),
                GameLevels.LEVEL_0
        );
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
