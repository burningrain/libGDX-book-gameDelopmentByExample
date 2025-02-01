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
import com.github.br.paper.airplane.ecs.component.render.ParticleEffectData;
import com.github.br.paper.airplane.ecs.component.render.RenderComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;
import com.github.br.paper.airplane.ecs.component.render.TextureData;

public class RenderSystem extends EntitySystem {

    private final RenderTextureSubsystem textureSubsystem;
    private final RenderParticleSubsystem particleSubsystem;

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

        particleSubsystem = new RenderParticleSubsystem(this.utils);
        textureSubsystem = new RenderTextureSubsystem(this.utils);
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

    private static class EntityRenderData {
        public Entity entity;
        public RenderData renderData;

        public EntityRenderData(Entity entity, RenderData renderData) {
            this.entity = entity;
            this.renderData = renderData;
        }
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);

        // раскладываем сущности по слоям
        int length = spriteBatchArray.length;
        Array<EntityRenderData>[] ents = new Array[length];
        for (int i = 0; i < length; i++) {
            ents[i] = new Array<>();
        }
        for (Entity entity : entities) {
            RenderComponent renderComponent = mappers.renderMapper.get(entity);
            // textures
            TextureData[] textureData = renderComponent.textureData;
            if (textureData != null) {
                for (TextureData textureDatum : textureData) {
                    if (textureDatum != null) {
                        ents[textureDatum.renderPosition.layer].add(new EntityRenderData(entity, textureDatum));
                    }
                }
            }
            // particles
            ParticleEffectData[] effectData = renderComponent.effectData;
            if (effectData != null) {
                for (ParticleEffectData effectDatum : effectData) {
                    if (effectDatum != null) {
                        ents[effectDatum.renderPosition.layer].add(new EntityRenderData(entity, effectDatum));
                    }
                }
            }
        }

        // рисуем по слоям
        for (int i = 0; i < length; i++) {
            SpriteBatch spriteBatch = this.spriteBatchArray[i];
            ShaderUpdater shaderUpdater = shaderUpdaters[i];
            drawEntities(spriteBatch, shaderUpdater, ents[i], deltaTime);
        }

        // костыль для отрисовки дебага box2d
        postDrawCallback.run();
    }

    private void drawEntities(
            SpriteBatch spriteBatch,
            ShaderUpdater shaderUpdater,
            Array<EntityRenderData> renderData,
            float deltaTime
    ) {
        applyCameraToBatch(spriteBatch, camera);

        spriteBatch.begin();
        if (shaderUpdater != null) {
            shaderUpdater.update(deltaTime, resolution, spriteBatch.getShader());
        }

        for (EntityRenderData renderDatum : renderData) {
            TransformComponent transformComponent = mappers.transformMapper.get(renderDatum.entity);

            if (renderDatum.renderData instanceof TextureData) {
                TextureData textureData = (TextureData) renderDatum.renderData;
                textureSubsystem.render(transformComponent, spriteBatch, deltaTime, textureData);
            } else if (renderDatum.renderData instanceof ParticleEffectData) {
                ParticleEffectData effectData = (ParticleEffectData) renderDatum.renderData;
                particleSubsystem.render(transformComponent, spriteBatch, deltaTime, effectData);
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

}
