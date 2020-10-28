package com.github.br.ecs.simple.utils;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class ViewHelper {

    public static void applyCameraAndViewPort(Batch batch, Viewport viewport) {
        Camera camera = viewport.getCamera();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
    }

    public static void applyCameraAndViewPort(ShapeRenderer shapeRenderer, Viewport viewport) {
        Camera camera = viewport.getCamera();
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
    }

}
