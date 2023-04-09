package com.packt.snake.render.layers.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.packt.snake.model.WorldModel;
import com.packt.snake.model.gameobject.Apple;
import com.packt.snake.model.gameobject.Snake;
import com.packt.snake.model.utils.Vector;
import com.packt.snake.render.layers.Layer;
import com.packt.snake.render.utils.ViewHelper;

/**
 * Created by user on 10.03.2017.
 */
public class GameLayer extends Layer implements WorldModel.Listener {

    public static final String TITLE = "GameLayer";

    private SpriteBatch batch;
    private Texture snakeHead;
    private Texture snakeBodySegment;
    private Texture appleTexture;
    private WorldModel.WorldState worldState;

    public GameLayer() {
        super(TITLE);
        batch = new SpriteBatch();
        snakeHead = new Texture(Gdx.files.internal("snake/head2.png"));
        snakeBodySegment = new Texture(Gdx.files.internal("snake/body2.png"));
        appleTexture = new Texture(Gdx.files.internal("snake/apple2.png"));
    }

    @Override
    public void draw() {
        batch.setProjectionMatrix(ViewHelper.camera.projection);
        batch.setTransformMatrix(ViewHelper.camera.view);
        batch.begin();
        drawGame();
        batch.end();
    }

    private void drawGame() {
        if (worldState != null) {
            Snake snake = worldState.getSnake();
            Apple apple = worldState.getApple();

            Snake.SnakeBody body = snake.getBody();
            for (Vector segment : body.getSegments()) {
                drawTexture(snakeBodySegment, segment);
            }
            drawTexture(snakeHead, snake.getHead());

            if (apple.isAppleAvailable()) {
                drawTexture(appleTexture, apple.getCoords());
            }
        }
    }

    private void drawTexture(Texture texture, Vector coords) {
        coords = ViewHelper.toPx(coords);
        batch.draw(texture, coords.x, coords.y);
    }

    @Override
    public void handleEvent(WorldModel.WorldEvent event) {
        this.worldState = event.getState();
    }
}
