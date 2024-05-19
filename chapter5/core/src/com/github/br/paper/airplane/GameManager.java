package com.github.br.paper.airplane;

import com.badlogic.gdx.assets.AssetManager;
import com.github.br.paper.airplane.gameworld.GameEntityFactory;
import com.github.br.paper.airplane.screen.loading.LoadingScreen;
import com.github.br.paper.airplane.screen.statemachine.GameScreenState;
import com.github.br.paper.airplane.screen.statemachine.GameScreenStateManager;

public class GameManager {

    public GameSettings gameSettings;
    public Utils utils;
    public GameEntityFactory gameEntityFactory;
    public AssetManager assetManager;
    public GameScreenStateManager screenStateManager;


    public void init(
            GameSettings gameSettings,
            Utils utils,
            AssetManager assetManager,
            LoadingScreen loadingScreen,
            GameScreenState startState
    ) {
        this.gameSettings = gameSettings;
        this.utils = utils;
        this.assetManager = assetManager;
        gameEntityFactory = new GameEntityFactory(assetManager, gameSettings, utils);
        screenStateManager = new GameScreenStateManager(this, loadingScreen, startState);
    }

}
