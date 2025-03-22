package com.github.br.gdx.simple.structure;

import com.github.br.gdx.simple.structure.audio.AudioAssetManager;
import com.badlogic.gdx.assets.AssetManager;
import com.github.br.gdx.simple.structure.screen.loading.LoadingScreen;
import com.github.br.gdx.simple.structure.screen.statemachine.GameScreenState;
import com.github.br.gdx.simple.structure.screen.statemachine.GameScreenStateManager;

public class GameManager<U extends UserFactory> {

    public GameSettings gameSettings;
    public AudioSettings audioSettings;
    public AudioAssetManager audioAssetManager;
    public Utils utils;
    public U userFactory;
    public AssetManager assetManager;
    public GameScreenStateManager screenStateManager;


    public void init(
            GameSettings gameSettings,
            AudioSettings audioSettings,
            Utils utils,
            AssetManager assetManager,
            AudioAssetManager audioAssetManager,
            LoadingScreen loadingScreen,
            GameScreenState startState,
            U userFactory
    ) {
        this.gameSettings = gameSettings;
        this.audioSettings = audioSettings;
        this.utils = utils;
        this.assetManager = assetManager;
        this.audioAssetManager = audioAssetManager;
        screenStateManager = new GameScreenStateManager(this, loadingScreen, startState);

        this.userFactory = userFactory;
        this.userFactory.init(this);
    }

}
