package com.packt.snake.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.packt.snake.GamePublisher;
import com.packt.snake.model.*;
import com.packt.snake.ScreenManager;
import com.packt.snake.render.layers.impl.GameLayer;
import com.packt.snake.render.layers.impl.GameOverLayer;
import com.packt.snake.render.layers.impl.GridLayer;
import com.packt.snake.render.layers.LayerFolder;

/**
 * Created by user on 28.02.2017.
 */
public class RenderDispatcher implements ScreenManager, GamePublisher.Subscriber {

    private LayerFolder layerFolder;

    public RenderDispatcher(WorldModel model) {
        GamePublisher.self().addListener(GamePublisher.State.NEW_GAME, this);
        GamePublisher.self().addListener(GamePublisher.State.GAME_OVER, this);

        layerFolder = new LayerFolder("GameBaseLayers");

        GameLayer gameLayer = new GameLayer();
        model.addListener(gameLayer);

        GameOverLayer gameOverLayer = new GameOverLayer();
        model.addListener(gameOverLayer);
        gameOverLayer.hide();

        // порядок важен! Отрисовка в порядке добавления
        layerFolder.addLayer(new GridLayer());
        layerFolder.addLayer(gameLayer);
        layerFolder.addLayer(gameOverLayer);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        layerFolder.draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void handleGameState(GamePublisher.State state) {
        switch (state) {
            case NEW_GAME:
                layerFolder.showLayer(GridLayer.TITLE);
                layerFolder.hideLayer(GameOverLayer.TITLE);
                break;
            case GAME_OVER:
                layerFolder.hideLayer(GridLayer.TITLE);
                layerFolder.showLayer(GameOverLayer.TITLE);
                break;
        }
    }
}
