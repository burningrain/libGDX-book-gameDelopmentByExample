package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.Gdx;

/**
 * Created by user on 17.06.2018.
 */
public final class EcsSimple {

    public static EcsSimple ECS = new EcsSimple();

    public GameTimer TIMER = new GameTimer();

    private EcsSimple() {}

    void update(float delta) {
        TIMER.update(delta);
    }

    public int fps() {
        return Gdx.graphics.getFramesPerSecond();
    }

}
