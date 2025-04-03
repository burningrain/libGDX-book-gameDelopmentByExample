package com.packt.flappeebee.screen;


import com.github.br.gdx.simple.structure.screen.statemachine.GameScreenState;
import com.packt.flappeebee.screen.level.level.Level0AssetLoader;
import com.packt.flappeebee.screen.level.level.Level0Screen;
import com.packt.flappeebee.screen.level.level1.Level1AssetLoader;
import com.packt.flappeebee.screen.level.level1.Level1Screen;


public interface GameScreens {

    GameScreenState LEVEL_0 = new GameScreenState(new Level0Screen(), new Level0AssetLoader());
    GameScreenState LEVEL_1 = new GameScreenState(new Level1Screen(), new Level1AssetLoader());

}
