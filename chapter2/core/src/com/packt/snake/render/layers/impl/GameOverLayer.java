package com.packt.snake.render.layers.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.packt.snake.model.WorldModel;
import com.packt.snake.render.layers.Layer;
import com.packt.snake.render.utils.ViewHelper;

/**
 * Created by user on 10.03.2017.
 */
public class GameOverLayer extends Layer implements WorldModel.Listener {

    private int score;

    public static final String TITLE = "GameOverLayer";
    private static final String GAME_OVER_TEXT = "      GAME OVER\n" +
            "(tap space to restart!)"; // русский текст не поддерживается по умолчанию
    private SpriteBatch batch;

    private GlyphLayout layout = new GlyphLayout();
    private BitmapFont gameOverFont = new BitmapFont();

    private BitmapFont scoreFont = new BitmapFont();
    private GlyphLayout scoreLayout = new GlyphLayout();

    public GameOverLayer() {
        super(TITLE);
        batch = new SpriteBatch();
        layout.setText(gameOverFont, GAME_OVER_TEXT);

        scoreFont.setColor(Color.FOREST);

    }

    @Override
    public void draw() {
        batch.setProjectionMatrix(ViewHelper.camera.projection);
        batch.setTransformMatrix(ViewHelper.camera.view);
        batch.begin();
        gameOverFont.draw(batch, GAME_OVER_TEXT, (ViewHelper.viewport.getWorldWidth() -
                layout.width) / 2, (ViewHelper.viewport.getWorldHeight() - layout.height) / 2);
        drawScore();
        batch.end();
    }

    private void drawScore() {
        String scoreString = "Score: " + String.valueOf(score);
        scoreLayout.setText(scoreFont, scoreString);
        scoreFont.draw(batch, scoreString,
                (ViewHelper.viewport.getWorldWidth() - scoreLayout.width) / 2,
                3 * ViewHelper.viewport.getWorldHeight() / 5 - scoreLayout.height / 2);
    }

    @Override
    public void handleEvent(WorldModel.WorldEvent event) {
        this.score = event.getScore();
    }
}
