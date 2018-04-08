package com.github.br.ecs.simple.engine.debug.data;

/**
 * Created by user on 07.04.2018.
 */
public class CircleData implements DebugData {

    private float x, y, radius, degree;

    public CircleData(float x, float y, float radius, float degree) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.degree = degree;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public float getDegree() {
        return degree;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CircleData{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", radius=").append(radius);
        sb.append(", degree=").append(degree);
        sb.append('}');
        return sb.toString();
    }
}
