package com.github.br.ecs.simple.system.render;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.br.ecs.simple.engine.IDebugSystem;
import com.github.br.ecs.simple.engine.debug.DebugDataContainer;
import com.github.br.ecs.simple.engine.debug.data.TableData;

import static java.lang.String.format;

/**
 * Created by user on 02.04.2017.
 */
public class RenderSystem implements IDebugSystem<RendererNode> {

    private Array<Layer> layersList = new Array<Layer>();
    private OrderedMap<String, Layer> layersMap = new OrderedMap<String, Layer>();
    private IntMap<String> entityLayerMap = new IntMap<String>();

    private boolean debugMode;
    private DebugDataContainer debugDataContainer;
    private long executionTime;
    private int nodesAmount;

    private ShaderSubsystem shaderSubsystem;

    public RenderSystem(LayerData[] layers) {
        shaderSubsystem = new ShaderSubsystem(layers);
        for (LayerData layerData : layers) {
            addLayer(layerData.title);
        }
        debugDataContainer = new DebugDataContainer(); //todo см. DebugSystem
    }

    @Override
    public void addNode(RendererNode node) {
        String layerTitle = node.renderer.layer;
        Layer layer = layersMap.get(layerTitle);
        if (layer == null) {
            throw new IllegalArgumentException(format("Слой '%s' не найден", layerTitle));
        }

        layer.addNode(node);
        entityLayerMap.put(node.entityId, layerTitle);
    }

    @Override
    public void removeNode(int entityId) {
        String layerTitle = entityLayerMap.get(entityId);
        layersMap.get(layerTitle).removeNode(entityId);
    }

    @Override
    public Class<RendererNode> getNodeClass() {
        return RendererNode.class;
    }

    @Override
    public boolean isDebugMode() {
        return debugMode;
    }

    @Override
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public void update(float delta) {
        long before = 0;
        TableData.Builder builder = null;
        if (isDebugMode()) {
            debugDataContainer.clear(); // очищаем предыдущее состояние
            before = System.nanoTime();
            nodesAmount = 0;
            builder = new TableData.Builder();
        }
        final TableData.Builder finalBuilder = builder;

        shaderSubsystem.update(new ShaderSubsystem.BatchListener() {
            @Override
            public void update(int layerNumber, SpriteBatch batch) {
                Layer layer = layersList.get(layerNumber);
                layer.render(batch);
                if (isDebugMode()) {
                    nodesAmount += layer.getNodesAmount();
                    finalBuilder.put(layer.getTitle(), "");
                }
            }
        });

        if (isDebugMode()) {
            executionTime = System.nanoTime() - before;
            debugDataContainer.put(builder.build());
        }
    }

    @Override
    public DebugDataContainer getDebugData() {
        return debugDataContainer;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public int getNodesAmount() {
        return nodesAmount;
    }

    private void addLayer(String title) {
        Layer layer = new Layer(title);
        layersList.add(layer);
        layersMap.put(title, layer);
    }

}
