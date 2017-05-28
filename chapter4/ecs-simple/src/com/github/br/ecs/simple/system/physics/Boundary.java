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

    public Boundary(Shape2D shape){
        this.shape = shape;
        offset = new Vector2((float)EcsReflectionHelper.getValue(shape, "x"), (float)EcsReflectionHelper.getValue(shape, "y"));
        cacheWidth = new HashCache(shape, ShapeUtils.calculateWidth(shape));
        cacheHeight = new HashCache(shape, ShapeUtils.calculateHeight(shape));
    }

    public float getX(){
        return ShapeUtils.getX0(shape);
    }

    public float getY(){
        return ShapeUtils.getY0(shape);
    }

    public float getWidth(){
        if(!cacheWidth.isValid(shape)){
            cacheWidth.update(shape, ShapeUtils.calculateWidth(shape));
        }
        return (float) cacheWidth.getValue();
    }

    public float getHeight(){
        if(!cacheHeight.isValid(shape)){
            cacheHeight.update(shape, ShapeUtils.calculateHeight(shape));
        }
        return (float) cacheHeight.getValue();
    }

    public Vector2 getOffset() {
        return offset;
    }
}
