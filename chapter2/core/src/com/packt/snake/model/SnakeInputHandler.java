package com.packt.snake.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.packt.snake.InputManager;
import com.packt.snake.model.gameobject.Snake;

/**
 * Created by user on 28.02.2017.
 */
public class SnakeInputHandler implements InputManager.Handler, WorldModel.Listener {

    private Snake snake;
    private boolean directionSet; // флаг, чтобы змея меняла направление 1 раз за 1 ход

    public SnakeInputHandler(Snake snake){
        this.snake = snake;
    }

    @Override
    public void handleInput() {
        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);

        if (lPressed) updateDirection(Snake.LEFT);
        if (rPressed) updateDirection(Snake.RIGHT);
        if (uPressed) updateDirection(Snake.UP);
        if (dPressed) updateDirection(Snake.DOWN);
    }

    @Override
    public void handleEvent(WorldModel.WorldEvent event) {
        resetDirectionSet(); // это чтобы змея не могла развернуться на 180 градусов за ход
    }

    private void resetDirectionSet(){
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
            if(!oppositeDirection){
                snake.setSnakeDirection(newSnakeDirection);
            }
        }
    }


}
