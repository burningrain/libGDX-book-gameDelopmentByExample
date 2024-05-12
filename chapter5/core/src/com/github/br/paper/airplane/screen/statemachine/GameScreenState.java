package com.github.br.paper.airplane.screen.statemachine;

import com.badlogic.gdx.Screen;
import com.github.br.paper.airplane.screen.AbstractGameScreen;
import com.github.br.paper.airplane.screen.loading.AssetsLoader;

public class GameScreenState {

    public final AbstractGameScreen screen;
    public final AssetsLoader assetsLoader;

    public GameScreenState(AbstractGameScreen screen, AssetsLoader assetsLoader) {
        this.screen = screen;
        this.assetsLoader = assetsLoader;
    }

}
