package com.github.br.ecs.simple.system.physics;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.github.br.ecs.simple.engine.EcsReflectionHelper;
import com.github.br.ecs.simple.utils.HashCache;

/**
 * Прямоугольная граница фигуры.
 */
public class Boundary {

    public Shape2D shape;

    private Vector2 offset;
    private HashCache cacheWidth;
    private HashCache cacheHeight;

    public Boundary(Shape2D shape) {
        this.shape = shape;
        offset = new Vector2(Float.class.cast(EcsReflectionHelper.getValue(shape, "x")), Float.class.cast(EcsReflectionHelper.getValue(shape, "y")));
        cacheWidth = new HashCache(shape, ShapeUtils.calculateWidth(shape));
        cacheHeight = new HashCache(shape, ShapeUtils.calculateHeight(shape));
    }

    public float getX() {
        return ShapeUtils.getX0(shape);
    }

    public float getY() {
        return ShapeUtils.getY0(shape);
    }

    public float getWidth() {
        if (!cacheWidth.isValid(shape)) {
            cacheWidth.update(shape, ShapeUtils.calculateWidth(shape));
        }
        return Float.class.cast(cacheWidth.getValue());
    }

    public float getHeight() {
        if (!cacheHeight.isValid(shape)) {
            cacheHeight.update(shape, ShapeUtils.calculateHeight(shape));
        }
        return Float.class.cast(cacheHeight.getValue());
    }

    public Vector2 getOffset() {
        return offset;
    }
}
