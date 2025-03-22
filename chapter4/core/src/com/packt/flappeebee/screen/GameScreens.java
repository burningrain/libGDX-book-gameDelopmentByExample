package com.packt.flappeebee.screen;


import com.github.br.gdx.simple.structure.screen.statemachine.GameScreenState;
import com.packt.flappeebee.screen.level.menu.MainMenuAssetsLoader;
import com.packt.flappeebee.screen.level.menu.MainMenuScreen;

public interface GameScreens {

    GameScreenState MAIN_MENU = new GameScreenState(new MainMenuScreen(), new MainMenuAssetsLoader());

}
