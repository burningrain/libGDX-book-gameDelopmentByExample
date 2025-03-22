package com.github.br.gdx.simple.structure.screen.statemachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.github.br.gdx.simple.structure.GameManager;
import com.github.br.gdx.simple.structure.screen.AbstractGameScreen;
import com.github.br.gdx.simple.structure.screen.loading.AssetsLoader;
import com.github.br.gdx.simple.structure.screen.loading.LoadingScreen;

public class GameScreenStateManager implements Screen {

    private final LoadingScreen loadingScreen;
    private final GameManager gameManager;

    private GameScreenState currentState = new GameScreenState(
            new AbstractGameScreen() {
                @Override
                public void show() {
                }

                @Override
                public void render(float delta) {
                }

                @Override
                public void resize(int width, int height) {
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

                @Override
                public void dispose() {
                }
            },
            AssetsLoader.EMPTY_ASSET_LOADER
    );

    private boolean isLoading = false;

    public GameScreenStateManager(
            GameManager gameManager,
            LoadingScreen loadingScreen,
            GameScreenState startState
    ) {
        this.gameManager = gameManager;
        this.loadingScreen = loadingScreen;

        changeCurrentState(startState);
    }

    public void changeCurrentState(GameScreenState newState) {
        hideScreen(currentState.screen);

        this.currentState.assetsLoader.unloadAssets(this.gameManager.assetManager);
        this.currentState = newState;
        this.currentState.screen.setGameManager(gameManager);
        this.currentState.assetsLoader.loadAssets(this.gameManager.assetManager);

        showScreen(loadingScreen);
        this.isLoading = true;

        System.gc();
    }

    private void hideScreen(Screen screen) {
        if (screen != null) screen.hide();
    }

    private void showScreen(Screen screen) {
        if (screen != null) {
            screen.show();
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    @Override
    public void show() {
        currentState.screen.show();
    }

    @Override
    public void render(float delta) {
        if (isLoading) {
            if (gameManager.assetManager.update()) {
                handleLoadingComplete();
            } else {
                loadingScreen.updateProgress(gameManager.assetManager.getProgress());
            }
            loadingScreen.render(delta);
        } else {
            currentState.screen.render(delta);
        }
    }

    private void handleLoadingComplete() {
        isLoading = false;
        hideScreen(loadingScreen);
        loadingScreen.reset();
        showScreen(currentState.screen);
    }

    @Override
    public void resize(int width, int height) {
        if (isLoading) {
            loadingScreen.resize(width, height);
            return;
        }
        currentState.screen.resize(width, height);
    }

    @Override
    public void pause() {
        currentState.screen.pause();
    }

    @Override
    public void resume() {
        currentState.screen.resume();
    }

    @Override
    public void hide() {
        currentState.screen.hide();
    }

    @Override
    public void dispose() {
        currentState.screen.dispose();
    }

}
