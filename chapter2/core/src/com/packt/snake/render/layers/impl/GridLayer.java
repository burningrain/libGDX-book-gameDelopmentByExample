package com.packt.snake.render.layers.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.packt.snake.model.utils.GameHelper;
import com.packt.snake.render.layers.Layer;
import com.packt.snake.render.utils.ViewHelper;

/**
 * Created by user on 10.03.2017.
 */
public class GridLayer extends Layer {

    public static final String TITLE = "GridLayer";

    private ShapeRenderer shapeRenderer;

    public GridLayer() {
        super(TITLE);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw() {
        shapeRenderer.setProjectionMatrix(ViewHelper.camera.projection);
        shapeRenderer.setTransformMatrix(ViewHelper.camera.view);
        shapeRenderer.setColor(Color.SLATE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < GameHelper.MAX_WIDTH; x++) {
            for (int y = 0; y < GameHelper.MAX_HEIGHT; y++) {
                shapeRenderer.rect(ViewHelper.toPx(x), ViewHelper.toPx(y),
                        ViewHelper.CELL_VIEW_SIZE, ViewHelper.CELL_VIEW_SIZE);
            }
        }
        shapeRenderer.end();
    }

}
