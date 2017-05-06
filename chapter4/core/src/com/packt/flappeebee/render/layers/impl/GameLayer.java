package com.packt.flappeebee.render.layers.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.packt.flappeebee.render.layers.Layer;
import com.github.br.ecs.simple.utils.ViewHelper;


public class GameLayer extends Layer {

    public static final String GAME_LAYER = "GAME_LAYER";

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch batch = new SpriteBatch();

    public GameLayer() {
        super(GAME_LAYER);
    }

    @Override
    public void draw() {
        ViewHelper.applyCameraAndViewPort(shapeRenderer);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.end();
    }

    private void drawShape(){

    }

    private void drawBatch(){
        ViewHelper.applyCameraAndViewPort(batch);
        batch.begin();
        batch.end();
    }
}
