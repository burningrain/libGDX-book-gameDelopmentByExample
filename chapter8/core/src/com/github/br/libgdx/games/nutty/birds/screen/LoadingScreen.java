package com.github.br.libgdx.games.nutty.birds.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.libgdx.games.nutty.birds.Constants;
import com.github.br.libgdx.games.nutty.birds.NuttyBirdsGame;


public class LoadingScreen extends ScreenAdapter {

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private float progress = 0;
    private final NuttyBirdsGame nuttyGame;

    public LoadingScreen(NuttyBirdsGame peteGame) {
        this.nuttyGame = peteGame;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        load();
    }

    private void load() {
        nuttyGame.getAssetManager().load(Constants.NUTTYBIRDS_TMX, TiledMap.class);
        nuttyGame.getAssetManager().load(Constants.OBSTACLE_VERTICAL_PNG, Texture.class);
        nuttyGame.getAssetManager().load(Constants.OBSTACLE_HORIZONTAL_PNG, Texture.class);
        nuttyGame.getAssetManager().load(Constants.BIRD_PNG, Texture.class);
        nuttyGame.getAssetManager().load(Constants.SLINGSHOT_PNG, Texture.class);
        nuttyGame.getAssetManager().load(Constants.SQUIRREL_PNG, Texture.class);
        nuttyGame.getAssetManager().load(Constants.ACORN_PNG, Texture.class);
    }

    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void update() {
        if (nuttyGame.getAssetManager().update()) {
            nuttyGame.setScreen(new GameScreen(nuttyGame));
        } else {
            progress = nuttyGame.getAssetManager().getProgress();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (Constants.WORLD_WIDTH - Constants.PROGRESS_BAR_WIDTH) / 2,
                Constants.WORLD_HEIGHT / 2 - Constants.PROGRESS_BAR_HEIGHT / 2,
                progress * Constants.PROGRESS_BAR_WIDTH, Constants.PROGRESS_BAR_HEIGHT
        );

        shapeRenderer.end();
    }

}
