package com.github.br.ecs.simple.system.physics;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by user on 13.01.2019.
 */
public class CirclePosUpdater implements ShapePosUpdater<Circle> {
    @Override
    public void update(Circle circle, Vector2 vector2) {
        circle.setX(vector2.x); //todo offset
        circle.setY(vector2.y); //todo offset
    }
}
