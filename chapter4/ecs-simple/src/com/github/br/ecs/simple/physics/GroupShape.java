package com.github.br.ecs.simple.physics;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.github.br.ecs.simple.utils.EcsReflectionHelper;
import com.github.br.ecs.simple.system.ShapeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GroupShape implements Serializable, Shape2D {

    public float x, y; // глобальные координаты объекта
    public float rotation; //TODO допилить ротацию
    private HashMap<String, Shape2D> localPosShapes = new HashMap<>();

    public GroupShape(GroupShape groupShape){
        this.x = groupShape.x;
        this.y = groupShape.y;
        this.rotation = groupShape.rotation;

        for(Map.Entry<String, Shape2D> entry : groupShape.localPosShapes.entrySet()){
            this.localPosShapes.put(entry.getKey(), ShapeUtils.copyShape(entry.getValue()));
        }
    }

    public GroupShape(Vector2 vector2, float rotation) {
        this.x = vector2.x;
        this.y = vector2.y;
        this.rotation = rotation;
    }

    /**
     * @param name
     * @param shape задаются с локальными координатами, относительно группы
     */
    public void addShape(String name, Shape2D shape) {
        localPosShapes.put(name, shape);
    }


    public <T extends Shape2D> T getLocalPosShape(String name) {
        return (T) localPosShapes.get(name);
    }

    // fixme кривота какая-то с возвращением глобальных координат, ну пока сойдет
    public <T extends Shape2D> T getGlobalPosShape(String name) {
        Shape2D shape2D = localPosShapes.get(name);
        Shape2D copy = ShapeUtils.copyShape(shape2D);
        setGlobalPosition(copy);
        return (T)copy;
    }

    public Collection<Shape2D> getLocalPosShapes() {
        return localPosShapes.values();
    }

    public Collection<Shape2D> getGlobalPosShapes() {
        ArrayList<Shape2D> list = new ArrayList<>(localPosShapes.size());
        for(Shape2D shape2D : localPosShapes.values()){
            Shape2D copy = ShapeUtils.copyShape(shape2D);
            setGlobalPosition(copy);
            list.add(copy);
        }
        return list;
    }

    public float getX0(){
        float minX = Integer.MAX_VALUE;
        for(Shape2D shape : localPosShapes.values()){
            float x = ShapeUtils.getX0(shape);
            if(x < minX){
                minX = x;
            }
        }
        return this.x + minX;
    }

    public float getY0(){
        float minY = Integer.MAX_VALUE;
        for(Shape2D shape : localPosShapes.values()){
            float y = ShapeUtils.getY0(shape);
            if(y < minY){
                minY = y;
            }
        }
        return this.y + minY;
    }

    public float getWidth(){
        float maxX = Integer.MIN_VALUE;
        float minX = Integer.MAX_VALUE;
        for(Shape2D shape : localPosShapes.values()){
            float x = ShapeUtils.getX0(shape);
            float width = ShapeUtils.calculateWidth(shape);
            if(x + width > maxX){
                maxX = x + width;
            }
            if(x < minX){
                minX = x;
            }
        }
        return maxX - minX;
    }

    public float getHeight(){
        float maxY = Integer.MIN_VALUE;
        float minY = Integer.MAX_VALUE;
        for(Shape2D shape : localPosShapes.values()){
            float y = ShapeUtils.getY0(shape);

            float height = ShapeUtils.calculateHeight(shape);
            if(y + height > maxY){
                maxY = y + height;
            }
            if(y < minY){
                minY = y;
            }
        }
        return maxY - minY;
    }

    private void setGlobalPosition(Shape2D copy){
        EcsReflectionHelper.setValue(copy, "x", (float)EcsReflectionHelper.getValue(copy, "x") + this.x);
        EcsReflectionHelper.setValue(copy, "y", (float)EcsReflectionHelper.getValue(copy, "y") + this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupShape that = (GroupShape) o;

        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;
        if (Float.compare(that.rotation, rotation) != 0) return false;
        return !(localPosShapes != null ? !localPosShapes.equals(that.localPosShapes) : that.localPosShapes != null);

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (rotation != +0.0f ? Float.floatToIntBits(rotation) : 0);
        result = 31 * result + (localPosShapes != null ? localPosShapes.hashCode() : 0);
        return result;
    }
}
