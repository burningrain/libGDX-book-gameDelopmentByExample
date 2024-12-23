package com.github.br.paper.airplane;

import com.badlogic.gdx.assets.AssetManager;
import com.github.br.paper.airplane.audio.AudioAssetManager;
import com.github.br.paper.airplane.bullet.BulletFactory;
import com.github.br.paper.airplane.gameworld.GameEntityFactory;
import com.github.br.paper.airplane.screen.loading.LoadingScreen;
import com.github.br.paper.airplane.screen.statemachine.GameScreenState;
import com.github.br.paper.airplane.screen.statemachine.GameScreenStateManager;

public class GameManager {

    public GameSettings gameSettings;
    private AudioSettings audioSettings;
    private AudioAssetManager audioAssetManager;
    public Utils utils;
    public GameEntityFactory gameEntityFactory;
    public BulletFactory bulletFactory;
    public AssetManager assetManager;
    public GameScreenStateManager screenStateManager;


    public void init(
            GameSettings gameSettings,
            AudioSettings audioSettings,
            Utils utils,
            AssetManager assetManager,
            AudioAssetManager audioAssetManager,
            LoadingScreen loadingScreen,
            GameScreenState startState
    ) {
        this.gameSettings = gameSettings;
        this.audioSettings = audioSettings;
        this.utils = utils;
        this.assetManager = assetManager;
        this.audioAssetManager = audioAssetManager;
        gameEntityFactory = new GameEntityFactory(assetManager, gameSettings, utils);
        screenStateManager = new GameScreenStateManager(this, loadingScreen, startState);
        bulletFactory = new BulletFactory(this);
    }

}
