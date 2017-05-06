package com.packt.snake.model.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.packt.snake.model.gameobject.Apple;
import com.packt.snake.model.gameobject.Snake;
import com.packt.snake.render.utils.ViewHelper;

/**
 * Created by user on 27.02.2017.
 */
public final class GameHelper {

    public static final int MAX_WIDTH = (int) (ViewHelper.viewport.getWorldWidth() / 32);
    public static final int MAX_HEIGHT = (int) (ViewHelper.viewport.getWorldHeight() / 32);

    private GameHelper() {
    }

    public static Vector checkForOutOfBounds(Vector coordinats) {
        if (coordinats.x >= MAX_WIDTH) {
            coordinats.x = 0;
        }
        if (coordinats.x < 0) {
            coordinats.x = MAX_WIDTH - 1;
        }
        if (coordinats.y >= MAX_HEIGHT) {
            coordinats.y = 0;
        }
        if (coordinats.y < 0) {
            coordinats.y = MAX_HEIGHT - 1;
        }
        return coordinats;
    }

    public static Vector newApplePlace(final Snake snake) {
        Vector newPosition = new Vector();
        boolean collisionWithSnake;
        do {
            newPosition.x = MathUtils.random(MAX_WIDTH - 1);
            newPosition.y = MathUtils.random(MAX_HEIGHT - 1);

            collisionWithSnake = (newPosition.x == snake.getHead().x) && (newPosition.y == snake.getHead().y);
            collisionWithSnake = collisionWithSnake || checkSnakeBodyCollision(snake.getBody(), newPosition);
        } while (collisionWithSnake);
        return newPosition;
    }

    public static boolean checkAppleCollision(Apple apple, Snake snake) {
        return apple.isAppleAvailable() &&
                apple.getCoords().x == snake.getHead().x &&
                apple.getCoords().y == snake.getHead().y;
    }


    public static boolean checkSnakeBodyCollision(Snake.SnakeBody body, Vector target) {
        for (Vector segment : body.getSegments()) {
            if (segment.x == target.x && segment.y == target.y) return true;
        }
        return false;
    }


}
