package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.ecs.component.TextureComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class RenderSystem extends EntitySystem {

    private final Family family = Family.all(TransformComponent.class, TextureComponent.class).get();
    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);

    private final SpriteBatch spriteBatch = new SpriteBatch();

    private Viewport viewport;
    private OrthographicCamera camera;

    private Runnable runnable;

    private final GameSettings gameSettings;

    public RenderSystem(GameSettings gameSettings, Runnable runnable) {
        this.gameSettings = gameSettings;
        this.runnable = runnable;

        camera = new OrthographicCamera();
        camera.update();
        viewport = new FitViewport(gameSettings.getVirtualScreenWidth(), gameSettings.getVirtualScreenHeight(), camera);
        viewport.apply(true);
    }

    @Override
    public void update(float deltaTime) {
        clearScreen();
        drawEntities();
        runnable.run();
    }

    private void drawEntities() {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);
        applyCameraToBatch(spriteBatch, camera);
        spriteBatch.begin();
        for (Entity entity : entities) {
            TransformComponent transformComponent = transformMapper.get(entity);
            TextureComponent textureComponent = textureMapper.get(entity);

            spriteBatch.draw(
                    textureComponent.region,
                    transformComponent.position.x,
                    transformComponent.position.y,
                    transformComponent.origin.x,
                    transformComponent.origin.y,
                    textureComponent.region.getRegionWidth(),
                    textureComponent.region.getRegionHeight(),
                    transformComponent.scale.x,
                    transformComponent.scale.y,
                    transformComponent.angle
            );
        }
        spriteBatch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void hide() {

    }

    public void dispose() {
        spriteBatch.dispose();
    }

    public void applyCameraToBatch(Batch batch, Camera camera) {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
