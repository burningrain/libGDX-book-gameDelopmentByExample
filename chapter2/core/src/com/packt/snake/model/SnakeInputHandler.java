package com.packt.snake.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.packt.snake.InputManager;
import com.packt.snake.model.gameobject.Snake;
import com.packt.snake.render.utils.ViewHelper;

/**
 * Created by user on 28.02.2017.
 */
public class SnakeInputHandler implements InputManager.Handler, WorldModel.Listener {

    private Snake snake;
    private boolean directionSet; // флаг, чтобы змея меняла направление 1 раз за 1 ход

    public SnakeInputHandler(Snake snake) {
        this.snake = snake;
    }

    @Override
    public void handleInput() {
        boolean lPressed = false;
        boolean rPressed = false;
        boolean uPressed = false;
        boolean dPressed = false;
        switch (Gdx.app.getType()) {
            case Desktop:
                lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
                rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
                uPressed = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
                dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);
                break;
            case Android:
                Vector2 snakeVectorNormal = new Vector2(snake.getBody().getSegments().get(0).y - snake.getHead().y,
                        snake.getHead().x - snake.getBody().getSegments().get(0).x);
                Vector2 clickPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                ViewHelper.viewport.unproject(clickPos);
                clickPos.scl(1f/ ViewHelper.CELL_VIEW_SIZE);
                Vector2 newDirectionVector = new Vector2(clickPos.x - snake.getHead().x, clickPos.y - snake.getHead().y);
                float dot = snakeVectorNormal.x * newDirectionVector.x + snakeVectorNormal.y * newDirectionVector.y;
                if(dot > 0){
                    // тапнуто влево от текущего положения головы змеи
                    switch(snake.getSnakeDirection()){
                        case Snake.UP:
                            lPressed = true;
                            break;
                        case Snake.DOWN:
                            rPressed = true;
                            break;
                        case Snake.LEFT:
                            dPressed = true;
                            break;
                        case Snake.RIGHT:
                            uPressed = true;
                            break;
                    }
                } else if(dot < 0) {
                    // тапнуто вправо от текущего положения головы змеи
                    switch(snake.getSnakeDirection()){
                        case Snake.UP:
                            rPressed = true;
                            break;
                        case Snake.DOWN:
                            lPressed = true;
                            break;
                        case Snake.LEFT:
                            uPressed = true;
                            break;
                        case Snake.RIGHT:
                            dPressed = true;
                            break;
                    }
                } else if(dot == 0){
                    //ничего не делаем. направление совпадает или противоположно направлению змеи
                }

                break;
        }

        if (lPressed) updateDirection(Snake.LEFT);
        if (rPressed) updateDirection(Snake.RIGHT);
        if (uPressed) updateDirection(Snake.UP);
        if (dPressed) updateDirection(Snake.DOWN);
    }

    @Override
    public void handleEvent(WorldModel.WorldEvent event) {
        resetDirectionSet(); // это чтобы змея не могла развернуться на 180 градусов за ход
    }

    private void resetDirectionSet() {
        directionSet = false;
    }

    private static boolean isOppositeDirection(int snakeDirection, int oppositeDirection) {
        return snakeDirection == oppositeDirection;
    }

    private void updateDirection(int newSnakeDirection) {
        if (!directionSet && snake.getSnakeDirection() != newSnakeDirection) {
            directionSet = true;
            boolean oppositeDirection = false;
            switch (newSnakeDirection) {
                case Snake.LEFT:
                    oppositeDirection = isOppositeDirection(snake.getSnakeDirection(), Snake.RIGHT);
                    break;
                case Snake.RIGHT:
                    oppositeDirection = isOppositeDirection(snake.getSnakeDirection(), Snake.LEFT);
                    break;
                case Snake.UP:
                    oppositeDirection = isOppositeDirection(snake.getSnakeDirection(), Snake.DOWN);
                    break;
                case Snake.DOWN:
                    oppositeDirection = isOppositeDirection(snake.getSnakeDirection(), Snake.UP);
                    break;
            }
            if (!oppositeDirection) {
                snake.setSnakeDirection(newSnakeDirection);
            }
        }
    }


}
