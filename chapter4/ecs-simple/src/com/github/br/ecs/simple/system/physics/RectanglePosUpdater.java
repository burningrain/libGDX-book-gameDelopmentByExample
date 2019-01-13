package com.github.br.ecs.simple.system.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by user on 13.01.2019.
 */
public class RectanglePosUpdater implements ShapePosUpdater<Rectangle> {
    @Override
    public void update(Rectangle rectangle, Vector2 vector2) {
        rectangle.setX(vector2.x); //todo offset
        rectangle.setY(vector2.y); //todo offset
    }
}
