package com.github.br.ecs.simple.system.physics;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

/**
 * Created by user on 27.03.2017.
 */
public final class ShapeUtils {

    private ShapeUtils(){}

    public static Shape2D copyShape(Shape2D shape2D){
        if(shape2D instanceof Circle){
            Circle shape = (Circle) shape2D;
            return new Circle(shape);
        } else if(shape2D instanceof Rectangle){
            Rectangle shape = (Rectangle) shape2D;
            return new Rectangle(shape);
        }
        throw new IllegalArgumentException(String.format("Тип физической формы %s пока не поддерживается",
                shape2D.getClass().getSimpleName()));

    }

    public static float calculateWidth(Shape2D shape2D){
        if(shape2D instanceof Circle){
            Circle circle = (Circle) shape2D;
            return circle.radius*2;
        } else if(shape2D instanceof Rectangle){
            Rectangle rectangle = (Rectangle) shape2D;
            return rectangle.width;
        }
        throw new IllegalArgumentException(String.format("Тип физической формы %s пока не поддерживается",
                shape2D.getClass().getSimpleName()));
    }

    public static float calculateHeight(Shape2D shape2D){
        if(shape2D instanceof Circle){
            Circle circle = (Circle) shape2D;
            return circle.radius*2;
        } else if(shape2D instanceof Rectangle){
            Rectangle rectangle = (Rectangle) shape2D;
            return rectangle.height;
        }
        throw new IllegalArgumentException(String.format("Тип физической формы %s пока не поддерживается",
                shape2D.getClass().getSimpleName()));
    }

    public static float getX0(Shape2D shape2D){
        if(shape2D instanceof Circle){
            Circle circle = (Circle) shape2D;
            return circle.x - circle.radius;
        } else if(shape2D instanceof Rectangle){
            Rectangle rectangle = (Rectangle) shape2D;
            return rectangle.x;
        }
        throw new IllegalArgumentException(String.format("Тип физической формы %s пока не поддерживается",
                shape2D.getClass().getSimpleName()));
    }

    public static float getY0(Shape2D shape2D){
        if(shape2D instanceof Circle){
            Circle circle = (Circle) shape2D;
            return circle.y - circle.radius;
        } else if(shape2D instanceof Rectangle){
            Rectangle rectangle = (Rectangle) shape2D;
            return rectangle.y;
        }
        throw new IllegalArgumentException(String.format("Тип физической формы %s пока не поддерживается",
                shape2D.getClass().getSimpleName()));
    }



}
