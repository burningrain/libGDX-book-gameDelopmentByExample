package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
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

    private final Runnable postDrawCallback;

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
                rotateBy(particleEffect, transformComponent.degreeAngle - 180); //TODO FIXME ?!
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
                        transformComponent.degreeAngle
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

    private void rotateBy(ParticleEffect particleEffect, float targetAngle) {
        Array<ParticleEmitter> emitters = particleEffect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            /* find angle property and adjust that by letting the min, max of low and high span their current size around your angle */
            ParticleEmitter particleEmitter = emitters.get(i);
            ParticleEmitter.ScaledNumericValue angle = particleEmitter.getAngle();

            float angleHighMin = angle.getHighMin();
            float angleHighMax = angle.getHighMax();
            float spanHigh = angleHighMax - angleHighMin;
            angle.setHigh(targetAngle, targetAngle);

            float angleLowMin = angle.getLowMin();
            float angleLowMax = angle.getLowMax();
            float spanLow = angleLowMax - angleLowMin;
            angle.setLow(0);
        }
    }
}
