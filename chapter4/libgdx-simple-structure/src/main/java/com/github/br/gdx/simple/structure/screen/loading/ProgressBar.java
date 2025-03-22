package com.github.br.gdx.simple.structure.screen.loading;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface ProgressBar {
    void draw(ShapeRenderer shapeRenderer);

    void updateProgress(float progress);

    void reset();
}
