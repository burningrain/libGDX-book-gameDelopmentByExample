package com.github.br.paper.airplane.ecs.system;

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
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.ecs.component.Mappers;
import com.github.br.paper.airplane.ecs.component.RenderComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class RenderSystem extends EntitySystem {

    private final Family family = Family.all(TransformComponent.class, RenderComponent.class).get();
    private final Mappers mappers;
    private final SpriteBatch spriteBatch = new SpriteBatch();

    private final Viewport viewport;
    private final OrthographicCamera camera;

    private Runnable postDrawCallback;

    private final GameSettings gameSettings;

    public RenderSystem(Mappers mappers, GameSettings gameSettings, Runnable postDrawCallback) {
        this.mappers = mappers;
        this.gameSettings = gameSettings;
        this.postDrawCallback = postDrawCallback;

        camera = new OrthographicCamera();
        camera.update();
        viewport = new FitViewport(gameSettings.getVirtualScreenWidth(), gameSettings.getVirtualScreenHeight(), camera);
        viewport.apply(true);
    }

    @Override
    public void update(float deltaTime) {
        clearScreen();
        drawEntities(deltaTime);
        postDrawCallback.run();
    }

    private void drawEntities(float deltaTime) {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);
        applyCameraToBatch(spriteBatch, camera);
        spriteBatch.begin();
        for (Entity entity : entities) {
            TransformComponent transformComponent = mappers.transformMapper.get(entity);
            RenderComponent renderComponent = mappers.renderMapper.get(entity);

            ParticleEffect particleEffect = renderComponent.particleEffect;
            if (particleEffect != null) {
                particleEffect.setPosition(
                        transformComponent.position.x + transformComponent.width / 2,
                        transformComponent.position.y + transformComponent.height / 2
                );
                particleEffect.draw(spriteBatch, deltaTime);
            } else {
                TextureRegion region = renderComponent.region;
                spriteBatch.draw(
                        region,
                        transformComponent.position.x,
                        transformComponent.position.y,
                        transformComponent.origin.x,
                        transformComponent.origin.y,
                        region.getRegionWidth(),
                        region.getRegionHeight(),
                        transformComponent.scale.x,
                        transformComponent.scale.y,
                        transformComponent.angle
                );
            }
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
