package com.github.br.ecs.simple.utils;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class ViewHelper {

    public static float WORLD_WIDTH;
    public static float WORLD_HEIGHT;

    public static Viewport viewport;
    public static Camera camera;

    private ViewHelper(float worldWidth, float worldHeight){
        WORLD_WIDTH = worldWidth;
        WORLD_HEIGHT = worldHeight;

        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    }

    static {
        new ViewHelper(640, 480);
    }

    public static void applyCameraAndViewPort(Batch batch){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
    }

    public static void applyCameraAndViewPort(ShapeRenderer shapeRenderer){
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
    }

}
