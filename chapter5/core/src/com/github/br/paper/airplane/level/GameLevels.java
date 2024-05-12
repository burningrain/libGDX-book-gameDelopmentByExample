package com.github.br.paper.airplane.level;

import com.github.br.paper.airplane.level.level0.Level0AssetsLoader;
import com.github.br.paper.airplane.level.level0.Level0Screen;
import com.github.br.paper.airplane.screen.statemachine.GameScreenState;

public interface GameLevels {

    GameScreenState LEVEL_0 = new GameScreenState(new Level0Screen(), new Level0AssetsLoader());

}
