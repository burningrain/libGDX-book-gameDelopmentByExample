package com.github.br.ecs.simple.engine.debug.data;

/**
 * Created by user on 07.04.2018.
 */
public class RectangleData implements DebugData {

    private float x, y;
    private float width, height;

    public RectangleData(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
