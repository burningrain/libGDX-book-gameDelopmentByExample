package com.github.br.paper.airplane.screen.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends ScreenAdapter {

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;

    private final float worldWidth;
    private final float worldHeight;

    private final ProgressBar progressBar;

    public LoadingScreen(
            ProgressBar progressBar,
            float worldWidth,
            float worldHeight
    ) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.progressBar = progressBar;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(worldWidth / 2, worldHeight / 2, 0);
        camera.update();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    public void updateProgress(float progress) {
        progressBar.updateProgress(progress);
    }

    public void reset() {
        progressBar.reset();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        progressBar.draw(shapeRenderer);

        shapeRenderer.end();
    }

}
