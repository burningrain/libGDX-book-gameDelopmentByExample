package com.github.br.paper.airplane.screen.loading;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SimpleProgressBarImpl implements ProgressBar {

    private final float progressBarWidth;
    private final float progressBarHeight;
    private float progress = 0f;

    private int x, y;

    public SimpleProgressBarImpl(float progressBarWidth, float progressBarHeight, int x, int y) {
        this.progressBarWidth = progressBarWidth;
        this.progressBarHeight = progressBarHeight;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        Color color = shapeRenderer.getColor();

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                x,
                y,
                progress * progressBarWidth,
                progressBarHeight
        );

        shapeRenderer.setColor(color);
    }

    @Override
    public void updateProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void reset() {
        progress = 0;
        x = 0;
        y = 0;
    }

}
