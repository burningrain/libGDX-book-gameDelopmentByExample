package com.github.br.gdx.simple.structure.screen;

import com.badlogic.gdx.Screen;
import com.github.br.gdx.simple.structure.GameManager;

public abstract class AbstractGameScreen implements Screen {

    private GameManager gameManager;

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public abstract void show() ;

    @Override
    public abstract void render(float delta);

    @Override
    public abstract void resize(int width, int height);

    @Override
    public abstract void pause();

    @Override
    public abstract void resume();

    @Override
    public abstract void hide();

    @Override
    public abstract void dispose();

}
