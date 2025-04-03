package com.packt.flappeebee.screen.level.level.gameloop;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class GameLoopManager {

    public static int version;

    private GameLoopStrategy strategy1;
    private GameLoopStrategy strategy2;
    private GameLoopStrategy strategy3;

    private GameLoopStrategy currentStrategy;

    public void init(GameLoopStrategy strategy1,
                     GameLoopStrategy strategy2,
                     GameLoopStrategy strategy3) {
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
        this.strategy3 = strategy3;

        currentStrategy = strategy1;
        version = 1;
    }

    public void update() {
        currentStrategy.update();
    }

    public void changeStrategy(int version) {
        if (version <= 0) {
            throw new GdxRuntimeException("strategy version must grower than 0");
        }
        switch (version) {
            case 1:
                currentStrategy = strategy1;
                break;
            case 2:
                currentStrategy = strategy2;
                break;
            case 3:
                currentStrategy = strategy3;
                break;
            default:
                throw new GdxRuntimeException(version + " is not supported");
        }
        GameLoopManager.version = version;
    }

}
