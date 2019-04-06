package com.github.br.ecs.simple.system.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.ecs.simple.utils.ViewHelper;

/**
 * Created by user on 06.04.2019.
 */
public class ShaderSubsystem {

    private Array<SpriteBatch> batches = new Array<SpriteBatch>();
    private Array<ShaderUpdater> shaderUpdaters = new Array<ShaderUpdater>();

    public ShaderSubsystem(LayerData[] layers) {
        ObjectMap<String, SpriteBatch> batchMap = new ObjectMap<String, SpriteBatch>();
        SpriteBatch batchWithoutShaders = new SpriteBatch();
        String lastShader = null;
        SpriteBatch currentBatch = null;
        ShaderProgram currentShader = null;
        ShaderUpdater shaderUpdater = null;
        for (LayerData layerData : layers) {
            if (layerData.shaderData != null) {
                ShaderData shaderData = layerData.shaderData;
                if (!shaderData.title.equals(lastShader)) {
                    lastShader = shaderData.title;
                    SpriteBatch spriteBatch = batchMap.get(lastShader);
                    if(spriteBatch == null) {
                        currentShader = new ShaderProgram(shaderData.vertexShader, shaderData.fragmentShader);
                        if (!currentShader.isCompiled())
                            throw new IllegalArgumentException("Error compiling shader: " + currentShader.getLog());
                        shaderUpdater = shaderData.shaderUpdater;
                        spriteBatch = new SpriteBatch(1000, currentShader);
                    }
                    currentBatch = spriteBatch;
                }
            } else {
                lastShader = null;
                currentBatch = batchWithoutShaders;
            }

            batches.add(currentBatch);
            shaderUpdaters.add(shaderUpdater);
        }
    }

    public void update(BatchListener batchListener) {
        SpriteBatch lastBatch = null;
        int size = batches.size;
        for (int i = 0; i < size; i++) {
            SpriteBatch batch = batches.get(i);
            if(batch != lastBatch) {
                if(lastBatch != null) lastBatch.end();
                lastBatch = batch;
                ViewHelper.applyCameraAndViewPort(lastBatch);
                lastBatch.begin();
                if(shaderUpdaters.get(i) != null) shaderUpdaters.get(i).update(batch.getShader());
            }
            batchListener.update(i, lastBatch);
        }
        lastBatch.end();
    }

    public interface BatchListener {
        void update(int layerNumber, SpriteBatch batch);
    }

}
