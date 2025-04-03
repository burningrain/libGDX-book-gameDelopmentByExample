package com.github.br.ecs.simple.system.render;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.ecs.simple.utils.ViewHelper;

/**
 * Created by user on 06.04.2019.
 */
public class ShaderSubsystem {

    private final Array<SpriteBatch> batches = new Array<SpriteBatch>();
    private final Array<ShaderUpdater> shaderUpdaters = new Array<ShaderUpdater>();

    private final SpriteBatch DEFAULT_SHADER_BATCH = new SpriteBatch();

    private final Viewport viewport;
    private final AssetManager assetManager;

    public ShaderSubsystem(AssetManager assetManager, Viewport viewport, LayerData[] layers) {
        this.viewport = viewport;
        this.assetManager = assetManager;

        ObjectMap<String, SpriteBatch> batchMap = new ObjectMap<String, SpriteBatch>();

        String lastShader = null;
        SpriteBatch currentBatch = null;
        ShaderProgram currentShader = null;
        ShaderUpdater shaderUpdater = null;
        for (LayerData layerData : layers) {
            if (layerData.shaderData == null) {
                lastShader = null;
                currentBatch = DEFAULT_SHADER_BATCH;
            } else {
                ShaderData shaderData = layerData.shaderData;
                if (!shaderData.title.equals(lastShader)) {
                    lastShader = shaderData.title;
                    SpriteBatch spriteBatch = batchMap.get(lastShader);
                    if (spriteBatch == null) {
                        currentShader = getShaderProgram(shaderData);
                        if (!currentShader.isCompiled())
                            throw new IllegalArgumentException("Error compiling shader: " + currentShader.getLog());
                        shaderUpdater = shaderData.shaderUpdater;
                        spriteBatch = new SpriteBatch(1000, currentShader); //todo вот size хорошо бы высчитать
                    }
                    currentBatch = spriteBatch;
                }
            }

            batches.add(currentBatch);
            shaderUpdaters.add(shaderUpdater);
        }
    }

    private ShaderProgram getShaderProgram(ShaderData shaderData) {
        return assetManager.<ShaderProgram>get(shaderData.title);
    }

    public void update(BatchListener batchListener) {
        viewport.apply();

        // оптимизация для уменьшения draw calls при дефолтном шейдере
        SpriteBatch lastBatch = null;
        int size = batches.size;
        for (int i = 0; i < size; i++) {
            SpriteBatch batch = batches.get(i);
            if (batch != lastBatch) {
                // не рисовать, если нету на этом слое сущностей
                if(!batchListener.isNeedPaintLayer(i)) {
                    continue;
                }

                if (lastBatch != null) {
                    lastBatch.end();
                }
                lastBatch = batch;
                ViewHelper.applyCameraAndViewPort(lastBatch, viewport);
                lastBatch.begin();
                if (shaderUpdaters.get(i) != null) {
                    shaderUpdaters.get(i).update(batch.getShader());
                }
            }
            batchListener.update(i, lastBatch);
        }
        if (lastBatch != null) {
            lastBatch.end();
        }
    }

    public void resize(int width, int height) {
        viewport.apply();
        viewport.update(width, height);
    }

    public interface BatchListener {
        void update(int layerNumber, SpriteBatch batch);

        boolean isNeedPaintLayer(int layerNumber);
    }

}
