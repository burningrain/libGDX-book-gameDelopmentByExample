package com.packt.snake;

import com.badlogic.gdx.ScreenAdapter;
import com.packt.snake.model.WorldModel;
import com.packt.snake.render.RenderDispatcher;
import com.packt.snake.render.utils.ViewHelper;


public class GameScreen extends ScreenAdapter {

    private InputManager inputManager;
    private RenderDispatcher renderDispatcher;
    private WorldModel worldModel;

    @Override
    public void show() {
        inputManager = new InputManager();
        worldModel = new WorldModel(inputManager);
        renderDispatcher = new RenderDispatcher(worldModel);

        GamePublisher.self().changeState(GamePublisher.State.NEW_GAME);
    }

    @Override
    public void render(float delta) {
        inputManager.render(delta);      // обновление информации о нажатии клавиш
        worldModel.render(delta);        // обновление состояния модели игрового мира
        renderDispatcher.render(delta);  // а вот рендерить мир надо постоянно

        //TODO вывод runtime-ошибок в лог Gdx
    }

    @Override
    public void resize(int width, int height) {
        ViewHelper.viewport.update(width, height);
    }

}