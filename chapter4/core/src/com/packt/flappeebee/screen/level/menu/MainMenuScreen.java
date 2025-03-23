package com.packt.flappeebee.screen.level.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.github.br.gdx.simple.structure.GameManager;
import com.github.br.gdx.simple.structure.screen.AbstractGameScreen;
import com.github.br.simple.input.InputSystem;
import com.packt.flappeebee.action.ActionMapperImpl;
import com.packt.flappeebee.model.GameWorld;
import com.packt.flappeebee.model.GameWorldSettings;

public class MainMenuScreen extends AbstractGameScreen {

    private GameWorld gameWorld;

    private InputSystem inputSystem;

    @Override
    public void show() {
        super.show();

        GameManager gameManager = getGameManager();

        //TODO убрать старый код
        GameWorldSettings gameWorldSettings = new GameWorldSettings();
        gameWorldSettings.virtualWidth = 640;
        gameWorldSettings.virtualHeight = 480;
        gameWorld = new GameWorld(gameWorldSettings);
        //TODO убрать старый код

        inputSystem = new InputSystem(5, new ActionMapperImpl());
        //TODO Gdx.input.setInputProcessor(inputSystem.getInputProcessor()); //TODO
    }

    @Override
    public void render(float delta) {
        inputSystem.update(delta);

        clearScreen();                      // очищаем экран
        gameWorld.render(delta);            // обновление игрового кадра (состояние и рендеринг)

        inputSystem.reset();
    }

    public void resize(int width, int height) {
        gameWorld.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    public void dispose() {
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
