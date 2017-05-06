package com.packt.snake.render.utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.snake.model.utils.Vector;

/**
 * Created by user on 28.02.2017.
 */
public final class ViewHelper {

    public static final float WORLD_WIDTH = 640 + 1;  // не хватает пикселя для отрисовки грани(
    public static final float WORLD_HEIGHT = 480 + 1; // не хватает пикселя для отрисовки грани(

    public static Viewport viewport;
    public static Camera camera;

    static {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    }

    public static final int CELL_VIEW_SIZE = 32;

    public static Vector toPx(Vector position) {
        return new Vector(position.x * CELL_VIEW_SIZE, position.y * CELL_VIEW_SIZE);
    }

    public static int toPx(int x) {
        return x * CELL_VIEW_SIZE;
    }

    private ViewHelper(){}



}
