package com.github.br.ecs.simple.engine;

/**
 * Created by user on 17.06.2018.
 */
public final class GameTimer {

    private static final int SEC_IN_ONE_MIN = 60;

    private float gameTime; // в секундах

    private int mins;
    private int secs;

    public GameTimer(){}

    void update(float delta) {
        this.gameTime += delta;
        mins = (int) (this.gameTime/SEC_IN_ONE_MIN);
        secs = (int) (this.gameTime % SEC_IN_ONE_MIN);
    }

    public float getGameTime() {
        return gameTime;
    }

    public int getMins() {
        return mins;
    }

    public int getSecs() {
        return secs;
    }
}