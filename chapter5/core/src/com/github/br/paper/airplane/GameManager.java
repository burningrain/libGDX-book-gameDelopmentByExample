package com.github.br.paper.airplane;

import com.badlogic.gdx.assets.AssetManager;
import com.github.br.paper.airplane.screen.loading.LoadingScreen;
import com.github.br.paper.airplane.screen.statemachine.GameScreenState;
import com.github.br.paper.airplane.screen.statemachine.GameScreenStateManager;

public class GameManager {

    public AssetManager assetManager;
    public GameScreenStateManager screenStateManager;


    public void init(AssetManager assetManager, LoadingScreen loadingScreen, GameScreenState startState) {
        this.assetManager = assetManager;
        screenStateManager = new GameScreenStateManager(this, loadingScreen, startState);
    }

}
