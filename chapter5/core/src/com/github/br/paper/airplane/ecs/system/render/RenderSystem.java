package com.github.br.paper.airplane.ecs.system.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.Mappers;
import com.github.br.paper.airplane.ecs.component.RenderComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class RenderSystem extends EntitySystem {

    private final Family family = Family.all(TransformComponent.class, RenderComponent.class).get();
    private final Mappers mappers;
    private final SpriteBatch[] spriteBatchArray;
    private final ShaderUpdater[] shaderUpdaters;

    private final Viewport viewport;
    private final OrthographicCamera camera;

    private final Runnable postDrawCallback;

    private final GameSettings gameSettings;

    private final Utils utils;

    private final Vector2 resolution = new Vector2();

    public RenderSystem(int layersSize, Utils utils, Mappers mappers, GameSettings gameSettings, Runnable postDrawCallback) {
        this.spriteBatchArray = createSpriteBatchArray(layersSize);
        this.shaderUpdaters = new ShaderUpdater[layersSize];

        this.mappers = mappers;
        this.gameSettings = gameSettings;
        this.postDrawCallback = postDrawCallback;
        this.utils = utils;

        camera = new OrthographicCamera();
        camera.update();
        viewport = new FitViewport(gameSettings.getVirtualScreenWidth(), gameSettings.getVirtualScreenHeight(), camera);
        viewport.apply(true);
    }

    private SpriteBatch[] createSpriteBatchArray(int layersSize) {
        SpriteBatch[] result = new SpriteBatch[layersSize];
        for (int i = 0; i < layersSize; i++) {
            result[i] = new SpriteBatch();
        }
        return result;
    }

    public void setShader(byte layer, ShaderProgram shaderProgram, ShaderUpdater shaderUpdater) {
        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());
        }
        this.shaderUpdaters[layer] = shaderUpdater;
        this.spriteBatchArray[layer].setShader(shaderProgram);
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);

        int length = spriteBatchArray.length;
        Array<Entity>[] ents = new Array[length];
        for (int i = 0; i < length; i++) {
            ents[i] = new Array<>();
        }
        for (Entity entity : entities) {
            RenderComponent renderComponent = mappers.renderMapper.get(entity);
            ents[renderComponent.layer].add(entity);
        }

        for (int i = 0; i < length; i++) {
            SpriteBatch spriteBatch = this.spriteBatchArray[i];
            ShaderUpdater shaderUpdater = shaderUpdaters[i];
            drawEntities(spriteBatch, shaderUpdater, ents[i], deltaTime);
        }

        postDrawCallback.run();
    }

    private void drawEntities(SpriteBatch spriteBatch, ShaderUpdater shaderUpdater, Array<Entity> entities, float deltaTime) {
        applyCameraToBatch(spriteBatch, camera);

        spriteBatch.begin();
        if (shaderUpdater != null) {
            shaderUpdater.update(deltaTime, resolution, spriteBatch.getShader());
        }
        for (Entity entity : entities) {
            TransformComponent transformComponent = mappers.transformMapper.get(entity);
            RenderComponent renderComponent = mappers.renderMapper.get(entity);

            Vector2 anchor = renderComponent.anchorDelta;
            ParticleEffect particleEffect = renderComponent.particleEffect;
            Vector2 newPosition = utils.rotatePointToAngle(anchor.x, anchor.y, transformComponent.degreeAngle);
            if (particleEffect != null) {
                particleEffect.setPosition(
                        transformComponent.position.x + transformComponent.width / 2 + newPosition.x,
                        transformComponent.position.y + transformComponent.height / 2 + newPosition.y
                );
                rotateBy(particleEffect, transformComponent.degreeAngle - 180); //TODO FIXME ?!
                particleEffect.draw(spriteBatch, deltaTime);
            } else {
                TextureRegion region = renderComponent.region;
                spriteBatch.draw(
                        region,
                        transformComponent.position.x + newPosition.x,
                        transformComponent.position.y + newPosition.y,
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
        this.resolution.set(width, height);
        viewport.update(width, height);
    }

    public void hide() {
    }

    public void dispose() {
        for (SpriteBatch spriteBatch : spriteBatchArray) {
            spriteBatch.dispose();
        }
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
