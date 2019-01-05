package com.github.br.ecs.simple.engine.debug.data;

/**
 * Created by user on 03.01.2019.
 */
public class LabelData implements DebugData {

    private float x, y;
    private String label;

    public LabelData(float x, float y, String label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getLabel() {
        return label;
    }

}
