package com.github.br.paper.airplane.screen;

import com.github.br.paper.airplane.level.level0.Level0AssetsLoader;
import com.github.br.paper.airplane.level.level0.Level0Screen;
import com.github.br.paper.airplane.screen.menu.MainMenuAssetsLoader;
import com.github.br.paper.airplane.screen.menu.MainMenuScreen;
import com.github.br.paper.airplane.screen.statemachine.GameScreenState;

public interface GameScreens {

    GameScreenState MAIN_MENU = new GameScreenState(new MainMenuScreen(), new MainMenuAssetsLoader());
    GameScreenState LEVEL_0 = new GameScreenState(new Level0Screen(), new Level0AssetsLoader());

}
