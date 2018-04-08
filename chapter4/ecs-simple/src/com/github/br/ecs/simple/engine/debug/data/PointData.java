package com.github.br.ecs.simple.engine.debug.data;

/**
 * Created by user on 07.04.2018.
 */
public class PointData implements DebugData {

    private float x, y;

    public PointData(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
